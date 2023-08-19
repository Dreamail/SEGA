/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.sql.DataSource;

import jp.co.sega.allnet.auth.api.domain.ReportImage;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author NakanoY
 * 
 */
@Component("reportDao")
@Scope("singleton")
public class ReportDaoBean implements ReportDao {

    @Resource(name = "dataSource")
    private DataSource _dataSource;

    @Override
    public void updateAppDeliverReport(ReportImage image) {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        MapSqlParameterSource params = new MapSqlParameterSource("serial",
                image.getSerial()).addValue("rfState", image.getRfState())
                .addValue("tsc", image.getTsc())
                .addValue("tdsc", image.getTdsc())
                .addValue("ot", new Timestamp(image.getOt()))
                .addValue("rt", new Timestamp(image.getRt()))
                .addValue("at", new Timestamp(image.getAt()))
                .addValue("as", image.getAs())
                .addValue("gd", image.getGd())
                .addValue("dav", image.getDav())
                .addValue("wdav", image.getWdav())
                .addValue("dov", image.getDov())
                .addValue("wdov", image.getWdov())
                .addValue("dfl", createStr(image.getDfl()))
                .addValue("wfl", createStr(image.getWfl()))
                .addValue("updateUserId", USER_ID);

        String dmlUpdate = "UPDATE app_deliver_reports SET "
                + "download_state = :rfState, segs_total = :tsc, segs_downloaded = :tdsc, order_time = :ot, release_time = :rt, auth_time = :at, "
                + "auth_state = :as, description = :gd, ap_ver_released = :dav, ap_ver_working = :wdav, os_ver_released = :dov, os_ver_working = :wdov, "
                + "files_released = :dfl, files_working = :wfl, update_date = CURRENT_TIMESTAMP, update_user_id = :updateUserId "
                + "WHERE serial = :serial";
        String dmlInsert = "INSERT INTO app_deliver_reports (serial, download_state, segs_total, segs_downloaded, order_time, release_time, auth_time, auth_state, description, ap_ver_released, "
                + "ap_ver_working, os_ver_released, os_ver_working, files_released, files_working, create_user_id, update_user_id)"
                + "VALUES (:serial, :rfState, :tsc, :tdsc, :ot, :rt, :at, :as, :gd, :dav, "
                + ":wdav, :dov, :wdov, :dfl, :wfl, :createUserId, :updateUserId)";

        // 配信レポート情報を登録・更新
        int affected = jdbc.update(dmlUpdate, params);
        if (affected < 1) {
            jdbc.update(dmlInsert, params.addValue("createUserId", USER_ID));
        }
    }

    @Override
    public void updateOptDeliverReport(ReportImage image) {

    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        MapSqlParameterSource params = new MapSqlParameterSource("serial",
                image.getSerial()).addValue("rfState", image.getRfState())
                .addValue("tsc", image.getTsc())
                .addValue("tdsc", image.getTdsc())
                .addValue("ot", new Timestamp(image.getOt()))
                .addValue("rt", new Timestamp(image.getRt()))
                .addValue("at", new Timestamp(image.getAt()))
                .addValue("as", image.getAs()).addValue("gd", image.getGd())
                .addValue("dav", image.getDav())
                .addValue("dov", image.getDov())
                .addValue("dfl", createStr(image.getDfl()))
                .addValue("wfl", createStr(image.getWfl()))
                .addValue("updateUserId", USER_ID);

        String dmlUpdate = "UPDATE opt_deliver_reports SET "
                + "download_state = :rfState, segs_total = :tsc, segs_downloaded = :tdsc, order_time = :ot, release_time =:rt, auth_time = :at, "
                + "auth_state = :as, description = :gd, ap_ver_released = :dav, os_ver_released = :dov, "
                + "files_released = :dfl, files_working = :wfl, update_date = CURRENT_TIMESTAMP, update_user_id = :updateUserId "
                + "WHERE serial = :serial";
        String dmlInsert = "INSERT INTO opt_deliver_reports (serial, download_state, segs_total, segs_downloaded, order_time, release_time, auth_time, auth_state, description, ap_ver_released, "
                + "os_ver_released, files_released, files_working, create_user_id, update_user_id)"
                + "VALUES (:serial, :rfState, :tsc, :tdsc, :ot, :rt, :at, :as, :gd, :dav, "
                + ":dov, :dfl, :wfl, :createUserId, :updateUserId)";

        // 配信レポート情報を登録・更新
        int affected = jdbc.update(dmlUpdate, params);
        if (affected < 1) {
            jdbc.update(dmlInsert, params.addValue("createUserId", USER_ID));
        }

    }

    private String createStr(String[] str) {

        StringBuffer sb = new StringBuffer("");
        for (String s : str) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(s);
        }

        return sb.toString();
    }

}
