/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.log.AdminLogUtils;
import jp.co.sega.allnet.auth.log.LogUtils;
import jp.co.sega.allnet.auth.service.security.AuthenticationDelegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TsuboiY
 * 
 */
public abstract class AbstractService {

    private static final Logger _log = LoggerFactory
            .getLogger(AbstractService.class);

    @PersistenceContext
    protected EntityManager _em;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authDelegate;

    @Resource(name = "logUtils")
    private LogUtils logUtils;

    /**
     * 認証情報からユーザIDを取得します。
     * 
     * @return
     */
    protected String getCurrentUser() {
        return _authDelegate.getCurrentUser();
    }

    /**
     * 参照の基準となる日時を作成する。
     * 
     * @param day
     * @return
     */
    protected Date createLimitDate(int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, day * -1);
        return c.getTime();
    }

    /**
     * {@link LogUtils} のインスタンスを取得する。
     * 
     * @return the logUtils
     */
    protected LogUtils getLogUtils() {
        return logUtils;
    }

    /**
     * {@link AdminLogUtils}#{@link format(Object, Object...)} を代行する。
     * 
     * @param log
     * @param args
     * @return
     */
    protected String formatLog(Object log, Object... args) {
        return logUtils.format(log, args);
    }

    /**
     * クエリによる一括DELETEのログを出力する。
     * 
     * @param target
     * @param keyTemplate
     * @param args
     * @return
     */
    protected String formatBulkDeleteLog(String target, String keyTemplate,
            Object... args) {
        return formatLog("Bulk deleted: %s [%s]", target,
                String.format(keyTemplate, args));
    }

    /**
     * {@link EntityManager}#{@link persist(Object)} を代行する。
     * 
     * @param entity
     */
    protected void persist(Object entity) {
        persist(entity, false, true);
    }

    /**
     * {@link EntityManager}#{@link persist(Object)} を代行する。
     * 
     * @param entity
     * @param flush
     */
    protected void persist(Object entity, boolean flush) {
        persist(entity, flush, true);
    }

    /**
     * {@link EntityManager}#{@link persist(Object)} を代行する。
     * 
     * @param entity
     * @param flush
     * @param logging
     */
    protected void persist(Object entity, boolean flush, boolean logging) {
        _em.persist(entity);
        if (flush) {
            _em.flush();
        }
        if (logging) {
            _log.info(logUtils.format("Entity persisted: %s", entity));
        }
    }

    /**
     * {@link EntityManager}#{@link merge(T)} を代行する。
     * 
     * @param entity
     * @return
     */
    protected <T> T merge(T entity) {
        return merge(entity, false, true);
    }

    /**
     * {@link EntityManager}#{@link merge(T)} を代行する。
     * 
     * @param entity
     * @param flush
     * @return
     */
    protected <T> T merge(T entity, boolean flush) {
        return merge(entity, flush, true);
    }

    /**
     * {@link EntityManager}#{@link merge(T)} を代行する。
     * 
     * @param entity
     * @param flush
     * @param logging
     * @return
     */
    protected <T> T merge(T entity, boolean flush, boolean logging) {
        T merged = _em.merge(entity);
        if (flush) {
            _em.flush();
        }
        if (logging) {
            _log.info(logUtils.format("Entity merged: %s", entity));
        }
        return merged;
    }

    /**
     * {@link EntityManager}#{@link remove(Object)} を代行する。
     * 
     * @param entity
     */
    protected void remove(Object entity) {
        remove(entity, false, true);
    }

    /**
     * {@link EntityManager}#{@link remove(Object)} を代行する。
     * 
     * @param entity
     * @param flush
     */
    protected void remove(Object entity, boolean flush) {
        remove(entity, flush, true);
    }

    /**
     * {@link EntityManager}#{@link remove(Object)} を代行する。
     * 
     * @param entity
     * @param flush
     * @param logging
     */
    protected void remove(Object entity, boolean flush, boolean logging) {
        _em.remove(entity);
        if (flush) {
            _em.flush();
        }
        if (logging) {
            _log.info(logUtils.format("Entity removed: %s", entity));
        }
    }
}
