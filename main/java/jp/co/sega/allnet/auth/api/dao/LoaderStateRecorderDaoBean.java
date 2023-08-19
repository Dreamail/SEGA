/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import javax.annotation.Resource;
import javax.sql.DataSource;

import jp.co.sega.allnet.auth.api.domain.LoaderStateLog;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author TsuboiY
 * 
 */
@Component("loaderStateRecorderDao")
@Scope("singleton")
public class LoaderStateRecorderDaoBean implements LoaderStateRecorderDao {

    @Resource(name = "dataSource")
    private DataSource _dataSource;

    @Override
    public boolean checkExist(String serial) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "SELECT COUNT(*) FROM loader_state_logs "
                + "WHERE serial = :serial";

        int count = jdbc.queryForObject(sql, new MapSqlParameterSource("serial", serial), Integer.class);

        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void insertLoaderState(LoaderStateLog loaderStateLog) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "INSERT INTO loader_state_logs (serial, dvd, net, work, old_net, deliver, files_to_download, files_downloaded, last_auth, last_auth_state, download_state, create_user_id, update_user_id) "
                + "VALUES (:serial, :dvd, :net, :work, :oldNet, :deliver, :filesToDownload, :filesDownloaded, :lastAuth, :lastAuthState, :downloadState, :userId, :userId)";

        MapSqlParameterSource param = new MapSqlParameterSource("serial",
                loaderStateLog.getSerial())
                .addValue("dvd", loaderStateLog.getDvd())
                .addValue("net", loaderStateLog.getNet())
                .addValue("work", loaderStateLog.getWork())
                .addValue("oldNet", loaderStateLog.getOldNet())
                .addValue("deliver", loaderStateLog.getDeliver())
                .addValue("filesToDownload",
                        loaderStateLog.getFilesToDownload())
                .addValue("filesDownloaded",
                        loaderStateLog.getFilesDownloaded())
                .addValue("lastAuth", loaderStateLog.getLastAuth())
                .addValue("lastAuthState", loaderStateLog.getLastAuthState())
                .addValue("downloadState", loaderStateLog.getDownloadState())
                .addValue("userId", USER_ID);

        jdbc.update(sql, param);
    }

    @Override
    public void updateLoaderState(LoaderStateLog loaderStateLog) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_dataSource);

        String sql = "UPDATE loader_state_logs SET " + "dvd = :dvd, "
                + "net = :net, " + "work = :work, " + "old_net = :oldNet, "
                + "deliver = :deliver, "
                + "files_to_download = :filesToDownload, "
                + "files_downloaded = :filesDownloaded, "
                + "last_auth = :lastAuth, "
                + "last_auth_state = :lastAuthState, "
                + "download_state = :downloadState, "
                + "receipt_date = CURRENT_TIMESTAMP, "
                + "update_date = CURRENT_TIMESTAMP, "
                + "update_user_id = :userId " + "WHERE serial = :serial";

        MapSqlParameterSource param = new MapSqlParameterSource("serial",
                loaderStateLog.getSerial())
                .addValue("dvd", loaderStateLog.getDvd())
                .addValue("net", loaderStateLog.getNet())
                .addValue("work", loaderStateLog.getWork())
                .addValue("oldNet", loaderStateLog.getOldNet())
                .addValue("deliver", loaderStateLog.getDeliver())
                .addValue("filesToDownload",
                        loaderStateLog.getFilesToDownload())
                .addValue("filesDownloaded",
                        loaderStateLog.getFilesDownloaded())
                .addValue("lastAuth", loaderStateLog.getLastAuth())
                .addValue("lastAuthState", loaderStateLog.getLastAuthState())
                .addValue("downloadState", loaderStateLog.getDownloadState())
                .addValue("userId", USER_ID);

        jdbc.update(sql, param);
    }

}
