/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.poweron;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.PowerOnDao;
import jp.co.sega.allnet.auth.api.domain.AuthAllowedComp;
import jp.co.sega.allnet.auth.api.domain.AuthAllowedPlace;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Game;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Machine;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Router;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Status;
import jp.co.sega.allnet.auth.api.util.RequestUtils;
import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author TsuboiY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PowerOnServiceBeanTest {

    @Resource
    private PowerOnService _service;

    @Resource
    private PowerOnDao _dao;

    @After
    public void after() {
        reset(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証成功（ゲームID入れ替えなし）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalSuccess() throws UnsupportedEncodingException {
        // パラメータを設定

        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証成功（ゲームID入れ替えあり）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalSuccessGameIdExchange()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);

        String reservedGameId = "SBYY";

        Router router = new Router();
        router.setRouterId("0000");
        router.setAllnetId(allnetId);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setCountryCode("JPN");
        machine.setGameId(reservedGameId);
        machine.setReservedGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.exchangeMachineGameId(data);
        _dao.deleteLoaderStateLogs(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals("0", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name0", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name1", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name2", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name3", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証失敗（失敗原因コード：-1）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalFail1() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);

        Router router = new Router();

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-3);
        stat.setCause(-1);

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証失敗（失敗原因コード：-2）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalFail2() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-3);
        stat.setCause(-2);

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証失敗（失敗原因コード：-3）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalFail3() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setCountryCode("JPN");

        Game game = new Game();

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-1);
        stat.setCause(-3);

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証失敗(失敗原因コード：-1004)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalFail1004() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();

        Status stat = new Status();
        stat.setStat(-2);
        stat.setCause(-1004);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals("0", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name0", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name1", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name2", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name3", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(17, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証失敗(失敗原因コード：-1005)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalFail1005() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setPlaceId("XXXX");
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-3);
        stat.setCause(-1005);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals("0", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name0", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name1", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name2", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name3", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(18, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証失敗(失敗原因コード：-1006)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalFail1006() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setAllnetId(allnetId2);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗2");
        machine.setPlaceNickName("テスト店舗2#ニックネーム");
        machine.setRegion0Id(2);
        machine.setRegion0Name("地域0名2");
        machine.setRegion1Name("地域1名2");
        machine.setRegion2Name("地域2名2");
        machine.setRegion3Name("地域3名2");
        machine.setCountryCode("TWN");
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-3);
        stat.setCause(-1006);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証失敗(失敗原因コード：-1007)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalFail1007() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String failGameId = "SBYY";
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(failGameId);
        machine.setReservedGameId(failGameId);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-1);
        stat.setCause(-1007);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証失敗（失敗原因コード：1008）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalFail1008() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(2);

        Status stat = new Status();
        stat.setStat(-2);
        stat.setCause(-1008);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証にて例外発生（ありえないエンコーディングが指定された）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test(expected = ApplicationException.class)
    public void testPowerOnNormalErrorInvalidEncode() {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "TestEncode";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        _service.powerOn(param);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証成功ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnAutoSuccess() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";
        String billCode = "123456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(2);

        Status stat = new Status();

        Machine machine = new Machine();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        expect(_dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(false);
        _dao.insertMachine(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証成功ケース（請求先コードが6桁の場合）
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnAutoSuccessShortBillCode()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";
        String billCode = "123456";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(2);

        Status stat = new Status();

        Machine machine = new Machine();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        expect(_dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(false);
        _dao.insertMachine(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証成功ケース（請求先コードが6桁の場合）
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnAutoSuccessBillCodeIsNull()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";
        String billCode = null;

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(2);

        Status stat = new Status();

        Machine machine = new Machine();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        expect(_dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(_dao.checkAuthDenied(eq(gameId), eq(""), eq(billCode)))
                .andReturn(Boolean.FALSE);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(false);
        _dao.insertMachine(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証成功ケース(生産情報チェックで成功)
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnAutoSuccessCheckCrdInfo()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";
        String billCode = "123456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(2);

        Status stat = new Status();

        Machine machine = new Machine();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        expect(_dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(true);
        expect(_dao.findKeychipStat(eq(serial), eq(gameId))).andReturn(1);
        _dao.insertMachine(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証失敗(失敗原因コード：-1)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnAutoFail1() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        Router router = new Router();

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(2);

        Machine machine = new Machine();

        Status stat = new Status();
        stat.setStat(-3);
        stat.setCause(-1);

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals("0", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name0", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name1", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name2", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name3", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(0), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(17, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 失敗原因コード：-2004のケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnAutoFail2004() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";
        String billCode = "123456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(2);

        Status stat = new Status();
        stat.setStat(-2);
        stat.setCause(-2004);

        Machine machine = new Machine();

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(true);
        expect(_dao.findKeychipStat(eq(serial), eq(gameId))).andReturn(null);
        _dao.updateUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 失敗原因コード：-2005のケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnAutoFail2005() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(2);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setPlaceId("XXXX");
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-3);
        stat.setCause(-2005);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals("0", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name0", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name1", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name2", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name3", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(18, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証失敗(失敗原因コード：-2006)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnAutoFail2006() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(2);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setAllnetId(allnetId2);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗2");
        machine.setPlaceNickName("テスト店舗2#ニックネーム");
        machine.setRegion0Id(2);
        machine.setRegion0Name("地域0名2");
        machine.setRegion1Name("地域1名2");
        machine.setRegion2Name("地域2名2");
        machine.setRegion3Name("地域3名2");
        machine.setCountryCode("TWN");
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-2006), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証失敗(失敗原因コード：-2007)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnAutoFail2007() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String failGameId = "SBYY";
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(2);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(failGameId);
        machine.setReservedGameId(failGameId);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-1);
        stat.setCause(-2007);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証失敗（失敗原因コード：2008）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnAutoFail2008() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(2);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(2);

        Status stat = new Status();
        stat.setStat(-2);
        stat.setCause(-2008);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証失敗（失敗原因コード：2009）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnAutoFail2009() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(2);

        Machine machine = new Machine();

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.insertLog(eq(param), eq(-2), eq(-2009), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-2", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証失敗（失敗原因コード：2010）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnAutoFail2010() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";
        String billCode = "123456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(2);

        Machine machine = new Machine();

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.TRUE);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(false);
        _dao.updateUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-2010), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 店舗自動認証成功ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnPlaceAutoSuccess()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";
        String billCode = "123456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(3);

        Status stat = new Status();

        Machine machine = new Machine();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        AuthAllowedPlace placePK = new AuthAllowedPlace();
        placePK.setAllnetId(allnetId);
        placePK.setGameId(gameId);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        expect(_dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(_dao.findAuthAllowedPlace(eq(gameId), eq(router.getAllnetId())))
                .andReturn(placePK);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(false);
        _dao.insertMachine(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 店舗自動認証失敗（失敗原因コード：3004）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnPlaceAutoFail3004()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(3);

        Machine machine = new Machine();

        Status stat = new Status();
        stat.setStat(-2);
        stat.setCause(-3004);

        AuthAllowedPlace placePK = new AuthAllowedPlace();
        placePK.setAllnetId(allnetId);
        placePK.setGameId(gameId);

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-2", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 店舗認証失敗(失敗原因コード：-3005)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnPlaceAutoFail3005()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(3);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setPlaceId("XXXX");
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-3);
        stat.setCause(-3005);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals("0", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name0", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name1", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name2", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name3", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(18, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 店舗自動認証失敗(失敗原因コード：-3006)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnPlaceAutoFail3006()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(3);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setAllnetId(allnetId2);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗2");
        machine.setPlaceNickName("テスト店舗2#ニックネーム");
        machine.setRegion0Id(2);
        machine.setRegion0Name("地域0名2");
        machine.setRegion1Name("地域1名2");
        machine.setRegion2Name("地域2名2");
        machine.setRegion3Name("地域3名2");
        machine.setCountryCode("TWN");
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-3);
        stat.setCause(-3006);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 店舗自動認証失敗(失敗原因コード：-3007)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnPlaceAutoFail3007()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String failGameId = "SBYY";
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(3);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(failGameId);
        machine.setReservedGameId(failGameId);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-1);
        stat.setCause(-3007);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 店舗自動認証失敗（失敗原因コード：3008）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnPlaceAutoFail3008()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(3);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(2);

        Status stat = new Status();
        stat.setStat(-2);
        stat.setCause(-3008);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 店舗自動認証失敗（失敗原因コード：3009）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnPlaceAutoFail3009()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(3);

        Machine machine = new Machine();

        Status stat = new Status();

        AuthAllowedPlace placePK = new AuthAllowedPlace();
        placePK.setAllnetId(allnetId);
        placePK.setGameId(gameId);

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.insertLog(eq(param), eq(-2), eq(-3009), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-2", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 店舗自動認証失敗（失敗原因コード：3011）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnPlaceAutoFail3011()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(3);

        Machine machine = new Machine();

        Status stat = new Status();

        AuthAllowedPlace placePK = null;

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(_dao.findAuthAllowedPlace(eq(gameId), eq(router.getAllnetId())))
                .andReturn(placePK);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(false);
        _dao.updateUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-3011), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証成功ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoSuccess()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";
        String billCode = "123456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Status stat = new Status();

        Machine machine = new Machine();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        AuthAllowedComp compPK = new AuthAllowedComp();
        compPK.setCompCode(billCode.substring(0, 6));
        compPK.setGameId(gameId);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        expect(_dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.findAuthAllowedComp(eq(gameId),
                        eq(billCode.substring(0, 6)))).andReturn(compPK);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(false);
        _dao.insertMachine(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動移設成功ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoMoveSuccess()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setReservedGameId(gameId);
        machine.setAllnetId(allnetId2);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");
        machine.setSetting(1);
        machine.setBillCode(billCode);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        AuthAllowedComp compPK = new AuthAllowedComp();
        compPK.setCompCode(billCode.substring(0, 6));
        compPK.setGameId(gameId);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.checkMoveDenied(eq(gameId), eq(gameVer),
                        eq(billCode.substring(0, 6)), eq(billCode),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.findAuthAllowedComp(eq(gameId),
                        eq(billCode.substring(0, 6)))).andReturn(compPK);
        _dao.updateMachine(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗(失敗原因コード：-1)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail1() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);

        Router router = new Router();

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-3);
        stat.setCause(-1);

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name0", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name1", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name2", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name3", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(17, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗(失敗原因コード：-2)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail2() throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-3);
        stat.setCause(-2);

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name0", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name1", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name2", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name3", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(17, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗(失敗原因コード：-4007)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4007()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String failGameId = "SBYY";
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(failGameId);
        machine.setReservedGameId(failGameId);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-1);
        stat.setCause(-4007);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗(失敗原因コード：-4007)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4007_2()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String failGameId = "SBYY";
        String routerId = "0000";
        String billCode = "2Z3456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(failGameId);
        machine.setReservedGameId(failGameId);
        machine.setAllnetId(allnetId2);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");
        machine.setSetting(1);
        machine.setBillCode(billCode);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        AuthAllowedComp compPK = new AuthAllowedComp();
        compPK.setCompCode(billCode.substring(0, 6));
        compPK.setGameId(gameId);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.checkMoveDenied(eq(gameId), eq(gameVer),
                        eq(billCode.substring(0, 6)), eq(billCode),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.findAuthAllowedComp(eq(gameId),
                        eq(billCode.substring(0, 6)))).andReturn(compPK);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-1), eq(-4007), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗（失敗原因コード：4008）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4008()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗2");
        machine.setPlaceNickName("テスト店舗2#ニックネーム");
        machine.setRegion0Id(2);
        machine.setRegion0Name("地域0名2");
        machine.setRegion1Name("地域1名2");
        machine.setRegion2Name("地域2名2");
        machine.setRegion3Name("地域3名2");
        machine.setCountryCode("TWN");

        machine.setGameId(gameId);
        machine.setSetting(2);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-4008), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗（失敗原因コード：4008・移設が起きない）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4008_2()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(2);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-2), eq(-4008), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-2", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗（失敗原因コード：4009）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4009()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.insertLog(eq(param), eq(-2), eq(-4009), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-2", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗（失敗原因コード：4010）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4010()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";
        String billCode = "123456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        AuthAllowedComp compPK = new AuthAllowedComp();
        compPK.setCompCode(billCode.substring(0, 6));
        compPK.setGameId(gameId);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.TRUE);
        expect(
                _dao.findAuthAllowedComp(eq(gameId),
                        eq(billCode.substring(0, 6)))).andReturn(compPK);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(false);
        _dao.updateUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-4010), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗（失敗原因コード：4012）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4012()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";
        String billCode = "123456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.findAuthAllowedComp(eq(gameId),
                        eq(billCode.substring(0, 6)))).andReturn(null);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(false);
        _dao.updateUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-4012), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗(失敗原因コード：-4012)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4012_2()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setBillCode(billCode);
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setBillCode(billCode);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.checkMoveDenied(eq(gameId), eq(gameVer),
                        eq(billCode.substring(0, 6)), eq(billCode),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.findAuthAllowedComp(eq(gameId),
                        eq(billCode.substring(0, 6)))).andReturn(null);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-4012), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗(失敗原因コード：-4013)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4013()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-4013), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗(失敗原因コード：-4014)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4014()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z345678";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setBillCode(billCode);
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-4014), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗(失敗原因コード：-4015)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4015()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";
        String billCode2 = "2Z345678";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setBillCode(billCode);
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setBillCode(billCode2);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-4015), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗(失敗原因コード：-4016)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4016()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";
        String billCode2 = "02Z345678";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setBillCode(billCode);
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setBillCode(billCode2);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-4016), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗(失敗原因コード：-4017)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4017()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setBillCode(billCode);
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setBillCode(billCode);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        AuthAllowedComp compPK = new AuthAllowedComp();
        compPK.setCompCode(billCode.substring(0, 6));
        compPK.setGameId(gameId);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.checkMoveDenied(eq(gameId), eq(gameVer),
                        eq(billCode.substring(0, 6)), eq(billCode),
                        eq(billCode))).andReturn(Boolean.TRUE);
        expect(
                _dao.findAuthAllowedComp(eq(gameId),
                        eq(billCode.substring(0, 6)))).andReturn(compPK);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-4017), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 包括先自動認証失敗(失敗原因コード：-4018)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnCompAutoFail4018()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setBillCode(billCode);
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(4);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setBillCode(billCode);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        AuthAllowedComp compPK = new AuthAllowedComp();
        compPK.setCompCode(billCode.substring(0, 6));
        compPK.setGameId(gameId);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.TRUE);
        expect(
                _dao.checkMoveDenied(eq(gameId), eq(gameVer),
                        eq(billCode.substring(0, 6)), eq(billCode),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.findAuthAllowedComp(eq(gameId),
                        eq(billCode.substring(0, 6)))).andReturn(compPK);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-4018), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設の自動認証処理成功ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoSuccess()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";
        String billCode = "123456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Status stat = new Status();

        Machine machine = new Machine();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        expect(_dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(false);
        _dao.insertMachine(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設の移設処理成功ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoMoveSuccess()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setReservedGameId(gameId);
        machine.setAllnetId(allnetId2);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");
        machine.setSetting(1);
        machine.setBillCode(billCode);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.checkMoveDenied(eq(gameId), eq(gameVer),
                        eq(billCode.substring(0, 6)), eq(billCode),
                        eq(billCode))).andReturn(Boolean.FALSE);
        _dao.updateMachine(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設の移設処理成功（ゲームID入れ替えあり）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoMoveSuccessGameIdExchange()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        String reservedGameId = "SBYY";

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(reservedGameId);
        machine.setReservedGameId(gameId);
        machine.setAllnetId(allnetId2);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");
        machine.setSetting(1);
        machine.setBillCode(billCode);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.checkMoveDenied(eq(gameId), eq(gameVer),
                        eq(billCode.substring(0, 6)), eq(billCode),
                        eq(billCode))).andReturn(Boolean.FALSE);
        _dao.exchangeMachineGameId(data);
        _dao.deleteLoaderStateLogs(data);
        _dao.updateMachine(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗(失敗原因コード：-5007)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5007()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String failGameId = "SBYY";
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(failGameId);
        machine.setReservedGameId(failGameId);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-1), eq(-5007), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗(失敗原因コード：-5007)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5007_2()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String failGameId = "SBYY";
        String routerId = "0000";
        String billCode = "2Z3456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(failGameId);
        machine.setReservedGameId(failGameId);
        machine.setAllnetId(allnetId2);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");
        machine.setSetting(1);
        machine.setBillCode(billCode);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.checkMoveDenied(eq(gameId), eq(gameVer),
                        eq(billCode.substring(0, 6)), eq(billCode),
                        eq(billCode))).andReturn(Boolean.FALSE);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-1), eq(-5007), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗（失敗原因コード：5008）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5008()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId1 = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId1);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(2);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-5008), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗（失敗原因コード：5008・移設が起きない）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5008_2()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(2);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-2), eq(-5008), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-2", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗（失敗原因コード：5009）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5009()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.insertLog(eq(param), eq(-2), eq(-5009), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-2", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗（失敗原因コード：5010）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5010()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";
        String billCode = "123456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        AuthAllowedComp compPK = new AuthAllowedComp();
        compPK.setCompCode(billCode.substring(0, 6));
        compPK.setGameId(gameId);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.TRUE);
        expect(_dao.checkPrdCheckGame(eq(gameId))).andReturn(false);
        _dao.updateUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-5010), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗(失敗原因コード：-5013)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5013()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-5013), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗(失敗原因コード：-5014)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5014()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z345678";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setBillCode(billCode);
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-5014), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗(失敗原因コード：-5014[店舗の請求先コードがnull])ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5014_2()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = null;

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setBillCode(billCode);
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-5014), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗(失敗原因コード：-5015)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5015()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";
        String billCode2 = "2Z345678";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setBillCode(billCode);
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setBillCode(billCode2);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-5015), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗(失敗原因コード：-5016)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5016()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";
        String billCode2 = "02Z345678";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setBillCode(billCode);
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setBillCode(billCode2);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-5016), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        System.out.println(ret);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗(失敗原因コード：-5017)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5017()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setBillCode(billCode);
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setBillCode(billCode);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        AuthAllowedComp compPK = new AuthAllowedComp();
        compPK.setCompCode(billCode.substring(0, 6));
        compPK.setGameId(gameId);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.checkMoveDenied(eq(gameId), eq(gameVer),
                        eq(billCode.substring(0, 6)), eq(billCode),
                        eq(billCode))).andReturn(Boolean.TRUE);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-5017), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設失敗(失敗原因コード：-5018)ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoFail5018()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setBillCode(billCode);
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId2);
        machine.setBillCode(billCode);
        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.TRUE);
        expect(
                _dao.checkMoveDenied(eq(gameId), eq(gameVer),
                        eq(billCode.substring(0, 6)), eq(billCode),
                        eq(billCode))).andReturn(Boolean.FALSE);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(-3), eq(-5018), anyObject(Router.class),
                anyObject(String.class), anyObject(String.class),
                anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("-3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証成功（FORMAT_VERが無い）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalSuccessNoFormatVer()
            throws UnsupportedEncodingException {
        // パラメータを設定

        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = null;
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()) + "\n", el[1]);
        i++;

        assertEquals(18, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証成功（encodingがnull）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalEncodeIsNull()
            throws UnsupportedEncodingException {
        // パラメータを設定

        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = null;
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1],
                RequestUtils.DEFAULT_CHARACTER_ENCODING));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(), URLDecoder.decode(el[1],
                RequestUtils.DEFAULT_CHARACTER_ENCODING));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(), URLDecoder.decode(el[1],
                RequestUtils.DEFAULT_CHARACTER_ENCODING));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(), URLDecoder.decode(el[1],
                RequestUtils.DEFAULT_CHARACTER_ENCODING));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(), URLDecoder.decode(el[1],
                RequestUtils.DEFAULT_CHARACTER_ENCODING));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(), URLDecoder.decode(el[1],
                RequestUtils.DEFAULT_CHARACTER_ENCODING));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証成功（パラメータのencodingがない）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalEncodeIsEmpty()
            throws UnsupportedEncodingException {
        // パラメータを設定

        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "";
        String formatVer = "2.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setCountryCode("JPN");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");

        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1],
                RequestUtils.DEFAULT_CHARACTER_ENCODING));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(), URLDecoder.decode(el[1],
                RequestUtils.DEFAULT_CHARACTER_ENCODING));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(), URLDecoder.decode(el[1],
                RequestUtils.DEFAULT_CHARACTER_ENCODING));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(), URLDecoder.decode(el[1],
                RequestUtils.DEFAULT_CHARACTER_ENCODING));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(), URLDecoder.decode(el[1],
                RequestUtils.DEFAULT_CHARACTER_ENCODING));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(), URLDecoder.decode(el[1],
                RequestUtils.DEFAULT_CHARACTER_ENCODING));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("year", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("month", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("day", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("hour", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("minute", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("second", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("timezone", el[0]);
        assertEquals("+09:00", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_class", el[0]);
        assertEquals("PowerOnResponseVer2\n", el[1]);
        i++;

        assertEquals(21, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証成功（format_ver=3）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalSuccessFormatVerIs3()
            throws UnsupportedEncodingException {
        // パラメータを設定

        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "3.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";
        String token = "1234567890";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString, token);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setCountryCode("JPN");
        router.setTimezone("+0900");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setCountryCode("JPN");
        machine.setTimezone("+0900");

        machine.setGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("allnet_id", el[0]);
        assertEquals(router.getAllnetId().toString(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("client_timezone", el[0]);
        assertEquals(router.getTimezone(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("utc_time", el[0]);
        assertTrue(el[1]
                .matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z"));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_ver", el[0]);
        assertEquals("3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("token", el[0]);
        assertEquals(token + "\n", el[1]);
        i++;

        assertEquals(18, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証成功（format_ver=3,ゲームID入れ替えあり）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalSuccessGameIdExchangeFormatVerIs3()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "3.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";
        String token = "1234567890";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString, token);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);

        String reservedGameId = "SBYY";

        Router router = new Router();
        router.setRouterId("0000");
        router.setAllnetId(allnetId);
        router.setCountryCode("JPN");
        router.setTimezone("+0900");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(allnetId);
        machine.setPlaceId("XXXX");
        machine.setPlaceName("テスト店舗1");
        machine.setPlaceNickName("テスト店舗1#ニックネーム");
        machine.setCountryCode("JPN");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名");
        machine.setRegion1Name("地域1名");
        machine.setRegion2Name("地域2名");
        machine.setRegion3Name("地域3名");
        machine.setTimezone("+0900");
        machine.setGameId(reservedGameId);
        machine.setReservedGameId(gameId);
        machine.setSetting(1);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.exchangeMachineGameId(data);
        _dao.deleteDeliverReport(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals("1", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(machine.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(machine.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(machine.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(machine.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(machine.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(machine.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(machine.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(machine.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("allnet_id", el[0]);
        assertEquals(router.getAllnetId().toString(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("client_timezone", el[0]);
        assertEquals(router.getTimezone(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("utc_time", el[0]);
        assertTrue(el[1]
                .matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z"));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_ver", el[0]);
        assertEquals("3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("token", el[0]);
        assertEquals(token + "\n", el[1]);
        i++;

        assertEquals(18, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 自動認証・移設の移設処理成功（format_ver=3）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnMoveAutoMoveSuccessFormatVerIs3()
            throws UnsupportedEncodingException {
        // パラメータを設定
        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "3.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";
        String token = "1234567890";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString, token);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        BigDecimal allnetId2 = new BigDecimal(2);
        String routerId = "0000";
        String billCode = "2Z3456789";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setPlaceId("XXXX");
        router.setPlaceName("テスト店舗1");
        router.setPlaceNickName("テスト店舗1#ニックネーム");
        router.setRegion0Id(1);
        router.setRegion0Name("地域0名");
        router.setRegion1Name("地域1名");
        router.setRegion2Name("地域2名");
        router.setRegion3Name("地域3名");
        router.setCountryCode("JPN");
        router.setTimezone("+0900");
        router.setBillCode(billCode);

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(5);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setReservedGameId(gameId);
        machine.setAllnetId(allnetId2);
        machine.setPlaceId("YYYY");
        machine.setPlaceName("テスト店舗2");
        machine.setPlaceNickName("テスト店舗2#ニックネーム");
        machine.setRegion0Id(1);
        machine.setRegion0Name("地域0名2");
        machine.setRegion1Name("地域1名2");
        machine.setRegion2Name("地域2名2");
        machine.setRegion3Name("地域3名2");
        machine.setCountryCode("TWN");
        machine.setTimezone("+0800");
        machine.setSetting(1);
        machine.setBillCode(billCode);

        Status stat = new Status();

        PowerOnData data = new PowerOnData(router, game, machine, stat);

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        expect(
                _dao.checkAuthDenied(eq(gameId), eq(billCode.substring(0, 6)),
                        eq(billCode))).andReturn(Boolean.FALSE);
        expect(
                _dao.checkMoveDenied(eq(gameId), eq(gameVer),
                        eq(billCode.substring(0, 6)), eq(billCode),
                        eq(billCode))).andReturn(Boolean.FALSE);
        _dao.updateMachine(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("uri", el[0]);
        assertEquals(game.getUri(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("host", el[0]);
        assertEquals(game.getHost(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(router.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("name", el[0]);
        assertEquals(router.getPlaceName(), URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("nickname", el[0]);
        assertEquals(router.getPlaceNickName(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals(String.valueOf(router.getRegion0Id()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name0", el[0]);
        assertEquals(router.getRegion0Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name1", el[0]);
        assertEquals(router.getRegion1Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name2", el[0]);
        assertEquals(router.getRegion2Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region_name3", el[0]);
        assertEquals(router.getRegion3Name(),
                URLDecoder.decode(el[1], encoding));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("country", el[0]);
        assertEquals(router.getCountryCode(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("allnet_id", el[0]);
        assertEquals(router.getAllnetId().toString(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("client_timezone", el[0]);
        assertEquals(router.getTimezone(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("utc_time", el[0]);
        assertTrue(el[1]
                .matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z"));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_ver", el[0]);
        assertEquals("3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("token", el[0]);
        assertEquals(token + "\n", el[1]);
        i++;

        assertEquals(18, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.poweron.PowerOnServiceBean#powerOn(jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter)}
     * 
     * 通常認証失敗（失敗原因コード：-1005,format_ver=3）ケース
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testPowerOnNormalFail1005FormatVerIs3()
            throws UnsupportedEncodingException {
        // パラメータを設定

        String gameId = "SBXX";
        String gameVer = "2.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String globalIp = "255.255.255.255";
        String firmVer = "1";
        String bootVer = "2";
        String encoding = "UTF-8";
        String formatVer = "3.00";
        String userAgent = "junit";
        String hops = "2";
        String queryString = "queryString";
        String token = "1234567890";

        PowerOnParameter param = new PowerOnParameter(gameId, gameVer, serial,
                placeIp, globalIp, firmVer, bootVer, encoding, formatVer,
                userAgent, hops, queryString, token);

        // ルータ情報・ゲーム情報・基盤情報を設定
        BigDecimal allnetId = new BigDecimal(1);
        String routerId = "0000";

        Router router = new Router();
        router.setRouterId(routerId);
        router.setAllnetId(allnetId);
        router.setCountryCode("JPN");
        router.setTimezone("+0900");

        Game game = new Game();
        game.setGameId(gameId);
        game.setGameVer(gameVer);
        game.setUri("http://localhost/sys");
        game.setHost("localhost");
        game.setAuth(1);

        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setGameId(gameId);
        machine.setPlaceId("XXXX");
        machine.setSetting(1);

        Status stat = new Status();
        stat.setStat(-3);
        stat.setCause(-1005);

        PowerOnData data = new PowerOnData(router, game, machine, new Status());

        // PowerOnDaoで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _dao.find(eq(gameId), eq(gameVer), eq(serial), eq(placeIp)))
                .andReturn(data);
        _dao.updateMachineStatus(eq(data), eq(param));
        _dao.deleteUnRegisteredMachine(eq(param));
        _dao.insertLog(eq(param), eq(stat.getStat()), eq(stat.getCause()),
                anyObject(Router.class), anyObject(String.class),
                anyObject(String.class), anyObject(String.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        // テスト実行
        String ret = _service.powerOn(param);

        // 結果を検証
        String[] ampSep = ret.split("&");

        int i = 0;
        String[] el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("stat", el[0]);
        assertEquals(String.valueOf(stat.getStat()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("uri", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("host", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("place_id", el[0]);
        assertEquals(machine.getPlaceId(), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("region0", el[0]);
        assertEquals("0", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name0", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name1", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name2", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("region_name3", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(1, el.length);
        assertEquals("client_timezone", el[0]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("utc_time", el[0]);
        assertTrue(el[1]
                .matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z"));
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("setting", el[0]);
        assertEquals(String.valueOf(machine.getSetting()), el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("res_ver", el[0]);
        assertEquals("3", el[1]);
        i++;

        el = null;
        el = ampSep[i].split("=");
        assertEquals(2, el.length);
        assertEquals("token", el[0]);
        assertEquals(token + "\n", el[1]);
        i++;

        assertEquals(14, i);

        // 振る舞いを設定したメソッドのアクセスを検証
        verify(_dao);

    }

}
