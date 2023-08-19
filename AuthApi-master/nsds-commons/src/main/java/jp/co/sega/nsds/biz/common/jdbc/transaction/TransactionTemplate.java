/**
 * Copyright (C) 2013 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.nsds.biz.common.jdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * @author TsuboiY
 * 
 */
public abstract class TransactionTemplate<T> {

    // --------------------------------------------------------- Public Methods

    /**
     * readOnly=false,autoCommit=falseでトランザクション処理を実行する。
     * 
     * @param dataSource
     * @return
     */
    public T executeTransaction(DataSource dataSource) throws Exception {
        return executeTransaction(dataSource, TransactionConfig.NONE);
    }

    /**
     * トランザクション処理を実行する。
     * 
     * @param dataSource
     * @param config
     * @return
     */
    public T executeTransaction(DataSource dataSource, TransactionConfig config)
            throws Exception {
        Connection conn = dataSource.getConnection();
        try {
            return executeTransaction(conn, config);
        } finally {
            conn.close();
        }
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * トランザクション中の処理を定義する。
     * 
     * @param conn
     * @return
     * @throws Exception
     */
    protected abstract T execute(Connection conn) throws Exception;

    // -------------------------------------------------------- Private Methods

    /**
     * DBコネクションを使用してトランザクション処理を実行する。
     * 
     * @param conn
     * @param config
     * @return
     * @throws SQLException
     */
    private T executeTransaction(Connection conn, TransactionConfig config)
            throws Exception {
        switch (config) {
        case READ_ONLY:
            conn.setReadOnly(true);
            break;
        case AUTO_COMMIT:
            conn.setReadOnly(false);
            conn.setAutoCommit(true);
            break;
        case NONE:
            conn.setReadOnly(false);
            conn.setAutoCommit(false);
            break;
        }
        try {
            T t = execute(conn);
            commit(conn, config);
            return t;
        } catch (RuntimeException e) {
            rollback(conn, config);
            throw e;
        } catch (Exception e) {
            rollback(conn, config);
            throw e;
        }
    }

    /**
     * トランザクションをコミットする。
     * 
     * @param conn
     * @param config
     * @throws SQLException
     */
    private void commit(Connection conn, TransactionConfig config)
            throws SQLException {
        switch (config) {
        case NONE:
            conn.commit();
        default:
            return;
        }
    }

    /**
     * トランザクションをロールバックする。
     * 
     * @param conn
     * @param config
     * @throws SQLException
     */
    private void rollback(Connection conn, TransactionConfig config)
            throws SQLException {
        switch (config) {
        case NONE:
            conn.rollback();
        default:
            break;
        }
    }

    // ----------------------------------------------------- Inner Declarements

    public enum TransactionConfig {
        READ_ONLY, AUTO_COMMIT, NONE
    }

}
