/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.place;

import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jp.co.sega.allnet.auth.common.entity.Bill;
import jp.co.sega.allnet.auth.common.entity.Country;
import jp.co.sega.allnet.auth.common.entity.Place;
import jp.co.sega.allnet.auth.common.entity.Region0;
import jp.co.sega.allnet.auth.common.entity.Region0PK;
import jp.co.sega.allnet.auth.common.entity.Region1;
import jp.co.sega.allnet.auth.common.entity.Region1PK;
import jp.co.sega.allnet.auth.common.entity.Region2;
import jp.co.sega.allnet.auth.common.entity.Region2PK;
import jp.co.sega.allnet.auth.common.entity.Region3;
import jp.co.sega.allnet.auth.common.entity.Region3PK;
import jp.co.sega.allnet.auth.service.security.AuthenticationDelegate;
import jp.co.sega.allnet.auth.service.util.SequenceGenerator;
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
 * @author TsuboiY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class PlaceRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(PlaceRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "placeRegisterService")
    private PlaceRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @Resource(name = "sequenceGenerator")
    private SequenceGenerator _sequenceMock;

    @After
    public void after() {
        reset(_authMock);
        reset(_sequenceMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceNew() throws IOException {

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99998);
        map2.put("name", "テスト店舗2");
        map2.put("tel", "0120333332");
        map2.put("address", "テスト住所b");
        map2.put("zipCode", "0123459");
        map2.put("station", "テスト道案内2");
        map2.put("openTimeHour", "11");
        map2.put("openTimeMin", "30");
        map2.put("closeTimeHour", "21");
        map2.put("closeTimeMin", "45");
        map2.put("specialInfo", "テストPR2");
        map2.put("nickname", "テストニッ#クネーム2");
        map2.put("billCode", "2Z3456780");
        map2.put("countryCode", "IDN");
        map2.put("region0", "SGP地域0");
        map2.put("region1", "SGP地域1");
        map2.put("region2", "SGP地域2");
        map2.put("region3", "SGP地域3");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("name"), map2.get("tel"),
                        map2.get("address"), map2.get("zipCode"),
                        map2.get("station"), map2.get("openTimeHour"),
                        map2.get("openTimeMin"), map2.get("closeTimeHour"),
                        map2.get("closeTimeMin"), map2.get("specialInfo"),
                        map2.get("region0"), map2.get("region1"),
                        map2.get("region2"), map2.get("region3"),
                        map2.get("nickname"), map2.get("billCode"),
                        map2.get("allnetId"), map2.get("countryCode"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"),
                (String) map1.get("countryCode"), (String) map1.get("billCode"));
        deletePlaceWithRelations((Integer) map2.get("allnetId"),
                (String) map2.get("countryCode"), (String) map2.get("billCode"));

        createCountry((String) map1.get("countryCode"));
        createCountry((String) map2.get("countryCode"));
        createRegion0(1, (String) map1.get("countryCode"),
                (String) map1.get("region0"));
        createRegion0(1, (String) map2.get("countryCode"),
                (String) map2.get("region0"));

        _em.flush();

        // PlaceRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        EasyMock.expect(_sequenceMock.generatePlaceId()).andReturn("0001");
        EasyMock.expect(
                _sequenceMock.generateRegionId(
                        (String) map1.get("countryCode"), 1)).andReturn(1);
        EasyMock.expect(
                _sequenceMock.generateRegionId(
                        (String) map1.get("countryCode"), 2)).andReturn(1);
        EasyMock.expect(
                _sequenceMock.generateRegionId(
                        (String) map1.get("countryCode"), 3)).andReturn(1);
        EasyMock.expect(_sequenceMock.generatePlaceId()).andReturn("0002");
        EasyMock.expect(
                _sequenceMock.generateRegionId(
                        (String) map2.get("countryCode"), 1)).andReturn(1);
        EasyMock.expect(
                _sequenceMock.generateRegionId(
                        (String) map2.get("countryCode"), 2)).andReturn(1);
        EasyMock.expect(
                _sequenceMock.generateRegionId(
                        (String) map2.get("countryCode"), 3)).andReturn(1);
        EasyMock.replay(_sequenceMock);

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertTrue(line[2].matches("[0-9A-Z]{4}"));
			assertEquals("1", line[3]);
			assertEquals("1", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(String.valueOf((Integer) map2.get("allnetId")), line[1]);
			assertTrue(line[2].matches("[0-9A-Z]{4}"));
			assertEquals("1", line[3]);
			assertEquals("1", line[4]);
			// 4行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("2", line[4]);

			assertNull(reader.readNext());
		}

        Place p = _em.find(Place.class,
                new Long((Integer) map1.get("allnetId")));
        _em.refresh(p);

        // 店舗情報のDB確認
        assertEquals(map1.get("allnetId"), (int) p.getAllnetId());
        assertEquals(map1.get("name"), p.getName());
        assertEquals(map1.get("tel"), p.getTel());
        assertEquals(map1.get("address"), p.getAddress());
        assertEquals(map1.get("zipCode"), p.getZipCode());
        assertEquals(map1.get("station"), p.getStation());
        assertEquals(map1.get("openTimeHour") + ":" + map1.get("openTimeMin"),
                p.getOpenTime());
        assertEquals(
                map1.get("closeTimeHour") + ":" + map1.get("closeTimeMin"),
                p.getCloseTime());
        assertEquals(map1.get("specialInfo"), p.getSpecialInfo());
        assertEquals(map1.get("billCode"), p.getBill().getBillCode());
        assertEquals(map1.get("nickname"), p.getNickname());
        assertEquals(100000, p.getKbps().intValue());
        assertEquals(map1.get("countryCode"), p.getCountry().getCountryCode());
        assertEquals(map1.get("region0"), p.getRegion0().getName());
        assertEquals(map1.get("region1"), p.getRegion1().getName());
        assertEquals(map1.get("region2"), p.getRegion2().getName());
        assertEquals(map1.get("region3"), p.getRegion3().getName());
        assertEquals(mockUserId, p.getCreateUserId());
        assertEquals(mockUserId, p.getUpdateUserId());

        // 親地域コードの確認
        _em.refresh(p.getRegion1());
        assertEquals(map1.get("region0"), p.getRegion1().getParentRegion()
                .getName());
        _em.refresh(p.getRegion2());
        assertEquals(map1.get("region1"), p.getRegion2().getParentRegion()
                .getName());
        _em.refresh(p.getRegion3());
        assertEquals(map1.get("region2"), p.getRegion3().getParentRegion()
                .getName());

        p = _em.find(Place.class, new Long((Integer) map2.get("allnetId")));
        _em.refresh(p);

        assertEquals(map2.get("allnetId"), (int) p.getAllnetId());
        assertEquals(map2.get("name"), p.getName());
        assertEquals(map2.get("tel"), p.getTel());
        assertEquals(map2.get("address"), p.getAddress());
        assertEquals(map2.get("zipCode"), p.getZipCode());
        assertEquals(map2.get("station"), p.getStation());
        assertEquals(map2.get("openTimeHour") + ":" + map2.get("openTimeMin"),
                p.getOpenTime());
        assertEquals(
                map2.get("closeTimeHour") + ":" + map2.get("closeTimeMin"),
                p.getCloseTime());
        assertEquals(map2.get("specialInfo"), p.getSpecialInfo());
        assertEquals(map2.get("billCode"), p.getBill().getBillCode());
        assertEquals(map2.get("nickname"), p.getNickname());
        assertEquals(100000, p.getKbps().intValue());
        assertEquals(map2.get("countryCode"), p.getCountry().getCountryCode());
        assertEquals(map2.get("region0"), p.getRegion0().getName());
        assertEquals(map2.get("region1"), p.getRegion1().getName());
        assertEquals(map2.get("region2"), p.getRegion2().getName());
        assertEquals(map2.get("region3"), p.getRegion3().getName());
        assertEquals(mockUserId, p.getCreateUserId());
        assertEquals(mockUserId, p.getUpdateUserId());

        // 親地域コードの確認
        _em.refresh(p.getRegion1());
        assertEquals(map2.get("region0"), p.getRegion1().getParentRegion()
                .getName());
        _em.refresh(p.getRegion2());
        assertEquals(map2.get("region1"), p.getRegion2().getParentRegion()
                .getName());
        _em.refresh(p.getRegion3());
        assertEquals(map2.get("region2"), p.getRegion3().getParentRegion()
                .getName());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceUpdate() throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("placeId", "0001");
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99998);
        map2.put("placeId", "0002");
        map2.put("name", "テスト店舗2");
        map2.put("tel", "0120333332");
        map2.put("address", "テスト住所b");
        map2.put("zipCode", "0123459");
        map2.put("station", "テスト道案内2");
        map2.put("openTimeHour", "11");
        map2.put("openTimeMin", "30");
        map2.put("closeTimeHour", "21");
        map2.put("closeTimeMin", "45");
        map2.put("specialInfo", "テストPR2");
        map2.put("nickname", "テストニッ#クネーム2");
        map2.put("billCode", "2Z3456780");
        map2.put("countryCode", "SGP");
        map2.put("region0", "SGP地域0");
        map2.put("region1", "SGP地域1");
        map2.put("region2", "SGP地域2");
        map2.put("region3", "SGP地域3");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("name"), map2.get("tel"),
                        map2.get("address"), map2.get("zipCode"),
                        map2.get("station"), map2.get("openTimeHour"),
                        map2.get("openTimeMin"), map2.get("closeTimeHour"),
                        map2.get("closeTimeMin"), map2.get("specialInfo"),
                        map2.get("region0"), map2.get("region1"),
                        map2.get("region2"), map2.get("region3"),
                        map2.get("nickname"), map2.get("billCode"),
                        map2.get("allnetId"), map2.get("countryCode"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"),
                (String) map1.get("countryCode"), (String) map1.get("billCode"));
        deletePlaceWithRelations((Integer) map2.get("allnetId"),
                (String) map2.get("countryCode"), (String) map2.get("billCode"));

        Country c1 = createCountry((String) map1.get("countryCode"));
        Country c2 = createCountry((String) map2.get("countryCode"));
        createRegion0(1, (String) map1.get("countryCode"),
                (String) map1.get("region0"));
        createRegion0(10, (String) map2.get("countryCode"),
                (String) map2.get("region0"));
        createRegion1(2, (String) map1.get("countryCode"),
                (String) map1.get("region1"), 1);
        createRegion1(20, (String) map2.get("countryCode"),
                (String) map2.get("region1"), 10);
        createRegion2(3, (String) map1.get("countryCode"),
                (String) map1.get("region2"), 2);
        createRegion2(30, (String) map2.get("countryCode"),
                (String) map2.get("region2"), 20);
        createRegion3(4, (String) map1.get("countryCode"),
                (String) map1.get("region3"), 3);
        createRegion3(40, (String) map2.get("countryCode"),
                (String) map2.get("region3"), 30);
        Bill b1 = createBill((String) map1.get("billCode"));
        Bill b2 = createBill((String) map2.get("billCode"));
        Place orgPlace1 = createPlace((Integer) map1.get("allnetId"),
                (String) map1.get("placeId"), c2, b2, 10, 20, 30, 40);
        String orgCreateUserId1 = orgPlace1.getCreateUserId();
        Date orgCreateDate1 = orgPlace1.getCreateDate();
        Date orgUpdateDate1 = orgPlace1.getUpdateDate();
        Place orgPlace2 = createPlace((Integer) map2.get("allnetId"),
                (String) map2.get("placeId"), c1, b1, 1, 2, 3, 4);
        String orgCreateUserId2 = orgPlace2.getCreateUserId();
        Date orgCreateDate2 = orgPlace2.getCreateDate();
        Date orgUpdateDate2 = orgPlace2.getUpdateDate();

        _em.flush();

        // PlaceRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);

        // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertTrue(line[2].matches("[0-9A-Z]{4}"));
			assertEquals("2", line[3]);
			assertEquals("1", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(String.valueOf((Integer) map2.get("allnetId")), line[1]);
			assertTrue(line[2].matches("[0-9A-Z]{4}"));
			assertEquals("2", line[3]);
			assertEquals("1", line[4]);
			// 4行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("2", line[4]);

			assertNull(reader.readNext());
		}

        Place p = _em.find(Place.class,
                new Long((Integer) map1.get("allnetId")));
        _em.refresh(p);

        // 店舗情報のDB確認
        assertEquals(map1.get("allnetId"), (int) p.getAllnetId());
        assertEquals(map1.get("name"), p.getName());
        assertEquals(map1.get("tel"), p.getTel());
        assertEquals(map1.get("address"), p.getAddress());
        assertEquals(map1.get("zipCode"), p.getZipCode());
        assertEquals(map1.get("station"), p.getStation());
        assertEquals(map1.get("openTimeHour") + ":" + map1.get("openTimeMin"),
                p.getOpenTime());
        assertEquals(
                map1.get("closeTimeHour") + ":" + map1.get("closeTimeMin"),
                p.getCloseTime());
        assertEquals(map1.get("specialInfo"), p.getSpecialInfo());
        assertEquals(map1.get("billCode"), p.getBill().getBillCode());
        assertEquals(map1.get("nickname"), p.getNickname());
        assertEquals(100000, p.getKbps().intValue());
        assertEquals(map1.get("countryCode"), p.getCountry().getCountryCode());
        assertEquals(map1.get("region0"), p.getRegion0().getName());
        assertEquals(map1.get("region1"), p.getRegion1().getName());
        assertEquals(map1.get("region2"), p.getRegion2().getName());
        assertEquals(map1.get("region3"), p.getRegion3().getName());
        assertEquals(orgCreateDate1, p.getCreateDate());
        assertEquals(orgCreateUserId1, p.getCreateUserId());
        assertNotSame(orgUpdateDate1, p.getUpdateDate());
        assertEquals("test", p.getUpdateUserId());

        p = _em.find(Place.class, new Long((Integer) map2.get("allnetId")));
        _em.refresh(p);

        assertEquals(map2.get("allnetId"), (int) p.getAllnetId());
        assertEquals(map2.get("name"), p.getName());
        assertEquals(map2.get("tel"), p.getTel());
        assertEquals(map2.get("address"), p.getAddress());
        assertEquals(map2.get("zipCode"), p.getZipCode());
        assertEquals(map2.get("station"), p.getStation());
        assertEquals(map2.get("openTimeHour") + ":" + map2.get("openTimeMin"),
                p.getOpenTime());
        assertEquals(
                map2.get("closeTimeHour") + ":" + map2.get("closeTimeMin"),
                p.getCloseTime());
        assertEquals(map2.get("specialInfo"), p.getSpecialInfo());
        assertEquals(map2.get("billCode"), p.getBill().getBillCode());
        assertEquals(map2.get("nickname"), p.getNickname());
        assertEquals(100000, p.getKbps().intValue());
        assertEquals(map2.get("countryCode"), p.getCountry().getCountryCode());
        assertEquals(map2.get("region0"), p.getRegion0().getName());
        assertEquals(map2.get("region1"), p.getRegion1().getName());
        assertEquals(map2.get("region2"), p.getRegion2().getName());
        assertEquals(map2.get("region3"), p.getRegion3().getName());
        assertEquals(orgCreateDate2, p.getCreateDate());
        assertEquals(orgCreateUserId2, p.getCreateUserId());
        assertNotSame(orgUpdateDate2, p.getUpdateDate());
        assertEquals("test", p.getUpdateUserId());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceCountryIsEmpty() throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル ");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"));

        String countryCode = "JPN";
        deletePlaceWithRelations((Integer) map1.get("allnetId"), countryCode,
                (String) map1.get("billCode"));

        createCountry(countryCode);
        createRegion0(1, countryCode, (String) map1.get("region0"));

        _em.flush();

        // PlaceRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);

        // 振る舞いを記憶
        EasyMock.replay(_authMock);

        EasyMock.expect(_sequenceMock.generatePlaceId()).andReturn("0001");
        EasyMock.expect(_sequenceMock.generateRegionId(countryCode, 1))
                .andReturn(1);
        EasyMock.expect(_sequenceMock.generateRegionId(countryCode, 2))
                .andReturn(1);
        EasyMock.expect(_sequenceMock.generateRegionId(countryCode, 3))
                .andReturn(1);
        EasyMock.replay(_sequenceMock);

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertTrue(line[2].matches("[0-9A-Z]{4}"));
			assertEquals("1", line[3]);
			assertEquals("1", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("1", line[4]);

			assertNull(reader.readNext());
		}
        Place p = _em.find(Place.class,
                new Long((Integer) map1.get("allnetId")));
        _em.refresh(p);

        // 店舗情報のDB確認
        assertEquals(map1.get("allnetId"), (int) p.getAllnetId());
        assertEquals(map1.get("name"), p.getName());
        assertEquals(map1.get("tel"), p.getTel());
        assertEquals(map1.get("address"), p.getAddress());
        assertEquals(map1.get("zipCode"), p.getZipCode());
        assertEquals(map1.get("station"), p.getStation());
        assertEquals(map1.get("openTimeHour") + ":" + map1.get("openTimeMin"),
                p.getOpenTime());
        assertEquals(
                map1.get("closeTimeHour") + ":" + map1.get("closeTimeMin"),
                p.getCloseTime());
        assertEquals(map1.get("specialInfo"), p.getSpecialInfo());
        assertEquals(map1.get("billCode"), p.getBill().getBillCode());
        assertEquals(map1.get("nickname"), p.getNickname());
        assertEquals(100000, p.getKbps().intValue());
        assertEquals(countryCode, p.getCountry().getCountryCode());
        assertEquals(map1.get("region0"), p.getRegion0().getName());
        assertEquals(map1.get("region1"), p.getRegion1().getName());
        assertEquals(map1.get("region2"), p.getRegion2().getName());
        assertEquals(((String) map1.get("region3")).trim(), p.getRegion3()
                .getName());
        assertEquals("test", p.getCreateUserId());
        assertEquals("test", p.getUpdateUserId());

        // 親地域コードの確認
        _em.refresh(p.getRegion1());
        assertEquals(map1.get("region0"), p.getRegion1().getParentRegion()
                .getName());
        _em.refresh(p.getRegion2());
        assertEquals(map1.get("region1"), p.getRegion2().getParentRegion()
                .getName());
        _em.refresh(p.getRegion3());
        assertEquals(map1.get("region2"), p.getRegion3().getParentRegion()
                .getName());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceRegion0IsEmpty() throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"),
                (String) map1.get("countryCode"), (String) map1.get("billCode"));

        createCountry((String) map1.get("countryCode"));

        _em.flush();

        // PlaceRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);

        // 振る舞いを記憶
        EasyMock.replay(_authMock);

        EasyMock.expect(_sequenceMock.generatePlaceId()).andReturn("0001");
        EasyMock.replay(_sequenceMock);

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertTrue(line[2].matches("[0-9A-Z]{4}"));
			assertEquals("1", line[3]);
			assertEquals("1", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("1", line[4]);

			assertNull(reader.readNext());
		}
        Place p = _em.find(Place.class,
                new Long((Integer) map1.get("allnetId")));
        _em.refresh(p);

        // 店舗情報のDB確認
        assertEquals(map1.get("allnetId"), (int) p.getAllnetId());
        assertEquals(map1.get("name"), p.getName());
        assertEquals(map1.get("tel"), p.getTel());
        assertEquals(map1.get("address"), p.getAddress());
        assertEquals(map1.get("zipCode"), p.getZipCode());
        assertEquals(map1.get("station"), p.getStation());
        assertEquals(map1.get("openTimeHour") + ":" + map1.get("openTimeMin"),
                p.getOpenTime());
        assertEquals(
                map1.get("closeTimeHour") + ":" + map1.get("closeTimeMin"),
                p.getCloseTime());
        assertEquals(map1.get("specialInfo"), p.getSpecialInfo());
        assertEquals(map1.get("billCode"), p.getBill().getBillCode());
        assertEquals(map1.get("nickname"), p.getNickname());
        assertEquals(100000, p.getKbps().intValue());
        assertEquals(map1.get("countryCode"), p.getCountry().getCountryCode());
        assertNull(p.getRegion0Id());
        assertNull(p.getRegion1Id());
        assertNull(p.getRegion2Id());
        assertNull(p.getRegion3Id());
        assertEquals("test", p.getCreateUserId());
        assertEquals("test", p.getUpdateUserId());
        // 地域1に存在しないことを確認する
        Query query = _em
                .createQuery("SELECT count(r.pk.regionId) FROM Region1 r WHERE r.pk.countryCode = :countryCode AND r.name = :name");
        query.setParameter("countryCode", map1.get("countryCode"));
        query.setParameter("name", map1.get("region1"));
        assertEquals(0l, query.getSingleResult());
        // 地域2に存在しないことを確認する
        query = _em
                .createQuery("SELECT count(r.pk.regionId) FROM Region2 r WHERE r.pk.countryCode = :countryCode AND r.name = :name");
        query.setParameter("countryCode", map1.get("countryCode"));
        query.setParameter("name", map1.get("region2"));
        assertEquals(0l, query.getSingleResult());
        // 地域3に存在しないことを確認する
        query = _em
                .createQuery("SELECT count(r.pk.regionId) FROM Region3 r WHERE r.pk.countryCode = :countryCode AND r.name = :name");
        query.setParameter("countryCode", map1.get("countryCode"));
        query.setParameter("name", map1.get("region2"));
        assertEquals(0l, query.getSingleResult());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceRegion123AreEmpty() throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "");
        map1.put("region2", "");
        map1.put("region3", "");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"),
                (String) map1.get("countryCode"), (String) map1.get("billCode"));

        createCountry((String) map1.get("countryCode"));
        createRegion0(1, (String) map1.get("countryCode"),
                (String) map1.get("region0"));
        _em.flush();

        // PlaceRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);

        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        EasyMock.expect(_sequenceMock.generatePlaceId()).andReturn("0001");
        EasyMock.replay(_sequenceMock);

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertTrue(line[2].matches("[0-9A-Z]{4}"));
			assertEquals("1", line[3]);
			assertEquals("1", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("1", line[4]);

			assertNull(reader.readNext());
		}
        Place p = _em.find(Place.class,
                new Long((Integer) map1.get("allnetId")));
        _em.refresh(p);

        // 店舗情報のDB確認
        assertEquals(map1.get("allnetId"), (int) p.getAllnetId());
        assertEquals(map1.get("name"), p.getName());
        assertEquals(map1.get("tel"), p.getTel());
        assertEquals(map1.get("address"), p.getAddress());
        assertEquals(map1.get("zipCode"), p.getZipCode());
        assertEquals(map1.get("station"), p.getStation());
        assertEquals(map1.get("openTimeHour") + ":" + map1.get("openTimeMin"),
                p.getOpenTime());
        assertEquals(
                map1.get("closeTimeHour") + ":" + map1.get("closeTimeMin"),
                p.getCloseTime());
        assertEquals(map1.get("specialInfo"), p.getSpecialInfo());
        assertEquals(map1.get("billCode"), p.getBill().getBillCode());
        assertEquals(map1.get("nickname"), p.getNickname());
        assertEquals(100000, p.getKbps().intValue());
        assertEquals(map1.get("countryCode"), p.getCountry().getCountryCode());
        assertEquals(map1.get("region0"), p.getRegion0().getName());
        assertNull(p.getRegion1());
        assertNull(p.getRegion2());
        assertNull(p.getRegion3());
        assertEquals(mockUserId, p.getCreateUserId());
        assertEquals(mockUserId, p.getUpdateUserId());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceBillIsEmpty() throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"),
                (String) map1.get("countryCode"), (String) map1.get("billCode"));

        createCountry((String) map1.get("countryCode"));
        createRegion0(1, (String) map1.get("countryCode"),
                (String) map1.get("region0"));

        _em.flush();

        // PlaceRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);

        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        EasyMock.expect(_sequenceMock.generatePlaceId()).andReturn("0001");
        EasyMock.expect(
                _sequenceMock.generateRegionId(
                        (String) map1.get("countryCode"), 1)).andReturn(1);
        EasyMock.expect(
                _sequenceMock.generateRegionId(
                        (String) map1.get("countryCode"), 2)).andReturn(1);
        EasyMock.expect(
                _sequenceMock.generateRegionId(
                        (String) map1.get("countryCode"), 3)).andReturn(1);
        EasyMock.replay(_sequenceMock);

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertTrue(line[2].matches("[0-9A-Z]{4}"));
			assertEquals("1", line[3]);
			assertEquals("1", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("1", line[4]);

			assertNull(reader.readNext());
		}

        Place p = _em.find(Place.class,
                new Long((Integer) map1.get("allnetId")));
        _em.refresh(p);

        // 店舗情報のDB確認
        assertEquals(map1.get("allnetId"), (int) p.getAllnetId());
        assertEquals(map1.get("name"), p.getName());
        assertEquals(map1.get("tel"), p.getTel());
        assertEquals(map1.get("address"), p.getAddress());
        assertEquals(map1.get("zipCode"), p.getZipCode());
        assertEquals(map1.get("station"), p.getStation());
        assertEquals(map1.get("openTimeHour") + ":" + map1.get("openTimeMin"),
                p.getOpenTime());
        assertEquals(
                map1.get("closeTimeHour") + ":" + map1.get("closeTimeMin"),
                p.getCloseTime());
        assertEquals(map1.get("specialInfo"), p.getSpecialInfo());
        assertNull(p.getBill());
        assertEquals(map1.get("nickname"), p.getNickname());
        assertEquals(100000, p.getKbps().intValue());
        assertEquals(map1.get("countryCode"), p.getCountry().getCountryCode());
        assertEquals(map1.get("region0"), p.getRegion0().getName());
        assertEquals(map1.get("region1"), p.getRegion1().getName());
        assertEquals(map1.get("region2"), p.getRegion2().getName());
        assertEquals(map1.get("region3"), p.getRegion3().getName());
        assertEquals(mockUserId, p.getCreateUserId());
        assertEquals(mockUserId, p.getUpdateUserId());

        // 親地域コードの確認
        _em.refresh(p.getRegion1());
        assertEquals(map1.get("region0"), p.getRegion1().getParentRegion()
                .getName());
        _em.refresh(p.getRegion2());
        assertEquals(map1.get("region1"), p.getRegion2().getParentRegion()
                .getName());
        _em.refresh(p.getRegion3());
        assertEquals(map1.get("region2"), p.getRegion3().getParentRegion()
                .getName());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersAllnetIdIsEmpty()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", "");
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersAllnetIdIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", 12345678901l);

        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersAllnetIdIsNotNum()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", "12345678901");
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("allnetId"), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersNameIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "あいうえおかきくけこさしすせそたちつてとn");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersTelIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "12345678901234567");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        deletePlaceWithRelations((Integer) map1.get("allnetId"),
                (String) map1.get("countryCode"), (String) map1.get("billCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersTelIsNotNum()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "error-");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersAddressIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address",
                "あいうえおかきくけこさしすせそたちつてとなにぬねのあいうえおかきくけこさしすせそたちつてとなにぬねのh");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersZipCodeIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "1234567890");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersZipCodeIsNotNum()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "error-");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersStationIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "あいうえおかきくけこさしすせそたちるてとなにぬねのあいうえおかきくけこさしすせそt");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersOpenTimeHourOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "100");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersOpenTimeHourIsNotNum()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "error");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersOpenTimeMinIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "60");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersOpenTimeMinIsNotNum()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "error");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersCloseTimeHourIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "100");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersCloseTimeHourIsNotNum()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "error");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersCloseTimeMinIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "60");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersCloseTimeMinIsNotNum()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "error");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersSpecialInfoIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo",
                "あいうえおかきくけこさしすせそたちつてとなにぬねのあいうえおかきくけこさしすせそたちつてとなにぬねのあいうえおかきくけこさしすせs");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersNicknameIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "あいうえおかきくけこさしすせ");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersNicknameIsOverMaxBySepF()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニックネー#ム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersNicknameIsOverMaxBySepR()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テス#トニックネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersBillCodeIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "1234567890");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersBillCodeIsNotValid()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "エラー");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersCountryCodeIsNotValid()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "JPNN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersCountryCodeIsNotLetter()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "012");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersRegion0IsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "あいうえおかきくけこさしすせそたt");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersRegion1IsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "あいうえおかきくけこさしすせそたt");
        map1.put("region2", "東糀谷");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersRegion2IsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "あいうえおかきくけこさしすせそたt");
        map1.put("region3", "TRC羽田ビル");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterServiceBean#registerPlace(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterPlaceInvalidParametersRegion3IsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("name", "テスト店舗1");
        map1.put("tel", "0120333333");
        map1.put("address", "テスト住所A");
        map1.put("zipCode", "0123456");
        map1.put("station", "テスト道案内1");
        map1.put("openTimeHour", "10");
        map1.put("openTimeMin", "00");
        map1.put("closeTimeHour", "23");
        map1.put("closeTimeMin", "15");
        map1.put("specialInfo", "テストPR1");
        map1.put("nickname", "テストニッ#クネーム1");
        map1.put("billCode", "2Z3456789");
        map1.put("countryCode", "TWN");
        map1.put("region0", "東京都");
        map1.put("region1", "大田区");
        map1.put("region2", "東糀谷");
        map1.put("region3", "あいうえおかきくけこさしすせそたt");

        String format = "%s,%s,%s,,,,%s,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("name"), map1.get("tel"),
                map1.get("address"), map1.get("zipCode"), map1.get("station"),
                map1.get("openTimeHour"), map1.get("openTimeMin"),
                map1.get("closeTimeHour"), map1.get("closeTimeMin"),
                map1.get("specialInfo"), map1.get("region0"),
                map1.get("region1"), map1.get("region2"), map1.get("region3"),
                map1.get("nickname"), map1.get("billCode"),
                map1.get("allnetId"), map1.get("countryCode"));

        String ret = _service.registerPlace(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);

			// 2行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("0", line[4]);

			assertNull(reader.readNext());
		}
    }

    private void deletePlaceWithRelations(int allnetId, String countryCode,
            String billCode) {
        deletePlace(allnetId, billCode);
        deleteRegions(countryCode);

        _em.createNativeQuery("DELETE FROM country_download_orders")
                .executeUpdate();
        _em.createNativeQuery("DELETE FROM game_attributes").executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM countries WHERE country_code = :countryCode")
                .setParameter("countryCode", countryCode).executeUpdate();
        _em.createNativeQuery("DELETE FROM move_denied_bills").executeUpdate();
        _em.createNativeQuery("DELETE FROM auth_denied_bills").executeUpdate();
        _em.createNativeQuery("DELETE FROM move_denied_game_bills")
                .executeUpdate();
        _em.createNativeQuery("DELETE FROM auth_denied_game_bills")
                .executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM bills WHERE bill_code = :billCode OR bill_code IN (SELECT bill_code FROM places WHERE allnet_id = :allnetId)")
                .setParameter("billCode", billCode)
                .setParameter("allnetId", allnetId).executeUpdate();
    }

    private void deleteRegions(String countryCode) {
        _em.createNativeQuery("DELETE FROM routers").executeUpdate();
        _em.createNativeQuery("DELETE FROM auth_allowed_places")
                .executeUpdate();
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

    private void deletePlace(int allnetId, String billCode) {
        _em.createNativeQuery("DELETE FROM routers").executeUpdate();
        _em.createNativeQuery("DELETE FROM auth_allowed_places")
                .executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM places WHERE allnet_id = :allnetId OR bill_code = :billCode")
                .setParameter("allnetId", allnetId)
                .setParameter("billCode", billCode).executeUpdate();
    }

    private Country createCountry(String countryCode) {
        Country c = new Country();
        c.setCountryCode(countryCode);
        c.setCountryName(countryCode);
        c.setCreateDate(new Date());
        c.setCreateUserId("TestUser");
        c.setUpdateDate(new Date());
        c.setUpdateUserId("TestUser");
        _em.persist(c);
        return c;
    }

    private void createRegion0(int regionId, String countryCode, String name) {
        Region0 r = new Region0();
        Region0PK pk = new Region0PK();
        pk.setCountryCode(countryCode);
        pk.setRegionId(new BigDecimal(regionId));
        r.setPk(pk);
        r.setName(name);
        r.setCreateDate(new Date());
        r.setCreateUserId("TestUser");
        r.setUpdateDate(new Date());
        r.setUpdateUserId("TestUser");
        _em.persist(r);
    }

    private void createRegion1(int regionId, String countryCode, String name,
            int parentRegionId) {
        Region1 r = new Region1();
        Region1PK pk = new Region1PK();
        pk.setCountryCode(countryCode);
        pk.setRegionId(new BigDecimal(regionId));
        r.setPk(pk);
        r.setName(name);
        r.setParentRegionId(new BigDecimal(parentRegionId));
        r.setCreateDate(new Date());
        r.setCreateUserId("TestUser");
        r.setUpdateDate(new Date());
        r.setUpdateUserId("TestUser");
        _em.persist(r);
    }

    private void createRegion2(int regionId, String countryCode, String name,
            int parentRegionId) {
        Region2 r = new Region2();
        Region2PK pk = new Region2PK();
        pk.setCountryCode(countryCode);
        pk.setRegionId(new BigDecimal(regionId));
        r.setPk(pk);
        r.setName(name);
        r.setParentRegionId(new BigDecimal(parentRegionId));
        r.setCreateDate(new Date());
        r.setCreateUserId("TestUser");
        r.setUpdateDate(new Date());
        r.setUpdateUserId("TestUser");
        _em.persist(r);
    }

    private void createRegion3(int regionId, String countryCode, String name,
            int parentRegionId) {
        Region3 r = new Region3();
        Region3PK pk = new Region3PK();
        pk.setCountryCode(countryCode);
        pk.setRegionId(new BigDecimal(regionId));
        r.setPk(pk);
        r.setName(name);
        r.setParentRegionId(new BigDecimal(parentRegionId));
        r.setCreateDate(new Date());
        r.setCreateUserId("TestUser");
        r.setUpdateDate(new Date());
        r.setUpdateUserId("TestUser");
        _em.persist(r);
    }

    private Bill createBill(String billCode) {
        Bill b = new Bill();
        b.setBillCode(billCode);
        b.setCreateDate(new Date());
        b.setCreateUserId("TestUser");
        _em.persist(b);
        return b;
    }

    private Place createPlace(int allnetId, String placeId, Country country,
            Bill bill, int region0Id, int region1Id, int region2Id,
            int region3Id) {
        Place place = new Place();
        place.setAllnetId(allnetId);
        place.setPlaceId(placeId);
        place.setName("店舗名_変更前");
        place.setTel("0000000000");
        place.setAddress("店舗名_変更前");
        place.setZipCode("9999999");
        place.setStation("店舗名_変更前");
        place.setOpenTime("00:00");
        place.setCloseTime("24:00");
        place.setSpecialInfo("店舗名_変更前");
        place.setBillCode(bill.getBillCode());
        place.setNickname("店舗名_変更前");
        place.setCountryCode(country.getCountryCode());
        place.setRegion0Id(new BigDecimal(region0Id));
        place.setRegion1Id(new BigDecimal(region1Id));
        place.setRegion2Id(new BigDecimal(region2Id));
        place.setRegion3Id(new BigDecimal(region3Id));
        place.setCreateDate(new Date());
        place.setCreateUserId("TestUserCraete");
        place.setUpdateDate(new Date());
        place.setUpdateUserId("TestUserCreate");
        _em.persist(place);
        return place;
    }

}
