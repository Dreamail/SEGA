/**
 * Copyright (C) 2013 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.nsds.biz.common.jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * クエリ実行を簡略化するヘルパークラス。
 * 
 * @author TsuboiY
 * 
 */
public class QueryHelper {

    private static final Logger _log = LoggerFactory
            .getLogger(QueryHelper.class);

    private final Connection _conn;

    private final String _query;

    private final boolean oneTime;

    private PreparedStatement _stmt;

    /**
     * ヘルパーにコネクション、クエリを設定する。
     * 
     * @param conn
     * @param query
     */
    public QueryHelper(Connection conn, String query) {
        this(conn, query, true);
    }

    /**
     * ヘルパーにコネクション、クエリを設定する。oneTimeがfalseならステートメントの自動クローズは行われず、実行したステートメントは保持される
     * 。
     * 
     * @param conn
     * @param query
     * @param oneTime
     */
    public QueryHelper(Connection conn, String query, boolean oneTime) {
        this._conn = conn;
        this._query = query;
        this.oneTime = oneTime;
    }

    /**
     * DMLステートメントを実行する。
     * 
     * @param params
     * @return
     * @throws SQLException
     */
    public int execute(Object... params) throws QueryHelperException {
        try {
            if (_stmt == null) {
                _stmt = _conn.prepareStatement(_query);
            } else {
                _stmt.clearParameters();
            }
            try {
                for (int i = 0; i < params.length; i++) {
                    _stmt.setObject(i + 1, params[i]);
                }
                Date start = new Date();
                try {
                    return _stmt.executeUpdate();
                } finally {
                    logging(_query, params, start);
                }
            } finally {
                if (oneTime) {
                    _stmt.close();
                    _stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new QueryHelperException(e);
        }
    }

    /**
     * SELECTステートメントを実行し、受け渡されたハンドラの処理結果を返却する。
     * 
     * @param handler
     * @param params
     * @return
     * @throws SQLException
     */
    public <T> T execute(QueryHelperHandler<T> handler, Object... params)
            throws QueryHelperException {
        try {
            if (_stmt == null) {
                _stmt = _conn.prepareStatement(_query);
            } else {
                _stmt.clearParameters();
            }
            try {
                for (int i = 0; i < params.length; i++) {
                    _stmt.setObject(i + 1, params[i]);
                }
                Date start = new Date();
                ResultSet rs = _stmt.executeQuery();
                try {
                    return handler.handle(rs);
                } finally {
                    logging(_query, params, start);
                    rs.close();
                }
            } finally {
                if (oneTime) {
                    _stmt.close();
                    _stmt = null;
                }
            }
        } catch (QueryHelperException e) {
            throw e;
        } catch (Exception e) {
            throw new QueryHelperException(e);
        }
    }

    /**
     * ステートメントをクローズする。
     * 
     * @throws QueryHelperException
     */
    public void close() throws QueryHelperException {
        if (oneTime) {
            _log.warn("This instance is set to use one time. It is needless to call close().");
            return;
        }
        try {
            if (_stmt == null) {
                _log.warn("The statement has already closed. This call is no effect.");
                return;
            }
            _stmt.close();
            _stmt = null;
        } catch (SQLException e) {
            throw new QueryHelperException(e);
        }
    }

    // ------------------------------------------------------ Protected methods

    /**
     * クエリのログを出力する。
     * 
     * @param query
     * @param params
     */
    protected void logging(String query, Object[] params, Date start) {
        if (!_log.isDebugEnabled()) {
            return;
        }
        long time = new Date().getTime() - start.getTime();
        if (params != null) {
            StringBuilder sb = new StringBuilder();
            for (Object o : params) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                if (o instanceof String) {
                    sb.append("'");
                }
                sb.append(o);
                if (o instanceof String) {
                    sb.append("'");
                }
            }
            _log.debug("{}ms: {} [{}]",
                    new Object[] { time, query, sb.toString() });
        } else {
            _log.debug("{}ms: {}", new Object[] { time, query });
        }
    }

    // -------------------------------------------------------------- Accessors

    public boolean isOneTime() {
        return oneTime;
    }

    // ----------------------------------------------- Inner Interfaces/Classes

    /**
     * クエリの実行結果を処理するためのハンドラーインターフェース。
     * 
     * @author TsuboiY
     * 
     * @param <T>
     */
    public interface QueryHelperHandler<T> {

        /**
         * クエリの実行結果を受け取って処理を行う。
         * 
         * @param rs
         * @param basket
         * @return
         * @throws Exception
         */
        T handle(ResultSet rs) throws Exception;

    }

}
