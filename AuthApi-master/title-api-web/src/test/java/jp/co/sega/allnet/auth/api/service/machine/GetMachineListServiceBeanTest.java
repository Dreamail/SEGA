/**
 * Copyright (C) 2014 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.machine;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

import com.opencsv.CSVReader;
import jp.co.sega.allnet.auth.api.dao.ApiAccountDao;
import jp.co.sega.allnet.auth.api.dao.MachineTableDao;
import jp.co.sega.allnet.auth.api.domain.Machine;
import jp.co.sega.allnet.auth.api.service.AuthenticationException;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

/**
 * @author NakanoY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
public class GetMachineListServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(GetMachineListServiceBeanTest.class);

    @Resource(name = "getMachineListService")
    private GetMachineListService _service;

    @Resource(name = "apiAccountDao")
    private ApiAccountDao _apiAccountDao;

    @Resource(name = "machineTableDao")
    private MachineTableDao _machineTableDao;

    @After
    public void after() {
        reset(_apiAccountDao);
        reset(_machineTableDao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.machine.GetMachineListServiceBean#getMachineList(java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetMachineList()
            throws IOException, AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        boolean allMachine = false;

        List<Machine> machines = new ArrayList<Machine>();
        machines.add(createMachine(-99999, "0001", "2Z3456789"));
        machines.add(createMachine(-99998, "0002", "3Z3456789"));
        machines.add(createMachine(-99998, "0002", "4Z3456789"));

        // GetMachineListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(_machineTableDao.findMachines(eq(gameId)))
                .andReturn(machines);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_machineTableDao);

        String ret = _service.getMachineList(gameId, password, allMachine);
        _log.info(ret);

        @SuppressWarnings("resource")
        CSVReader reader = new CSVReader(
                new BufferedReader(new StringReader(ret)));
        String[] line;
        int i = 0;
        while ((line = reader.readNext()) != null) {
            switch (i) {
            case 0:
            case 1:
            case 2:
                assertEquals(5, line.length);
                Machine m = machines.get(i);
                assertEquals(m.getAllnetId(), Long.parseLong(line[0]));
                assertEquals(m.getPlaceId().replaceAll("\r|\n", ""), line[1]);
                assertEquals(m.getSerial().replaceAll("\r|\n", ""), line[2]);
                break;
            default:
                fail("予期しないレスポンスが追加されています");
            }
            i++;
        }
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.machine.GetMachineListServiceBean#getMachineList(java.lang.String, java.lang.String, boolean)}
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetMachineListIsAllMachine()
            throws IOException, AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        boolean allMachine = true;

        List<Machine> machines = new ArrayList<Machine>();
        machines.add(createMachine(-99999, "0001", "2Z3456789"));
        machines.add(createMachine(-99998, "0002", "3Z3456789"));
        machines.add(createMachine(-99997, "0003", "4Z3456789"));

        // GetPlaceListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _machineTableDao.findMachinesIncludeAuthDenied(eq(gameId)))
                .andReturn(machines);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_machineTableDao);

        String ret = _service.getMachineList(gameId, password, allMachine);
        _log.info(ret);

        @SuppressWarnings("resource")
        CSVReader reader = new CSVReader(
                new BufferedReader(new StringReader(ret)));
        String[] line;
        int i = 0;
        while ((line = reader.readNext()) != null) {
            switch (i) {
            case 0:
            case 1:
            case 2:
                assertEquals(5, line.length);
                Machine m = machines.get(i);
                assertEquals(m.getAllnetId(), Long.parseLong(line[0]));
                assertEquals(m.getPlaceId().replaceAll("\r|\n", ""), line[1]);
                assertEquals(m.getSerial().replaceAll("\r|\n", ""), line[2]);
                break;
            default:
                fail("予期しないレスポンスが追加されています");
            }
            i++;
        }
    }

    private Machine createMachine(int allnetId, String placeId, String serial) {
        Machine m = new Machine();
        m.setAllnetId(allnetId);
        m.setPlaceId(placeId);
        m.setSerial(serial);

        return m;
    }

}
