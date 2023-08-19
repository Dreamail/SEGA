/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authdenied.bill;

import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.AuthDeniedBill;
import jp.co.sega.allnet.auth.common.entity.Bill;
import jp.co.sega.allnet.auth.common.entity.Country;
import jp.co.sega.allnet.auth.common.entity.Place;
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
 * @author TsuboiY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class AuthDeniedBillRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(AuthDeniedBillRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "authDeniedBillRegisterService")
    private AuthDeniedBillRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @After
    public void after() {
        reset(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.comp.MoveDeniedGameBillRegisterServiceBean#registerAuthDeniedComp(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterAuthDeniedBill() throws IOException {
        // 登録成功
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "2Z9901001");
        map1.put("registerFlag", "1");

        // 削除成功
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("billCode", "2Z9901002");
        map2.put("registerFlag", "0");

        // 店舗がないので失敗
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("billCode", "2Z9901003");
        map3.put("registerFlag", "1");

        // 既に登録済みなので失敗
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("billCode", "2Z9901004");
        map4.put("registerFlag", "1");

        // 削除対象がないので失敗
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("billCode", "2Z9901005");
        map5.put("registerFlag", "0");

        // 登録成功（請求先に小文字が含まれていた）
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("billCode", "2z9901006");
        map6.put("registerFlag", "1");

        String format = "%s,,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("billCode"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("billCode"));
        val += "\n"
                + String.format(format, map3.get("registerFlag"),
                        map3.get("billCode"));
        val += "\n"
                + String.format(format, map4.get("registerFlag"),
                        map4.get("billCode"));
        val += "\n"
                + String.format(format, map5.get("registerFlag"),
                        map5.get("billCode"));
        val += "\n"
                + String.format(format, map6.get("registerFlag"),
                        map6.get("billCode"));

        String countryCode = "TWN";
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId4 = -99996;
        int allnetId5 = -99995;
        int allnetId6 = -99994;
        String placeId1 = "0001";
        String placeId2 = "0002";
        String placeId4 = "0003";
        String placeId5 = "0004";
        String placeId6 = "0005";

        deleteBill(map1.get("billCode"));
        deleteBill(map2.get("billCode"));
        deleteBill(map3.get("billCode"));
        deleteBill(map4.get("billCode"));
        deleteBill(map5.get("billCode"));
        deleteBill(map6.get("billCode").toUpperCase());

        deletePlace(allnetId1);
        deletePlace(allnetId2);
        deletePlace(allnetId4);
        deletePlace(allnetId5);
        deletePlace(allnetId6);

        createBill(map1.get("billCode"));
        createBill(map2.get("billCode"));
        createBill(map4.get("billCode"));
        createBill(map5.get("billCode"));
        createBill(map6.get("billCode").toUpperCase());

        createCountry(countryCode);

        createPlace(countryCode, placeId1, map1.get("billCode"), allnetId1);
        createPlace(countryCode, placeId2, map2.get("billCode"), allnetId2);
        createPlace(countryCode, placeId4, map4.get("billCode"), allnetId4);
        createPlace(countryCode, placeId5, map5.get("billCode"), allnetId5);
        createPlace(countryCode, placeId6, map6.get("billCode").toUpperCase(),
                allnetId6);

        createAuthDeniedBill(map2.get("billCode"));
        createAuthDeniedBill(map4.get("billCode"));

        _em.flush();

        // AuthDeniedBillRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerAuthDeniedBill(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目（正常に追加）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals("正常に処理が行われました", line[2]);
			assertEquals("1", line[3]);
			// 3行目（正常に削除）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("billCode"), line[1]);
			assertEquals("正常に処理が行われました", line[2]);
			assertEquals("1", line[3]);
			// 4行目（店舗がないので失敗）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("billCode"), line[1]);
			assertEquals("店舗が未登録です", line[2]);
			assertEquals("-1", line[3]);
			// 5行目（既に登録済みなので失敗）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("4", line[0]);
			assertEquals(map4.get("billCode"), line[1]);
			assertEquals("この情報は登録済みです", line[2]);
			assertEquals("2", line[3]);
			// 6行目（削除対象がないので失敗）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("5", line[0]);
			assertEquals(map5.get("billCode"), line[1]);
			assertEquals("削除対象がありません", line[2]);
			assertEquals("3", line[3]);
			// 7行目（正常に登録・請求先コードに小文字が含まれていた）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("6", line[0]);
			assertEquals(map6.get("billCode").toUpperCase(), line[1]);
			assertEquals("正常に処理が行われました", line[2]);
			assertEquals("1", line[3]);
			// 8行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("3", line[3]);

			assertNull(reader.readNext());
		}

        // 自動認証不許可請求先情報のDB確認
        AuthDeniedBill b = _em.find(AuthDeniedBill.class, map1.get("billCode"));

        assertEquals(map1.get("billCode"), b.getBillCode());
        assertEquals(mockUserId, b.getCreateUserId());

        b = _em.find(AuthDeniedBill.class, map2.get("billCode"));

        assertNull(b);

        b = _em.find(AuthDeniedBill.class, map3.get("billCode"));

        assertNull(b);

        b = _em.find(AuthDeniedBill.class, map4.get("billCode"));

        assertEquals("TestUserCreate", b.getCreateUserId());

        b = _em.find(AuthDeniedBill.class, map5.get("billCode"));

        assertNull(b);

        b = _em.find(AuthDeniedBill.class, map6.get("billCode").toUpperCase());

        assertEquals(map6.get("billCode").toUpperCase(), b.getBillCode());
        assertEquals(mockUserId, b.getCreateUserId());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthDeniedBillInvalidParameterCompCodeIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "");
        map1.put("registerFlag", "1");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("registerFlag", "1");

        String format1 = "%s,,%s";
        String format2 = "%s,";
        String val = String.format(format1, map1.get("registerFlag"),
                map1.get("billCode"));
        val += "\n" + String.format(format2, map1.get("registerFlag"));

        String ret = _service.registerAuthDeniedBill(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals("パラメータのフォーマットが不正です", line[2]);
			assertEquals("-3", line[3]);
			// 3行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("2", line[0]);
			assertEquals("", line[1]);
			assertEquals("パラメータのフォーマットが不正です", line[2]);
			assertEquals("-3", line[3]);
			// 4行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("0", line[3]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthDeniedBillInvalidParameterCompCodeIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "2Z99010011");
        map1.put("registerFlag", "1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("billCode", "2Z990001");
        map2.put("registerFlag", "0");

        String format = "%s,,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("billCode"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("billCode"));

        String ret = _service.registerAuthDeniedBill(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals("パラメータのフォーマットが不正です", line[2]);
			assertEquals("-3", line[3]);
			// 3行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("billCode"), line[1]);
			assertEquals("パラメータのフォーマットが不正です", line[2]);
			assertEquals("-3", line[3]);
			// 4行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("0", line[3]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthDeniedBillInvalidParameterCompCodeIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "エラー");
        map1.put("registerFlag", "1");

        String format = "%s,,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("billCode"));

        String ret = _service.registerAuthDeniedBill(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals("パラメータのフォーマットが不正です", line[2]);
			assertEquals("-3", line[3]);
			// 3行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("0", line[3]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthDeniedBillInvalidParameterRegisterFlagIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "2Z9901001");

        String format = ",,%s";
        String val = String.format(format, map1.get("billCode"));

        String ret = _service.registerAuthDeniedBill(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals("登録フラグのフォーマットが不正です", line[2]);
			assertEquals("-4", line[3]);
			// 3行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("0", line[3]);

			assertNull(reader.readNext());
		}
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthDeniedBillInvalidParameterRegisterFlagIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "2Z9901001");
        map1.put("registerFlag", "A");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("billCode", "2Z9901001");
        map2.put("registerFlag", "2");

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("billCode", "2Z9901001");
        map3.put("registerFlag", "１");

        String format = "%s,,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("billCode"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("billCode"));
        val += "\n"
                + String.format(format, map3.get("registerFlag"),
                        map3.get("billCode"));

        String ret = _service.registerAuthDeniedBill(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals("登録フラグのフォーマットが不正です", line[2]);
			assertEquals("-4", line[3]);
			// 3行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("billCode"), line[1]);
			assertEquals("登録フラグのフォーマットが不正です", line[2]);
			assertEquals("-4", line[3]);
			// 4行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("billCode"), line[1]);
			assertEquals("登録フラグのフォーマットが不正です", line[2]);
			assertEquals("-4", line[3]);
			// 5行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("0", line[3]);

			assertNull(reader.readNext());
		}
    }

    private void deleteBill(String billCode) {
        _em.createNativeQuery(
                "DELETE FROM move_denied_game_bills WHERE bill_code = :billCode")
                .setParameter("billCode", billCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM auth_denied_game_bills WHERE bill_code = :billCode")
                .setParameter("billCode", billCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM auth_denied_bills WHERE bill_code = :billCode")
                .setParameter("billCode", billCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM move_denied_bills WHERE bill_code = :billCode")
                .setParameter("billCode", billCode).executeUpdate();
        _em.createNativeQuery("DELETE FROM places WHERE bill_code = :billCode")
                .setParameter("billCode", billCode).executeUpdate();
        _em.createNativeQuery("DELETE FROM bills WHERE bill_code = :billCode")
                .setParameter("billCode", billCode).executeUpdate();
    }

    private void deletePlace(int allnetId) {
        _em.createNativeQuery("DELETE FROM routers").executeUpdate();
        _em.createNativeQuery("DELETE FROM auth_allowed_places")
                .executeUpdate();
        _em.createNativeQuery("DELETE FROM places WHERE allnet_id = :allnetId")
                .setParameter("allnetId", allnetId).executeUpdate();
    }

    private void createBill(String billCode) {
        Bill b = _em.find(Bill.class, billCode);
        if (b != null) {
            return;
        }
        b = new Bill();
        b.setBillCode(billCode);
        b.setCreateDate(new Date());
        b.setCreateUserId("TestUserCreate");
        _em.persist(b);
    }

    private void createCountry(String countryCode) {
        Country c = _em.find(Country.class, countryCode);
        if (c != null) {
            return;
        }
        Date now = new Date();
        c = new Country();
        c.setCountryCode(countryCode);
        c.setCreateDate(now);
        c.setCreateUserId("TestUserCreate");
        c.setUpdateDate(now);
        c.setUpdateUserId("TestUserCreate");
        _em.persist(c);
    }

    private void createPlace(String countryCode, String placeId,
            String billCode, int allnetId) {
        _em.createQuery(
                "delete from Place p where p.allnetId = :allnetId or p.placeId = :placeId")
                .setParameter("allnetId", (long) allnetId)
                .setParameter("placeId", placeId).executeUpdate();
        Place p = new Place();
        p.setAllnetId((long) allnetId);
        p.setPlaceId(placeId);
        p.setName("テスト店舗");
        p.setBillCode(billCode);
        p.setCountryCode(countryCode);
        p.setCreateDate(new Date());
        p.setCreateUserId("TestUserCreate");
        p.setUpdateDate(new Date());
        p.setUpdateUserId("TestUserCreate");
        _em.persist(p);
    }

    private AuthDeniedBill createAuthDeniedBill(String billCode) {
        AuthDeniedBill b = _em.find(AuthDeniedBill.class, billCode);
        if (b != null) {
            return b;
        }
        b = new AuthDeniedBill();
        b.setBillCode(billCode);
        b.setCreateDate(new Date());
        b.setCreateUserId("TestUserCreate");
        _em.persist(b);
        return b;
    }

}
