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

import jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrder;

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
@Component("downloadOrderDao")
@Scope("singleton")
public class DownloadOrderDaoBean implements DownloadOrderDao {

    @Resource(name = "dataSource")
    private DataSource _dataSource;

    @Override
    public DownloadOrder findMachineDownloadOrder(String serial, int type) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT m.serial, m.group_index, m.setting, p.allnet_id, p.country_code, d.uri "
                + "FROM machines m "
                + "INNER JOIN places p on m.allnet_id = p.allnet_id "
                + "LEFT OUTER JOIN machine_download_orders d on m.serial = d.serial and d.type = :type "
                + "WHERE m.serial = :serial";

        try {
			return jdbc.queryForObject(sql, new MapSqlParameterSource("serial", serial).addValue("type", type),
					new RowMapper<DownloadOrder>() {
						@Override
						public DownloadOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
							DownloadOrder order = new DownloadOrder();
							order.setSerial(rs.getString("serial"));
							order.setAllnetId(rs.getInt("allnet_id"));
							order.setCountryCode(rs.getString("country_code"));
							order.setGroupIndex(rs.getInt("group_index"));
							order.setSetting(rs.getInt("setting"));
							order.setUri(rs.getString("uri"));
							return order;
						}
					});
        } catch (EmptyResultDataAccessException e) {
            return new DownloadOrder();
        }
    }

    @Override
    public List<String> findGroupSerials(String serial, int allnetId,
            int groupIndex, String gameId) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT serial FROM machines "
                + "WHERE allnet_id = :allnetId AND game_id = :gameId "
                + "AND group_index = :groupIndex AND serial <> :serial";

        MapSqlParameterSource param = new MapSqlParameterSource("serial",
                serial).addValue("allnetId", allnetId)
                .addValue("gameId", gameId).addValue("groupIndex", groupIndex);
        try {
            return jdbc.queryForObject(sql, param, new RowMapper<List<String>>() {

                @Override
                public List<String> mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    List<String> list = new ArrayList<String>();
                    do {
                        list.add(rs.getString("serial"));
                    } while (rs.next());
                    return list;
                }

            });
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public String findUriByCountry(String gameId, String gameVer,
            String countryCode, int type) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT uri FROM country_download_orders "
                + "WHERE game_id = :gameId AND game_ver = :gameVer "
                + "AND country_code = :countryCode "
                + "AND type = :type";

        MapSqlParameterSource param = new MapSqlParameterSource("gameId",
                gameId).addValue("gameVer", gameVer).addValue("countryCode",
                countryCode).addValue("type", type);

        try {
            return jdbc.queryForObject(sql, param, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String findUriByGame(String gameId, String gameVer, int type) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT uri FROM download_orders "
                + "WHERE game_id = :gameId AND game_ver = :gameVer AND type = :type";

        MapSqlParameterSource param = new MapSqlParameterSource("gameId",
                gameId).addValue("gameVer", gameVer).addValue("type", type);

        try {
            return jdbc.queryForObject(sql, param, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
