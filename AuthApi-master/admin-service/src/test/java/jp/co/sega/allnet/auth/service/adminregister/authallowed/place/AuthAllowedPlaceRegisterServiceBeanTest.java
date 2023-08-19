/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authallowed.place;

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

import jp.co.sega.allnet.auth.common.entity.AuthAllowedPlace;
import jp.co.sega.allnet.auth.common.entity.AuthAllowedPlacePK;
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
 * @author NakanoY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class AuthAllowedPlaceRegisterServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(AuthAllowedPlaceRegisterServiceBeanTest.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "authAllowedPlaceRegisterService")
    private AuthAllowedPlaceRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @After
    public void after() {
        reset(_authMock);
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterServiceBean#registerAuthAllowedPlace(java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterAuthAllowedPlace() throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("placeId", "0001");
        map1.put("gameId", "SBXX");
        map1.put("mode", 1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("allnetId", -99998);
        map2.put("placeId", "0002");
        map2.put("gameId", "SBYY");
        map2.put("mode", 2);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("allnetId", -99997);
        map3.put("placeId", "0003");
        map3.put("gameId", "SBXX");
        map3.put("mode", 1);

        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("allnetId", -99998);
        map4.put("placeId", "0004");
        map4.put("gameId", "SBZZ");
        map4.put("mode", 1);

        Map<String, Object> map5 = new HashMap<String, Object>();
        map5.put("allnetId", -99997);
        map5.put("placeId", "0005");
        map5.put("gameId", "SBZZ");
        map5.put("mode", 1);

        Map<String, Object> map6 = new HashMap<String, Object>();
        map6.put("allnetId", -99996);
        map6.put("placeId", "0006");
        map6.put("gameId", "SBXA");
        map6.put("mode", 1);

        Map<String, Object> map7 = new HashMap<String, Object>();
        map7.put("allnetId", -99995);
        map7.put("placeId", "0007");
        map7.put("gameId", "SBXB");
        map7.put("mode", 2);

        Map<String, Object> map8 = new HashMap<String, Object>();
        map8.put("allnetId", "-99994 ");
        map8.put("placeId", "0008");
        map8.put("gameId", " sBxC");
        map8.put("mode", 1);

        String format = ",%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("gameId"), map1.get("mode"));
        val += "\n"
                + String.format(format, map2.get("allnetId"),
                        map2.get("gameId"), map2.get("mode"));
        val += "\n"
                + String.format(format, map3.get("allnetId"),
                        map3.get("gameId"), map3.get("mode"));
        val += "\n"
                + String.format(format, map4.get("allnetId"),
                        map4.get("gameId"), map4.get("mode"));
        val += "\n"
                + String.format(format, map5.get("allnetId"),
                        map5.get("gameId"), map5.get("mode"));
        val += "\n"
                + String.format(format, map6.get("allnetId"),
                        map6.get("gameId"), map6.get("mode"));
        val += "\n"
                + String.format(format, map7.get("allnetId"),
                        map7.get("gameId"), map7.get("mode"));
        val += "\n"
                + String.format(format, map8.get("allnetId"),
                        map8.get("gameId"), map8.get("mode"));

        deletePlace((Integer) map1.get("allnetId"), (String) map1.get("gameId"));
        deletePlace((Integer) map2.get("allnetId"), (String) map2.get("gameId"));
        deletePlace((Integer) map3.get("allnetId"), (String) map3.get("gameId"));
        deletePlace((Integer) map4.get("allnetId"), (String) map4.get("gameId"));
        deletePlace((Integer) map5.get("allnetId"), (String) map5.get("gameId"));
        deletePlace((Integer) map6.get("allnetId"), (String) map6.get("gameId"));
        deletePlace((Integer) map7.get("allnetId"), (String) map7.get("gameId"));
        deletePlace(Integer.parseInt(((String) map8.get("allnetId")).trim()),
                ((String) map8.get("gameId")).toUpperCase().trim());

        deleteGame((String) map1.get("gameId"));
        deleteGame((String) map2.get("gameId"));
        deleteGame((String) map4.get("gameId"));
        deleteGame((String) map6.get("gameId"));
        deleteGame((String) map7.get("gameId"));
        deleteGame(((String) map8.get("gameId")).toUpperCase().trim());

        String countryCode = "TWN";
        createCountry(countryCode);
        createPlace((Integer) map1.get("allnetId"),
                (String) map1.get("placeId"),
                (String) map1.get("gameId"), countryCode);
        createPlace((Integer) map2.get("allnetId"),
                (String) map2.get("placeId"),
                (String) map2.get("gameId"), countryCode);
        createPlace((Integer) map6.get("allnetId"),
                (String) map6.get("placeId"),
                (String) map6.get("gameId"), countryCode);
        createPlace((Integer) map7.get("allnetId"),
                (String) map7.get("placeId"),
                (String) map7.get("gameId"), countryCode);
        createPlace(Integer.parseInt(((String) map8.get("allnetId")).trim()),
                (String) map8.get("placeId"),
                ((String) map8.get("gameId")).toUpperCase().trim(), countryCode);

        createGame((String) map1.get("gameId"));
        createGame((String) map2.get("gameId"));
        createGame((String) map6.get("gameId"));
        createGame((String) map7.get("gameId"));
        createGame(((String) map8.get("gameId")).toUpperCase().trim());

        createAuthAllowedPlace((Integer) map2.get("allnetId"),
                (String) map2.get("gameId"));
        createAuthAllowedPlace((Integer) map6.get("allnetId"),
                (String) map6.get("gameId"));

        _em.flush();

        // AuthAllowedPlaceRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        String ret = _service.registerAuthAllowedPlace(val);
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
			assertEquals(String.valueOf((Integer) map1.get("allnetId")), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("1", line[3]);
			// 3行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("2", line[0]);
			assertEquals(String.valueOf((Integer) map2.get("allnetId")), line[1]);
			assertEquals(map2.get("gameId"), line[2]);
			assertEquals("1", line[3]);
			// 4行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("3", line[0]);
			assertEquals(String.valueOf((Integer) map3.get("allnetId")), line[1]);
			assertEquals(map3.get("gameId"), line[2]);
			assertEquals("-1", line[3]);
			// 5行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("4", line[0]);
			assertEquals(String.valueOf((Integer) map4.get("allnetId")), line[1]);
			assertEquals(map4.get("gameId"), line[2]);
			assertEquals("-2", line[3]);
			// 6行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("5", line[0]);
			assertEquals(String.valueOf((Integer) map5.get("allnetId")), line[1]);
			assertEquals(map5.get("gameId"), line[2]);
			assertEquals("-1", line[3]);
			// 7行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("6", line[0]);
			assertEquals(String.valueOf((Integer) map6.get("allnetId")), line[1]);
			assertEquals(map6.get("gameId"), line[2]);
			assertEquals("2", line[3]);
			// 8行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("7", line[0]);
			assertEquals(String.valueOf((Integer) map7.get("allnetId")), line[1]);
			assertEquals(map7.get("gameId"), line[2]);
			assertEquals("3", line[3]);
			// 9行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("8", line[0]);
			assertEquals(((String) map8.get("allnetId")).trim(), line[1]);
			assertEquals(((String) map8.get("gameId")).toUpperCase().trim(), line[2]);
			assertEquals("1", line[3]);
			// 10行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("3", line[3]);

			assertNull(reader.readNext());
		}

        // 店舗認証情報のDB確認
        AuthAllowedPlacePK pk = new AuthAllowedPlacePK();
        pk.setAllnetId(new Long((Integer) map1.get("allnetId")));
        pk.setGameId((String) map1.get("gameId"));
        AuthAllowedPlace p = _em.find(AuthAllowedPlace.class, pk);

        assertEquals(map1.get("allnetId"), (int) p.getPk().getAllnetId());
        assertEquals(map1.get("gameId"), p.getPk().getGameId());
        assertEquals(mockUserId, p.getCreateUserId());

        pk = new AuthAllowedPlacePK();
        pk.setAllnetId(new Long((Integer) map2.get("allnetId")));
        pk.setGameId((String) map2.get("gameId"));
        p = _em.find(AuthAllowedPlace.class, pk);

        assertNull(p);

        pk = new AuthAllowedPlacePK();
        pk.setAllnetId(new Long((Integer) map3.get("allnetId")));
        pk.setGameId((String) map3.get("gameId"));
        p = _em.find(AuthAllowedPlace.class, pk);

        assertNull(p);

        pk = new AuthAllowedPlacePK();
        pk.setAllnetId(new Long((Integer) map4.get("allnetId")));
        pk.setGameId((String) map4.get("gameId"));
        p = _em.find(AuthAllowedPlace.class, pk);

        assertNull(p);

        pk = new AuthAllowedPlacePK();
        pk.setAllnetId(new Long((Integer) map5.get("allnetId")));
        pk.setGameId((String) map5.get("gameId"));
        p = _em.find(AuthAllowedPlace.class, pk);

        assertNull(p);

        pk = new AuthAllowedPlacePK();
        pk.setAllnetId(new Long((Integer) map6.get("allnetId")));
        pk.setGameId((String) map6.get("gameId"));
        p = _em.find(AuthAllowedPlace.class, pk);

        assertEquals("TestUserCreate", p.getCreateUserId());

        pk = new AuthAllowedPlacePK();
        pk.setAllnetId(new Long((Integer) map7.get("allnetId")));
        pk.setGameId((String) map7.get("gameId"));
        p = _em.find(AuthAllowedPlace.class, pk);

        assertNull(p);

        pk = new AuthAllowedPlacePK();
        pk.setAllnetId(Long.parseLong(((String) map8.get("allnetId")).trim()));
        pk.setGameId(((String) map8.get("gameId")).toUpperCase().trim());
        p = _em.find(AuthAllowedPlace.class, pk);

        assertEquals(Long.parseLong(((String) map8.get("allnetId")).trim()), p
                .getPk().getAllnetId());
        assertEquals(((String) map8.get("gameId")).toUpperCase().trim(), p
                .getPk().getGameId());
        assertEquals(mockUserId, p.getCreateUserId());

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
    public void testRegisterAuthAllowedPlaceInvalidParameterAllnetIdIsEmpty()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", "");
        map1.put("gameId", "SBXX");
        map1.put("mode", 1);

        String format = ",%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("gameId"), map1.get("mode"));

        String ret = _service.registerAuthAllowedPlace(val);
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
			assertEquals(map1.get("allnetId"), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("-1", line[3]);
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
    public void testRegisterAuthAllowedPlaceInvalidParameterAllnetIdIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", 12345687901L);
        map1.put("gameId", "SBXX");
        map1.put("mode", 1);

        String format = ",%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("gameId"), map1.get("mode"));

        String ret = _service.registerAuthAllowedPlace(val);
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
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("-1", line[3]);
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
    public void testRegisterAuthAllowedPlaceInvalidParameterAllnetIdIsNotNum()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", "error");
        map1.put("gameId", "SBXX");
        map1.put("mode", 1);

        String format = ",%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("gameId"), map1.get("mode"));

        String ret = _service.registerAuthAllowedPlace(val);
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
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("-1", line[3]);
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
    public void testRegisterAuthAllowedPlaceInvalidParameterGameIdIsEmpty()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("gameId", "");
        map1.put("mode", 1);

        String format = ",%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("gameId"), map1.get("mode"));

        String ret = _service.registerAuthAllowedPlace(val);
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
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("-2", line[3]);
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
    public void testRegisterAuthAllowedPlaceInvalidParameterGameIdIsOverMax()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("gameId", "123ABC");
        map1.put("mode", 1);

        String format = ",%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("gameId"), map1.get("mode"));

        String ret = _service.registerAuthAllowedPlace(val);
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
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("-2", line[3]);
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
    public void testRegisterAuthAllowedPlaceInvalidParameterGameIdIsInvalidLetter()
            throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("gameId", "Ａ-えらー");
        map1.put("mode", 1);

        String format = ",%s,%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("gameId"), map1.get("mode"));

        String ret = _service.registerAuthAllowedPlace(val);
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
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("-2", line[3]);
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
    public void testRegisterAuthAllowedPlaceNoMode() throws IOException {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("allnetId", -99999);
        map1.put("gameId", "SBXX");

        String format = ",%s,%s";
        String val = String.format(format, map1.get("allnetId"),
                map1.get("gameId"));

        deletePlace((Integer) map1.get("allnetId"), (String) map1.get("gameId"));
        deleteGame((String) map1.get("gameId"));
        String countryCode = "TWN";
        createCountry(countryCode);
        createPlace((Integer) map1.get("allnetId"), "0001",
                (String) map1.get("gameId"), countryCode);
        createGame((String) map1.get("gameId"));
        createAuthAllowedPlace((Integer) map1.get("allnetId"),
                (String) map1.get("gameId"));
        _em.flush();

        String ret = _service.registerAuthAllowedPlace(val);
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
			assertEquals(String.valueOf(map1.get("allnetId")), line[1]);
			assertEquals(map1.get("gameId"), line[2]);
			assertEquals("1", line[3]);
			// 3行目
			line = reader.readNext();
			assertEquals(4, line.length);
			assertEquals("END", line[0]);
			assertEquals("", line[1]);
			assertEquals("", line[2]);
			assertEquals("1", line[3]);

			assertNull(reader.readNext());
		}

        // 店舗認証情報のDB確認
        AuthAllowedPlacePK pk = new AuthAllowedPlacePK();
        pk.setAllnetId(new Long((Integer) map1.get("allnetId")));
        pk.setGameId((String) map1.get("gameId"));
        AuthAllowedPlace p = _em.find(AuthAllowedPlace.class, pk);

        assertNull(p);

    }

    private void deletePlace(int allnetId, String gameId) {
        _em.createNativeQuery(
                "DELETE FROM auth_allowed_places WHERE allnet_id = :allnetId AND game_id = :gameId")
                .setParameter("allnetId", allnetId)
                .setParameter("gameId", gameId).executeUpdate();
        _em.createNativeQuery("DELETE FROM routers WHERE allnet_id = :allnetId")
                .setParameter("allnetId", allnetId).executeUpdate();
        _em.createNativeQuery("DELETE FROM places WHERE allnet_id = :allnetId")
                .setParameter("allnetId", allnetId).executeUpdate();
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

    private void createPlace(int allnetId, String placeId, String gameId,
            String countryCode) {
        _em.createQuery(
                "delete from Place p where p.allnetId = :allnetId or p.placeId = :placeId")
                .setParameter("allnetId", (long) allnetId)
                .setParameter("placeId", placeId).executeUpdate();
        Date now = new Date();
        Place p = new Place();
        p.setAllnetId(allnetId);
        p.setPlaceId(placeId);
        p.setName("テスト店舗");
        p.setCountryCode(countryCode);
        p.setCreateDate(now);
        p.setCreateUserId("TestUserCreate");
        p.setUpdateDate(now);
        p.setUpdateUserId("TestUserCreate");
        _em.persist(p);
    }

    private void createAuthAllowedPlace(int allnetId, String gameId) {
        AuthAllowedPlacePK pk = new AuthAllowedPlacePK(gameId, allnetId);
        AuthAllowedPlace a = _em.find(AuthAllowedPlace.class, pk);
        if (a != null) {
            return;
        }
        a = new AuthAllowedPlace();
        a.setPk(pk);
        a.setCreateDate(new Date());
        a.setCreateUserId("TestUserCreate");
        _em.persist(a);
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
