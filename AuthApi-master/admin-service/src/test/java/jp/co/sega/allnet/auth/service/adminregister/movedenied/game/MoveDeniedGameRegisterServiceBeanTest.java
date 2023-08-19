/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.game;

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

import jp.co.sega.allnet.auth.common.entity.Game;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedGame;
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
public class MoveDeniedGameRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(MoveDeniedGameRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "moveDeniedGameRegisterService")
    private MoveDeniedGameRegisterService _service;

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
    public final void testRegisterMoveDeniedGame() throws IOException {
        // 登録成功
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("registerFlag", "1");

        // 削除成功
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBYY");
        map2.put("registerFlag", "0");

        // ゲームIDがないので失敗
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("gameId", "SBZZ");
        map3.put("registerFlag", "1");

        // 既に登録済みなので失敗
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("gameId", "SBXA");
        map4.put("registerFlag", "1");

        // 削除対象がないので失敗
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("gameId", "SBXB");
        map5.put("registerFlag", "0");

        // 登録成功（小文字が含まれていた）
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("gameId", "sBxC");
        map6.put("registerFlag", "1");

        String format = "%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("gameId"));
        val += "\n"
                + String.format(format, map3.get("registerFlag"),
                        map3.get("gameId"));
        val += "\n"
                + String.format(format, map4.get("registerFlag"),
                        map4.get("gameId"));
        val += "\n"
                + String.format(format, map5.get("registerFlag"),
                        map5.get("gameId"));
        val += "\n"
                + String.format(format, map6.get("registerFlag"),
                        map6.get("gameId"));

        deleteGame(map1.get("gameId"));
        deleteGame(map2.get("gameId"));
        deleteGame(map3.get("gameId"));
        deleteGame(map4.get("gameId"));
        deleteGame(map5.get("gameId"));
        deleteGame(map6.get("gameId").toUpperCase());

        createGame(map1.get("gameId"));
        createGame(map2.get("gameId"));
        createGame(map4.get("gameId"));
        createGame(map5.get("gameId"));
        createGame(map6.get("gameId").toUpperCase());

        createMoveDeniedGame(map2.get("gameId"));
        createMoveDeniedGame(map4.get("gameId"));

        _em.flush();

        // MoveDeniedGameRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerMoveDeniedGame(val);
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
			assertEquals(map1.get("gameId"), line[1]);
			assertEquals("正常に処理が行われました", line[2]);
			assertEquals("1", line[3]);
			// 3行目（正常に削除）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("gameId"), line[1]);
			assertEquals("正常に処理が行われました", line[2]);
			assertEquals("1", line[3]);
			// 4行目（ゲームIDがないので失敗）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("gameId"), line[1]);
			assertEquals("ゲームIDが未登録です", line[2]);
			assertEquals("-2", line[3]);
			// 5行目（既に登録済みなので失敗）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("4", line[0]);
			assertEquals(map4.get("gameId"), line[1]);
			assertEquals("この情報は登録済みです", line[2]);
			assertEquals("2", line[3]);
			// 6行目（削除対象がないので失敗）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("5", line[0]);
			assertEquals(map5.get("gameId"), line[1]);
			assertEquals("削除対象がありません", line[2]);
			assertEquals("3", line[3]);
			// 7行目（正常に追加・小文字が含まれていた）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("6", line[0]);
			assertEquals(map6.get("gameId").toUpperCase(), line[1]);
			assertEquals("正常に処理が行われました", line[2]);
			assertEquals("1", line[3]);
			// 3行目（正常に削除）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("3", line[3]);

			assertNull(reader.readNext());
		}

        // 自動認証不許可ゲーム・請求先情報のDB確認
        MoveDeniedGame g = _em.find(MoveDeniedGame.class, map1.get("gameId"));

        assertEquals(map1.get("gameId"), g.getGameId());
        assertEquals(mockUserId, g.getCreateUserId());

        g = _em.find(MoveDeniedGame.class, map2.get("gameId"));

        assertNull(g);

        g = _em.find(MoveDeniedGame.class, map3.get("gameId"));

        assertNull(g);

        g = _em.find(MoveDeniedGame.class, map4.get("gameId"));

        assertEquals("TestUserCreate", g.getCreateUserId());

        g = _em.find(MoveDeniedGame.class, map5.get("gameId"));

        assertNull(g);

        g = _em.find(MoveDeniedGame.class, map6.get("gameId").toUpperCase());

        assertEquals(map6.get("gameId").toUpperCase(), g.getGameId());
        assertEquals(mockUserId, g.getCreateUserId());

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
    public void testRegisterMoveDeniedGameInvalidParameterGameIdIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "");
        map1.put("registerFlag", "1");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("registerFlag", "1");

        String format1 = "%s,%s";
        String format2 = "%s";
        String val = String.format(format1, map1.get("registerFlag"),
                map1.get("gameId"));
        val += "\n" + String.format(format2, map2.get("registerFlag"));

        String ret = _service.registerMoveDeniedGame(val);
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
			assertEquals(map1.get("gameId"), line[1]);
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
    public void testRegisterMoveDeniedGameInvalidParameterGameIdIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXXZZ");
        map1.put("registerFlag", "1");

        String format = "%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"));

        String ret = _service.registerMoveDeniedGame(val);
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
			assertEquals(map1.get("gameId"), line[1]);
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
    public void testRegisterMoveDeniedGameInvalidParameterGameIdIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "エラー");
        map1.put("registerFlag", "1");

        String format = "%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"));

        String ret = _service.registerMoveDeniedGame(val);
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
			assertEquals(map1.get("gameId"), line[1]);
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
    public void testRegisterMoveDeniedGameInvalidParameterRegisterFlagIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");

        String format = "%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"));

        String ret = _service.registerMoveDeniedGame(val);
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
			assertEquals(map1.get("gameId"), line[1]);
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
    public void testRegisterMoveDeniedGameInvalidParameterRegisterFlagIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("registerFlag", "A");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBXX");
        map2.put("registerFlag", "2");

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("gameId", "SBXX");
        map3.put("registerFlag", "１");

        String format = "%s,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("gameId"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("gameId"));
        val += "\n"
                + String.format(format, map3.get("registerFlag"),
                        map3.get("gameId"));

        String ret = _service.registerMoveDeniedGame(val);
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
			assertEquals(map1.get("gameId"), line[1]);
			assertEquals("登録フラグのフォーマットが不正です", line[2]);
			assertEquals("-4", line[3]);
			// 3行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("gameId"), line[1]);
			assertEquals("登録フラグのフォーマットが不正です", line[2]);
			assertEquals("-4", line[3]);
			// 4行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("gameId"), line[1]);
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

    private MoveDeniedGame createMoveDeniedGame(String gameId) {

        MoveDeniedGame g = new MoveDeniedGame();
        g.setGameId(gameId);
        g.setCreateDate(new Date());
        g.setCreateUserId("TestUserCreate");
        _em.persist(g);
        return g;
    }

    private void createGame(String gameId) {
        Date now = new Date();
        Game g = _em.find(Game.class, gameId);
        if (g == null) {
            g = new Game();
            g.setGameId(gameId);
            g.setTitle("テストゲームタイトル");
            g.setCreateDate(now);
            g.setCreateUserId("TestUserCreate");
            g.setUpdateDate(now);
            g.setUpdateUserId("TestUserCreate");
            _em.persist(g);
        } else {
            g.setTitle("テストゲームタイトル");
            g.setCreateUserId("TestUserCreate");
            g.setUpdateUserId("TestUserCreate");
            _em.merge(g);
        }
    }

}
