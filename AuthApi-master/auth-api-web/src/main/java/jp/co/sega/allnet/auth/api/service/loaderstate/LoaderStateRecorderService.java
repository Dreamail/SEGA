/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.loaderstate;

import jp.co.sega.allnet.auth.api.util.InvalidParameterException;

/**
 * @author TsuboiY
 * 
 */
public interface LoaderStateRecorderService {

    String RESPONSE_STR_OK = "OK";

    String RESPONSE_STR_NG = "NG";

    /**
     * 配信レポート処理を行う。
     * 
     * @param param
     * @return
     * @throws InvalidParameterException
     */
    String loaderStateRecorder(LoaderStateRecorderParameter param)
            throws InvalidParameterException;

}
