/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authdenied.gamebill;

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

import jp.co.sega.allnet.auth.common.entity.AuthDeniedGameBill;
import jp.co.sega.allnet.auth.common.entity.AuthDeniedGameBillPK;
import jp.co.sega.allnet.auth.common.entity.Bill;
import jp.co.sega.allnet.auth.common.entity.Country;
import jp.co.sega.allnet.auth.common.entity.Game;
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
public class AuthDeniedGameBillRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(AuthDeniedGameBillRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "authDeniedGameBillRegisterService")
    private AuthDeniedGameBillRegisterService _service;

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
    public final void testRegisterAuthDeniedGameBill() throws IOException {
        // 登録成功
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "2Z9901001");
        map1.put("gameId", "SBXX");
        map1.put("registerFlag", "1");

        // 削除成功
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("billCode", "2Z9901002");
        map2.put("gameId", "SBXX");
        map2.put("registerFlag", "0");

        // 請求先がないので失敗
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("billCode", "2Z9901003");
        map3.put("gameId", "SBXX");
        map3.put("registerFlag", "1");

        // 既に登録済みなので失敗
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("billCode", "2Z9901004");
        map4.put("gameId", "SBXX");
        map4.put("registerFlag", "1");

        // 削除対象がないので失敗
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("billCode", "2Z9901005");
        map5.put("gameId", "SBXX");
        map5.put("registerFlag", "0");

        // ゲームIDがないので失敗
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("billCode", "2Z9901001");
        map6.put("gameId", "SBYY");
        map6.put("registerFlag", "1");

        // 請求先とゲームIDがないので失敗
        Map<String, String> map7 = new HashMap<String, String>();
        map7.put("billCode", "2Z9901003");
        map7.put("gameId", "SBYY");
        map7.put("registerFlag", "1");

        // 登録成功（小文字が含まれていた）
        Map<String, String> map8 = new HashMap<String, String>();
        map8.put("billCode", "2z9901008");
        map8.put("gameId", "sBxX");
        map8.put("registerFlag", "1");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("billCode"), map1.get("gameId"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("billCode"), map2.get("gameId"));
        val += "\n"
                + String.format(format, map3.get("registerFlag"),
                        map3.get("billCode"), map3.get("gameId"));
        val += "\n"
                + String.format(format, map4.get("registerFlag"),
                        map4.get("billCode"), map4.get("gameId"));
        val += "\n"
                + String.format(format, map5.get("registerFlag"),
                        map5.get("billCode"), map5.get("gameId"));
        val += "\n"
                + String.format(format, map6.get("registerFlag"),
                        map6.get("billCode"), map6.get("gameId"));
        val += "\n"
                + String.format(format, map7.get("registerFlag"),
                        map7.get("billCode"), map7.get("gameId"));
        val += "\n"
                + String.format(format, map8.get("registerFlag"),
                        map8.get("billCode"), map8.get("gameId"));

        String countryCode = "TWN";
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId4 = -99996;
        int allnetId5 = -99995;
        int allnetId8 = -99994;
        String placeId1 = "0001";
        String placeId2 = "0002";
        String placeId4 = "0003";
        String placeId5 = "0004";
        String placeId8 = "0005";

        deleteBill(map1.get("billCode"));
        deleteBill(map2.get("billCode"));
        deleteBill(map3.get("billCode"));
        deleteBill(map4.get("billCode"));
        deleteBill(map5.get("billCode"));
        deleteBill(map8.get("billCode").toUpperCase());

        deletePlace(allnetId1);
        deletePlace(allnetId2);
        deletePlace(allnetId4);
        deletePlace(allnetId5);
        deletePlace(allnetId8);

        deleteGame(map1.get("gameId"));
        deleteGame(map6.get("gameId"));
        deleteGame(map8.get("gameId"));

        createBill(map1.get("billCode"));
        createBill(map2.get("billCode"));
        createBill(map4.get("billCode"));
        createBill(map5.get("billCode"));
        createBill(map8.get("billCode").toUpperCase());

        createCountry(countryCode);

        createPlace(countryCode, placeId1, map1.get("billCode"), allnetId1);
        createPlace(countryCode, placeId2, map2.get("billCode"), allnetId2);
        createPlace(countryCode, placeId4, map4.get("billCode"), allnetId4);
        createPlace(countryCode, placeId5, map5.get("billCode"), allnetId5);
        createPlace(countryCode, placeId8, map8.get("billCode").toUpperCase(),
                allnetId8);

        createGame(map1.get("gameId"));

        createAuthDeniedGameBill(map2.get("billCode"), map2.get("gameId"));
        createAuthDeniedGameBill(map4.get("billCode"), map4.get("gameId"));

        _em.flush();

        // AuthDeniedGameBillRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerAuthDeniedGameBill(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目（正常に追加）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("正常に処理が行われました", line[3]);
			assertEquals("1", line[4]);
			// 3行目（正常に削除）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("billCode"), line[1]);
			assertEquals(map2.get("gameId"), line[2]);
			assertEquals("正常に処理が行われました", line[3]);
			assertEquals("1", line[4]);
			// 4行目（店舗がないので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("billCode"), line[1]);
			assertEquals(map3.get("gameId"), line[2]);
			assertEquals("店舗が未登録です", line[3]);
			assertEquals("-1", line[4]);
			// 5行目（既に登録済みなので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("4", line[0]);
			assertEquals(map4.get("billCode"), line[1]);
			assertEquals(map4.get("gameId"), line[2]);
			assertEquals("この情報は登録済みです", line[3]);
			assertEquals("2", line[4]);
			// 6行目（削除対象がないので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("5", line[0]);
			assertEquals(map5.get("billCode"), line[1]);
			assertEquals(map5.get("gameId"), line[2]);
			assertEquals("削除対象がありません", line[3]);
			assertEquals("3", line[4]);
			// 7行目（ゲームIDがないので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("6", line[0]);
			assertEquals(map6.get("billCode"), line[1]);
			assertEquals(map6.get("gameId"), line[2]);
			assertEquals("ゲームIDが未登録です", line[3]);
			assertEquals("-2", line[4]);
			// 8行目（店舗・ゲームIDがないので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("7", line[0]);
			assertEquals(map7.get("billCode"), line[1]);
			assertEquals(map7.get("gameId"), line[2]);
			assertEquals("店舗が未登録です", line[3]);
			assertEquals("-1", line[4]);
			// 9行目（正常に追加・小文字が含まれていた）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("8", line[0]);
			assertEquals(map8.get("billCode").toUpperCase(), line[1]);
			assertEquals(map8.get("gameId").toUpperCase(), line[2]);
			assertEquals("正常に処理が行われました", line[3]);
			assertEquals("1", line[4]);
			// 10行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("", line[3]);
			assertEquals("3", line[4]);

			assertNull(reader.readNext());
		}

        // 自動認証不許可ゲーム・請求先情報のDB確認
        AuthDeniedGameBillPK pk = new AuthDeniedGameBillPK();
        pk.setBillCode(map1.get("billCode"));
        pk.setGameId(map1.get("gameId"));
        AuthDeniedGameBill c = _em.find(AuthDeniedGameBill.class, pk);

        assertEquals(map1.get("billCode"), c.getPk().getBillCode());
        assertEquals(map1.get("gameId"), c.getPk().getGameId());
        assertEquals(mockUserId, c.getCreateUserId());

        pk = new AuthDeniedGameBillPK();
        pk.setBillCode(map2.get("billCode"));
        pk.setGameId(map2.get("gameId"));
        c = _em.find(AuthDeniedGameBill.class, pk);

        assertNull(c);

        pk = new AuthDeniedGameBillPK();
        pk.setBillCode(map3.get("billCode"));
        pk.setGameId(map3.get("gameId"));
        c = _em.find(AuthDeniedGameBill.class, pk);

        assertNull(c);

        pk = new AuthDeniedGameBillPK();
        pk.setBillCode(map4.get("billCode"));
        pk.setGameId(map4.get("gameId"));
        c = _em.find(AuthDeniedGameBill.class, pk);

        assertEquals("TestUserCreate", c.getCreateUserId());

        pk = new AuthDeniedGameBillPK();
        pk.setBillCode(map5.get("billCode"));
        pk.setGameId(map5.get("gameId"));
        c = _em.find(AuthDeniedGameBill.class, pk);

        assertNull(c);

        pk = new AuthDeniedGameBillPK();
        pk.setBillCode(map6.get("billCode"));
        pk.setGameId(map6.get("gameId"));
        c = _em.find(AuthDeniedGameBill.class, pk);

        assertNull(c);

        pk = new AuthDeniedGameBillPK();
        pk.setBillCode(map7.get("billCode"));
        pk.setGameId(map7.get("gameId"));
        c = _em.find(AuthDeniedGameBill.class, pk);

        assertNull(c);

        pk = new AuthDeniedGameBillPK();
        pk.setBillCode(map8.get("billCode").toUpperCase());
        pk.setGameId(map8.get("gameId").toUpperCase());
        c = _em.find(AuthDeniedGameBill.class, pk);

        assertEquals(map8.get("billCode").toUpperCase(), c.getPk()
                .getBillCode());
        assertEquals(map8.get("gameId").toUpperCase(), c.getPk().getGameId());
        assertEquals(mockUserId, c.getCreateUserId());

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
    public void testRegisterAuthDeniedGameBillInvalidParameterCompCodeIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "");
        map1.put("gameId", "SBXX");
        map1.put("registerFlag", "1");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("billCode"), map1.get("gameId"));

        String ret = _service.registerAuthDeniedGameBill(val);
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
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("パラメータのフォーマットが不正です", line[3]);
			assertEquals("-3", line[4]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthDeniedGameBillInvalidParameterCompCodeIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "2Z99011001");
        map1.put("gameId", "SBXX");
        map1.put("registerFlag", "1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("billCode", "2Z991001");
        map2.put("gameId", "SBXX");
        map2.put("registerFlag", "0");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("billCode"), map1.get("gameId"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("billCode"), map2.get("gameId"));

        String ret = _service.registerAuthDeniedGameBill(val);
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
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("パラメータのフォーマットが不正です", line[3]);
			assertEquals("-3", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("billCode"), line[1]);
			assertEquals(map2.get("gameId"), line[2]);
			assertEquals("パラメータのフォーマットが不正です", line[3]);
			assertEquals("-3", line[4]);
			// 4行目
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthDeniedGameBillInvalidParameterCompCodeIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "エラー");
        map1.put("gameId", "SBXX");
        map1.put("registerFlag", "1");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("billCode"), map1.get("gameId"));

        String ret = _service.registerAuthDeniedGameBill(val);
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
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("パラメータのフォーマットが不正です", line[3]);
			assertEquals("-3", line[4]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthDeniedGameBillInvalidParameterGameIdIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "2Z9901001");
        map1.put("gameId", "");
        map1.put("registerFlag", "1");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("billCode", "2Z9901001");
        map2.put("registerFlag", "1");

        String format1 = "%s,,%s,%s";
        String format2 = "%s,,%s";
        String val = String.format(format1, map1.get("registerFlag"),
                map1.get("billCode"), map1.get("gameId"));
        val += "\n"
                + String.format(format2, map2.get("registerFlag"),
                        map2.get("billCode"));

        String ret = _service.registerAuthDeniedGameBill(val);
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
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("パラメータのフォーマットが不正です", line[3]);
			assertEquals("-3", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("billCode"), line[1]);
			assertEquals("", line[2]);
			assertEquals("パラメータのフォーマットが不正です", line[3]);
			assertEquals("-3", line[4]);
			// 4行目
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthDeniedGameBillInvalidParameterGameIdIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "2Z9901001");
        map1.put("gameId", "SBXXZZ");
        map1.put("registerFlag", "1");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("billCode"), map1.get("gameId"));

        String ret = _service.registerAuthDeniedGameBill(val);
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
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("パラメータのフォーマットが不正です", line[3]);
			assertEquals("-3", line[4]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthDeniedGameBillInvalidParameterGameIdIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "2Z9901001");
        map1.put("gameId", "エラー");
        map1.put("registerFlag", "1");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("billCode"), map1.get("gameId"));

        String ret = _service.registerAuthDeniedGameBill(val);
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
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("パラメータのフォーマットが不正です", line[3]);
			assertEquals("-3", line[4]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthDeniedGameBillInvalidParameterRegisterFlagIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "2Z9901001");
        map1.put("gameId", "SBXX");

        String format = ",,%s,%s";
        String val = String.format(format, map1.get("billCode"),
                map1.get("gameId"));

        String ret = _service.registerAuthDeniedGameBill(val);
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
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("登録フラグのフォーマットが不正です", line[3]);
			assertEquals("-4", line[4]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthDeniedGameBillInvalidParameterRegisterFlagIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("billCode", "2Z9901001");
        map1.put("gameId", "SBXX");
        map1.put("registerFlag", "A");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("billCode", "2Z9901001");
        map2.put("gameId", "SBXX");
        map2.put("registerFlag", "2");

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("billCode", "2Z9901001");
        map3.put("gameId", "SBXX");
        map3.put("registerFlag", "１");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("billCode"), map1.get("gameId"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("billCode"), map2.get("gameId"));
        val += "\n"
                + String.format(format, map3.get("registerFlag"),
                        map3.get("billCode"), map3.get("gameId"));

        String ret = _service.registerAuthDeniedGameBill(val);
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
			assertEquals(map1.get("billCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("登録フラグのフォーマットが不正です", line[3]);
			assertEquals("-4", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("billCode"), line[1]);
			assertEquals(map2.get("gameId"), line[2]);
			assertEquals("登録フラグのフォーマットが不正です", line[3]);
			assertEquals("-4", line[4]);
			// 4行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("billCode"), line[1]);
			assertEquals(map3.get("gameId"), line[2]);
			assertEquals("登録フラグのフォーマットが不正です", line[3]);
			assertEquals("-4", line[4]);
			// 5行目
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

    private void deleteGame(String gameId) {
        _em.createNativeQuery(
                "DELETE FROM game_attributes WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM title_api_accounts WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM country_download_orders WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM download_orders WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM auth_denied_game_bills WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM move_denied_game_bills WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM auth_denied_game_comps WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM move_denied_game_comps WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM move_denied_games WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM move_denied_gamevers WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM auth_allowed_comps WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM game_competences WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM bill_open_allowed_games WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery("DELETE FROM games WHERE game_id = :gameId")
                .setParameter("gameId", gameId).executeUpdate();
    }

    private void createBill(String billCode) {
        Bill b = new Bill();
        b.setBillCode(billCode);
        b.setCreateDate(new Date());
        b.setCreateUserId("TestUserCreate");
        _em.persist(b);
    }

    private AuthDeniedGameBill createAuthDeniedGameBill(String billCode,
            String gameId) {

        AuthDeniedGameBillPK pk = new AuthDeniedGameBillPK();
        pk.setBillCode(billCode);
        pk.setGameId(gameId);
        AuthDeniedGameBill c = new AuthDeniedGameBill();
        c.setPk(pk);
        c.setCreateDate(new Date());
        c.setCreateUserId("TestUserCreate");
        _em.persist(c);
        return c;
    }

    private void createGame(String gameId) {
        Game g = _em.find(Game.class, gameId);
        if (g != null) {
            return;
        }
        Date now = new Date();
        g = new Game();
        g.setGameId(gameId);
        g.setTitle("テストゲームタイトル");
        g.setCreateDate(now);
        g.setCreateUserId("TestUserCreate");
        g.setUpdateDate(now);
        g.setUpdateUserId("TestUserCreate");
        _em.persist(g);
    }

    private void deletePlace(int allnetId) {
        _em.createNativeQuery("DELETE FROM routers").executeUpdate();
        _em.createNativeQuery("DELETE FROM auth_allowed_places")
                .executeUpdate();
        _em.createNativeQuery("DELETE FROM places WHERE allnet_id = :allnetId")
                .setParameter("allnetId", allnetId).executeUpdate();
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

}
