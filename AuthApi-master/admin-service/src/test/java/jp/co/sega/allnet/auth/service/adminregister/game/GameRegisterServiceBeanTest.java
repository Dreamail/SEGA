/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.game;

import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.Country;
import jp.co.sega.allnet.auth.common.entity.Game;
import jp.co.sega.allnet.auth.common.entity.GameAttribute;
import jp.co.sega.allnet.auth.common.entity.GameAttributePK;
import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;
import jp.co.sega.allnet.auth.service.security.AuthenticationDelegate;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class GameRegisterServiceBeanTest {

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "gameRegisterService")
    private GameRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @After
    public void after() {
        reset(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGame(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGame() {
        assertEquals("", _service.registerGame(""));
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdmin() {
        // 1行目：追加
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "1.00");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");
        // 2行目：更新
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBYY");
        map2.put("gameVer", "2.00");
        map2.put("title", "ゲームタイトル2");
        map2.put("uri", "uri2");
        map2.put("host", "host2");
        map2.put("auth", "2");
        map2.put("countryCode", "TWN");
        // 3行目：削除
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("gameId", "SBZZ");
        map3.put("gameVer", "1.00");
        map3.put("title", "ゲームタイトル3");
        map3.put("uri", "uri3");
        map3.put("host", "host3");
        map3.put("auth", "0");
        map3.put("countryCode", "TWN");
        // 4行目：追加だが国コードがない
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("gameId", "SBAA");
        map4.put("gameVer", "1.00");
        map4.put("title", "ゲームタイトル4");
        map4.put("uri", "uri4");
        map4.put("host", "host4");
        map4.put("auth", "4");
        map4.put("countryCode", "CHN");
        // 5行目：削除だが削除対象がない
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("gameId", "SBBB");
        map5.put("gameVer", "1.00");
        map5.put("title", "ゲームタイトル5");
        map5.put("uri", "uri5");
        map5.put("host", "host5");
        map5.put("auth", "0");
        map5.put("countryCode", "TWN");
        // 6行目：追加（ゲーム情報は既に存在する）
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("gameId", "SBCC");
        map6.put("gameVer", "2.00");
        map6.put("title", "ゲームタイトル6");
        map6.put("uri", "uri6");
        map6.put("host", "host6");
        map6.put("auth", "");
        map6.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("title"),
                        map2.get("uri"), map2.get("host"), map2.get("auth"),
                        map2.get("countryCode"));
        val += "\n"
                + String.format(format, map3.get("gameId"),
                        map3.get("gameVer"), map3.get("title"),
                        map3.get("uri"), map3.get("host"), map3.get("auth"),
                        map3.get("countryCode"));
        val += "\n"
                + String.format(format, map4.get("gameId"),
                        map4.get("gameVer"), map4.get("title"),
                        map4.get("uri"), map4.get("host"), map4.get("auth"),
                        map4.get("countryCode"));
        val += "\n"
                + String.format(format, map5.get("gameId"),
                        map5.get("gameVer"), map5.get("title"),
                        map5.get("uri"), map5.get("host"), map5.get("auth"),
                        map5.get("countryCode"));
        val += "\n"
                + String.format(format, map6.get("gameId"),
                        map6.get("gameVer"), map6.get("title"),
                        map6.get("uri"), map6.get("host"), map6.get("auth"),
                        map6.get("countryCode"));

        deleteCountry("TWN");
        deleteCountry("CHN");

        deleteGame(map1.get("gameId"));
        deleteGame(map2.get("gameId"));
        deleteGame(map3.get("gameId"));
        deleteGame(map4.get("gameId"));
        deleteGame(map5.get("gameId"));
        deleteGame(map6.get("gameId"));

        createCountry("TWN");

        createGame(map2.get("gameId"), "ゲームタイトル", new Date(), "TestUserCreate");
        createGame(map3.get("gameId"), "ゲームタイトル", new Date(), "TestUserCreate");
        createGame(map5.get("gameId"), "ゲームタイトル", new Date(), "TestUserCreate");
        createGame(map6.get("gameId"), "ゲームタイトル", new Date(), "TestUserCreate");

        Date gaDate2 = new Date();
        String gaUserId2 = "TestUserCreate";
        createGameAttribute(map2.get("gameId"), map2.get("gameVer"),
                map2.get("countryCode"), "ゲームタイトル", "uri", "host", 99999,
                gaDate2, gaUserId2);

        String gaTitle3 = "ゲームタイトル";
        String gaUri3 = "uri";
        String gaHost3 = "host";
        int gaAuth3 = 99999;
        Date gaDate3 = new Date();
        String gaUserId3 = "TestUserCreate";
        createGameAttribute(map3.get("gameId"), map3.get("gameVer"),
                map3.get("countryCode"), gaTitle3, gaUri3, gaHost3, gaAuth3,
                gaDate3, gaUserId3);

        _em.flush();

        // GameRegisterServiceで使用するメソッドの振る舞いを設定
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
        // 振る舞いを記憶
        EasyMock.replay(_authMock);

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(6, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertTrue(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        assertEquals(map1.get("gameId"), r.getGameAttr().getPk().getGameId());
        assertEquals(map1.get("gameVer"), r.getGameAttr().getPk().getGameVer());
        assertEquals(map1.get("countryCode"), r.getGameAttr().getPk()
                .getCountryCode());
        assertEquals(map1.get("title"), r.getGameAttr().getTitle());
        assertEquals(map1.get("uri"), r.getGameAttr().getUri());
        assertEquals(map1.get("host"), r.getGameAttr().getHost());
        assertEquals(map1.get("auth"), r.getGameAttr().getAuth().toString());

        Game game = _em.find(Game.class, map1.get("gameId"));

        assertEquals(map1.get("gameId"), game.getGameId());
        assertEquals(map1.get("title"), game.getTitle());
        assertEquals(mockUserId, game.getCreateUserId());
        assertEquals(mockUserId, game.getUpdateUserId());

        GameAttributePK pk = new GameAttributePK();
        pk.setGameId(map1.get("gameId"));
        pk.setGameVer(map1.get("gameVer"));
        pk.setCountryCode(map1.get("countryCode"));
        GameAttribute g = _em.find(GameAttribute.class, pk);

        assertEquals(map1.get("gameId"), g.getPk().getGameId());
        assertEquals(map1.get("gameVer"), g.getPk().getGameVer());
        assertEquals(map1.get("countryCode"), g.getPk().getCountryCode());
        assertEquals(map1.get("title"), g.getTitle());
        assertEquals(map1.get("uri"), g.getUri());
        assertEquals(map1.get("host"), g.getHost());
        assertEquals(map1.get("auth"), g.getAuth().toString());
        assertEquals(mockUserId, g.getCreateUserId());
        assertEquals(mockUserId, g.getUpdateUserId());

        // 2行目
        r = list.get(1);
        assertTrue(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("title"), r.getParam().getTitle());
        assertEquals(map2.get("uri"), r.getParam().getUri());
        assertEquals(map2.get("host"), r.getParam().getHost());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

        assertEquals(map2.get("gameId"), r.getGameAttr().getPk().getGameId());
        assertEquals(map2.get("gameVer"), r.getGameAttr().getPk().getGameVer());
        assertEquals(map2.get("countryCode"), r.getGameAttr().getPk()
                .getCountryCode());
        assertEquals(map2.get("title"), r.getGameAttr().getTitle());
        assertEquals(map2.get("uri"), r.getGameAttr().getUri());
        assertEquals(map2.get("host"), r.getGameAttr().getHost());
        assertEquals(map2.get("auth"), r.getGameAttr().getAuth().toString());

        pk = new GameAttributePK();
        pk.setGameId(map2.get("gameId"));
        pk.setGameVer(map2.get("gameVer"));
        pk.setCountryCode(map2.get("countryCode"));
        g = _em.find(GameAttribute.class, pk);

        assertEquals(map2.get("gameId"), g.getPk().getGameId());
        assertEquals(map2.get("gameVer"), g.getPk().getGameVer());
        assertEquals(map2.get("countryCode"), g.getPk().getCountryCode());
        assertEquals(map2.get("title"), g.getTitle());
        assertEquals(map2.get("uri"), g.getUri());
        assertEquals(map2.get("host"), g.getHost());
        assertEquals(map2.get("auth"), g.getAuth().toString());
        assertEquals(gaDate2, g.getCreateDate());
        assertEquals(gaUserId2, g.getCreateUserId());
        assertNotSame(gaDate2, g.getUpdateDate());
        assertEquals(mockUserId, g.getUpdateUserId());

        // 3行目
        r = list.get(2);
        assertTrue(r.isSuccess());

        assertEquals(map3.get("gameId"), r.getParam().getGameId());
        assertEquals(map3.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map3.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map3.get("title"), r.getParam().getTitle());
        assertEquals(map3.get("uri"), r.getParam().getUri());
        assertEquals(map3.get("host"), r.getParam().getHost());
        assertEquals(map3.get("auth"), r.getParam().getAuth());

        assertEquals(map3.get("gameId"), r.getGameAttr().getPk().getGameId());
        assertEquals(map3.get("gameVer"), r.getGameAttr().getPk().getGameVer());
        assertEquals(map3.get("countryCode"), r.getGameAttr().getPk()
                .getCountryCode());
        assertEquals(gaTitle3, r.getGameAttr().getTitle());
        assertEquals(gaUri3, r.getGameAttr().getUri());
        assertEquals(gaHost3, r.getGameAttr().getHost());
        assertEquals(new BigDecimal(gaAuth3), r.getGameAttr().getAuth());

        pk = new GameAttributePK();
        pk.setGameId(map3.get("gameId"));
        pk.setGameVer(map3.get("gameVer"));
        pk.setCountryCode(map3.get("countryCode"));
        assertNull(_em.find(GameAttribute.class, pk));

        // 4行目
        r = list.get(3);
        assertFalse(r.isSuccess());

        assertEquals(map4.get("gameId"), r.getParam().getGameId());
        assertEquals(map4.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map4.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map4.get("title"), r.getParam().getTitle());
        assertEquals(map4.get("uri"), r.getParam().getUri());
        assertEquals(map4.get("host"), r.getParam().getHost());
        assertEquals(map4.get("auth"), r.getParam().getAuth());

        pk = new GameAttributePK();
        pk.setGameId(map4.get("gameId"));
        pk.setGameVer(map4.get("gameVer"));
        pk.setCountryCode(map4.get("countryCode"));
        assertNull(_em.find(GameAttribute.class, pk));

        // 5行目
        r = list.get(4);
        assertFalse(r.isSuccess());

        assertEquals(map5.get("gameId"), r.getParam().getGameId());
        assertEquals(map5.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map5.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map5.get("title"), r.getParam().getTitle());
        assertEquals(map5.get("uri"), r.getParam().getUri());
        assertEquals(map5.get("host"), r.getParam().getHost());
        assertEquals(map5.get("auth"), r.getParam().getAuth());

        pk = new GameAttributePK();
        pk.setGameId(map5.get("gameId"));
        pk.setGameVer(map5.get("gameVer"));
        pk.setCountryCode(map5.get("countryCode"));
        assertNull(_em.find(GameAttribute.class, pk));

        // 6行目
        r = list.get(5);
        assertTrue(r.isSuccess());

        assertEquals(map6.get("gameId"), r.getParam().getGameId());
        assertEquals(map6.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map6.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map6.get("title"), r.getParam().getTitle());
        assertEquals(map6.get("uri"), r.getParam().getUri());
        assertEquals(map6.get("host"), r.getParam().getHost());
        assertEquals("1", r.getParam().getAuth());

        assertEquals(map6.get("gameId"), r.getGameAttr().getPk().getGameId());
        assertEquals(map6.get("gameVer"), r.getGameAttr().getPk().getGameVer());
        assertEquals(map6.get("countryCode"), r.getGameAttr().getPk()
                .getCountryCode());
        assertEquals(map6.get("title"), r.getGameAttr().getTitle());
        assertEquals(map6.get("uri"), r.getGameAttr().getUri());
        assertEquals(map6.get("host"), r.getGameAttr().getHost());
        assertEquals("1", r.getGameAttr().getAuth().toString());

        pk = new GameAttributePK();
        pk.setGameId(map6.get("gameId"));
        pk.setGameVer(map6.get("gameVer"));
        pk.setCountryCode(map6.get("countryCode"));
        g = _em.find(GameAttribute.class, pk);

        assertEquals(map6.get("gameId"), g.getPk().getGameId());
        assertEquals(map6.get("gameVer"), g.getPk().getGameVer());
        assertEquals(map6.get("countryCode"), g.getPk().getCountryCode());
        assertEquals(map6.get("title"), g.getTitle());
        assertEquals(map6.get("uri"), g.getUri());
        assertEquals(map6.get("host"), g.getHost());
        assertEquals("1", g.getAuth().toString());
        assertEquals(mockUserId, g.getCreateUserId());
        assertEquals(mockUserId, g.getUpdateUserId());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersGameIdIsEmpty() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "");
        map1.put("gameVer", "1.00");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(1, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersGameIdIsInvalidLength() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SB");
        map1.put("gameVer", "1.00");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");
        // 2行目
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBYYAB");
        map2.put("gameVer", "2.00");
        map2.put("title", "ゲームタイトル2");
        map2.put("uri", "uri2");
        map2.put("host", "host2");
        map2.put("auth", "2");
        map2.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("title"),
                        map2.get("uri"), map2.get("host"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("title"), r.getParam().getTitle());
        assertEquals(map2.get("uri"), r.getParam().getUri());
        assertEquals(map2.get("host"), r.getParam().getHost());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersGameIdIsInvalidLetter() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "ゲームID");
        map1.put("gameVer", "1.00");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");
        // 2行目
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "+*ID");
        map2.put("gameVer", "2.00");
        map2.put("title", "ゲームタイトル2");
        map2.put("uri", "uri2");
        map2.put("host", "host2");
        map2.put("auth", "2");
        map2.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("title"),
                        map2.get("uri"), map2.get("host"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("title"), r.getParam().getTitle());
        assertEquals(map2.get("uri"), r.getParam().getUri());
        assertEquals(map2.get("host"), r.getParam().getHost());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersGameVerIsEmpty() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(1, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersGameVerIsInvalidLength() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "-1");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");
        // 2行目
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBYY");
        map2.put("gameVer", "65.541");
        map2.put("title", "ゲームタイトル2");
        map2.put("uri", "uri2");
        map2.put("host", "host2");
        map2.put("auth", "2");
        map2.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("title"),
                        map2.get("uri"), map2.get("host"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("title"), r.getParam().getTitle());
        assertEquals(map2.get("uri"), r.getParam().getUri());
        assertEquals(map2.get("host"), r.getParam().getHost());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersGameVerIsInvalidLetter() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "ゲームVer1");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");
        // 2行目
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBXX");
        map2.put("gameVer", "+*ID");
        map2.put("title", "ゲームタイトル2");
        map2.put("uri", "uri2");
        map2.put("host", "host2");
        map2.put("auth", "2");
        map2.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("title"),
                        map2.get("uri"), map2.get("host"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("title"), r.getParam().getTitle());
        assertEquals(map2.get("uri"), r.getParam().getUri());
        assertEquals(map2.get("host"), r.getParam().getHost());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersTitleIsEmpty() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "1.00");
        map1.put("title", "");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));

        createCountry(map1.get("countryCode"));
        deleteGame(map1.get("gameId"));

        _em.flush();

        // GameRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // 振る舞いを記憶
        EasyMock.replay(_authMock);

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(1, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertTrue(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersTitleIsInvalidLength() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "-1");
        map1.put("title", "あいうえおかきくけこさしすせそたちつてとなにぬねのあいうえおかきくけこさしすせそたちつてとなに1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(1, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersUriIsEmpty() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "1.00");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));

        createCountry(map1.get("countryCode"));
        deleteGame(map1.get("gameId"));

        _em.flush();

        // GameRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // 振る舞いを記憶
        EasyMock.replay(_authMock);

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(1, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertTrue(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersUriIsInvalidLength() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "-1");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "あいうえおかきくけこさしすせそたちつてとなにぬねのあいうえおかきくけこさしすせそたちつてとなに1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(1, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersHostIsEmpty() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "1.00");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));

        createCountry(map1.get("countryCode"));
        deleteGame(map1.get("gameId"));

        _em.flush();

        // GameRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // 振る舞いを記憶
        EasyMock.replay(_authMock);

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(1, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertTrue(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersHostIsInvalidLength() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "-1");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "あいうえおかきくけこさしすせそたちつてとなにぬねのあいうえおかきくけこさしすせそたちつてとなに1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(1, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersAuthIsInvalidLength() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "1.00");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "32768");
        map1.put("countryCode", "TWN");
        // 2行目
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBYY");
        map2.put("gameVer", "2.00");
        map2.put("title", "ゲームタイトル2");
        map2.put("uri", "uri2");
        map2.put("host", "host2");
        map2.put("auth", "32767.1");
        map2.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("title"),
                        map2.get("uri"), map2.get("host"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("title"), r.getParam().getTitle());
        assertEquals(map2.get("uri"), r.getParam().getUri());
        assertEquals(map2.get("host"), r.getParam().getHost());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersAuthIsInvalidLetter() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "1.00");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "認証方式1");
        map1.put("countryCode", "TWN");
        // 2行目
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBXX");
        map2.put("gameVer", "2.00");
        map2.put("title", "ゲームタイトル2");
        map2.put("uri", "uri2");
        map2.put("host", "host2");
        map2.put("auth", "+*ID");
        map2.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("title"),
                        map2.get("uri"), map2.get("host"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("title"), r.getParam().getTitle());
        assertEquals(map2.get("uri"), r.getParam().getUri());
        assertEquals(map2.get("host"), r.getParam().getHost());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersCountryCodeIsEmpty() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "1.00");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBXX");
        map2.put("gameVer", "1.00");
        map2.put("title", "ゲームタイトル1");
        map2.put("uri", "uri1");
        map2.put("host", "host1");
        map2.put("auth", "1");

        String format1 = "%s,%s,%s,%s,%s,%s,%s";
        String format2 = "%s,%s,%s,%s,%s,%s";
        String val = String.format(format1, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format2, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("title"),
                        map2.get("uri"), map2.get("host"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("title"), r.getParam().getTitle());
        assertEquals(map2.get("uri"), r.getParam().getUri());
        assertEquals(map2.get("host"), r.getParam().getHost());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersCountryCodeIsInvalidLength() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "1.00");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TW");
        // 2行目
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBYY");
        map2.put("gameVer", "2.00");
        map2.put("title", "ゲームタイトル2");
        map2.put("uri", "uri2");
        map2.put("host", "host2");
        map2.put("auth", "2");
        map2.put("countryCode", "TWNN");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("title"),
                        map2.get("uri"), map2.get("host"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("title"), r.getParam().getTitle());
        assertEquals(map2.get("uri"), r.getParam().getUri());
        assertEquals(map2.get("host"), r.getParam().getHost());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.game.GameRegisterServiceBean#registerGameForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameForAdminInvalidParametersCountryCodeIsInvalidLetter() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "1.00");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "台湾１");
        // 2行目
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBXX");
        map2.put("gameVer", "2.00");
        map2.put("title", "ゲームタイトル2");
        map2.put("uri", "uri2");
        map2.put("host", "host2");
        map2.put("auth", "2");
        map2.put("countryCode", "+*A");

        String format = "%s,%s,%s,%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("title"), map1.get("uri"),
                map1.get("host"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("title"),
                        map2.get("uri"), map2.get("host"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameRegisterResult> result = _service
                .registerGameForAdmin(val);
        List<GameRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("title"), r.getParam().getTitle());
        assertEquals(map1.get("uri"), r.getParam().getUri());
        assertEquals(map1.get("host"), r.getParam().getHost());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("title"), r.getParam().getTitle());
        assertEquals(map2.get("uri"), r.getParam().getUri());
        assertEquals(map2.get("host"), r.getParam().getHost());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

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
                "DELETE FROM auth_allowed_places WHERE game_id = :gameId")
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

    private void deleteCountry(String countryCode) {
        _em.createNativeQuery(
                "DELETE FROM routers WHERE allnet_id IN (SELECT allnet_id FROM places WHERE country_code = :countryCode)")
                .setParameter("countryCode", countryCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM auth_allowed_places WHERE allnet_id IN (SELECT allnet_id FROM places WHERE country_code = :countryCode)")
                .setParameter("countryCode", countryCode).executeUpdate();
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
        _em.createNativeQuery(
                "DELETE FROM game_attributes WHERE country_code = :countryCode")
                .setParameter("countryCode", countryCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM country_download_orders WHERE country_code = :countryCode")
                .setParameter("countryCode", countryCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM countries WHERE country_code = :countryCode")
                .setParameter("countryCode", countryCode).executeUpdate();
    }

    private void createCountry(String countryCode) {
		if (_em.find(Country.class, countryCode) == null) {
			Country c = new Country();
			c.setCountryCode(countryCode);
			c.setCountryName("テスト国");
			c.setCreateDate(new Date());
			c.setCreateUserId("TestUserCreate");
			c.setUpdateDate(new Date());
			c.setUpdateUserId("TestUserCreate");
			_em.persist(c);
		}
    }

    private void createGame(String gameId, String title, Date date,
            String userId) {
        Game g = new Game();
        g.setGameId(gameId);
        g.setTitle(title);
        g.setCreateDate(date);
        g.setCreateUserId(userId);
        g.setUpdateDate(date);
        g.setUpdateUserId(userId);
        _em.persist(g);
    }

    private void createGameAttribute(String gameId, String gameVer,
            String countryCode, String title, String uri, String host,
            int auth, Date date, String userId) {
        GameAttributePK pk = new GameAttributePK();
        pk.setGameId(gameId);
        pk.setGameVer(gameVer);
        pk.setCountryCode(countryCode);
        GameAttribute g = new GameAttribute();
        g.setPk(pk);
        g.setTitle(title);
        g.setUri(uri);
        g.setHost(host);
        g.setAuth(new BigDecimal(auth));
        g.setCreateDate(date);
        g.setCreateUserId(userId);
        g.setUpdateDate(date);
        g.setUpdateUserId(userId);
        _em.persist(g);
    }
}
