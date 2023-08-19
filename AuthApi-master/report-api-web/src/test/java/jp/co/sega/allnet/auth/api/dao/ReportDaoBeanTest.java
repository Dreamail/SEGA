/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sega.allnet.auth.api.domain.ReportImage;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

/**
 * @author TsuboiY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "dao-test-context.xml" })
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class ReportDaoBeanTest {

    @Resource(name = "dataSource")
    private DataSource _ds;

    @Resource(name = "reportDao")
    private ReportDao _dao;

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.ReportDaoBean#updateAppDeliverReport(jp.co.sega.allnet.auth.api.domain.ReportImage)}
     * .
     * 
     * @throws ParseException
     */
    @Test
    public void testUpdateAppDeliverReportSuccessExistReport()
            throws ParseException {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime());
        image.setOt(ot.getTime());
        image.setRt(rt.getTime());
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        String user = "TestUser";

        Timestamp now = new Timestamp(new Date().getTime());
        MapSqlParameterSource params = new MapSqlParameterSource("serial",
                image.getSerial()).addValue("dfl", null).addValue("wfl", null)
                        .addValue("tsc", 0).addValue("tdsc", 0)
                        .addValue("at", null).addValue("ot", null)
                        .addValue("rt", null).addValue("as", 0)
                        .addValue("rfState", 0).addValue("gd", null)
                        .addValue("dav", null).addValue("wdav", "0.0")
                        .addValue("dov", null).addValue("wdov", null)
                        .addValue("now", now).addValue("userId", user);
        int affected = jdbc.update("UPDATE app_deliver_reports SET "
                + "files_released = :dfl, files_working = :wfl, segs_total = :tsc, segs_downloaded = :tdsc, auth_time = :at, order_time = :ot, release_time = :rt, auth_state = :as, download_state = :rfState, description = :gd, ap_ver_released = :dav, ap_ver_working = :wdav, os_ver_released = :dov, os_ver_working = :wdov, update_date = :now, update_user_id = :userId "
                + "WHERE serial = :serial", params);
        if (affected < 1) {
            jdbc.update("INSERT INTO app_deliver_reports ("
                    + "serial, files_released, files_working, segs_total, segs_downloaded, auth_time, order_time, release_time, auth_state, download_state, description, ap_ver_released, ap_ver_working, os_ver_released, os_ver_working, create_date, create_user_id, update_date, update_user_id"
                    + ") VALUES ("
                    + ":serial, :dfl, :wfl, :tsc, :tdsc, :at, :ot, :rt, :as, :rfState, :gd, :dav, :wdav, :dov, :wdov, :now, :userId, :now, :userId "
                    + ")", params);
        }

        _dao.updateAppDeliverReport(image);

        Map<String, Object> m = jdbc.getJdbcTemplate().queryForMap(
                "SELECT * FROM app_deliver_reports WHERE serial = ?",
                image.getSerial());

        assertEquals(dflstr, m.get("files_released"));
        assertEquals(wflstr, m.get("files_working"));
        assertEquals(image.getTsc().intValue(),
                ((BigDecimal) m.get("segs_total")).intValue());
        assertEquals(image.getTdsc().intValue(),
                ((BigDecimal) m.get("segs_downloaded")).intValue());
        assertEquals(at.getTime(), ((Timestamp) m.get("auth_time")).getTime());
        assertEquals(ot.getTime(), ((Timestamp) m.get("order_time")).getTime());
        assertEquals(rt.getTime(),
                ((Timestamp) m.get("release_time")).getTime());
        assertEquals(image.getAs().intValue(),
                ((BigDecimal) m.get("auth_state")).intValue());
        assertEquals(image.getRfState().intValue(),
                ((BigDecimal) m.get("download_state")).intValue());
        assertEquals(image.getGd(), m.get("description"));
        assertEquals(image.getDav(), m.get("ap_ver_released"));
        assertEquals(image.getWdav(), m.get("ap_ver_working"));
        assertEquals(image.getDov(), m.get("os_ver_released"));
        assertEquals(image.getWdov(), m.get("os_ver_working"));
        assertNotSame(now, m.get("update_date"));
        assertEquals(ReportDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.ReportDaoBean#updateAppDeliverReport(jp.co.sega.allnet.auth.api.domain.ReportImage)}
     * .
     */
    @Test
    public void testUpdateAppDeliverReportSuccessNotExistReport()
            throws ParseException {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime());
        image.setOt(ot.getTime());
        image.setRt(rt.getTime());
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        jdbc.getJdbcTemplate().update("DELETE FROM app_deliver_reports t WHERE t.serial = ?",
                image.getSerial());

        _dao.updateAppDeliverReport(image);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM app_deliver_reports WHERE serial = :serial",
                new MapSqlParameterSource("serial", image.getSerial()));

        assertEquals(dflstr, m.get("files_released"));
        assertEquals(wflstr, m.get("files_working"));
        assertEquals(image.getTsc().intValue(),
                ((BigDecimal) m.get("segs_total")).intValue());
        assertEquals(image.getTdsc().intValue(),
                ((BigDecimal) m.get("segs_downloaded")).intValue());
        assertEquals(at.getTime(), ((Timestamp) m.get("auth_time")).getTime());
        assertEquals(ot.getTime(), ((Timestamp) m.get("order_time")).getTime());
        assertEquals(rt.getTime(),
                ((Timestamp) m.get("release_time")).getTime());
        assertEquals(image.getAs().intValue(),
                ((BigDecimal) m.get("auth_state")).intValue());
        assertEquals(image.getRfState().intValue(),
                ((BigDecimal) m.get("download_state")).intValue());
        assertEquals(image.getGd(), m.get("description"));
        assertEquals(image.getDav(), m.get("ap_ver_released"));
        assertEquals(image.getWdav(), m.get("ap_ver_working"));
        assertEquals(image.getDov(), m.get("os_ver_released"));
        assertEquals(image.getWdov(), m.get("os_ver_working"));
        assertNotNull(m.get("create_date"));
        assertEquals(ReportDao.USER_ID, m.get("create_user_id"));
        assertNotNull(m.get("update_date"));
        assertEquals(ReportDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.ReportDaoBean#updateOptDeliverReport(jp.co.sega.allnet.auth.api.domain.ReportImage)}
     * .
     */
    @Test
    public void testUpdateOptDeliverReportSuccessExistReport()
            throws ParseException {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime());
        image.setOt(ot.getTime());
        image.setRt(rt.getTime());
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        String user = "TestUser";

        Timestamp now = new Timestamp(new Date().getTime());
        MapSqlParameterSource params = new MapSqlParameterSource("serial",
                image.getSerial()).addValue("dfl", null).addValue("wfl", null)
                        .addValue("tsc", 0).addValue("tdsc", 0)
                        .addValue("at", null).addValue("ot", null)
                        .addValue("rt", null).addValue("as", 0)
                        .addValue("rfState", 0).addValue("gd", null)
                        .addValue("dav", null).addValue("dov", null)
                        .addValue("now", now).addValue("userId", user);
        int affected = jdbc.update("UPDATE opt_deliver_reports SET "
                + "files_released = :dfl, files_working = :wfl, segs_total = :tsc, segs_downloaded = :tdsc, auth_time = :at, order_time = :ot, release_time = :rt, auth_state = :as, download_state = :rfState, description = :gd, ap_ver_released = :dav,  os_ver_released = :dov, update_date = :now, update_user_id = :userId "
                + "WHERE serial = :serial", params);
        if (affected < 1) {
            jdbc.update("INSERT INTO opt_deliver_reports ("
                    + "serial, files_released, files_working, segs_total, segs_downloaded, auth_time, order_time, release_time, auth_state, download_state, description, ap_ver_released, os_ver_released, create_date, create_user_id, update_date, update_user_id"
                    + ") VALUES ("
                    + ":serial, :dfl, :wfl, :tsc, :tdsc, :at, :ot, :rt, :as, :rfState, :gd, :dav, :dov, :now, :userId, :now, :userId "
                    + ")", params);
        }

        _dao.updateOptDeliverReport(image);

        Map<String, Object> m = jdbc.getJdbcTemplate().queryForMap(
                "SELECT * FROM opt_deliver_reports WHERE serial = ?",
                image.getSerial());

        assertEquals(dflstr, m.get("files_released"));
        assertEquals(wflstr, m.get("files_working"));
        assertEquals(image.getTsc().intValue(),
                ((BigDecimal) m.get("segs_total")).intValue());
        assertEquals(image.getTdsc().intValue(),
                ((BigDecimal) m.get("segs_downloaded")).intValue());
        assertEquals(at.getTime(), ((Timestamp) m.get("auth_time")).getTime());
        assertEquals(ot.getTime(), ((Timestamp) m.get("order_time")).getTime());
        assertEquals(rt.getTime(),
                ((Timestamp) m.get("release_time")).getTime());
        assertEquals(image.getAs().intValue(),
                ((BigDecimal) m.get("auth_state")).intValue());
        assertEquals(image.getRfState().intValue(),
                ((BigDecimal) m.get("download_state")).intValue());
        assertEquals(image.getGd(), m.get("description"));
        assertEquals(image.getDav(), m.get("ap_ver_released"));
        assertNull(m.get("ap_ver_working"));
        assertEquals(image.getDov(), m.get("os_ver_released"));
        assertNull(m.get("os_ver_working"));
        assertNotSame(now, m.get("update_date"));
        assertEquals(ReportDao.USER_ID, m.get("update_user_id"));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.ReportDaoBean#updateOptDeliverReport(jp.co.sega.allnet.auth.api.domain.ReportImage)}
     * .
     */
    @Test
    public void testUpdateOptDeliverReportSuccessNotExistReport()
            throws ParseException {
        JdbcTemplate jdbc = new JdbcTemplate(_ds);

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime());
        image.setOt(ot.getTime());
        image.setRt(rt.getTime());
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        jdbc.update("DELETE FROM opt_deliver_reports WHERE serial = ?",
                image.getSerial());

        _dao.updateOptDeliverReport(image);

        Map<String, Object> m = jdbc.queryForMap(
                "SELECT * FROM opt_deliver_reports WHERE serial = ?",
                image.getSerial());

        assertEquals(dflstr, m.get("files_released"));
        assertEquals(wflstr, m.get("files_working"));
        assertEquals(image.getTsc().intValue(),
                ((BigDecimal) m.get("segs_total")).intValue());
        assertEquals(image.getTdsc().intValue(),
                ((BigDecimal) m.get("segs_downloaded")).intValue());
        assertEquals(at.getTime(), ((Timestamp) m.get("auth_time")).getTime());
        assertEquals(ot.getTime(), ((Timestamp) m.get("order_time")).getTime());
        assertEquals(rt.getTime(),
                ((Timestamp) m.get("release_time")).getTime());
        assertEquals(image.getAs().intValue(),
                ((BigDecimal) m.get("auth_state")).intValue());
        assertEquals(image.getRfState().intValue(),
                ((BigDecimal) m.get("download_state")).intValue());
        assertEquals(image.getGd(), m.get("description"));
        assertEquals(image.getDav(), m.get("ap_ver_released"));
        assertNull(m.get("ap_ver_working"));
        assertEquals(image.getDov(), m.get("os_ver_released"));
        assertNull(m.get("os_ver_working"));
        assertNotNull(m.get("create_date"));
        assertEquals(ReportDao.USER_ID, m.get("create_user_id"));
        assertNotNull(m.get("update_date"));
        assertEquals(ReportDao.USER_ID, m.get("update_user_id"));
    }

}
