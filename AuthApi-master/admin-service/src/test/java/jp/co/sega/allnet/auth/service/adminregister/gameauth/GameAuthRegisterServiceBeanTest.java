/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.gameauth;

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
public class GameAuthRegisterServiceBeanTest {

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "gameAuthRegisterService")
    private GameAuthRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @After
    public void after() {
        reset(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.gameauth.GameAuthRegisterServiceBean#registerGameAuthForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameAuthForAdmin() {
        // 1行目：更新
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "2.00");
        map1.put("auth", "2");
        map1.put("countryCode", "TWN");
        // 2行目：認証方式が0なので処理しない
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("gameId", "SBYY");
        map2.put("gameVer", "1.00");
        map2.put("auth", "0");
        map2.put("countryCode", "TWN");
        // 2行目：更新だが更新対象がない
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("gameId", "SBZZ");
        map3.put("gameVer", "1.00");
        map3.put("auth", "1");
        map3.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("auth"),
                        map2.get("countryCode"));
        val += "\n"
                + String.format(format, map3.get("gameId"),
                        map3.get("gameVer"), map3.get("auth"),
                        map3.get("countryCode"));

        deleteCountry("TWN");

        deleteGame(map1.get("gameId"));
        deleteGame(map2.get("gameId"));
        deleteGame(map3.get("gameId"));

        createCountry("TWN");

        createGame(map1.get("gameId"), "ゲームタイトル", new Date(), "TestUserCreate");

        String gaTitle1 = "ゲームタイトル";
        String gaUri1 = "uri";
        String gaHost1 = "host";
        int gaAuth1 = 99999;
        Date gaDate1 = new Date();
        String gaUserId1 = "TestUserCreate";
        createGameAttribute(map1.get("gameId"), map1.get("gameVer"),
                map1.get("countryCode"), gaTitle1, gaUri1, gaHost1, gaAuth1,
                gaDate1, gaUserId1);

        _em.flush();

        // GameAuthRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // 振る舞いを記憶
        EasyMock.replay(_authMock);

        RegisterServiceResult<GameAuthRegisterResult> result = _service
                .registerGameAuthForAdmin(val);
        List<GameAuthRegisterResult> list = result.getList();

        assertEquals(3, list.size());

        // 1行目
        GameAuthRegisterResult r = list.get(0);
        assertTrue(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        assertEquals(map1.get("gameId"), r.getGameAttr().getPk().getGameId());
        assertEquals(map1.get("gameVer"), r.getGameAttr().getPk().getGameVer());
        assertEquals(map1.get("countryCode"), r.getGameAttr().getPk()
                .getCountryCode());
        assertEquals(gaTitle1, r.getGameAttr().getTitle());
        assertEquals(gaUri1, r.getGameAttr().getUri());
        assertEquals(gaHost1, r.getGameAttr().getHost());
        assertEquals(map1.get("auth"), r.getGameAttr().getAuth().toString());

        GameAttributePK pk = new GameAttributePK();
        pk.setGameId(map1.get("gameId"));
        pk.setGameVer(map1.get("gameVer"));
        pk.setCountryCode(map1.get("countryCode"));
        GameAttribute g = _em.find(GameAttribute.class, pk);

        assertEquals(map1.get("gameId"), g.getPk().getGameId());
        assertEquals(map1.get("gameVer"), g.getPk().getGameVer());
        assertEquals(map1.get("countryCode"), g.getPk().getCountryCode());
        assertEquals(gaTitle1, g.getTitle());
        assertEquals(gaUri1, g.getUri());
        assertEquals(gaHost1, g.getHost());
        assertEquals(map1.get("auth"), g.getAuth().toString());
        assertEquals(gaDate1, g.getCreateDate());
        assertEquals(gaUserId1, g.getCreateUserId());
        assertNotSame(gaDate1, g.getUpdateDate());
        assertEquals(mockUserId, g.getUpdateUserId());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

        pk = new GameAttributePK();
        pk.setGameId(map2.get("gameId"));
        pk.setGameVer(map2.get("gameVer"));
        pk.setCountryCode(map2.get("countryCode"));
        assertNull(_em.find(GameAttribute.class, pk));

        // 3行目
        r = list.get(2);
        assertFalse(r.isSuccess());

        assertEquals(map3.get("gameId"), r.getParam().getGameId());
        assertEquals(map3.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map3.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map3.get("auth"), r.getParam().getAuth());

        pk = new GameAttributePK();
        pk.setGameId(map3.get("gameId"));
        pk.setGameVer(map3.get("gameVer"));
        pk.setCountryCode(map3.get("countryCode"));
        assertNull(_em.find(GameAttribute.class, pk));

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.gameauth.GameAuthRegisterServiceBean#registerGameAuthForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameAuthForAdminInvalidParametersGameIdIsEmpty() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "");
        map1.put("gameVer", "1.00");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("auth"), map1.get("countryCode"));

        RegisterServiceResult<GameAuthRegisterResult> result = _service
                .registerGameAuthForAdmin(val);
        List<GameAuthRegisterResult> list = result.getList();

        assertEquals(1, list.size());

        // 1行目
        GameAuthRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.gameauth.GameAuthRegisterServiceBean#registerGameAuthForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameAuthForAdminInvalidParametersGameIdIsInvalidLength() {
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

        String format = "%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameAuthRegisterResult> result = _service
                .registerGameAuthForAdmin(val);
        List<GameAuthRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameAuthRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.gameauth.GameAuthRegisterServiceBean#registerGameAuthForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameAuthForAdminInvalidParametersGameIdIsInvalidLetter() {
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

        String format = "%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameAuthRegisterResult> result = _service
                .registerGameAuthForAdmin(val);
        List<GameAuthRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameAuthRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.gameauth.GameAuthRegisterServiceBean#registerGameAuthForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameAuthForAdminInvalidParametersGameVerIsEmpty() {
        // 1行目
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("gameId", "SBXX");
        map1.put("gameVer", "");
        map1.put("title", "ゲームタイトル1");
        map1.put("uri", "uri1");
        map1.put("host", "host1");
        map1.put("auth", "1");
        map1.put("countryCode", "TWN");

        String format = "%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("auth"), map1.get("countryCode"));

        RegisterServiceResult<GameAuthRegisterResult> result = _service
                .registerGameAuthForAdmin(val);
        List<GameAuthRegisterResult> list = result.getList();

        assertEquals(1, list.size());

        // 1行目
        GameAuthRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.gameauth.GameAuthRegisterServiceBean#registerGameAuthForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameAuthForAdminInvalidParametersGameVerIsInvalidLength() {
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

        String format = "%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameAuthRegisterResult> result = _service
                .registerGameAuthForAdmin(val);
        List<GameAuthRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameAuthRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.gameauth.GameAuthRegisterServiceBean#registerGameAuthForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameAuthForAdminInvalidParametersGameVerIsInvalidLetter() {
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

        String format = "%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameAuthRegisterResult> result = _service
                .registerGameAuthForAdmin(val);
        List<GameAuthRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameAuthRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.gameauth.GameAuthRegisterServiceBean#registerGameAuthForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameAuthForAdminInvalidParametersAuthIsInvalidLength() {
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

        String format = "%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameAuthRegisterResult> result = _service
                .registerGameAuthForAdmin(val);
        List<GameAuthRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameAuthRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.gameauth.GameAuthRegisterServiceBean#registerGameAuthForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameAuthForAdminInvalidParametersAuthIsInvalidLetter() {
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

        String format = "%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameAuthRegisterResult> result = _service
                .registerGameAuthForAdmin(val);
        List<GameAuthRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameAuthRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.gameauth.GameAuthRegisterServiceBean#registerGameAuthForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameAuthForAdminInvalidParametersCountryCodeIsEmpty() {
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

        String format1 = "%s,%s,%s,%s";
        String format2 = "%s,%s,%s";
        String val = String.format(format1, map1.get("gameId"),
                map1.get("gameVer"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format2, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameAuthRegisterResult> result = _service
                .registerGameAuthForAdmin(val);
        List<GameAuthRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameAuthRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.gameauth.GameAuthRegisterServiceBean#registerGameAuthForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameAuthForAdminInvalidParametersCountryCodeIsInvalidLength() {
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

        String format = "%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameAuthRegisterResult> result = _service
                .registerGameAuthForAdmin(val);
        List<GameAuthRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameAuthRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map2.get("auth"), r.getParam().getAuth());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.gameauth.GameAuthRegisterServiceBean#registerGameAuthForAdmin(java.lang.String)}
     * .
     */
    @Test
    public final void testRegisterGameAuthForAdminInvalidParametersCountryCodeIsInvalidLetter() {
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

        String format = "%s,%s,%s,%s";
        String val = String.format(format, map1.get("gameId"),
                map1.get("gameVer"), map1.get("auth"), map1.get("countryCode"));
        val += "\n"
                + String.format(format, map2.get("gameId"),
                        map2.get("gameVer"), map2.get("auth"),
                        map2.get("countryCode"));

        RegisterServiceResult<GameAuthRegisterResult> result = _service
                .registerGameAuthForAdmin(val);
        List<GameAuthRegisterResult> list = result.getList();

        assertEquals(2, list.size());

        // 1行目
        GameAuthRegisterResult r = list.get(0);
        assertFalse(r.isSuccess());

        assertEquals(map1.get("gameId"), r.getParam().getGameId());
        assertEquals(map1.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map1.get("countryCode"), r.getParam().getCountryCode());
        assertEquals(map1.get("auth"), r.getParam().getAuth());

        // 2行目
        r = list.get(1);
        assertFalse(r.isSuccess());

        assertEquals(map2.get("gameId"), r.getParam().getGameId());
        assertEquals(map2.get("gameVer"), r.getParam().getGameVer());
        assertEquals(map2.get("countryCode"), r.getParam().getCountryCode());
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
        Country c = new Country();
        c.setCountryCode(countryCode);
        c.setCountryName("テスト国");
        c.setCreateDate(new Date());
        c.setCreateUserId("TestUserCreate");
        c.setUpdateDate(new Date());
        c.setUpdateUserId("TestUserCreate");
        _em.persist(c);
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
