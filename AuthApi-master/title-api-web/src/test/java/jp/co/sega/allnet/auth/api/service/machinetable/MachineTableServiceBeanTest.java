/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.machinetable;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.ApiAccountDao;
import jp.co.sega.allnet.auth.api.dao.MachineTableDao;
import jp.co.sega.allnet.auth.api.domain.MachineTable;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

import com.opencsv.CSVReader;

/**
 * @author NakanoY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MachineTableServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(MachineTableServiceBeanTest.class);

    @Resource(name = "machineTableService")
    private MachineTableService _service;

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
     * {@link jp.co.sega.allnet.auth.api.service.machinetable.MachineTableServiceBean#machineTable(java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public final void testMachineTableSuccess() throws IOException,
            ParseException {

        String gameId = "SBXX";
        String password = "pass";
        boolean all = false;

        String lastAccess1 = "2011-11-01 09:30:01.000";
        String lastAccess2 = "2011-11-02 09:30:01.000";
        String lastAuth1 = "2011-11-01 09:30:01.000";
        String lastAuth2 = "";

        List<MachineTable> machines = new ArrayList<MachineTable>();
        machines.add(createMachine(1, "XXXX", "\"テスト\",店舗\n\r1", "AZZZZZZZZZZ",
                1, lastAccess1, lastAuth1));
        machines.add(createMachine(2, "YYYY", "\"テスト\",店舗\n\r2", "AYYYYYYYYYY",
                2, lastAccess2, lastAuth2));

        // MachineTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(_machineTableDao.findMachineTables(eq(gameId)))
                .andReturn(machines);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_machineTableDao);

        String ret = _service.machineTable(gameId, password, all);
        _log.info(ret);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:MACHINES", line[0]);
					break;
				case 1:
				case 2:
					assertEquals(7, line.length);
					MachineTable m = machines.get(i - 1);
					assertEquals(m.getRegion0Id().toString(), line[0]);
					assertEquals(m.getPlaceId(), line[1]);
					assertEquals(m.getName().replaceAll("\r|\n", ""), line[2]);
					assertEquals(m.getSerial(), line[3]);
					assertEquals(String.valueOf(m.getGroupIndex()), line[4]);
					assertEquals(df.format(m.getLastAccess()), line[5]);
					if (i == 2) {
						assertEquals("null", line[6]);
					} else {
						assertEquals(df.format(m.getLastAuth()), line[6]);
					}
					break;
				default:
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.machinetable.MachineTableServiceBean#machineTable(java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public final void testMachineTableSuccessAllMachine() throws IOException,
            ParseException {

        String gameId = "SBXX";
        String password = "pass";
        boolean all = true;

        String lastAccess1 = "2011-11-01 09:30:01.000";
        String lastAccess2 = "2011-11-02 09:30:01.000";
        String lastAuth1 = "2011-11-01 09:30:01.000";
        String lastAuth2 = "";

        List<MachineTable> machines = new ArrayList<MachineTable>();
        machines.add(createMachine(1, "XXXX", "\"テスト\",店舗\n\r1", "AZZZZZZZZZZ",
                1, lastAccess1, lastAuth1));
        machines.add(createMachine(2, "YYYY", "\"テスト\",店舗\n\r2", "AYYYYYYYYYY",
                2, lastAccess2, lastAuth2));

        // MachineTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _machineTableDao.findMachineTablesIncludeAuthDenied(eq(gameId)))
                .andReturn(machines);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_machineTableDao);

        String ret = _service.machineTable(gameId, password, all);
        _log.info(ret);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:MACHINES", line[0]);
					break;
				case 1:
				case 2:
					assertEquals(7, line.length);
					MachineTable m = machines.get(i - 1);
					assertEquals(m.getRegion0Id().toString(), line[0]);
					assertEquals(m.getPlaceId(), line[1]);
					assertEquals(m.getName().replaceAll("\r|\n", ""), line[2]);
					assertEquals(m.getSerial(), line[3]);
					assertEquals(String.valueOf(m.getGroupIndex()), line[4]);
					assertEquals(df.format(m.getLastAccess()), line[5]);
					if (i == 2) {
						assertEquals("null", line[6]);
					} else {
						assertEquals(df.format(m.getLastAuth()), line[6]);
					}
					break;
				default:
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.machinetable.MachineTableServiceBean#machineTable(java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public final void testMachineTableAuthFailed() throws IOException,
            ParseException {

        String gameId = "SBXX";
        String password = "pass";
        boolean all = false;

        // MachineTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(false);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);

        String ret = _service.machineTable(gameId, password, all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:MACHINES", line[0]);
					break;
				default:
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.machinetable.MachineTableServiceBean#machineTable(java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public final void testMachineTableArgGameIdIsNull() throws IOException,
            ParseException {

        String gameId = null;
        String password = "pass";
        boolean all = false;

        // MachineTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(false);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);

        String ret = _service.machineTable(gameId, password, all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:MACHINES", line[0]);
					break;
				default:
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.machinetable.MachineTableServiceBean#machineTable(java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public final void testMachineTableArgPasswordIsNull() throws IOException,
            ParseException {

        String gameId = "SBXX";
        String password = null;
        boolean all = false;

        // MachineTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(false);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);

        String ret = _service.machineTable(gameId, password, all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:MACHINES", line[0]);
					break;
				default:
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    private MachineTable createMachine(int region0Id, String placeId,
            String name, String serial, int groupIndex, String lastAccess,
            String lastAuth) throws ParseException {
        MachineTable m = new MachineTable();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        m.setRegion0Id(new BigDecimal(region0Id));
        m.setPlaceId(placeId);
        m.setName(name);
        m.setSerial(serial);
        m.setGroupIndex(groupIndex);
        m.setLastAccess(new Timestamp(df.parse(lastAccess).getTime()));
        if (!lastAuth.isEmpty()) {
            m.setLastAuth(new Timestamp(df.parse(lastAuth).getTime()));
        } else {
            m.setLastAuth(null);
        }

        return m;
    }

}
