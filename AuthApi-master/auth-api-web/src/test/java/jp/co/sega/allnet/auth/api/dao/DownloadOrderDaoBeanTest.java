/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrder;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

/**
 * @author TsuboiY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "dao-test-context.xml" })
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class DownloadOrderDaoBeanTest {

    @Resource(name = "dataSource")
    private DataSource _ds;

    @Resource(name = "downloadOrderDao")
    private DownloadOrderDao _dao;

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findMachineDownloadOrder(java.lang.String)}
     * .
     */
    @Test
    public final void testFindMachineDownloadOrderSuccess() {
        String serial = "AZZZZZZZZZZ";

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 2;
        int setting = 1;
        String uri = "Test/uri";
        String gameId = "SBXX";
        int imageType = 0;

        deleteMachine(serial);
        deletePlace(allnetId, countryCode);
        createTestPlace(allnetId, countryCode);
        createTestMachine(serial, allnetId, gameId, groupIndex, setting);
        createTestMachineDownloadOrder(serial, uri, gameId);

        DownloadOrder order = _dao.findMachineDownloadOrder(serial, imageType);

        assertEquals(serial, order.getSerial());
        assertEquals(allnetId, order.getAllnetId());
        assertEquals(countryCode, order.getCountryCode());
        assertEquals(groupIndex, order.getGroupIndex());
        assertEquals(setting, order.getSetting());
        assertEquals(uri, order.getUri());
        assertNull(order.getGroupSerials());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findMachineDownloadOrder(java.lang.String)}
     * .
     */
    @Test
    public final void testFindMachineDownloadOrderNoMachine() {
        String serial = "AZZZZZZZZZZ";

        int allnetId = -99999;
        String countryCode = "TWN";
        int imageType = 0;

        deleteMachine(serial);
        deletePlace(allnetId, countryCode);
        createTestPlace(allnetId, countryCode);

        DownloadOrder order = _dao.findMachineDownloadOrder(serial, imageType);

        assertNull(order.getSerial());
        assertEquals(0, order.getAllnetId());
        assertNull(order.getCountryCode());
        assertEquals(0, order.getGroupIndex());
        assertEquals(0, order.getSetting());
        assertNull(order.getUri());
        assertNull(order.getGroupSerials());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findMachineDownloadOrder(java.lang.String)}
     * .
     */
    @Test
    public final void testFindMachineDownloadOrderNoPlace() {
        String serial = "AZZZZZZZZZZ";

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 2;
        int setting = 1;
        String uri = "Test/uri";
        String gameId = "SBXX";
        int imageType = 0;

        deleteMachine(serial);
        deletePlace(allnetId, countryCode);
        createTestMachine(serial, allnetId, gameId, groupIndex, setting);
        createTestMachineDownloadOrder(serial, uri, gameId);

        DownloadOrder order = _dao.findMachineDownloadOrder(serial, imageType);

        assertNull(order.getSerial());
        assertEquals(0, order.getAllnetId());
        assertNull(order.getCountryCode());
        assertEquals(0, order.getGroupIndex());
        assertEquals(0, order.getSetting());
        assertNull(order.getUri());
        assertNull(order.getGroupSerials());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findMachineDownloadOrder(java.lang.String)}
     * .
     */
    @Test
    public final void testFindMachineDownloadOrderNoMachineDownloadOrder() {
        String serial = "AZZZZZZZZZZ";

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 2;
        int setting = 1;
        String gameId = "SBXX";
        int imageType = 0;

        deleteMachine(serial);
        deletePlace(allnetId, countryCode);
        createTestPlace(allnetId, countryCode);
        createTestMachine(serial, allnetId, gameId, groupIndex, setting);

        DownloadOrder order = _dao.findMachineDownloadOrder(serial, imageType);

        assertEquals(serial, order.getSerial());
        assertEquals(allnetId, order.getAllnetId());
        assertEquals(countryCode, order.getCountryCode());
        assertEquals(groupIndex, order.getGroupIndex());
        assertEquals(setting, order.getSetting());
        assertNull(order.getUri());
        assertNull(order.getGroupSerials());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findMachineDownloadOrder(java.lang.String)}
     * .
     */
    @Test
    public final void testFindMachineDownloadOrderArgSerialIsNull() {
        String serial = "AZZZZZZZZZZ";

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 2;
        int setting = 1;
        String uri = "Test/uri";
        String gameId = "SBXX";
        int imageType = 0;

        deleteMachine(serial);
        deletePlace(allnetId, countryCode);
        createTestPlace(allnetId, countryCode);
        createTestMachine(serial, allnetId, gameId, groupIndex, setting);
        createTestMachineDownloadOrder(serial, uri, gameId);

        DownloadOrder order = _dao.findMachineDownloadOrder(null, imageType);

        assertNull(order.getSerial());
        assertEquals(0, order.getAllnetId());
        assertNull(order.getCountryCode());
        assertEquals(0, order.getGroupIndex());
        assertEquals(0, order.getSetting());
        assertNull(order.getUri());
        assertNull(order.getGroupSerials());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findMachineDownloadOrder(java.lang.String)}
     * .
     */
    @Test
    public final void testFindMachineDownloadOrderMachineDownloadOrderUriIsNull() {
        String serial = "AZZZZZZZZZZ";

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 2;
        int setting = 1;
        String uri = null;
        String gameId = "SBXX";
        int imageType = 0;

        deleteMachine(serial);
        deletePlace(allnetId, countryCode);
        createTestPlace(allnetId, countryCode);
        createTestMachine(serial, allnetId, gameId, groupIndex, setting);
        createTestMachineDownloadOrder(serial, uri, gameId);

        DownloadOrder order = _dao.findMachineDownloadOrder(serial, imageType);

        assertEquals(serial, order.getSerial());
        assertEquals(allnetId, order.getAllnetId());
        assertEquals(countryCode, order.getCountryCode());
        assertEquals(groupIndex, order.getGroupIndex());
        assertEquals(setting, order.getSetting());
        assertNull(order.getUri());
        assertNull(order.getGroupSerials());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findGroupSerials(java.lang.String, int, int, java.lang.String)}
     * .
     */
    @Test
    public final void testFindGroupSerialsSuccess() {
        String serial = "AZZZZZZZZZZ";
        int allnetId = -99999;
        int groupIndex = 2;
        String gameId = "SBXX";

        String otherSerial = "AYYYYYYYYYY";
        int setting = 1;

        deleteMachine(serial);
        deleteMachinesByAllnetId(allnetId);
        createTestMachine(serial, allnetId, gameId, groupIndex, setting);
        createTestMachine(otherSerial, allnetId, gameId, groupIndex, setting);

        List<String> list = _dao.findGroupSerials(serial, allnetId, groupIndex,
                gameId);
        assertEquals(1, list.size());
        assertEquals(otherSerial, list.get(0));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findGroupSerials(java.lang.String, int, int, java.lang.String)}
     * .
     */
    @Test
    public final void testFindGroupSerialsSuccessMany() {
        String serial = "AZZZZZZZZZZ";
        int allnetId = -99999;
        int groupIndex = 2;
        String gameId = "SBXX";

        String otherSerial1 = "AYYYYYYYYYY";
        String otherSerial2 = "AXXXXXXXXXX";
        int otherAllnetId2 = -99998;
        String otherSerial3 = "AWWWWWWWWWW";
        int otherGroupIndex3 = 1;
        String otherSerial4 = "AVVVVVVVVVV";
        String otherGameId4 = "SBYY";
        String otherSerial5 = "AUUUUUUUUUU";

        int setting = 1;

        deleteMachine(serial);
        deleteMachinesByAllnetId(allnetId);
        createTestMachine(serial, allnetId, gameId, groupIndex, setting);
        createTestMachine(otherSerial1, allnetId, gameId, groupIndex, setting);
        createTestMachine(otherSerial2, otherAllnetId2, gameId, groupIndex,
                setting);
        createTestMachine(otherSerial3, allnetId, gameId, otherGroupIndex3,
                setting);
        createTestMachine(otherSerial4, allnetId, otherGameId4, groupIndex,
                setting);
        createTestMachine(otherSerial5, allnetId, gameId, groupIndex, setting);

        List<String> list = _dao.findGroupSerials(serial, allnetId, groupIndex,
                gameId);
        Collections.sort(list);

        assertEquals(2, list.size());
        assertEquals(otherSerial5, list.get(0));
        assertEquals(otherSerial1, list.get(1));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findGroupSerials(java.lang.String, int, int, java.lang.String)}
     * .
     */
    @Test
    public final void testFindGroupSerialsNoResult() {
        String serial = "AZZZZZZZZZZ";
        int allnetId = -99999;
        int groupIndex = 2;
        String gameId = "SBXX";

        int setting = 1;

        deleteMachine(serial);
        deleteMachinesByAllnetId(allnetId);
        createTestMachine(serial, allnetId, gameId, groupIndex, setting);

        List<String> list = _dao.findGroupSerials(serial, allnetId, groupIndex,
                gameId);
        assertEquals(0, list.size());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findGroupSerials(java.lang.String, int, int, java.lang.String)}
     * .
     */
    @Test
    public final void testFindGroupSerialsArgSerialIsNull() {
        String serial = null;
        int allnetId = -99999;
        int groupIndex = 2;
        String gameId = "SBXX";

        String otherSerial = "AYYYYYYYYYY";
        int setting = 1;

        deleteMachine(otherSerial);
        deleteMachinesByAllnetId(allnetId);
        createTestMachine(otherSerial, allnetId, gameId, groupIndex, setting);

        List<String> list = _dao.findGroupSerials(serial, allnetId, groupIndex,
                gameId);

        assertEquals(0, list.size());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findGroupSerials(java.lang.String, int, int, java.lang.String)}
     * .
     */
    @Test
    public final void testFindGroupSerialsArgGameIdIsNull() {
        String serial = "AZZZZZZZZZZ";
        int allnetId = -99999;
        int groupIndex = 2;
        String gameId = null;

        String otherSerial = "AYYYYYYYYYY";
        String dataGameId = "SBXX";
        int setting = 1;

        deleteMachine(serial);
        deleteMachine(otherSerial);
        deleteMachinesByAllnetId(allnetId);
        createTestMachine(serial, allnetId, dataGameId, groupIndex, setting);
        createTestMachine(otherSerial, allnetId, dataGameId, groupIndex,
                setting);

        List<String> list = _dao.findGroupSerials(serial, allnetId, groupIndex,
                gameId);

        assertEquals(0, list.size());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findUriByCountry(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindUriByCountrySuccess() {
        String gameId = "SBXX";
        String gameVer = "1.00";
        String countryCode = "TWN";
        int imageType = 0;

        String uri = "Test/Uri";

        createTestCountryDownloadOrder(gameId, gameVer, countryCode, uri);

        assertEquals(uri,
                _dao.findUriByCountry(gameId, gameVer, countryCode, imageType));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findUriByCountry(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindUriByCountryNoResult() {
        String gameId = "SBXX";
        String gameVer = "1.00";
        String countryCode = "TWN";
        int imageType = 0;

        String dataGameId = "SBYY";
        String uri = "Test/Uri";

        createTestCountryDownloadOrder(dataGameId, gameVer, countryCode, uri);

        assertNull(
                _dao.findUriByCountry(gameId, gameVer, countryCode, imageType));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findUriByCountry(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindUriByCountryArgGameIdIsNull() {
        String gameId = null;
        String gameVer = "1.00";
        String countryCode = "TWN";
        int imageType = 0;

        String dataGameId = "SBYY";
        String uri = "Test/Uri";

        createTestCountryDownloadOrder(dataGameId, gameVer, countryCode, uri);

        assertNull(
                _dao.findUriByCountry(gameId, gameVer, countryCode, imageType));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findUriByCountry(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindUriByCountryArgGameVerIsNull() {
        String gameId = "SBXX";
        String gameVer = null;
        String countryCode = "TWN";
        int imageType = 0;

        String dataGameVer = "1.00";
        String uri = "Test/Uri";

        createTestCountryDownloadOrder(gameId, dataGameVer, countryCode, uri);

        assertNull(
                _dao.findUriByCountry(gameId, gameVer, countryCode, imageType));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findUriByCountry(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindUriByCountryArgCountryCodeIsNull() {
        String gameId = "SBXX";
        String gameVer = "1.00";
        String countryCode = null;
        int imageType = 0;

        String dataCountryCode = "TWN";
        String uri = "Test/Uri";

        createTestCountryDownloadOrder(gameId, gameVer, dataCountryCode, uri);

        assertNull(
                _dao.findUriByCountry(gameId, gameVer, countryCode, imageType));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findUriByGame(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindUriByGameSuccess() {
        String gameId = "SBXX";
        String gameVer = "1.00";
        int imageType = 0;

        String uri = "Test/Uri";

        createTestDownloadOrder(gameId, gameVer, uri);

        assertEquals(uri, _dao.findUriByGame(gameId, gameVer, imageType));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findUriByGame(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindUriByGameNoResult() {
        String gameId = "SBXX";
        String gameVer = "1.00";
        int imageType = 0;

        String dataGameId = "SBYY";
        String uri = "Test/Uri";

        createTestDownloadOrder(dataGameId, gameVer, uri);

        assertNull(_dao.findUriByGame(gameId, gameVer, imageType));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findUriByGame(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindUriByGameArgGameIdIsNull() {
        String gameId = null;
        String gameVer = "1.00";
        int imageType = 0;

        String dataGameId = "SBYY";
        String uri = "Test/Uri";

        createTestDownloadOrder(dataGameId, gameVer, uri);

        assertNull(_dao.findUriByGame(gameId, gameVer, imageType));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.DownloadOrderDaoBean#findUriByGame(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindUriByGameArgGameVerIsNull() {
        String gameId = "SBXX";
        String gameVer = null;
        int imageType = 0;

        String dataGameVer = "1.00";
        String uri = "Test/Uri";

        createTestDownloadOrder(gameId, dataGameVer, uri);

        assertNull(_dao.findUriByGame(gameId, gameVer, imageType));
    }

    private void deleteMachine(String serial) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM machine_statuses WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "DELETE FROM machine_download_orders WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));
        jdbc.update("DELETE FROM machines WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));
    }

    private void deleteMachinesByAllnetId(int allnetId) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update(
                "DELETE FROM machine_statuses WHERE serial in (SELECT serial FROM machines WHERE allnet_id = :allnetId)",
                new MapSqlParameterSource("allnetId", allnetId));
        jdbc.update(
                "DELETE FROM machine_download_orders WHERE serial in (SELECT serial FROM machines WHERE allnet_id = :allnetId)",
                new MapSqlParameterSource("allnetId", allnetId));
        jdbc.update("DELETE FROM machines WHERE allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId", allnetId));
    }

    private void deletePlace(int allnetId, String countryCode) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM routers", new MapSqlParameterSource());
        jdbc.update("DELETE FROM auth_allowed_places", new MapSqlParameterSource());
        jdbc.update("DELETE FROM places WHERE allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId", allnetId));
        jdbc.update("DELETE FROM places WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode));
        jdbc.update("DELETE FROM region3 WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode));
        jdbc.update("DELETE FROM region2 WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode));
        jdbc.update("DELETE FROM region1 WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode));
        jdbc.update("DELETE FROM region0 WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode));
        jdbc.update("DELETE FROM country_download_orders", new MapSqlParameterSource());
        jdbc.update("DELETE FROM game_attributes", new MapSqlParameterSource());
        jdbc.update("DELETE FROM countries WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode));
    }

    private void createTestPlace(int allnetId, String countryCode) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update(
                "INSERT INTO countries (country_code, create_user_id, update_user_id) VALUES (:countryCode, :userId, :userId)",
                new MapSqlParameterSource("countryCode", countryCode)
                        .addValue("userId", "TestUser"));
        jdbc.update(
                "INSERT INTO places (allnet_id, place_id, name, country_code, create_user_id, update_user_id) "
                        + "VALUES (:allnetId, :placeId, :name, :countryCode, :userId, :userId)",
                new MapSqlParameterSource("countryCode", countryCode)
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("placeId", "XXXX")
                        .addValue("name", "テスト店舗1"));
    }

    private void createTestMachine(String serial, int allnetId, String gameId,
            int groupIndex, int setting) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update(
                "INSERT INTO machines (serial, allnet_id, game_id, group_index, setting, create_user_id, update_user_id) VALUES (:serial, :allnetId, :gameId, :groupIndex, :setting, :userId, :userId)",
                new MapSqlParameterSource("serial", serial)
                        .addValue("allnetId", allnetId)
                        .addValue("gameId", gameId)
                        .addValue("groupIndex", groupIndex)
                        .addValue("setting", setting)
                        .addValue("userId", "TestUser"));
    }

    private void createTestMachineDownloadOrder(String serial, String uri,
            String gameId) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update(
                "INSERT INTO machine_download_orders (serial, uri, create_user_id, update_user_id, game_id) VALUES (:serial, :uri, :userId, :userId, :gameId)",
                new MapSqlParameterSource("serial", serial).addValue("uri", uri)
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));
    }

    private void createTestCountryDownloadOrder(String gameId, String gameVer,
            String countryCode, String uri) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);

        MapSqlParameterSource params = new MapSqlParameterSource("countryCode",
                countryCode);
        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM countries WHERE country_code = :countryCode",
                params, Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO countries ( country_code, create_user_id, update_user_id ) "
                            + "VALUES ( :countryCode, :userId, :userId )",
                    params.addValue("userId", "TestUser"));
        }

        params = new MapSqlParameterSource("gameId", gameId);
        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM games WHERE game_id = :gameId",
                params, Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games ( game_id, title, create_user_id, update_user_id ) "
                            + "VALUES ( :gameId, :title, :userId, :userId )",
                    params.addValue("title", "TestGame").addValue("userId",
                            "TestUser"));
        }

        params = new MapSqlParameterSource("gameId", gameId)
                .addValue("gameVer", gameVer)
                .addValue("countryCode", countryCode);
        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM country_download_orders WHERE game_id = :gameId AND game_ver = :gameVer AND country_code = :countryCode",
                params, Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO country_download_orders (game_id, game_ver, country_code, uri, create_user_id, update_user_id) "
                            + "VALUES (:gameId, :gameVer, :countryCode, :uri, :userId, :userId)",
                    params.addValue("uri", uri).addValue("userId", "TestUser"));
        }
    }

    private void createTestDownloadOrder(String gameId, String gameVer,
            String uri) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);

        MapSqlParameterSource params = new MapSqlParameterSource("gameId",
                gameId);
        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM games WHERE game_id = :gameId",
                params, Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games ( game_id, title, create_user_id, update_user_id )"
                            + "VALUES ( :gameId, :title, :userId, :userId )",
                    params.addValue("title", "TestGame").addValue("userId",
                            "TestUser"));
        }

        params = new MapSqlParameterSource("gameId", gameId).addValue("gameVer",
                gameVer);
        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM download_orders WHERE game_id = :gameId AND game_ver = :gameVer",
                params, Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO download_orders (game_id, game_ver, uri, create_user_id, update_user_id) "
                            + "VALUES (:gameId, :gameVer, :uri, :userId, :userId)",
                    params.addValue("uri", uri).addValue("userId", "TestUser"));
        }
    }
}
