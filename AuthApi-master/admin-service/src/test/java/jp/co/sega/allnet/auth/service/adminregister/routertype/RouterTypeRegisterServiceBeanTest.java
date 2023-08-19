/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.routertype;

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

import jp.co.sega.allnet.auth.common.entity.RouterType;
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
public class RouterTypeRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(RouterTypeRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "routerTypeRegisterService")
    private RouterTypeRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @After
    public void after() {
        reset(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.comp.LcTypeRegisterServiceBean#registerRouterType(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterRouterType() throws IOException {
        // 登録成功
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("routerTypeId", "1");
        map1.put("routerTypeName", "ルータ種別名1");
        map1.put("deleteFlag", "0");
        // 更新成功
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("routerTypeId", "2");
        map2.put("routerTypeName", "ルータ種別名2");
        map2.put("deleteFlag", "0");
        // 削除成功
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("routerTypeId", "3");
        map3.put("routerTypeName", "ルータ種別名3");
        map3.put("deleteFlag", "1");
        // 削除時に包括先が存在しないのでスキップ
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("routerTypeId", "4");
        map4.put("routerTypeName", "ルータ種別名4");
        map4.put("deleteFlag", "1");
        // 更新時に包括名が同じデータが存在するのでスキップ
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("routerTypeId", "5");
        map5.put("routerTypeName", "ルータ種別名5");
        map5.put("deleteFlag", "0");
        // 登録成功（削除処理フラグがない）
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("routerTypeId", "6");
        map6.put("routerTypeName", "ルータ種別名6");
        // 更新成功（削除処理フラグが0ではない）
        Map<String, String> map7 = new HashMap<String, String>();
        map7.put("routerTypeId", "7");
        map7.put("routerTypeName", "ルータ種別名7");
        map7.put("deleteFlag", "99999");

        String format = "%s,%s,%s";
        String val = String.format(format, map1.get("routerTypeId"),
                map1.get("routerTypeName"), map1.get("deleteFlag"));
        val += "\n"
                + String.format(format, map2.get("routerTypeId"),
                        map2.get("routerTypeName"), map2.get("deleteFlag"));
        val += "\n"
                + String.format(format, map3.get("routerTypeId"),
                        map3.get("routerTypeName"), map3.get("deleteFlag"));
        val += "\n"
                + String.format(format, map4.get("routerTypeId"),
                        map4.get("routerTypeName"), map4.get("deleteFlag"));
        val += "\n"
                + String.format(format, map5.get("routerTypeId"),
                        map5.get("routerTypeName"), map5.get("deleteFlag"));
        val += "\n"
                + String.format("%s,%s", map6.get("routerTypeId"),
                        map6.get("routerTypeName"));
        val += "\n"
                + String.format(format, map7.get("routerTypeId"),
                        map7.get("routerTypeName"), map7.get("deleteFlag"));

        String oldRouterTypeName2 = "oldRouterTypeName2";
        String oldRouterTypeName7 = "oldRouterTypeName7";
        Date oldDate2 = new Date();
        Date oldDate5 = new Date();
        Date oldDate7 = new Date();
        String oldUserId2 = "TestUserCreate";
        String oldUserId5 = "TestUserCreate";
        String oldUserId7 = "TestUserCreate";

        delete(map1.get("routerTypeId"));
        delete(map2.get("routerTypeId"));
        delete(map3.get("routerTypeId"));
        delete(map4.get("routerTypeId"));
        delete(map5.get("routerTypeId"));
        delete(map6.get("routerTypeId"));
        delete(map7.get("routerTypeId"));

        createRouterType(map2.get("routerTypeId"), oldRouterTypeName2,
                oldDate2, oldUserId2);
        createRouterType(map3.get("routerTypeId"), map3.get("routerTypeName"),
                new Date(), "TestUserCreate");
        createRouterType(map5.get("routerTypeId"), map5.get("routerTypeName"),
                oldDate5, oldUserId5);
        createRouterType(map7.get("routerTypeId"), oldRouterTypeName7,
                oldDate7, oldUserId7);

        // RouterTypeRegisterServiceで使用するメソッドの振る舞いを設定
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

        String ret = _service.registerRouterType(val);
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
			assertEquals(map1.get("routerTypeId"), line[1]);
			assertEquals("1", line[2]);
			// 3行目：更新成功
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("routerTypeId"), line[1]);
			assertEquals("2", line[2]);
			// 4行目：削除成功
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("routerTypeId"), line[1]);
			assertEquals("3", line[2]);
			// 5行目：削除時に包括先が存在しないでスキップ
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("4", line[0]);
			assertEquals(map4.get("routerTypeId"), line[1]);
			assertEquals("0", line[2]);
			// 6行目：削除時に包括先が存在しないのでスキップ
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("5", line[0]);
			assertEquals(map5.get("routerTypeId"), line[1]);
			assertEquals("0", line[2]);
			// 7行目：登録成功（削除処理フラグがない）
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("6", line[0]);
			assertEquals(map6.get("routerTypeId"), line[1]);
			assertEquals("1", line[2]);
			// 8行目：更新成功（削除処理フラグが0ではない）
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("7", line[0]);
			assertEquals(map7.get("routerTypeId"), line[1]);
			assertEquals("2", line[2]);
			// 9行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("5", line[2]);

			assertNull(reader.readNext());
		}

        // ルータ種別情報のDB確認
        RouterType r = _em.find(RouterType.class,
                Integer.parseInt(map1.get("routerTypeId")));
        assertEquals(Integer.parseInt(map1.get("routerTypeId")),
                r.getRouterTypeId());
        assertEquals(map1.get("routerTypeName"), r.getName());
        assertEquals(mockUserId, r.getCreateUserId());
        assertEquals(mockUserId, r.getUpdateUserId());

        r = _em.find(RouterType.class,
                Integer.parseInt(map2.get("routerTypeId")));
        assertEquals(Integer.parseInt(map2.get("routerTypeId")),
                r.getRouterTypeId());
        assertEquals(map2.get("routerTypeName"), r.getName());
        assertEquals(oldDate2, r.getCreateDate());
        assertEquals(oldUserId2, r.getCreateUserId());
        assertNotSame(oldDate2, r.getUpdateDate());
        assertEquals(mockUserId, r.getUpdateUserId());

        r = _em.find(RouterType.class,
                Integer.parseInt(map3.get("routerTypeId")));
        assertNull(r);

        r = _em.find(RouterType.class,
                Integer.parseInt(map4.get("routerTypeId")));
        assertNull(r);

        r = _em.find(RouterType.class,
                Integer.parseInt(map5.get("routerTypeId")));
        assertEquals(Integer.parseInt(map5.get("routerTypeId")),
                r.getRouterTypeId());
        assertEquals(map5.get("routerTypeName"), r.getName());
        assertEquals(oldDate5, r.getCreateDate());
        assertEquals(oldUserId5, r.getCreateUserId());
        assertEquals(oldDate5, r.getUpdateDate());
        assertEquals(oldUserId5, r.getUpdateUserId());

        r = _em.find(RouterType.class,
                Integer.parseInt(map6.get("routerTypeId")));
        assertEquals(Integer.parseInt(map6.get("routerTypeId")),
                r.getRouterTypeId());
        assertEquals(map6.get("routerTypeName"), r.getName());
        assertEquals(mockUserId, r.getCreateUserId());
        assertEquals(mockUserId, r.getUpdateUserId());

        r = _em.find(RouterType.class,
                Integer.parseInt(map7.get("routerTypeId")));
        assertEquals(Integer.parseInt(map7.get("routerTypeId")),
                r.getRouterTypeId());
        assertEquals(map7.get("routerTypeName"), r.getName());
        assertEquals(oldDate7, r.getCreateDate());
        assertEquals(oldUserId7, r.getCreateUserId());
        assertNotSame(oldDate7, r.getUpdateDate());
        assertEquals(mockUserId, r.getUpdateUserId());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.routertype.RouterTypeRegisterServiceBean#registerRouterType(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterTypeInvalidParameterRouterTypeIdIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("routerTypeId", "");
        map1.put("routerTypeName", "ルータ種別名");

        String format = "%s,%s";
        String val = String.format(format, map1.get("routerTypeId"),
                map1.get("routerTypeName"));

        String ret = _service.registerRouterType(val);
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
			assertEquals(map1.get("routerTypeId"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.routertype.RouterTypeRegisterServiceBean#registerRouterType(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterTypeInvalidParameterRouterTypeIdIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("routerTypeId", "999999");
        map1.put("routerTypeName", "ルータ種別名1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("routerTypeId", "-999999");
        map1.put("routerTypeName", "ルータ種別名2");

        String format = "%s,%s";
        String val = String.format(format, map1.get("routerTypeId"),
                map1.get("routerTypeName"));
        val += "\n"
                + String.format(format, map2.get("routerTypeId"),
                        map2.get("routerTypeName"));

        String ret = _service.registerRouterType(val);
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
			assertEquals(map1.get("routerTypeId"), line[1]);
			assertEquals("-1", line[2]);
			// 3行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("routerTypeId"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.routertype.RouterTypeRegisterServiceBean#registerRouterType(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterTypeInvalidParameterRouterTypeIdIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("routerTypeId", "エラー");
        map1.put("routerTypeName", "ルータ種別名");

        String format = "%s,%s";
        String val = String.format(format, map1.get("routerTypeId"),
                map1.get("routerTypeName"));

        String ret = _service.registerRouterType(val);
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
			assertEquals(map1.get("routerTypeId"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.routertype.RouterTypeRegisterServiceBean#registerRouterType(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterTypeInvalidParameterRouterTypeNameIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("routerTypeId", "1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("routerTypeId", "2");
        map2.put("routerTypeName", "");

        String val = map1.get("routerTypeId");
        val += "\n"
                + String.format("%s,%s", map2.get("routerTypeId"),
                        map2.get("routerTypeName"));

        String ret = _service.registerRouterType(val);
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
			assertEquals(map1.get("routerTypeId"), line[1]);
			assertEquals("-2", line[2]);
			// 3行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("routerTypeId"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.routertype.RouterTypeRegisterServiceBean#registerRouterType(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterRouterTypeInvalidParameterRouterTypeNameIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("routerTypeId", "1");
        map1.put("routerTypeName", "１２３４５６７８９０あいうえおかきくけこ1");

        String format = "%s,%s";
        String val = String.format(format, map1.get("routerTypeId"),
                map1.get("routerTypeName"));

        String ret = _service.registerRouterType(val);
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
			assertEquals(map1.get("routerTypeId"), line[1]);
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

    private void delete(String routerTypeId) {
        _em.createQuery(
                "delete from RouterType r where r.routerTypeId = :routerTypeId")
                .setParameter("routerTypeId", Integer.parseInt(routerTypeId))
                .executeUpdate();
    }

    private void createRouterType(String routerTypeId, String routerTypeName,
            Date date, String userId) {
        RouterType r = new RouterType();
        r.setRouterTypeId(Integer.parseInt(routerTypeId));
        r.setName(routerTypeName);
        r.setCreateDate(date);
        r.setCreateUserId(userId);
        r.setUpdateDate(date);
        r.setUpdateUserId(userId);
        _em.persist(r);
    }

}
