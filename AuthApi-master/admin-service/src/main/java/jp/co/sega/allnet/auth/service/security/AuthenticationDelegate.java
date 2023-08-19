/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.security;

/**
 * @author TsuboiY
 * 
 */
public interface AuthenticationDelegate {

    boolean isAuthenticated();

    String getCurrentUser();

}
