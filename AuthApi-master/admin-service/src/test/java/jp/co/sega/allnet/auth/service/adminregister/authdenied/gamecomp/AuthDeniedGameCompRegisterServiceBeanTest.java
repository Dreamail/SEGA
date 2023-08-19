/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authdenied.gamecomp;

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

import jp.co.sega.allnet.auth.common.entity.AuthDeniedGameComp;
import jp.co.sega.allnet.auth.common.entity.AuthDeniedGameCompPK;
import jp.co.sega.allnet.auth.common.entity.Comp;
import jp.co.sega.allnet.auth.common.entity.Game;
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
public class AuthDeniedGameCompRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(AuthDeniedGameCompRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "authDeniedGameCompRegisterService")
    private AuthDeniedGameCompRegisterService _service;

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
    public final void testRegisterAuthDeniedGameComp() throws IOException {
        // 登録成功
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z9901");
        map1.put("gameId", "SBXX");
        map1.put("registerFlag", "1");

        // 削除成功
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("compCode", "2Z9902");
        map2.put("gameId", "SBXX");
        map2.put("registerFlag", "0");

        // 包括先がないので失敗
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("compCode", "2Z9903");
        map3.put("gameId", "SBXX");
        map3.put("registerFlag", "1");

        // 既に登録済みなので失敗
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("compCode", "2Z9904");
        map4.put("gameId", "SBXX");
        map4.put("registerFlag", "1");

        // 削除対象がないので失敗
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("compCode", "2Z9905");
        map5.put("gameId", "SBXX");
        map5.put("registerFlag", "0");

        // ゲームIDがないので失敗
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("compCode", "2Z9901");
        map6.put("gameId", "SBYY");
        map6.put("registerFlag", "1");

        // 包括先とゲームIDがないので失敗
        Map<String, String> map7 = new HashMap<String, String>();
        map7.put("compCode", "2Z9903");
        map7.put("gameId", "SBYY");
        map7.put("registerFlag", "1");

        // 登録成功（小文字が含まれていた）
        Map<String, String> map8 = new HashMap<String, String>();
        map8.put("compCode", "2z9908");
        map8.put("gameId", "sBxX");
        map8.put("registerFlag", "1");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("compCode"), map1.get("gameId"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("compCode"), map2.get("gameId"));
        val += "\n"
                + String.format(format, map3.get("registerFlag"),
                        map3.get("compCode"), map3.get("gameId"));
        val += "\n"
                + String.format(format, map4.get("registerFlag"),
                        map4.get("compCode"), map4.get("gameId"));
        val += "\n"
                + String.format(format, map5.get("registerFlag"),
                        map5.get("compCode"), map5.get("gameId"));
        val += "\n"
                + String.format(format, map6.get("registerFlag"),
                        map6.get("compCode"), map6.get("gameId"));
        val += "\n"
                + String.format(format, map7.get("registerFlag"),
                        map7.get("compCode"), map7.get("gameId"));
        val += "\n"
                + String.format(format, map8.get("registerFlag"),
                        map8.get("compCode"), map8.get("gameId"));

        deleteComp(map1.get("compCode"));
        deleteComp(map2.get("compCode"));
        deleteComp(map3.get("compCode"));
        deleteComp(map4.get("compCode"));
        deleteComp(map5.get("compCode"));
        deleteComp(map8.get("compCode").toUpperCase());

        deleteGame(map1.get("gameId"));
        deleteGame(map6.get("gameId"));

        createComp(map1.get("compCode"));
        createComp(map2.get("compCode"));
        createComp(map4.get("compCode"));
        createComp(map5.get("compCode"));
        createComp(map8.get("compCode").toUpperCase());

        createGame(map1.get("gameId"));

        createAuthDeniedGameComp(map2.get("compCode"), map2.get("gameId"));
        createAuthDeniedGameComp(map4.get("compCode"), map4.get("gameId"));

        _em.flush();

        // AuthDeniedGameCompRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerAuthDeniedGameComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("正常に処理が行われました", line[3]);
			assertEquals("1", line[4]);
			// 3行目（正常に削除）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("compCode"), line[1]);
			assertEquals(map2.get("gameId"), line[2]);
			assertEquals("正常に処理が行われました", line[3]);
			assertEquals("1", line[4]);
			// 4行目（包括先がないので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("compCode"), line[1]);
			assertEquals(map3.get("gameId"), line[2]);
			assertEquals("包括先が未登録です", line[3]);
			assertEquals("-6", line[4]);
			// 5行目（既に登録済みなので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("4", line[0]);
			assertEquals(map4.get("compCode"), line[1]);
			assertEquals(map4.get("gameId"), line[2]);
			assertEquals("この情報は登録済みです", line[3]);
			assertEquals("2", line[4]);
			// 6行目（削除対象がないので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("5", line[0]);
			assertEquals(map5.get("compCode"), line[1]);
			assertEquals(map5.get("gameId"), line[2]);
			assertEquals("削除対象がありません", line[3]);
			assertEquals("3", line[4]);
			// 7行目（ゲームIDがないので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("6", line[0]);
			assertEquals(map6.get("compCode"), line[1]);
			assertEquals(map6.get("gameId"), line[2]);
			assertEquals("ゲームIDが未登録です", line[3]);
			assertEquals("-2", line[4]);
			// 8行目（包括先・ゲームIDがないので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("7", line[0]);
			assertEquals(map7.get("compCode"), line[1]);
			assertEquals(map7.get("gameId"), line[2]);
			assertEquals("包括先が未登録です", line[3]);
			assertEquals("-6", line[4]);
			// 9行目（正常に追加・小文字が含まれていた）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("8", line[0]);
			assertEquals(map8.get("compCode").toUpperCase(), line[1]);
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

        // 自動認証不許可ゲーム・包括先情報のDB確認
        AuthDeniedGameCompPK pk = new AuthDeniedGameCompPK();
        pk.setCompCode(map1.get("compCode"));
        pk.setGameId(map1.get("gameId"));
        AuthDeniedGameComp c = _em.find(AuthDeniedGameComp.class, pk);

        assertEquals(map1.get("compCode"), c.getPk().getCompCode());
        assertEquals(map1.get("gameId"), c.getPk().getGameId());
        assertEquals(mockUserId, c.getCreateUserId());

        pk = new AuthDeniedGameCompPK();
        pk.setCompCode(map2.get("compCode"));
        pk.setGameId(map2.get("gameId"));
        c = _em.find(AuthDeniedGameComp.class, pk);

        assertNull(c);

        pk = new AuthDeniedGameCompPK();
        pk.setCompCode(map3.get("compCode"));
        pk.setGameId(map3.get("gameId"));
        c = _em.find(AuthDeniedGameComp.class, pk);

        assertNull(c);

        pk = new AuthDeniedGameCompPK();
        pk.setCompCode(map4.get("compCode"));
        pk.setGameId(map4.get("gameId"));
        c = _em.find(AuthDeniedGameComp.class, pk);

        assertEquals("TestUserCreate", c.getCreateUserId());

        pk = new AuthDeniedGameCompPK();
        pk.setCompCode(map5.get("compCode"));
        pk.setGameId(map5.get("gameId"));
        c = _em.find(AuthDeniedGameComp.class, pk);

        assertNull(c);

        pk = new AuthDeniedGameCompPK();
        pk.setCompCode(map6.get("compCode"));
        pk.setGameId(map6.get("gameId"));
        c = _em.find(AuthDeniedGameComp.class, pk);

        assertNull(c);

        pk = new AuthDeniedGameCompPK();
        pk.setCompCode(map7.get("compCode"));
        pk.setGameId(map7.get("gameId"));
        c = _em.find(AuthDeniedGameComp.class, pk);

        assertNull(c);

        pk = new AuthDeniedGameCompPK();
        pk.setCompCode(map8.get("compCode").toUpperCase());
        pk.setGameId(map8.get("gameId").toUpperCase());
        c = _em.find(AuthDeniedGameComp.class, pk);

        assertEquals(map8.get("compCode").toUpperCase(), c.getPk()
                .getCompCode());
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
    public void testRegisterAuthDeniedGameCompInvalidParameterCompCodeIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "");
        map1.put("gameId", "SBXX");
        map1.put("registerFlag", "1");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("compCode"), map1.get("gameId"));

        String ret = _service.registerAuthDeniedGameComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
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
    public void testRegisterAuthDeniedGameCompInvalidParameterCompCodeIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z99011");
        map1.put("gameId", "SBXX");
        map1.put("registerFlag", "1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("compCode", "2Z991");
        map2.put("gameId", "SBXX");
        map2.put("registerFlag", "0");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("compCode"), map1.get("gameId"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("compCode"), map2.get("gameId"));

        String ret = _service.registerAuthDeniedGameComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("パラメータのフォーマットが不正です", line[3]);
			assertEquals("-3", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("compCode"), line[1]);
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
    public void testRegisterAuthDeniedGameCompInvalidParameterCompCodeIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "エラー");
        map1.put("gameId", "SBXX");
        map1.put("registerFlag", "1");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("compCode"), map1.get("gameId"));

        String ret = _service.registerAuthDeniedGameComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
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
    public void testRegisterAuthDeniedGameCompInvalidParameterGameIdIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z9901");
        map1.put("gameId", "");
        map1.put("registerFlag", "1");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("compCode", "2Z9901");
        map2.put("registerFlag", "1");

        String format1 = "%s,,%s,%s";
        String format2 = "%s,,%s";
        String val = String.format(format1, map1.get("registerFlag"),
                map1.get("compCode"), map1.get("gameId"));
        val += "\n"
                + String.format(format2, map2.get("registerFlag"),
                        map2.get("compCode"));

        String ret = _service.registerAuthDeniedGameComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("パラメータのフォーマットが不正です", line[3]);
			assertEquals("-3", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("compCode"), line[1]);
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
    public void testRegisterAuthDeniedGameCompInvalidParameterGameIdIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z9901");
        map1.put("gameId", "SBXXZZ");
        map1.put("registerFlag", "1");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("compCode"), map1.get("gameId"));

        String ret = _service.registerAuthDeniedGameComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
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
    public void testRegisterAuthDeniedGameCompInvalidParameterGameIdIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z9901");
        map1.put("gameId", "エラー");
        map1.put("registerFlag", "1");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("compCode"), map1.get("gameId"));

        String ret = _service.registerAuthDeniedGameComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
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
    public void testRegisterAuthDeniedGameCompInvalidParameterRegisterFlagIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z9901");
        map1.put("gameId", "SBXX");

        String format = ",,%s,%s";
        String val = String.format(format, map1.get("compCode"),
                map1.get("gameId"));

        String ret = _service.registerAuthDeniedGameComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
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
    public void testRegisterAuthDeniedGameCompInvalidParameterRegisterFlagIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z9901");
        map1.put("gameId", "SBXX");
        map1.put("registerFlag", "A");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("compCode", "2Z9901");
        map2.put("gameId", "SBXX");
        map2.put("registerFlag", "2");

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("compCode", "2Z9901");
        map3.put("gameId", "SBXX");
        map3.put("registerFlag", "１");

        String format = "%s,,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("compCode"), map1.get("gameId"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("compCode"), map2.get("gameId"));
        val += "\n"
                + String.format(format, map3.get("registerFlag"),
                        map3.get("compCode"), map3.get("gameId"));

        String ret = _service.registerAuthDeniedGameComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("登録フラグのフォーマットが不正です", line[3]);
			assertEquals("-4", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("compCode"), line[1]);
			assertEquals(map2.get("gameId"), line[2]);
			assertEquals("登録フラグのフォーマットが不正です", line[3]);
			assertEquals("-4", line[4]);
			// 4行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("compCode"), line[1]);
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

    private void deleteComp(String compCode) {
        _em.createNativeQuery(
                "DELETE FROM move_denied_game_comps WHERE comp_code = :compCode")
                .setParameter("compCode", compCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM auth_denied_game_comps WHERE comp_code = :compCode")
                .setParameter("compCode", compCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM auth_allowed_comps WHERE comp_code = :compCode")
                .setParameter("compCode", compCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM auth_denied_comps WHERE comp_code = :compCode")
                .setParameter("compCode", compCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM move_denied_comps WHERE comp_code = :compCode")
                .setParameter("compCode", compCode).executeUpdate();
        _em.createNativeQuery("DELETE FROM comps WHERE comp_code = :compCode")
                .setParameter("compCode", compCode).executeUpdate();
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

    private void createComp(String compCode) {
        Comp c = _em.find(Comp.class, compCode);
        if (c != null) {
            return;
        }
        c = new Comp();
        c.setCompCode(compCode);
        c.setName("テスト包括先");
        c.setCreateDate(new Date());
        c.setCreateUserId("TestUserCreate");
        c.setUpdateDate(new Date());
        c.setUpdateUserId("TestUserCreate");
        _em.persist(c);
    }

    private AuthDeniedGameComp createAuthDeniedGameComp(String compCode,
            String gameId) {
        AuthDeniedGameCompPK pk = new AuthDeniedGameCompPK();
        pk.setCompCode(compCode);
        pk.setGameId(gameId);
        AuthDeniedGameComp c = _em.find(AuthDeniedGameComp.class, pk);
        if (c != null) {
            return c;
        }
        c = new AuthDeniedGameComp();
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

}
