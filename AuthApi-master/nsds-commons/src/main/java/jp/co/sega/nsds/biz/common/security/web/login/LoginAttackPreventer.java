/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.nsds.biz.common.security.web.login;

import jp.co.sega.nsds.biz.common.security.exception.screen.ScreenException;

/**
 * @author TsuboiY
 * 
 */
public interface LoginAttackPreventer {
	
	/**
     * ユーザが存在するか確認する。
     * 
     * @param userId
     */
    boolean checkUserId(String userId);


    /**
     * ログイン試行アクションを記録する。
     * 
     * @param type
     * @param value
     */
    void recordLoginAttempt(int type, String value);

    /**
     * 記録されたログイン試行アクションをクリアする。
     * 
     * @param type
     * @param value
     */
    void clearLoginAttempt(int type, String value);

    /**
     * ログイン認証を受ける資格があるか審査する。
     * 
     * @param type
     * @param value
     * @throws ScreenException
     */
    void screen(int type, String value) throws ScreenException;

}
