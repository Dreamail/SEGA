/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.order;

import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.Machine;
import jp.co.sega.allnet.auth.common.entity.MachineDownloadOrder;
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
 * @author NakanoY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class DownloadOrderRegisterServiceBeanTest {

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "downloadOrderRegisterService")
    private DownloadOrderRegisterService _service;

    @Resource(name = "authenticationDelegate")
    private AuthenticationDelegate _authMock;

    @After
    public void after() {
        reset(_authMock);
    }

    /**
     * {@link jp.co.sega.allnet.auth.service.adminregister.order.DownloadOrderRegisterServiceBean#registerDownloadOrder(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     * 
     * @throws IOException
     */
    @Test
    public void testRegisterDownloadOrder() throws IOException {

        // 登録成功
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("serial", "A0000000001");
        map1.put("gameId", "SBXX");
        map1.put("uri", "uri1");

        // 更新成功
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("serial", "A0000000002");
        map2.put("gameId", "SBXX");
        map2.put("uri", "uri2");

        // 削除成功
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("serial", "A0000000003");
        map3.put("gameId", "SBXX");
        map3.put("uri", "");

        // 項目数が多いのでエラー
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("serial", "A0000000004");
        map4.put("gameId", "SBXX");
        map4.put("uri", "uri4");

        // 項目数が少ないのでエラー
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("serial", "A0000000005");
        map5.put("gameId", "SBXX");
        map5.put("uri", "uri5");

        // 基板シリアルの値が不正なのでエラー
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("serial", "A00000000060");
        map6.put("gameId", "SBXX");
        map6.put("uri", "uri6");

        // ゲームIDの値が不正なのでエラー
        Map<String, String> map7 = new HashMap<String, String>();
        map7.put("serial", "A0000000007");
        map7.put("gameId", "SBXXXX");
        map7.put("uri", "uri7");

        // URIの値が不正なのでエラー
        Map<String, String> map8 = new HashMap<String, String>();
        map8.put("serial", "A0000000008");
        map8.put("gameId", "SBXX");
        map8.put(
                "uri",
                "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111100000uri8");

        // 入力されたゲームIDとCSVのゲームIDが一致しないのでエラー
        Map<String, String> map9 = new HashMap<String, String>();
        map9.put("serial", "A0000000009");
        map9.put("gameId", "SBYY");
        map9.put("uri", "uri9");

        // 基板情報が存在しないのでエラー
        Map<String, String> map10 = new HashMap<String, String>();
        map10.put("serial", "A0000000010");
        map10.put("gameId", "SBXX");
        map10.put("uri", "uri10");

        // 基板情報にゲームIDが存在しないのでエラー
        Map<String, String> map11 = new HashMap<String, String>();
        map11.put("serial", "A0000000011");
        map11.put("gameId", "SBXX");
        map11.put("uri", "uri11");

        // 削除時に配信指示書情報がないのでエラー
        Map<String, String> map12 = new HashMap<String, String>();
        map12.put("serial", "A0000000012");
        map12.put("gameId", "SBXX");
        map12.put("uri", "");

        deleteMachines(map1.get("serial"));
        deleteMachines(map2.get("serial"));
        deleteMachines(map3.get("serial"));
        deleteMachines(map4.get("serial"));
        deleteMachines(map5.get("serial"));
        deleteMachines(map6.get("serial"));
        deleteMachines(map7.get("serial"));
        deleteMachines(map8.get("serial"));
        deleteMachines(map9.get("serial"));
        deleteMachines(map10.get("serial"));
        deleteMachines(map11.get("serial"));
        deleteMachines(map12.get("serial"));

        createMachine(1, map1.get("serial"), map1.get("gameId"), null, 1, 1,
                "0001");
        createMachine(2, map2.get("serial"), map2.get("gameId"), null, 1, 1,
                "0002");
        createMachine(3, map3.get("serial"), map3.get("gameId"), null, 1, 1,
                "0003");
        createMachine(4, map4.get("serial"), map4.get("gameId"), null, 1, 1,
                "0004");
        createMachine(5, map5.get("serial"), map5.get("gameId"), null, 1, 1,
                "0005");
        createMachine(9, map9.get("serial"), map9.get("gameId"), null, 1, 1,
                "0009");
        createMachine(11, map11.get("serial"), map9.get("gameId"), null, 1, 1,
                "0011");
        createMachine(12, map12.get("serial"), map12.get("gameId"), null, 1, 1,
                "0012");

        createMachineDownloadOrder(map2.get("serial"), map2.get("gameId"), "update_uri");
        createMachineDownloadOrder(map3.get("serial"), map3.get("gameId"), "delete_uri");

        _em.flush();

        String format1 = "%s,%s,1.00,%s";
        String format2 = "%s,%s,1.00,%s,%s";
        String format3 = "%s,%s,1.00";

        String val = String.format(format1, map1.get("serial"),
                map1.get("gameId"), map1.get("uri"));
        val += "\n"
                + String.format(format1, map2.get("serial"),
                        map2.get("gameId"), map2.get("uri"));
        val += "\n"
                + String.format(format1, map3.get("serial"),
                        map3.get("gameId"), map3.get("uri"));
        val += "\n"
                + String.format(format2, map4.get("serial"),
                        map4.get("gameId"), map4.get("uri"), null);
        val += "\n"
                + String.format(format3, map5.get("serial"), map5.get("gameId"));
        val += "\n"
                + String.format(format1, map6.get("serial"),
                        map6.get("gameId"), map6.get("uri"));
        val += "\n"
                + String.format(format1, map7.get("serial"),
                        map7.get("gameId"), map7.get("uri"));
        val += "\n"
                + String.format(format1, map8.get("serial"),
                        map8.get("gameId"), map8.get("uri"));
        val += "\n"
                + String.format(format1, map9.get("serial"),
                        map9.get("gameId"), map9.get("uri"));
        val += "\n"
                + String.format(format1, map10.get("serial"),
                        map10.get("gameId"), map10.get("uri"));
        val += "\n"
                + String.format(format1, map11.get("serial"),
                        map11.get("gameId"), map11.get("uri"));
        val += "\n"
                + String.format(format1, map12.get("serial"),
                        map12.get("gameId"), map12.get("uri"));

        // MachineRegisterServiceで使用するメソッドの振る舞いを設定
        String mockUserId = "test";
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        EasyMock.expect(_authMock.getCurrentUser()).andReturn(mockUserId);
        // // 振る舞いを記憶
        EasyMock.replay(_authMock);

        RegisterServiceResult<DownloadOrderRegisterResult> result = _service
                .registerDownloadOrder(val, map1.get("gameId"));
        List<DownloadOrderRegisterResult> list = result.getList();

        assertEquals(12, list.size());

        DownloadOrderRegisterResult r = list.get(0);
        assertEquals(map1.get("serial"), r.getSerial());
        assertEquals(map1.get("gameId"), r.getGameId());
        assertEquals("1.00", r.getGameVer());
        assertEquals(map1.get("uri"), r.getUri());
        assertEquals("正常に処理が行われました", r.getMessage());
        assertEquals(1, r.getStatus().intValue());

        r = list.get(1);
        assertEquals(map2.get("serial"), r.getSerial());
        assertEquals(map2.get("gameId"), r.getGameId());
        assertEquals("1.00", r.getGameVer());
        assertEquals(map2.get("uri"), r.getUri());
        assertEquals("正常に処理が行われました", r.getMessage());
        assertEquals(1, r.getStatus().intValue());

        r = list.get(2);
        assertEquals(map3.get("serial"), r.getSerial());
        assertEquals(map3.get("gameId"), r.getGameId());
        assertEquals("1.00", r.getGameVer());
        assertEquals(map3.get("uri"), r.getUri());
        assertEquals("正常に処理が行われました", r.getMessage());
        assertEquals(1, r.getStatus().intValue());

        r = list.get(3);
        assertEquals(map4.get("serial"), r.getSerial());
        assertEquals(map4.get("gameId"), r.getGameId());
        assertEquals("1.00", r.getGameVer());
        assertEquals(map4.get("uri"), r.getUri());
        assertEquals("CSVの項目数が不正です", r.getMessage());
        assertEquals(-5, r.getStatus().intValue());

        r = list.get(4);
        assertEquals(map5.get("serial"), r.getSerial());
        assertEquals(map5.get("gameId"), r.getGameId());
        assertEquals("1.00", r.getGameVer());
        assertNull(r.getUri());
        assertEquals("CSVの項目数が不正です", r.getMessage());
        assertEquals(-5, r.getStatus().intValue());

        r = list.get(5);
        assertEquals(map6.get("serial"), r.getSerial());
        assertEquals(map6.get("gameId"), r.getGameId());
        assertEquals("1.00", r.getGameVer());
        assertEquals(map6.get("uri"), r.getUri());
        assertEquals("パラメータのフォーマットが不正です", r.getMessage());
        assertEquals(-9, r.getStatus().intValue());

        r = list.get(6);
        assertEquals(map7.get("serial"), r.getSerial());
        assertEquals(map7.get("gameId"), r.getGameId());
        assertEquals("1.00", r.getGameVer());
        assertEquals(map7.get("uri"), r.getUri());
        assertEquals("パラメータのフォーマットが不正です", r.getMessage());
        assertEquals(-9, r.getStatus().intValue());

        r = list.get(7);
        assertEquals(map8.get("serial"), r.getSerial());
        assertEquals(map8.get("gameId"), r.getGameId());
        assertEquals("1.00", r.getGameVer());
        assertEquals(map8.get("uri"), r.getUri());
        assertEquals("URIが128byteより大きい", r.getMessage());
        assertEquals(-9, r.getStatus().intValue());

        r = list.get(8);
        assertEquals(map9.get("serial"), r.getSerial());
        assertEquals(map9.get("gameId"), r.getGameId());
        assertEquals("1.00", r.getGameVer());
        assertEquals(map9.get("uri"), r.getUri());
        assertEquals("ゲームIDが一致しません", r.getMessage());
        assertEquals(-8, r.getStatus().intValue());

        r = list.get(9);
        assertEquals(map10.get("serial"), r.getSerial());
        assertEquals(map10.get("gameId"), r.getGameId());
        assertEquals("1.00", r.getGameVer());
        assertEquals(map10.get("uri"), r.getUri());
        assertEquals("基板情報が未登録です", r.getMessage());
        assertEquals(-7, r.getStatus().intValue());

        r = list.get(10);
        assertEquals(map11.get("serial"), r.getSerial());
        assertEquals(map11.get("gameId"), r.getGameId());
        assertEquals("1.00", r.getGameVer());
        assertEquals(map11.get("uri"), r.getUri());
        assertEquals("基板情報が未登録です", r.getMessage());
        assertEquals(-7, r.getStatus().intValue());

        r = list.get(11);
        assertEquals(map12.get("serial"), r.getSerial());
        assertEquals(map12.get("gameId"), r.getGameId());
        assertEquals("1.00", r.getGameVer());
        assertEquals(map12.get("uri"), r.getUri());
        assertEquals("基板配信指示書URIが未登録です", r.getMessage());
        assertEquals(-7, r.getStatus().intValue());

        MachineDownloadOrder o = _em.find(MachineDownloadOrder.class,
                map1.get("serial"));
        assertEquals(map1.get("uri"), o.getUri());

        o = _em.find(MachineDownloadOrder.class, map2.get("serial"));
        assertEquals(map2.get("uri"), o.getUri());
        assertNotSame(o.getCreateDate(), o.getUpdateDate());

        o = _em.find(MachineDownloadOrder.class, map3.get("serial"));
        assertNull(o);

        o = _em.find(MachineDownloadOrder.class, map4.get("serial"));
        assertNull(o);

        o = _em.find(MachineDownloadOrder.class, map5.get("serial"));
        assertNull(o);

        o = _em.find(MachineDownloadOrder.class, map6.get("serial"));
        assertNull(o);

        o = _em.find(MachineDownloadOrder.class, map7.get("serial"));
        assertNull(o);

        o = _em.find(MachineDownloadOrder.class, map8.get("serial"));
        assertNull(o);

        o = _em.find(MachineDownloadOrder.class, map9.get("serial"));
        assertNull(o);

        o = _em.find(MachineDownloadOrder.class, map10.get("serial"));
        assertNull(o);

        o = _em.find(MachineDownloadOrder.class, map11.get("serial"));
        assertNull(o);

        o = _em.find(MachineDownloadOrder.class, map12.get("serial"));
        assertNull(o);

        // 振る舞いを設定したメソッドのアクセスを検証
        EasyMock.verify(_authMock);

    }

    private void deleteMachines(String serial) {
        _em.createNativeQuery("DELETE FROM machines WHERE serial = :serial")
                .setParameter("serial", serial).executeUpdate();
    }

    private void createMachine(int allnetId, String serial, String gameId,
            String reservedGameId, int groupIndex, int setting, String placeId) {
        Machine machine = new Machine();
        machine.setSerial(serial);
        machine.setAllnetId(new BigDecimal(allnetId));
        machine.setGameId(gameId);
        machine.setReservedGameId(reservedGameId);
        machine.setGroupIndex(new BigDecimal(groupIndex));
        machine.setSetting(new BigDecimal(setting));
        machine.setPlaceId(placeId);
        machine.setCreateDate(new Date());
        machine.setCreateUserId("TestUserCraete");
        machine.setUpdateDate(new Date());
        machine.setUpdateUserId("TestUserCreate");
        _em.persist(machine);
    }

    private void createMachineDownloadOrder(String serial, String gameId, String uri) {
        MachineDownloadOrder m = new MachineDownloadOrder();
        m.setSerial(serial);
        m.setGameId(gameId);
        m.setUri(uri);
        m.setCreateDate(new Date());
        m.setCreateUserId("TestUserCreate");
        m.setUpdateDate(new Date());
        m.setUpdateUserId("TestUserCreate");
        _em.persist(m);
    }

}
