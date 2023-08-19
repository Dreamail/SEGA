/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminview;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.Log;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TsuboiY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class AdminViewServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(AdminViewServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "adminViewService")
    private AdminViewService _service;

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminview.AdminViewServiceBean#adminView(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws ParseException
     * @throws IOException
     */
    @Test
    public final void testAdminViewNoTime() throws ParseException, IOException {
        String gameId = "SBXX";
        String date = null;
        String time = null;

        deleteLogs(gameId);

        Log l1_1 = createLogs("AZZZZZZZZZZ", gameId, "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l1_2 = createLogs("AZZZZZZZZZZ", gameId, "2011/11/14 11:00:00",
                "debugInfo_2011/11/14_11:00:00");
        Log l1_3 = createLogs("AZZZZZZZZZZ", "SBYY", "2011/11/14 12:00:00",
                "debugInfo_2011/11/14_12:00:00");
        Log l2_1 = createLogs("AYYYYYYYYYY", gameId, "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l2_2 = createLogs("AYYYYYYYYYY", gameId, "2011/1/14 11:00:00",
                "debugInfo_2011/1/14_11:00:00");
        Log l2_3 = createLogs("AYYYYYYYYYY", gameId, "2011/11/13 23:59:59",
                "debugInfo_2011/11/13 23:59:59");

        _em.flush();

        String ret = _service.adminView(gameId, date, time);
        _log.info(ret);

        BufferedReader reader = new BufferedReader(new StringReader(ret));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        assertEquals("START", reader.readLine());
        assertEquals(
                l2_1.getDebugInfo() + ",time="
                        + df.format(l2_1.getAccessDate()), reader.readLine());
        assertEquals(
                l1_2.getDebugInfo() + ",time="
                        + df.format(l1_2.getAccessDate()), reader.readLine());
        assertEquals("END", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminview.AdminViewServiceBean#adminView(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws ParseException
     * @throws IOException
     */
    @Test
    public final void testAdminViewTimeSpecified() throws ParseException,
            IOException {
        String gameId = "SBXX";
        String date = "20111113";
        String time = "235959";

        deleteLogs(gameId);

        Log l1_1 = createLogs("AZZZZZZZZZZ", gameId, "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l1_2 = createLogs("AZZZZZZZZZZ", gameId, "2011/11/14 11:00:00",
                "debugInfo_2011/11/14_11:00:00");
        Log l1_3 = createLogs("AZZZZZZZZZZ", "SBYY", "2011/11/14 12:00:00",
                "debugInfo_2011/11/14_12:00:00");
        Log l2_1 = createLogs("AYYYYYYYYYY", gameId, "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l2_2 = createLogs("AYYYYYYYYYY", gameId, "2011/1/14 11:00:00",
                "debugInfo_2011/1/14_11:00:00");
        Log l2_3 = createLogs("AYYYYYYYYYY", gameId, "2011/11/13 23:59:59",
                "debugInfo_2011/11/13 23:59:59");

        _em.flush();

        String ret = _service.adminView(gameId, date, time);
        _log.info(ret);

        BufferedReader reader = new BufferedReader(new StringReader(ret));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        assertEquals("START", reader.readLine());
        assertEquals(
                l2_1.getDebugInfo() + ",time="
                        + df.format(l2_1.getAccessDate()), reader.readLine());
        assertEquals(
                l1_1.getDebugInfo() + ",time="
                        + df.format(l1_1.getAccessDate()), reader.readLine());
        assertEquals(
                l1_2.getDebugInfo() + ",time="
                        + df.format(l1_2.getAccessDate()), reader.readLine());

        String lastLine = reader.readLine();
        assertEquals("END", lastLine.split(",")[0]);
        assertTrue(lastLine.split(",")[1].matches("[0-9]{14}"));
        assertNull(reader.readLine());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminview.AdminViewServiceBean#adminView(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws ParseException
     * @throws IOException
     */
    @Test
    public final void testAdminViewArgGameIdIsNull() throws ParseException,
            IOException {
        String gameId = null;
        String date = "20111113";
        String time = "235959";

        deleteLogs(gameId);

        Log l1_1 = createLogs("AZZZZZZZZZZ", "SBXX", "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l1_2 = createLogs("AZZZZZZZZZZ", "SBXX", "2011/11/14 11:00:00",
                "debugInfo_2011/11/14_11:00:00");
        Log l1_3 = createLogs("AZZZZZZZZZZ", "SBYY", "2011/11/14 12:00:00",
                "debugInfo_2011/11/14_12:00:00");
        Log l2_1 = createLogs("AYYYYYYYYYY", "SBXX", "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l2_2 = createLogs("AYYYYYYYYYY", "SBXX", "2011/1/14 11:00:00",
                "debugInfo_2011/1/14_11:00:00");
        Log l2_3 = createLogs("AYYYYYYYYYY", "SBXX", "2011/11/13 23:59:59",
                "debugInfo_2011/11/13 23:59:59");

        _em.flush();

        String ret = _service.adminView(gameId, date, time);
        _log.info(ret);

        BufferedReader reader = new BufferedReader(new StringReader(ret));
        assertEquals("START", reader.readLine());
        assertEquals("END", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminview.AdminViewServiceBean#adminView(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws ParseException
     * @throws IOException
     */
    @Test
    public final void testAdminViewArgDateIsNull() throws ParseException,
            IOException {
        String gameId = "SBXX";
        String date = null;
        String time = "235959";

        deleteLogs(gameId);

        Log l1_1 = createLogs("AZZZZZZZZZZ", gameId, "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l1_2 = createLogs("AZZZZZZZZZZ", gameId, "2011/11/14 11:00:00",
                "debugInfo_2011/11/14_11:00:00");
        Log l1_3 = createLogs("AZZZZZZZZZZ", "SBYY", "2011/11/14 12:00:00",
                "debugInfo_2011/11/14_12:00:00");
        Log l2_1 = createLogs("AYYYYYYYYYY", gameId, "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l2_2 = createLogs("AYYYYYYYYYY", gameId, "2011/1/14 11:00:00",
                "debugInfo_2011/1/14_11:00:00");
        Log l2_3 = createLogs("AYYYYYYYYYY", gameId, "2011/11/13 23:59:59",
                "debugInfo_2011/11/13 23:59:59");

        _em.flush();

        String ret = _service.adminView(gameId, date, time);
        _log.info(ret);

        BufferedReader reader = new BufferedReader(new StringReader(ret));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        assertEquals("START", reader.readLine());
        assertEquals(
                l2_1.getDebugInfo() + ",time="
                        + df.format(l2_1.getAccessDate()), reader.readLine());
        assertEquals(
                l1_2.getDebugInfo() + ",time="
                        + df.format(l1_2.getAccessDate()), reader.readLine());
        assertEquals("END", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminview.AdminViewServiceBean#adminView(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws ParseException
     * @throws IOException
     */
    @Test
    public final void testAdminViewArgTimeIsNull() throws ParseException,
            IOException {
        String gameId = "SBXX";
        String date = "20111113";
        String time = null;

        deleteLogs(gameId);

        Log l1_1 = createLogs("AZZZZZZZZZZ", gameId, "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l1_2 = createLogs("AZZZZZZZZZZ", gameId, "2011/11/14 11:00:00",
                "debugInfo_2011/11/14_11:00:00");
        Log l1_3 = createLogs("AZZZZZZZZZZ", "SBYY", "2011/11/14 12:00:00",
                "debugInfo_2011/11/14_12:00:00");
        Log l2_1 = createLogs("AYYYYYYYYYY", gameId, "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l2_2 = createLogs("AYYYYYYYYYY", gameId, "2011/1/14 11:00:00",
                "debugInfo_2011/1/14_11:00:00");
        Log l2_3 = createLogs("AYYYYYYYYYY", gameId, "2011/11/13 23:59:59",
                "debugInfo_2011/11/13 23:59:59");

        _em.flush();

        String ret = _service.adminView(gameId, date, time);
        _log.info(ret);

        BufferedReader reader = new BufferedReader(new StringReader(ret));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        assertEquals("START", reader.readLine());
        assertEquals(
                l2_1.getDebugInfo() + ",time="
                        + df.format(l2_1.getAccessDate()), reader.readLine());
        assertEquals(
                l1_2.getDebugInfo() + ",time="
                        + df.format(l1_2.getAccessDate()), reader.readLine());
        assertEquals("END", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminview.AdminViewServiceBean#adminView(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws ParseException
     * @throws IOException
     */
    @Test
    public final void testAdminViewArgGameIdIsInvalid() throws ParseException,
            IOException {
        String gameId = "SBXX　";
        String date = "20111113";
        String time = "235959";

        deleteLogs(gameId);

        Log l1_1 = createLogs("AZZZZZZZZZZ", "SBXX", "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l1_2 = createLogs("AZZZZZZZZZZ", "SBXX", "2011/11/14 11:00:00",
                "debugInfo_2011/11/14_11:00:00");
        Log l1_3 = createLogs("AZZZZZZZZZZ", "SBYY", "2011/11/14 12:00:00",
                "debugInfo_2011/11/14_12:00:00");
        Log l2_1 = createLogs("AYYYYYYYYYY", "SBXX", "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l2_2 = createLogs("AYYYYYYYYYY", "SBXX", "2011/1/14 11:00:00",
                "debugInfo_2011/1/14_11:00:00");
        Log l2_3 = createLogs("AYYYYYYYYYY", "SBXX", "2011/11/13 23:59:59",
                "debugInfo_2011/11/13 23:59:59");

        _em.flush();

        String ret = _service.adminView(gameId, date, time);
        _log.info(ret);

        BufferedReader reader = new BufferedReader(new StringReader(ret));
        assertEquals("START", reader.readLine());
        assertEquals("END", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminview.AdminViewServiceBean#adminView(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws ParseException
     * @throws IOException
     */
    @Test
    public final void testAdminViewArgDateIsInvalid() throws ParseException,
            IOException {
        String gameId = "SBXX";
        String date = "２0１1１1１3";
        String time = "235959";

        deleteLogs(gameId);

        Log l1_1 = createLogs("AZZZZZZZZZZ", gameId, "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l1_2 = createLogs("AZZZZZZZZZZ", gameId, "2011/11/14 11:00:00",
                "debugInfo_2011/11/14_11:00:00");
        Log l1_3 = createLogs("AZZZZZZZZZZ", "SBYY", "2011/11/14 12:00:00",
                "debugInfo_2011/11/14_12:00:00");
        Log l2_1 = createLogs("AYYYYYYYYYY", gameId, "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l2_2 = createLogs("AYYYYYYYYYY", gameId, "2011/1/14 11:00:00",
                "debugInfo_2011/1/14_11:00:00");
        Log l2_3 = createLogs("AYYYYYYYYYY", gameId, "2011/11/13 23:59:59",
                "debugInfo_2011/11/13 23:59:59");

        _em.flush();

        String ret = _service.adminView(gameId, date, time);
        _log.info(ret);

        BufferedReader reader = new BufferedReader(new StringReader(ret));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        assertEquals("START", reader.readLine());
        assertEquals(
                l2_1.getDebugInfo() + ",time="
                        + df.format(l2_1.getAccessDate()), reader.readLine());
        assertEquals(
                l1_2.getDebugInfo() + ",time="
                        + df.format(l1_2.getAccessDate()), reader.readLine());
        assertEquals("END", reader.readLine());
        assertNull(reader.readLine());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminview.AdminViewServiceBean#adminView(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws ParseException
     * @throws IOException
     */
    @Test
    public final void testAdminViewArgTimeIsInvalid() throws ParseException,
            IOException {
        String gameId = "SBXX";
        String date = "20111113";
        String time = "２３59５9";

        deleteLogs(gameId);

        Log l1_1 = createLogs("AZZZZZZZZZZ", gameId, "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l1_2 = createLogs("AZZZZZZZZZZ", gameId, "2011/11/14 11:00:00",
                "debugInfo_2011/11/14_11:00:00");
        Log l1_3 = createLogs("AZZZZZZZZZZ", "SBYY", "2011/11/14 12:00:00",
                "debugInfo_2011/11/14_12:00:00");
        Log l2_1 = createLogs("AYYYYYYYYYY", gameId, "2011/11/14 10:00:00",
                "debugInfo_2011/11/14_10:00:00");
        Log l2_2 = createLogs("AYYYYYYYYYY", gameId, "2011/1/14 11:00:00",
                "debugInfo_2011/1/14_11:00:00");
        Log l2_3 = createLogs("AYYYYYYYYYY", gameId, "2011/11/13 23:59:59",
                "debugInfo_2011/11/13 23:59:59");

        _em.flush();

        String ret = _service.adminView(gameId, date, time);
        _log.info(ret);

        BufferedReader reader = new BufferedReader(new StringReader(ret));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        assertEquals("START", reader.readLine());
        assertEquals(
                l2_1.getDebugInfo() + ",time="
                        + df.format(l2_1.getAccessDate()), reader.readLine());
        assertEquals(
                l1_2.getDebugInfo() + ",time="
                        + df.format(l1_2.getAccessDate()), reader.readLine());
        assertEquals("END", reader.readLine());
        assertNull(reader.readLine());
    }

    private void deleteLogs(String gameId) {
        _em.createQuery("delete from LOGS l where l.gameId = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
    }

    private Log createLogs(String serial, String gameId, String accessDate,
            String debugInfo) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Log l = new Log();
        l.setSerial(serial);
        l.setAccessDate(df.parse(accessDate));
        l.setStat(new BigDecimal(1));
        l.setCause(new BigDecimal(1));
        l.setGameId(gameId);
        l.setGameVer("1.00");
        l.setPlaceIp("0.0.0.0");
        l.setGlobalIp("255.255.255.255");
        l.setRequest("request");
        l.setResponse("response");
        l.setDebugInfo(debugInfo);
        l.setDebugInfoPlace("debugInfoPlace");
        l.setCreateDate(new Date());
        l.setCreateUserId("TestUser");
        _em.createNativeQuery(
                "DELETE FROM logs WHERE serial = :serial AND access_date = :accessDate")
                .setParameter("serial", l.getSerial())
                .setParameter("accessDate", l.getAccessDate()).executeUpdate();
        _em.createNativeQuery(
                "INSERT INTO logs (access_date, serial, stat, cause, game_id, game_ver, place_ip, global_ip, request, response, debug_info, debug_info_place, create_user_id) "
                        + "VALUES (:accessDate, :serial, :stat, :cause, :gameId, :gameVer, :placeIp, :globalIp, :request, :response, :debugInfo, :debugInfoPlace, :createUserId)")
                .setParameter("accessDate", l.getAccessDate())
                .setParameter("serial", l.getSerial())
                .setParameter("stat", l.getStat())
                .setParameter("cause", l.getCause())
                .setParameter("gameId", l.getGameId())
                .setParameter("gameVer", l.getGameVer())
                .setParameter("placeIp", l.getPlaceIp())
                .setParameter("globalIp", l.getGlobalIp())
                .setParameter("request", l.getRequest())
                .setParameter("response", l.getResponse())
                .setParameter("debugInfo", l.getDebugInfo())
                .setParameter("debugInfoPlace", l.getDebugInfoPlace())
                .setParameter("createUserId", l.getCreateUserId())
                .executeUpdate();
        return l;
    }

}
