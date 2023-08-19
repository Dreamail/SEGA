/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.comp;

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

import jp.co.sega.allnet.auth.common.entity.Comp;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedComp;
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
public class MoveDeniedCompRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(MoveDeniedCompRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "moveDeniedCompRegisterService")
    private MoveDeniedCompRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @After
    public void after() {
        reset(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.comp.MoveDeniedCompRegisterServiceBean#registerMoveDeniedComp(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterMoveDeniedComp() throws IOException {
        // 登録成功
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z9901");
        map1.put("registerFlag", "1");

        // 削除成功
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("compCode", "2Z9902");
        map2.put("registerFlag", "0");

        // 包括先がないので失敗
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("compCode", "2Z9903");
        map3.put("registerFlag", "1");

        // 既に登録済みなので失敗
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("compCode", "2Z9904");
        map4.put("registerFlag", "1");

        // 削除対象がないので失敗
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("compCode", "2Z9905");
        map5.put("registerFlag", "0");

        // 登録成功（包括先に小文字が含まれていた）
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("compCode", "2z9906");
        map6.put("registerFlag", "1");

        String format = "%s,,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("compCode"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("compCode"));
        val += "\n"
                + String.format(format, map3.get("registerFlag"),
                        map3.get("compCode"));
        val += "\n"
                + String.format(format, map4.get("registerFlag"),
                        map4.get("compCode"));
        val += "\n"
                + String.format(format, map5.get("registerFlag"),
                        map5.get("compCode"));
        val += "\n"
                + String.format(format, map6.get("registerFlag"),
                        map6.get("compCode"));

        deleteComp(map1.get("compCode"));
        deleteComp(map2.get("compCode"));
        deleteComp(map3.get("compCode"));
        deleteComp(map4.get("compCode"));
        deleteComp(map5.get("compCode"));
        deleteComp(map6.get("compCode").toUpperCase());

        createComp(map1.get("compCode"));
        createComp(map2.get("compCode"));
        createComp(map4.get("compCode"));
        createComp(map5.get("compCode"));
        createComp(map6.get("compCode").toUpperCase());

        createMoveDeniedComp(map2.get("compCode"));
        createMoveDeniedComp(map4.get("compCode"));

        _em.flush();

        // MoveDeniedCompRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerMoveDeniedComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals("正常に処理が行われました", line[2]);
			assertEquals("1", line[3]);
			// 3行目（正常に削除）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("compCode"), line[1]);
			assertEquals("正常に処理が行われました", line[2]);
			assertEquals("1", line[3]);
			// 4行目（包括先がないので失敗）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("compCode"), line[1]);
			assertEquals("包括先が未登録です", line[2]);
			assertEquals("-6", line[3]);
			// 5行目（既に登録済みなので失敗）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("4", line[0]);
			assertEquals(map4.get("compCode"), line[1]);
			assertEquals("この情報は登録済みです", line[2]);
			assertEquals("2", line[3]);
			// 6行目（削除対象がないので失敗）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("5", line[0]);
			assertEquals(map5.get("compCode"), line[1]);
			assertEquals("削除対象がありません", line[2]);
			assertEquals("3", line[3]);
			// 7行目（正常に追加・包括先に小文字が含まれていた）
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("6", line[0]);
			assertEquals(map6.get("compCode").toUpperCase(), line[1]);
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

        // 店舗認証情報のDB確認
        MoveDeniedComp c = _em.find(MoveDeniedComp.class, map1.get("compCode"));

        assertEquals(map1.get("compCode"), c.getCompCode());
        assertEquals(mockUserId, c.getCreateUserId());

        c = _em.find(MoveDeniedComp.class, map2.get("compCode"));

        assertNull(c);

        c = _em.find(MoveDeniedComp.class, map3.get("compCode"));

        assertNull(c);

        c = _em.find(MoveDeniedComp.class, map4.get("compCode"));

        assertEquals("TestUserCreate", c.getCreateUserId());

        c = _em.find(MoveDeniedComp.class, map5.get("compCode"));

        assertNull(c);

        c = _em.find(MoveDeniedComp.class, map6.get("compCode").toUpperCase());

        assertEquals(map6.get("compCode").toUpperCase(), c.getCompCode());
        assertEquals(mockUserId, c.getCreateUserId());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.comp.MoveDeniedCompRegisterServiceBean#registerMoveDeniedComp(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedCompInvalidParameterCompCodeIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "");
        map1.put("registerFlag", "1");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("registerFlag", "1");

        String format1 = "%s,,%s";
        String format2 = "%s,";
        String val = String.format(format1, map1.get("registerFlag"),
                map1.get("compCode"));
        val += "\n" + String.format(format2, map1.get("registerFlag"));

        String ret = _service.registerMoveDeniedComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.comp.MoveDeniedCompRegisterServiceBean#registerMoveDeniedComp(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedCompInvalidParameterCompCodeIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z99011");
        map1.put("registerFlag", "1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("compCode", "2Z991");
        map2.put("registerFlag", "0");

        String format = "%s,,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("compCode"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("compCode"));

        String ret = _service.registerMoveDeniedComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals("パラメータのフォーマットが不正です", line[2]);
			assertEquals("-3", line[3]);
			// 3行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("compCode"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.comp.MoveDeniedCompRegisterServiceBean#registerMoveDeniedComp(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedCompInvalidParameterCompCodeIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "エラー");
        map1.put("registerFlag", "1");

        String format = "%s,,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("compCode"));

        String ret = _service.registerMoveDeniedComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.comp.MoveDeniedCompRegisterServiceBean#registerMoveDeniedComp(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedCompInvalidParameterRegisterFlagIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z9901");

        String format = ",,%s";
        String val = String.format(format, map1.get("compCode"));

        deleteComp(map1.get("compCode"));
        createComp(map1.get("compCode"));

        String ret = _service.registerMoveDeniedComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.movedenied.comp.MoveDeniedCompRegisterServiceBean#registerMoveDeniedComp(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterMoveDeniedCompInvalidParameterRegisterFlagIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("compCode", "2Z9901");
        map1.put("registerFlag", "A");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("compCode", "2Z9901");
        map2.put("registerFlag", "2");

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("compCode", "2Z9901");
        map3.put("registerFlag", "１");

        String format = "%s,,%s";
        String val = String.format(format, map1.get("registerFlag"),
                map1.get("compCode"));
        val += "\n"
                + String.format(format, map2.get("registerFlag"),
                        map2.get("compCode"));
        val += "\n"
                + String.format(format, map3.get("registerFlag"),
                        map3.get("compCode"));

        deleteComp(map1.get("compCode"));
        createComp(map1.get("compCode"));

        String ret = _service.registerMoveDeniedComp(val);
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
			assertEquals(map1.get("compCode"), line[1]);
			assertEquals("登録フラグのフォーマットが不正です", line[2]);
			assertEquals("-4", line[3]);
			// 3行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("compCode"), line[1]);
			assertEquals("登録フラグのフォーマットが不正です", line[2]);
			assertEquals("-4", line[3]);
			// 4行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("compCode"), line[1]);
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

    private void createComp(String compCode) {
        Comp c = new Comp();
        c.setCompCode(compCode);
        c.setName("テスト包括先");
        c.setCreateDate(new Date());
        c.setCreateUserId("TestUserCreate");
        c.setUpdateDate(new Date());
        c.setUpdateUserId("TestUserCreate");
        _em.persist(c);
    }

    private MoveDeniedComp createMoveDeniedComp(String compCode) {
        MoveDeniedComp c = new MoveDeniedComp();
        c.setCompCode(compCode);
        c.setCreateDate(new Date());
        c.setCreateUserId("TestUserCreate");
        _em.persist(c);
        return c;
    }

}
