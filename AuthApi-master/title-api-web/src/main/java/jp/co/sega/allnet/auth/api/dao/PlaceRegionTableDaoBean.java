/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import jp.co.sega.allnet.auth.api.domain.Place;
import jp.co.sega.allnet.auth.api.domain.Region;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author NakanoY
 * 
 */
@Component("placeRegionTableDao")
@Scope("singleton")
public class PlaceRegionTableDaoBean implements PlaceRegionTableDao {

    @Resource(name = "dataSource")
    private DataSource _dataSource;

    @Override
    public List<Place> findPlaces(String gameId, int activeDays,
            String placeType) {
        return findPlaces(gameId, null, false, activeDays, placeType);
    }

    @Override
    public List<Place> findPlaces(String gameId, String countryCode,
            int activeDays, String placeType) {
        return findPlaces(gameId, countryCode, false, activeDays, placeType);
    }

    @Override
    public List<Place> findPlacesIncludeAuthDenied(String gameId,
            int activeDays, String placeType) {
        return findPlaces(gameId, null, true, activeDays, placeType);
    }

    @Override
    public List<Place> findPlacesIncludeAuthDenied(String gameId,
            String countryCode, int activeDays, String placeType) {
        return findPlaces(gameId, countryCode, true, activeDays, placeType);
    }

    @Override
    public List<Place> findPlacesAll() {
        JdbcTemplate jdbc = new JdbcTemplate(_dataSource);

        String sql = "SELECT p.* FROM places p ORDER BY p.place_id";

        try {
            return jdbc.queryForObject(sql, new RowMapper<List<Place>>() {

                @Override
                public List<Place> mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    List<Place> list = new ArrayList<Place>();
                    do {
                        Place p = new Place();
                        p.setAllnetId(rs.getLong("allnet_id"));
                        p.setPlaceId(rs.getString("place_id"));
                        p.setName(rs.getString("name"));
                        p.setTel(rs.getString("tel"));
                        p.setAddress(rs.getString("address"));
                        p.setZipCode(rs.getString("zip_code"));
                        p.setRegion0Id(rs.getBigDecimal("region0_id"));
                        p.setRegion1Id(rs.getBigDecimal("region1_id"));
                        p.setRegion2Id(rs.getBigDecimal("region2_id"));
                        p.setRegion3Id(rs.getBigDecimal("region3_id"));
                        p.setStation(rs.getString("station"));
                        p.setOpenTime(rs.getString("open_time"));
                        p.setCloseTime(rs.getString("close_time"));
                        p.setSpecialInfo(rs.getString("special_info"));
                        p.setNickname(rs.getString("nickname"));
                        p.setBillCode(rs.getString("bill_code"));
                        p.setCountryCode(rs.getString("country_code"));
                        list.add(p);
                    } while (rs.next());
                    return list;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Region> findRegions(String countryCode, final int level) {
        if (!checkRegionLevel(level)) {
            throw new IllegalArgumentException("不正なレベルが指定されました");
        }
        JdbcTemplate jdbc = new JdbcTemplate(_dataSource);

        String sql = String
                .format("SELECT * FROM region%s WHERE country_code = ? ORDER BY region_id",
                        level);

        try {
            return jdbc.queryForObject(sql, new RowMapper<List<Region>>() {

                @Override
                public List<Region> mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    List<Region> list = new ArrayList<Region>();
                    do {

                        Region r = new Region();

                        r.setCountryCode(rs.getString("country_code"));
                        r.setLevel(level);
                        r.setName(rs.getString("name"));
                        if (level != 0) {
                            r.setParentRegionId(rs
                                    .getBigDecimal("parent_region_id"));
                        }
                        r.setRegionId(rs.getInt("region_id"));

                        list.add(r);

                    } while (rs.next());
                    return list;
                }

            }, new Object[] {countryCode});
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Region> findRegions(List<Place> places) {
        if (places.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> list = new ArrayList<String>();

        for (Place p : places) {
            // 店舗が存在する国を変数に保管
            if (list.contains(p.getCountryCode())) {
                continue;
            }
            list.add(p.getCountryCode());
        }

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT country_code, region_id, name, null AS parent_region_id, 0 AS region_level "
                + "FROM region0 WHERE country_code IN ( %s ) "
                + "UNION ALL SELECT country_code, region_id, name, parent_region_id, 1 AS region_level "
                + "FROM region1 WHERE country_code IN ( %1$s ) "
                + "UNION ALL SELECT country_code, region_id, name, parent_region_id, 2 AS region_level "
                + "FROM region2 WHERE country_code IN ( %1$s ) "
                + "UNION ALL SELECT country_code, region_id, name, parent_region_id, 3 AS region_level "
                + "FROM region3 WHERE country_code IN ( %1$s ) "
                + "ORDER BY country_code, region_level, region_id ";

        MapSqlParameterSource param = new MapSqlParameterSource();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                sb.append(" , ");
            }

            sb.append(String.format(":countryCode%s", i));
            param.addValue(String.format("countryCode%s", i), list.get(i));
        }

        try {
            return jdbc.queryForObject(String.format(sql, sb.toString()), param,
                    new RowMapper<List<Region>>() {

                        @Override
                        public List<Region> mapRow(ResultSet rs, int rowNum)
                                throws SQLException {
                            List<Region> list = new ArrayList<Region>();
                            do {

                                Region r = new Region();

                                r.setCountryCode(rs.getString("country_code"));
                                r.setLevel(rs.getInt("region_level"));
                                r.setName(rs.getString("name"));
                                if (r.getLevel() != 0) {
                                    r.setParentRegionId(rs
                                            .getBigDecimal("parent_region_id"));
                                }
                                r.setRegionId(rs.getInt("region_id"));

                                list.add(r);

                            } while (rs.next());
                            return list;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean checkBillOpenAllowedGame(String gameId) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT COUNT(*) FROM bill_open_allowed_games "
                + "WHERE game_id = :gameId";

        int count = jdbc.queryForObject(sql, new MapSqlParameterSource("gameId",
                gameId), Integer.class);

        if (count > 0) {
            return true;
        }
        return false;

    }

    /**
     * 店舗情報を取得する。
     * 
     * @param gameId
     * @param countryCode
     * @param includeAuthDenied
     * @return
     */
    private List<Place> findPlaces(String gameId, String countryCode,
            boolean includeAuthDenied, int activeDays, String placeType) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT DISTINCT p.* FROM places p "
                + "INNER JOIN machines m ON p.allnet_id = m.allnet_id ";

        if (activeDays > 0) {
            sql += "INNER JOIN machine_statuses ms ON m.serial = ms.serial ";
        }

        sql += "WHERE (m.game_id = :gameId OR m.reserved_game_id = :gameId) ";
        MapSqlParameterSource param = new MapSqlParameterSource("gameId",
                gameId);

        if (countryCode != null) {
            sql += " AND p.country_code = :countryCode";
            param.addValue("countryCode", countryCode);
        }
        if (!includeAuthDenied) {
            sql += " AND m.setting = :setting";
            param.addValue("setting", SETTING_COMM_OK);
        }
        if (activeDays > 0) {
            Date now = new Date();
            Calendar start = Calendar.getInstance();
            start.add(Calendar.DATE, activeDays * -1);
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            start.set(Calendar.MILLISECOND, 0);
            sql += " AND (ms.last_access BETWEEN :start AND :end)";
            param.addValue("start", start.getTime());
            param.addValue("end", now);
        }
        switch (PlaceType.fromValue(placeType)) {
        case TEST:
            sql += " AND p.allnet_id < 0";
            break;
        case REAL:
            sql += " AND p.allnet_id >= 0";
            break;
        }

        sql += " ORDER BY p.place_id";

        try {
            return jdbc.queryForObject(sql, param, new RowMapper<List<Place>>() {

                @Override
                public List<Place> mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    List<Place> list = new ArrayList<Place>();
                    do {
                        Place p = new Place();
                        p.setAllnetId(rs.getLong("allnet_id"));
                        p.setPlaceId(rs.getString("place_id"));
                        p.setName(rs.getString("name"));
                        p.setTel(rs.getString("tel"));
                        p.setAddress(rs.getString("address"));
                        p.setZipCode(rs.getString("zip_code"));
                        p.setRegion0Id(rs.getBigDecimal("region0_id"));
                        p.setRegion1Id(rs.getBigDecimal("region1_id"));
                        p.setRegion2Id(rs.getBigDecimal("region2_id"));
                        p.setRegion3Id(rs.getBigDecimal("region3_id"));
                        p.setStation(rs.getString("station"));
                        p.setOpenTime(rs.getString("open_time"));
                        p.setCloseTime(rs.getString("close_time"));
                        p.setSpecialInfo(rs.getString("special_info"));
                        p.setNickname(rs.getString("nickname"));
                        p.setBillCode(rs.getString("bill_code"));
                        p.setCountryCode(rs.getString("country_code"));
                        list.add(p);
                    } while (rs.next());
                    return list;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    /**
     * 地域レベルをチェックする。
     * 
     * @param level
     * @return
     */
    private boolean checkRegionLevel(int level) {
        for (int i : REGION_LEVEL_RANGE) {
            if (i == level) {
                return true;
            }
        }
        return false;
    }

    private enum PlaceType {
        TEST("test"), REAL("real"), UNKNOWN("");

        private String value;

        private PlaceType(String v) {
            value = v;
        }

        public static PlaceType fromValue(String v) {
            for (PlaceType t : PlaceType.values()) {
                if (t.value.equals(v)) {
                    return t;
                }
            }
            return PlaceType.UNKNOWN;
        }
    }

}
