/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import jp.co.sega.allnet.auth.api.domain.LoaderStateLog;

/**
 * 配信レポートに関するデータアクセスクラス。
 * 
 * @author TsuboiY
 * 
 */
public interface LoaderStateRecorderDao {

    String USER_ID = "LoaderStateRecorder";

    /**
     * 配信レポートログに当該シリアルのレコードが存在するかチェックする。
     * 
     * @param serial
     * @return
     */
    boolean checkExist(String serial);

    /**
     * 配信レポートを追加する。
     * 
     * @param loaderStateLog
     */
    void insertLoaderState(LoaderStateLog loaderStateLog);

    /**
     * 配信レポートを更新する。
     * 
     * @param loaderStateLog
     */
    void updateLoaderState(LoaderStateLog loaderStateLog);

}
