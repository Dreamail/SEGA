/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever;

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
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.Country;
import jp.co.sega.allnet.auth.common.entity.Game;
import jp.co.sega.allnet.auth.common.entity.GameAttribute;
import jp.co.sega.allnet.auth.common.entity.GameAttributePK;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedGamever;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedGameverPK;
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
public class MoveDeniedGameVerRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(MoveDeniedGameVerRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "moveDeniedGameVerRegisterService")
    private MoveDeniedGameVerRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @After
    public void after() {
        reset(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.comp.MoveDeniedGameVerRegisterServiceBean#registerAuthDeniedComp(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterMoveDeniedGameVer() throws IOException {
        // 登録成功
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "1.00");
        map1.put("registerFlag", "1");

        // 削除成功
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBYY");
        map2.put("gameVer", "1.00");
        map2.put("registerFlag", "0");

        // ゲームIDがないので失敗
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("gameId", "SBZZ");
        map3.put("gameVer", "1.00");
        map3.put("registerFlag", "1");

        // ゲームバージョンがないので失敗
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("gameId", "SBXX");
        map4.put("gameVer", "0.00");
        map4.put("registerFlag", "1");

        // ゲームID,ゲームバージョンがないので失敗
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("gameId", "SBZZ");
        map5.put("gameVer", "0.00");
        map5.put("registerFlag", "1");

        // 既に登録済みなので失敗
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("gameId", "SBXA");
        map6.put("gameVer", "1.00");
        map6.put("registerFlag", "1");

        // 削除対象がないので失敗
        Map<String, String> map7 = new HashMap<String, String>();
        map7.put("gameId", "SBXB");
        map7.put("gameVer", "1.00");
        map7.put("registerFlag", "0");

        // 登録成功
        Map<String, String> map8 = new HashMap<String, String>();
        map8.put("gameId", "sBxC");
        map8.put("gameVer", "1.00");
        map8.put("registerFlag", "1");

        String format = "%s,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"), map1.get("gameVer"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("gameId"), map2.get("gameVer"));
        val += "\n"
                + String.format(format, map3.get("registerFlag"),
                        map3.get("gameId"), map3.get("gameVer"));
        val += "\n"
                + String.format(format, map4.get("registerFlag"),
                        map4.get("gameId"), map4.get("gameVer"));
        val += "\n"
                + String.format(format, map5.get("registerFlag"),
                        map5.get("gameId"), map5.get("gameVer"));
        val += "\n"
                + String.format(format, map6.get("registerFlag"),
                        map6.get("gameId"), map6.get("gameVer"));
        val += "\n"
                + String.format(format, map7.get("registerFlag"),
                        map7.get("gameId"), map7.get("gameVer"));
        val += "\n"
                + String.format(format, map8.get("registerFlag"),
                        map8.get("gameId"), map8.get("gameVer"));

        String countryCode = "TWN";

        deleteGame(map1.get("gameId"));
        deleteGame(map2.get("gameId"));
        deleteGame(map3.get("gameId"));
        deleteGame(map6.get("gameId"));
        deleteGame(map7.get("gameId"));
        deleteGame(map8.get("gameId").toUpperCase());

        createGame(map1.get("gameId"), map1.get("gameVer"), countryCode);
        createGame(map2.get("gameId"), map2.get("gameVer"), countryCode);
        createGame(map6.get("gameId"), map6.get("gameVer"), countryCode);
        createGame(map7.get("gameId"), map7.get("gameVer"), countryCode);
        createGame(map8.get("gameId").toUpperCase(), map8.get("gameVer"),
                countryCode);

        createMoveDeniedGameVer(map2.get("gameId"), map2.get("gameVer"));
        createMoveDeniedGameVer(map6.get("gameId"), map6.get("gameVer"));

        _em.flush();

        // MoveDeniedGameVerRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerMoveDeniedGameVer(val);
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
			assertEquals(map1.get("gameId"), line[1]);
			assertEquals(map1.get("gameVer"), line[2]);
			assertEquals("正常に処理が行われました", line[3]);
			assertEquals("1", line[4]);
			// 3行目（正常に削除）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("gameId"), line[1]);
			assertEquals(map2.get("gameVer"), line[2]);
			assertEquals("正常に処理が行われました", line[3]);
			assertEquals("1", line[4]);
			// 4行目（ゲームIDがないので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("gameId"), line[1]);
			assertEquals(map3.get("gameVer"), line[2]);
			assertEquals("ゲームIDが未登録です", line[3]);
			assertEquals("-2", line[4]);
			// 5行目（ゲームバージョンがないので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("4", line[0]);
			assertEquals(map4.get("gameId"), line[1]);
			assertEquals(map4.get("gameVer"), line[2]);
			assertEquals("ゲームIDが未登録です", line[3]);
			assertEquals("-2", line[4]);
			// 6行目（ゲームIDとゲームバージョンがないので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("5", line[0]);
			assertEquals(map5.get("gameId"), line[1]);
			assertEquals(map5.get("gameVer"), line[2]);
			assertEquals("ゲームIDが未登録です", line[3]);
			assertEquals("-2", line[4]);
			// 7行目（既に登録済みなので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("6", line[0]);
			assertEquals(map6.get("gameId"), line[1]);
			assertEquals(map6.get("gameVer"), line[2]);
			assertEquals("この情報は登録済みです", line[3]);
			assertEquals("2", line[4]);
			// 8行目（削除対象がないので失敗）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("7", line[0]);
			assertEquals(map7.get("gameId"), line[1]);
			assertEquals(map7.get("gameVer"), line[2]);
			assertEquals("削除対象がありません", line[3]);
			assertEquals("3", line[4]);
			// 9行目（正常に追加）
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("8", line[0]);
			assertEquals(map8.get("gameId").toUpperCase(), line[1]);
			assertEquals(map8.get("gameVer"), line[2]);
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

        // 同一包括先内自動移設不許可ゲームバージョン情報のDB確認
        MoveDeniedGameverPK pk = new MoveDeniedGameverPK();
        pk.setGameId(map1.get("gameId"));
        pk.setGameVer(map1.get("gameVer"));
        MoveDeniedGamever g = _em.find(MoveDeniedGamever.class, pk);

        assertEquals(map1.get("gameId"), g.getPk().getGameId());
        assertEquals(map1.get("gameVer"), g.getPk().getGameVer());
        assertEquals(mockUserId, g.getCreateUserId());

        pk = new MoveDeniedGameverPK();
        pk.setGameId(map2.get("gameId"));
        pk.setGameVer(map2.get("gameVer"));
        g = _em.find(MoveDeniedGamever.class, pk);

        assertNull(g);

        pk = new MoveDeniedGameverPK();
        pk.setGameId(map3.get("gameId"));
        pk.setGameVer(map3.get("gameVer"));
        g = _em.find(MoveDeniedGamever.class, pk);

        assertNull(g);

        pk = new MoveDeniedGameverPK();
        pk.setGameId(map4.get("gameId"));
        pk.setGameVer(map4.get("gameVer"));
        g = _em.find(MoveDeniedGamever.class, pk);

        assertNull(g);

        pk = new MoveDeniedGameverPK();
        pk.setGameId(map5.get("gameId"));
        pk.setGameVer(map5.get("gameVer"));
        g = _em.find(MoveDeniedGamever.class, pk);

        assertNull(g);

        pk = new MoveDeniedGameverPK();
        pk.setGameId(map6.get("gameId"));
        pk.setGameVer(map6.get("gameVer"));
        g = _em.find(MoveDeniedGamever.class, pk);

        assertEquals("TestUserCreate", g.getCreateUserId());

        pk = new MoveDeniedGameverPK();
        pk.setGameId(map7.get("gameId"));
        pk.setGameVer(map7.get("gameVer"));
        g = _em.find(MoveDeniedGamever.class, pk);

        assertNull(g);

        pk = new MoveDeniedGameverPK();
        pk.setGameId(map8.get("gameId").toUpperCase());
        pk.setGameVer(map8.get("gameVer"));
        g = _em.find(MoveDeniedGamever.class, pk);

        assertEquals(map8.get("gameId").toUpperCase(), g.getPk().getGameId());
        assertEquals(map8.get("gameVer"), g.getPk().getGameVer());
        assertEquals(mockUserId, g.getCreateUserId());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever.MoveDeniedGameVerRegisterServiceBean#registerMoveDeniedGameVer(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedGameVerInvalidParameterGameIdIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "");
        map1.put("gameVer", "1.00");
        map1.put("registerFlag", "1");

        String format = "%s,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"), map1.get("gameVer"));

        String ret = _service.registerMoveDeniedGameVer(val);
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
			assertEquals(map1.get("gameId"), line[1]);
			assertEquals(map1.get("gameVer"), line[2]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever.MoveDeniedGameVerRegisterServiceBean#registerMoveDeniedGameVer(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedGameVerInvalidParameterGameIdIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXXZZ");
        map1.put("gameVer", "1.00");
        map1.put("registerFlag", "1");

        String format = "%s,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"), map1.get("gameVer"));

        String ret = _service.registerMoveDeniedGameVer(val);
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
			assertEquals(map1.get("gameId"), line[1]);
			assertEquals(map1.get("gameVer"), line[2]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever.MoveDeniedGameVerRegisterServiceBean#registerMoveDeniedGameVer(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedGameVerInvalidParameterGameIdIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "エラー");
        map1.put("gameVer", "1.00");
        map1.put("registerFlag", "1");

        String format = "%s,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"), map1.get("gameVer"));

        String ret = _service.registerMoveDeniedGameVer(val);
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
			assertEquals(map1.get("gameId"), line[1]);
			assertEquals(map1.get("gameVer"), line[2]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever.MoveDeniedGameVerRegisterServiceBean#registerMoveDeniedGameVer(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedGameVerInvalidParameterGameVerIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "");
        map1.put("registerFlag", "1");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBXX");
        map2.put("registerFlag", "1");

        String format1 = "%s,%s,%s";
        String format2 = "%s,%s";
        String val = String.format(format1, map1.get("registerFlag"),
                map1.get("gameId"), map1.get("gameVer"));
        val += "\n"
                + String.format(format2, map2.get("registerFlag"),
                        map2.get("gameId"));

        String ret = _service.registerMoveDeniedGameVer(val);
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
			assertEquals(map1.get("gameId"), line[1]);
			assertEquals(map1.get("gameVer"), line[2]);
			assertEquals("パラメータのフォーマットが不正です", line[3]);
			assertEquals("-3", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("gameId"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever.MoveDeniedGameVerRegisterServiceBean#registerMoveDeniedGameVer(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedGameVerInvalidParameterGameVerIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "11.00.");
        map1.put("registerFlag", "1");

        String format = "%s,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"), map1.get("gameVer"));

        String ret = _service.registerMoveDeniedGameVer(val);
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
			assertEquals(map1.get("gameId"), line[1]);
			assertEquals(map1.get("gameVer"), line[2]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever.MoveDeniedGameVerRegisterServiceBean#registerMoveDeniedGameVer(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedGameVerInvalidParameterGameVerIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "error");
        map1.put("registerFlag", "1");

        String format = "%s,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"), map1.get("gameVer"));

        String ret = _service.registerMoveDeniedGameVer(val);
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
			assertEquals(map1.get("gameId"), line[1]);
			assertEquals(map1.get("gameVer"), line[2]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever.MoveDeniedGameVerRegisterServiceBean#registerMoveDeniedGameVer(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedGameVerInvalidParameterRegisterFlagIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "1.00");

        String format = "%s,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"), map1.get("gameVer"));

        String ret = _service.registerMoveDeniedGameVer(val);
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
			assertEquals(map1.get("gameId"), line[1]);
			assertEquals(map1.get("gameVer"), line[2]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever.MoveDeniedGameVerRegisterServiceBean#registerMoveDeniedGameVer(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedGameVerInvalidParameterRegisterFlagIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "1.00");
        map1.put("registerFlag", "A");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBXX");
        map2.put("gameVer", "1.00");
        map2.put("registerFlag", "2");

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("gameId", "SBXX");
        map3.put("gameVer", "1.00");
        map3.put("registerFlag", "１");

        String format = "%s,%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"), map1.get("gameVer"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("gameId"), map2.get("gameVer"));
        val += "\n"
                + String.format(format, map3.get("registerFlag"),
                        map3.get("gameId"), map3.get("gameVer"));

        String ret = _service.registerMoveDeniedGameVer(val);
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
			assertEquals(map1.get("gameId"), line[1]);
			assertEquals(map1.get("gameVer"), line[2]);
			assertEquals("登録フラグのフォーマットが不正です", line[3]);
			assertEquals("-4", line[4]);
			// 3行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("gameId"), line[1]);
			assertEquals(map2.get("gameVer"), line[2]);
			assertEquals("登録フラグのフォーマットが不正です", line[3]);
			assertEquals("-4", line[4]);
			// 4行目
			line = reader.readNext();
			assertEquals(5, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("gameId"), line[1]);
			assertEquals(map3.get("gameVer"), line[2]);
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

    private MoveDeniedGamever createMoveDeniedGameVer(String gameId,
            String gameVer) {
        MoveDeniedGameverPK pk = new MoveDeniedGameverPK();
        pk.setGameId(gameId);
        pk.setGameVer(gameVer);
        MoveDeniedGamever g = new MoveDeniedGamever();
        g.setPk(pk);
        g.setCreateDate(new Date());
        g.setCreateUserId("TestUserCreate");
        _em.persist(g);
        return g;
    }

    private void createGame(String gameId, String gameVer, String countryCode) {
        Game g = _em.find(Game.class, gameId);
        if (g == null) {
            g = new Game();
            g.setGameId(gameId);
            g.setTitle("テストゲームタイトル");
            g.setCreateDate(new Date());
            g.setCreateUserId("TestUserCreate");
            g.setUpdateDate(new Date());
            g.setUpdateUserId("TestUserCreate");
            _em.persist(g);
        }

        Country c = _em.find(Country.class, countryCode);
        if (c == null) {
            c = new Country();
            c.setCountryCode(countryCode);
            c.setCreateDate(new Date());
            c.setCreateUserId("TestUserCreate");
            c.setUpdateDate(new Date());
            c.setUpdateUserId("TestUserCreate");
            _em.persist(c);
        }

        GameAttributePK pk = new GameAttributePK();
        pk.setGameId(gameId);
        pk.setGameVer(gameVer);
        pk.setCountryCode(countryCode);
        GameAttribute ga = _em.find(GameAttribute.class, pk);
        if (ga == null) {
            ga = new GameAttribute();
            ga.setPk(pk);
            ga.setAuth(new BigDecimal(1));
            ga.setCreateDate(new Date());
            ga.setCreateUserId("TestUserCreate");
            ga.setUpdateDate(new Date());
            ga.setUpdateUserId("TestUserCreate");
            _em.persist(ga);
        }
    }

}
