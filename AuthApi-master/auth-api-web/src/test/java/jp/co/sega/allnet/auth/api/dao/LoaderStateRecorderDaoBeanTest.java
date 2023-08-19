/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sega.allnet.auth.api.domain.LoaderStateLog;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

/**
 * @author NakanoY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "dao-test-context.xml" })
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class LoaderStateRecorderDaoBeanTest {

    @Resource(name = "dataSource")
    private DataSource _ds;

    @Resource(name = "loaderStateRecorderDao")
    private LoaderStateRecorderDao _dao;

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.LoaderStateRecorderDaoBean#checkExist(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckExistReturnTrue() {
        String serial = "AZZZZZZZZZZ";

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM loader_state_logs WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "INSERT INTO loader_state_logs ( serial, create_user_id, update_user_id ) "
                        + "VALUES ( :serial, :userId, :userId )",
                new MapSqlParameterSource("serial", serial).addValue("userId",
                        "TestUser"));

        assertTrue(_dao.checkExist(serial));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.LoaderStateRecorderDaoBean#checkExist(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckExistReturnFalse() {
        String serial = "AZZZZZZZZZZ";

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM loader_state_logs WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));

        assertFalse(_dao.checkExist(serial));

    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.LoaderStateRecorderDaoBean#checkExist(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckExistReturnTrueCountMany() {
        String serial = "AZZZZZZZZZZ";

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        MapSqlParameterSource params = new MapSqlParameterSource("serial",
                serial);
        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM loader_state_logs WHERE serial = :serial",
                params, Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO loader_state_logs ( serial, create_user_id, update_user_id ) "
                            + "VALUES ( :serial, :userId, :userId )",
                    params.addValue("userId", "TestUser"));
        }

        assertTrue(_dao.checkExist(serial));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.LoaderStateRecorderDaoBean#checkExist(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckExistArgSerialIsNull() {
        String serial = "AZZZZZZZZZZ";

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM loader_state_logs WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "INSERT INTO loader_state_logs ( serial, create_user_id, update_user_id ) "
                        + "VALUES ( :serial, :userId, :userId )",
                new MapSqlParameterSource("serial", serial).addValue("userId",
                        "TestUser"));

        assertFalse(_dao.checkExist(null));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.LoaderStateRecorderDaoBean#insertLoaderState(jp.co.sega.allnet.auth.common.entity.LoaderStateLog)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testInsertLoaderStateSuccess() {
        LoaderStateLog log = new LoaderStateLog();
        log.setSerial("AZZZZZZZZZZ");
        log.setDvd(new BigDecimal(1));
        log.setNet(new BigDecimal(2));
        log.setWork(new BigDecimal(3));
        log.setOldNet(new BigDecimal(4));
        log.setDeliver(new BigDecimal(5));
        log.setFilesToDownload(new BigDecimal(6));
        log.setFilesDownloaded(new BigDecimal(7));
        // リクエストはunix秒で渡ってくる
        log.setLastAuth(new Timestamp(Long.parseLong("8")));
        log.setLastAuthState(new BigDecimal(9));
        log.setDownloadState(new BigDecimal(0));

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM loader_state_logs WHERE serial = :serial ",
                new MapSqlParameterSource("serial", log.getSerial()));

        _dao.insertLoaderState(log);

        Map<String, Object> map = jdbc.queryForMap(
                "SELECT * FROM loader_state_logs WHERE serial = :serial",
                new MapSqlParameterSource("serial", log.getSerial()));

        assertEquals(log.getSerial(), map.get("serial"));
        assertTrue(map.get("log_date") instanceof Timestamp);
        assertEquals(log.getDvd(), map.get("dvd"));
        assertEquals(log.getNet(), map.get("net"));
        assertEquals(log.getWork(), map.get("work"));
        assertEquals(log.getOldNet(), map.get("old_net"));
        assertEquals(log.getDeliver(), map.get("deliver"));
        assertEquals(log.getFilesToDownload(), map.get("files_to_download"));
        assertEquals(log.getFilesDownloaded(), map.get("files_downloaded"));
        assertEquals(log.getLastAuth(), map.get("last_auth"));
        assertEquals(log.getLastAuthState(), map.get("last_auth_state"));
        assertEquals(log.getDownloadState(), map.get("download_state"));
        assertTrue(map.get("receipt_date") instanceof Timestamp);
        assertEquals(LoaderStateRecorderDao.USER_ID, map.get("create_user_id"));
        assertEquals(LoaderStateRecorderDao.USER_ID, map.get("update_user_id"));

    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.LoaderStateRecorderDaoBean#insertLoaderState(jp.co.sega.allnet.auth.common.entity.LoaderStateLog)}
     * のためのテスト・メソッド。
     */
    @Test(expected = NullPointerException.class)
    public final void testInsertLoaderStateArgIsNull() {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM loader_state_logs WHERE serial = :serial ",
                new MapSqlParameterSource("serial", "AZZZZZZZZZZ"));

        _dao.insertLoaderState(null);

    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.LoaderStateRecorderDaoBean#updateLoaderState(jp.co.sega.allnet.auth.common.entity.LoaderStateLog)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testUpdateLoaderStateSuccess() {
        LoaderStateLog log = new LoaderStateLog();
        log.setSerial("AZZZZZZZZZZ");
        log.setDvd(new BigDecimal(1));
        log.setNet(new BigDecimal(2));
        log.setWork(new BigDecimal(3));
        log.setOldNet(new BigDecimal(4));
        log.setDeliver(new BigDecimal(5));
        log.setFilesToDownload(new BigDecimal(6));
        log.setFilesDownloaded(new BigDecimal(7));
        // リクエストはunix秒で渡ってくる
        log.setLastAuth(new Timestamp(Long.parseLong("8")));
        log.setLastAuthState(new BigDecimal(9));
        log.setDownloadState(new BigDecimal(10));

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM loader_state_logs WHERE serial = :serial ",
                new MapSqlParameterSource("serial", log.getSerial()));
        jdbc.update(
                "INSERT INTO loader_state_logs ( serial, create_user_id, update_user_id ) "
                        + "VALUES ( :serial, :userId, :userId )",
                new MapSqlParameterSource("serial", log.getSerial())
                        .addValue("userId", "TestUser"));

        _dao.updateLoaderState(log);

        Map<String, Object> map = jdbc.queryForMap(
                "SELECT * FROM loader_state_logs WHERE serial = :serial",
                new MapSqlParameterSource("serial", log.getSerial()));

        assertEquals(log.getSerial(), map.get("serial"));
        assertTrue(map.get("log_date") instanceof Timestamp);
        assertEquals(log.getDvd(), map.get("dvd"));
        assertEquals(log.getNet(), map.get("net"));
        assertEquals(log.getWork(), map.get("work"));
        assertEquals(log.getOldNet(), map.get("old_net"));
        assertEquals(log.getDeliver(), map.get("deliver"));
        assertEquals(log.getFilesToDownload(), map.get("files_to_download"));
        assertEquals(log.getFilesDownloaded(), map.get("files_downloaded"));
        assertEquals(log.getLastAuth(), map.get("last_auth"));
        assertEquals(log.getLastAuthState(), map.get("last_auth_state"));
        assertEquals(log.getDownloadState(), map.get("download_state"));
        assertTrue(map.get("receipt_date") instanceof Timestamp);
        assertEquals("TestUser", map.get("create_user_id"));
        assertEquals(LoaderStateRecorderDao.USER_ID, map.get("update_user_id"));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.LoaderStateRecorderDaoBean#updateLoaderState(jp.co.sega.allnet.auth.common.entity.LoaderStateLog)}
     * のためのテスト・メソッド。
     */
    @Test(expected = NullPointerException.class)
    public final void testUpdateLoaderStateargIsNull() {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM loader_state_logs WHERE serial = :serial ",
                new MapSqlParameterSource("serial", "AZZZZZZZZZZ"));
        jdbc.update(
                "INSERT INTO loader_state_logs ( serial, create_user_id, update_user_id ) "
                        + "VALUES ( :serial, :userId, :userId )",
                new MapSqlParameterSource("serial", "AZZZZZZZZZZ")
                        .addValue("userId", "TestUser"));

        _dao.updateLoaderState(null);

    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.LoaderStateRecorderDaoBean#updateLoaderState(jp.co.sega.allnet.auth.common.entity.LoaderStateLog)}
     * のためのテスト・メソッド。
     */
    @Test(expected = EmptyResultDataAccessException.class)
    public final void testUpdateLoaderStateArgDifferentSerial() {
        LoaderStateLog log = new LoaderStateLog();
        log.setSerial("AZZZZZZZZZZ");
        log.setDvd(new BigDecimal(1));
        log.setNet(new BigDecimal(2));
        log.setWork(new BigDecimal(3));
        log.setOldNet(new BigDecimal(4));
        log.setDeliver(new BigDecimal(5));
        log.setFilesToDownload(new BigDecimal(6));
        log.setFilesDownloaded(new BigDecimal(7));
        // リクエストはunix秒で渡ってくる
        log.setLastAuth(new Timestamp(Long.parseLong("8")));
        log.setLastAuthState(new BigDecimal(9));
        log.setDownloadState(new BigDecimal(10));

        String otherSerial = "AYYYYYYYYYY";

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM loader_state_logs WHERE serial = :serial ",
                new MapSqlParameterSource("serial", log.getSerial()));
        jdbc.update("DELETE FROM loader_state_logs WHERE serial = :serial ",
                new MapSqlParameterSource("serial", otherSerial));
        jdbc.update(
                "INSERT INTO loader_state_logs ( serial, create_user_id, update_user_id ) "
                        + "VALUES ( :serial, :userId, :userId )",
                new MapSqlParameterSource("serial", otherSerial)
                        .addValue("userId", "TestUser"));

        _dao.updateLoaderState(log);

        jdbc.queryForMap(
                "SELECT * FROM loader_state_logs WHERE serial = :serial",
                new MapSqlParameterSource("serial", log.getSerial()));

    }
}
