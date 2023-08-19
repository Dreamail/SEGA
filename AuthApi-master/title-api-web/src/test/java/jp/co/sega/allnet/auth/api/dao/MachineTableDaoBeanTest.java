/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sega.allnet.auth.api.domain.Machine;
import jp.co.sega.allnet.auth.api.domain.MachineTable;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

/**
 * @author TsuboiY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "dao-test-context.xml" })
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class MachineTableDaoBeanTest {

    @Resource(name = "dataSource")
    private DataSource _ds;

    @Resource(name = "machineTableDao")
    private MachineTableDao _dao;

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.MachineTableDaoBean#findMachineTables(java.lang.String)}
     * .
     * 
     * @throws ParseException
     */
    @Test
    public final void testFindMachineTablesSuccess() throws ParseException {

        String gameId = "SBXX";

        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AYYYYYYYYYY";
        String serial3 = "AXXXXXXXXXX";
        String countryCode = "TWN";
        int region0Id1 = 1;
        int region0Id2 = 2;
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int groupIndex1 = 1;
        int groupIndex2 = 2;
        String lastAccess1 = "2011-11-01 09:30:01.000";
        String lastAccess2 = "2011-11-02 09:30:01.000";
        String lastAccess3 = "2011-11-03 09:00:00.000";
        String lastAuth1 = "2011-11-01 09:30:01.000";
        String lastAuth2 = "";
        String lastAuth3 = "2002-11-01 09:30:01.000";
        String placeId1 = "XXXX";
        String placeId2 = "YYYY";
        String name1 = "\"テスト\",店舗\n\r1";
        String name2 = "\"テスト\",店舗\n\r2";

        deleteMachine(serial1);
        deleteMachine(serial2);

        deleteRoutersAndAuthAllowedPlaces();
        deleteCountryRelations(countryCode);
        deleteRegions(countryCode, region0Id1);
        deleteRegions(countryCode, region0Id2);
        deletePlace(allnetId1);
        deletePlace(allnetId2);

        createTestMachine(serial1, allnetId1, placeId1, gameId, groupIndex1, 1);
        createTestMachine(serial2, allnetId2, placeId2, gameId, groupIndex2, 1);
        createTestMachine(serial3, allnetId2, placeId2, gameId, groupIndex2, 0);

        createTestMachineStatus(serial1, lastAccess1, lastAuth1);
        createTestMachineStatus(serial2, lastAccess2, lastAuth2);
        createTestMachineStatus(serial3, lastAccess3, lastAuth3);

        createTestCountry(countryCode);
        createTestRegion0(countryCode, region0Id1);
        createTestRegion0(countryCode, region0Id2);
        createTestPlace(countryCode, allnetId1, placeId1, name1, region0Id1);
        createTestPlace(countryCode, allnetId2, placeId2, name2, region0Id2);

        List<MachineTable> list = _dao.findMachineTables(gameId);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        assertEquals(2, list.size());

        assertEquals(region0Id1, list.get(0).getRegion0Id().intValue());
        assertEquals(placeId1, list.get(0).getPlaceId());
        assertEquals(name1, list.get(0).getName());
        assertEquals(serial1, list.get(0).getSerial());
        assertEquals(groupIndex1, list.get(0).getGroupIndex());
        assertEquals(df.parse(lastAccess1), list.get(0).getLastAccess());
        assertEquals(df.parse(lastAuth1), list.get(0).getLastAuth());

        assertEquals(region0Id2, list.get(1).getRegion0Id().intValue());
        assertEquals(placeId2, list.get(1).getPlaceId());
        assertEquals(name2, list.get(1).getName());
        assertEquals(serial2, list.get(1).getSerial());
        assertEquals(groupIndex2, list.get(1).getGroupIndex());
        assertEquals(df.parse(lastAccess2), list.get(1).getLastAccess());
        assertNull(list.get(1).getLastAuth());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.MachineTableDaoBean#findMachineTables(java.lang.String)}
     * .
     */
    @Test
    public final void testFindMachineTablesNoMachines() {

        String gameId = "SBXX";

        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AYYYYYYYYYY";
        String countryCode = "TWN";
        int region0Id1 = 1;
        int region0Id2 = 2;
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        String placeId1 = "XXXX";
        String placeId2 = "YYYY";
        String name1 = "\"テスト\",店舗\n\r1";
        String name2 = "\"テスト\",店舗\n\r2";

        deleteMachine(serial1);
        deleteMachine(serial2);

        deleteRoutersAndAuthAllowedPlaces();
        deleteCountryRelations(countryCode);
        deleteRegions(countryCode, region0Id1);
        deleteRegions(countryCode, region0Id2);
        deletePlace(allnetId1);
        deletePlace(allnetId2);

        createTestCountry(countryCode);
        createTestRegion0(countryCode, region0Id1);
        createTestRegion0(countryCode, region0Id2);
        createTestPlace(countryCode, allnetId1, placeId1, name1, region0Id1);
        createTestPlace(countryCode, allnetId2, placeId2, name2, region0Id2);

        List<MachineTable> list = _dao.findMachineTables(gameId);

        assertEquals(0, list.size());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.MachineTableDaoBean#findMachineTables(java.lang.String)}
     * .
     * 
     * @throws ParseException
     */
    @Test
    public final void testFindMachineTablesNoPlaces() throws ParseException {

        String gameId = "SBXX";

        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AYYYYYYYYYY";
        String serial3 = "AXXXXXXXXXX";
        String countryCode = "TWN";
        int region0Id1 = 1;
        int region0Id2 = 2;
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int groupIndex1 = 1;
        int groupIndex2 = 2;
        String lastAccess1 = "2011-11-01 09:30:01.000";
        String lastAccess2 = "2011-11-02 09:30:01.000";
        String lastAccess3 = "2011-11-03 09:00:00.000";
        String lastAuth1 = "2011-11-01 09:30:01.000";
        String lastAuth2 = "";
        String lastAuth3 = "2002-11-01 09:30:01.000";

        deleteMachine(serial1);
        deleteMachine(serial2);

        deleteRoutersAndAuthAllowedPlaces();
        deleteCountryRelations(countryCode);
        deleteRegions(countryCode, region0Id1);
        deleteRegions(countryCode, region0Id2);
        deletePlace(allnetId1);
        deletePlace(allnetId2);

        createTestMachine(serial1, allnetId1, null, gameId, groupIndex1, 1);
        createTestMachine(serial2, allnetId2, null, gameId, groupIndex2, 1);
        createTestMachine(serial3, allnetId2, null, gameId, groupIndex2, 0);

        createTestMachineStatus(serial1, lastAccess1, lastAuth1);
        createTestMachineStatus(serial2, lastAccess2, lastAuth2);
        createTestMachineStatus(serial3, lastAccess3, lastAuth3);

        List<MachineTable> list = _dao.findMachineTables(gameId);

        assertEquals(0, list.size());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.MachineTableDaoBean#findMachineTables(java.lang.String)}
     * .
     */
    @Test
    public final void testFindMachineTablesNoMachineStatuses() {
        String gameId = "SBXX";

        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AYYYYYYYYYY";
        String serial3 = "AXXXXXXXXXX";
        String countryCode = "TWN";
        int region0Id1 = 1;
        int region0Id2 = 2;
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int groupIndex1 = 1;
        int groupIndex2 = 2;
        String placeId1 = "XXXX";
        String placeId2 = "YYYY";
        String name1 = "\"テスト\",店舗\n\r1";
        String name2 = "\"テスト\",店舗\n\r2";

        deleteMachine(serial1);
        deleteMachine(serial2);

        deleteRoutersAndAuthAllowedPlaces();
        deleteCountryRelations(countryCode);
        deleteRegions(countryCode, region0Id1);
        deleteRegions(countryCode, region0Id2);
        deletePlace(allnetId1);
        deletePlace(allnetId2);

        createTestMachine(serial1, allnetId1, placeId1, gameId, groupIndex1, 1);
        createTestMachine(serial2, allnetId2, placeId2, gameId, groupIndex2, 1);
        createTestMachine(serial3, allnetId2, placeId2, gameId, groupIndex2, 0);

        createTestCountry(countryCode);
        createTestRegion0(countryCode, region0Id1);
        createTestRegion0(countryCode, region0Id2);
        createTestPlace(countryCode, allnetId1, placeId1, name1, region0Id1);
        createTestPlace(countryCode, allnetId2, placeId2, name2, region0Id2);

        List<MachineTable> list = _dao.findMachineTables(gameId);

        assertEquals(2, list.size());

        assertEquals(region0Id1, list.get(0).getRegion0Id().intValue());
        assertEquals(placeId1, list.get(0).getPlaceId());
        assertEquals(name1, list.get(0).getName());
        assertEquals(serial1, list.get(0).getSerial());
        assertEquals(groupIndex1, list.get(0).getGroupIndex());
        assertNull(list.get(0).getLastAccess());
        assertNull(list.get(0).getLastAuth());

        assertEquals(region0Id2, list.get(1).getRegion0Id().intValue());
        assertEquals(placeId2, list.get(1).getPlaceId());
        assertEquals(name2, list.get(1).getName());
        assertEquals(serial2, list.get(1).getSerial());
        assertEquals(groupIndex2, list.get(1).getGroupIndex());
        assertNull(list.get(1).getLastAccess());
        assertNull(list.get(1).getLastAuth());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.MachineTableDaoBean#findMachineTables(java.lang.String)}
     * .
     * 
     * @throws ParseException
     */
    @Test
    public final void testFindMachineTablesArgGameIdIsNull()
            throws ParseException {

        String gameId = "SBXX";

        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AYYYYYYYYYY";
        String serial3 = "AXXXXXXXXXX";
        String countryCode = "TWN";
        int region0Id1 = 1;
        int region0Id2 = 2;
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int groupIndex1 = 1;
        int groupIndex2 = 2;
        String lastAccess1 = "2011-11-01 09:30:01.000";
        String lastAccess2 = "2011-11-02 09:30:01.000";
        String lastAccess3 = "2011-11-03 09:00:00.000";
        String lastAuth1 = "2011-11-01 09:30:01.000";
        String lastAuth2 = "";
        String lastAuth3 = "2002-11-01 09:30:01.000";
        String placeId1 = "XXXX";
        String placeId2 = "YYYY";
        String name1 = "\"テスト\",店舗\n\r1";
        String name2 = "\"テスト\",店舗\n\r2";

        deleteMachine(serial1);
        deleteMachine(serial2);

        deleteRoutersAndAuthAllowedPlaces();
        deleteCountryRelations(countryCode);
        deleteRegions(countryCode, region0Id1);
        deleteRegions(countryCode, region0Id2);
        deletePlace(allnetId1);
        deletePlace(allnetId2);

        createTestMachine(serial1, allnetId1, placeId1, gameId, groupIndex1, 1);
        createTestMachine(serial2, allnetId2, placeId2, gameId, groupIndex2, 1);
        createTestMachine(serial3, allnetId2, placeId2, gameId, groupIndex2, 0);

        createTestMachineStatus(serial1, lastAccess1, lastAuth1);
        createTestMachineStatus(serial2, lastAccess2, lastAuth2);
        createTestMachineStatus(serial3, lastAccess3, lastAuth3);

        createTestCountry(countryCode);
        createTestRegion0(countryCode, region0Id1);
        createTestRegion0(countryCode, region0Id2);
        createTestPlace(countryCode, allnetId1, placeId1, name1, region0Id1);
        createTestPlace(countryCode, allnetId2, placeId2, name2, region0Id2);

        List<MachineTable> list = _dao.findMachineTables(null);

        assertEquals(0, list.size());

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.MachineTableDaoBean#findMachineTablesIncludeAuthDenied(java.lang.String)}
     * .
     * 
     * @throws ParseException
     */
    @Test
    public final void testFindMachineTablesIncludeAuthDeniedSuccess()
            throws ParseException {

        String gameId = "SBXX";

        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AXXXXXXXXXX";
        String serial3 = "AYYYYYYYYYY";
        String serial4 = "AWWWWWWWWWW";
        String serial5 = "AVVVVVVVVVV";
        String countryCode = "TWN";
        int region0Id1 = 1;
        int region0Id2 = 2;
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId3 = -99997;
        int groupIndex1 = 1;
        int groupIndex2 = 2;
        String lastAccess1 = "2011-11-01 09:30:01.000";
        String lastAccess2 = "2011-11-02 09:30:01.000";
        String lastAccess3 = "2011-11-03 09:00:00.000";
        String lastAuth1 = "2011-11-01 09:30:01.000";
        String lastAuth2 = "";
        String lastAuth3 = "2002-11-01 09:30:01.000";
        String placeId1 = "XXXX";
        String placeId2 = "YYYY";
        String placeId3 = "ZZZZ";
        String name1 = "\"テスト\",店舗\n\r1";
        String name2 = "\"テスト\",店舗\n\r2";
        String name3 = "\"テスト\",店舗\n\r3";

        deleteMachine(serial1);
        deleteMachine(serial2);

        deleteRoutersAndAuthAllowedPlaces();
        deleteCountryRelations(countryCode);
        deleteRegions(countryCode, region0Id1);
        deleteRegions(countryCode, region0Id2);
        deletePlace(allnetId1);
        deletePlace(allnetId2);
        deletePlace(allnetId3);

        createTestMachine(serial1, allnetId1, placeId1, gameId, groupIndex1, 1);
        createTestMachine(serial2, allnetId2, placeId2, gameId, groupIndex2, 1);
        createTestMachine(serial3, allnetId2, placeId2, gameId, groupIndex2, 0);
        createTestMachine(serial4, allnetId2, placeId2, gameId, groupIndex1, 1);
        createTestMachine(serial5, allnetId3, placeId3, gameId, groupIndex2, 0);

        createTestMachineStatus(serial1, lastAccess1, lastAuth1);
        createTestMachineStatus(serial2, lastAccess2, lastAuth2);
        createTestMachineStatus(serial3, lastAccess3, lastAuth3);
        createTestMachineStatus(serial4, lastAccess1, lastAuth1);
        createTestMachineStatus(serial5, lastAccess2, lastAuth2);

        createTestCountry(countryCode);
        createTestRegion0(countryCode, region0Id1);
        createTestRegion0(countryCode, region0Id2);
        createTestPlace(countryCode, allnetId1, placeId1, name1, region0Id1);
        createTestPlace(countryCode, allnetId2, placeId2, name2, region0Id2);
        createTestPlace(countryCode, allnetId3, placeId3, name3, region0Id2);

        List<MachineTable> list = _dao
                .findMachineTablesIncludeAuthDenied(gameId);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        assertEquals(5, list.size());

        assertEquals(region0Id1, list.get(0).getRegion0Id().intValue());
        assertEquals(placeId1, list.get(0).getPlaceId());
        assertEquals(name1, list.get(0).getName());
        assertEquals(serial1, list.get(0).getSerial());
        assertEquals(groupIndex1, list.get(0).getGroupIndex());
        assertEquals(df.parse(lastAccess1), list.get(0).getLastAccess());
        assertEquals(df.parse(lastAuth1), list.get(0).getLastAuth());

        assertEquals(region0Id2, list.get(1).getRegion0Id().intValue());
        assertEquals(placeId2, list.get(1).getPlaceId());
        assertEquals(name2, list.get(1).getName());
        assertEquals(serial4, list.get(1).getSerial());
        assertEquals(groupIndex1, list.get(1).getGroupIndex());
        assertEquals(df.parse(lastAccess1), list.get(1).getLastAccess());
        assertEquals(df.parse(lastAuth1), list.get(1).getLastAuth());

        assertEquals(region0Id2, list.get(2).getRegion0Id().intValue());
        assertEquals(placeId2, list.get(2).getPlaceId());
        assertEquals(name2, list.get(2).getName());
        assertEquals(serial2, list.get(2).getSerial());
        assertEquals(groupIndex2, list.get(2).getGroupIndex());
        assertEquals(df.parse(lastAccess2), list.get(2).getLastAccess());
        assertNull(list.get(2).getLastAuth());

        assertEquals(region0Id2, list.get(3).getRegion0Id().intValue());
        assertEquals(placeId2, list.get(3).getPlaceId());
        assertEquals(name2, list.get(3).getName());
        assertEquals(serial3, list.get(3).getSerial());
        assertEquals(groupIndex2, list.get(3).getGroupIndex());
        assertEquals(df.parse(lastAccess3), list.get(3).getLastAccess());
        assertEquals(df.parse(lastAuth3), list.get(3).getLastAuth());

        assertEquals(region0Id2, list.get(4).getRegion0Id().intValue());
        assertEquals(placeId3, list.get(4).getPlaceId());
        assertEquals(name3, list.get(4).getName());
        assertEquals(serial5, list.get(4).getSerial());
        assertEquals(groupIndex2, list.get(4).getGroupIndex());
        assertEquals(df.parse(lastAccess2), list.get(4).getLastAccess());
        assertNull(list.get(4).getLastAuth());

    }
    
    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.MachineTableDaoBean#findMachines(java.lang.String)}
     * .
     * 
     * @throws ParseException
     */
    @Test
    public final void testFindMachinesSuccess() throws ParseException {

        String gameId = "SBXX";

        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AYYYYYYYYYY";
        String serial3 = "AXXXXXXXXXX";
        String countryCode = "TWN";
        int region0Id1 = 1;
        int region0Id2 = 2;
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int groupIndex1 = 1;
        int groupIndex2 = 2;
        String lastAccess1 = "2011-11-01 09:30:01.000";
        String lastAccess2 = "2011-11-02 09:30:01.000";
        String lastAccess3 = "2011-11-03 09:00:00.000";
        String lastAuth1 = "2011-11-01 09:30:01.000";
        String lastAuth2 = "";
        String lastAuth3 = "2002-11-01 09:30:01.000";
        String placeId1 = "XXXX";
        String placeId2 = "YYYY";
        String name1 = "\"テスト\",店舗\n\r1";
        String name2 = "\"テスト\",店舗\n\r2";

        deleteMachine(serial1);
        deleteMachine(serial2);

        deleteRoutersAndAuthAllowedPlaces();
        deleteCountryRelations(countryCode);
        deleteRegions(countryCode, region0Id1);
        deleteRegions(countryCode, region0Id2);
        deletePlace(allnetId1);
        deletePlace(allnetId2);

        createTestMachine(serial1, allnetId1, placeId1, gameId, groupIndex1, 1);
        createTestMachine(serial2, allnetId2, placeId2, gameId, groupIndex2, 1);
        createTestMachine(serial3, allnetId2, placeId2, gameId, groupIndex2, 0);

        createTestMachineStatus(serial1, lastAccess1, lastAuth1);
        createTestMachineStatus(serial2, lastAccess2, lastAuth2);
        createTestMachineStatus(serial3, lastAccess3, lastAuth3);

        createTestCountry(countryCode);
        createTestRegion0(countryCode, region0Id1);
        createTestRegion0(countryCode, region0Id2);
        createTestPlace(countryCode, allnetId1, placeId1, name1, region0Id1);
        createTestPlace(countryCode, allnetId2, placeId2, name2, region0Id2);

        List<Machine> list = _dao.findMachines(gameId);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        assertEquals(2, list.size());

        assertEquals(allnetId1, list.get(0).getAllnetId());
        assertEquals(placeId1, list.get(0).getPlaceId());
        assertEquals(serial1, list.get(0).getSerial());
        assertEquals(df.parse(lastAccess1), list.get(0).getLastAccess());
        assertEquals(df.parse(lastAuth1), list.get(0).getLastAuth());

        assertEquals(allnetId2, list.get(1).getAllnetId());
        assertEquals(placeId2, list.get(1).getPlaceId());
        assertEquals(serial2, list.get(1).getSerial());
        assertEquals(df.parse(lastAccess2), list.get(1).getLastAccess());
        assertNull(list.get(1).getLastAuth());

    }
    

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.dao.MachineTableDaoBean#findMachinesIncludeAuthDenied(java.lang.String)}
     * .
     * 
     * @throws ParseException
     */
    @Test
    public final void testFindMachinesIncludeAuthDeniedSuccess()
            throws ParseException {

        String gameId = "SBXX";

        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AXXXXXXXXXX";
        String serial3 = "AYYYYYYYYYY";
        String serial4 = "AWWWWWWWWWW";
        String serial5 = "AVVVVVVVVVV";
        String countryCode = "TWN";
        int region0Id1 = 1;
        int region0Id2 = 2;
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId3 = -99997;
        int groupIndex1 = 1;
        int groupIndex2 = 2;
        String lastAccess1 = "2011-11-01 09:30:01.000";
        String lastAccess2 = "2011-11-02 09:30:01.000";
        String lastAccess3 = "2011-11-03 09:00:00.000";
        String lastAuth1 = "2011-11-01 09:30:01.000";
        String lastAuth2 = "";
        String lastAuth3 = "2002-11-01 09:30:01.000";
        String placeId1 = "XXXX";
        String placeId2 = "YYYY";
        String placeId3 = "ZZZZ";
        String name1 = "\"テスト\",店舗\n\r1";
        String name2 = "\"テスト\",店舗\n\r2";
        String name3 = "\"テスト\",店舗\n\r3";

        deleteMachine(serial1);
        deleteMachine(serial2);

        deleteRoutersAndAuthAllowedPlaces();
        deleteCountryRelations(countryCode);
        deleteRegions(countryCode, region0Id1);
        deleteRegions(countryCode, region0Id2);
        deletePlace(allnetId1);
        deletePlace(allnetId2);
        deletePlace(allnetId3);

        createTestMachine(serial1, allnetId1, placeId1, gameId, groupIndex1, 1);
        createTestMachine(serial2, allnetId2, placeId2, gameId, groupIndex2, 1);
        createTestMachine(serial3, allnetId2, placeId2, gameId, groupIndex2, 0);
        createTestMachine(serial4, allnetId2, placeId2, gameId, groupIndex1, 1);
        createTestMachine(serial5, allnetId3, placeId3, gameId, groupIndex2, 0);

        createTestMachineStatus(serial1, lastAccess1, lastAuth1);
        createTestMachineStatus(serial2, lastAccess2, lastAuth2);
        createTestMachineStatus(serial3, lastAccess3, lastAuth3);
        createTestMachineStatus(serial4, lastAccess1, lastAuth1);
        createTestMachineStatus(serial5, lastAccess2, lastAuth2);

        createTestCountry(countryCode);
        createTestRegion0(countryCode, region0Id1);
        createTestRegion0(countryCode, region0Id2);
        createTestPlace(countryCode, allnetId1, placeId1, name1, region0Id1);
        createTestPlace(countryCode, allnetId2, placeId2, name2, region0Id2);
        createTestPlace(countryCode, allnetId3, placeId3, name3, region0Id2);

        List<Machine> list = _dao
                .findMachinesIncludeAuthDenied(gameId);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        assertEquals(5, list.size());

        assertEquals(allnetId1, list.get(0).getAllnetId());
        assertEquals(placeId1, list.get(0).getPlaceId());
        assertEquals(serial1, list.get(0).getSerial());
        assertEquals(df.parse(lastAccess1), list.get(0).getLastAccess());
        assertEquals(df.parse(lastAuth1), list.get(0).getLastAuth());

        assertEquals(allnetId2, list.get(1).getAllnetId());
        assertEquals(placeId2, list.get(1).getPlaceId());
        assertEquals(serial4, list.get(1).getSerial());
        assertEquals(df.parse(lastAccess1), list.get(1).getLastAccess());
        assertEquals(df.parse(lastAuth1), list.get(1).getLastAuth());

        assertEquals(allnetId2, list.get(2).getAllnetId());
        assertEquals(placeId2, list.get(2).getPlaceId());
        assertEquals(serial2, list.get(2).getSerial());
        assertEquals(df.parse(lastAccess2), list.get(2).getLastAccess());
        assertNull(list.get(2).getLastAuth());
        
        assertEquals(allnetId2, list.get(3).getAllnetId());
        assertEquals(placeId2, list.get(3).getPlaceId());
        assertEquals(serial3, list.get(3).getSerial());
        assertEquals(df.parse(lastAccess3), list.get(3).getLastAccess());
        assertEquals(df.parse(lastAuth3), list.get(3).getLastAuth());
        
        assertEquals(allnetId3, list.get(4).getAllnetId());
        assertEquals(placeId3, list.get(4).getPlaceId());
        assertEquals(serial5, list.get(4).getSerial());
        assertEquals(df.parse(lastAccess2), list.get(4).getLastAccess());
        assertNull(list.get(4).getLastAuth());
        
    }

    private void deleteMachine(String serial) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM machine_statuses WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "DELETE FROM machine_download_orders WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));
        jdbc.update("DELETE FROM machines WHERE serial = :serial",
                new MapSqlParameterSource("serial", serial));
    }

    private void createTestMachine(String serial, int allnetId, String placeId, String gameId,
            int groupIndex, int setting) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update(
                "INSERT INTO machines (serial, allnet_id, place_id, game_id, group_index, setting, create_user_id, update_user_id) VALUES (:serial, :allnetId, :placeId, :gameId, :groupIndex, :setting, :userId, :userId)",
                new MapSqlParameterSource("serial", serial)
                        .addValue("allnetId", allnetId)
                        .addValue("placeId", placeId)
                        .addValue("gameId", gameId)
                        .addValue("groupIndex", groupIndex)
                        .addValue("setting", setting)
                        .addValue("userId", "TestUser"));
    }

    private void createTestMachineStatus(String serial, String lastAccess,
            String lastAuth) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        MapSqlParameterSource param = new MapSqlParameterSource("serial",
                serial).addValue("lastAccess",
                new Timestamp(df.parse(lastAccess).getTime())).addValue(
                "userId", "TestUser");
        if (!lastAuth.isEmpty()) {
            param.addValue("lastAuth", new Timestamp(df.parse(lastAuth)
                    .getTime()));
        } else {
            param.addValue("lastAuth", null);
        }
        jdbc.update(
                "INSERT INTO machine_statuses (serial, last_access, last_auth, create_user_id, update_user_id) VALUES (:serial, :lastAccess, :lastAuth, :userId, :userId)",
                param);
    }

    private void deletePlace(int allnetId) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        MapSqlParameterSource param = new MapSqlParameterSource("allnetId",
                allnetId);
        jdbc.update("DELETE FROM places WHERE allnet_id = :allnetId", param);
    }

    private void deleteRoutersAndAuthAllowedPlaces() {
        JdbcTemplate jdbc = new JdbcTemplate(_ds);
        jdbc.update("DELETE FROM routers");
        jdbc.update("DELETE FROM auth_allowed_places");
    }

    private void deleteCountryRelations(String countryCode) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        MapSqlParameterSource param = new MapSqlParameterSource("countryCode",
                countryCode);
        jdbc.update("DELETE FROM places WHERE country_code = :countryCode",
                param);
        jdbc.update("DELETE FROM region3 WHERE country_code = :countryCode",
                param);
        jdbc.update("DELETE FROM region2 WHERE country_code = :countryCode",
                param);
        jdbc.update("DELETE FROM region1 WHERE country_code = :countryCode",
                param);
        jdbc.update("DELETE FROM region0 WHERE country_code = :countryCode",
                param);
        jdbc.update(
                "DELETE FROM country_download_orders WHERE country_code = :countryCode",
                param);
        jdbc.update(
                "DELETE FROM game_attributes WHERE country_code = :countryCode",
                param);
        jdbc.update("DELETE FROM countries WHERE country_code = :countryCode",
                param);
    }

    private void deleteRegions(String countryCode, int region0Id) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        MapSqlParameterSource param = new MapSqlParameterSource("regionId",
                region0Id).addValue("countryCode", countryCode);
        jdbc.update(
                "DELETE FROM places p where region0_id = :regionId AND country_code = :countryCode",
                param);
        jdbc.update(
                "DELETE FROM region3 r3 where parent_region_id in ("
                        + "select r2.region_id from region2 r2 inner join region1 r1 on r2.parent_region_id = r1.region_id "
                        + "inner join region0 r0 on r1.parent_region_id = r0.region_id where r0.region_id = :regionId AND r0.country_code = :countryCode) AND r3.country_code = :countryCode",
                param);
        jdbc.update(
                "DELETE FROM region2 r2 where parent_region_id in ("
                        + "select r1.region_id from region1 r1 inner join region0 r0 on r1.parent_region_id = r0.region_id "
                        + "where r0.region_id = :regionId AND r0.country_code = :countryCode) AND r2.country_code = :countryCode",
                param);
        jdbc.update(
                "DELETE FROM region1 r1 where parent_region_id in (select r0.region_id from region0 r0 where r0.region_id = :regionId AND r0.country_code = :countryCode) AND r1.country_code = :countryCode",
                param);
        jdbc.update(
                "DELETE FROM region0 where region_id = :regionId AND country_code = :countryCode",
                param);
    }

    private void createTestCountry(String countryCode) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update(
                "INSERT INTO countries (country_code, create_user_id, update_user_id) VALUES (:countryCode, :userId, :userId)",
                new MapSqlParameterSource("countryCode", countryCode).addValue(
                        "userId", "TestUser"));
    }

    private void createTestRegion0(String countryCode, int region0Id) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);

        jdbc.update(
                "INSERT INTO region0 (country_code, region_id, name, create_user_id, update_user_id) VALUES (:countryCode, :regionId, :name, :userId, :userId)",
                new MapSqlParameterSource("countryCode", countryCode)
                        .addValue("regionId", region0Id)
                        .addValue("name",
                                String.format("TestRegion%s", region0Id))
                        .addValue("userId", "TestUser"));
    }

    private void createTestPlace(String countryCode, int allnetId,
            String placeId, String name, int region0Id) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update(
                "INSERT INTO places (allnet_id, place_id, name, region0_id, country_code, create_user_id, update_user_id) "
                        + "VALUES (:allnetId, :placeId, :name, :region0Id, :countryCode, :userId, :userId)",
                new MapSqlParameterSource("countryCode", countryCode)
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", allnetId)
                        .addValue("placeId", placeId).addValue("name", name)
                        .addValue("countryCode", countryCode)
                        .addValue("region0Id", region0Id));
    }

}
