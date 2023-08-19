/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.security;

import jp.co.sega.allnet.auth.exception.ApplicationException;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author TsuboiY
 * 
 */
@Component("authenticationDelegate")
@Scope("singleton")
public class SpringSecurityDelegateBean implements AuthenticationDelegate {

    @Override
    public boolean isAuthenticated() {
        SecurityContext context = getSecurityContext();
        return context.getAuthentication() != null;
    }

    @Override
    public String getCurrentUser() {
        Authentication auth = getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        if (user == null) {
            throw new ApplicationException("This session is not authenticated.");
        }

        return user.getUsername();
    }

    /**
     * {@link SecurityContext}実装のインスタンスを取得します。
     * 
     * @return
     */
    private SecurityContext getSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            throw new ApplicationException("Cannot get security context.");
        }
        return context;
    }

    /**
     * {@link Authentication}実装のインスタンスを取得します。
     * 
     * @return
     */
    private Authentication getAuthentication() {
        SecurityContext context = getSecurityContext();
        Authentication auth = context.getAuthentication();
        if (auth == null) {
            throw new ApplicationException(
                    "Cannot get authentication instance.");
        }
        return auth;
    }
}
