/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authtype;

/**
 * @author TsuboiY
 * 
 */
public interface AuthTypeRegisterService {

    int CSV_COLUMN_SIZE = 2;

    String registerAuthType(String val);

}
