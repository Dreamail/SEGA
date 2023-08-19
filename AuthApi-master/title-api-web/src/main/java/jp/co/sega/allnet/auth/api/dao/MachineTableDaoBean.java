/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import jp.co.sega.allnet.auth.api.domain.Machine;
import jp.co.sega.allnet.auth.api.domain.MachineTable;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author TsuboiY
 * 
 */
@Component("machineTableDao")
@Scope("singleton")
public class MachineTableDaoBean implements MachineTableDao {

    @Resource(name = "dataSource")
    private DataSource _dataSource;

    @Override
    public List<MachineTable> findMachineTables(String gameId) {
        return findMachine(gameId, false);
    }

    @Override
    public List<MachineTable> findMachineTablesIncludeAuthDenied(String gameId) {
        return findMachine(gameId, true);
    }
    
    @Override
    public List<Machine> findMachines(String gameId) {
        return findMachines(gameId, false);
    }
    
    @Override
    public List<Machine> findMachinesIncludeAuthDenied(String gameId) {
        return findMachines(gameId, true);
    }

    /**
     * MachineTable用の基板情報を取得する。
     * 
     * @param gameId
     * @param includeAuthDenied
     * @return
     */
    private List<MachineTable> findMachine(String gameId,
            boolean includeAuthDenied) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT p.region0_id, p.place_id, p.name, m.serial, m.group_index, s.last_access, s.last_auth "
                + "FROM machines m INNER JOIN places p ON m.allnet_id = p.allnet_id "
                + "LEFT OUTER JOIN machine_statuses s ON m.serial = s.serial "
                + "WHERE (m.game_id = :gameId OR m.reserved_game_id = :gameId)";

        MapSqlParameterSource param = new MapSqlParameterSource("gameId",
                gameId);

        if (!includeAuthDenied) {
            sql += " AND m.setting = :setting";
            param.addValue("setting", SETTING_COMM_OK);
        }
        sql += " ORDER BY p.region0_id, p.place_id, m.group_index, m.serial";

        try {
            return jdbc.queryForObject(sql, param,
                    new RowMapper<List<MachineTable>>() {

                        @Override
                        public List<MachineTable> mapRow(ResultSet rs,
                                int rowNum) throws SQLException {
                            List<MachineTable> list = new ArrayList<MachineTable>();
                            do {
                                MachineTable m = new MachineTable();
                                m.setRegion0Id(rs.getBigDecimal("region0_id"));
                                m.setPlaceId(rs.getString("place_id"));
                                m.setName(rs.getString("name"));
                                m.setSerial(rs.getString("serial"));
                                m.setGroupIndex(rs.getInt("group_index"));
                                m.setLastAccess(rs.getTimestamp("last_access"));
                                m.setLastAuth(rs.getTimestamp("last_auth"));
                                list.add(m);
                            } while (rs.next());
                            return list;
                        }
                    });
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }
    
    /**
     * GetMachineList用の基板情報を取得する。
     * 
     * @param gameId
     * @param includeAuthDenied
     * @return
     */
    private List<Machine> findMachines(String gameId,
            boolean includeAuthDenied) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT m.serial, m.allnet_id, m.place_id, s.last_access, s.last_auth "
                + "FROM machines m LEFT OUTER JOIN machine_statuses s ON m.serial = s.serial "
                + "WHERE (m.game_id = :gameId OR m.reserved_game_id = :gameId)";

        MapSqlParameterSource param = new MapSqlParameterSource("gameId",
                gameId);

        if (!includeAuthDenied) {
            sql += " AND m.setting = :setting";
            param.addValue("setting", SETTING_COMM_OK);
        }
        sql += " ORDER BY m.allnet_id, m.serial";

        try {
            return jdbc.queryForObject(sql, param,
                    new RowMapper<List<Machine>>() {

                        @Override
                        public List<Machine> mapRow(ResultSet rs,
                                int rowNum) throws SQLException {
                            List<Machine> list = new ArrayList<Machine>();
                            do {
                                Machine m = new Machine();
                                m.setAllnetId(rs.getLong("allnet_id"));
                                m.setPlaceId(rs.getString("place_id"));
                                m.setSerial(rs.getString("serial"));
                                m.setLastAccess(rs.getTimestamp("last_access"));
                                m.setLastAuth(rs.getTimestamp("last_auth"));
                                list.add(m);
                            } while (rs.next());
                            return list;
                        }
                    });
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

}
