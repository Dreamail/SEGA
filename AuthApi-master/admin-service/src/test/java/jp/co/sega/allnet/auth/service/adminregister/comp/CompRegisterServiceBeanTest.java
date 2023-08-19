/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.comp;

import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.AuthAllowedComp;
import jp.co.sega.allnet.auth.common.entity.AuthAllowedCompPK;
import jp.co.sega.allnet.auth.common.entity.AuthDeniedComp;
import jp.co.sega.allnet.auth.common.entity.AuthDeniedGameComp;
import jp.co.sega.allnet.auth.common.entity.AuthDeniedGameCompPK;
import jp.co.sega.allnet.auth.common.entity.Bill;
import jp.co.sega.allnet.auth.common.entity.Comp;
import jp.co.sega.allnet.auth.common.entity.Game;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedComp;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedGameComp;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedGameCompPK;
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
public class CompRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(CompRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "compRegisterService")
    private CompRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @After
    public void after() {
        reset(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.comp.CompRegisterServiceBean#registerComp(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterComp() throws IOException {
        // 登録成功
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z9901");
        map1.put("compName", "包括先名2Z9901");
        map1.put("deleteFlag", "0");
        // 更新成功
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("compCode", "2Z9902");
        map2.put("compName", "包括先名2Z9902");
        map2.put("deleteFlag", "0");
        // 削除成功
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("compCode", "2Z9903");
        map3.put("compName", "包括先名2Z9903");
        map3.put("deleteFlag", "1");
        // 削除時に包括先が存在しないのでスキップ
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("compCode", "2Z9904");
        map4.put("compName", "包括先名2Z9904");
        map4.put("deleteFlag", "1");
        // 更新時に包括名が同じデータが存在するのでスキップ
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("compCode", "2Z9905");
        map5.put("compName", "包括先名2Z9905");
        map5.put("deleteFlag", "0");
        // 登録成功（小文字、削除処理フラグがない）
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("compCode", "2z9906");
        map6.put("compName", "包括先名2z9906");
        // 更新成功（小文字、削除処理フラグが0ではない）
        Map<String, String> map7 = new HashMap<String, String>();
        map7.put("compCode", "2z9907");
        map7.put("compName", "包括先名2Z9907");
        map7.put("deleteFlag", "99999");

        String format = "%s,%s,%s";
        String val = String.format(format, map1.get("compCode"),
                map1.get("compName"), map1.get("deleteFlag"));
        val += "\n"
                + String.format(format, map2.get("compCode"),
                        map2.get("compName"), map2.get("deleteFlag"));
        val += "\n"
                + String.format(format, map3.get("compCode"),
                        map3.get("compName"), map3.get("deleteFlag"));
        val += "\n"
                + String.format(format, map4.get("compCode"),
                        map4.get("compName"), map4.get("deleteFlag"));
        val += "\n"
                + String.format(format, map5.get("compCode"),
                        map5.get("compName"), map5.get("deleteFlag"));
        val += "\n"
                + String.format("%s,%s", map6.get("compCode"),
                        map6.get("compName"));
        val += "\n"
                + String.format(format, map7.get("compCode"),
                        map7.get("compName"), map7.get("deleteFlag"));

        String oldCompName2 = "oldCompName2";
        String oldCompName7 = "oldCompName7";
        Date oldDate2 = new Date();
        Date oldDate5 = new Date();
        Date oldDate7 = new Date();
        String oldUserId2 = "TestUserCreate";
        String oldUserId5 = "TestUserCreate";
        String oldUserId7 = "TestUserCreate";

        delete(map1.get("compCode"));
        delete(map2.get("compCode"));
        delete(map3.get("compCode"));
        delete(map4.get("compCode"));
        delete(map5.get("compCode"));
        delete(map6.get("compCode").toUpperCase());
        delete(map7.get("compCode").toUpperCase());

        createComp(map2.get("compCode"), oldCompName2, oldDate2, oldUserId2);
        createComp(map3.get("compCode"), map3.get("compName"), new Date(),
                "TestUserCreate");
        createComp(map5.get("compCode"), map5.get("compName"), oldDate5,
                oldUserId5);
        createComp(map7.get("compCode").toUpperCase(), oldCompName7, oldDate7,
                oldUserId7);

        // map3削除テスト実行のための依存先データ作成
        String gameId = "SBXX";
        String billCode = "2Z9903001";
        mergeGame(gameId);
        mergeBill(billCode, map3.get("compCode"));
        createAuthAllowedComp(gameId, map3.get("compCode"));
        createAuthDeniedComp(map3.get("compCode"));
        createAuthDeniedGameComp(gameId, map3.get("compCode"));
        createMoveDeniedComp(map3.get("compCode"));
        createMoveDeniedGameComp(gameId, map3.get("compCode"));

        // CompRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        _em.flush();

        String ret = _service.registerComp(val);
        _log.info(ret);
        _em.flush();

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目：登録成功
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals("1", line[2]);
			// 3行目：更新成功
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("compCode"), line[1]);
			assertEquals("2", line[2]);
			// 4行目：削除成功
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("compCode"), line[1]);
			assertEquals("3", line[2]);
			// 5行目：削除時に包括先が存在しないでスキップ
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("4", line[0]);
			assertEquals(map4.get("compCode"), line[1]);
			assertEquals("0", line[2]);
			// 6行目：削除時に包括先が存在しないのでスキップ
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("5", line[0]);
			assertEquals(map5.get("compCode"), line[1]);
			assertEquals("0", line[2]);
			// 7行目：登録成功（小文字、削除処理フラグがない）
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("6", line[0]);
			assertEquals(map6.get("compCode").toUpperCase(), line[1]);
			assertEquals("1", line[2]);
			// 8行目：更新成功（小文字、削除処理フラグが0ではない）
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("7", line[0]);
			assertEquals(map7.get("compCode").toUpperCase(), line[1]);
			assertEquals("2", line[2]);
			// 9行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("5", line[2]);

			assertNull(reader.readNext());
		}

        // 包括先情報のDB確認
        Comp c = _em.find(Comp.class, map1.get("compCode"));
        assertEquals(map1.get("compCode"), c.getCompCode());
        assertEquals(map1.get("compName"), c.getName());
        assertEquals(mockUserId, c.getCreateUserId());
        assertEquals(mockUserId, c.getUpdateUserId());

        c = _em.find(Comp.class, map2.get("compCode"));
        assertEquals(map2.get("compCode"), c.getCompCode());
        assertEquals(map2.get("compName"), c.getName());
        assertEquals(oldDate2, c.getCreateDate());
        assertEquals(oldUserId2, c.getCreateUserId());
        assertNotSame(oldDate2, c.getUpdateDate());
        assertEquals(mockUserId, c.getUpdateUserId());

        c = _em.find(Comp.class, map3.get("compCode"));
        assertNull(c);

        c = _em.find(Comp.class, map4.get("compCode"));
        assertNull(c);

        c = _em.find(Comp.class, map5.get("compCode"));
        assertEquals(map5.get("compCode"), c.getCompCode());
        assertEquals(map5.get("compName"), c.getName());
        assertEquals(oldDate5, c.getCreateDate());
        assertEquals(oldUserId5, c.getCreateUserId());
        assertEquals(oldDate5, c.getUpdateDate());
        assertEquals(oldUserId5, c.getUpdateUserId());

        c = _em.find(Comp.class, map6.get("compCode").toUpperCase());
        assertEquals(map6.get("compCode").toUpperCase(), c.getCompCode());
        assertEquals(map6.get("compName"), c.getName());
        assertEquals(mockUserId, c.getCreateUserId());
        assertEquals(mockUserId, c.getUpdateUserId());

        c = _em.find(Comp.class, map7.get("compCode").toUpperCase());
        assertEquals(map7.get("compCode").toUpperCase(), c.getCompCode());
        assertEquals(map7.get("compName"), c.getName());
        assertEquals(oldDate7, c.getCreateDate());
        assertEquals(oldUserId7, c.getCreateUserId());
        assertNotSame(oldDate7, c.getUpdateDate());
        assertEquals(mockUserId, c.getUpdateUserId());

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
    public void testRegisterCompInvalidParameterCompCodeIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "");
        map1.put("compName", "包括先名");

        String format = "%s,%s";
        String val = String.format(format, map1.get("compCode"),
                map1.get("compName"));

        String ret = _service.registerComp(val);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals("-1", line[2]);
			// 3行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("0", line[2]);

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
    public void testRegisterCompInvalidParameterCompCodeIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z99011");
        map1.put("compName", "包括先名1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("compCode", "2Z991");
        map1.put("compName", "包括先名2");

        String format = "%s,%s";
        String val = String.format(format, map1.get("compCode"),
                map1.get("compName"));
        val += "\n"
                + String.format(format, map2.get("compCode"),
                        map2.get("compName"));

        String ret = _service.registerComp(val);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals("-1", line[2]);
			// 3行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("compCode"), line[1]);
			assertEquals("-1", line[2]);
			// 4行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("0", line[2]);

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
    public void testRegisterCompInvalidParameterCompCodeIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "エラー");
        map1.put("compName", "包括先名");

        String format = "%s,%s";
        String val = String.format(format, map1.get("compCode"),
                map1.get("compName"));

        String ret = _service.registerComp(val);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals("-1", line[2]);
			// 3行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("0", line[2]);

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
    public void testRegisterCompInvalidParameterCompNameIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z9901");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("compCode", "2Z9902");
        map2.put("compName", "");

        String val = map1.get("compCode");
        val += "\n"
                + String.format("%s,%s", map2.get("compCode"),
                        map2.get("compName"));

        String ret = _service.registerComp(val);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals("-2", line[2]);
			// 3行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("compCode"), line[1]);
			assertEquals("-2", line[2]);
			// 4行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("0", line[2]);

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
    public void testRegisterCompInvalidParameterCompNameIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z9901");
        map1.put("compName",
                "１２３４５６７８９０あいうえおかきくけこアイウエオカキクケコＡＢＣＤＥＦＧＨＩＪａｂｃｄｅｆｇｈｉｊ1");

        String format = "%s,%s";
        String val = String.format(format, map1.get("compCode"),
                map1.get("compName"));

        String ret = _service.registerComp(val);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new StringReader(ret))) {
			// 1行目
			String[] line = reader.readNext();
			assertEquals(1, line.length);
			assertEquals("START", line[0]);
			// 2行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("1", line[0]);
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals("-2", line[2]);
			// 3行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("0", line[2]);

			assertNull(reader.readNext());
		}
    }

    private void delete(String compCode) {
        _em.createNativeQuery(
                "DELETE FROM auth_denied_comps WHERE comp_code = :compCode")
                .setParameter("compCode", compCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM move_denied_comps WHERE comp_code = :compCode")
                .setParameter("compCode", compCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM auth_allowed_comps WHERE comp_code = :compCode")
                .setParameter("compCode", compCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM auth_denied_game_comps WHERE comp_code = :compCode")
                .setParameter("compCode", compCode).executeUpdate();
        _em.createNativeQuery(
                "DELETE FROM move_denied_game_comps WHERE comp_code = :compCode")
                .setParameter("compCode", compCode).executeUpdate();
        _em.createNativeQuery("DELETE FROM comps WHERE comp_code = :compCode")
                .setParameter("compCode", compCode).executeUpdate();
    }

    private void createComp(String compCode, String compName, Date date,
            String userId) {
        Comp c = new Comp();
        c.setCompCode(compCode);
        c.setName(compName);
        c.setCreateDate(date);
        c.setCreateUserId(userId);
        c.setUpdateDate(date);
        c.setUpdateUserId(userId);
        _em.persist(c);
    }

    private void mergeGame(String gameId) {
        Game g = _em.find(Game.class, gameId);
        if (g == null) {
            g = new Game();
            g.setGameId(gameId);
            g.setTitle("テストタイトル1");
            g.setCreateDate(new Date());
            g.setCreateUserId("TestUserCreate");
            g.setUpdateDate(new Date());
            g.setUpdateUserId("TestUserCreate");
            _em.persist(g);
        }
    }

    private void mergeBill(String billCode, String compCode) {
        Bill b = _em.find(Bill.class, billCode);
        if (b == null) {
            b = new Bill();
            b.setBillCode(billCode);
            b.setCompCode(compCode);
            b.setCreateDate(new Date());
            b.setCreateUserId("TestUserCreate");
            _em.persist(b);
        } else {
            b.setCompCode(compCode);
            _em.persist(b);
        }
    }

    private void createAuthAllowedComp(String gameId, String compCode) {
        AuthAllowedComp e = new AuthAllowedComp();
        e.setPk(new AuthAllowedCompPK(gameId, compCode));
        e.setCreateDate(new Date());
        e.setCreateUserId("TestUserCreate");
        _em.persist(e);
    }

    private void createAuthDeniedGameComp(String gameId, String compCode) {
        AuthDeniedGameComp e = new AuthDeniedGameComp();
        e.setPk(new AuthDeniedGameCompPK(gameId, compCode));
        e.setCreateDate(new Date());
        e.setCreateUserId("TestUserCreate");
        _em.persist(e);
    }

    private void createMoveDeniedGameComp(String gameId, String compCode) {
        MoveDeniedGameComp e = new MoveDeniedGameComp();
        e.setPk(new MoveDeniedGameCompPK(gameId, compCode));
        e.setCreateDate(new Date());
        e.setCreateUserId("TestUserCreate");
        _em.persist(e);
    }

    private void createAuthDeniedComp(String compCode) {
        AuthDeniedComp e = new AuthDeniedComp();
        e.setCompCode(compCode);
        e.setCreateDate(new Date());
        e.setCreateUserId("TestUserCreate");
        _em.persist(e);
    }

    private void createMoveDeniedComp(String compCode) {
        MoveDeniedComp e = new MoveDeniedComp();
        e.setCompCode(compCode);
        e.setCreateDate(new Date());
        e.setCreateUserId("TestUserCreate");
        _em.persist(e);
    }

}
