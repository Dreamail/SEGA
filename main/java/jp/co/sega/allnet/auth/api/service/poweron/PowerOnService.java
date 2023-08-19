/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.poweron;

/**
 * @author NakanoY
 * 
 */
public interface PowerOnService {

    /**
     * 基板認証処理を行う。
     * 
     * @param param
     * @return
     */
    String powerOn(PowerOnParameter param);
}
