/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.routertype;

/**
 * @author NakanoY
 * 
 */
public interface RouterTypeRegisterService {

    /**
     * ルータ種別情報の登録を行います。
     * 
     * @param val
     * @return
     */
    String registerRouterType(String val);

}
