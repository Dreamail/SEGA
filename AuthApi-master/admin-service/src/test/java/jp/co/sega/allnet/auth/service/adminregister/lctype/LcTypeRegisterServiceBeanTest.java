/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.lctype;

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

import jp.co.sega.allnet.auth.common.entity.LcType;
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
public class LcTypeRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(LcTypeRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "lcTypeRegisterService")
    private LcTypeRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @After
    public void after() {
        reset(_authMock);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.service.adminregister.comp.LcTypeRegisterServiceBean#registerLcType(java.lang.String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testRegisterLcType() throws IOException {
        // 登録成功
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("lcTypeId", "1");
        map1.put("lcTypeName", "回線種別名1");
        map1.put("deleteFlag", "0");
        // 更新成功
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("lcTypeId", "2");
        map2.put("lcTypeName", "回線種別名2");
        map2.put("deleteFlag", "0");
        // 削除成功
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("lcTypeId", "3");
        map3.put("lcTypeName", "回線種別名3");
        map3.put("deleteFlag", "1");
        // 削除時に包括先が存在しないのでスキップ
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("lcTypeId", "4");
        map4.put("lcTypeName", "回線種別名4");
        map4.put("deleteFlag", "1");
        // 更新時に包括名が同じデータが存在するのでスキップ
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("lcTypeId", "5");
        map5.put("lcTypeName", "回線種別名5");
        map5.put("deleteFlag", "0");
        // 登録成功（削除処理フラグがない）
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("lcTypeId", "6");
        map6.put("lcTypeName", "回線種別名6");
        // 更新成功（削除処理フラグが0ではない）
        Map<String, String> map7 = new HashMap<String, String>();
        map7.put("lcTypeId", "7");
        map7.put("lcTypeName", "回線種別名7");
        map7.put("deleteFlag", "99999");

        String format = "%s,%s,%s";
        String val = String.format(format, map1.get("lcTypeId"),
                map1.get("lcTypeName"), map1.get("deleteFlag"));
        val += "\n"
                + String.format(format, map2.get("lcTypeId"),
                        map2.get("lcTypeName"), map2.get("deleteFlag"));
        val += "\n"
                + String.format(format, map3.get("lcTypeId"),
                        map3.get("lcTypeName"), map3.get("deleteFlag"));
        val += "\n"
                + String.format(format, map4.get("lcTypeId"),
                        map4.get("lcTypeName"), map4.get("deleteFlag"));
        val += "\n"
                + String.format(format, map5.get("lcTypeId"),
                        map5.get("lcTypeName"), map5.get("deleteFlag"));
        val += "\n"
                + String.format("%s,%s", map6.get("lcTypeId"),
                        map6.get("lcTypeName"));
        val += "\n"
                + String.format(format, map7.get("lcTypeId"),
                        map7.get("lcTypeName"), map7.get("deleteFlag"));

        String oldLcTypeName2 = "oldLcTypeName2";
        String oldLcTypeName7 = "oldLcTypeName7";
        Date oldDate2 = new Date();
        Date oldDate5 = new Date();
        Date oldDate7 = new Date();
        String oldUserId2 = "TestUserCreate";
        String oldUserId5 = "TestUserCreate";
        String oldUserId7 = "TestUserCreate";

        delete(map1.get("lcTypeId"));
        delete(map2.get("lcTypeId"));
        delete(map3.get("lcTypeId"));
        delete(map4.get("lcTypeId"));
        delete(map5.get("lcTypeId"));
        delete(map6.get("lcTypeId"));
        delete(map7.get("lcTypeId"));

        createLcType(map2.get("lcTypeId"), oldLcTypeName2, oldDate2, oldUserId2);
        createLcType(map3.get("lcTypeId"), map3.get("lcTypeName"), new Date(),
                "TestUserCreate");
        createLcType(map5.get("lcTypeId"), map5.get("lcTypeName"), oldDate5,
                oldUserId5);
        createLcType(map7.get("lcTypeId"), oldLcTypeName7, oldDate7, oldUserId7);

        // LcTypeRegisterServiceで使用するメソッドの振る舞いを設定
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

        String ret = _service.registerLcType(val);
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
			assertEquals(map1.get("lcTypeId"), line[1]);
			assertEquals("1", line[2]);
			// 3行目：更新成功
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("lcTypeId"), line[1]);
			assertEquals("2", line[2]);
			// 4行目：削除成功
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("3", line[0]);
			assertEquals(map3.get("lcTypeId"), line[1]);
			assertEquals("3", line[2]);
			// 5行目：削除時に包括先が存在しないでスキップ
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("4", line[0]);
			assertEquals(map4.get("lcTypeId"), line[1]);
			assertEquals("0", line[2]);
			// 6行目：削除時に包括先が存在しないのでスキップ
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("5", line[0]);
			assertEquals(map5.get("lcTypeId"), line[1]);
			assertEquals("0", line[2]);
			// 7行目：登録成功（削除処理フラグがない）
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("6", line[0]);
			assertEquals(map6.get("lcTypeId"), line[1]);
			assertEquals("1", line[2]);
			// 8行目：更新成功（削除処理フラグが0ではない）
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("7", line[0]);
			assertEquals(map7.get("lcTypeId"), line[1]);
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
        LcType r = _em.find(LcType.class,
                Integer.parseInt(map1.get("lcTypeId")));
        assertEquals(Integer.parseInt(map1.get("lcTypeId")), r.getLcTypeId());
        assertEquals(map1.get("lcTypeName"), r.getName());
        assertEquals(mockUserId, r.getCreateUserId());
        assertEquals(mockUserId, r.getUpdateUserId());

        r = _em.find(LcType.class, Integer.parseInt(map2.get("lcTypeId")));
        assertEquals(Integer.parseInt(map2.get("lcTypeId")), r.getLcTypeId());
        assertEquals(map2.get("lcTypeName"), r.getName());
        assertEquals(oldDate2, r.getCreateDate());
        assertEquals(oldUserId2, r.getCreateUserId());
        assertNotSame(oldDate2, r.getUpdateDate());
        assertEquals(mockUserId, r.getUpdateUserId());

        r = _em.find(LcType.class, Integer.parseInt(map3.get("lcTypeId")));
        assertNull(r);

        r = _em.find(LcType.class, Integer.parseInt(map4.get("lcTypeId")));
        assertNull(r);

        r = _em.find(LcType.class, Integer.parseInt(map5.get("lcTypeId")));
        assertEquals(Integer.parseInt(map5.get("lcTypeId")), r.getLcTypeId());
        assertEquals(map5.get("lcTypeName"), r.getName());
        assertEquals(oldDate5, r.getCreateDate());
        assertEquals(oldUserId5, r.getCreateUserId());
        assertEquals(oldDate5, r.getUpdateDate());
        assertEquals(oldUserId5, r.getUpdateUserId());

        r = _em.find(LcType.class, Integer.parseInt(map6.get("lcTypeId")));
        assertEquals(Integer.parseInt(map6.get("lcTypeId")), r.getLcTypeId());
        assertEquals(map6.get("lcTypeName"), r.getName());
        assertEquals(mockUserId, r.getCreateUserId());
        assertEquals(mockUserId, r.getUpdateUserId());

        r = _em.find(LcType.class, Integer.parseInt(map7.get("lcTypeId")));
        assertEquals(Integer.parseInt(map7.get("lcTypeId")), r.getLcTypeId());
        assertEquals(map7.get("lcTypeName"), r.getName());
        assertEquals(oldDate7, r.getCreateDate());
        assertEquals(oldUserId7, r.getCreateUserId());
        assertNotSame(oldDate7, r.getUpdateDate());
        assertEquals(mockUserId, r.getUpdateUserId());

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.routertype.LcTypeRegisterServiceBean#registerLcType(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterLcTypeInvalidParameterLcTypeIdIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("lcTypeId", "");
        map1.put("lcTypeName", "回線種別名");

        String format = "%s,%s";
        String val = String.format(format, map1.get("lcTypeId"),
                map1.get("lcTypeName"));

        String ret = _service.registerLcType(val);
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
			assertEquals(map1.get("lcTypeId"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.routertype.LcTypeRegisterServiceBean#registerLcType(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterLcTypeInvalidParameterLcTypeIdIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("lcTypeId", "999999");
        map1.put("lcTypeName", "回線種別名1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("lcTypeId", "-999999");
        map1.put("lcTypeName", "回線種別名2");

        String format = "%s,%s";
        String val = String.format(format, map1.get("lcTypeId"),
                map1.get("lcTypeName"));
        val += "\n"
                + String.format(format, map2.get("lcTypeId"),
                        map2.get("lcTypeName"));

        String ret = _service.registerLcType(val);
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
			assertEquals(map1.get("lcTypeId"), line[1]);
			assertEquals("-1", line[2]);
			// 3行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("lcTypeId"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.routertype.LcTypeRegisterServiceBean#registerLcType(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterLcTypeInvalidParameterLcTypeIdIsInvalidLetter()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("lcTypeId", "エラー");
        map1.put("lcTypeName", "回線種別名");

        String format = "%s,%s";
        String val = String.format(format, map1.get("lcTypeId"),
                map1.get("lcTypeName"));

        String ret = _service.registerLcType(val);
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
			assertEquals(map1.get("lcTypeId"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.routertype.LcTypeRegisterServiceBean#registerLcType(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterLcTypeInvalidParameterLcTypeNameIsEmpty()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("lcTypeId", "1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("lcTypeId", "2");
        map2.put("lcTypeName", "");

        String val = map1.get("lcTypeId");
        val += "\n"
                + String.format("%s,%s", map2.get("lcTypeId"),
                        map2.get("lcTypeName"));

        String ret = _service.registerLcType(val);
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
			assertEquals(map1.get("lcTypeId"), line[1]);
			assertEquals("-2", line[2]);
			// 3行目
			line = reader.readNext();
			assertEquals(3, line.length);
			assertEquals("2", line[0]);
			assertEquals(map2.get("lcTypeId"), line[1]);
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
     * {@link jp.co.sega.allnet.auth.service.adminregister.routertype.LcTypeRegisterServiceBean#registerLcType(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterLcTypeInvalidParameterLcTypeNameIsInvalidSize()
            throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("lcTypeId", "1");
        map1.put("lcTypeName", "１２３４５６７８９０あいうえおかきくけこ1");

        String format = "%s,%s";
        String val = String.format(format, map1.get("lcTypeId"),
                map1.get("lcTypeName"));

        String ret = _service.registerLcType(val);
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
			assertEquals(map1.get("lcTypeId"), line[1]);
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

    private void delete(String lcTypeId) {
        _em.createQuery(
                "delete from LcType l where l.lcTypeId = :lcTypeId")
                .setParameter("lcTypeId", Integer.parseInt(lcTypeId))
                .executeUpdate();

    }

    private void createLcType(String lcTypeId, String lcTypeName, Date date,
            String userId) {
        LcType l = new LcType();
        l.setLcTypeId(Integer.parseInt(lcTypeId));
        l.setName(lcTypeName);
        l.setCreateDate(date);
        l.setCreateUserId(userId);
        l.setUpdateDate(date);
        l.setUpdateUserId(userId);
        _em.persist(l);
    }

}
