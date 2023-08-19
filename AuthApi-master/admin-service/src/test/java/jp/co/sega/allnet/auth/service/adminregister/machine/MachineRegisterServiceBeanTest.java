/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.machine;

import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jp.co.sega.allnet.auth.common.entity.AppDeliverReport;
import jp.co.sega.allnet.auth.common.entity.Country;
import jp.co.sega.allnet.auth.common.entity.LoaderStateLog;
import jp.co.sega.allnet.auth.common.entity.LoaderStateLogPK;
import jp.co.sega.allnet.auth.common.entity.Machine;
import jp.co.sega.allnet.auth.common.entity.MachineDeletionReason;
import jp.co.sega.allnet.auth.common.entity.MachineDownloadOrder;
import jp.co.sega.allnet.auth.common.entity.MachineStatus;
import jp.co.sega.allnet.auth.common.entity.OptDeliverReport;
import jp.co.sega.allnet.auth.common.entity.Place;
import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;
import jp.co.sega.allnet.auth.service.security.AuthenticationDelegate;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVReader;

/**
 * @author NakanoY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class MachineRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(MachineRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "machineRegisterService")
    private MachineRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @After
    public void after() {
        reset(_authMock);
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineNew() throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "AZZZZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99998);
        map2.put("serial", "AYYYYYYYYYY");
        map2.put("gameId", "SBYY");
        map2.put("groupIndex", 2);
        map2.put("setting", 2);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("allnetId", -99997);
        map3.put("serial", "AXXXXXXXXXX");
        map3.put("gameId", "SBZZ");
        map3.put("groupIndex", 3);
        map3.put("setting", 3);

        String countryCode = "TWN";

        String placeId1 = "XXXX";
        String placeId3 = "YYYY";

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));
        val += "\n"
                + String.format(format, map2.get("allnetId"),
                        map2.get("serial"), map2.get("gameId"),
                        map2.get("groupIndex"), map2.get("setting"));
        val += "\n"
                + String.format(format, map3.get("allnetId"),
                        map3.get("serial"), map3.get("gameId"),
                        map3.get("groupIndex"), map3.get("setting") + ",");

        deleteMachines((String) map1.get("serial"));
        deleteMachines((String) map2.get("serial"));
        deleteMachines((String) map3.get("serial"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode);
        deletePlaceWithRelations((Integer) map2.get("allnetId"), countryCode);
        deletePlaceWithRelations((Integer) map3.get("allnetId"), countryCode);

        createCountry(countryCode);
        createPlace((Integer) map1.get("allnetId"), placeId1, countryCode);
        createPlace((Integer) map3.get("allnetId"), placeId3, countryCode);

        _em.flush();

        // MachineRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerMachine(val);

        _log.info(ret);

        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertEquals(map1.get("serial"), line[2]);
			assertEquals("1", line[3]);
			assertEquals(placeId1, line[4]);
			assertEquals("", line[5]);
			assertEquals("1", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("2", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 4行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("3", line[0]);
			assertEquals(String.valueOf((Integer) map3.get("allnetId")), line[1]);
			assertEquals(map3.get("serial"), line[2]);
			assertEquals("1", line[3]);
			assertEquals(placeId3, line[4]);
			assertEquals("", line[5]);
			assertEquals("1", line[6]);
			// 5行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("2", line[6]);

			assertNull(reader.readNext());
		}

        // 基板情報のDB確認
        Machine m = _em.find(Machine.class, map1.get("serial"));
        assertEquals(map1.get("allnetId"), m.getAllnetId().intValue());
        assertEquals(map1.get("gameId"), m.getGameId());
        assertEquals(map1.get("gameId"), m.getReservedGameId());
        assertEquals(map1.get("groupIndex"), m.getGroupIndex().intValue());
        assertEquals(map1.get("setting"), m.getSetting().intValue());
        assertEquals(placeId1, m.getPlaceId());
        assertEquals(mockUserId, m.getCreateUserId());
        assertEquals(mockUserId, m.getUpdateUserId());

        m = _em.find(Machine.class, map2.get("serial"));
        assertNull(m);

        m = _em.find(Machine.class, map3.get("serial"));
        assertEquals(map3.get("allnetId"), m.getAllnetId().intValue());
        assertEquals(map3.get("gameId"), m.getGameId());
        assertEquals(map3.get("gameId"), m.getReservedGameId());
        assertEquals(map3.get("groupIndex"), m.getGroupIndex().intValue());
        assertEquals(map3.get("setting"), m.getSetting().intValue());
        assertEquals(placeId3, m.getPlaceId());
        assertEquals(mockUserId, m.getCreateUserId());
        assertEquals(mockUserId, m.getUpdateUserId());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);

    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineUpdate() throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "AZZZZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99998);
        map2.put("serial", "AYYYYYYYYYY");
        map2.put("gameId", "SBYY");
        map2.put("groupIndex", 2);
        map2.put("setting", 2);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("allnetId", -99997);
        map3.put("serial", "AXXXXXXXXXX");
        map3.put("gameId", "SBZZ");
        map3.put("groupIndex", 3);
        map3.put("setting", 3);

        String countryCode = "TWN";

        String placeId1 = "XXXX";
        String placeId2 = "YYYY";
        String placeId3 = "XXZZ";
        String placeId4 = "YYZZ";

        String existGameId = "AAAA";

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));
        val += "\n"
                + String.format(format, map2.get("allnetId"),
                        map2.get("serial"), map2.get("gameId"),
                        map2.get("groupIndex"), map2.get("setting"));
        val += "\n"
                + String.format(format, map3.get("allnetId"),
                        map3.get("serial"), map3.get("gameId"),
                        map3.get("groupIndex"), map3.get("setting") + ",");

        deleteMachines((String) map1.get("serial"));
        deleteMachines((String) map2.get("serial"));
        deleteMachines((String) map3.get("serial"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode);
        deletePlaceWithRelations((Integer) map2.get("allnetId"), countryCode);
        deletePlaceWithRelations((Integer) map3.get("allnetId"), countryCode);

        createCountry(countryCode);
        createPlace((Integer) map1.get("allnetId"), placeId1, countryCode);
        createPlace((Integer) map3.get("allnetId"), placeId2, countryCode);

        Machine machine1 = createMachine((Integer) map1.get("allnetId"),
                (String) map1.get("serial"), existGameId,
                (String) map1.get("gameId"), (Integer) map1.get("groupIndex"),
                (Integer) map1.get("setting"), placeId3);
        Machine machine3 = createMachine((Integer) map3.get("allnetId"),
                (String) map3.get("serial"), existGameId,
                (String) map3.get("gameId"), (Integer) map3.get("groupIndex"),
                (Integer) map3.get("setting"), placeId4);

        Date createDate1 = machine1.getCreateDate();
        Date createDate3 = machine3.getCreateDate();

        _em.flush();

        // MachineRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerMachine(val);

        _log.info(ret);

        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertEquals(map1.get("serial"), line[2]);
			assertEquals("2", line[3]);
			assertEquals(placeId1, line[4]);
			assertEquals(placeId3, line[5]);
			assertEquals("1", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("2", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 4行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("3", line[0]);
			assertEquals(String.valueOf((Integer) map3.get("allnetId")), line[1]);
			assertEquals(map3.get("serial"), line[2]);
			assertEquals("2", line[3]);
			assertEquals(placeId2, line[4]);
			assertEquals(placeId4, line[5]);
			assertEquals("1", line[6]);
			// 5行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("2", line[6]);

			assertNull(reader.readNext());
		}

        // 基板情報のDB確認
        Machine m = _em.find(Machine.class, map1.get("serial"));
        assertEquals(map1.get("allnetId"), m.getAllnetId().intValue());
        assertEquals(existGameId, m.getGameId());
        assertEquals(map1.get("gameId"), m.getReservedGameId());
        assertEquals(map1.get("groupIndex"), m.getGroupIndex().intValue());
        assertEquals(map1.get("setting"), m.getSetting().intValue());
        assertEquals(placeId1, m.getPlaceId());
        assertEquals("TestUserCraete", m.getCreateUserId());
        assertEquals(createDate1, m.getCreateDate());
        assertEquals(mockUserId, m.getUpdateUserId());

        m = _em.find(Machine.class, map2.get("serial"));
        assertNull(m);

        m = _em.find(Machine.class, map3.get("serial"));
        assertEquals(map3.get("allnetId"), m.getAllnetId().intValue());
        assertEquals(existGameId, m.getGameId());
        assertEquals(map3.get("gameId"), m.getReservedGameId());
        assertEquals(map3.get("groupIndex"), m.getGroupIndex().intValue());
        assertEquals(map3.get("setting"), m.getSetting().intValue());
        assertEquals(placeId2, m.getPlaceId());
        assertEquals("TestUserCraete", m.getCreateUserId());
        assertEquals(createDate3, m.getCreateDate());
        assertEquals(mockUserId, m.getUpdateUserId());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);

    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineUpdateWithLoaderStateAndDeliverReport()
            throws IOException {
        Date now = new Date();

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "AZZZZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        String countryCode = "TWN";

        String placeId1 = "XXXX";

        String existGameId = "AAAA";

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));

        deleteMachines((String) map1.get("serial"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode);

        createCountry(countryCode);
        createPlace((Integer) map1.get("allnetId"), placeId1, countryCode);

        Machine machine1 = createMachine((Integer) map1.get("allnetId"),
                (String) map1.get("serial"), (String) map1.get("gameId"),
                existGameId, (Integer) map1.get("groupIndex"),
                (Integer) map1.get("setting"), placeId1);

        Date createDate1 = machine1.getCreateDate();

        createLoaderState((String) map1.get("serial"), now);
        createAppDeliverReport((String) map1.get("serial"));
        createOptDeliverReport((String) map1.get("serial"));

        _em.flush();

        // MachineRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerMachine(val);

        _em.flush();


		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertEquals(map1.get("serial"), line[2]);
			assertEquals("2", line[3]);
			assertEquals(placeId1, line[4]);
			assertEquals(placeId1, line[5]);
			assertEquals("1", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("1", line[6]);

			assertNull(reader.readNext());
		}

        // 基板情報のDB確認
        Machine m = _em.find(Machine.class, map1.get("serial"));
        assertEquals(map1.get("allnetId"), m.getAllnetId().intValue());
        assertEquals(map1.get("gameId"), m.getGameId());
        assertEquals(map1.get("gameId"), m.getReservedGameId());
        assertEquals(map1.get("groupIndex"), m.getGroupIndex().intValue());
        assertEquals(map1.get("setting"), m.getSetting().intValue());
        assertEquals(placeId1, m.getPlaceId());
        assertEquals("TestUserCraete", m.getCreateUserId());
        assertEquals(createDate1, m.getCreateDate());
        assertEquals(mockUserId, m.getUpdateUserId());

        // 配信PCの稼働状況ログのDB確認
        Query query = _em
                .createNativeQuery("SELECT COUNT(1) FROM loader_state_logs WHERE serial = :serial");
        query.setParameter("serial", (String) map1.get("serial"));
        BigInteger c = (BigInteger) query.getSingleResult();
        assertEquals(1, c.intValue());

        // NU用配信レポート(APP)のDB確認
        query = _em
                .createNativeQuery("SELECT COUNT(1) FROM app_deliver_reports WHERE serial = :serial");
        query.setParameter("serial", (String) map1.get("serial"));
        c = (BigInteger) query.getSingleResult();
        assertEquals(1, c.intValue());

        // NU用配信レポート(OPT)のDB確認
        query = _em
                .createNativeQuery("SELECT COUNT(1) FROM opt_deliver_reports WHERE serial = :serial");
        query.setParameter("serial", (String) map1.get("serial"));
        c = (BigInteger) query.getSingleResult();
        assertEquals(1, c.intValue());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);

    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineUpdateWithLoaderStateAndDeliverReportForAdmin()
            throws IOException {
        Date now = new Date();

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "AZZZZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        String countryCode = "TWN";

        String placeId1 = "XXXX";

        String existGameId = "AAAA";

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));

        deleteMachines((String) map1.get("serial"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode);

        createCountry(countryCode);
        createPlace((Integer) map1.get("allnetId"), placeId1, countryCode);

        Machine machine1 = createMachine((Integer) map1.get("allnetId"),
                (String) map1.get("serial"), (String) map1.get("gameId"),
                existGameId, (Integer) map1.get("groupIndex"),
                (Integer) map1.get("setting"), placeId1);

        Date createDate1 = machine1.getCreateDate();

        createLoaderState((String) map1.get("serial"), now);
        createAppDeliverReport((String) map1.get("serial"));
        createOptDeliverReport((String) map1.get("serial"));

        _em.flush();

        // MachineRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        RegisterServiceResult<MachineRegisterResult> ret = _service
                .registerMachineForAdmin(val, (String) map1.get("gameId"),
                        false);

        _em.flush();

        MachineRegisterResult mr = ret.getList().get(0);

        assertEquals((String) map1.get("serial"), mr.getMachine().getSerial());
        assertEquals(new BigDecimal((Integer) map1.get("allnetId")), mr
                .getMachine().getAllnetId());
        assertEquals((String) map1.get("gameId"), mr.getMachine().getGameId());
        assertEquals(new BigDecimal((Integer) map1.get("groupIndex")), mr
                .getMachine().getGroupIndex());
        assertEquals(new BigDecimal((Integer) map1.get("setting")), mr
                .getMachine().getSetting());

        // 基板情報のDB確認
        Machine m = _em.find(Machine.class, map1.get("serial"));
        assertEquals(map1.get("allnetId"), m.getAllnetId().intValue());
        assertEquals(map1.get("gameId"), m.getGameId());
        assertEquals(existGameId, m.getReservedGameId());
        assertEquals(map1.get("groupIndex"), m.getGroupIndex().intValue());
        assertEquals(map1.get("setting"), m.getSetting().intValue());
        assertEquals(placeId1, m.getPlaceId());
        assertEquals("TestUserCraete", m.getCreateUserId());
        assertEquals(createDate1, m.getCreateDate());
        assertEquals(mockUserId, m.getUpdateUserId());

        // 配信PCの稼働状況ログのDB確認
        Query query = _em
                .createNativeQuery("SELECT COUNT(1) FROM loader_state_logs WHERE serial = :serial");
        query.setParameter("serial", (String) map1.get("serial"));
        BigInteger c = (BigInteger) query.getSingleResult();
        assertEquals(0, c.intValue());

        // NU用配信レポート(APP)のDB確認
        query = _em
                .createNativeQuery("SELECT COUNT(1) FROM app_deliver_reports WHERE serial = :serial");
        query.setParameter("serial", (String) map1.get("serial"));
        c = (BigInteger) query.getSingleResult();
        assertEquals(0, c.intValue());

        // NU用配信レポート(OPT)のDB確認
        query = _em
                .createNativeQuery("SELECT COUNT(1) FROM opt_deliver_reports WHERE serial = :serial");
        query.setParameter("serial", (String) map1.get("serial"));
        c = (BigInteger) query.getSingleResult();
        assertEquals(0, c.intValue());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);

    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineDelete() throws IOException {

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "AZZZZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);
        map1.put("deletionReasonNo", 9999);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99998);
        map2.put("serial", "AYYYYYYYYYY");
        map2.put("gameId", "SBYY");
        map2.put("groupIndex", 2);
        map2.put("setting", 2);
        map2.put("deletionReasonNo", 1);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("allnetId", -99997);
        map3.put("serial", "AXXXXXXXXXX");
        map3.put("gameId", "SBZZ");
        map3.put("groupIndex", 3);
        map3.put("setting", 3);
        map3.put("deletionReasonNo", 9998);

        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("allnetId", -99996);
        map4.put("serial", "AWWWWWWWWWW");
        map4.put("gameId", "SBZZ");
        map4.put("groupIndex", 4);
        map4.put("setting", 4);
        map4.put("deletionReasonNo", 1);

        String countryCode = "TWN";

        String placeId1 = "XXXX";
        String placeId3 = "YYYY";
        String placeId4 = "ZZZZ";

        String reservedGameId = "AAAA";

        String format = ",%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"), map1.get("deletionReasonNo"));
        val += "\n"
                + String.format(format, map2.get("allnetId"),
                        map2.get("serial"), map2.get("gameId"),
                        map2.get("groupIndex"), map2.get("setting"),
                        map2.get("deletionReasonNo"));
        val += "\n"
                + String.format(format, map3.get("allnetId"),
                        map3.get("serial"), map3.get("gameId"),
                        map3.get("groupIndex"), map3.get("setting"),
                        map3.get("deletionReasonNo"));
        val += "\n"
                + String.format(format, map4.get("allnetId"),
                        map4.get("serial"), map4.get("gameId"),
                        map4.get("groupIndex"), map4.get("setting"),
                        map4.get("deletionReasonNo"));

        deleteMachines((String) map1.get("serial"));
        deleteMachines((String) map2.get("serial"));
        deleteMachines((String) map3.get("serial"));
        deleteMachines((String) map4.get("serial"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode);
        deletePlaceWithRelations((Integer) map2.get("allnetId"), countryCode);
        deletePlaceWithRelations((Integer) map3.get("allnetId"), countryCode);
        deletePlaceWithRelations((Integer) map4.get("allnetId"), countryCode);

        deleteMachineDeletion((Integer) map1.get("deletionReasonNo"));
        deleteMachineDeletion((Integer) map3.get("deletionReasonNo"));

        createCountry(countryCode);
        createPlace((Integer) map1.get("allnetId"), placeId1, countryCode);
        createPlace((Integer) map3.get("allnetId"), placeId3, countryCode);
        createPlace((Integer) map4.get("allnetId"), placeId4, countryCode);

        createMachine((Integer) map1.get("allnetId"),
                (String) map1.get("serial"), (String) map1.get("gameId"),
                reservedGameId, (Integer) map1.get("groupIndex"),
                (Integer) map1.get("setting"), placeId1);
        createMachine((Integer) map3.get("allnetId"),
                (String) map3.get("serial"), (String) map3.get("gameId"),
                reservedGameId, (Integer) map3.get("groupIndex"),
                (Integer) map3.get("setting"), placeId3);

        createMachineDeletionReason((Integer) map1.get("deletionReasonNo"));

        _em.flush();

        // MachineRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId)
                .times(2);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerMachine(val);

        _log.info(ret);

        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertEquals(map1.get("serial"), line[2]);
			assertEquals("2", line[3]);
			assertEquals(placeId1, line[4]);
			assertEquals(placeId1, line[5]);
			assertEquals("1", line[6]);
			// 3行目(削除対象がない)
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("2", line[0]);
			assertEquals(String.valueOf((Integer) map2.get("allnetId")), line[1]);
			assertEquals(map2.get("serial"), line[2]);
			assertEquals("2", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("1", line[6]);
			// 4行目(削除理由がないが削除成功)
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("3", line[0]);
			assertEquals(String.valueOf((Integer) map3.get("allnetId")), line[1]);
			assertEquals(map3.get("serial"), line[2]);
			assertEquals("2", line[3]);
			assertEquals(placeId3, line[4]);
			assertEquals(placeId3, line[5]);
			assertEquals("1", line[6]);
			// 5行目(店舗情報はあるが削除対象基板がない)
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("4", line[0]);
			assertEquals(String.valueOf((Integer) map4.get("allnetId")), line[1]);
			assertEquals(map4.get("serial"), line[2]);
			assertEquals("2", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("1", line[6]);
			// 5行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("4", line[6]);

			assertNull(reader.readNext());
		}

        // 基板情報のDB確認
        Machine m = _em.find(Machine.class, map1.get("serial"));
        assertNull(m);
        MachineDownloadOrder mdo = _em.find(MachineDownloadOrder.class,
                map1.get("serial"));
        assertTrue(mdo != null);

        m = _em.find(Machine.class, map2.get("serial"));
        assertNull(m);

        m = _em.find(Machine.class, map3.get("serial"));
        assertNull(m);
        mdo = _em.find(MachineDownloadOrder.class, map3.get("serial"));
        assertTrue(mdo != null);

        m = _em.find(Machine.class, map4.get("serial"));
        assertNull(m);

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);

    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineDeleteWithLoaderStateAndDeliverReport()
            throws IOException {
        Date now = new Date();

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "AZZZZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);
        map1.put("deletionReasonNo", 9999);

        String countryCode = "TWN";

        String placeId1 = "XXXX";

        String reservedGameId = "AAAA";

        String format = ",%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"), map1.get("deletionReasonNo"));

        deleteMachines((String) map1.get("serial"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode);

        deleteMachineDeletion((Integer) map1.get("deletionReasonNo"));

        createCountry(countryCode);
        createPlace((Integer) map1.get("allnetId"), placeId1, countryCode);

        createMachine((Integer) map1.get("allnetId"),
                (String) map1.get("serial"), (String) map1.get("gameId"),
                reservedGameId, (Integer) map1.get("groupIndex"),
                (Integer) map1.get("setting"), placeId1);

        createMachineDeletionReason((Integer) map1.get("deletionReasonNo"));

        createLoaderState((String) map1.get("serial"), now);
        createAppDeliverReport((String) map1.get("serial"));
        createOptDeliverReport((String) map1.get("serial"));

        _em.flush();

        // MachineRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerMachine(val);

        _log.info(ret);

        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertEquals(map1.get("serial"), line[2]);
			assertEquals("2", line[3]);
			assertEquals(placeId1, line[4]);
			assertEquals(placeId1, line[5]);
			assertEquals("1", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("1", line[6]);

			assertNull(reader.readNext());
		}

        // 基板情報のDB確認
        Machine m = _em.find(Machine.class, map1.get("serial"));
        assertNull(m);
        MachineDownloadOrder mdo = _em.find(MachineDownloadOrder.class,
                map1.get("serial"));
        assertTrue(mdo != null);

        // 配信PCの稼働状況ログのDB確認
        Query query = _em
                .createNativeQuery("SELECT COUNT(1) FROM loader_state_logs WHERE serial = :serial");
        query.setParameter("serial", (String) map1.get("serial"));
        BigInteger c = (BigInteger) query.getSingleResult();
        assertEquals(0, c.intValue());

        // NU用配信レポート(APP)のDB確認
        query = _em
                .createNativeQuery("SELECT COUNT(1) FROM app_deliver_reports WHERE serial = :serial");
        query.setParameter("serial", (String) map1.get("serial"));
        c = (BigInteger) query.getSingleResult();
        assertEquals(0, c.intValue());

        // NU用配信レポート(OPT)のDB確認
        query = _em
                .createNativeQuery("SELECT COUNT(1) FROM opt_deliver_reports WHERE serial = :serial");
        query.setParameter("serial", (String) map1.get("serial"));
        c = (BigInteger) query.getSingleResult();
        assertEquals(0, c.intValue());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);

    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineDeleteWithLoaderStateAndDeliverReportForAdmin()
            throws IOException {
        Date now = new Date();
    
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "AZZZZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);
        map1.put("deletionReasonNo", 9999);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99998);
        map2.put("serial", "AYYYYYYYYYY");
        map2.put("gameId", "SBXX");
        map2.put("groupIndex", 2);
        map2.put("setting", 2);
        map2.put("deletionReasonNo", 9998);
    
        String countryCode = "TWN";
    
        String placeId1 = "XXXX";
    
        String reservedGameId = "AAAA";
    
        String format = ",%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"), map1.get("deletionReasonNo"));
        val += "\n";
        val += String.format(format, map2.get("allnetId"), map2.get("serial"),
                map2.get("gameId"), map2.get("groupIndex"),
                map2.get("setting"), map2.get("deletionReasonNo"));
    
        deleteMachines((String) map1.get("serial"));
        deleteMachines((String) map2.get("serial"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode);
    
        deleteMachineDeletion((Integer) map1.get("deletionReasonNo"));
    
        createCountry(countryCode);
        createPlace((Integer) map1.get("allnetId"), placeId1, countryCode);
    
        createMachine((Integer) map1.get("allnetId"),
                (String) map1.get("serial"), (String) map1.get("gameId"),
                reservedGameId, (Integer) map1.get("groupIndex"),
                (Integer) map1.get("setting"), placeId1);
    
        createMachineDeletionReason((Integer) map1.get("deletionReasonNo"));
    
        createLoaderState((String) map1.get("serial"), now);
        createAppDeliverReport((String) map1.get("serial"));
        createOptDeliverReport((String) map1.get("serial"));
    
        _em.flush();
    
        // MachineRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);
    
        RegisterServiceResult<MachineRegisterResult> ret = _service
                .registerMachineForAdmin(val, (String) map1.get("gameId"),
                        false);

        _em.flush();

        MachineRegisterResult mr = ret.getList().get(0);

        assertEquals((String) map1.get("serial"), mr.getMachine().getSerial());
        assertEquals(new BigDecimal((Integer) map1.get("allnetId")), mr
                .getMachine().getAllnetId());
        assertEquals((String) map1.get("gameId"), mr.getMachine().getGameId());
        assertEquals(new BigDecimal((Integer) map1.get("groupIndex")), mr
                .getMachine().getGroupIndex());
        assertEquals(new BigDecimal((Integer) map1.get("setting")), mr
                .getMachine().getSetting());

        // 2行目(削除失敗)
        mr = ret.getList().get(1);
        assertEquals(-3, mr.getSuccess());

    
        // 基板情報のDB確認
        Machine m = _em.find(Machine.class, map1.get("serial"));
        assertNull(m);
        MachineDownloadOrder mdo = _em.find(MachineDownloadOrder.class,
                map1.get("serial"));
        assertTrue(mdo != null);
    
        // 配信PCの稼働状況ログのDB確認
        Query query = _em
                .createNativeQuery("SELECT COUNT(1) FROM loader_state_logs WHERE serial = :serial");
        query.setParameter("serial", (String) map1.get("serial"));
        BigInteger c = (BigInteger) query.getSingleResult();
        assertEquals(0, c.intValue());
    
        // NU用配信レポート(APP)のDB確認
        query = _em
                .createNativeQuery("SELECT COUNT(1) FROM app_deliver_reports WHERE serial = :serial");
        query.setParameter("serial", (String) map1.get("serial"));
        c = (BigInteger) query.getSingleResult();
        assertEquals(0, c.intValue());
    
        // NU用配信レポート(OPT)のDB確認
        query = _em
                .createNativeQuery("SELECT COUNT(1) FROM opt_deliver_reports WHERE serial = :serial");
        query.setParameter("serial", (String) map1.get("serial"));
        c = (BigInteger) query.getSingleResult();
        assertEquals(0, c.intValue());
    
        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineDeleteAndInsert() throws IOException {
        String serial = "AZZZZZZZZZZ";
        String gameId = "SBXX";
    
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", serial);
        map1.put("gameId", gameId);
        map1.put("groupIndex", 1);
        map1.put("setting", 1);
        map1.put("deletionReasonNo", 9999);
    
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99998);
        map2.put("serial", serial);
        map2.put("gameId", gameId);
        map2.put("groupIndex", 2);
        map2.put("setting", 2);
        map2.put("deletionReasonNo", "");
    
        String countryCode = "TWN";
    
        String placeId1 = "XXXX";
        String placeId2 = "YYYY";
    
        String reservedGameId = "AAAA";
    
        String format = ",%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"), map1.get("deletionReasonNo"));
        val += "\n"
                + String.format(format, map2.get("allnetId"),
                        map2.get("serial"), map2.get("gameId"),
                        map2.get("groupIndex"), map2.get("setting"),
                        map2.get("deletionReasonNo"));
    
        deleteMachines(serial);
    
        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode);
        deletePlaceWithRelations((Integer) map2.get("allnetId"), countryCode);
    
        deleteMachineDeletion((Integer) map1.get("deletionReasonNo"));
    
        createCountry(countryCode);
        createPlace((Integer) map1.get("allnetId"), placeId1, countryCode);
        createPlace((Integer) map2.get("allnetId"), placeId2, countryCode);
    
        Machine m1 = createMachine((Integer) map1.get("allnetId"),
                (String) map1.get("serial"), (String) map1.get("gameId"),
                reservedGameId, (Integer) map1.get("groupIndex"),
                (Integer) map1.get("setting"), placeId1);
    
        createMachineDeletionReason((Integer) map1.get("deletionReasonNo"));
    
        _em.flush();
    
        // MachineRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);
    
        String ret = _service.registerMachine(val);
    
        _log.info(ret);
    
        _em.flush();
    
		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertEquals(map1.get("serial"), line[2]);
			assertEquals("2", line[3]);
			assertEquals(placeId1, line[4]);
			assertEquals(placeId1, line[5]);
			assertEquals("1", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("2", line[0]);
			assertEquals(String.valueOf((Integer) map2.get("allnetId")), line[1]);
			assertEquals(map2.get("serial"), line[2]);
			assertEquals("1", line[3]);
			assertEquals(placeId2, line[4]);
			assertEquals("", line[5]);
			assertEquals("1", line[6]);
			// 5行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("2", line[6]);

			assertNull(reader.readNext());
		}
    
        // 基板情報のDB確認
        Machine m = _em.find(Machine.class, map2.get("serial"));
        assertEquals(map2.get("allnetId"), m.getAllnetId().intValue());
        assertEquals(map2.get("gameId"), m.getGameId());
        assertEquals(map2.get("gameId"), m.getReservedGameId());
        assertEquals(map2.get("groupIndex"), m.getGroupIndex().intValue());
        assertEquals(map2.get("setting"), m.getSetting().intValue());
        assertEquals(placeId2, m.getPlaceId());
        assertEquals(mockUserId, m.getCreateUserId());
        assertNotSame(m1.getCreateDate(), m.getCreateDate());
        assertEquals(mockUserId, m.getUpdateUserId());
        assertNotSame(m1.getCreateDate(), m.getUpdateDate());
    
        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersAllnetIdIsEmpty()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", "");
        map1.put("serial", "AZZZZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersAllnetIdIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", 12345678901L);
        map1.put("serial", "AZZZZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersAllnetIdIsNotNum()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", "error");
        map1.put("serial", "AZZZZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersSerialIsEmpty()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersSerialIsInvalidLength()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "A1234AGZ8901");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99999);
        map2.put("serial", "C1AGZ5678901");
        map2.put("gameId", "SBXX");
        map2.put("groupIndex", 1);
        map2.put("setting", 1);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("allnetId", -99999);
        map3.put("serial", "A123456AGZ");
        map3.put("gameId", "SBXX");
        map3.put("groupIndex", 1);
        map3.put("setting", 1);

        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("allnetId", -99999);
        map4.put("serial", "C12345AGZ9");
        map4.put("gameId", "SBXX");
        map4.put("groupIndex", 1);
        map4.put("setting", 1);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));
        val += "\n"
                + String.format(format, map2.get("allnetId"),
                        map2.get("serial"), map2.get("gameId"),
                        map2.get("groupIndex"), map2.get("setting"));
        val += "\n"
                + String.format(format, map3.get("allnetId"),
                        map3.get("serial"), map3.get("gameId"),
                        map3.get("groupIndex"), map3.get("setting"));
        val += "\n"
                + String.format(format, map4.get("allnetId"),
                        map4.get("serial"), map4.get("gameId"),
                        map4.get("groupIndex"), map4.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("2", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 4行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("3", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 5行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("4", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 6行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersSerialIsInvalidLetter()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "B1234567890");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99999);
        map2.put("serial", "Ａ1234567890");
        map2.put("gameId", "SBXX");
        map2.put("groupIndex", 1);
        map2.put("setting", 1);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("allnetId", -99999);
        map3.put("serial", "Aeらー");
        map3.put("gameId", "SBXX");
        map3.put("groupIndex", 1);
        map3.put("setting", 1);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));
        val += "\n"
                + String.format(format, map2.get("allnetId"),
                        map2.get("serial"), map2.get("gameId"),
                        map2.get("groupIndex"), map2.get("setting"));
        val += "\n"
                + String.format(format, map3.get("allnetId"),
                        map3.get("serial"), map3.get("gameId"),
                        map3.get("groupIndex"), map3.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("2", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 4行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("3", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 5行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersGameIdIsEmpty()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "A123ZZZZZZZ");
        map1.put("gameId", "");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersGameIdIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "AZZZZ123ZZZ");
        map1.put("gameId", "123456");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersGameIdIsInvalidLetter()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "AZZZZZZZ123");
        map1.put("gameId", "ゲームID");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99999);
        map2.put("serial", "AZZZZZZZ123");
        map2.put("gameId", "+*ID");
        map2.put("groupIndex", 1);
        map2.put("setting", 1);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));
        val += "\n"
                + String.format(format, map1.get("allnetId"),
                        map1.get("serial"), map1.get("gameId"),
                        map1.get("groupIndex"), map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("2", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 4行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersGroupIndexIsEmpty()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "A123ZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", "");
        map1.put("setting", 1);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersGroupIndexIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "A123ZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 12345678901L);
        map1.put("setting", 1);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersGroupIndexIsInvalidLetter()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "A123ZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", "error");
        map1.put("setting", 1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99999);
        map2.put("serial", "A123ZZZZZZZ");
        map2.put("gameId", "SBXX");
        map2.put("groupIndex", "エラー");
        map2.put("setting", 1);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));
        val += "\n"
                + String.format(format, map1.get("allnetId"),
                        map1.get("serial"), map1.get("gameId"),
                        map1.get("groupIndex"), map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("2", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 4行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersSettingIsEmpty()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "A123ZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", "");

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersSettingIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "A123ZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 12345678901L);

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersSettingIsInvalidLetter()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "A123ZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", "error");

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99999);
        map2.put("serial", "A123ZZZZZZZ");
        map2.put("gameId", "SBXX");
        map2.put("groupIndex", 1);
        map2.put("setting", "エラー");

        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));
        val += "\n"
                + String.format(format, map1.get("allnetId"),
                        map1.get("serial"), map1.get("gameId"),
                        map1.get("groupIndex"), map1.get("setting"));

        String ret = _service.registerMachine(val);

        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("2", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);
			// 4行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("", line[5]);
			assertEquals("0", line[6]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidParametersAllnetIdIsEmptyForAdmin()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", "");
        map1.put("serial", "AZZZZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);
    
        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));
    
        RegisterServiceResult<MachineRegisterResult> ret = _service
                .registerMachineForAdmin(val, (String) map1.get("gameId"),
                        false);

        MachineRegisterResult mr = ret.getList().get(0);

        assertFalse(mr.isSuccess());
    
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterServiceBean#registerMachine(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMachineInvalidSelectGameIdForAdmin()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("serial", "AZZZZZZZZZZ");
        map1.put("gameId", "SBXX");
        map1.put("groupIndex", 1);
        map1.put("setting", 1);
    
        String countryCode = "TWN";
    
        String placeId1 = "XXXX";
    
        String existGameId = "AAAA";
    
        String format = ",%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("serial"), map1.get("gameId"), map1.get("groupIndex"),
                map1.get("setting"));
    
        deleteMachines((String) map1.get("serial"));
    
        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode);
    
        createCountry(countryCode);
        createPlace((Integer) map1.get("allnetId"), placeId1, countryCode);

        _em.flush();
    
        RegisterServiceResult<MachineRegisterResult> ret = _service
                .registerMachineForAdmin(val, existGameId,
                        false);
    
        _em.flush();
    
        MachineRegisterResult mr = ret.getList().get(0);

        assertEquals(-1, mr.getSuccess());
    
    
        // 基板情報のDB確認
        Machine m = _em.find(Machine.class, map1.get("serial"));
        assertNull(m);
    
    }

    private void deleteMachines(String serial) {
        _em.createNativeQuery(
                "DELETE FROM machine_statuses WHERE serial = :serial")
                .setParameter("serial", serial).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM machine_download_orders WHERE serial = :serial")
                .setParameter("serial", serial).executeUpdate();
        _em.createNativeQuery("DELETE FROM machines WHERE serial = :serial")
                .setParameter("serial", serial).executeUpdate();
    }

    private void deletePlaceWithRelations(int allnetId, String countryCode) {
        deletePlace(allnetId);
        deleteRegions(countryCode);

        _em.createNativeQuery("DELETE FROM country_download_orders")
                .executeUpdate();
        _em.createNativeQuery("DELETE FROM game_attributes").executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM countries WHERE country_code = :countryCode")
                .setParameter("countryCode", countryCode).executeUpdate();
    }

    private void deleteRegions(String countryCode) {
        _em.createNativeQuery(
                "DELETE FROM places WHERE country_code = :countryCode")
                .setParameter("countryCode", countryCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM region3 WHERE country_code = :countryCode")
                .setParameter("countryCode", countryCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM region2 WHERE country_code = :countryCode")
                .setParameter("countryCode", countryCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM region1 WHERE country_code = :countryCode")
                .setParameter("countryCode", countryCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM region0 WHERE country_code = :countryCode")
                .setParameter("countryCode", countryCode).executeUpdate();

    }

    private void deletePlace(int allnetId) {
        _em.createNativeQuery("DELETE FROM routers").executeUpdate();
        _em.createNativeQuery("DELETE FROM auth_allowed_places")
                .executeUpdate();
        _em.createNativeQuery("DELETE FROM places WHERE allnet_id = :allnetId")
                .setParameter("allnetId", allnetId).executeUpdate();
    }

    private void deleteMachineDeletion(int reasonId) {
        _em.createNativeQuery(
                "DELETE FROM machine_deletion_histories WHERE reason_id = :reasonId")
                .setParameter("reasonId", reasonId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM machine_deletion_reasons WHERE reason_id = :reasonId")
                .setParameter("reasonId", reasonId).executeUpdate();
    }

    private Place createPlace(int allnetId, String placeId, String countryCode) {
        Place place = new Place();
        place.setAllnetId(allnetId);
        place.setPlaceId(placeId);
        place.setName("店舗名");
        place.setCountryCode(countryCode);
        place.setCreateDate(new Date());
        place.setCreateUserId("TestUserCraete");
        place.setUpdateDate(new Date());
        place.setUpdateUserId("TestUserCreate");
        _em.persist(place);
        return place;
    }

    private Country createCountry(String countryCode) {
        Country c = new Country();
        c.setCountryCode(countryCode);
        c.setCountryName(countryCode);
        c.setCreateDate(new Date());
        c.setCreateUserId("TestUser");
        c.setUpdateDate(new Date());
        c.setUpdateUserId("TestUser");
        return _em.merge(c);
    }

    private Machine createMachine(int allnetId, String serial, String gameId,
            String reservedGameId, int groupIndex, int setting, String placeId) {
        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(new BigDecimal(allnetId));
        machine.setGameId(gameId);
        machine.setReservedGameId(reservedGameId);
        machine.setGroupIndex(new BigDecimal(groupIndex));
        machine.setSetting(new BigDecimal(setting));
        machine.setPlaceId(placeId);
        machine.setCreateDate(new Date());
        machine.setCreateUserId("TestUserCraete");
        machine.setUpdateDate(new Date());
        machine.setUpdateUserId("TestUserCreate");
        _em.persist(machine);

        MachineStatus ms = new MachineStatus();
        ms.setSerial(serial);
        ms.setLastAccess(new Date());
        ms.setHops(new BigDecimal(1));
        ms.setCreateDate(new Date());
        ms.setCreateUserId("TestUserCreate");
        ms.setUpdateDate(new Date());
        ms.setUpdateUserId("TestUserCreate");
        _em.persist(ms);

        MachineDownloadOrder md = new MachineDownloadOrder();
        md.setSerial(serial);
        md.setGameId(gameId);
        md.setCreateDate(new Date());
        md.setCreateUserId("TestUserCreate");
        md.setUpdateDate(new Date());
        md.setUpdateUserId("TestUserCreate");
        _em.persist(md);

        _em.flush();
        _em.refresh(machine);

        return machine;
    }

    private MachineDeletionReason createMachineDeletionReason(int reasonId) {
        MachineDeletionReason r = new MachineDeletionReason();
        r.setReasonId(reasonId);
        r.setCreateDate(new Date());
        r.setCreateUserId("TestUserCraete");
        r.setUpdateDate(new Date());
        r.setUpdateUserId("TestUserCreate");
        _em.persist(r);
        return r;
    }

    private LoaderStateLog createLoaderState(String serial, Date logDate) {
        LoaderStateLog l = _em.find(LoaderStateLog.class, new LoaderStateLogPK(
                serial, logDate));
        if (l != null) {
            return l;
        }
        l = new LoaderStateLog();
        l.setPk(new LoaderStateLogPK(serial, logDate));
        l.setDvd(new BigDecimal(0));
        l.setNet(new BigDecimal(0));
        l.setWork(new BigDecimal(0));
        l.setOldNet(new BigDecimal(0));
        l.setDeliver(new BigDecimal(0));
        l.setFilesToDownload(new BigDecimal(0));
        l.setFilesDownloaded(new BigDecimal(0));
        l.setLastAuth(new Date());
        l.setLastAuthState(new BigDecimal(0));
        l.setDownloadState(new BigDecimal(0));
        l.setReceiptDate(new Date());
        l.setCreateDate(new Date());
        l.setCreateUserId("TestUserCraete");
        l.setUpdateDate(new Date());
        l.setUpdateUserId("TestUserCreate");
        _em.persist(l);
        return l;
    }

    private AppDeliverReport createAppDeliverReport(String serial) {
        AppDeliverReport a = _em.find(AppDeliverReport.class, serial);
        if (a != null) {
            return a;
        }
        a = new AppDeliverReport();
        a.setSerial(serial);
        a.setDownloadState(1);
        a.setApVerWorking("0.00");
        a.setCreateDate(new Date());
        a.setCreateUserId("TestUserCraete");
        a.setUpdateDate(new Date());
        a.setUpdateUserId("TestUserCreate");
        _em.persist(a);
        return a;
    }

    private OptDeliverReport createOptDeliverReport(String serial) {
        OptDeliverReport o = _em.find(OptDeliverReport.class, serial);
        if (o != null) {
            return o;
        }
        o = new OptDeliverReport();
        o.setSerial(serial);
        o.setDownloadState(1);
        o.setCreateDate(new Date());
        o.setCreateUserId("TestUserCraete");
        o.setUpdateDate(new Date());
        o.setUpdateUserId("TestUserCreate");
        _em.persist(o);
        return o;
    }
}
