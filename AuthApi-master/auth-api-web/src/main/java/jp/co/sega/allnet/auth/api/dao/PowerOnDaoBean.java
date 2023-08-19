/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Resource;
import javax.sql.DataSource;

import jp.co.sega.allnet.auth.api.domain.AuthAllowedComp;
import jp.co.sega.allnet.auth.api.domain.AuthAllowedPlace;
import jp.co.sega.allnet.auth.api.service.poweron.AuthStatus;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Game;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Machine;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Router;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Status;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

/**
 * @author NakanoY
 * 
 */
@Component("powerOnDao")
@Scope("singleton")
public class PowerOnDaoBean implements PowerOnDao {
    private static final int DEFAULT_INDEX = 1;
    private static final String ROUTER_IP_END_STRING = "254";

    @Resource(name = "dataSource")
    private DataSource _dataSource;

    @Override
    public PowerOnData find(String gameId, String gameVer, String serial,
            String ip) {

        Status stat = new Status();

        Router router = null;
        Game game = null;
        Machine machine = null;

        // ルータ情報取得処理
        router = findRouter(ip);
        if (router.getRouterId() == null) {
            // ルータ情報が存在しない
            stat.setStat(AuthStatus.FAIL_LOC.value());
            stat.setCause(-1);
        }

        // ゲーム情報取得処理
        game = findGame(gameId, gameVer, router.getCountryCode());
        if (game.getGameId() == null && stat.getStat() >= 0) {
            // ゲーム情報が存在しない
            stat.setStat(AuthStatus.FAIL_GAME.value());
            stat.setCause(-3);
        }

        // 基板情報取得処理
        machine = findMachine(serial);

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        return data;
    }

    @Override
    public boolean checkAuthDenied(String gameId, String compCode,
            String billCode) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT COUNT(*) FROM("
                + "SELECT COUNT(*) AS cnt FROM auth_denied_comps c WHERE comp_code = :compCode "
                + "UNION ALL "
                + "SELECT COUNT(*) AS cnt FROM auth_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId "
                + "UNION ALL "
                + "SELECT COUNT(*) AS cnt FROM auth_denied_bills b WHERE bill_code = :billCode "
                + "UNION ALL "
                + "SELECT COUNT(*) AS cnt FROM auth_denied_game_bills gb WHERE bill_code = :billCode AND game_id = :gameId) t "
                + "WHERE cnt > 0";

		int result = jdbc.queryForObject(sql, 
        new MapSqlParameterSource()
                .addValue("compCode", compCode).addValue("gameId", gameId)
                .addValue("billCode", billCode), Integer.class);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkMoveDenied(String gameId, String gameVer,
            String compCode, String routerBillCode, String machineBillCode) {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT COUNT(*) FROM("
                + "SELECT COUNT(*) AS cnt FROM move_denied_games g WHERE game_id = :gameId "
                + "UNION ALL "
                + "SELECT COUNT(*) AS cnt FROM move_denied_gamevers gv WHERE game_id = :gameId AND game_ver = :gameVer "
                + "UNION ALL "
                + "SELECT COUNT(*) AS cnt FROM move_denied_comps c WHERE comp_code = :compCode "
                + "UNION ALL "
                + "SELECT COUNT(*) AS cnt FROM move_denied_game_comps gc WHERE comp_code = :compCode AND game_id = :gameId "
                + "UNION ALL "
                + "SELECT COUNT(*) AS cnt FROM move_denied_bills b WHERE bill_code = :routerBillCode OR bill_code = :machineBillCode "
                + "UNION ALL "
                + "SELECT COUNT(*) AS cnt FROM move_denied_game_bills gb WHERE (bill_code = :routerBillCode OR bill_code = :machineBillCode) AND game_id = :gameId) t "
                + "WHERE cnt > 0";

        int result = jdbc.queryForObject(
                sql,
                new MapSqlParameterSource().addValue("gameId", gameId)
                        .addValue("gameVer", gameVer)
                        .addValue("compCode", compCode)
                        .addValue("routerBillCode", routerBillCode)
                        .addValue("machineBillCode", machineBillCode), Integer.class);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public AuthAllowedComp findAuthAllowedComp(String gameId, String compCode) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT game_id, comp_code FROM auth_allowed_comps "
                + "WHERE game_id = :gameId AND comp_code = :compCode";

        SqlParameterSource params = new MapSqlParameterSource().addValue(
                "gameId", gameId).addValue("compCode", compCode);
        try {
            return jdbc.queryForObject(sql, params, new RowMapper<AuthAllowedComp>() {

                @Override
                public AuthAllowedComp mapRow(ResultSet rs, int rowNum)
                        throws SQLException {

                    AuthAllowedComp authAllowedComp = new AuthAllowedComp();
                    authAllowedComp.setGameId(rs.getString("game_id"));
                    authAllowedComp.setCompCode(rs.getString("comp_code"));

                    return authAllowedComp;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public AuthAllowedPlace findAuthAllowedPlace(String gameId,
            BigDecimal allnetId) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT game_id, allnet_id FROM auth_allowed_places "
                + "WHERE game_id = :gameId AND allnet_id = :allnetId";

        SqlParameterSource params = new MapSqlParameterSource().addValue(
                "gameId", gameId).addValue("allnetId", allnetId);
        try {
            return jdbc.queryForObject(sql, params, new RowMapper<AuthAllowedPlace>() {

                @Override
                public AuthAllowedPlace mapRow(ResultSet rs, int rowNum)
                        throws SQLException {

                    AuthAllowedPlace authAllowedPlace = new AuthAllowedPlace();
                    authAllowedPlace.setGameId(rs.getString("game_id"));
                    authAllowedPlace.setAllnetId(rs.getBigDecimal("allnet_id"));

                    return authAllowedPlace;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void exchangeMachineGameId(PowerOnData data) {
        Machine m = data.getMachine();
        String gameId = m.getGameId();
        // DBを更新
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);
        jdbc.update(
                "UPDATE machines SET game_id = :gameId, reserved_game_id = :reservedGameId, "
                        + "update_date = CURRENT_TIMESTAMP, update_user_id = :updateUserId WHERE serial = :serial",
                new MapSqlParameterSource()
                        .addValue("gameId", m.getReservedGameId())
                        .addValue("reservedGameId", gameId)
                        .addValue("serial", m.getSerial())
                        .addValue("updateUserId", USER_ID));
        // 取得済みGameを交換
        m.setGameId(m.getReservedGameId());
        m.setReservedGameId(gameId);
    }

    @Override
    public void deleteLoaderStateLogs(PowerOnData data) {
        Machine m = data.getMachine();
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);
        jdbc.update("DELETE FROM loader_state_logs WHERE serial = :serial",
                new MapSqlParameterSource("serial", m.getSerial()));
    }

    @Override
    public void updateMachineStatus(PowerOnData data, PowerOnParameter param) {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);
        Date date = new Date();

        MapSqlParameterSource sqlParam = new MapSqlParameterSource("serial",
                param.getSerial()).addValue("gameId", param.getGameId())
                .addValue("gameVer", param.getVer())
                .addValue("placeIp", param.getIp())
                .addValue("formatVer", param.getFormatVer())
                .addValue("userAgent", param.getUserAgent())
                .addValue("lastAccess", new Timestamp(date.getTime()))
                .addValue("hops", new BigDecimal(param.getHops()))
                .addValue("updateUserId", USER_ID);
        if (param.getFirmVer() != null) {
            sqlParam.addValue("firmVer", new BigDecimal(param.getFirmVer()));
        } else {
            sqlParam.addValue("firmVer", null);
        }
        if (param.getBootVer() != null) {
            sqlParam.addValue("bootVer", new BigDecimal(param.getBootVer()));
        } else {
            sqlParam.addValue("bootVer", null);
        }

        String sql;

        if (data.getStatus().getStat() == 1) {
            // 認証が成功している
            sqlParam.addValue("lastAuth", new Timestamp(date.getTime()));

            if (data.getMachine().isExistStatus()) {

                sql = "UPDATE machine_statuses "
                        + "SET game_id = :gameId, game_ver = :gameVer, place_ip = :placeIp, firm_ver = :firmVer, boot_ver = :bootVer, format_ver = :formatVer, user_agent = :userAgent, "
                        + "last_access = :lastAccess, last_auth = :lastAuth, hops = :hops, update_date = CURRENT_TIMESTAMP, update_user_id = :updateUserId "
                        + "WHERE serial = :serial";
            } else {
                sqlParam.addValue("createUserId", USER_ID);
                sql = "INSERT INTO machine_statuses (serial, game_id, game_ver, place_ip, firm_ver, boot_ver, format_ver, user_agent, last_access, last_auth, hops, create_user_id, update_user_id) "
                        + "VALUES (:serial, :gameId, :gameVer, :placeIp, :firmVer, :bootVer, :formatVer, :userAgent, "
                        + ":lastAccess, :lastAuth,:hops, :createUserId, :updateUserId )";
            }

        } else {
            // 認証が失敗している
            if (data.getMachine().isExistStatus()) {
                sql = "UPDATE machine_statuses "
                        + "SET game_id = :gameId, game_ver = :gameVer, place_ip = :placeIp, firm_ver = :firmVer, boot_ver = :bootVer, format_ver = :formatVer, user_agent = :userAgent, "
                        + "last_access = :lastAccess, hops = :hops, update_date = CURRENT_TIMESTAMP, update_user_id = :updateUserId "
                        + "WHERE serial = :serial";
            } else {
                sqlParam.addValue("createUserId", USER_ID);
                sql = "INSERT INTO machine_statuses (serial, game_id, game_ver, place_ip, firm_ver, boot_ver, format_ver, user_agent, last_access, hops, create_user_id, update_user_id) "
                        + "VALUES (:serial, :gameId, :gameVer, :placeIp, :firmVer, :bootVer, :formatVer, :userAgent, "
                        + ":lastAccess, :hops, :createUserId, :updateUserId )";
            }
        }

        // DBを更新
        jdbc.update(sql, sqlParam);

    }

    @Override
    public void updateUnRegisteredMachine(PowerOnParameter param) {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        MapSqlParameterSource params = new MapSqlParameterSource("serial",
                param.getSerial()).addValue("gameId", param.getGameId())
                .addValue("placeIp", param.getIp())
                .addValue("createUserId", USER_ID)
                .addValue("updateUserId", USER_ID);

        // 未登録基板情報を更新
        String sql = "UPDATE unregistered_machines u SET "
                + "place_ip = :placeIp, game_id = :gameId, "
                + "last_access = CURRENT_TIMESTAMP, "
                + "update_date = CURRENT_TIMESTAMP, "
                + "update_user_id = :updateUserId WHERE serial = :serial";
        int affected = jdbc.update(sql, params);
        if (affected < 1) {
            // 更新できなかったので登録
            sql = "INSERT INTO unregistered_machines (serial, place_ip, game_id, last_access, create_user_id, update_user_id ) "
                    + "VALUES (:serial, :placeIp, :gameId, CURRENT_TIMESTAMP, :createUserId, :updateUserId )";
            jdbc.update(sql, params);
        }
    }

    @Override
    public void deleteUnRegisteredMachine(PowerOnParameter param) {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);
        MapSqlParameterSource params = new MapSqlParameterSource("serial",
                param.getSerial());

        String sql = "DELETE FROM unregistered_machines u WHERE serial = :serial";

        // 未登録基板情報を削除
        jdbc.update(sql, params);
    }

    @Override
    public void insertMachine(PowerOnData data) {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        MapSqlParameterSource params = new MapSqlParameterSource("serial", data
                .getMachine().getSerial())
                .addValue("allnetId", data.getMachine().getAllnetId())
                .addValue("gameId", data.getMachine().getGameId())
                .addValue("placeId", data.getMachine().getPlaceId())
                .addValue("createUserId", USER_ID)
                .addValue("updateUserId", USER_ID);

        String sql = "INSERT INTO machines (serial, allnet_id, game_id, place_id, create_user_id, update_user_id) "
                + "VALUES (:serial, :allnetId, :gameId, :placeId, :createUserId, :updateUserId)";

        jdbc.update(sql, params);

    }

    @Override
    public void updateMachine(PowerOnData data) {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        MapSqlParameterSource params = new MapSqlParameterSource("serial", data
                .getMachine().getSerial())
                .addValue("allnetId", data.getRouter().getAllnetId())
                .addValue("index", DEFAULT_INDEX)
                .addValue("placeId", data.getRouter().getPlaceId())
                .addValue("updateUserId", USER_ID);

        String sql = "UPDATE machines SET allnet_id = :allnetId, group_index = :index, place_id = :placeId, "
                + "register_timestamp = CURRENT_TIMESTAMP, update_date = CURRENT_TIMESTAMP, update_user_id = :updateUserId "
                + "WHERE serial = :serial";

        jdbc.update(sql, params);
    }

    @Override
    public void insertLog(PowerOnParameter param, int stat, int cause,
            Router router, String res, String debugInfo, String debugInfoPlace) {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        MapSqlParameterSource params = new MapSqlParameterSource("serial",
                param.getSerial()).addValue("stat", stat)
                .addValue("cause", cause).addValue("gameId", param.getGameId())
                .addValue("gameVer", param.getVer())
                .addValue("placeId", router.getPlaceId())
                .addValue("countryCode", router.getCountryCode())
                .addValue("placeIp", param.getIp())
                .addValue("globalIp", param.getGlobalIp())
                .addValue("allnetId", router.getAllnetId())
                .addValue("request", param.getQueryString())
                .addValue("response", res).addValue("debugInfo", debugInfo)
                .addValue("debugInfoPlace", debugInfoPlace)
                .addValue("createUserId", USER_ID);

        String sql = "INSERT INTO logs (serial, stat, cause, game_id, game_ver, place_id, country_code, place_ip, global_ip, allnet_id, request, response, debug_info, debug_info_place, create_user_id) "
                + "VALUES (:serial, :stat, :cause, :gameId, :gameVer, :placeId, :countryCode, :placeIp, :globalIp, :allnetId, :request, :response, :debugInfo, :debugInfoPlace, :createUserId)";

        jdbc.update(sql, params);
    }

    @Override
    public void deleteDeliverReport(PowerOnData data) {
        Machine m = data.getMachine();
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);
        jdbc.update("DELETE FROM app_deliver_reports WHERE serial = :serial",
                new MapSqlParameterSource("serial", m.getSerial()));
        jdbc.update("DELETE FROM opt_deliver_reports WHERE serial = :serial",
                new MapSqlParameterSource("serial", m.getSerial()));
    }

    @Override
    public boolean checkPrdCheckGame(String gameId) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT COUNT(*) FROM prd_check_games WHERE game_id = :gameId";

        int result = jdbc.queryForObject(sql,
                new MapSqlParameterSource().addValue("gameId", gameId), Integer.class);
        if (result > 0) {
            return true;
        }
        return false;

    }

    @Override
    public Integer findKeychipStat(String serial, String gameId) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        try {
            String sql = "SELECT keychip_stat FROM v_keychips WHERE keychip_sn = :serial AND game_id = :gameId";

            return jdbc.queryForObject(sql,
                    new MapSqlParameterSource().addValue("serial", serial)
                            .addValue("gameId", gameId), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    /**
     * ルータ情報取得処理
     * 
     * @param ip
     * @return
     */
    private Router findRouter(String ip) {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        SqlParameterSource params = new MapSqlParameterSource("ip", ip)
                .addValue("convertedIp", convertRouterIp(ip));

        String sql = "SELECT r.router_id, p.allnet_id, p.place_id, p.name, p.bill_code, p.nickname, p.country_code, p.region0_id, r0.name as r0_name, r1.name as r1_name, r2.name as r2_name, r3.name as r3_name, p.timezone "
                + "FROM routers r LEFT OUTER JOIN places p ON r.allnet_id = p.allnet_id "
                + "LEFT OUTER JOIN region0 r0 ON p.country_code = r0.country_code AND p.region0_id = r0.region_id "
                + "LEFT OUTER JOIN region1 r1 ON p.country_code = r1.country_code AND p.region1_id = r1.region_id "
                + "LEFT OUTER JOIN region2 r2 ON p.country_code = r2.country_code AND p.region2_id = r2.region_id "
                + "lEFT OUTER JOIN region3 r3 ON p.country_code = r3.country_code AND p.region3_id = r3.region_id "
                + "WHERE r.place_ip = :ip OR r.place_ip = :convertedIp";
        try {
            return jdbc.queryForObject(sql, params, new RowMapper<Router>() {

                @Override
                public Router mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    Router router = new Router();

                    router.setRouterId(rs.getString("router_id"));
                    router.setAllnetId(rs.getBigDecimal("allnet_id"));
                    router.setPlaceId(rs.getString("place_id"));
                    router.setPlaceName(rs.getString("name"));
                    router.setBillCode(rs.getString("bill_code"));
                    router.setPlaceNickName(rs.getString("nickname"));
                    router.setCountryCode(rs.getString("country_code"));
                    router.setRegion0Id(rs.getInt("region0_id"));
                    router.setRegion0Name(rs.getString("r0_name"));
                    router.setRegion1Name(rs.getString("r1_name"));
                    router.setRegion2Name(rs.getString("r2_name"));
                    router.setRegion3Name(rs.getString("r3_name"));
                    router.setTimezone(rs.getString("timezone"));

                    return router;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return new Router();
        }
    }

    /**
     * ゲーム情報取得処理
     * 
     * @param gameId
     * @param gameVer
     * @param countryCode
     * @return
     */
    private Game findGame(String gameId, String gameVer, String countryCode) {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        SqlParameterSource params = new MapSqlParameterSource("gameId", gameId)
                .addValue("gameVer", gameVer).addValue("countryCode",
                        countryCode);

        String sql = "SELECT g.game_id, g.game_ver, g.auth, g.uri, g.host from game_attributes g "
                + "WHERE g.game_id = :gameId AND g.game_ver = :gameVer "
                + "AND g.country_code = :countryCode";
        try {
            return jdbc.queryForObject(sql, params, new RowMapper<Game>() {

                @Override
                public Game mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    Game game = new Game();

                    game.setGameId(rs.getString("game_id"));
                    game.setGameVer(rs.getString("game_ver"));
                    game.setAuth(rs.getInt("auth"));
                    game.setUri(rs.getString("uri"));
                    game.setHost(rs.getString("host"));

                    return game;

                }
            });
        } catch (EmptyResultDataAccessException e) {
            return new Game();
        }
    }

    /**
     * 基板情報取得処理
     * 
     * @param serial
     * @return
     */
    private Machine findMachine(String serial) {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        SqlParameterSource params = new MapSqlParameterSource("serial", serial);

        String sql = "SELECT m.serial, ms.serial as stat_serial, m.game_id, m.reserved_game_id, m.setting, p.allnet_id, m.place_id, p.name, p.bill_code, "
                + "p.nickname, p.country_code, p.region0_id, r0.name AS r0_name, r1.name AS r1_name, r2.name AS r2_name, r3.name AS r3_name, p.timezone "
                + "FROM machines m LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial LEFT OUTER JOIN places p ON m.allnet_id = p.allnet_id "
                + "LEFT OUTER JOIN region0 r0 ON p.country_code = r0.country_code AND p.region0_id = r0.region_id "
                + "LEFT OUTER JOIN region1 r1 ON p.country_code = r1.country_code AND p.region1_id = r1.region_id "
                + "LEFT OUTER JOIN region2 r2 ON p.country_code = r2.country_code AND p.region2_id = r2.region_id "
                + "LEFT OUTER JOIN region3 r3 ON p.country_code = r3.country_code AND p.region3_id = r3.region_id "
                + "WHERE m.serial = :serial";
        try {
            return jdbc.queryForObject(sql, params, new RowMapper<Machine>() {

                @Override
                public Machine mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    Machine machine = new Machine();

                    machine.setSerial(rs.getString("serial"));
                    machine.setGameId(rs.getString("game_id"));
                    machine.setReservedGameId(rs.getString("reserved_game_id"));
                    machine.setSetting(rs.getInt("setting"));
                    machine.setAllnetId(rs.getBigDecimal("allnet_id"));
                    machine.setPlaceId(rs.getString("place_id"));
                    machine.setPlaceName(rs.getString("name"));
                    machine.setBillCode(rs.getString("bill_code"));
                    machine.setPlaceNickName(rs.getString("nickname"));
                    machine.setCountryCode(rs.getString("country_code"));
                    machine.setRegion0Id(rs.getInt("region0_id"));
                    machine.setRegion0Name(rs.getString("r0_name"));
                    machine.setRegion1Name(rs.getString("r1_name"));
                    machine.setRegion2Name(rs.getString("r2_name"));
                    machine.setRegion3Name(rs.getString("r3_name"));
                    machine.setTimezone(rs.getString("timezone"));

                    if (rs.getString("stat_serial") != null) {
                        machine.setExistStatus(true);
                    }

                    return machine;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return new Machine();
        }
    }

    /**
     * 基板IPが送られてきた場合は末尾が254なルータIPに変換する
     * 
     * @param ip
     * @return
     */
    private String convertRouterIp(String ip) {
        if (ip == null)
            return ip;
        if (ip.trim().endsWith(ROUTER_IP_END_STRING) || ip.lastIndexOf(".") < 0)
            return ip.trim();
        return ip.substring(0, ip.lastIndexOf(".") + 1) + ROUTER_IP_END_STRING;
    }
}
