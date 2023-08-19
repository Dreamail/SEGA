/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sega.allnet.auth.api.dao.ApiAccountDao.ApiAccountStatus;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

/**
 * @author NakanoY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "dao-test-context.xml" })
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class ApiAccountDaoBeanTest {

    @Resource(name = "dataSource")
    private DataSource _ds;

    @Resource(name = "apiAccountDao")
    private ApiAccountDao _dao;

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkApiAccount(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckApiAccountSuccess() {
        String gameId = "SBXX";
        String password = "pass";

        createAccount(new NamedParameterJdbcTemplate(_ds), gameId, password);

        assertTrue(_dao.checkApiAccount(gameId, password));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkApiAccount(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckApiAccountFailGameId() {
        String gameId = "SBXX";
        String password = "pass";
        String gameIdNG = "SBYY";

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        createAccount(jdbc, gameId, password);
        jdbc.update("DELETE FROM title_api_accounts WHERE game_id = :game_id", 
        		new MapSqlParameterSource("game_id", gameIdNG));

        assertFalse(_dao.checkApiAccount(gameIdNG, password));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkApiAccount(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckApiAccountFailPassword() {
        String gameId = "SBXX";
        String password = "pass";
        String passwordNG = "passNG";

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        createAccount(jdbc, gameId, password);
        jdbc.update("DELETE FROM title_api_accounts WHERE password = :password",
        		new MapSqlParameterSource("password", passwordNG));

        assertFalse(_dao.checkApiAccount(gameId, passwordNG));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkApiAccount(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckApiAccountArgGameIdIsNull() {
        String gameId = "SBXX";
        String password = "pass";

        createAccount(new NamedParameterJdbcTemplate(_ds), gameId, password);

        assertFalse(_dao.checkApiAccount(null, password));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkApiAccount(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckApiAccountArgPasswordIsNull() {
        String gameId = "SBXX";
        String password = "pass";

        createAccount(new NamedParameterJdbcTemplate(_ds), gameId, password);

        assertFalse(_dao.checkApiAccount(gameId, null));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkApiAccount(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckApiAccountAndPlaceAllOk() {
        String gameId = "SBXX";
        String password = "pass";

        createAccount(new NamedParameterJdbcTemplate(_ds), gameId, password);

        assertEquals(ApiAccountStatus.OK,
                _dao.checkApiAccountAndPlaceAll(gameId, password));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkApiAccount(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckApiAccountAndPlaceAllOkPlaceAll() {
        String gameId = "SBXX";
        String password = "pass";

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        createAccount(jdbc, gameId, password);
        jdbc.update(
                "UPDATE title_api_accounts SET get_place_list_all = :get_place_list_all WHERE game_id = :game_id",
                new MapSqlParameterSource("get_place_list_all", "1").addValue("game_id", gameId));

        assertEquals(ApiAccountStatus.OK_PLACE_ALL,
                _dao.checkApiAccountAndPlaceAll(gameId, password));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkApiAccount(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckApiAccountAndPlaceAllFailGameId() {
        String gameId = "SBXX";
        String password = "pass";
        String gameIdNG = "SBYY";

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        createAccount(jdbc, gameId, password);
        jdbc.update("DELETE FROM title_api_accounts WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameIdNG));

        assertEquals(ApiAccountStatus.NG,
                _dao.checkApiAccountAndPlaceAll(gameIdNG, password));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkApiAccount(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckApiAccountAndPlaceAllFailPassword() {
        String gameId = "SBXX";
        String password = "pass";
        String passwordNG = "passNG";

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        createAccount(jdbc, gameId, password);
        jdbc.update("DELETE FROM title_api_accounts WHERE password = :password",
                new MapSqlParameterSource("password", passwordNG));

        assertEquals(ApiAccountStatus.NG,
                _dao.checkApiAccountAndPlaceAll(gameId, passwordNG));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkApiAccount(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckApiAccountAndPlaceAllArgGameIdIsNull() {
        String gameId = "SBXX";
        String password = "pass";

        createAccount(new NamedParameterJdbcTemplate(_ds), gameId, password);

        assertEquals(ApiAccountStatus.NG,
                _dao.checkApiAccountAndPlaceAll(null, password));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkApiAccount(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckApiAccountAndPlaceAllArgPasswordIsNull() {
        String gameId = "SBXX";
        String password = "pass";

        createAccount(new NamedParameterJdbcTemplate(_ds), gameId, password);

        assertEquals(ApiAccountStatus.NG,
                _dao.checkApiAccountAndPlaceAll(gameId, null));
    }

    /**
     * 
     * @param jdbc
     * @param gameId
     * @param password
     */
    private void createAccount(NamedParameterJdbcTemplate jdbc, String gameId,
            String password) {
        MapSqlParameterSource params = new MapSqlParameterSource("gameId",
                gameId);
        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM games WHERE game_id = :gameId",
                params, Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games ( game_id, title, create_user_id, update_user_id ) "
                            + "VALUES ( :gameId, :title, :userId, :userId )",
                    params.addValue("title", "TestGame").addValue("userId",
                            "TestUser"));
        }

        params = new MapSqlParameterSource("gameId", gameId);
        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM title_api_accounts WHERE game_id = :gameId",
                params, Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO title_api_accounts ( game_id, password, create_user_id, update_user_id ) "
                            + "VALUES ( :gameId, :password, :userId, :userId )",
                    params.addValue("password", password).addValue("userId",
                            "TestUser"));
        }
    }

}
