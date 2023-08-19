/**
 * Copyright (C) 2011-2013 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.nsds.biz.common.security.web.login;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.sega.nsds.biz.common.jdbc.query.QueryHelper;
import jp.co.sega.nsds.biz.common.jdbc.query.QueryHelper.QueryHelperHandler;
import jp.co.sega.nsds.biz.common.jdbc.query.QueryHelperException;
import jp.co.sega.nsds.biz.common.security.entity.LoginStatus;
import jp.co.sega.nsds.biz.common.security.exception.PropertiesNotFoundException;
import jp.co.sega.nsds.biz.common.security.exception.screen.LockedException;
import jp.co.sega.nsds.biz.common.security.exception.screen.ScreenException;

/**
 * @author TsuboiY
 * 
 */
public class LoginAttackPreventerBean implements LoginAttackPreventer {
	
    private static final Logger _log = LoggerFactory
            .getLogger(QueryHelper.class);
	
    private Properties prop;
    
    private DataSource dataSource;
    
    private int _lockCount;

    private int _lockTime;

    private int _unlockTime;
    
    private static final int INCREMENT_VALUE = 1;

    public LoginAttackPreventerBean(String path) {
    	try {
	    	this.prop = new Properties();
	    	InputStream is = getClass().getClassLoader().getResourceAsStream(
	                path);
	        if (is == null) {
	            throw new PropertiesNotFoundException("PropertyFileNotFound : " + path);
	        }
        
			this.prop.load(is);
			
			_lockCount = Integer.parseInt(prop.getProperty("lock_rated_count"));
			_lockTime = Integer.parseInt(prop.getProperty("lock_rated_minutes"));
			_unlockTime = Integer.parseInt(prop.getProperty("unlock_interval_minutes"));
			
			is.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
	public boolean checkUserId(String userId) {
    	try {
			// コネクションを作成
			Connection conn = dataSource.getConnection();
			
			try {
				String sql = "SELECT 1 as cnt FROM users WHERE user_id = ?";
		    	QueryHelper _helper = new QueryHelper(conn, sql);
		    	
		    	// クエリ実行後の処理を定義
	            QueryHelperHandler<Boolean> handler = new QueryHelperHandler<Boolean>() {
	                @Override
	                public Boolean handle(ResultSet rs) throws Exception {
	                    if (!rs.next()) {
	                        return false;
	                    }
	
	                    return true;
	                }
	            };
	            Boolean check = _helper.execute(handler, userId);
				
	            return check.booleanValue();
				
			} catch (QueryHelperException e) {
				_log.error(e.getMessage());
			} finally {
				conn.close();
			}
			
		} catch (SQLException e) {
			_log.error(e.getMessage());
		}
    	
    	return false;
	}

	@Override
    public void recordLoginAttempt(int type, String value) {

		try {
			// コネクションを作成
			Connection conn = dataSource.getConnection();
			
			try {
				// LOGIN_STATUSESにレコードが存在するか確認
				LoginStatus ls = findLoginStatus(conn, type, value);
				
				if(ls == null){
					//レコードが存在しない場合は登録
					insertLoginStatus(conn, type, value);
				} else {
					Date now = new Date();
					long diff = now.getTime() - ls.getUpdateDate().getTime();
					
					if(TimeUnit.MILLISECONDS.toMinutes(diff) >= _lockTime){
						//レコードが存在しており、ロックを施行する間隔が規定の間隔以上空いている
						ls.setCount(INCREMENT_VALUE);
					}else{
						// 上記以外はカウントアップ
						ls.setCount(INCREMENT_VALUE + ls.getCount());
					}
					updateLoginStatusFailedCount(conn, ls);
				}
				
			} catch (QueryHelperException e) {
				_log.error(e.getMessage());
			} finally {
				conn.close();
			}
			
		} catch (SQLException e) {
			_log.error(e.getMessage());
		}
    	
    }

    @Override
    public void clearLoginAttempt(int type, String value) {
    	try {
			// コネクションを作成
			Connection conn = dataSource.getConnection();
			
			try {	
		    	String sql = "DELETE FROM login_statuses WHERE type = ? AND value = ?";
		    	QueryHelper _helper = new QueryHelper(conn, sql);
				_helper.execute(type, value);
			
			} catch (QueryHelperException e) {
				_log.error(e.getMessage());
			} finally {
				conn.close();
			}
			
		} catch (SQLException e) {
			_log.error(e.getMessage());
		}
    }

    @Override
    public void screen(int type, String value) 
            throws ScreenException {
    	
    	try {
			// コネクションを作成
			Connection conn = dataSource.getConnection();
			
			try {
				// LOGIN_STATUSESにレコードが存在するか
				LoginStatus ls = findLoginStatus(conn, type, value);
				if(ls != null){
					
					//　失敗回数が規定回数を超えているか
					if(ls.getCount() >= _lockCount){
						Date now = new Date();
						long diff = now.getTime() - ls.getUpdateDate().getTime();
						
						//最終更新時刻からロック解除と判断できるまで時間が経過したか
						if(TimeUnit.MILLISECONDS.toMinutes(diff) < _unlockTime){	
							//ロックされているとみなす
							throw new LockedException();
						}
					}
				}
				
			} catch (QueryHelperException e) {
				_log.error(e.getMessage());
			} finally {
				conn.close();
			}
			
		} catch (SQLException e) {
			_log.error(e.getMessage());
		}
    	
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private LoginStatus findLoginStatus(Connection conn, int type, String value) throws QueryHelperException{

		String sql = "SELECT * FROM login_statuses WHERE type = ? AND value = ?";
    	QueryHelper _helper = new QueryHelper(conn, sql);
    	
    	// クエリ実行後の処理を定義
        QueryHelperHandler<LoginStatus> handler = new QueryHelperHandler<LoginStatus>() {
            @Override
            public LoginStatus handle(ResultSet rs) throws Exception {
                if (!rs.next()) {
                    return null;
                }
                
                LoginStatus ls = new LoginStatus();
                ls.setType(rs.getInt("type"));
                ls.setValue(rs.getString("value"));
                ls.setCount(rs.getInt("failed_count"));
                ls.setCreateDate(rs.getTimestamp("create_date"));
                ls.setCreateUserId(rs.getString("create_user_id"));
                ls.setUpdateDate(rs.getTimestamp("update_date"));
                ls.setUpdateUserId(rs.getString("update_user_id"));
                
                return ls;
            }
        };
        return _helper.execute(handler, type, value);
		
    }
    
    private void insertLoginStatus(Connection conn, int type, String value) throws QueryHelperException{

    	String sql = "INSERT INTO login_statuses VALUES (?, ?, ?, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system')";
    	QueryHelper _helper = new QueryHelper(conn, sql);
		_helper.execute(type, value, INCREMENT_VALUE);
		
    }
    
    private void updateLoginStatusFailedCount(Connection conn, LoginStatus ls) throws QueryHelperException{

    	String sql = "UPDATE login_statuses "
    			+ "SET failed_count = ?, update_date = CURRENT_TIMESTAMP, update_user_id = 'system' "
    			+ "WHERE type = ? AND value = ?";
    	QueryHelper _helper = new QueryHelper(conn, sql);
		_helper.execute(ls.getCount() , ls.getType(), ls.getValue());
		
    }

}
