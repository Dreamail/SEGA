/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.log.ApiLogUtils;

/**
 * @author TsuboiY
 * 
 */
public abstract class AbstractApiService {

    @Resource(name = "apiLogUtils")
    private ApiLogUtils _logUtils;

    protected String formatLog(String msg, Object... args) {
        return _logUtils.format(msg, args);
    }

}
