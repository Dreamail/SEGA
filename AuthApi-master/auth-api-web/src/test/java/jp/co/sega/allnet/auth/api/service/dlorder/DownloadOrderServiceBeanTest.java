/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.dlorder;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.DownloadOrderDao;
import jp.co.sega.allnet.auth.api.util.RequestUtils;
import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author NakanoY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DownloadOrderServiceBeanTest {

    @Resource(name = "downloadOrderService")
    private DownloadOrderService _service;

    @Resource(name = "downloadOrderDao")
    private DownloadOrderDao _dao;

    @After
    public void after() {
        reset(_dao);
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderServiceBean#downloadOrder(jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDownloadOrderSuccess() {
        String gameId = "SBXX";
        String ver = "1.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String encode = "UTF-8";

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 1;
        String uri = "Test/Uri";
        int setting = 1;

        String otherSerial1 = "AYYYYYYYYYY";
        String otherSerial2 = "AXXXXXXXXXX";

        DownloadOrderParameter param = new DownloadOrderParameter(gameId, ver,
                serial, placeIp, encode);

        DownloadOrder order = new DownloadOrder();
        order.setSerial(serial);
        order.setAllnetId(allnetId);
        order.setCountryCode(countryCode);
        order.setGroupIndex(groupIndex);
        order.setUri(uri);
        order.setSetting(setting);
        
        List<String> list = new ArrayList<String>();
        list.add(otherSerial1);
        list.add(otherSerial2);

        // DownloadOrderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(0))).andReturn(
                order);
        EasyMock.expect(
                _dao.findGroupSerials(eq(serial), eq(allnetId), eq(groupIndex),
                        eq(gameId))).andReturn(list);
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(1))).andReturn(
                order);
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        String res = _service.downloadOrder(param);

        Map<String, String> map = parseResponce(res);

        assertEquals(3, map.size());
        assertEquals(String.valueOf(setting), map.get("stat"));
        assertEquals(list.get(0) + "," + list.get(1), map.get("serial"));
        assertEquals(uri + "|" + uri, map.get("uri"));
        assertEquals(encode, param.getEncode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderServiceBean#downloadOrder(jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDownloadOrderArgEncodeIsNull() {
        String gameId = "SBXX";
        String ver = "1.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String encode = RequestUtils.DEFAULT_CHARACTER_ENCODING;

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 1;
        String uri = "Test/Uri";
        int setting = 1;

        String otherSerial1 = "AYYYYYYYYYY";
        String otherSerial2 = "AXXXXXXXXXX";

        DownloadOrderParameter param = new DownloadOrderParameter(gameId, ver,
                serial, placeIp, null);

        DownloadOrder order = new DownloadOrder();
        order.setSerial(serial);
        order.setAllnetId(allnetId);
        order.setCountryCode(countryCode);
        order.setGroupIndex(groupIndex);
        order.setUri(uri);
        order.setSetting(setting);

        List<String> list = new ArrayList<String>();
        list.add(otherSerial1);
        list.add(otherSerial2);

        // DownloadOrderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(0))).andReturn(
                order);
        EasyMock.expect(
                _dao.findGroupSerials(eq(serial), eq(allnetId), eq(groupIndex),
                        eq(gameId))).andReturn(list);
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(1))).andReturn(
                order);
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        String res = _service.downloadOrder(param);

        Map<String, String> map = parseResponce(res);

        assertEquals(3, map.size());
        assertEquals(String.valueOf(setting), map.get("stat"));
        assertEquals(list.get(0) + "," + list.get(1), map.get("serial"));
        assertEquals(uri + "|" +uri, map.get("uri"));
        assertEquals(encode, param.getEncode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderServiceBean#downloadOrder(jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDownloadOrderArgEncodeIsEmpty() {
        String gameId = "SBXX";
        String ver = "1.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String encode = RequestUtils.DEFAULT_CHARACTER_ENCODING;

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 1;
        String uri = "Test/Uri";
        int setting = 1;

        String otherSerial1 = "AYYYYYYYYYY";
        String otherSerial2 = "AXXXXXXXXXX";

        DownloadOrderParameter param = new DownloadOrderParameter(gameId, ver,
                serial, placeIp, "");

        DownloadOrder order = new DownloadOrder();
        order.setSerial(serial);
        order.setAllnetId(allnetId);
        order.setCountryCode(countryCode);
        order.setGroupIndex(groupIndex);
        order.setUri(uri);
        order.setSetting(setting);

        List<String> list = new ArrayList<String>();
        list.add(otherSerial1);
        list.add(otherSerial2);

        // DownloadOrderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(0))).andReturn(
                order);
        EasyMock.expect(
                _dao.findGroupSerials(eq(serial), eq(allnetId), eq(groupIndex),
                        eq(gameId))).andReturn(list);
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(1))).andReturn(
                order);
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        String res = _service.downloadOrder(param);

        Map<String, String> map = parseResponce(res);

        assertEquals(3, map.size());
        assertEquals(String.valueOf(setting), map.get("stat"));
        assertEquals(list.get(0) + "," + list.get(1), map.get("serial"));
        assertEquals(uri + "|" + uri, map.get("uri"));
        assertEquals(encode, param.getEncode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderServiceBean#downloadOrder(jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDownloadOrderSettingNG() {
        String gameId = "SBXX";
        String ver = "1.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String encode = RequestUtils.DEFAULT_CHARACTER_ENCODING;

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 1;
        String uri = "Test/Uri";
        int setting = 2;

        String otherSerial1 = "AYYYYYYYYYY";
        String otherSerial2 = "AXXXXXXXXXX";

        DownloadOrderParameter param = new DownloadOrderParameter(gameId, ver,
                serial, placeIp, null);

        DownloadOrder order = new DownloadOrder();
        order.setSerial(serial);
        order.setAllnetId(allnetId);
        order.setCountryCode(countryCode);
        order.setGroupIndex(groupIndex);
        order.setUri(uri);
        order.setSetting(setting);

        List<String> list = new ArrayList<String>();
        list.add(otherSerial1);
        list.add(otherSerial2);

        // DownloadOrderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(0))).andReturn(
                order);
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(1))).andReturn(
                order);

        // 振る舞いを記憶
        EasyMock.replay(_dao);

        String res = _service.downloadOrder(param);

        Map<String, String> map = parseResponce(res);

        assertEquals(1, map.size());
        assertEquals(String.valueOf(setting), map.get("stat"));
        assertEquals(encode, param.getEncode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderServiceBean#downloadOrder(jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDownloadOrderUseCountryDownloadOrder() {
        String gameId = "SBXX";
        String ver = "1.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String encode = RequestUtils.DEFAULT_CHARACTER_ENCODING;

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 1;
        String uri = "Test/Uri";
        int setting = 1;

        String otherSerial1 = "AYYYYYYYYYY";
        String otherSerial2 = "AXXXXXXXXXX";

        DownloadOrderParameter param = new DownloadOrderParameter(gameId, ver,
                serial, placeIp, null);

        DownloadOrder order = new DownloadOrder();
        order.setSerial(serial);
        order.setAllnetId(allnetId);
        order.setCountryCode(countryCode);
        order.setGroupIndex(groupIndex);
        order.setSetting(setting);

        List<String> list = new ArrayList<String>();
        list.add(otherSerial1);
        list.add(otherSerial2);

        // DownloadOrderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(0))).andReturn(
                order);
        EasyMock.expect(
                _dao.findGroupSerials(eq(serial), eq(allnetId), eq(groupIndex),
                        eq(gameId))).andReturn(list);
        EasyMock.expect(
                _dao.findUriByCountry(eq(gameId), eq(ver), eq(countryCode), eq(0)))
                .andReturn(uri);
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(1))).andReturn(
                order);
        EasyMock.expect(
                _dao.findUriByCountry(eq(gameId), eq(ver), eq(countryCode), eq(1)))
                .andReturn(uri);
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        String res = _service.downloadOrder(param);

        Map<String, String> map = parseResponce(res);

        assertEquals(3, map.size());
        assertEquals(String.valueOf(setting), map.get("stat"));
        assertEquals(list.get(0) + "," + list.get(1), map.get("serial"));
        assertEquals(uri + "|" + uri, map.get("uri"));
        assertEquals(encode, param.getEncode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderServiceBean#downloadOrder(jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDownloadOrderUseDownloadOrder() {
        String gameId = "SBXX";
        String ver = "1.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String encode = RequestUtils.DEFAULT_CHARACTER_ENCODING;

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 1;
        String uri = "Test/Uri";
        int setting = 1;

        String otherSerial1 = "AYYYYYYYYYY";
        String otherSerial2 = "AXXXXXXXXXX";

        DownloadOrderParameter param = new DownloadOrderParameter(gameId, ver,
                serial, placeIp, null);

        DownloadOrder order = new DownloadOrder();
        order.setSerial(serial);
        order.setAllnetId(allnetId);
        order.setCountryCode(countryCode);
        order.setGroupIndex(groupIndex);
        order.setSetting(setting);

        List<String> list = new ArrayList<String>();
        list.add(otherSerial1);
        list.add(otherSerial2);

        // DownloadOrderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(0))).andReturn(
                order);
        EasyMock.expect(
                _dao.findGroupSerials(eq(serial), eq(allnetId), eq(groupIndex),
                        eq(gameId))).andReturn(list);
        EasyMock.expect(
                _dao.findUriByCountry(eq(gameId), eq(ver), eq(countryCode), eq(0)))
                .andReturn(null);
        EasyMock.expect(_dao.findUriByGame(eq(gameId), eq(ver), eq(0))).andReturn(uri);
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(1))).andReturn(
                order);
        EasyMock.expect(
                _dao.findUriByCountry(eq(gameId), eq(ver), eq(countryCode), eq(1)))
                .andReturn(null);
        EasyMock.expect(_dao.findUriByGame(eq(gameId), eq(ver), eq(1))).andReturn(uri);
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        String res = _service.downloadOrder(param);

        Map<String, String> map = parseResponce(res);

        assertEquals(3, map.size());
        assertEquals(String.valueOf(setting), map.get("stat"));
        assertEquals(list.get(0) + "," + list.get(1), map.get("serial"));
        assertEquals(uri + "|" + uri, map.get("uri"));
        assertEquals(encode, param.getEncode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderServiceBean#downloadOrder(jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDownloadOrderNoUri() {
        String gameId = "SBXX";
        String ver = "1.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String encode = RequestUtils.DEFAULT_CHARACTER_ENCODING;

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 1;
        int setting = 1;

        String otherSerial1 = "AYYYYYYYYYY";
        String otherSerial2 = "AXXXXXXXXXX";

        DownloadOrderParameter param = new DownloadOrderParameter(gameId, ver,
                serial, placeIp, null);

        DownloadOrder order = new DownloadOrder();
        order.setSerial(serial);
        order.setAllnetId(allnetId);
        order.setCountryCode(countryCode);
        order.setGroupIndex(groupIndex);
        order.setSetting(setting);

        List<String> list = new ArrayList<String>();
        list.add(otherSerial1);
        list.add(otherSerial2);

        // DownloadOrderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(0))).andReturn(
                order);
        EasyMock.expect(
                _dao.findGroupSerials(eq(serial), eq(allnetId), eq(groupIndex),
                        eq(gameId))).andReturn(list);
        EasyMock.expect(
                _dao.findUriByCountry(eq(gameId), eq(ver), eq(countryCode), eq(0)))
                .andReturn(null);
        EasyMock.expect(_dao.findUriByGame(eq(gameId), eq(ver), eq(0)))
                .andReturn(null);
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(1))).andReturn(
                order);
        EasyMock.expect(
                _dao.findUriByCountry(eq(gameId), eq(ver), eq(countryCode), eq(1)))
                .andReturn(null);
        EasyMock.expect(_dao.findUriByGame(eq(gameId), eq(ver), eq(1)))
                .andReturn(null);
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        String res = _service.downloadOrder(param);

        Map<String, String> map = parseResponce(res);

        assertEquals(3, map.size());
        assertEquals(String.valueOf(setting), map.get("stat"));
        assertEquals(list.get(0) + "," + list.get(1), map.get("serial"));
        assertEquals("null", map.get("uri"));
        assertEquals(encode, param.getEncode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderServiceBean#downloadOrder(jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDownloadOrderArgGameIdIsNull() {
        String gameId = null;
        String ver = "1.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String encode = RequestUtils.DEFAULT_CHARACTER_ENCODING;

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 1;
        int setting = 1;

        String otherSerial1 = "AYYYYYYYYYY";
        String otherSerial2 = "AXXXXXXXXXX";

        DownloadOrderParameter param = new DownloadOrderParameter(gameId, ver,
                serial, placeIp, null);

        DownloadOrder order = new DownloadOrder();
        order.setSerial(serial);
        order.setAllnetId(allnetId);
        order.setCountryCode(countryCode);
        order.setGroupIndex(groupIndex);
        order.setSetting(setting);

        List<String> list = new ArrayList<String>();
        list.add(otherSerial1);
        list.add(otherSerial2);

        // DownloadOrderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(0))).andReturn(
                order);
        EasyMock.expect(
                _dao.findGroupSerials(eq(serial), eq(allnetId), eq(groupIndex),
                        eq(gameId))).andReturn(list);
        EasyMock.expect(
                _dao.findUriByCountry(eq(gameId), eq(ver), eq(countryCode), eq(0)))
                .andReturn(null);
        EasyMock.expect(_dao.findUriByGame(eq(gameId), eq(ver), eq(0)))
                .andReturn(null);
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(1))).andReturn(
                order);
        EasyMock.expect(
                _dao.findUriByCountry(eq(gameId), eq(ver), eq(countryCode), eq(1)))
                .andReturn(null);
        EasyMock.expect(_dao.findUriByGame(eq(gameId), eq(ver), eq(1)))
                .andReturn(null);
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        String res = _service.downloadOrder(param);

        Map<String, String> map = parseResponce(res);

        assertEquals(3, map.size());
        assertEquals(String.valueOf(setting), map.get("stat"));
        assertEquals(list.get(0) + "," + list.get(1), map.get("serial"));
        assertEquals("null", map.get("uri"));
        assertEquals(encode, param.getEncode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderServiceBean#downloadOrder(jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDownloadOrderArgGameVerIsNull() {
        String gameId = "SBXX";
        String ver = null;
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String encode = RequestUtils.DEFAULT_CHARACTER_ENCODING;

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 1;
        int setting = 1;

        String otherSerial1 = "AYYYYYYYYYY";
        String otherSerial2 = "AXXXXXXXXXX";

        DownloadOrderParameter param = new DownloadOrderParameter(gameId, ver,
                serial, placeIp, null);

        DownloadOrder order = new DownloadOrder();
        order.setSerial(serial);
        order.setAllnetId(allnetId);
        order.setCountryCode(countryCode);
        order.setGroupIndex(groupIndex);
        order.setSetting(setting);

        List<String> list = new ArrayList<String>();
        list.add(otherSerial1);
        list.add(otherSerial2);

        // DownloadOrderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(0))).andReturn(
                order);
        EasyMock.expect(
                _dao.findGroupSerials(eq(serial), eq(allnetId), eq(groupIndex),
                        eq(gameId))).andReturn(list);
        EasyMock.expect(
                _dao.findUriByCountry(eq(gameId), eq(ver), eq(countryCode), eq(0)))
                .andReturn(null);
        EasyMock.expect(_dao.findUriByGame(eq(gameId), eq(ver), eq(0)))
                .andReturn(null);
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(1))).andReturn(
                order);
        EasyMock.expect(
                _dao.findUriByCountry(eq(gameId), eq(ver), eq(countryCode), eq(1)))
                .andReturn(null);
        EasyMock.expect(_dao.findUriByGame(eq(gameId), eq(ver), eq(1)))
                .andReturn(null);
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        String res = _service.downloadOrder(param);

        Map<String, String> map = parseResponce(res);

        assertEquals(3, map.size());
        assertEquals(String.valueOf(setting), map.get("stat"));
        assertEquals(list.get(0) + "," + list.get(1), map.get("serial"));
        assertEquals("null", map.get("uri"));
        assertEquals(encode, param.getEncode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderServiceBean#downloadOrder(jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDownloadOrderArgSerialIsNull() {
        String gameId = "SBXX";
        String ver = "1.00";
        String serial = null;
        String placeIp = "1.1.1.1";
        String encode = RequestUtils.DEFAULT_CHARACTER_ENCODING;

        int setting = 0;

        DownloadOrderParameter param = new DownloadOrderParameter(gameId, ver,
                serial, placeIp, null);

        DownloadOrder order = new DownloadOrder();

        // DownloadOrderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(0))).andReturn(
                order);
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(1))).andReturn(
                order);
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        String res = _service.downloadOrder(param);

        Map<String, String> map = parseResponce(res);

        assertEquals(1, map.size());
        assertEquals(String.valueOf(setting), map.get("stat"));
        assertEquals(encode, param.getEncode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderServiceBean#downloadOrder(jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDownloadOrderArgPlaceIpIsNull() {
        String gameId = "SBXX";
        String ver = "1.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = null;
        String encode = "UTF-8";

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 1;
        String uri = "Test/Uri";
        int setting = 1;

        String otherSerial1 = "AYYYYYYYYYY";
        String otherSerial2 = "AXXXXXXXXXX";

        DownloadOrderParameter param = new DownloadOrderParameter(gameId, ver,
                serial, placeIp, encode);

        DownloadOrder order = new DownloadOrder();
        order.setSerial(serial);
        order.setAllnetId(allnetId);
        order.setCountryCode(countryCode);
        order.setGroupIndex(groupIndex);
        order.setUri(uri);
        order.setSetting(setting);

        List<String> list = new ArrayList<String>();
        list.add(otherSerial1);
        list.add(otherSerial2);

        // DownloadOrderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(0))).andReturn(
                order);
        EasyMock.expect(
                _dao.findGroupSerials(eq(serial), eq(allnetId), eq(groupIndex),
                        eq(gameId))).andReturn(list);
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(1))).andReturn(
                order);
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        String res = _service.downloadOrder(param);

        Map<String, String> map = parseResponce(res);

        assertEquals(3, map.size());
        assertEquals(String.valueOf(setting), map.get("stat"));
        assertEquals(list.get(0) + "," + list.get(1), map.get("serial"));
        assertEquals(uri + "|" + uri, map.get("uri"));
        assertEquals(encode, param.getEncode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderServiceBean#downloadOrder(jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testDownloadOrderNoOtherSerial() {
        String gameId = "SBXX";
        String ver = "1.00";
        String serial = "AZZZZZZZZZZ";
        String placeIp = "1.1.1.1";
        String encode = "UTF-8";

        int allnetId = -99999;
        String countryCode = "TWN";
        int groupIndex = 1;
        String uri = "Test/Uri";
        int setting = 1;

        DownloadOrderParameter param = new DownloadOrderParameter(gameId, ver,
                serial, placeIp, encode);

        DownloadOrder order = new DownloadOrder();
        order.setSerial(serial);
        order.setAllnetId(allnetId);
        order.setCountryCode(countryCode);
        order.setGroupIndex(groupIndex);
        order.setUri(uri);
        order.setSetting(setting);

        List<String> list = new ArrayList<String>();

        // DownloadOrderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(0))).andReturn(
                order);
        EasyMock.expect(
                _dao.findGroupSerials(eq(serial), eq(allnetId), eq(groupIndex),
                        eq(gameId))).andReturn(list);
        EasyMock.expect(_dao.findMachineDownloadOrder(eq(serial), eq(1))).andReturn(
                order);
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        String res = _service.downloadOrder(param);

        Map<String, String> map = parseResponce(res);

        assertEquals(2, map.size());
        assertEquals(String.valueOf(setting), map.get("stat"));
        assertEquals(uri + "|" + uri, map.get("uri"));
        assertEquals(encode, param.getEncode());
    }

    /**
     * レスポンスを分割する。
     * 
     * @param res
     * @return
     */
    private Map<String, String> parseResponce(String res) {
        Map<String, String> map = new HashMap<String, String>();

        // リクエストを&で分割
        String[] st = res.split("&");

        for (String s : st) {
            // リクエストを=で分割
            String[] sp = s.split("=");
            String key = null;
            String value = null;
            switch (sp.length) {
            case 1:
                key = sp[0];
                value = "";
                break;
            case 2:
                key = sp[0];
                value = sp[1];
                break;
            default:
                throw new ApplicationException(new IllegalArgumentException(
                        "パラメータのセットに\"=\"が2つ以上存在する"));
            }
            map.put(key, value);

        }
        return map;
    }
}
