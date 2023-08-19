/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author TsuboiY
 * 
 */
@Component("apiAccountDao")
@Scope("singleton")
public class ApiAccountDaoBean implements ApiAccountDao {

    @Resource(name = "dataSource")
    private DataSource _dataSource;

    @Override
    public boolean checkApiAccount(String gameId, String password) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT COUNT(*) FROM title_api_accounts "
                + "WHERE game_id = :gameId AND password = :password";

        int count = jdbc.queryForObject(sql, 
        		new MapSqlParameterSource("gameId",
                gameId).addValue("password", password), Integer.class);

        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public ApiAccountStatus checkApiAccountAndPlaceAll(String gameId,
            String password) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT get_place_list_all FROM title_api_accounts "
                + "WHERE game_id = :gameId AND password = :password";

        try {
            String result = jdbc.queryForObject(sql, new MapSqlParameterSource("gameId", gameId).addValue("password", password), String.class); 
            if (result.equals(GET_PLACE_LIST_ALL_TRUE)) {
                return ApiAccountStatus.OK_PLACE_ALL;
            }
            return ApiAccountStatus.OK;
        } catch (EmptyResultDataAccessException e) {
            return ApiAccountStatus.NG;
        }
    }

}
