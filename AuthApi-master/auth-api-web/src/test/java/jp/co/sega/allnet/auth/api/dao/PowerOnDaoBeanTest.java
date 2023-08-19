/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sega.allnet.auth.api.domain.AuthAllowedComp;
import jp.co.sega.allnet.auth.api.domain.AuthAllowedPlace;
import jp.co.sega.allnet.auth.api.service.poweron.AuthStatus;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Game;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Machine;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Router;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Status;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

/**
 * @author TsuboiY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "dao-test-context.xml" })
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class PowerOnDaoBeanTest {

    @Resource(name = "dataSource")
    private DataSource _ds;

    @Resource(name = "powerOnDao")
    private PowerOnDao _dao;

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#find(java.lang.String, java.lang.String, java.lang.String, java.lang.String, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testFindSuccess() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBTY";
        String gameVer = "2.00";
        String serial = "A72E01B5800";
        String ip = "1.1.1.254";

        int allnetId = -99999;
        String billCode = "2Z3456789";
        String country = "ZZZ";
        int region0Id = 0;
        int region1Id = 0;
        int region2Id = 0;
        int region3Id = 0;

        // ルータ情報作成
        jdbc.update("DELETE FROM routers", new MapSqlParameterSource());
        jdbc.update("DELETE FROM auth_allowed_places", new MapSqlParameterSource());
        jdbc.update("DELETE FROM places WHERE allnet_id = :allnetId ",
                new MapSqlParameterSource("allnetId", allnetId));
        jdbc.update("DELETE FROM places WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update("DELETE FROM bills WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "DELETE FROM region3 WHERE country_code = :country and region_id = :region3Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "DELETE FROM region2 WHERE country_code = :country and region_id = :region2Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region2Id", region2Id));
        jdbc.update(
                "DELETE FROM region1 WHERE country_code = :country and region_id = :region1Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region1Id", region1Id));
        jdbc.update(
                "DELETE FROM region0 WHERE country_code = :country and region_id = :region0Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region0Id", region0Id));
        jdbc.update("DELETE FROM countries WHERE country_code = :country",
                new MapSqlParameterSource("country", country));

        jdbc.update(
                "INSERT INTO countries (country_code, create_user_id, update_user_id) VALUES (:country, :userId, :userId)",
                new MapSqlParameterSource("country", country).addValue("userId",
                        "TestUser"));
        jdbc.update(
                "INSERT INTO region0 (country_code, region_id, name, create_user_id, update_user_id) VALUES (:country, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域0"));
        jdbc.update(
                "INSERT INTO region1 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region1Id, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region1Id", region1Id)
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域1"));
        jdbc.update(
                "INSERT INTO region2 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region2Id, :region1Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region2Id", region2Id)
                        .addValue("region1Id", region1Id)
                        .addValue("name", "テスト地域2"));
        jdbc.update(
                "INSERT INTO region3 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region3Id, :region2Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region3Id", region3Id)
                        .addValue("region2Id", region2Id)
                        .addValue("name", "テスト地域3"));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser"));
        jdbc.update(
                "INSERT INTO places (allnet_id, place_id, name, tel, address, zip_code, station, open_time, close_time, special_info, bill_code, nickname, country_code, region0_id, region1_id, region2_id, region3_id, create_user_id, update_user_id) "
                        + "VALUES (:allnetId, :placeId, :name, :tel, :address, :zipCode, :station, :openTime, :closeTime, :specialInfo, :billCode, :nickname, :country, :region0Id, :region1Id, :region2Id, :region3Id, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("placeId", "XXXX").addValue("name", "テスト店舗1")
                        .addValue("tel", "1234567890")
                        .addValue("address", "テスト住所")
                        .addValue("zipCode", "1234567")
                        .addValue("station", "テスト駅")
                        .addValue("openTime", "10:00")
                        .addValue("closeTime", "21:00")
                        .addValue("specialInfo", "テストPR文")
                        .addValue("billCode", billCode)
                        .addValue("nickname", "テスト#ニックネーム")
                        .addValue("region0Id", region0Id)
                        .addValue("region1Id", region1Id)
                        .addValue("region2Id", region2Id)
                        .addValue("region3Id", region3Id)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "INSERT INTO routers (router_id, allnet_id, router_type_id, place_ip, create_user_id, update_user_id) VALUES (:routerId, :allnetId, :routerType, :ip, :userId, :userId)",
                new MapSqlParameterSource("routerId", "0000")
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("routerType", 99).addValue("ip", ip));

        // ゲーム情報の作成
        jdbc.update("DELETE FROM game_attributes WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM download_orders WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM game_competences WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM title_api_accounts WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM games WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));

        jdbc.update(
                "INSERT INTO games (game_id, title, create_user_id, update_user_id) VALUES (:gameId, :title, :userId, :userId)",
                new MapSqlParameterSource("title", "TestGame")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO game_attributes (game_id, game_ver, country_code, title, uri, host, auth, create_user_id, update_user_id) VALUES (:gameId, :gameVer, :country, :title, :uri, :host, :auth, :userId, :userId)",
                new MapSqlParameterSource("title", "TestGame")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId).addValue("gameVer", gameVer)
                        .addValue("country", country)
                        .addValue("uri", "Test/uri")
                        .addValue("host", "Test/host").addValue("auth", 1));

        // 基板情報の作成
        jdbc.update("DELETE FROM machine_statuses WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "DELETE FROM machine_download_orders WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update("DELETE FROM machines WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "INSERT INTO machines (serial, allnet_id, game_id, reserved_game_id, place_id, create_user_id, update_user_id) VALUES (:serial, :allnetId, :gameId, :reservedGameId, :placeId, :userId, :userId)",
                new MapSqlParameterSource("serial", serial)
                        .addValue("allnetId", allnetId)
                        .addValue("reservedGameId", "SBUK")
                        .addValue("placeId", "XXXX")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));

        PowerOnData data = _dao.find(gameId, gameVer, serial, ip);

        Map<String, Object> router = jdbc.queryForMap(
                "select * from routers where place_ip = :placeIp",
                new MapSqlParameterSource("placeIp", ip));
        Map<String, Object> rPlace = jdbc.queryForMap(
                "select * from places where allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId", router.get("allnet_id")));
        Map<String, Object> rRegion0 = jdbc.queryForMap(
                "select * from region0 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region0_id")));
        Map<String, Object> rRegion1 = jdbc.queryForMap(
                "select * from region1 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region1_id")));
        Map<String, Object> rRegion2 = jdbc.queryForMap(
                "select * from region2 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region2_id")));

        Map<String, Object> rRegion3;
        try {
            rRegion3 = jdbc.queryForMap(
                    "select * from region3 where country_code = :countryCode and region_id = :regionId",
                    new MapSqlParameterSource("countryCode", country)
                            .addValue("regionId", rPlace.get("region3_id")));
        } catch (EmptyResultDataAccessException e) {
            rRegion3 = Collections.emptyMap();
        }
        Map<String, Object> game = jdbc.queryForMap(
                "select * from game_attributes where game_id = :gameId and game_ver = :gameVer and country_code = :countryCode",
                new MapSqlParameterSource().addValue("gameId", gameId)
                        .addValue("gameVer", gameVer)
                        .addValue("countryCode", country));
        Map<String, Object> machine = jdbc.queryForMap(
                "select * from machines where serial = :serial",
                new MapSqlParameterSource("serial", serial));
        Map<String, Object> mPlace = jdbc.queryForMap(
                "select * from places where allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId",
                        machine.get("allnet_id")));
        Map<String, Object> mRegion0 = jdbc.queryForMap(
                "select * from region0 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region0_id")));
        Map<String, Object> mRegion1 = jdbc.queryForMap(
                "select * from region1 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region1_id")));
        Map<String, Object> mRegion2 = jdbc.queryForMap(
                "select * from region2 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region2_id")));
        Map<String, Object> mRegion3;
        try {
            mRegion3 = jdbc.queryForMap(
                    "select * from region3 where country_code = :countryCode and region_id = :regionId",
                    new MapSqlParameterSource("countryCode", country)
                            .addValue("regionId", mPlace.get("region3_id")));
        } catch (EmptyResultDataAccessException e) {
            mRegion3 = Collections.emptyMap();
        }

        assertEquals(router.get("router_id"),
                data.getRouter().getRouterId());
        assertEquals(((BigDecimal) rPlace.get("allnet_id")).longValue(),
                data.getRouter().getAllnetId().longValue());
        assertEquals(rPlace.get("place_id"),
                data.getRouter().getPlaceId());
        assertEquals(rPlace.get("name"),
                data.getRouter().getPlaceName());
        assertEquals(rPlace.get("bill_code"),
                data.getRouter().getBillCode());
        assertEquals(rPlace.get("nickname"),
                data.getRouter().getPlaceNickName());
        assertEquals(((BigDecimal) rRegion0.get("region_id")).intValue(),
                data.getRouter().getRegion0Id());
        assertEquals(rPlace.get("country_code"),
                data.getRouter().getCountryCode());
        assertEquals(rRegion0.get("name"),
                data.getRouter().getRegion0Name());
        assertEquals(rRegion1.get("name"),
                data.getRouter().getRegion1Name());
        assertEquals(rRegion2.get("name"),
                data.getRouter().getRegion2Name());
        assertEquals(rRegion3.get("name"),
                data.getRouter().getRegion3Name());

        assertEquals(game.get("game_id"), data.getGame().getGameId());
        assertEquals(game.get("game_ver"), data.getGame().getGameVer());
        assertEquals(((BigDecimal) game.get("auth")).intValue(),
                data.getGame().getAuth());
        assertEquals(game.get("uri"), data.getGame().getUri());
        assertEquals(game.get("host"), data.getGame().getHost());

        assertEquals(machine.get("serial"),
                data.getMachine().getSerial());
        assertEquals(machine.get("game_id"),
                data.getMachine().getGameId());
        assertEquals(machine.get("reserved_game_id"),
                data.getMachine().getReservedGameId());
        assertEquals(((BigDecimal) machine.get("setting")).intValue(),
                data.getMachine().getSetting());
        assertEquals(((BigDecimal) mPlace.get("allnet_id")).longValue(),
                data.getMachine().getAllnetId().longValue());
        assertEquals(mPlace.get("place_id"),
                data.getMachine().getPlaceId());
        assertEquals(mPlace.get("name"),
                data.getMachine().getPlaceName());
        assertEquals(mPlace.get("bill_code"),
                data.getMachine().getBillCode());
        assertEquals(mPlace.get("nickname"),
                data.getMachine().getPlaceNickName());
        assertEquals(((BigDecimal) mRegion0.get("region_id")).intValue(),
                data.getMachine().getRegion0Id().intValue());
        assertEquals(mPlace.get("country_code"),
                data.getMachine().getCountryCode());
        assertEquals(mRegion0.get("name"),
                data.getMachine().getRegion0Name());
        assertEquals(mRegion1.get("name"),
                data.getMachine().getRegion1Name());
        assertEquals(mRegion2.get("name"),
                data.getMachine().getRegion2Name());
        assertEquals(mRegion3.get("name"),
                data.getMachine().getRegion3Name());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#find(java.lang.String, java.lang.String, java.lang.String, java.lang.String, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testFindSuccessSpecialIp() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBTY";
        String gameVer = "2.00";
        String serial = "A72E01B5800";
        String requestIp = "1.1.1.1";
        String routerIp = "1.1.1.254";

        int allnetId = -99999;
        String billCode = "2Z3456789";
        String country = "ZZZ";
        int region0Id = 0;
        int region1Id = 0;
        int region2Id = 0;
        int region3Id = 0;

        // ルータ情報作成
        jdbc.update("DELETE FROM routers", new MapSqlParameterSource());
        jdbc.update("DELETE FROM auth_allowed_places", new MapSqlParameterSource());
        jdbc.update("DELETE FROM places WHERE allnet_id = :allnetId ",
                new MapSqlParameterSource("allnetId", allnetId));
        jdbc.update("DELETE FROM places WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update("DELETE FROM bills WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "DELETE FROM region3 WHERE country_code = :country and region_id = :region3Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "DELETE FROM region2 WHERE country_code = :country and region_id = :region2Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region2Id", region2Id));
        jdbc.update(
                "DELETE FROM region1 WHERE country_code = :country and region_id = :region1Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region1Id", region1Id));
        jdbc.update(
                "DELETE FROM region0 WHERE country_code = :country and region_id = :region0Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region0Id", region0Id));
        jdbc.update("DELETE FROM countries WHERE country_code = :country",
                new MapSqlParameterSource("country", country));

        jdbc.update(
                "INSERT INTO countries (country_code, create_user_id, update_user_id) VALUES (:country, :userId, :userId)",
                new MapSqlParameterSource("country", country).addValue("userId",
                        "TestUser"));
        jdbc.update(
                "INSERT INTO region0 (country_code, region_id, name, create_user_id, update_user_id) VALUES (:country, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域0"));
        jdbc.update(
                "INSERT INTO region1 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region1Id, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region1Id", region1Id)
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域1"));
        jdbc.update(
                "INSERT INTO region2 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region2Id, :region1Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region2Id", region2Id)
                        .addValue("region1Id", region1Id)
                        .addValue("name", "テスト地域2"));
        jdbc.update(
                "INSERT INTO region3 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region3Id, :region2Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region3Id", region3Id)
                        .addValue("region2Id", region2Id)
                        .addValue("name", "テスト地域3"));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser"));
        jdbc.update(
                "INSERT INTO places (allnet_id, place_id, name, tel, address, zip_code, station, open_time, close_time, special_info, bill_code, nickname, country_code, region0_id, region1_id, region2_id, region3_id, create_user_id, update_user_id) "
                        + "VALUES (:allnetId, :placeId, :name, :tel, :address, :zipCode, :station, :openTime, :closeTime, :specialInfo, :billCode, :nickname, :country, :region0Id, :region1Id, :region2Id, :region3Id, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("placeId", "XXXX").addValue("name", "テスト店舗1")
                        .addValue("tel", "1234567890")
                        .addValue("address", "テスト住所")
                        .addValue("zipCode", "1234567")
                        .addValue("station", "テスト駅")
                        .addValue("openTime", "10:00")
                        .addValue("closeTime", "21:00")
                        .addValue("specialInfo", "テストPR文")
                        .addValue("billCode", billCode)
                        .addValue("nickname", "テスト#ニックネーム")
                        .addValue("region0Id", region0Id)
                        .addValue("region1Id", region1Id)
                        .addValue("region2Id", region2Id)
                        .addValue("region3Id", region3Id)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "INSERT INTO routers (router_id, allnet_id, router_type_id, place_ip, create_user_id, update_user_id) VALUES (:routerId, :allnetId, :routerType, :ip, :userId, :userId)",
                new MapSqlParameterSource("routerId", "0000")
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("routerType", 99).addValue("ip", routerIp));

        // ゲーム情報の作成
        jdbc.update("DELETE FROM game_attributes WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM download_orders WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM game_competences WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM title_api_accounts WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM games WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));

        jdbc.update(
                "INSERT INTO games (game_id, title, create_user_id, update_user_id) VALUES (:gameId, :title, :userId, :userId)",
                new MapSqlParameterSource("title", "TestGame")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO game_attributes (game_id, game_ver, country_code, title, uri, host, auth, create_user_id, update_user_id) VALUES (:gameId, :gameVer, :country, :title, :uri, :host, :auth, :userId, :userId)",
                new MapSqlParameterSource("title", "TestGame")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId).addValue("gameVer", gameVer)
                        .addValue("country", country)
                        .addValue("uri", "Test/uri")
                        .addValue("host", "Test/host").addValue("auth", 1));

        // 基板情報の作成
        jdbc.update("DELETE FROM machine_statuses WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "DELETE FROM machine_download_orders WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update("DELETE FROM machines WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "INSERT INTO machines (serial, allnet_id, game_id, reserved_game_id, place_id, create_user_id, update_user_id) VALUES (:serial, :allnetId, :gameId, :reservedGameId, :placeId, :userId, :userId)",
                new MapSqlParameterSource("serial", serial)
                        .addValue("allnetId", allnetId)
                        .addValue("reservedGameId", "SBUK")
                        .addValue("placeId", "XXXX")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));

        PowerOnData data = _dao.find(gameId, gameVer, serial, requestIp);

        Map<String, Object> router = jdbc.queryForMap(
                "select * from routers where place_ip = :placeIp",
                new MapSqlParameterSource("placeIp", routerIp));
        Map<String, Object> rPlace = jdbc.queryForMap(
                "select * from places where allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId", router.get("allnet_id")));
        Map<String, Object> rRegion0 = jdbc.queryForMap(
                "select * from region0 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region0_id")));
        Map<String, Object> rRegion1 = jdbc.queryForMap(
                "select * from region1 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region1_id")));
        Map<String, Object> rRegion2 = jdbc.queryForMap(
                "select * from region2 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region2_id")));

        Map<String, Object> rRegion3;
        try {
            rRegion3 = jdbc.queryForMap(
                    "select * from region3 where country_code = :countryCode and region_id = :regionId",
                    new MapSqlParameterSource("countryCode", country)
                            .addValue("regionId", rPlace.get("region3_id")));
        } catch (EmptyResultDataAccessException e) {
            rRegion3 = Collections.emptyMap();
        }
        Map<String, Object> game = jdbc.queryForMap(
                "select * from game_attributes where game_id = :gameId and game_ver = :gameVer and country_code = :countryCode",
                new MapSqlParameterSource().addValue("gameId", gameId)
                        .addValue("gameVer", gameVer)
                        .addValue("countryCode", country));
        Map<String, Object> machine = jdbc.queryForMap(
                "select * from machines where serial = :serial",
                new MapSqlParameterSource("serial", serial));
        Map<String, Object> mPlace = jdbc.queryForMap(
                "select * from places where allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId",
                        machine.get("allnet_id")));
        Map<String, Object> mRegion0 = jdbc.queryForMap(
                "select * from region0 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region0_id")));
        Map<String, Object> mRegion1 = jdbc.queryForMap(
                "select * from region1 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region1_id")));
        Map<String, Object> mRegion2 = jdbc.queryForMap(
                "select * from region2 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region2_id")));
        Map<String, Object> mRegion3;
        try {
            mRegion3 = jdbc.queryForMap(
                    "select * from region3 where country_code = :countryCode and region_id = :regionId",
                    new MapSqlParameterSource("countryCode", country)
                            .addValue("regionId", mPlace.get("region3_id")));
        } catch (EmptyResultDataAccessException e) {
            mRegion3 = Collections.emptyMap();
        }

        assertEquals(router.get("router_id"),
                data.getRouter().getRouterId());
        assertEquals(((BigDecimal) rPlace.get("allnet_id")).longValue(),
                data.getRouter().getAllnetId().longValue());
        assertEquals(rPlace.get("place_id"),
                data.getRouter().getPlaceId());
        assertEquals(rPlace.get("name"),
                data.getRouter().getPlaceName());
        assertEquals(rPlace.get("bill_code"),
                data.getRouter().getBillCode());
        assertEquals(rPlace.get("nickname"),
                data.getRouter().getPlaceNickName());
        assertEquals(((BigDecimal) rRegion0.get("region_id")).intValue(),
                data.getRouter().getRegion0Id());
        assertEquals(rPlace.get("country_code"),
                data.getRouter().getCountryCode());
        assertEquals(rRegion0.get("name"),
                data.getRouter().getRegion0Name());
        assertEquals(rRegion1.get("name"),
                data.getRouter().getRegion1Name());
        assertEquals(rRegion2.get("name"),
                data.getRouter().getRegion2Name());
        assertEquals(rRegion3.get("name"),
                data.getRouter().getRegion3Name());

        assertEquals(game.get("game_id"), data.getGame().getGameId());
        assertEquals(game.get("game_ver"), data.getGame().getGameVer());
        assertEquals(((BigDecimal) game.get("auth")).intValue(),
                data.getGame().getAuth());
        assertEquals(game.get("uri"), data.getGame().getUri());
        assertEquals(game.get("host"), data.getGame().getHost());

        assertEquals(machine.get("serial"),
                data.getMachine().getSerial());
        assertEquals(machine.get("game_id"),
                data.getMachine().getGameId());
        assertEquals(machine.get("reserved_game_id"),
                data.getMachine().getReservedGameId());
        assertEquals(((BigDecimal) machine.get("setting")).intValue(),
                data.getMachine().getSetting());
        assertEquals(((BigDecimal) mPlace.get("allnet_id")).longValue(),
                data.getMachine().getAllnetId().longValue());
        assertEquals(mPlace.get("place_id"),
                data.getMachine().getPlaceId());
        assertEquals(mPlace.get("name"),
                data.getMachine().getPlaceName());
        assertEquals(mPlace.get("bill_code"),
                data.getMachine().getBillCode());
        assertEquals(mPlace.get("nickname"),
                data.getMachine().getPlaceNickName());
        assertEquals(((BigDecimal) mRegion0.get("region_id")).intValue(),
                data.getMachine().getRegion0Id().intValue());
        assertEquals(mPlace.get("country_code"),
                data.getMachine().getCountryCode());
        assertEquals(mRegion0.get("name"),
                data.getMachine().getRegion0Name());
        assertEquals(mRegion1.get("name"),
                data.getMachine().getRegion1Name());
        assertEquals(mRegion2.get("name"),
                data.getMachine().getRegion2Name());
        assertEquals(mRegion3.get("name"),
                data.getMachine().getRegion3Name());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#find(java.lang.String, java.lang.String, java.lang.String, java.lang.String, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testFindPlaceIpIsNull() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBTY";
        String gameVer = "2.00";
        String serial = "A72E01B5800";
        String ip = null;

        int allnetId = -99999;
        String billCode = "2Z3456789";
        String country = "ZZZ";
        int region0Id = 0;
        int region1Id = 0;
        int region2Id = 0;
        int region3Id = 0;

        // ルータ情報作成
        jdbc.update("DELETE FROM routers", new MapSqlParameterSource());
        jdbc.update("DELETE FROM auth_allowed_places", new MapSqlParameterSource());
        jdbc.update("DELETE FROM places WHERE allnet_id = :allnetId ",
                new MapSqlParameterSource("allnetId", allnetId));
        jdbc.update("DELETE FROM places WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update("DELETE FROM bills WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "DELETE FROM region3 WHERE country_code = :country and region_id = :region3Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "DELETE FROM region2 WHERE country_code = :country and region_id = :region2Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region2Id", region2Id));
        jdbc.update(
                "DELETE FROM region1 WHERE country_code = :country and region_id = :region1Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region1Id", region1Id));
        jdbc.update(
                "DELETE FROM region0 WHERE country_code = :country and region_id = :region0Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region0Id", region0Id));
        jdbc.update("DELETE FROM countries WHERE country_code = :country",
                new MapSqlParameterSource("country", country));

        jdbc.update(
                "INSERT INTO countries (country_code, create_user_id, update_user_id) VALUES (:country, :userId, :userId)",
                new MapSqlParameterSource("country", country).addValue("userId",
                        "TestUser"));
        jdbc.update(
                "INSERT INTO region0 (country_code, region_id, name, create_user_id, update_user_id) VALUES (:country, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域0"));
        jdbc.update(
                "INSERT INTO region1 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region1Id, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region1Id", region1Id)
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域1"));
        jdbc.update(
                "INSERT INTO region2 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region2Id, :region1Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region2Id", region2Id)
                        .addValue("region1Id", region1Id)
                        .addValue("name", "テスト地域2"));
        jdbc.update(
                "INSERT INTO region3 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region3Id, :region2Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region3Id", region3Id)
                        .addValue("region2Id", region2Id)
                        .addValue("name", "テスト地域3"));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser"));
        jdbc.update(
                "INSERT INTO places (allnet_id, place_id, name, tel, address, zip_code, station, open_time, close_time, special_info, bill_code, nickname, country_code, region0_id, region1_id, region2_id, region3_id, create_user_id, update_user_id) "
                        + "VALUES (:allnetId, :placeId, :name, :tel, :address, :zipCode, :station, :openTime, :closeTime, :specialInfo, :billCode, :nickname, :country, :region0Id, :region1Id, :region2Id, :region3Id, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("placeId", "XXXX").addValue("name", "テスト店舗1")
                        .addValue("tel", "1234567890")
                        .addValue("address", "テスト住所")
                        .addValue("zipCode", "1234567")
                        .addValue("station", "テスト駅")
                        .addValue("openTime", "10:00")
                        .addValue("closeTime", "21:00")
                        .addValue("specialInfo", "テストPR文")
                        .addValue("billCode", billCode)
                        .addValue("nickname", "テスト#ニックネーム")
                        .addValue("region0Id", region0Id)
                        .addValue("region1Id", region1Id)
                        .addValue("region2Id", region2Id)
                        .addValue("region3Id", region3Id)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "INSERT INTO routers (router_id, allnet_id, router_type_id, place_ip, create_user_id, update_user_id) VALUES (:routerId, :allnetId, :routerType, :ip, :userId, :userId)",
                new MapSqlParameterSource("routerId", "0000")
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("routerType", 99).addValue("ip", "1.1.1.1"));

        // ゲーム情報の作成
        jdbc.update("DELETE FROM game_attributes WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM download_orders WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM game_competences WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM title_api_accounts WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM games WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));

        jdbc.update(
                "INSERT INTO games (game_id, title, create_user_id, update_user_id) VALUES (:gameId, :title, :userId, :userId)",
                new MapSqlParameterSource("title", "TestGame")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO game_attributes (game_id, game_ver, country_code, title, uri, host, auth, create_user_id, update_user_id) VALUES (:gameId, :gameVer, :country, :title, :uri, :host, :auth, :userId, :userId)",
                new MapSqlParameterSource("title", "TestGame")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId).addValue("gameVer", gameVer)
                        .addValue("country", country)
                        .addValue("uri", "Test/uri")
                        .addValue("host", "Test/host").addValue("auth", 1));

        // 基板情報の作成
        jdbc.update("DELETE FROM machine_statuses WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "DELETE FROM machine_download_orders WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update("DELETE FROM machines WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "INSERT INTO machines (serial, allnet_id, game_id, reserved_game_id, place_id, create_user_id, update_user_id) VALUES (:serial, :allnetId, :gameId, :reservedGameId, :placeId, :userId, :userId)",
                new MapSqlParameterSource("serial", serial)
                        .addValue("allnetId", allnetId)
                        .addValue("reservedGameId", "SBUK")
                        .addValue("placeId", "XXXX")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));

        PowerOnData data = _dao.find(gameId, gameVer, serial, ip);

        Map<String, Object> machine = jdbc.queryForMap(
                "select * from machines where serial = :serial",
                new MapSqlParameterSource("serial", serial));
        Map<String, Object> mPlace = jdbc.queryForMap(
                "select * from places where allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId",
                        machine.get("allnet_id")));
        Map<String, Object> mRegion0 = jdbc.queryForMap(
                "select * from region0 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region0_id")));
        Map<String, Object> mRegion1 = jdbc.queryForMap(
                "select * from region1 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region1_id")));
        Map<String, Object> mRegion2 = jdbc.queryForMap(
                "select * from region2 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region2_id")));
        Map<String, Object> mRegion3;
        try {
            mRegion3 = jdbc.queryForMap(
                    "select * from region3 where country_code = :countryCode and region_id = :regionId",
                    new MapSqlParameterSource("countryCode", country)
                            .addValue("regionId", mPlace.get("region3_id")));
        } catch (EmptyResultDataAccessException e) {
            mRegion3 = Collections.emptyMap();
        }

        assertNull(data.getRouter().getRouterId());
        assertNull(data.getRouter().getAllnetId());
        assertNull(data.getRouter().getPlaceId());
        assertNull(data.getRouter().getPlaceName());
        assertNull(data.getRouter().getBillCode());
        assertNull(data.getRouter().getPlaceNickName());
        assertEquals(0, data.getRouter().getRegion0Id());
        assertNull(data.getRouter().getCountryCode());
        assertNull(data.getRouter().getRegion0Name());
        assertNull(data.getRouter().getRegion1Name());
        assertNull(data.getRouter().getRegion2Name());
        assertNull(data.getRouter().getRegion3Name());

        assertNull(data.getGame().getGameId());
        assertNull(data.getGame().getGameVer());
        assertEquals(0, data.getGame().getAuth());
        assertNull(data.getGame().getUri());
        assertNull(data.getGame().getHost());

        assertEquals(machine.get("serial"),
                data.getMachine().getSerial());
        assertEquals(machine.get("game_id"),
                data.getMachine().getGameId());
        assertEquals(machine.get("reserved_game_id"),
                data.getMachine().getReservedGameId());
        assertEquals(((BigDecimal) machine.get("setting")).intValue(),
                data.getMachine().getSetting());
        assertEquals(((BigDecimal) mPlace.get("allnet_id")).longValue(),
                data.getMachine().getAllnetId().longValue());
        assertEquals(mPlace.get("place_id"),
                data.getMachine().getPlaceId());
        assertEquals(mPlace.get("name"),
                data.getMachine().getPlaceName());
        assertEquals(mPlace.get("bill_code"),
                data.getMachine().getBillCode());
        assertEquals(mPlace.get("nickname"),
                data.getMachine().getPlaceNickName());
        assertEquals(((BigDecimal) mRegion0.get("region_id")).intValue(),
                data.getMachine().getRegion0Id().intValue());
        assertEquals(mPlace.get("country_code"),
                data.getMachine().getCountryCode());
        assertEquals(mRegion0.get("name"),
                data.getMachine().getRegion0Name());
        assertEquals(mRegion1.get("name"),
                data.getMachine().getRegion1Name());
        assertEquals(mRegion2.get("name"),
                data.getMachine().getRegion2Name());
        assertEquals(mRegion3.get("name"),
                data.getMachine().getRegion3Name());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#find(java.lang.String, java.lang.String, java.lang.String, java.lang.String, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testFindNoRouter() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBTY";
        String gameVer = "2.00";
        String serial = "A72E01B5800";
        String ip = "1.1.1.1";

        int allnetId = -99999;
        String billCode = "2Z3456789";
        String country = "ZZZ";
        int region0Id = 0;
        int region1Id = 0;
        int region2Id = 0;
        int region3Id = 0;

        // ルータ情報の削除
        jdbc.update("DELETE FROM routers WHERE place_ip = :ip ",
                new MapSqlParameterSource("ip", ip));

        // 店舗情報の作成
        jdbc.update("DELETE FROM auth_allowed_places", new MapSqlParameterSource());
        jdbc.update("DELETE FROM places WHERE allnet_id = :allnetId ",
                new MapSqlParameterSource("allnetId", allnetId));
        jdbc.update("DELETE FROM places WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update("DELETE FROM bills WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "DELETE FROM region3 WHERE country_code = :country and region_id = :region3Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "DELETE FROM region2 WHERE country_code = :country and region_id = :region2Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region2Id", region2Id));
        jdbc.update(
                "DELETE FROM region1 WHERE country_code = :country and region_id = :region1Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region1Id", region1Id));
        jdbc.update(
                "DELETE FROM region0 WHERE country_code = :country and region_id = :region0Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region0Id", region0Id));
        jdbc.update("DELETE FROM countries WHERE country_code = :country",
                new MapSqlParameterSource("country", country));

        jdbc.update(
                "INSERT INTO countries (country_code, create_user_id, update_user_id) VALUES (:country, :userId, :userId)",
                new MapSqlParameterSource("country", country).addValue("userId",
                        "TestUser"));
        jdbc.update(
                "INSERT INTO region0 (country_code, region_id, name, create_user_id, update_user_id) VALUES (:country, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域0"));
        jdbc.update(
                "INSERT INTO region1 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region1Id, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region1Id", region1Id)
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域1"));
        jdbc.update(
                "INSERT INTO region2 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region2Id, :region1Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region2Id", region2Id)
                        .addValue("region1Id", region1Id)
                        .addValue("name", "テスト地域2"));
        jdbc.update(
                "INSERT INTO region3 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region3Id, :region2Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region3Id", region3Id)
                        .addValue("region2Id", region2Id)
                        .addValue("name", "テスト地域3"));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser"));
        jdbc.update(
                "INSERT INTO places (allnet_id, place_id, name, tel, address, zip_code, station, open_time, close_time, special_info, bill_code, nickname, country_code, region0_id, region1_id, region2_id, region3_id, create_user_id, update_user_id) "
                        + "VALUES (:allnetId, :placeId, :name, :tel, :address, :zipCode, :station, :openTime, :closeTime, :specialInfo, :billCode, :nickname, :country, :region0Id, :region1Id, :region2Id, :region3Id, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("placeId", "XXXX").addValue("name", "テスト店舗1")
                        .addValue("tel", "1234567890")
                        .addValue("address", "テスト住所")
                        .addValue("zipCode", "1234567")
                        .addValue("station", "テスト駅")
                        .addValue("openTime", "10:00")
                        .addValue("closeTime", "21:00")
                        .addValue("specialInfo", "テストPR文")
                        .addValue("billCode", billCode)
                        .addValue("nickname", "テスト#ニックネーム")
                        .addValue("region0Id", region0Id)
                        .addValue("region1Id", region1Id)
                        .addValue("region2Id", region2Id)
                        .addValue("region3Id", region3Id)
                        .addValue("region3Id", region3Id));

        // ゲーム情報の作成
        jdbc.update("DELETE FROM game_attributes WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM download_orders WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM game_competences WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM title_api_accounts WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM games WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));

        jdbc.update(
                "INSERT INTO games (game_id, title, create_user_id, update_user_id) VALUES (:gameId, :title, :userId, :userId)",
                new MapSqlParameterSource("title", "TestGame")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO game_attributes (game_id, game_ver, country_code, title, uri, host, auth, create_user_id, update_user_id) VALUES (:gameId, :gameVer, :country, :title, :uri, :host, :auth, :userId, :userId)",
                new MapSqlParameterSource("title", "TestGame")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId).addValue("gameVer", gameVer)
                        .addValue("country", country)
                        .addValue("uri", "Test/uri")
                        .addValue("host", "Test/host").addValue("auth", 1));

        // 基板情報の作成
        jdbc.update("DELETE FROM machine_statuses WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "DELETE FROM machine_download_orders WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update("DELETE FROM machines WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "INSERT INTO machines (serial, allnet_id, game_id, reserved_game_id, place_id, create_user_id, update_user_id) VALUES (:serial, :allnetId, :gameId, :reservedGameId, :placeId, :userId, :userId)",
                new MapSqlParameterSource("serial", serial)
                        .addValue("allnetId", allnetId)
                        .addValue("reservedGameId", "SBUK")
                        .addValue("placeId", "XXXX")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));

        PowerOnData data = _dao.find(gameId, gameVer, serial, ip);

        Map<String, Object> machine = jdbc.queryForMap(
                "select * from machines where serial = :serial",
                new MapSqlParameterSource("serial", serial));
        Map<String, Object> mPlace = jdbc.queryForMap(
                "select * from places where allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId",
                        machine.get("allnet_id")));
        Map<String, Object> mRegion0 = jdbc.queryForMap(
                "select * from region0 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region0_id")));
        Map<String, Object> mRegion1 = jdbc.queryForMap(
                "select * from region1 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region1_id")));
        Map<String, Object> mRegion2 = jdbc.queryForMap(
                "select * from region2 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region2_id")));
        Map<String, Object> mRegion3;
        try {
            mRegion3 = jdbc.queryForMap(
                    "select * from region3 where country_code = :countryCode and region_id = :regionId",
                    new MapSqlParameterSource("countryCode", country)
                            .addValue("regionId", mPlace.get("region3_id")));
        } catch (EmptyResultDataAccessException e) {
            mRegion3 = Collections.emptyMap();
        }

        assertNull(data.getRouter().getRouterId());
        assertNull(data.getRouter().getAllnetId());
        assertNull(data.getRouter().getPlaceId());
        assertNull(data.getRouter().getPlaceName());
        assertNull(data.getRouter().getBillCode());
        assertNull(data.getRouter().getPlaceNickName());
        assertEquals(0, data.getRouter().getRegion0Id());
        assertNull(data.getRouter().getCountryCode());
        assertNull(data.getRouter().getRegion0Name());
        assertNull(data.getRouter().getRegion1Name());
        assertNull(data.getRouter().getRegion2Name());
        assertNull(data.getRouter().getRegion3Name());

        assertNull(data.getGame().getGameId());
        assertNull(data.getGame().getGameVer());
        assertEquals(0, data.getGame().getAuth());
        assertNull(data.getGame().getUri());
        assertNull(data.getGame().getHost());

        assertEquals(machine.get("serial"),
                data.getMachine().getSerial());
        assertEquals(machine.get("game_id"),
                data.getMachine().getGameId());
        assertEquals(machine.get("reserved_game_id"),
                data.getMachine().getReservedGameId());
        assertEquals(((BigDecimal) machine.get("setting")).intValue(),
                data.getMachine().getSetting());
        assertEquals(((BigDecimal) mPlace.get("allnet_id")).longValue(),
                data.getMachine().getAllnetId().longValue());
        assertEquals(mPlace.get("place_id"),
                data.getMachine().getPlaceId());
        assertEquals(mPlace.get("name"),
                data.getMachine().getPlaceName());
        assertEquals(mPlace.get("bill_code"),
                data.getMachine().getBillCode());
        assertEquals(mPlace.get("nickname"),
                data.getMachine().getPlaceNickName());
        assertEquals(((BigDecimal) mRegion0.get("region_id")).intValue(),
                data.getMachine().getRegion0Id().intValue());
        assertEquals(mPlace.get("country_code"),
                data.getMachine().getCountryCode());
        assertEquals(mRegion0.get("name"),
                data.getMachine().getRegion0Name());
        assertEquals(mRegion1.get("name"),
                data.getMachine().getRegion1Name());
        assertEquals(mRegion2.get("name"),
                data.getMachine().getRegion2Name());
        assertEquals(mRegion3.get("name"),
                data.getMachine().getRegion3Name());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#find(java.lang.String, java.lang.String, java.lang.String, java.lang.String, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testFindNoGame() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBTY";
        String gameVer = "99.999";
        String serial = "A72E01B5800";
        String ip = "10.11.169.254";

        int allnetId = -99999;
        String billCode = "2Z3456789";
        String country = "ZZZ";
        int region0Id = 0;
        int region1Id = 0;
        int region2Id = 0;
        int region3Id = 0;

        // ルータ情報作成
        jdbc.update("DELETE FROM routers", new MapSqlParameterSource());
        jdbc.update("DELETE FROM auth_allowed_places", new MapSqlParameterSource());
        jdbc.update("DELETE FROM places WHERE allnet_id = :allnetId ",
                new MapSqlParameterSource("allnetId", allnetId));
        jdbc.update("DELETE FROM places WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update("DELETE FROM bills WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "DELETE FROM region3 WHERE country_code = :country and region_id = :region3Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "DELETE FROM region2 WHERE country_code = :country and region_id = :region2Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region2Id", region2Id));
        jdbc.update(
                "DELETE FROM region1 WHERE country_code = :country and region_id = :region1Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region1Id", region1Id));
        jdbc.update(
                "DELETE FROM region0 WHERE country_code = :country and region_id = :region0Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region0Id", region0Id));
        jdbc.update("DELETE FROM countries WHERE country_code = :country",
                new MapSqlParameterSource("country", country));

        jdbc.update(
                "INSERT INTO countries (country_code, create_user_id, update_user_id) VALUES (:country, :userId, :userId)",
                new MapSqlParameterSource("country", country).addValue("userId",
                        "TestUser"));
        jdbc.update(
                "INSERT INTO region0 (country_code, region_id, name, create_user_id, update_user_id) VALUES (:country, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域0"));
        jdbc.update(
                "INSERT INTO region1 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region1Id, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region1Id", region1Id)
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域1"));
        jdbc.update(
                "INSERT INTO region2 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region2Id, :region1Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region2Id", region2Id)
                        .addValue("region1Id", region1Id)
                        .addValue("name", "テスト地域2"));
        jdbc.update(
                "INSERT INTO region3 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region3Id, :region2Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region3Id", region3Id)
                        .addValue("region2Id", region2Id)
                        .addValue("name", "テスト地域3"));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser"));
        jdbc.update(
                "INSERT INTO places (allnet_id, place_id, name, tel, address, zip_code, station, open_time, close_time, special_info, bill_code, nickname, country_code, region0_id, region1_id, region2_id, region3_id, create_user_id, update_user_id) "
                        + "VALUES (:allnetId, :placeId, :name, :tel, :address, :zipCode, :station, :openTime, :closeTime, :specialInfo, :billCode, :nickname, :country, :region0Id, :region1Id, :region2Id, :region3Id, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("placeId", "XXXX").addValue("name", "テスト店舗1")
                        .addValue("tel", "1234567890")
                        .addValue("address", "テスト住所")
                        .addValue("zipCode", "1234567")
                        .addValue("station", "テスト駅")
                        .addValue("openTime", "10:00")
                        .addValue("closeTime", "21:00")
                        .addValue("specialInfo", "テストPR文")
                        .addValue("billCode", billCode)
                        .addValue("nickname", "テスト#ニックネーム")
                        .addValue("region0Id", region0Id)
                        .addValue("region1Id", region1Id)
                        .addValue("region2Id", region2Id)
                        .addValue("region3Id", region3Id)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "INSERT INTO routers (router_id, allnet_id, router_type_id, place_ip, create_user_id, update_user_id) VALUES (:routerId, :allnetId, :routerType, :ip, :userId, :userId)",
                new MapSqlParameterSource("routerId", "0000")
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("routerType", 99).addValue("ip", ip));

        // ゲーム情報の削除
        jdbc.update("DELETE FROM game_attributes WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM download_orders WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM game_competences WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM title_api_accounts WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM games WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));

        // 基板情報の作成
        jdbc.update("DELETE FROM machine_statuses WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "DELETE FROM machine_download_orders WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update("DELETE FROM machines WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "INSERT INTO machines (serial, allnet_id, game_id, reserved_game_id, place_id, create_user_id, update_user_id) VALUES (:serial, :allnetId, :gameId, :reservedGameId, :placeId, :userId, :userId)",
                new MapSqlParameterSource("serial", serial)
                        .addValue("allnetId", allnetId)
                        .addValue("reservedGameId", "SBUK")
                        .addValue("placeId", "XXXX")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));

        PowerOnData data = _dao.find(gameId, gameVer, serial, ip);

        Map<String, Object> router = jdbc.queryForMap(
                "select * from routers where place_ip = :placeIp",
                new MapSqlParameterSource("placeIp", ip));
        Map<String, Object> rPlace = jdbc.queryForMap(
                "select * from places where allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId", router.get("allnet_id")));
        Map<String, Object> rRegion0 = jdbc.queryForMap(
                "select * from region0 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region0_id")));
        Map<String, Object> rRegion1 = jdbc.queryForMap(
                "select * from region1 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region1_id")));
        Map<String, Object> rRegion2 = jdbc.queryForMap(
                "select * from region2 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region2_id")));

        Map<String, Object> rRegion3;
        try {
            rRegion3 = jdbc.queryForMap(
                    "select * from region3 where country_code = :countryCode and region_id = :regionId",
                    new MapSqlParameterSource("countryCode", country)
                            .addValue("regionId", rPlace.get("region3_id")));
        } catch (EmptyResultDataAccessException e) {
            rRegion3 = Collections.emptyMap();
        }

        Map<String, Object> machine = jdbc.queryForMap(
                "select * from machines where serial = :serial",
                new MapSqlParameterSource("serial", serial));
        Map<String, Object> mPlace = jdbc.queryForMap(
                "select * from places where allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId",
                        machine.get("allnet_id")));
        Map<String, Object> mRegion0 = jdbc.queryForMap(
                "select * from region0 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region0_id")));
        Map<String, Object> mRegion1 = jdbc.queryForMap(
                "select * from region1 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region1_id")));
        Map<String, Object> mRegion2 = jdbc.queryForMap(
                "select * from region2 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region2_id")));
        Map<String, Object> mRegion3;
        try {
            mRegion3 = jdbc.queryForMap(
                    "select * from region3 where country_code = :countryCode and region_id = :regionId",
                    new MapSqlParameterSource("countryCode", country)
                            .addValue("regionId", mPlace.get("region3_id")));
        } catch (EmptyResultDataAccessException e) {
            mRegion3 = Collections.emptyMap();
        }

        assertEquals(router.get("router_id"),
                data.getRouter().getRouterId());
        assertEquals(((BigDecimal) rPlace.get("allnet_id")).longValue(),
                data.getRouter().getAllnetId().longValue());
        assertEquals(rPlace.get("place_id"),
                data.getRouter().getPlaceId());
        assertEquals(rPlace.get("name"),
                data.getRouter().getPlaceName());
        assertEquals(rPlace.get("bill_code"),
                data.getRouter().getBillCode());
        assertEquals(rPlace.get("nickname"),
                data.getRouter().getPlaceNickName());
        assertEquals(((BigDecimal) rRegion0.get("region_id")).intValue(),
                data.getRouter().getRegion0Id());
        assertEquals(rPlace.get("country_code"),
                data.getRouter().getCountryCode());
        assertEquals(rRegion0.get("name"),
                data.getRouter().getRegion0Name());
        assertEquals(rRegion1.get("name"),
                data.getRouter().getRegion1Name());
        assertEquals(rRegion2.get("name"),
                data.getRouter().getRegion2Name());
        assertEquals(rRegion3.get("name"),
                data.getRouter().getRegion3Name());

        assertNull(data.getGame().getGameId());
        assertNull(data.getGame().getGameVer());
        assertEquals(0, data.getGame().getAuth());
        assertNull(data.getGame().getUri());
        assertNull(data.getGame().getHost());

        assertEquals(machine.get("serial"),
                data.getMachine().getSerial());
        assertEquals(machine.get("game_id"),
                data.getMachine().getGameId());
        assertEquals(machine.get("reserved_game_id"),
                data.getMachine().getReservedGameId());
        assertEquals(((BigDecimal) machine.get("setting")).intValue(),
                data.getMachine().getSetting());
        assertEquals(((BigDecimal) mPlace.get("allnet_id")).intValue(),
                data.getMachine().getAllnetId().intValue());
        assertEquals(mPlace.get("place_id"),
                data.getMachine().getPlaceId());
        assertEquals(mPlace.get("name"),
                data.getMachine().getPlaceName());
        assertEquals(mPlace.get("bill_code"),
                data.getMachine().getBillCode());
        assertEquals(mPlace.get("nickname"),
                data.getMachine().getPlaceNickName());
        assertEquals(((BigDecimal) mRegion0.get("region_id")).intValue(),
                data.getMachine().getRegion0Id().intValue());
        assertEquals(mPlace.get("country_code"),
                data.getMachine().getCountryCode());
        assertEquals(mRegion0.get("name"),
                data.getMachine().getRegion0Name());
        assertEquals(mRegion1.get("name"),
                data.getMachine().getRegion1Name());
        assertEquals(mRegion2.get("name"),
                data.getMachine().getRegion2Name());
        assertEquals(mRegion3.get("name"),
                data.getMachine().getRegion3Name());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#find(java.lang.String, java.lang.String, java.lang.String, java.lang.String, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testFindNoMachine() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBTY";
        String gameVer = "2.00";
        String serial = "A72E01B580";
        String ip = "1.1.1.254";

        int allnetId = -99999;
        String billCode = "2Z3456789";
        String country = "ZZZ";
        int region0Id = 0;
        int region1Id = 0;
        int region2Id = 0;
        int region3Id = 0;

        // ルータ情報作成
        jdbc.update("DELETE FROM routers", new MapSqlParameterSource());
        jdbc.update("DELETE FROM auth_allowed_places", new MapSqlParameterSource());
        jdbc.update("DELETE FROM places WHERE allnet_id = :allnetId ",
                new MapSqlParameterSource("allnetId", allnetId));
        jdbc.update("DELETE FROM places WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update("DELETE FROM bills WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "DELETE FROM region3 WHERE country_code = :country and region_id = :region3Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "DELETE FROM region2 WHERE country_code = :country and region_id = :region2Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region2Id", region2Id));
        jdbc.update(
                "DELETE FROM region1 WHERE country_code = :country and region_id = :region1Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region1Id", region1Id));
        jdbc.update(
                "DELETE FROM region0 WHERE country_code = :country and region_id = :region0Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region0Id", region0Id));
        jdbc.update("DELETE FROM countries WHERE country_code = :country",
                new MapSqlParameterSource("country", country));

        jdbc.update(
                "INSERT INTO countries (country_code, create_user_id, update_user_id) VALUES (:country, :userId, :userId)",
                new MapSqlParameterSource("country", country).addValue("userId",
                        "TestUser"));
        jdbc.update(
                "INSERT INTO region0 (country_code, region_id, name, create_user_id, update_user_id) VALUES (:country, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域0"));
        jdbc.update(
                "INSERT INTO region1 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region1Id, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region1Id", region1Id)
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域1"));
        jdbc.update(
                "INSERT INTO region2 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region2Id, :region1Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region2Id", region2Id)
                        .addValue("region1Id", region1Id)
                        .addValue("name", "テスト地域2"));
        jdbc.update(
                "INSERT INTO region3 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region3Id, :region2Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region3Id", region3Id)
                        .addValue("region2Id", region2Id)
                        .addValue("name", "テスト地域3"));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser"));
        jdbc.update(
                "INSERT INTO places (allnet_id, place_id, name, tel, address, zip_code, station, open_time, close_time, special_info, bill_code, nickname, country_code, region0_id, region1_id, region2_id, region3_id, create_user_id, update_user_id) "
                        + "VALUES (:allnetId, :placeId, :name, :tel, :address, :zipCode, :station, :openTime, :closeTime, :specialInfo, :billCode, :nickname, :country, :region0Id, :region1Id, :region2Id, :region3Id, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("placeId", "XXXX").addValue("name", "テスト店舗1")
                        .addValue("tel", "1234567890")
                        .addValue("address", "テスト住所")
                        .addValue("zipCode", "1234567")
                        .addValue("station", "テスト駅")
                        .addValue("openTime", "10:00")
                        .addValue("closeTime", "21:00")
                        .addValue("specialInfo", "テストPR文")
                        .addValue("billCode", billCode)
                        .addValue("nickname", "テスト#ニックネーム")
                        .addValue("region0Id", region0Id)
                        .addValue("region1Id", region1Id)
                        .addValue("region2Id", region2Id)
                        .addValue("region3Id", region3Id)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "INSERT INTO routers (router_id, allnet_id, router_type_id, place_ip, create_user_id, update_user_id) VALUES (:routerId, :allnetId, :routerType, :ip, :userId, :userId)",
                new MapSqlParameterSource("routerId", "0000")
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("routerType", 99).addValue("ip", ip));

        // ゲーム情報の作成
        jdbc.update("DELETE FROM game_attributes WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM download_orders WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM game_competences WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM title_api_accounts WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM games WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));

        jdbc.update(
                "INSERT INTO games (game_id, title, create_user_id, update_user_id) VALUES (:gameId, :title, :userId, :userId)",
                new MapSqlParameterSource("title", "TestGame")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO game_attributes (game_id, game_ver, country_code, title, uri, host, auth, create_user_id, update_user_id) VALUES (:gameId, :gameVer, :country, :title, :uri, :host, :auth, :userId, :userId)",
                new MapSqlParameterSource("title", "TestGame")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId).addValue("gameVer", gameVer)
                        .addValue("country", country)
                        .addValue("uri", "Test/uri")
                        .addValue("host", "Test/host").addValue("auth", 1));

        // 基板情報の削除
        jdbc.update("DELETE FROM machine_statuses WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "DELETE FROM machine_download_orders WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update("DELETE FROM machines WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));

        PowerOnData data = _dao.find(gameId, gameVer, serial, ip);

        Map<String, Object> router = jdbc.queryForMap(
                "select * from routers where place_ip = :placeIp",
                new MapSqlParameterSource("placeIp", ip));
        Map<String, Object> rPlace = jdbc.queryForMap(
                "select * from places where allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId", router.get("allnet_id")));
        Map<String, Object> rRegion0 = jdbc.queryForMap(
                "select * from region0 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region0_id")));
        Map<String, Object> rRegion1 = jdbc.queryForMap(
                "select * from region1 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region1_id")));
        Map<String, Object> rRegion2 = jdbc.queryForMap(
                "select * from region2 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region2_id")));

        Map<String, Object> game = jdbc.queryForMap(
                "select * from game_attributes where game_id = :gameId and game_ver = :gameVer and country_code = :countryCode",
                new MapSqlParameterSource().addValue("gameId", gameId)
                        .addValue("gameVer", gameVer)
                        .addValue("countryCode", country));

        Map<String, Object> rRegion3;
        try {
            rRegion3 = jdbc.queryForMap(
                    "select * from region3 where country_code = :countryCode and region_id = :regionId",
                    new MapSqlParameterSource("countryCode", country)
                            .addValue("regionId", rPlace.get("region3_id")));
        } catch (EmptyResultDataAccessException e) {
            rRegion3 = Collections.emptyMap();
        }

        assertEquals(router.get("router_id"),
                data.getRouter().getRouterId());
        assertEquals(((BigDecimal) rPlace.get("allnet_id")).longValue(),
                data.getRouter().getAllnetId().longValue());
        assertEquals(rPlace.get("place_id"),
                data.getRouter().getPlaceId());
        assertEquals(rPlace.get("name"),
                data.getRouter().getPlaceName());
        assertEquals(rPlace.get("bill_code"),
                data.getRouter().getBillCode());
        assertEquals(rPlace.get("nickname"),
                data.getRouter().getPlaceNickName());
        assertEquals(((BigDecimal) rRegion0.get("region_id")).intValue(),
                data.getRouter().getRegion0Id());
        assertEquals(rPlace.get("country_code"),
                data.getRouter().getCountryCode());
        assertEquals(rRegion0.get("name"),
                data.getRouter().getRegion0Name());
        assertEquals(rRegion1.get("name"),
                data.getRouter().getRegion1Name());
        assertEquals(rRegion2.get("name"),
                data.getRouter().getRegion2Name());
        assertEquals(rRegion3.get("name"),
                data.getRouter().getRegion3Name());

        assertEquals(game.get("game_id"), data.getGame().getGameId());
        assertEquals(game.get("game_ver"), data.getGame().getGameVer());
        assertEquals(((BigDecimal) game.get("auth")).intValue(),
                data.getGame().getAuth());
        assertEquals(game.get("uri"), data.getGame().getUri());
        assertEquals(game.get("host"), data.getGame().getHost());

        assertNull(data.getMachine().getSerial());
        assertNull(data.getMachine().getGameId());
        assertNull(data.getMachine().getReservedGameId());
        assertEquals(0, data.getMachine().getSetting());
        assertNull(data.getMachine().getAllnetId());
        assertNull(data.getMachine().getPlaceId());
        assertNull(data.getMachine().getPlaceName());
        assertNull(data.getMachine().getPlaceNickName());
        assertNull(data.getMachine().getRegion0Id());
        assertNull(data.getMachine().getCountryCode());
        assertNull(data.getMachine().getRegion0Name());
        assertNull(data.getMachine().getRegion1Name());
        assertNull(data.getMachine().getRegion2Name());
        assertNull(data.getMachine().getRegion3Name());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#find(java.lang.String, java.lang.String, java.lang.String, java.lang.String, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testFindExistMachineStatus() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBTY";
        String gameVer = "2.00";
        String serial = "A72E01B5800";
        String ip = "1.1.1.254";

        int allnetId = -99999;
        String billCode = "2Z3456789";
        String country = "ZZZ";
        int region0Id = 0;
        int region1Id = 0;
        int region2Id = 0;
        int region3Id = 0;

        // ルータ情報作成
        jdbc.update("DELETE FROM routers", new MapSqlParameterSource());
        jdbc.update("DELETE FROM auth_allowed_places", new MapSqlParameterSource());
        jdbc.update("DELETE FROM places WHERE allnet_id = :allnetId ",
                new MapSqlParameterSource("allnetId", allnetId));
        jdbc.update("DELETE FROM places WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update("DELETE FROM bills WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "DELETE FROM region3 WHERE country_code = :country and region_id = :region3Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "DELETE FROM region2 WHERE country_code = :country and region_id = :region2Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region2Id", region2Id));
        jdbc.update(
                "DELETE FROM region1 WHERE country_code = :country and region_id = :region1Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region1Id", region1Id));
        jdbc.update(
                "DELETE FROM region0 WHERE country_code = :country and region_id = :region0Id ",
                new MapSqlParameterSource("country", country)
                        .addValue("region0Id", region0Id));
        jdbc.update("DELETE FROM countries WHERE country_code = :country",
                new MapSqlParameterSource("country", country));

        jdbc.update(
                "INSERT INTO countries (country_code, create_user_id, update_user_id) VALUES (:country, :userId, :userId)",
                new MapSqlParameterSource("country", country).addValue("userId",
                        "TestUser"));
        jdbc.update(
                "INSERT INTO region0 (country_code, region_id, name, create_user_id, update_user_id) VALUES (:country, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域0"));
        jdbc.update(
                "INSERT INTO region1 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region1Id, :region0Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region1Id", region1Id)
                        .addValue("region0Id", region0Id)
                        .addValue("name", "テスト地域1"));
        jdbc.update(
                "INSERT INTO region2 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region2Id, :region1Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region2Id", region2Id)
                        .addValue("region1Id", region1Id)
                        .addValue("name", "テスト地域2"));
        jdbc.update(
                "INSERT INTO region3 (country_code, region_id, parent_region_id, name, create_user_id, update_user_id) VALUES (:country, :region3Id, :region2Id, :name, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("region3Id", region3Id)
                        .addValue("region2Id", region2Id)
                        .addValue("name", "テスト地域3"));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser"));
        jdbc.update(
                "INSERT INTO places (allnet_id, place_id, name, tel, address, zip_code, station, open_time, close_time, special_info, bill_code, nickname, country_code, region0_id, region1_id, region2_id, region3_id, create_user_id, update_user_id) "
                        + "VALUES (:allnetId, :placeId, :name, :tel, :address, :zipCode, :station, :openTime, :closeTime, :specialInfo, :billCode, :nickname, :country, :region0Id, :region1Id, :region2Id, :region3Id, :userId, :userId)",
                new MapSqlParameterSource("country", country)
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("placeId", "XXXX").addValue("name", "テスト店舗1")
                        .addValue("tel", "1234567890")
                        .addValue("address", "テスト住所")
                        .addValue("zipCode", "1234567")
                        .addValue("station", "テスト駅")
                        .addValue("openTime", "10:00")
                        .addValue("closeTime", "21:00")
                        .addValue("specialInfo", "テストPR文")
                        .addValue("billCode", billCode)
                        .addValue("nickname", "テスト#ニックネーム")
                        .addValue("region0Id", region0Id)
                        .addValue("region1Id", region1Id)
                        .addValue("region2Id", region2Id)
                        .addValue("region3Id", region3Id)
                        .addValue("region3Id", region3Id));
        jdbc.update(
                "INSERT INTO routers (router_id, allnet_id, router_type_id, place_ip, create_user_id, update_user_id) VALUES (:routerId, :allnetId, :routerType, :ip, :userId, :userId)",
                new MapSqlParameterSource("routerId", "0000")
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("routerType", 99).addValue("ip", ip));

        // ゲーム情報の作成
        jdbc.update("DELETE FROM game_attributes WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM download_orders WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM game_competences WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM title_api_accounts WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update("DELETE FROM games WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));

        jdbc.update(
                "INSERT INTO games (game_id, title, create_user_id, update_user_id) VALUES (:gameId, :title, :userId, :userId)",
                new MapSqlParameterSource("title", "TestGame")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO game_attributes (game_id, game_ver, country_code, title, uri, host, auth, create_user_id, update_user_id) VALUES (:gameId, :gameVer, :country, :title, :uri, :host, :auth, :userId, :userId)",
                new MapSqlParameterSource("title", "TestGame")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId).addValue("gameVer", gameVer)
                        .addValue("country", country)
                        .addValue("uri", "Test/uri")
                        .addValue("host", "Test/host").addValue("auth", 1));

        // 基板情報の作成
        jdbc.update("DELETE FROM machine_statuses WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "DELETE FROM machine_download_orders WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update("DELETE FROM machines WHERE serial = :serial ",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "INSERT INTO machines (serial, allnet_id, game_id, reserved_game_id, place_id, create_user_id, update_user_id) VALUES (:serial, :allnetId, :gameId, :reservedGameId, :placeId, :userId, :userId)",
                new MapSqlParameterSource("serial", serial)
                        .addValue("allnetId", allnetId)
                        .addValue("reservedGameId", "SBUK")
                        .addValue("placeId", "XXXX")
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO machine_statuses (serial, create_user_id, update_user_id) VALUES (:serial, :userId, :userId)",
                new MapSqlParameterSource("serial", serial).addValue("userId",
                        "TestUser"));

        PowerOnData data = _dao.find(gameId, gameVer, serial, ip);

        Map<String, Object> router = jdbc.queryForMap(
                "select * from routers where place_ip = :placeIp",
                new MapSqlParameterSource("placeIp", ip));
        Map<String, Object> rPlace = jdbc.queryForMap(
                "select * from places where allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId", router.get("allnet_id")));
        Map<String, Object> rRegion0 = jdbc.queryForMap(
                "select * from region0 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region0_id")));
        Map<String, Object> rRegion1 = jdbc.queryForMap(
                "select * from region1 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region1_id")));
        Map<String, Object> rRegion2 = jdbc.queryForMap(
                "select * from region2 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", rPlace.get("region2_id")));

        Map<String, Object> rRegion3;
        try {
            rRegion3 = jdbc.queryForMap(
                    "select * from region3 where country_code = :countryCode and region_id = :regionId",
                    new MapSqlParameterSource("countryCode", country)
                            .addValue("regionId", rPlace.get("region3_id")));
        } catch (EmptyResultDataAccessException e) {
            rRegion3 = Collections.emptyMap();
        }
        Map<String, Object> game = jdbc.queryForMap(
                "select * from game_attributes where game_id = :gameId and game_ver = :gameVer and country_code = :countryCode",
                new MapSqlParameterSource().addValue("gameId", gameId)
                        .addValue("gameVer", gameVer)
                        .addValue("countryCode", country));
        Map<String, Object> machine = jdbc.queryForMap(
                "select * from machines where serial = :serial",
                new MapSqlParameterSource("serial", serial));
        Map<String, Object> mPlace = jdbc.queryForMap(
                "select * from places where allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId",
                        machine.get("allnet_id")));
        Map<String, Object> mRegion0 = jdbc.queryForMap(
                "select * from region0 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region0_id")));
        Map<String, Object> mRegion1 = jdbc.queryForMap(
                "select * from region1 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region1_id")));
        Map<String, Object> mRegion2 = jdbc.queryForMap(
                "select * from region2 where country_code = :countryCode and region_id = :regionId",
                new MapSqlParameterSource("countryCode", country)
                        .addValue("regionId", mPlace.get("region2_id")));
        Map<String, Object> mRegion3;
        try {
            mRegion3 = jdbc.queryForMap(
                    "select * from region3 where country_code = :countryCode and region_id = :regionId",
                    new MapSqlParameterSource("countryCode", country)
                            .addValue("regionId", mPlace.get("region3_id")));
        } catch (EmptyResultDataAccessException e) {
            mRegion3 = Collections.emptyMap();
        }

        assertEquals(router.get("router_id"),
                data.getRouter().getRouterId());
        assertEquals(((BigDecimal) rPlace.get("allnet_id")).longValue(),
                data.getRouter().getAllnetId().longValue());
        assertEquals(rPlace.get("place_id"),
                data.getRouter().getPlaceId());
        assertEquals(rPlace.get("name"),
                data.getRouter().getPlaceName());
        assertEquals(rPlace.get("bill_code"),
                data.getRouter().getBillCode());
        assertEquals(rPlace.get("nickname"),
                data.getRouter().getPlaceNickName());
        assertEquals(((BigDecimal) rRegion0.get("region_id")).intValue(),
                data.getRouter().getRegion0Id());
        assertEquals(rPlace.get("country_code"),
                data.getRouter().getCountryCode());
        assertEquals(rRegion0.get("name"),
                data.getRouter().getRegion0Name());
        assertEquals(rRegion1.get("name"),
                data.getRouter().getRegion1Name());
        assertEquals(rRegion2.get("name"),
                data.getRouter().getRegion2Name());
        assertEquals(rRegion3.get("name"),
                data.getRouter().getRegion3Name());

        assertEquals(game.get("game_id"), data.getGame().getGameId());
        assertEquals(game.get("game_ver"), data.getGame().getGameVer());
        assertEquals(((BigDecimal) game.get("auth")).intValue(),
                data.getGame().getAuth());
        assertEquals(game.get("uri"), data.getGame().getUri());
        assertEquals(game.get("host"), data.getGame().getHost());

        assertEquals(machine.get("serial"),
                data.getMachine().getSerial());
        assertEquals(machine.get("game_id"),
                data.getMachine().getGameId());
        assertEquals(machine.get("reserved_game_id"),
                data.getMachine().getReservedGameId());
        assertEquals(((BigDecimal) machine.get("setting")).intValue(),
                data.getMachine().getSetting());
        assertEquals(((BigDecimal) mPlace.get("allnet_id")).longValue(),
                data.getMachine().getAllnetId().longValue());
        assertEquals(mPlace.get("place_id"),
                data.getMachine().getPlaceId());
        assertEquals(mPlace.get("name"),
                data.getMachine().getPlaceName());
        assertEquals(mPlace.get("bill_code"),
                data.getMachine().getBillCode());
        assertEquals(mPlace.get("nickname"),
                data.getMachine().getPlaceNickName());
        assertEquals(((BigDecimal) mRegion0.get("region_id")).intValue(),
                data.getMachine().getRegion0Id().intValue());
        assertEquals(mPlace.get("country_code"),
                data.getMachine().getCountryCode());
        assertEquals(mRegion0.get("name"),
                data.getMachine().getRegion0Name());
        assertEquals(mRegion1.get("name"),
                data.getMachine().getRegion1Name());
        assertEquals(mRegion2.get("name"),
                data.getMachine().getRegion2Name());
        assertEquals(mRegion3.get("name"),
                data.getMachine().getRegion3Name());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkAuthDenied(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckAuthDeniedSuccess() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String compCode = "3X9999";
        String billCode = "3X9999999";

        jdbc.update(
                "DELETE FROM auth_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "DELETE FROM auth_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "DELETE FROM auth_denied_bills b WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "DELETE FROM auth_denied_game_bills gc WHERE bill_code = :billCode AND game_id = :gameId",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("gameId", gameId));

        boolean b = _dao.checkAuthDenied(gameId, compCode, billCode);

        assertFalse(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkAuthDenied(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckAuthDeniedComps() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String compCode = "3X9999";
        String billCode = "3X9999999";

        jdbc.update("DELETE FROM comps WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "INSERT INTO comps (comp_code, name, create_user_id, update_user_id) VALUES (:compCode, :name, :userId, :userId)",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("userId", "TestUser")
                        .addValue("name", "TestComp"));

        jdbc.update(
                "DELETE FROM auth_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "INSERT INTO auth_denied_comps (comp_code, create_user_id) VALUES (:compCode, :userId)",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("userId", "TestUser"));

        jdbc.update(
                "DELETE FROM auth_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "DELETE FROM auth_denied_bills b WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "DELETE FROM auth_denied_game_bills gc WHERE bill_code = :billCode AND game_id = :gameId",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("gameId", gameId));

        boolean b = _dao.checkAuthDenied(gameId, compCode, billCode);

        assertTrue(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkAuthDenied(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckAuthDeniedGameComps() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String compCode = "3X9999";
        String billCode = "3X9999999";

        if (jdbc.queryForObject("SELECT COUNT(1) FROM games WHERE game_id = :game_id",
        		new MapSqlParameterSource("game_id", gameId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games (game_id, title, create_user_id, update_user_id) "
                            + "VALUES (:gameId, :title, :userId, :userId)",
                    new MapSqlParameterSource("title", "TestGame")
                            .addValue("userId", "TestUser")
                            .addValue("gameId", gameId));
        }

        jdbc.update("DELETE FROM comps WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "INSERT INTO comps (comp_code, name, create_user_id, update_user_id) VALUES (:compCode, :name, :userId, :userId)",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("userId", "TestUser")
                        .addValue("name", "TestComp"));

        jdbc.update(
                "DELETE FROM auth_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "DELETE FROM auth_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO auth_denied_game_comps (game_id, comp_code, create_user_id) VALUES (:gameId, :compCode, :userId)",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));
        jdbc.update(
                "DELETE FROM auth_denied_bills b WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "DELETE FROM auth_denied_game_bills gc WHERE bill_code = :billCode AND game_id = :gameId",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("gameId", gameId));

        boolean b = _dao.checkAuthDenied(gameId, compCode, billCode);

        assertTrue(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkAuthDenied(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckAuthDeniedBills() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String compCode = "3X9999";
        String billCode = "3X9999999";

        jdbc.update("DELETE FROM bills WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser"));

        jdbc.update(
                "DELETE FROM auth_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "DELETE FROM auth_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "DELETE FROM auth_denied_bills b WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "INSERT INTO auth_denied_bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser"));
        jdbc.update(
                "DELETE FROM auth_denied_game_bills gc WHERE bill_code = :billCode AND game_id = :gameId",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("gameId", gameId));

        boolean b = _dao.checkAuthDenied(gameId, compCode, billCode);

        assertTrue(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkAuthDenied(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckAuthDeniedGameBills() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String compCode = "3X9999";
        String billCode = "3X9999999";

        if (jdbc.queryForObject("SELECT COUNT(1) FROM games WHERE game_id = :game_id",
        		new MapSqlParameterSource("game_id", gameId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games (game_id, title, create_user_id, update_user_id) "
                            + "VALUES (:gameId, :title, :userId, :userId)",
                    new MapSqlParameterSource("title", "TestGame")
                            .addValue("userId", "TestUser")
                            .addValue("gameId", gameId));
        }

        jdbc.update("DELETE FROM bills WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser"));

        jdbc.update(
                "DELETE FROM auth_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "DELETE FROM auth_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "DELETE FROM auth_denied_bills b WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "DELETE FROM auth_denied_game_bills gc WHERE bill_code = :billCode AND game_id = :gameId",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO auth_denied_game_bills (game_id, bill_code, create_user_id) VALUES (:gameId, :billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));

        boolean b = _dao.checkAuthDenied(gameId, compCode, billCode);

        assertTrue(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkAuthDenied(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckAuthDeniedMulti() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String compCode = "3X9999";
        String billCode = "3X9999999";

        if (jdbc.queryForObject("SELECT COUNT(1) FROM games WHERE game_id = :game_id",
        		new MapSqlParameterSource("game_id", gameId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games (game_id, title, create_user_id, update_user_id) "
                            + "VALUES (:gameId, :title, :userId, :userId)",
                    new MapSqlParameterSource("title", "TestGame")
                            .addValue("userId", "TestUser")
                            .addValue("gameId", gameId));
        }

        jdbc.update("DELETE FROM comps WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "INSERT INTO comps (comp_code, name, create_user_id, update_user_id) VALUES (:compCode, :name, :userId, :userId)",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("userId", "TestUser")
                        .addValue("name", "TestComp"));

        jdbc.update("DELETE FROM bills WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser"));

        jdbc.update(
                "DELETE FROM auth_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "INSERT INTO auth_denied_comps (comp_code, create_user_id) VALUES (:compCode, :userId)",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("userId", "TestUser"));

        jdbc.update(
                "DELETE FROM auth_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO auth_denied_game_comps (game_id, comp_code, create_user_id) VALUES (:gameId, :compCode, :userId)",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));

        jdbc.update(
                "DELETE FROM auth_denied_bills b WHERE bill_code = :billCode ",
                new MapSqlParameterSource("billCode", billCode));
        jdbc.update(
                "INSERT INTO auth_denied_bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser"));

        jdbc.update(
                "DELETE FROM auth_denied_game_bills gc WHERE bill_code = :billCode AND game_id = :gameId",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO auth_denied_game_bills (game_id, bill_code, create_user_id) VALUES (:gameId, :billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode)
                        .addValue("userId", "TestUser")
                        .addValue("gameId", gameId));

        boolean b = _dao.checkAuthDenied(gameId, compCode, billCode);

        assertTrue(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkMoveDenied(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckMoveDeniedSuccess() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String gameVer = "1.00";
        String compCode = "3X9999";
        String billCode1 = "3X9999999";
        String billCode2 = "2X9999999";

        jdbc.update("DELETE FROM move_denied_games g WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_gamevers v WHERE game_id = :gameId AND game_ver = :gameVer",
                new MapSqlParameterSource("gameVer", gameVer).addValue("gameId",
                        gameId));
        jdbc.update(
                "DELETE FROM move_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "DELETE FROM move_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_bills b WHERE bill_code in (:billCode1, :billCode2) ",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2));
        jdbc.update(
                "DELETE FROM move_denied_game_bills gc WHERE bill_code in (:billCode1, :billCode2) AND game_id = :gameId",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2)
                        .addValue("gameId", gameId));

        boolean b = _dao.checkMoveDenied(gameId, gameVer, compCode, billCode1,
                billCode2);

        assertFalse(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkMoveDenied(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckMoveDeniedGames() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String gameVer = "1.00";
        String compCode = "3X9999";
        String billCode1 = "3X9999999";
        String billCode2 = "2X9999999";

        if (jdbc.queryForObject("SELECT COUNT(1) FROM games WHERE game_id = :game_id",
        		new MapSqlParameterSource("game_id", gameId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games (game_id, title, create_user_id, update_user_id) "
                            + "VALUES (:gameId, :title, :userId, :userId)",
                    new MapSqlParameterSource("title", "TestGame")
                            .addValue("userId", "TestUser")
                            .addValue("gameId", gameId));
        }

        jdbc.update("DELETE FROM move_denied_games g WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update(
                "INSERT INTO move_denied_games (game_id, create_user_id) VALUES (:gameId, :userId)",
                new MapSqlParameterSource("userId", "TestUser")
                        .addValue("gameId", gameId));

        jdbc.update(
                "DELETE FROM move_denied_gamevers v WHERE game_id = :gameId AND game_ver = :gameVer",
                new MapSqlParameterSource("gameVer", gameVer).addValue("gameId",
                        gameId));
        jdbc.update(
                "DELETE FROM move_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "DELETE FROM move_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_bills b WHERE bill_code in (:billCode1, :billCode2) ",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2));
        jdbc.update(
                "DELETE FROM move_denied_game_bills gc WHERE bill_code in (:billCode1, :billCode2) AND game_id = :gameId",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2)
                        .addValue("gameId", gameId));

        boolean b = _dao.checkMoveDenied(gameId, gameVer, compCode, billCode1,
                billCode2);

        assertTrue(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkMoveDenied(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckMoveDeniedGameVers() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String gameVer = "1.00";
        String compCode = "3X9999";
        String billCode1 = "3X9999999";
        String billCode2 = "2X9999999";

        if (jdbc.queryForObject("SELECT COUNT(1) FROM games WHERE game_id = :game_id",
        		new MapSqlParameterSource("game_id", gameId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games (game_id, title, create_user_id, update_user_id) "
                            + "VALUES (:gameId, :title, :userId, :userId)",
                    new MapSqlParameterSource("title", "TestGame")
                            .addValue("userId", "TestUser")
                            .addValue("gameId", gameId));
        }

        jdbc.update("DELETE FROM move_denied_games g WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_gamevers v WHERE game_id = :gameId AND game_ver = :gameVer",
                new MapSqlParameterSource("gameVer", gameVer).addValue("gameId",
                        gameId));
        jdbc.update(
                "INSERT INTO move_denied_gamevers (game_id, game_ver, create_user_id) VALUES (:gameId, :gameVer, :userId)",
                new MapSqlParameterSource("userId", "TestUser")
                        .addValue("gameId", gameId)
                        .addValue("gameVer", gameVer));
        jdbc.update(
                "DELETE FROM move_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "DELETE FROM move_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_bills b WHERE bill_code in (:billCode1, :billCode2) ",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2));
        jdbc.update(
                "DELETE FROM move_denied_game_bills gc WHERE bill_code in (:billCode1, :billCode2) AND game_id = :gameId",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2)
                        .addValue("gameId", gameId));

        boolean b = _dao.checkMoveDenied(gameId, gameVer, compCode, billCode1,
                billCode2);

        assertTrue(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkMoveDenied(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckMoveDeniedComps() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String gameVer = "1.00";
        String compCode = "3X9999";
        String billCode1 = "3X9999999";
        String billCode2 = "2X9999999";

        jdbc.update("DELETE FROM comps WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "INSERT INTO comps (comp_code, name, create_user_id, update_user_id) VALUES (:compCode, :name, :userId, :userId)",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("userId", "TestUser")
                        .addValue("name", "TestComp"));

        jdbc.update("DELETE FROM move_denied_games g WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_gamevers v WHERE game_id = :gameId AND game_ver = :gameVer",
                new MapSqlParameterSource("gameVer", gameVer).addValue("gameId",
                        gameId));

        jdbc.update(
                "DELETE FROM move_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "INSERT INTO move_denied_comps (comp_code, create_user_id) VALUES (:compCode, :userId)",
                new MapSqlParameterSource("userId", "TestUser")
                        .addValue("compCode", compCode));
        jdbc.update(
                "DELETE FROM move_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_bills b WHERE bill_code in (:billCode1, :billCode2) ",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2));
        jdbc.update(
                "DELETE FROM move_denied_game_bills gc WHERE bill_code in (:billCode1, :billCode2) AND game_id = :gameId",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2)
                        .addValue("gameId", gameId));

        boolean b = _dao.checkMoveDenied(gameId, gameVer, compCode, billCode1,
                billCode2);

        assertTrue(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkMoveDenied(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckMoveDeniedGameComps() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String gameVer = "1.00";
        String compCode = "3X9999";
        String billCode1 = "3X9999999";
        String billCode2 = "2X9999999";

        if (jdbc.queryForObject("SELECT COUNT(1) FROM games WHERE game_id = :gameId",
        		new MapSqlParameterSource("gameId", gameId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games (game_id, title, create_user_id, update_user_id) "
                            + "VALUES (:gameId, :title, :userId, :userId)",
                    new MapSqlParameterSource("title", "TestGame")
                            .addValue("userId", "TestUser")
                            .addValue("gameId", gameId));
        }

        jdbc.update("DELETE FROM comps WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "INSERT INTO comps (comp_code, name, create_user_id, update_user_id) VALUES (:compCode, :name, :userId, :userId)",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("userId", "TestUser")
                        .addValue("name", "TestComp"));

        jdbc.update("DELETE FROM move_denied_games g WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_gamevers v WHERE game_id = :gameId AND game_ver = :gameVer",
                new MapSqlParameterSource("gameVer", gameVer).addValue("gameId",
                        gameId));
        jdbc.update(
                "DELETE FROM move_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "DELETE FROM move_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO move_denied_game_comps (comp_code, game_id, create_user_id) VALUES (:compCode, :gameId, :userId)",
                new MapSqlParameterSource("userId", "TestUser")
                        .addValue("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_bills b WHERE bill_code in (:billCode1, :billCode2) ",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2));
        jdbc.update(
                "DELETE FROM move_denied_game_bills gc WHERE bill_code in (:billCode1, :billCode2) AND game_id = :gameId",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2)
                        .addValue("gameId", gameId));

        boolean b = _dao.checkMoveDenied(gameId, gameVer, compCode, billCode1,
                billCode2);

        assertTrue(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkMoveDenied(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckMoveDeniedBills() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String gameVer = "1.00";
        String compCode = "3X9999";
        String billCode1 = "3X9999999";
        String billCode2 = "2X9999999";

        jdbc.update(
                "DELETE FROM bills WHERE bill_code in (:billCode1, :billCode2) ",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode1)
                        .addValue("userId", "TestUser"));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode2)
                        .addValue("userId", "TestUser"));

        jdbc.update("DELETE FROM move_denied_games g WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_gamevers v WHERE game_id = :gameId AND game_ver = :gameVer",
                new MapSqlParameterSource("gameVer", gameVer).addValue("gameId",
                        gameId));
        jdbc.update(
                "DELETE FROM move_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "DELETE FROM move_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_bills b WHERE bill_code in (:billCode1, :billCode2) ",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2));
        jdbc.update(
                "INSERT INTO move_denied_bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("userId", "TestUser")
                        .addValue("billCode", billCode1));
        jdbc.update(
                "DELETE FROM move_denied_game_bills gc WHERE bill_code in (:billCode1, :billCode2) AND game_id = :gameId",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2)
                        .addValue("gameId", gameId));

        boolean b = _dao.checkMoveDenied(gameId, gameVer, compCode, billCode1,
                billCode2);

        assertTrue(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkMoveDenied(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckMoveDeniedGameBills() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String gameVer = "1.00";
        String compCode = "3X9999";
        String billCode1 = "3X9999999";
        String billCode2 = "2X9999999";

        if (jdbc.queryForObject("SELECT COUNT(1) FROM games WHERE game_id = :gameId",
        		new MapSqlParameterSource("gameId", gameId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games (game_id, title, create_user_id, update_user_id) "
                            + "VALUES (:gameId, :title, :userId, :userId)",
                    new MapSqlParameterSource("title", "TestGame")
                            .addValue("userId", "TestUser")
                            .addValue("gameId", gameId));
        }

        jdbc.update(
                "DELETE FROM bills WHERE bill_code in (:billCode1, :billCode2) ",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode1)
                        .addValue("userId", "TestUser"));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode2)
                        .addValue("userId", "TestUser"));

        jdbc.update("DELETE FROM move_denied_games g WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_gamevers v WHERE game_id = :gameId AND game_ver = :gameVer",
                new MapSqlParameterSource("gameVer", gameVer).addValue("gameId",
                        gameId));
        jdbc.update(
                "DELETE FROM move_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "DELETE FROM move_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "DELETE FROM move_denied_bills b WHERE bill_code in (:billCode1, :billCode2) ",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2));
        jdbc.update(
                "DELETE FROM move_denied_game_bills gc WHERE bill_code in (:billCode1, :billCode2) AND game_id = :gameId",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2)
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO move_denied_game_bills (game_id, bill_code, create_user_id) VALUES (:gameId, :billCode, :userId)",
                new MapSqlParameterSource("userId", "TestUser")
                        .addValue("billCode", billCode2)
                        .addValue("gameId", gameId));

        boolean b = _dao.checkMoveDenied(gameId, gameVer, compCode, billCode1,
                billCode2);

        assertTrue(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#checkMoveDenied(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCheckMoveDeniedMulti() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String gameVer = "1.00";
        String compCode = "3X9999";
        String billCode1 = "3X9999999";
        String billCode2 = "2X9999999";

        jdbc.update("DELETE FROM comps WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));
        jdbc.update(
                "INSERT INTO comps (comp_code, name, create_user_id, update_user_id) VALUES (:compCode, :name, :userId, :userId)",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("userId", "TestUser")
                        .addValue("name", "TestComp"));

        if (jdbc.queryForObject("SELECT COUNT(1) FROM games WHERE game_id = :game_id",
        		new MapSqlParameterSource("game_id", gameId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games (game_id, title, create_user_id, update_user_id) "
                            + "VALUES (:gameId, :title, :userId, :userId)",
                    new MapSqlParameterSource("title", "TestGame")
                            .addValue("userId", "TestUser")
                            .addValue("gameId", gameId));
        }

        jdbc.update(
                "DELETE FROM bills WHERE bill_code in (:billCode1, :billCode2) ",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode1)
                        .addValue("userId", "TestUser"));
        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", billCode2)
                        .addValue("userId", "TestUser"));

        jdbc.update("DELETE FROM move_denied_games g WHERE game_id = :gameId ",
                new MapSqlParameterSource("gameId", gameId));
        jdbc.update(
                "INSERT INTO move_denied_games (game_id, create_user_id) VALUES (:gameId, :userId)",
                new MapSqlParameterSource("userId", "TestUser")
                        .addValue("gameId", gameId));

        jdbc.update(
                "DELETE FROM move_denied_gamevers v WHERE game_id = :gameId AND game_ver = :gameVer",
                new MapSqlParameterSource("gameVer", gameVer).addValue("gameId",
                        gameId));
        jdbc.update(
                "DELETE FROM move_denied_comps c WHERE comp_code = :compCode ",
                new MapSqlParameterSource("compCode", compCode));

        jdbc.update(
                "DELETE FROM move_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId",
                new MapSqlParameterSource("compCode", compCode)
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO move_denied_game_comps (comp_code, game_id, create_user_id) VALUES (:compCode, :gameId, :userId)",
                new MapSqlParameterSource("userId", "TestUser")
                        .addValue("compCode", compCode)
                        .addValue("gameId", gameId));

        jdbc.update(
                "DELETE FROM move_denied_bills b WHERE bill_code in (:billCode1, :billCode2)",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2));

        jdbc.update(
                "DELETE FROM move_denied_game_bills gc WHERE bill_code in (:billCode1, :billCode2) AND game_id = :gameId",
                new MapSqlParameterSource("billCode1", billCode1)
                        .addValue("billCode2", billCode2)
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO move_denied_game_bills (game_id, bill_code, create_user_id) VALUES (:gameId, :billCode, :userId)",
                new MapSqlParameterSource("userId", "TestUser")
                        .addValue("billCode", billCode1)
                        .addValue("gameId", gameId));
        jdbc.update(
                "INSERT INTO move_denied_game_bills (game_id, bill_code, create_user_id) VALUES (:gameId, :billCode, :userId)",
                new MapSqlParameterSource("userId", "TestUser")
                        .addValue("billCode", billCode2)
                        .addValue("gameId", gameId));

        boolean b = _dao.checkMoveDenied(gameId, gameVer, compCode, billCode1,
                billCode2);

        assertTrue(b);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#findAuthAllowedComp(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindAuthAllowedCompSuccess() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String compCode = "3Z9999";

        if (jdbc.queryForObject("SELECT COUNT(1) FROM games WHERE game_id = :game_id",
        		new MapSqlParameterSource("game_id", gameId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games (game_id, title, create_user_id, update_user_id) "
                            + "VALUES (:gameId, :title, :userId, :userId )",
                    new MapSqlParameterSource("gameId", gameId)
                            .addValue("title", "TestGame")
                            .addValue("userId", "TestUser"));
        }

        if (jdbc.queryForObject("SELECT COUNT(1) FROM comps WHERE comp_code = :comp_code",
        		new MapSqlParameterSource("comp_code", compCode), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO comps (comp_code, name, create_user_id, update_user_id) "
                            + "VALUES (:compCode, :name, :userId, :userId )",
                    new MapSqlParameterSource("compCode", compCode)
                            .addValue("name", "TestComp")
                            .addValue("userId", "TestUser"));
        }

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM auth_allowed_comps WHERE game_id = :gameId AND comp_code = :compCode",
                new MapSqlParameterSource("gameId", gameId).addValue("compCode",
                        compCode), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO auth_allowed_comps ( game_id, comp_code, create_user_id ) "
                            + "VALUES ( :gameId, :compCode, :userId )",
                    new MapSqlParameterSource("gameId", gameId)
                            .addValue("compCode", compCode)
                            .addValue("userId", "TestUser"));
        }

        AuthAllowedComp t = _dao.findAuthAllowedComp(gameId, compCode);

        assertEquals(gameId, t.getGameId());
        assertEquals(compCode, t.getCompCode());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#findAuthAllowedComp(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindAuthAllowedCompArgsAreEmpty() {
        String gameId = "";
        String compCode = "";

        AuthAllowedComp t = _dao.findAuthAllowedComp(gameId, compCode);

        assertNull(t);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#findAuthAllowedComp(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindAuthAllowedCompArgsAreNull() {
        String gameId = null;
        String compCode = null;

        AuthAllowedComp t = _dao.findAuthAllowedComp(gameId, compCode);

        assertNull(t);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#findAuthAllowedComp(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testFindAuthAllowedCompNoExist() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String compCode = "3Z9999";
        jdbc.update(
                "DELETE FROM auth_allowed_comps WHERE game_id = :gameId AND comp_code = :compCode",
                new MapSqlParameterSource("gameId", gameId).addValue("compCode",
                        compCode));

        AuthAllowedComp t = _dao.findAuthAllowedComp(gameId, compCode);

        assertNull(t);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#findAuthAllowedPlace(java.lang.String, java.math.BigDecimal)}
     * .
     */
    @Test
    public final void testFindAuthAllowedPlaceSuccess() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        String countryCode = "JPN";
        BigDecimal allnetId = new BigDecimal(-9999);
        String placeId = "TEST";
        String placeName = "TestPlace";

        if (jdbc.queryForObject("SELECT COUNT(1) FROM games WHERE game_id = :gameId",
        		new MapSqlParameterSource("gameId", gameId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games (game_id, title, create_user_id, update_user_id) "
                            + "VALUES (:gameId, :title, :userId, :userId )",
                    new MapSqlParameterSource("gameId", gameId)
                            .addValue("title", "TestGame")
                            .addValue("userId", "TestUser"));
        }

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM countries WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO countries (country_code, create_user_id, update_user_id) "
                            + "VALUES (:countryCode, :userId, :userId)",
                    new MapSqlParameterSource("countryCode", countryCode)
                            .addValue("userId", "TestUser"));
        }

        if (jdbc.queryForObject("SELECT COUNT(1) FROM places WHERE allnet_id = :allnetId",
        		new MapSqlParameterSource("allnetId", allnetId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO places (allnet_id, place_id, name, country_code, create_user_id, update_user_id) "
                            + "VALUES (:allnetId, :placeId, :name, :countryCode, :userId, :userId)",
                    new MapSqlParameterSource("allnetId", allnetId)
                            .addValue("placeId", placeId)
                            .addValue("name", placeName)
                            .addValue("countryCode", countryCode)
                            .addValue("userId", "TestUser"));
        }

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM auth_allowed_places WHERE game_id = :gameId AND allnet_id = :allnetId",
                new MapSqlParameterSource("gameId", gameId).addValue("allnetId", allnetId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO auth_allowed_places (game_id, allnet_id, create_user_id) "
                            + "VALUES (:gameId, :allnetId, :userId)",
                    new MapSqlParameterSource("gameId", gameId)
                            .addValue("allnetId", allnetId)
                            .addValue("userId", "TestUser"));
        }

        AuthAllowedPlace t = _dao.findAuthAllowedPlace(gameId, allnetId);

        assertEquals(gameId, t.getGameId());
        assertEquals(allnetId, t.getAllnetId());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#findAuthAllowedPlace(java.lang.String, java.math.BigDecimal)}
     * .
     */
    @Test
    public final void testFindAuthAllowedPlaceArgsAreEmpty() {
        String gameId = "";
        BigDecimal allnetId = null;

        AuthAllowedPlace t = _dao.findAuthAllowedPlace(gameId, allnetId);

        assertNull(t);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#findAuthAllowedPlace(java.lang.String, java.math.BigDecimal)}
     * .
     */
    @Test
    public final void testFindAuthAllowedPlaceArgsAreNull() {
        String gameId = null;
        BigDecimal allnetId = null;

        AuthAllowedPlace t = _dao.findAuthAllowedPlace(gameId, allnetId);

        assertNull(t);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#findAuthAllowedPlace(java.lang.String, java.math.BigDecimal)}
     * .
     */
    @Test
    public final void testFindAuthAllowedPlaceNoExist() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SBXX";
        BigDecimal allnetId = new BigDecimal(-9999);

        jdbc.update(
                "DELETE FROM auth_allowed_places t WHERE t.game_id = :gameId AND t.allnet_id = :allnetId",
                new MapSqlParameterSource("gameId", gameId).addValue("allnetId",
                        allnetId));

        AuthAllowedPlace t = _dao.findAuthAllowedPlace(gameId, allnetId);

        assertNull(t);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#exchangeMachineGameId(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test
    public final void testExchangeMachineGameIdSuccess() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
        String reservedGameId = "SBYY";
        Timestamp now = new Timestamp(new Date().getTime());

        if (jdbc.queryForObject("SELECT COUNT(1) FROM machines WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machines (serial, game_id, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :gameId, :now, :userId, :now, :userId )",
                    new MapSqlParameterSource("gameId", gameId)
                            .addValue("serial", serial)
                            .addValue("userId", "TestUser")
                            .addValue("now", now));
        }

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setReservedGameId(reservedGameId);

        _dao.exchangeMachineGameId(
                new PowerOnData(null, null, machine, new Status()));

        Map<String, Object> map = jdbc.queryForMap(
                "select * from machines where serial = :serial",
                new MapSqlParameterSource("serial", serial));

        assertEquals(reservedGameId, map.get("game_id"));
        assertEquals(gameId, map.get("reserved_game_id"));
        assertEquals(PowerOnDao.USER_ID, map.get("update_user_id"));
        assertNotSame(now, map.get("update_date"));

        assertEquals(reservedGameId, machine.getGameId());
        assertEquals(gameId, machine.getReservedGameId());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#exchangeMachineGameId(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test
    public final void testExchangeMachineGameIdNullGameId() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String gameId = null;
        String previousGameId = "SBXX";
        String reservedGameId = "SBYY";
        Timestamp now = new Timestamp(new Date().getTime());

        if (jdbc.queryForObject("SELECT COUNT(1) FROM machines WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machines (serial, game_id, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :gameId, :now, :userId, :now, :userId )",
                    new MapSqlParameterSource("gameId", previousGameId)
                            .addValue("serial", serial)
                            .addValue("userId", "TestUser")
                            .addValue("now", now));
        }

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setReservedGameId(reservedGameId);

        _dao.exchangeMachineGameId(
                new PowerOnData(null, null, machine, new Status()));

        Map<String, Object> map = jdbc
                .queryForMap("SELECT * FROM machines WHERE serial = :serial",
                		new MapSqlParameterSource("serial", serial));

        assertEquals(reservedGameId, map.get("game_id"));
        assertEquals(gameId, map.get("reserved_game_id"));
        assertEquals(PowerOnDao.USER_ID, map.get("update_user_id"));
        assertNotSame(now, map.get("update_date"));

        assertEquals(reservedGameId, machine.getGameId());
        assertEquals(gameId, machine.getReservedGameId());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#exchangeMachineGameId(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test(expected = DataAccessException.class)
    public final void testExchangeMachineGameIdNullReservedGameId() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
        String reservedGameId = null;

        try {
            if (jdbc.queryForObject(
                    "SELECT COUNT(1) FROM machines WHERE serial = :serial",
                    new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
                jdbc.update(
                        "INSERT INTO machines (serial, game_id, create_user_id, update_user_id) "
                                + "VALUES (:serial, :gameId, :userId, :userId )",
                        new MapSqlParameterSource("gameId", gameId)
                                .addValue("serial", serial)
                                .addValue("userId", "TestUser"));
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setReservedGameId(reservedGameId);

        _dao.exchangeMachineGameId(
                new PowerOnData(null, null, machine, new Status()));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#deleteLoaderStateLogs(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test
    public final void testDeleteLoaderStateLogsSuccess() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM loader_state_logs WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO loader_state_logs (serial, create_user_id, update_user_id) "
                            + "VALUES (:serial, :userId, :userId )",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("userId", "TestUser"));
        }

        Machine machine = new Machine();
        machine.setSerial(serial);

        _dao.deleteLoaderStateLogs(
                new PowerOnData(null, null, machine, new Status()));

        assertEquals(0,
                jdbc.queryForObject(
                        "select count(*) from loader_state_logs where serial = :serial",
                        new MapSqlParameterSource("serial", serial), Integer.class).intValue());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#deleteLoaderStateLogs(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test
    public final void testDeleteLoaderStateLogsNoExist() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String existSerial = "AZZZZZZZZZZ";
        String serial = "AYYYYYYYYYY";

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM loader_state_logs WHERE serial = :serial",
                new MapSqlParameterSource("serial", existSerial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO loader_state_logs (serial, create_user_id, update_user_id) "
                            + "VALUES (:serial, :userId, :userId)",
                    new MapSqlParameterSource("serial", existSerial)
                            .addValue("userId", "TestUser"));
        }

        Machine machine = new Machine();
        machine.setSerial(serial);

        _dao.deleteLoaderStateLogs(
                new PowerOnData(null, null, machine, new Status()));

        assertEquals(1,
                jdbc.queryForObject(
                        "select count(*) from loader_state_logs where serial = :serial",
                        new MapSqlParameterSource("serial", "AZZZZZZZZZZ"), Integer.class).intValue());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#deleteLoaderStateLogs(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test
    public final void testDeleteDeliverReportsSuccess() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM app_deliver_reports WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO app_deliver_reports (serial, download_state, ap_ver_working, create_user_id, update_user_id) "
                            + "VALUES (:serial, :rfState, :wdav, :userId, :userId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("rfState", 1).addValue("wdav", "1.00.00")
                            .addValue("userId", "TestUser"));
        }

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM opt_deliver_reports WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO opt_deliver_reports (serial, download_state, create_user_id, update_user_id) "
                            + "VALUES (:serial, :rfState, :userId, :userId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("rfState", 1)
                            .addValue("userId", "TestUser"));
        }

        Machine machine = new Machine();
        machine.setSerial(serial);

        _dao.deleteDeliverReport(
                new PowerOnData(null, null, machine, new Status()));

        assertEquals(0,
                jdbc.queryForObject(
                        "SELECT COUNT(1) FROM app_deliver_reports WHERE serial = :serial",
                        new MapSqlParameterSource("serial", serial), Integer.class).intValue());

        assertEquals(0,
                jdbc.queryForObject(
                        "SELECT COUNT(1) FROM opt_deliver_reports WHERE serial = :serial",
                        new MapSqlParameterSource("serial", serial), Integer.class).intValue());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#deleteLoaderStateLogs(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test
    public final void testDeleteDeliverReportsSuccessNoApp() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";

        jdbc.update("DELETE FROM app_deliver_reports WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM opt_deliver_reports WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO opt_deliver_reports (serial, download_state, create_user_id, update_user_id) "
                            + "VALUES (:serial, :rfState, :userId, :userId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("rfState", 1)
                            .addValue("userId", "TestUser"));
        }

        Machine machine = new Machine();
        machine.setSerial(serial);

        _dao.deleteDeliverReport(
                new PowerOnData(null, null, machine, new Status()));

        assertEquals(0,
                jdbc.queryForObject(
                        "SELECT COUNT(1) FROM app_deliver_reports WHERE serial = :serial",
                        new MapSqlParameterSource("serial", serial), Integer.class).intValue());

        assertEquals(0,
                jdbc.queryForObject(
                        "SELECT COUNT(*) FROM opt_deliver_reports WHERE serial = :serial",
                        new MapSqlParameterSource("serial", serial), Integer.class).intValue());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#deleteLoaderStateLogs(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test
    public final void testDeleteDeliverReportsSuccessNoOpt() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM app_deliver_reports WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO app_deliver_reports (serial, download_state, ap_ver_working, create_user_id, update_user_id) "
                            + "VALUES (:serial, :rfState, :wdav, :userId, :userId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("rfState", 1).addValue("wdav", "1.00.00")
                            .addValue("userId", "TestUser"));
        }

        jdbc.update("DELETE FROM opt_deliver_reports WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));

        Machine machine = new Machine();
        machine.setSerial(serial);

        _dao.deleteDeliverReport(
                new PowerOnData(null, null, machine, new Status()));

        assertEquals(0,
                jdbc.queryForObject(
                        "SELECT COUNT(1) FROM app_deliver_reports WHERE serial = :serial",
                        new MapSqlParameterSource("serial", serial), Integer.class).intValue());

        assertEquals(0,
                jdbc.queryForObject(
                        "SELECT COUNT(*) FROM opt_deliver_reports WHERE serial = :serial",
                        new MapSqlParameterSource("serial", serial), Integer.class).intValue());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#deleteLoaderStateLogs(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test
    public final void testDeleteDeliverReportsSuccessNotExist() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";

        jdbc.update("DELETE FROM app_deliver_reports WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));

        jdbc.update("DELETE FROM opt_deliver_reports WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));

        Machine machine = new Machine();
        machine.setSerial(serial);

        _dao.deleteDeliverReport(
                new PowerOnData(null, null, machine, new Status()));

        assertEquals(0,
                jdbc.queryForObject(
                        "SELECT COUNT(1) FROM app_deliver_reports WHERE serial = :serial",
                        new MapSqlParameterSource("serial", serial), Integer.class).intValue());

        assertEquals(0,
                jdbc.queryForObject(
                        "SELECT COUNT(1) FROM opt_deliver_reports WHERE serial = :serial",
                        new MapSqlParameterSource("serial", serial), Integer.class).intValue());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateMachineStatus(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData, jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testUpdateMachineStatusAuthSuccessExistMachineStatus() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
        String ver = "2.00";
        String ip = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "0000";
        String bootVer = "1111";
        String encode = "Shift_JIS";
        String formatVer = "2.00";
        String userAgent = "TestUserAgent";
        String hops = "2";

        String user = "TestUser";

        Timestamp now = new Timestamp(new Date().getTime());

        if (jdbc.queryForObject("SELECT COUNT(1) FROM machines WHERE serial = :serial",
        		new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machines (serial, game_id, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :gameId, :now, :userId, :now, :userId)",
                    new MapSqlParameterSource("gameId", gameId)
                            .addValue("serial", serial).addValue("userId", user)
                            .addValue("now", now));
        }

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM machine_statuses WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machine_statuses (serial, last_access, last_auth, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :now, :now, :now, :userId, :now, :userId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("userId", user).addValue("now", now));
        }

        Machine machine = new Machine();
        machine.setExistStatus(true);

        PowerOnParameter param = new PowerOnParameter(gameId, ver, serial, ip,
                globalIp, firmVer, bootVer, encode, formatVer, userAgent, hops,
                null);

        _dao.updateMachineStatus(
                new PowerOnData(null, null, machine, new Status()), param);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM machine_statuses WHERE serial = :serial", new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertEquals(gameId, m.get("game_id"));
        assertEquals(ver, m.get("game_ver"));
        assertEquals(ip, m.get("place_ip"));
        assertEquals(new BigDecimal(firmVer), m.get("firm_ver"));
        assertEquals(new BigDecimal(bootVer), m.get("boot_ver"));
        assertEquals(formatVer, m.get("format_ver"));
        assertEquals(userAgent, m.get("user_agent"));
        assertNotSame(now, m.get("last_access"));
        assertNotSame(now, m.get("last_auth"));
        assertEquals(new BigDecimal(hops), m.get("hops"));
        assertEquals(now, m.get("create_date"));
        assertEquals(user, m.get("create_user_id"));
        assertNotSame(now, m.get("update_date"));
        assertEquals(PowerOnDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateMachineStatus(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData, jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testUpdateMachineStatusAuthSuccessNoExistMachineStatus() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
        String ver = "2.00";
        String ip = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "0000";
        String bootVer = "1111";
        String encode = "Shift_JIS";
        String formatVer = "2.00";
        String userAgent = "TestUserAgent";
        String hops = "2";

        String user = "TestUser";

        Timestamp now = new Timestamp(new Date().getTime());

        if (jdbc.queryForObject("SELECT COUNT(1) FROM machines WHERE serial = :serial",
        		new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machines (serial, game_id, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :gameId, :now, :userId, :now, :userId)",
                    new MapSqlParameterSource("gameId", gameId)
                            .addValue("serial", serial).addValue("userId", user)
                            .addValue("now", now));
        }

        jdbc.update("DELETE FROM machine_statuses t where t.serial = :serial",
                new MapSqlParameterSource("serial", serial));

        Machine machine = new Machine();
        machine.setExistStatus(false);

        PowerOnParameter param = new PowerOnParameter(gameId, ver, serial, ip,
                globalIp, firmVer, bootVer, encode, formatVer, userAgent, hops,
                null);

        _dao.updateMachineStatus(
                new PowerOnData(null, null, machine, new Status()), param);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM machine_statuses WHERE serial = :serial", new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertEquals(gameId, m.get("game_id"));
        assertEquals(ver, m.get("game_ver"));
        assertEquals(ip, m.get("place_ip"));
        assertEquals(new BigDecimal(firmVer), m.get("firm_ver"));
        assertEquals(new BigDecimal(bootVer), m.get("boot_ver"));
        assertEquals(formatVer, m.get("format_ver"));
        assertEquals(userAgent, m.get("user_agent"));
        assertNotNull(m.get("last_auth"));
        assertEquals(new BigDecimal(hops), m.get("hops"));
        assertEquals(PowerOnDao.USER_ID, m.get("create_user_id"));
        assertEquals(PowerOnDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateMachineStatus(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData, jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testUpdateMachineStatusAuthFailExistMachineStatus() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
        String ver = "2.00";
        String ip = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "0000";
        String bootVer = "1111";
        String encode = "Shift_JIS";
        String formatVer = "2.00";
        String userAgent = "TestUserAgent";
        String hops = "2";

        String user = "TestUser";

        Timestamp now = new Timestamp(new Date().getTime());

        if (jdbc.queryForObject("SELECT COUNT(1) FROM machines WHERE serial = :serial",
        		new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machines (serial, game_id, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :gameId, :now, :userId, :now, :userId)",
                    new MapSqlParameterSource("gameId", gameId)
                            .addValue("serial", serial).addValue("userId", user)
                            .addValue("now", now));
        }

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM machine_statuses WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machine_statuses (serial, last_access, last_auth, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :now, :now, :now, :userId, :now, :userId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("userId", user).addValue("now", now));
        }

        Status stat = new Status();
        stat.setStat(AuthStatus.FAIL_GAME.value());

        Machine machine = new Machine();
        machine.setExistStatus(true);

        PowerOnParameter param = new PowerOnParameter(gameId, ver, serial, ip,
                globalIp, firmVer, bootVer, encode, formatVer, userAgent, hops,
                null);

        _dao.updateMachineStatus(new PowerOnData(null, null, machine, stat),
                param);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM machine_statuses WHERE serial = :serial", new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertEquals(gameId, m.get("game_id"));
        assertEquals(ver, m.get("game_ver"));
        assertEquals(ip, m.get("place_ip"));
        assertEquals(new BigDecimal(firmVer), m.get("firm_ver"));
        assertEquals(new BigDecimal(bootVer), m.get("boot_ver"));
        assertEquals(formatVer, m.get("format_ver"));
        assertEquals(userAgent, m.get("user_agent"));
        assertNotSame(now, m.get("last_access"));
        assertEquals(now, m.get("last_auth"));
        assertEquals(new BigDecimal(hops), m.get("hops"));
        assertEquals(now, m.get("create_date"));
        assertEquals(user, m.get("create_user_id"));
        assertNotSame(now, m.get("update_date"));
        assertEquals(PowerOnDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateMachineStatus(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData, jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testUpdateMachineStatusAuthFailForMachineExistMachineStatus() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
        String ver = "2.00";
        String ip = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "0000";
        String bootVer = "1111";
        String encode = "Shift_JIS";
        String formatVer = "2.00";
        String userAgent = "TestUserAgent";
        String hops = "2";
        int setting = 2;

        String user = "TestUser";

        Timestamp now = new Timestamp(new Date().getTime());

        if (jdbc.queryForObject("SELECT COUNT(1) FROM machines WHERE serial = :serial",
        		new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machines (serial, game_id, setting, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :gameId, :setting, :now, :userId, :now, :userId)",
                    new MapSqlParameterSource("gameId", gameId)
                            .addValue("serial", serial).addValue("userId", user)
                            .addValue("setting", setting).addValue("now", now));
        }

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM machine_statuses WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machine_statuses (serial, last_access, last_auth, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :now, :now, :now, :userId, :now, :userId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("userId", user).addValue("now", now));
        }

        Status stat = new Status();
        stat.setStat(AuthStatus.FAIL_MACHINE.value());

        Machine machine = new Machine();
        machine.setExistStatus(true);

        PowerOnParameter param = new PowerOnParameter(gameId, ver, serial, ip,
                globalIp, firmVer, bootVer, encode, formatVer, userAgent, hops,
                null);

        Game game = new Game();
        game.setAuth(1);
        game.setGameId(gameId);
        game.setGameVer(ver);
        game.setHost("test");
        game.setUri("http://test");

        _dao.updateMachineStatus(new PowerOnData(null, game, machine, stat),
                param);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM machine_statuses WHERE serial = :serial", new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertEquals(gameId, m.get("game_id"));
        assertEquals(ver, m.get("game_ver"));
        assertEquals(ip, m.get("place_ip"));
        assertEquals(new BigDecimal(firmVer), m.get("firm_ver"));
        assertEquals(new BigDecimal(bootVer), m.get("boot_ver"));
        assertEquals(formatVer, m.get("format_ver"));
        assertEquals(userAgent, m.get("user_agent"));
        assertNotSame(now, m.get("last_access"));
        assertEquals(now, m.get("last_auth"));
        assertEquals(new BigDecimal(hops), m.get("hops"));
        assertEquals(now, m.get("create_date"));
        assertEquals(user, m.get("create_user_id"));
        assertNotSame(now, m.get("update_date"));
        assertEquals(PowerOnDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateMachineStatus(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData, jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testUpdateMachineStatusAuthFailNoExistMachineStatus() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
        String ver = "2.00";
        String ip = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "0000";
        String bootVer = "1111";
        String encode = "Shift_JIS";
        String formatVer = "2.00";
        String userAgent = "TestUserAgent";
        String hops = "2";

        String user = "TestUser";

        Timestamp now = new Timestamp(new Date().getTime());

        if (jdbc.queryForObject("SELECT COUNT(1) FROM machines WHERE serial = :serial",
        		new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machines (serial, game_id, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :gameId, :now, :userId, :now, :userId)",
                    new MapSqlParameterSource("gameId", gameId)
                            .addValue("serial", serial).addValue("userId", user)
                            .addValue("now", now));
        }

        jdbc.update("DELETE FROM machine_statuses t where t.serial = :serial",
        		new MapSqlParameterSource("serial", serial));

        Status stat = new Status();
        stat.setStat(AuthStatus.FAIL_LOC.value());

        Machine machine = new Machine();
        machine.setExistStatus(false);

        PowerOnParameter param = new PowerOnParameter(gameId, ver, serial, ip,
                globalIp, firmVer, bootVer, encode, formatVer, userAgent, hops,
                null);

        _dao.updateMachineStatus(new PowerOnData(null, null, machine, stat),
                param);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM machine_statuses WHERE serial = :serial", new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertEquals(gameId, m.get("game_id"));
        assertEquals(ver, m.get("game_ver"));
        assertEquals(ip, m.get("place_ip"));
        assertEquals(new BigDecimal(firmVer), m.get("firm_ver"));
        assertEquals(new BigDecimal(bootVer), m.get("boot_ver"));
        assertEquals(formatVer, m.get("format_ver"));
        assertEquals(userAgent, m.get("user_agent"));
        assertNull(m.get("last_auth"));
        assertEquals(new BigDecimal(hops), m.get("hops"));
        assertEquals(PowerOnDao.USER_ID, m.get("create_user_id"));
        assertEquals(PowerOnDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateMachineStatus(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData, jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test
    public final void testUpdateMachineStatusNoSerialExistMachineStatus() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
        String ver = "2.00";
        String ip = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "0000";
        String bootVer = "1111";
        String encode = "Shift_JIS";
        String formatVer = "2.00";
        String userAgent = "TestUserAgent";
        String hops = "2";

        String user = "TestUser";

        Timestamp now = new Timestamp(new Date().getTime());

        if (jdbc.queryForObject("SELECT COUNT(1) FROM machines WHERE serial = :serial",
        		new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machines (serial, game_id, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :gameId, :now, :userId, :now, :userId)",
                    new MapSqlParameterSource("gameId", gameId)
                            .addValue("serial", serial).addValue("userId", user)
                            .addValue("now", now));
        }

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM machine_statuses WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machine_statuses (serial, last_access, last_auth, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :now, :now, :now, :userId, :now, :userId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("userId", user).addValue("now", now));
        }

        Machine machine = new Machine();
        machine.setExistStatus(true);

        PowerOnParameter param = new PowerOnParameter(gameId, ver, null, ip,
                globalIp, firmVer, bootVer, encode, formatVer, userAgent, hops,
                null);

        _dao.updateMachineStatus(
                new PowerOnData(null, null, machine, new Status()), param);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM machine_statuses WHERE serial = :serial", new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertNull(m.get("game_id"));
        assertNull(m.get("game_ver"));
        assertNull(m.get("place_ip"));
        assertNull(m.get("firm_ver"));
        assertNull(m.get("boot_ver"));
        assertNull(m.get("format_ver"));
        assertNull(m.get("user_agent"));
        assertEquals(now, m.get("last_access"));
        assertEquals(now, m.get("last_auth"));
        assertEquals(new BigDecimal(-1), m.get("hops"));
        assertEquals(now, m.get("create_date"));
        assertEquals(user, m.get("create_user_id"));
        assertEquals(now, m.get("update_date"));
        assertEquals(user, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateMachineStatus(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData, jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus)}
     * .
     */
    @Test(expected = DataAccessException.class)
    public final void testUpdateMachineStatusNoSerialNoExistMachineStatus() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
        String ver = "2.00";
        String ip = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "0000";
        String bootVer = "1111";
        String encode = "Shift_JIS";
        String formatVer = "2.00";
        String userAgent = "TestUserAgent";
        String hops = "2";

        String user = "TestUser";

        Timestamp now = new Timestamp(new Date().getTime());

        try {
            if (jdbc.queryForObject(
                    "SELECT COUNT(1) FROM machines WHERE serial = :serial",
                    new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
                jdbc.update(
                        "INSERT INTO machines (serial, game_id, create_date, create_user_id, update_date, update_user_id) "
                                + "VALUES (:serial, :gameId, :now, :userId, :now, :userId)",
                        new MapSqlParameterSource("gameId", gameId)
                                .addValue("serial", serial)
                                .addValue("userId", user).addValue("now", now));
            }
            jdbc.update(
                    "DELETE FROM machine_statuses t WHERE t.serial = :serial",
                    new MapSqlParameterSource("serial", serial));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        Machine machine = new Machine();
        machine.setExistStatus(false);

        PowerOnParameter param = new PowerOnParameter(gameId, ver, null, ip,
                globalIp, firmVer, bootVer, encode, formatVer, userAgent, hops,
                null);

        _dao.updateMachineStatus(
                new PowerOnData(null, null, machine, new Status()), param);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateUnRegisteredMachine(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * .
     */
    @Test
    public final void testUpdateUnRegisteredMachineExist() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String ip = "1.1.1.1";
        String gameId = "SBXX";

        Timestamp now = new Timestamp(new Date().getTime());
        String user = "TestUser";

        try {
            if (jdbc.queryForObject(
                    "SELECT COUNT(1) FROM unregistered_machines WHERE serial = :serial",
                    new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
                jdbc.update(
                        "INSERT INTO unregistered_machines (serial, place_ip, game_id, last_access, create_date, create_user_id, update_date, update_user_id) "
                                + "VALUES (:serial, :placeIp, :gameId, :now, :now, :userId, :now, :userId)",
                        new MapSqlParameterSource("gameId", gameId)
                                .addValue("serial", serial)
                                .addValue("placeIp", ip)
                                .addValue("userId", user).addValue("now", now));
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        PowerOnParameter param = new PowerOnParameter(gameId, null, serial, ip,
                null, null, null, null, null, null, null, null);

        _dao.updateUnRegisteredMachine(param);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM UNREGISTERED_MACHINES WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertEquals(ip, m.get("place_ip"));
        assertEquals(gameId, m.get("game_id"));
        assertNotSame(now, m.get("last_access"));
        assertEquals(now, m.get("create_date"));
        assertEquals(user, m.get("create_user_id"));
        assertNotSame(now, m.get("update_date"));
        assertEquals(PowerOnDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateUnRegisteredMachine(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * .
     */
    @Test
    public final void testUpdateUnRegisteredMachineNoExist() {
        JdbcTemplate jdbc = new JdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String ip = "1.1.1.1";
        String gameId = "SBXX";

        jdbc.update("DELETE FROM unregistered_machines t WHERE t.serial = ?",
                serial);

        PowerOnParameter param = new PowerOnParameter(gameId, null, serial, ip,
                null, null, null, null, null, null, null, null);

        _dao.updateUnRegisteredMachine(param);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM unregistered_machines WHERE serial = ?", serial);

        assertEquals(serial, m.get("serial"));
        assertEquals(ip, m.get("place_ip"));
        assertEquals(gameId, m.get("game_id"));
        assertEquals(PowerOnDao.USER_ID, m.get("create_user_id"));
        assertEquals(PowerOnDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateUnRegisteredMachine(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * .
     */
    @Test
    public final void testUpdateUnRegisteredMachineExistAndPlaceIpGameIdAreNull() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String ip = null;
        String gameId = null;

        Timestamp now = new Timestamp(new Date().getTime());
        String user = "TestUser";

        try {
            if (jdbc.queryForObject(
                    "SELECT COUNT(1) FROM unregistered_machines WHERE serial = :serial",
                    new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
                jdbc.update(
                        "INSERT INTO unregistered_machines (serial, place_ip, game_id, last_access, create_date, create_user_id, update_date, update_user_id) "
                                + "VALUES (:serial, :placeIp, :gameId, :now, :now, :userId, :now, :userId)",
                        new MapSqlParameterSource("gameId", gameId)
                                .addValue("serial", serial)
                                .addValue("placeIp", ip)
                                .addValue("userId", user).addValue("now", now));
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        PowerOnParameter param = new PowerOnParameter(gameId, null, serial, ip,
                null, null, null, null, null, null, null, null);

        _dao.updateUnRegisteredMachine(param);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM UNREGISTERED_MACHINES WHERE serial = :serial", new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertEquals(ip, m.get("place_ip"));
        assertEquals(gameId, m.get("game_id"));
        assertNotSame(now, m.get("last_access"));
        assertEquals(now, m.get("create_date"));
        assertEquals(user, m.get("create_user_id"));
        assertNotSame(now, m.get("update_date"));
        assertEquals(PowerOnDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateUnRegisteredMachine(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * .
     */
    @Test
    public final void testUpdateUnRegisteredMachineNoExistAndPlaceIpGameIdAreNull() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String ip = null;
        String gameId = null;

        jdbc.update(
                "DELETE FROM unregistered_machines t WHERE t.serial = :serial",
                new MapSqlParameterSource("serial", serial));

        PowerOnParameter param = new PowerOnParameter(gameId, null, serial, ip,
                null, null, null, null, null, null, null, null);

        _dao.updateUnRegisteredMachine(param);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM UNREGISTERED_MACHINES WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertEquals(ip, m.get("place_ip"));
        assertEquals(gameId, m.get("game_id"));
        assertEquals(PowerOnDao.USER_ID, m.get("create_user_id"));
        assertEquals(PowerOnDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateUnRegisteredMachine(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * .
     */
    @Test(expected = DataAccessException.class)
    public final void testUpdateUnRegisteredMachineNoSerial() {
        String serial = null;
        String ip = "1.1.1.1";
        String gameId = "SBXX";

        PowerOnParameter param = new PowerOnParameter(gameId, null, serial, ip,
                null, null, null, null, null, null, null, null);

        _dao.updateUnRegisteredMachine(param);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#deleteUnRegisteredMachine(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * .
     */
    @Test
    public final void testDeleteUnRegisteredMachineSuccess() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM unregistered_machines WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO unregistered_machines (serial, create_user_id, update_user_id) "
                            + "VALUES (:serial, :userId, :userId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("userId", "TestUser"));
        }

        PowerOnParameter param = new PowerOnParameter(null, null, serial, null,
                null, null, null, null, null, null, null, null);

        _dao.deleteUnRegisteredMachine(param);

        assertEquals(0,
                jdbc.queryForObject(
                        "SELECT count(*) FROM unregistered_machines WHERE serial = :serial",
                        new MapSqlParameterSource("serial", serial), Integer.class).intValue());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#deleteUnRegisteredMachine(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * .
     */
    @Test
    public final void testDeleteUnRegisteredMachineNoExist() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String targetSerial = "AYYYYYYYYYY";

        jdbc.update("DELETE FROM unregistered_machines WHERE serial = :serial",
                new MapSqlParameterSource("serial", targetSerial));

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM unregistered_machines WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO unregistered_machines (serial, create_user_id, update_user_id) "
                            + "VALUES (:serial, :userId, :userId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("userId", "TestUser"));
        }

        PowerOnParameter param = new PowerOnParameter(null, null, targetSerial,
                null, null, null, null, null, null, null, null, null);

        _dao.deleteUnRegisteredMachine(param);

        assertEquals(1,
                jdbc.queryForObject(
                        "SELECT COUNT(1) FROM unregistered_machines WHERE serial = :serial",
                        new MapSqlParameterSource("serial", serial), Integer.class).intValue());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#insertMachine(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test
    public final void testInsertMachineSuccess() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        BigDecimal allnetId = new BigDecimal(-9999);
        String gameId = "SBXX";
        String placeId = "XXXX";

        jdbc.update("DELETE FROM machines WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setGameId(gameId);
        machine.setPlaceId(placeId);

        _dao.insertMachine(new PowerOnData(null, null, machine, new Status()));

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM machines WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertEquals(allnetId, m.get("allnet_id"));
        assertEquals(gameId, m.get("game_id"));
        assertEquals(placeId, m.get("place_id"));
        assertEquals(new BigDecimal(1), m.get("group_index"));
        assertEquals(new BigDecimal(1), m.get("setting"));
        assertEquals(PowerOnDao.USER_ID, m.get("create_user_id"));
        assertEquals(PowerOnDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#insertMachine(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test(expected = DataAccessException.class)
    public final void testInsertMachineNoSerial() {
        String serial = null;
        BigDecimal allnetId = new BigDecimal(-9999);
        String gameId = "SBXX";

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setGameId(gameId);

        _dao.insertMachine(new PowerOnData(null, null, machine, new Status()));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#insertMachine(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test(expected = DataAccessException.class)
    public final void testInsertMachineNoGameId() {
        String serial = "AZZZZZZZZZZ";
        BigDecimal allnetId = null;
        String gameId = null;

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setGameId(gameId);

        _dao.insertMachine(new PowerOnData(null, null, machine, new Status()));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateMachine(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test
    public final void testUpdateMachineSuccess() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        BigDecimal allnetId = new BigDecimal(-9999);
        String gameId = "SBXX";
        String placeId = "XXXX";

        Timestamp now = new Timestamp(new Date().getTime());
        String userId = "TestUser";

        if (jdbc.queryForObject("SELECT COUNT(1) FROM machines WHERE serial = :serial",
        		new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machines (serial, game_id, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :gameId, :now, :userId, :now, :userId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("gameId", gameId).addValue("now", now)
                            .addValue("userId", userId));
        }

        Machine machine = new Machine();
        machine.setSerial(serial);

        Router router = new Router();
        router.setAllnetId(allnetId);
        router.setPlaceId(placeId);

        _dao.updateMachine(
                new PowerOnData(router, null, machine, new Status()));

        Map<String, Object> m = jdbc
                .queryForMap("SELECT * FROM machines WHERE serial = :serial", new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertEquals(allnetId, m.get("allnet_id"));
        assertEquals(gameId, m.get("game_id"));
        assertEquals(placeId, m.get("place_id"));
        assertEquals(new BigDecimal(1), m.get("group_index"));
        assertEquals(new BigDecimal(1), m.get("setting"));
        assertEquals(now, m.get("create_date"));
        assertEquals(userId, m.get("create_user_id"));
        assertNotSame(now, m.get("update_date"));
        assertEquals(PowerOnDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#updateMachine(jp.co.sega.allnet.auth.api.service.poweron.PowerOnData)}
     * .
     */
    @Test
    public final void testUpdateMachineNoSerial() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String targetSerial = null;
        BigDecimal allnetId = new BigDecimal(-9999);
        String gameId = "SBXX";
        String placeId = "XXXX";

        Timestamp now = new Timestamp(new Date().getTime());
        String userId = "TestUser";

        if (jdbc.queryForObject("SELECT COUNT(1) FROM machines WHERE serial = :serial",
        		new MapSqlParameterSource("serial", serial), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO machines (serial, game_id, create_date, create_user_id, update_date, update_user_id) "
                            + "VALUES (:serial, :gameId, :now, :userId, :now, :userId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("gameId", gameId).addValue("now", now)
                            .addValue("userId", userId));
        }

        Machine machine = new Machine();
        machine.setSerial(targetSerial);

        Router router = new Router();
        router.setAllnetId(allnetId);
        router.setPlaceId(placeId);

        _dao.updateMachine(
                new PowerOnData(router, null, machine, new Status()));

        Map<String, Object> m = jdbc
                .queryForMap("SELECT * FROM machines WHERE serial = :serial", new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertNull(m.get("allnet_id"));
        assertEquals(gameId, m.get("game_id"));
        assertNull(m.get("place_id"));
        assertEquals(new BigDecimal(1), m.get("group_index"));
        assertEquals(new BigDecimal(1), m.get("setting"));
        assertEquals(now, m.get("create_date"));
        assertEquals(userId, m.get("create_user_id"));
        assertEquals(now, m.get("update_date"));
        assertEquals(userId, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.PowerOnDaoBean#insertLog(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter, jp.co.sega.allnet.auth.api.service.poweron.PowerOnStatus, java.lang.String)}
     * .
     */
    @Test
    public final void testInsertLog() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        BigDecimal stat = new BigDecimal(1);
        BigDecimal cause = new BigDecimal(1);
        String gameId = "SBXX";
        String gameVer = "2.00";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String request = "request";
        String response = "response";
        String debugInfo = "DebugInfo";
        String debugInfoPlace = "DebugInfoPlace";
        String placeId = "0001";
        String countryCode = "TWN";
        BigDecimal allnetId = new BigDecimal(1);

        jdbc.update("DELETE FROM logs WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, null, null, null, null, null, null, request);

        Status pos = new Status();
        pos.setStat(stat.intValue());
        pos.setCause(cause.intValue());

        Router r = new Router();
        r.setPlaceId(placeId);
        r.setCountryCode(countryCode);
        r.setAllnetId(allnetId);

        _dao.insertLog(param, pos.getStat(), pos.getCause(), r, response,
                debugInfo, debugInfoPlace);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM logs WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));

        assertEquals(serial, m.get("serial"));
        assertEquals(stat, m.get("stat"));
        assertEquals(cause, m.get("cause"));
        assertEquals(gameId, m.get("game_id"));
        assertEquals(gameVer, m.get("game_ver"));
        assertEquals(placeId, m.get("place_id"));
        assertEquals(countryCode, m.get("country_code"));
        assertEquals(placeIp, m.get("place_ip"));
        assertEquals(globalIp, m.get("global_ip"));
        assertEquals(allnetId, m.get("allnet_id"));
        assertEquals(request, m.get("request"));
        assertEquals(response, m.get("response"));
        assertEquals(PowerOnDao.USER_ID, m.get("create_user_id"));
    }

    @Test
    public final void testCheckPrdCheckGame() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String gameId = "SXXX";
        Date now = new Date();
        String userId = "TestUser";

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM prd_check_games WHERE game_id = :gameId",
                new MapSqlParameterSource("gameId", gameId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO prd_check_games (game_id, create_date, create_user_id) "
                            + "VALUES (:gameId, CURRENT_TIMESTAMP, :userId)",
                    new MapSqlParameterSource("gameId", gameId)
                            .addValue("now", now).addValue("userId", userId));
        }

        assertTrue(_dao.checkPrdCheckGame(gameId));
    }

    @Test
    public final void testCheckPrdCheckGameNoRecord() {
        JdbcTemplate jdbc = new JdbcTemplate(_ds);
        String gameId = "SXXX";

        jdbc.update("DELETE FROM prd_check_games WHERE game_id = ?", gameId);

        assertFalse(_dao.checkPrdCheckGame(gameId));
    }

    @Test
    public final void testFindKeychipStat() {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        int stat = 1;
        String gameId = "SXXX";

        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM v_keychips WHERE keychip_sn = :serial AND game_id = :gameId",
                new MapSqlParameterSource("serial", serial).addValue("gameId", gameId), Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO v_keychips (keychip_sn, parts_no, order_number, dev_type, keychip_stat, billing_type, seq_no, ins_datetime, upd_datetime, keychip_group, game_id) "
                            + "VALUES (:serial, :partsNo, :orderNumber, :devType, :keychipStat, :billingType, :seqNo, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, :keychipGroup, :gameId)",
                    new MapSqlParameterSource("serial", serial)
                            .addValue("partsNo", "1")
                            .addValue("orderNumber", "1")
                            .addValue("devType", 0)
                            .addValue("keychipStat", stat)
                            .addValue("billingType", 0)
                            .addValue("seqNo", 0)
                            .addValue("keychipGroup", "1")
                            .addValue("gameId", gameId));
        }

        Integer keychipStat = _dao.findKeychipStat(serial, gameId);
        assertNotNull(keychipStat);
        assertEquals(stat, keychipStat.intValue());
    }

    @Test
    public final void testFindKeychipStatNoRecord() {
        JdbcTemplate jdbc = new JdbcTemplate(_ds);
        String serial = "AZZZZZZZZZZ";
        String gameId = "SXXX";

        jdbc.update(
                "DELETE FROM v_keychips WHERE keychip_sn = ? AND game_id = ?",
                serial, gameId);

        assertNull(_dao.findKeychipStat(serial, gameId));
    }
}
