/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.router;

import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.Country;
import jp.co.sega.allnet.auth.common.entity.LcType;
import jp.co.sega.allnet.auth.common.entity.Place;
import jp.co.sega.allnet.auth.common.entity.Router;
import jp.co.sega.allnet.auth.common.entity.RouterType;
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
public class RouterRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(RouterRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "routerRegisterService")
    private RouterRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @After
    public void after() {
        reset(_authMock);
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterNew() throws IOException {

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", null);
        map2.put("routerId", "Z9998");
        map2.put("placeIp", "255.255.255.255");
        map2.put("routerTypeId", 99998);
        map2.put("lcTypeId", 89998);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("allnetId", -99997);
        map3.put("routerId", "Z9997");
        map3.put("placeIp", "254.254.254.254");
        map3.put("routerTypeId", 99997);
        map3.put("lcTypeId", null);

        // FIXME 種別チェックを外しているため
        // Map<String, Object> map4 = new HashMap<String, Object>();
        // map4.put("allnetId", -99999);
        // map4.put("routerId", "Z9996");
        // map4.put("placeIp", "255.0.0.0");
        // map4.put("routerTypeId", 99996);
        // map4.put("lcTypeId", 89996);

        Map<String, Object> map5 = new HashMap<String, Object>();
        map5.put("allnetId", -99999);
        map5.put("routerId", "Z9995");
        map5.put("placeIp", "0.0.0.0");
        map5.put("routerTypeId", 99995);
        map5.put("lcTypeId", 89995);

        String placeId1 = "XXXX";
        String placeId2 = "YYYY";

        String countryCode1 = "TWN";
        String countryCode2 = "SGP";

        String format1 = ",%s,%s,%s,%s,%s";
        String format2 = ",%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));
        val += "\n"
                + String.format(format1, "", map2.get("routerId"),
                        map2.get("placeIp"), map2.get("routerTypeId"),
                        map2.get("lcTypeId"));
        val += "\n"
                + String.format(format2, map3.get("allnetId"),
                        map3.get("routerId"), map3.get("placeIp"),
                        map3.get("routerTypeId"));
        // FIXME 種別チェックを外しているため
        // val += "\n"
        // + String.format(format1, map4.get("allnetId"),
        // map4.get("routerId"), map4.get("placeIp"),
        // map4.get("routerTypeId"), map4.get("lcTypeId"));
        val += "\n"
                + String.format(format1, map5.get("allnetId"),
                        map5.get("routerId"), map5.get("placeIp"),
                        map5.get("routerTypeId"), map5.get("lcTypeId"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode1);
        deletePlaceWithRelations((Integer) map3.get("allnetId"), countryCode2);

        createCountry(countryCode1);
        createCountry(countryCode2);
        createPlace((Integer) map1.get("allnetId"), placeId1, countryCode1);
        createPlace((Integer) map3.get("allnetId"), placeId2, countryCode2);

        // FIXME 種別チェックを外しているため
        // createRouterType((Integer) map1.get("routerTypeId"));
        // createLcType((Integer) map1.get("lcTypeId"));
        // createRouterType((Integer) map3.get("routerTypeId"));
        // createRouterType((Integer) map5.get("routerTypeId"));
        // createLcType((Integer) map5.get("lcTypeId"));

        _em.flush();

        // RouterRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[2]);
			assertEquals(placeId1, line[3]);
			assertEquals("", line[4]);
			assertEquals("1", line[5]);
			assertEquals("1", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("routerId"), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
			assertEquals("0", line[6]);
			// 4行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("routerId"), line[1]);
			assertEquals(String.valueOf((Integer) map3.get("allnetId")), line[2]);
			assertEquals(placeId2, line[3]);
			assertEquals("", line[4]);
			assertEquals("1", line[5]);
			assertEquals("1", line[6]);
			// 5行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("4", line[0]);
			// FIXME 種別チェックを外しているため
			// assertEquals(map4.get("routerId"), line[1]);
			// assertEquals(String.valueOf((Integer) map4.get("allnetId")),
			// line[2]);
			// assertEquals("", line[3]);
			// assertEquals("", line[4]);
			// assertEquals("2", line[5]);
			// assertEquals("0", line[6]);
			// // 6行目
			// line = reader.readNext();
			// assertEquals(7, line.length);
			// assertEquals("5", line[0]);
			assertEquals(map5.get("routerId"), line[1]);
			assertEquals(String.valueOf((Integer) map5.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("1", line[5]);
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
			assertEquals("2", line[6]);

			assertNull(reader.readNext());
		}

        // ルータ情報のDB確認
        Router r = _em.find(Router.class, map1.get("routerId"));
        assertEquals(map1.get("allnetId"), r.getAllnetId().intValue());
        assertEquals(map1.get("routerId"), r.getRouterId());
        assertEquals(map1.get("placeIp"), r.getPlaceIp());
        assertEquals(map1.get("routerTypeId"), r.getRouterTypeId().intValue());
        assertEquals(map1.get("lcTypeId"), r.getLcTypeId().intValue());
        assertEquals(mockUserId, r.getCreateUserId());
        assertEquals(mockUserId, r.getUpdateUserId());

        // ルータ情報のDB確認
        r = _em.find(Router.class, map2.get("routerId"));
        assertNull(r);

        // ルータ情報のDB確認
        r = _em.find(Router.class, map3.get("routerId"));
        assertEquals(map3.get("allnetId"), r.getAllnetId().intValue());
        assertEquals(map3.get("routerId"), r.getRouterId());
        assertEquals(map3.get("placeIp"), r.getPlaceIp());
        assertEquals(map3.get("routerTypeId"), r.getRouterTypeId().intValue());
        assertNull(r.getLcTypeId());
        assertEquals(mockUserId, r.getCreateUserId());
        assertEquals(mockUserId, r.getUpdateUserId());

        // ルータ情報のDB確認
        // FIXME 種別チェックを外しているため
        // r = _em.find(Router.class, map4.get("routerId"));
        // assertNull(r);

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);

    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterUpdate() throws IOException {

        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId3 = -99997;
        int allnetId4 = -99996;

        String placeId1 = "XXXX";
        String placeId2 = "YYYY";
        String placeId3 = "ZZZZ";
        String placeId4 = "0000";

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", allnetId1);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", null);
        map2.put("routerId", "Z9998");
        map2.put("placeIp", "255.255.255.255");
        map2.put("routerTypeId", 99998);
        map2.put("lcTypeId", 89998);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("allnetId", allnetId2);
        map3.put("routerId", "Z9997");
        map3.put("placeIp", "254.254.254.254");
        map3.put("routerTypeId", 99997);
        map3.put("lcTypeId", null);

        // FIXME 種別チェックを外しているため
        // Map<String, Object> map4 = new HashMap<String, Object>();
        // map4.put("allnetId", allnetId1);
        // map4.put("routerId", "Z9996");
        // map4.put("placeIp", "255.0.0.0");
        // map4.put("routerTypeId", 99996);
        // map4.put("lcTypeId", 89996);

        Map<String, Object> map5 = new HashMap<String, Object>();
        map5.put("allnetId", -99999);
        map5.put("routerId", "Z9995");
        map5.put("placeIp", "0.0.0.0");
        map5.put("routerTypeId", 99995);
        map5.put("lcTypeId", 89995);

        String countryCode1 = "TWN";
        String countryCode2 = "SGP";

        String format1 = ",%s,%s,%s,%s,%s";
        String format2 = ",%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));
        val += "\n"
                + String.format(format1, "", map2.get("routerId"),
                        map2.get("placeIp"), map2.get("routerTypeId"),
                        map2.get("lcTypeId"));
        val += "\n"
                + String.format(format2, map3.get("allnetId"),
                        map3.get("routerId"), map3.get("placeIp"),
                        map3.get("routerTypeId"));
        // FIXME 種別チェックを外しているため
        // val += "\n"
        // + String.format(format1, map4.get("allnetId"),
        // map4.get("routerId"), map4.get("placeIp"),
        // map4.get("routerTypeId"), map4.get("lcTypeId"));
        val += "\n"
                + String.format(format1, map5.get("allnetId"),
                        map5.get("routerId"), map5.get("placeIp"),
                        map5.get("routerTypeId"), map5.get("lcTypeId"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode1);
        deletePlaceWithRelations((Integer) map3.get("allnetId"), countryCode2);

        createCountry(countryCode1);
        createCountry(countryCode2);
        createPlace(allnetId1, placeId1, countryCode1);
        createPlace(allnetId2, placeId2, countryCode2);
        Place place3 = createPlace(allnetId3, placeId3, countryCode1);
        Place place4 = createPlace(allnetId4, placeId4, countryCode2);

        Router r1 = createRouter((String) map1.get("routerId"), place4,
                "0.0.0.0", (Integer) map1.get("routerTypeId"),
                (Integer) map1.get("lcTypeId"));
        Router r3 = createRouter((String) map3.get("routerId"), place3,
                "1.1.1.1", (Integer) map3.get("routerTypeId"),
                (Integer) map3.get("lcTypeId"));
        // FIXME 種別チェックを外しているため
        // Router r4 = createRouter((String) map4.get("routerId"), place3,
        // "2.2.2.2", (Integer) map4.get("routerTypeId"),
        // (Integer) map4.get("lcTypeId"));
        Router r5 = createRouter((String) map5.get("routerId"), place4,
                "3.3.3.3", (Integer) map5.get("routerTypeId"),
                (Integer) map5.get("lcTypeId"));
        Date createDateR1 = r1.getCreateDate();
        Date createDateR3 = r3.getCreateDate();
        // FIXME 種別チェックを外しているため
        // Date createDateR4 = r4.getCreateDate();
        Date createDateR5 = r5.getCreateDate();

        // FIXME 種別チェックを外しているため
        // createRouterType((Integer) map1.get("routerTypeId"));
        // createLcType((Integer) map1.get("lcTypeId"));
        // createRouterType((Integer) map3.get("routerTypeId"));
        // createRouterType((Integer) map5.get("routerTypeId"));
        // createLcType((Integer) map5.get("lcTypeId"));

        _em.flush();

        // RouterRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);

        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[2]);
			assertEquals(placeId1, line[3]);
			assertEquals(placeId4, line[4]);
			assertEquals("2", line[5]);
			assertEquals("1", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("routerId"), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
			assertEquals("0", line[6]);
			// 4行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("routerId"), line[1]);
			assertEquals(String.valueOf((Integer) map3.get("allnetId")), line[2]);
			assertEquals(placeId2, line[3]);
			assertEquals(placeId3, line[4]);
			assertEquals("2", line[5]);
			assertEquals("1", line[6]);
			// 5行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("4", line[0]);
			// FIXME 種別チェックを外しているため
			// assertEquals(map4.get("routerId"), line[1]);
			// assertEquals(String.valueOf((Integer) map4.get("allnetId")),
			// line[2]);
			// assertEquals("", line[3]);
			// assertEquals("", line[4]);
			// assertEquals("2", line[5]);
			// assertEquals("0", line[6]);
			// // 6行目
			// line = reader.readNext();
			// assertEquals(7, line.length);
			// assertEquals("5", line[0]);
			assertEquals(map5.get("routerId"), line[1]);
			assertEquals(String.valueOf((Integer) map5.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
			assertEquals("0", line[6]);
			// 7行目
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

        // ルータ情報のDB確認
        Router r = _em.find(Router.class, map1.get("routerId"));
        assertEquals(map1.get("allnetId"), r.getAllnetId().intValue());
        assertEquals(map1.get("routerId"), r.getRouterId());
        assertEquals(map1.get("placeIp"), r.getPlaceIp());
        assertEquals(map1.get("routerTypeId"), r.getRouterTypeId().intValue());
        assertEquals(map1.get("lcTypeId"), r.getLcTypeId().intValue());
        assertEquals("TestUserCreate", r.getCreateUserId());
        assertEquals(createDateR1, r.getCreateDate());
        assertEquals(mockUserId, r.getUpdateUserId());

        // ルータ情報のDB確認
        r = _em.find(Router.class, map3.get("routerId"));
        assertEquals(map3.get("allnetId"), r.getAllnetId().intValue());
        assertEquals(map3.get("routerId"), r.getRouterId());
        assertEquals(map3.get("placeIp"), r.getPlaceIp());
        assertEquals(map3.get("routerTypeId"), r.getRouterTypeId().intValue());
        assertNull(r.getLcTypeId());
        assertEquals("TestUserCreate", r.getCreateUserId());
        assertEquals(createDateR3, r.getCreateDate());
        assertEquals(mockUserId, r.getUpdateUserId());

        // ルータ情報のDB確認
        // FIXME 種別チェックを外しているため
        // r = _em.find(Router.class, map4.get("routerId"));
        // assertEquals(place3.getAllnetId(), r.getAllnetId().intValue());
        // assertEquals(map4.get("routerId"), r.getRouterId());
        // assertEquals("2.2.2.2", r.getPlaceIp());
        // assertEquals(map4.get("routerTypeId"),
        // r.getRouterTypeId().intValue());
        // assertEquals(map4.get("lcTypeId"), r.getLcTypeId().intValue());
        // assertEquals("TestUserCreate", r.getCreateUserId());
        // assertEquals(createDateR4, r.getCreateDate());
        // assertEquals("TestUserCreate", r.getUpdateUserId());

        // ルータ情報のDB確認
        r = _em.find(Router.class, map5.get("routerId"));
        assertEquals(place4.getAllnetId(), r.getAllnetId().intValue());
        assertEquals(map5.get("routerId"), r.getRouterId());
        assertEquals("3.3.3.3", r.getPlaceIp());
        assertEquals(map5.get("routerTypeId"), r.getRouterTypeId().intValue());
        assertEquals(map5.get("lcTypeId"), r.getLcTypeId().intValue());
        assertEquals("TestUserCreate", r.getCreateUserId());
        assertEquals(createDateR5, r.getCreateDate());
        assertEquals("TestUserCreate", r.getUpdateUserId());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);

    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterDelete() throws IOException {
        int allnetId1 = -99999;
        int allnetId2 = -99998;

        String placeId1 = "XXXX";
        String placeId2 = "YYYY";

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", 0);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", 0);
        map2.put("routerId", "Z9998");
        map2.put("placeIp", "255.255.255.255");
        map2.put("routerTypeId", 99998);
        map2.put("lcTypeId", null);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("allnetId", 0);
        map3.put("routerId", "Z9997");
        map3.put("placeIp", "255.255.255.255");
        map3.put("routerTypeId", 99997);
        map3.put("lcTypeId", 89997);

        String countryCode1 = "TWN";
        String countryCode2 = "SGP";

        String format1 = ",%s,%s,%s,%s,%s";
        String format2 = ",%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));
        val += "\n"
                + String.format(format2, map2.get("allnetId"),
                        map2.get("routerId"), map2.get("placeIp"),
                        map2.get("routerTypeId"));
        val += "\n"
                + String.format(format1, map3.get("allnetId"),
                        map3.get("routerId"), map3.get("placeIp"),
                        map3.get("routerTypeId"), map3.get("lcTypeId"));

        deletePlaceWithRelations(allnetId1, countryCode1);
        deletePlaceWithRelations(allnetId2, countryCode2);

        createCountry(countryCode1);
        createCountry(countryCode2);
        Place place1 = createPlace(allnetId1, placeId1, countryCode1);
        Place place2 = createPlace(allnetId2, placeId2, countryCode2);

        createRouter((String) map1.get("routerId"), place1, "0.0.0.0",
                (Integer) map1.get("routerTypeId"),
                (Integer) map1.get("lcTypeId"));
        createRouter((String) map2.get("routerId"), place2, "1.1.1.1",
                (Integer) map2.get("routerTypeId"),
                (Integer) map2.get("lcTypeId"));

        createRouterType((Integer) map1.get("routerTypeId"));
        createLcType((Integer) map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals(placeId1, line[4]);
			assertEquals("2", line[5]);
			assertEquals("1", line[6]);
			// 3行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("routerId"), line[1]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals(placeId2, line[4]);
			assertEquals("2", line[5]);
			assertEquals("1", line[6]);
			// 4行目
			line = reader.readNext();
			assertEquals(7, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("routerId"), line[1]);
			assertEquals(String.valueOf((Integer) map3.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
			assertEquals("2", line[6]);

			assertNull(reader.readNext());
		}

        // ルータ情報のDB確認
        Router r = _em.find(Router.class, map1.get("routerId"));
        try {
            _em.refresh(r);
        } catch (EntityNotFoundException e) {
            r = null;
        }
        assertNull(r);

        // ルータ種別情報のDB確認
        RouterType rt = _em.find(RouterType.class,
                ((Integer) map1.get("routerTypeId")));
        assertEquals(((Integer) map1.get("routerTypeId")).intValue(),
                rt.getRouterTypeId());
        assertEquals("テストルータ", rt.getName());
        assertEquals("TestUserCreate", rt.getCreateUserId());
        assertEquals("TestUserCreate", rt.getUpdateUserId());

        // 回線種別情報のDB確認
        LcType lc = _em.find(LcType.class, (Integer) map1.get("lcTypeId"));
        assertEquals(((Integer) map1.get("lcTypeId")).intValue(),
                lc.getLcTypeId());
        assertEquals("テスト回線", lc.getName());
        assertEquals("TestUserCreate", lc.getCreateUserId());
        assertEquals("TestUserCreate", lc.getUpdateUserId());

        // ルータ情報のDB確認
        r = _em.find(Router.class, map2.get("routerId"));
        try {
            _em.refresh(r);
        } catch (EntityNotFoundException e) {
            r = null;
        }
        assertNull(r);

    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterNoPlace() throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        String countryCode1 = "TWN";

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode1);

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("1", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersAllnetIdIsEmpty()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", "");
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals((String) map1.get("allnetId"), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersAllnetIdIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", 12345678901L);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersAllnetIdIsNotNum()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", "error");
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersRouterIdIsEmpty()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersRouterIdIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "12345678901234567");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersRouterIdIsInvalidLetter()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "エラー");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersPlaceIpIsEmpty()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersPlaceIpIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "1234567890123456789012345678901234567890");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersPlaceIpIsInvalidLetter()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "エラー\\_[],");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", 89999);

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersRouterTypeIdIsEmpty()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", "");
        map1.put("lcTypeId", 89999);

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersRouterTypeIdIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", "123456");
        map1.put("lcTypeId", 89999);

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersRouterTypeIdIsNotNum()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", "error");
        map1.put("lcTypeId", 89999);

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersLcTypeIdIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", "-123456");

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterServiceBean#registerRouter(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterInvalidParametersLcTypeIdIsNotNum()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("routerId", "Z9999");
        map1.put("placeIp", "0.0.0.0");
        map1.put("routerTypeId", 99999);
        map1.put("lcTypeId", "error");

        String format1 = ",%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("allnetId"),
                map1.get("routerId"), map1.get("placeIp"),
                map1.get("routerTypeId"), map1.get("lcTypeId"));

        _em.flush();

        String ret = _service.registerRouter(val);

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
			assertEquals(map1.get("routerId"), line[1]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[2]);
			assertEquals("", line[3]);
			assertEquals("", line[4]);
			assertEquals("2", line[5]);
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
        _em.createNativeQuery("DELETE FROM lc_types").executeUpdate();
        _em.createNativeQuery("DELETE FROM router_types").executeUpdate();
        _em.createNativeQuery("DELETE FROM auth_allowed_places")
                .executeUpdate();
        _em.createNativeQuery("DELETE FROM places WHERE allnet_id = :allnetId")
                .setParameter("allnetId", allnetId).executeUpdate();
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

    private RouterType createRouterType(int routerTypeId) {
        RouterType rt = new RouterType();
        rt.setRouterTypeId(routerTypeId);
        rt.setName("テストルータ");
        rt.setCreateDate(new Date());
        rt.setCreateUserId("TestUserCreate");
        rt.setUpdateDate(new Date());
        rt.setUpdateUserId("TestUserCreate");
        _em.persist(rt);
        return rt;
    }

    private LcType createLcType(int lcTypeId) {
        LcType lt = new LcType();
        lt.setLcTypeId(lcTypeId);
        lt.setName("テスト回線");
        lt.setCreateDate(new Date());
        lt.setCreateUserId("TestUserCreate");
        lt.setUpdateDate(new Date());
        lt.setUpdateUserId("TestUserCreate");
        _em.persist(lt);
        return lt;
    }

    private Router createRouter(String routerId, Place place, String placeIp,
            int routerTypeId, Integer lcTypeId) {
        Router r = new Router();
        r.setRouterId(routerId);
        r.setAllnetId(new BigDecimal(place.getAllnetId()));
        r.setPlaceIp(placeIp);
        r.setRouterTypeId(new BigDecimal(routerTypeId));
        if (lcTypeId != null) {
            r.setLcTypeId(new BigDecimal(lcTypeId));
        }
        r.setCreateDate(new Date());
        r.setCreateUserId("TestUserCreate");
        r.setUpdateDate(new Date());
        r.setUpdateUserId("TestUserCreate");
        _em.persist(r);
        return r;
    }

}
