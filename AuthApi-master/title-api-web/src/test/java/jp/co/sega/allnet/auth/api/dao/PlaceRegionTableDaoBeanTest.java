/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

import jp.co.sega.allnet.auth.api.domain.Place;
import jp.co.sega.allnet.auth.api.domain.Region;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

/**
 * @author NakanoY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "dao-test-context.xml" })
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class PlaceRegionTableDaoBeanTest {

    @Resource(name = "dataSource")
    private DataSource _ds;

    @Resource(name = "placeRegionTableDao")
    private PlaceRegionTableDao _dao;

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findPlaces(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindPlacesSuccess() {
        String serial = "AZZZZZZZZZ";
        int allnetId = -99999;
        String countryCode1 = "TWN";
        String countryCode2 = "CHN";
        String gameId = "SBXX";
        String otherGameId = "SBYY";
        String billCode1 = "2Z345678";
        String billCode2 = "3Z345678";

        int i = 0;
        for (; i < 2; i++) {

            deleteMachine(serial + i);
            createTestMachine(serial + i, allnetId - i, gameId, 1, 1);
        }

        deleteMachine(serial + i);
        createTestMachine(serial + i, allnetId - i, otherGameId, 1, 1);
        i++;

        deleteMachine(serial + i);
        createTestMachine(serial + i, allnetId - 1, gameId, 1, 1);

        i = 0;
        List<Place> testPlaceList = new ArrayList<Place>();
        deletePlaceWithRelations(allnetId - i, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId - i, countryCode2, billCode2);
        Place p = createPlace(allnetId - i, countryCode1, billCode1, 1, 2, 3,
                4, i);
        createTestPlaceRelations(p);
        createTestPlace(p);
        testPlaceList.add(p);

        for (i = 1; i < 5; i++) {
            p = createPlace(allnetId - i, countryCode2, billCode2, 1, 2, 3, 4,
                    i);
            deletePlace(allnetId - i);
            if (i == 1) {
                createTestPlaceRelations(p);
                testPlaceList.add(p);
            }
            createTestPlace(p);
        }

        List<Place> list = _dao.findPlaces(gameId, 0, null);

        assertEquals(2, list.size());

        for (i = 0; i < list.size(); i++) {
            assertNotNull(list.get(i).getPlaceId());
            assertEquals(testPlaceList.get(i).getPlaceId(), list.get(i)
                    .getPlaceId());
            assertEquals(testPlaceList.get(i).getName(), list.get(i).getName());
            assertEquals(testPlaceList.get(i).getTel(), list.get(i).getTel());
            assertEquals(testPlaceList.get(i).getAddress(), list.get(i)
                    .getAddress());
            assertEquals(testPlaceList.get(i).getZipCode(), list.get(i)
                    .getZipCode());
            assertEquals(testPlaceList.get(i).getRegion0Id(), list.get(i)
                    .getRegion0Id());
            assertEquals(testPlaceList.get(i).getRegion1Id(), list.get(i)
                    .getRegion1Id());
            assertEquals(testPlaceList.get(i).getRegion2Id(), list.get(i)
                    .getRegion2Id());
            assertEquals(testPlaceList.get(i).getRegion3Id(), list.get(i)
                    .getRegion3Id());
            assertEquals(testPlaceList.get(i).getStation(), list.get(i)
                    .getStation());
            assertEquals(testPlaceList.get(i).getOpenTime(), list.get(i)
                    .getOpenTime());
            assertEquals(testPlaceList.get(i).getCloseTime(), list.get(i)
                    .getCloseTime());
            assertEquals(testPlaceList.get(i).getSpecialInfo(), list.get(i)
                    .getSpecialInfo());
            assertEquals(testPlaceList.get(i).getNickname(), list.get(i)
                    .getNickname());
            assertEquals(testPlaceList.get(i).getBillCode(), list.get(i)
                    .getBillCode());
            assertEquals(testPlaceList.get(i).getAllnetId(), list.get(i)
                    .getAllnetId());
            assertEquals(testPlaceList.get(i).getCountryCode(), list.get(i)
                    .getCountryCode());
        }
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findPlaces(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindPlacesActiveDays() {
        String serial = "AZZZZZZZZZ";
        int allnetId = -99999;
        String countryCode1 = "TWN";
        String countryCode2 = "CHN";
        String gameId = "SBXX";
        String otherGameId = "SBYY";
        String billCode1 = "2Z345678";
        String billCode2 = "3Z345678";

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);

        int i = 0;
        for (; i < 2; i++) {
            deleteMachine(serial + i);
            createTestMachine(serial + i, allnetId - i, gameId, 1, 1);
            createTestMachineStatuses(serial + i, cal.getTime());
        }

        deleteMachine(serial + i);
        createTestMachine(serial + i, allnetId - i, otherGameId, 1, 1);
        createTestMachineStatuses(serial + i, cal.getTime());
        i++;

        deleteMachine(serial + i);
        createTestMachine(serial + i, allnetId - 1, gameId, 1, 1);
        createTestMachineStatuses(serial + i, cal.getTime());
        i++;

        deleteMachine(serial + i);
        createTestMachine(serial + i, allnetId - i, gameId, 1, 1);
        i++;

        // activeDaysより古い
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.MILLISECOND, -1);
        deleteMachine(serial + i);
        createTestMachine(serial + i, allnetId - i, gameId, 1, 1);
        createTestMachineStatuses(serial + i, cal.getTime());

        i = 0;
        List<Place> testPlaceList = new ArrayList<Place>();
        deletePlaceWithRelations(allnetId - i, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId - i, countryCode2, billCode2);
        Place p = createPlace(allnetId - i, countryCode1, billCode1, 1, 2, 3,
                4, i);
        createTestPlaceRelations(p);
        createTestPlace(p);
        testPlaceList.add(p);

        for (i = 1; i < 6; i++) {
            p = createPlace(allnetId - i, countryCode2, billCode2, 1, 2, 3, 4,
                    i);
            deletePlace(allnetId - i);
            if (i == 1) {
                createTestPlaceRelations(p);
                testPlaceList.add(p);
            }
            createTestPlace(p);
        }

        List<Place> list = _dao.findPlaces(gameId, 1, null);

        assertEquals(2, list.size());

        for (i = 0; i < list.size(); i++) {
            assertNotNull(list.get(i).getPlaceId());
            assertEquals(testPlaceList.get(i).getPlaceId(), list.get(i)
                    .getPlaceId());
            assertEquals(testPlaceList.get(i).getName(), list.get(i).getName());
            assertEquals(testPlaceList.get(i).getTel(), list.get(i).getTel());
            assertEquals(testPlaceList.get(i).getAddress(), list.get(i)
                    .getAddress());
            assertEquals(testPlaceList.get(i).getZipCode(), list.get(i)
                    .getZipCode());
            assertEquals(testPlaceList.get(i).getRegion0Id(), list.get(i)
                    .getRegion0Id());
            assertEquals(testPlaceList.get(i).getRegion1Id(), list.get(i)
                    .getRegion1Id());
            assertEquals(testPlaceList.get(i).getRegion2Id(), list.get(i)
                    .getRegion2Id());
            assertEquals(testPlaceList.get(i).getRegion3Id(), list.get(i)
                    .getRegion3Id());
            assertEquals(testPlaceList.get(i).getStation(), list.get(i)
                    .getStation());
            assertEquals(testPlaceList.get(i).getOpenTime(), list.get(i)
                    .getOpenTime());
            assertEquals(testPlaceList.get(i).getCloseTime(), list.get(i)
                    .getCloseTime());
            assertEquals(testPlaceList.get(i).getSpecialInfo(), list.get(i)
                    .getSpecialInfo());
            assertEquals(testPlaceList.get(i).getNickname(), list.get(i)
                    .getNickname());
            assertEquals(testPlaceList.get(i).getBillCode(), list.get(i)
                    .getBillCode());
            assertEquals(testPlaceList.get(i).getAllnetId(), list.get(i)
                    .getAllnetId());
            assertEquals(testPlaceList.get(i).getCountryCode(), list.get(i)
                    .getCountryCode());
        }
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findPlaces(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindPlacesTypeTest() {
        String serial = "AZZZZZZZZZ";
        int allnetId = -99999;
        String countryCode1 = "TWN";
        String countryCode2 = "CHN";
        String gameId = "SBXX";
        String otherGameId = "SBYY";
        String billCode1 = "2Z345678";
        String billCode2 = "3Z345678";

        int i = 0;
        for (; i < 2; i++) {

            deleteMachine(serial + i);
            createTestMachine(serial + i, allnetId - i, gameId, 1, 1);
        }

        deleteMachine(serial + i);
        createTestMachine(serial + i, allnetId - i, otherGameId, 1, 1);
        i++;

        deleteMachine(serial + i);
        createTestMachine(serial + i, allnetId - 1, gameId, 1, 1);

        i = 0;
        List<Place> testPlaceList = new ArrayList<Place>();
        deletePlaceWithRelations(allnetId - i, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId - i, countryCode2, billCode2);
        Place p = createPlace(allnetId - i, countryCode1, billCode1, 1, 2, 3,
                4, i);
        createTestPlaceRelations(p);
        createTestPlace(p);
        testPlaceList.add(p);

        for (i = 1; i < 5; i++) {
            p = createPlace(allnetId - i, countryCode2, billCode2, 1, 2, 3, 4,
                    i);
            deletePlace(allnetId - i);
            if (i == 1) {
                createTestPlaceRelations(p);
                testPlaceList.add(p);
            }
            createTestPlace(p);
        }

        List<Place> list = _dao.findPlaces(gameId, 0, "test");

        assertEquals(2, list.size());

        for (i = 0; i < list.size(); i++) {
            assertNotNull(list.get(i).getPlaceId());
            assertEquals(testPlaceList.get(i).getPlaceId(), list.get(i)
                    .getPlaceId());
            assertEquals(testPlaceList.get(i).getName(), list.get(i).getName());
            assertEquals(testPlaceList.get(i).getTel(), list.get(i).getTel());
            assertEquals(testPlaceList.get(i).getAddress(), list.get(i)
                    .getAddress());
            assertEquals(testPlaceList.get(i).getZipCode(), list.get(i)
                    .getZipCode());
            assertEquals(testPlaceList.get(i).getRegion0Id(), list.get(i)
                    .getRegion0Id());
            assertEquals(testPlaceList.get(i).getRegion1Id(), list.get(i)
                    .getRegion1Id());
            assertEquals(testPlaceList.get(i).getRegion2Id(), list.get(i)
                    .getRegion2Id());
            assertEquals(testPlaceList.get(i).getRegion3Id(), list.get(i)
                    .getRegion3Id());
            assertEquals(testPlaceList.get(i).getStation(), list.get(i)
                    .getStation());
            assertEquals(testPlaceList.get(i).getOpenTime(), list.get(i)
                    .getOpenTime());
            assertEquals(testPlaceList.get(i).getCloseTime(), list.get(i)
                    .getCloseTime());
            assertEquals(testPlaceList.get(i).getSpecialInfo(), list.get(i)
                    .getSpecialInfo());
            assertEquals(testPlaceList.get(i).getNickname(), list.get(i)
                    .getNickname());
            assertEquals(testPlaceList.get(i).getBillCode(), list.get(i)
                    .getBillCode());
            assertEquals(testPlaceList.get(i).getAllnetId(), list.get(i)
                    .getAllnetId());
            assertEquals(testPlaceList.get(i).getCountryCode(), list.get(i)
                    .getCountryCode());
        }
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findPlaces(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindPlacesTypeReal() {
        String serial = "AZZZZZZZZZ";
        int allnetId = 99999;
        String countryCode1 = "TWN";
        String countryCode2 = "CHN";
        String gameId = "SBXX";
        String otherGameId = "SBYY";
        String billCode1 = "2Z345678";
        String billCode2 = "3Z345678";

        int i = 0;
        for (; i < 2; i++) {
            deleteMachine(serial + i);
            createTestMachine(serial + i, allnetId - i, gameId, 1, 1);
        }

        deleteMachine(serial + i);
        createTestMachine(serial + i, allnetId - i, otherGameId, 1, 1);
        i++;

        deleteMachine(serial + i);
        createTestMachine(serial + i, allnetId - 1, gameId, 1, 1);

        i = 0;
        List<Place> testPlaceList = new ArrayList<Place>();
        deletePlaceWithRelations(allnetId - i, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId - i, countryCode2, billCode2);
        Place p = createPlace(allnetId - i, countryCode1, billCode1, 1, 2, 3,
                4, i);
        createTestPlaceRelations(p);
        createTestPlace(p);
        testPlaceList.add(p);

        for (i = 1; i < 5; i++) {
            p = createPlace(allnetId - i, countryCode2, billCode2, 1, 2, 3, 4,
                    i);
            deletePlace(allnetId - i);
            if (i == 1) {
                createTestPlaceRelations(p);
                testPlaceList.add(p);
            }
            createTestPlace(p);
        }

        List<Place> list = _dao.findPlaces(gameId, 0, "real");

        assertEquals(2, list.size());

        for (i = 0; i < list.size(); i++) {
            assertNotNull(list.get(i).getPlaceId());
            assertEquals(testPlaceList.get(i).getPlaceId(), list.get(i)
                    .getPlaceId());
            assertEquals(testPlaceList.get(i).getName(), list.get(i).getName());
            assertEquals(testPlaceList.get(i).getTel(), list.get(i).getTel());
            assertEquals(testPlaceList.get(i).getAddress(), list.get(i)
                    .getAddress());
            assertEquals(testPlaceList.get(i).getZipCode(), list.get(i)
                    .getZipCode());
            assertEquals(testPlaceList.get(i).getRegion0Id(), list.get(i)
                    .getRegion0Id());
            assertEquals(testPlaceList.get(i).getRegion1Id(), list.get(i)
                    .getRegion1Id());
            assertEquals(testPlaceList.get(i).getRegion2Id(), list.get(i)
                    .getRegion2Id());
            assertEquals(testPlaceList.get(i).getRegion3Id(), list.get(i)
                    .getRegion3Id());
            assertEquals(testPlaceList.get(i).getStation(), list.get(i)
                    .getStation());
            assertEquals(testPlaceList.get(i).getOpenTime(), list.get(i)
                    .getOpenTime());
            assertEquals(testPlaceList.get(i).getCloseTime(), list.get(i)
                    .getCloseTime());
            assertEquals(testPlaceList.get(i).getSpecialInfo(), list.get(i)
                    .getSpecialInfo());
            assertEquals(testPlaceList.get(i).getNickname(), list.get(i)
                    .getNickname());
            assertEquals(testPlaceList.get(i).getBillCode(), list.get(i)
                    .getBillCode());
            assertEquals(testPlaceList.get(i).getAllnetId(), list.get(i)
                    .getAllnetId());
            assertEquals(testPlaceList.get(i).getCountryCode(), list.get(i)
                    .getCountryCode());
        }
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findPlaces(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindPlacesNoMachine() {
        String serial = "AZZZZZZZZZZ";
        int allnetId = -99999;
        String countryCode = "TWN";
        String gameId = "SBXX";
        String billCode = "2Z345678";

        Place p = createPlace(allnetId, countryCode, billCode, 1, 2, 3, 4, 1);

        deleteMachine(serial);

        deletePlaceWithRelations(allnetId, countryCode, billCode);
        createTestPlaceRelations(p);
        createTestPlace(p);

        List<Place> list = _dao.findPlaces(gameId, 0, null);

        assertEquals(0, list.size());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findPlaces(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindPlacesNoPlace() {
        String serial = "AZZZZZZZZZZ";
        int allnetId = -99999;
        String countryCode = "TWN";
        String gameId = "SBXX";
        String billCode = "2Z345678";

        deleteMachine(serial);
        createTestMachine(serial, allnetId, gameId, 1, 1);

        deletePlaceWithRelations(allnetId, countryCode, billCode);

        List<Place> list = _dao.findPlaces(gameId, 0, null);

        assertEquals(0, list.size());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findPlaces(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindPlacesNoSettingOK() {
        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AYYYYYYYYYY";
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        String countryCode = "TWN";
        String gameId = "SBXX";
        String billCode = "2Z345678";

        Place p1 = createPlace(allnetId1, countryCode, billCode, 1, 2, 3, 4, 1);
        Place p2 = createPlace(allnetId2, countryCode, billCode, 1, 2, 3, 4, 2);

        deleteMachine(serial1);
        deleteMachine(serial2);
        createTestMachine(serial1, allnetId1, gameId, 1, 2);
        createTestMachine(serial2, allnetId2, gameId, 1, 3);

        deletePlaceWithRelations(allnetId1, countryCode, billCode);
        deletePlace(allnetId2);
        createTestPlaceRelations(p1);
        createTestPlace(p1);
        createTestPlace(p2);

        List<Place> list = _dao.findPlaces(gameId, 0, null);

        assertEquals(0, list.size());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findPlaces(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindPlacesArgGameIdIsNull() {
        String serial = "AZZZZZZZZZZ";
        int allnetId = -99999;
        String countryCode = "TWN";
        String gameId = "SBXX";
        String billCode = "2Z345678";

        Place p = createPlace(allnetId, countryCode, billCode, 1, 2, 3, 4, 1);

        deleteMachine(serial);
        createTestMachine(serial, allnetId, gameId, 1, 1);

        deletePlaceWithRelations(allnetId, countryCode, billCode);
        createTestPlaceRelations(p);
        createTestPlace(p);

        List<Place> list = _dao.findPlaces(null, 0, null);

        assertEquals(0, list.size());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findPlacesByCountry(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindPlacesByCountrySuccess() {
        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AYYYYYYYYYY";
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        String countryCode1 = "TWN";
        String countryCode2 = "CHN";
        String gameId = "SBXX";
        String billCode1 = "2Z345678";
        String billCode2 = "2Z098765";

        Place p1 = createPlace(allnetId1, countryCode1, billCode1, 1, 2, 3, 4,
                1);
        Place p2 = createPlace(allnetId2, countryCode2, billCode2, 1, 2, 3, 4,
                2);

        deleteMachine(serial1);
        deleteMachine(serial2);
        createTestMachine(serial1, allnetId1, gameId, 1, 1);
        createTestMachine(serial2, allnetId2, gameId, 1, 1);

        deletePlaceWithRelations(allnetId1, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId2, countryCode2, billCode2);
        createTestPlaceRelations(p1);
        createTestPlace(p1);
        createTestPlaceRelations(p2);
        createTestPlace(p2);

        List<Place> list = _dao.findPlaces(gameId, countryCode1, 0, null);

        assertEquals(1, list.size());

        assertNotNull(list.get(0).getPlaceId());
        assertEquals(p1.getPlaceId(), list.get(0).getPlaceId());
        assertEquals(p1.getName(), list.get(0).getName());
        assertEquals(p1.getTel(), list.get(0).getTel());
        assertEquals(p1.getAddress(), list.get(0).getAddress());
        assertEquals(p1.getZipCode(), list.get(0).getZipCode());
        assertEquals(p1.getRegion0Id(), list.get(0).getRegion0Id());
        assertEquals(p1.getRegion1Id(), list.get(0).getRegion1Id());
        assertEquals(p1.getRegion2Id(), list.get(0).getRegion2Id());
        assertEquals(p1.getRegion3Id(), list.get(0).getRegion3Id());
        assertEquals(p1.getStation(), list.get(0).getStation());
        assertEquals(p1.getOpenTime(), list.get(0).getOpenTime());
        assertEquals(p1.getCloseTime(), list.get(0).getCloseTime());
        assertEquals(p1.getSpecialInfo(), list.get(0).getSpecialInfo());
        assertEquals(p1.getNickname(), list.get(0).getNickname());
        assertEquals(p1.getBillCode(), list.get(0).getBillCode());
        assertEquals(p1.getAllnetId(), list.get(0).getAllnetId());
        assertEquals(p1.getCountryCode(), list.get(0).getCountryCode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findPlacesByCountry(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindPlacesByCountryArgCountryCodeIsNull() {
        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AYYYYYYYYYY";
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        String countryCode1 = "TWN";
        String countryCode2 = "CHN";
        String gameId = "SBXX";
        String billCode1 = "2Z345678";
        String billCode2 = "2Z098765";

        Place p1 = createPlace(allnetId1, countryCode1, billCode1, 1, 2, 3, 4,
                1);
        Place p2 = createPlace(allnetId2, countryCode2, billCode2, 1, 2, 3, 4,
                2);

        deleteMachine(serial1);
        deleteMachine(serial2);
        createTestMachine(serial1, allnetId1, gameId, 1, 1);
        createTestMachine(serial2, allnetId2, gameId, 1, 1);

        deletePlaceWithRelations(allnetId1, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId2, countryCode2, billCode2);
        createTestPlaceRelations(p1);
        createTestPlace(p1);
        createTestPlaceRelations(p2);
        createTestPlace(p2);

        List<Place> list = _dao.findPlaces(gameId, null, 0, null);

        assertEquals(2, list.size());

        assertNotNull(list.get(0).getPlaceId());
        assertEquals(p1.getPlaceId(), list.get(0).getPlaceId());
        assertEquals(p1.getName(), list.get(0).getName());
        assertEquals(p1.getTel(), list.get(0).getTel());
        assertEquals(p1.getAddress(), list.get(0).getAddress());
        assertEquals(p1.getZipCode(), list.get(0).getZipCode());
        assertEquals(p1.getRegion0Id(), list.get(0).getRegion0Id());
        assertEquals(p1.getRegion1Id(), list.get(0).getRegion1Id());
        assertEquals(p1.getRegion2Id(), list.get(0).getRegion2Id());
        assertEquals(p1.getRegion3Id(), list.get(0).getRegion3Id());
        assertEquals(p1.getStation(), list.get(0).getStation());
        assertEquals(p1.getOpenTime(), list.get(0).getOpenTime());
        assertEquals(p1.getCloseTime(), list.get(0).getCloseTime());
        assertEquals(p1.getSpecialInfo(), list.get(0).getSpecialInfo());
        assertEquals(p1.getNickname(), list.get(0).getNickname());
        assertEquals(p1.getBillCode(), list.get(0).getBillCode());
        assertEquals(p1.getAllnetId(), list.get(0).getAllnetId());
        assertEquals(p1.getCountryCode(), list.get(0).getCountryCode());

        assertNotNull(list.get(1).getPlaceId());
        assertEquals(p2.getPlaceId(), list.get(1).getPlaceId());
        assertEquals(p2.getName(), list.get(1).getName());
        assertEquals(p2.getTel(), list.get(1).getTel());
        assertEquals(p2.getAddress(), list.get(1).getAddress());
        assertEquals(p2.getZipCode(), list.get(1).getZipCode());
        assertEquals(p2.getRegion0Id(), list.get(1).getRegion0Id());
        assertEquals(p2.getRegion1Id(), list.get(1).getRegion1Id());
        assertEquals(p2.getRegion2Id(), list.get(1).getRegion2Id());
        assertEquals(p2.getRegion3Id(), list.get(1).getRegion3Id());
        assertEquals(p2.getStation(), list.get(1).getStation());
        assertEquals(p2.getOpenTime(), list.get(1).getOpenTime());
        assertEquals(p2.getCloseTime(), list.get(1).getCloseTime());
        assertEquals(p2.getSpecialInfo(), list.get(1).getSpecialInfo());
        assertEquals(p2.getNickname(), list.get(1).getNickname());
        assertEquals(p2.getBillCode(), list.get(1).getBillCode());
        assertEquals(p2.getAllnetId(), list.get(1).getAllnetId());
        assertEquals(p2.getCountryCode(), list.get(1).getCountryCode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findPlacesIncludeAuthDenied(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindPlacesIncludeAuthDeniedSuccess() {
        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AYYYYYYYYYY";
        String serial3 = "AXXXXXXXXXX";
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId3 = -99997;
        String countryCode1 = "TWN";
        String countryCode2 = "CHN";
        String countryCode3 = "SGP";
        String gameId = "SBXX";
        String billCode1 = "2Z345678";
        String billCode2 = "2Z098765";
        String billCode3 = "3Z098765";

        Place p1 = createPlace(allnetId1, countryCode1, billCode1, 1, 2, 3, 4,
                1);
        Place p2 = createPlace(allnetId2, countryCode2, billCode2, 11, 12, 13,
                14, 2);
        Place p3 = createPlace(allnetId3, countryCode3, billCode3, 21, 22, 23,
                24, 3);

        deleteMachine(serial1);
        deleteMachine(serial2);
        deleteMachine(serial3);
        createTestMachine(serial1, allnetId1, gameId, 1, 1);
        createTestMachine(serial2, allnetId2, gameId, 1, 2);
        createTestMachine(serial3, allnetId3, gameId, 1, 3);

        deletePlaceWithRelations(allnetId1, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId2, countryCode2, billCode2);
        deletePlaceWithRelations(allnetId3, countryCode3, billCode3);
        createTestPlaceRelations(p1);
        createTestPlace(p1);
        createTestPlaceRelations(p2);
        createTestPlace(p2);
        createTestPlaceRelations(p3);
        createTestPlace(p3);

        List<Place> list = _dao.findPlacesIncludeAuthDenied(gameId, 0, null);

        assertEquals(3, list.size());

        assertNotNull(list.get(0).getPlaceId());
        assertEquals(p1.getPlaceId(), list.get(0).getPlaceId());
        assertEquals(p1.getName(), list.get(0).getName());
        assertEquals(p1.getTel(), list.get(0).getTel());
        assertEquals(p1.getAddress(), list.get(0).getAddress());
        assertEquals(p1.getZipCode(), list.get(0).getZipCode());
        assertEquals(p1.getRegion0Id(), list.get(0).getRegion0Id());
        assertEquals(p1.getRegion1Id(), list.get(0).getRegion1Id());
        assertEquals(p1.getRegion2Id(), list.get(0).getRegion2Id());
        assertEquals(p1.getRegion3Id(), list.get(0).getRegion3Id());
        assertEquals(p1.getStation(), list.get(0).getStation());
        assertEquals(p1.getOpenTime(), list.get(0).getOpenTime());
        assertEquals(p1.getCloseTime(), list.get(0).getCloseTime());
        assertEquals(p1.getSpecialInfo(), list.get(0).getSpecialInfo());
        assertEquals(p1.getNickname(), list.get(0).getNickname());
        assertEquals(p1.getBillCode(), list.get(0).getBillCode());
        assertEquals(p1.getAllnetId(), list.get(0).getAllnetId());
        assertEquals(p1.getCountryCode(), list.get(0).getCountryCode());

        assertNotNull(list.get(1).getPlaceId());
        assertEquals(p2.getPlaceId(), list.get(1).getPlaceId());
        assertEquals(p2.getName(), list.get(1).getName());
        assertEquals(p2.getTel(), list.get(1).getTel());
        assertEquals(p2.getAddress(), list.get(1).getAddress());
        assertEquals(p2.getZipCode(), list.get(1).getZipCode());
        assertEquals(p2.getRegion0Id(), list.get(1).getRegion0Id());
        assertEquals(p2.getRegion1Id(), list.get(1).getRegion1Id());
        assertEquals(p2.getRegion2Id(), list.get(1).getRegion2Id());
        assertEquals(p2.getRegion3Id(), list.get(1).getRegion3Id());
        assertEquals(p2.getStation(), list.get(1).getStation());
        assertEquals(p2.getOpenTime(), list.get(1).getOpenTime());
        assertEquals(p2.getCloseTime(), list.get(1).getCloseTime());
        assertEquals(p2.getSpecialInfo(), list.get(1).getSpecialInfo());
        assertEquals(p2.getNickname(), list.get(1).getNickname());
        assertEquals(p2.getBillCode(), list.get(1).getBillCode());
        assertEquals(p2.getAllnetId(), list.get(1).getAllnetId());
        assertEquals(p2.getCountryCode(), list.get(1).getCountryCode());

        assertNotNull(list.get(2).getPlaceId());
        assertEquals(p3.getPlaceId(), list.get(2).getPlaceId());
        assertEquals(p3.getName(), list.get(2).getName());
        assertEquals(p3.getTel(), list.get(2).getTel());
        assertEquals(p3.getAddress(), list.get(2).getAddress());
        assertEquals(p3.getZipCode(), list.get(2).getZipCode());
        assertEquals(p3.getRegion0Id(), list.get(2).getRegion0Id());
        assertEquals(p3.getRegion1Id(), list.get(2).getRegion1Id());
        assertEquals(p3.getRegion2Id(), list.get(2).getRegion2Id());
        assertEquals(p3.getRegion3Id(), list.get(2).getRegion3Id());
        assertEquals(p3.getStation(), list.get(2).getStation());
        assertEquals(p3.getOpenTime(), list.get(2).getOpenTime());
        assertEquals(p3.getCloseTime(), list.get(2).getCloseTime());
        assertEquals(p3.getSpecialInfo(), list.get(2).getSpecialInfo());
        assertEquals(p3.getNickname(), list.get(2).getNickname());
        assertEquals(p3.getBillCode(), list.get(2).getBillCode());
        assertEquals(p3.getAllnetId(), list.get(2).getAllnetId());
        assertEquals(p3.getCountryCode(), list.get(2).getCountryCode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findPlacesByCountryIncludeAuthDenied(java.lang.String, java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindPlacesByCountryIncludeAuthDenied() {
        String serial1 = "AZZZZZZZZZZ";
        String serial2 = "AYYYYYYYYYY";
        String serial3 = "AXXXXXXXXXX";
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId3 = -99997;
        String countryCode1 = "TWN";
        String countryCode2 = "CHN";
        String countryCode3 = "SGP";
        String gameId = "SBXX";
        String billCode1 = "2Z345678";
        String billCode2 = "2Z098765";
        String billCode3 = "3Z098765";

        Place p1 = createPlace(allnetId1, countryCode1, billCode1, 1, 2, 3, 4,
                1);
        Place p2 = createPlace(allnetId2, countryCode2, billCode2, 1, 2, 3, 4,
                2);
        Place p3 = createPlace(allnetId3, countryCode3, billCode3, 1, 2, 3, 4,
                3);

        deleteMachine(serial1);
        deleteMachine(serial2);
        deleteMachine(serial3);
        createTestMachine(serial1, allnetId1, gameId, 1, 1);
        createTestMachine(serial2, allnetId2, gameId, 1, 2);
        createTestMachine(serial3, allnetId3, gameId, 1, 3);

        deletePlaceWithRelations(allnetId1, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId2, countryCode2, billCode2);
        deletePlaceWithRelations(allnetId3, countryCode3, billCode3);
        createTestPlaceRelations(p1);
        createTestPlace(p1);
        createTestPlaceRelations(p2);
        createTestPlace(p2);
        createTestPlaceRelations(p3);
        createTestPlace(p3);

        List<Place> list = _dao.findPlacesIncludeAuthDenied(gameId,
                countryCode3, 0, null);

        assertEquals(1, list.size());

        assertNotNull(list.get(0).getPlaceId());
        assertEquals(p3.getPlaceId(), list.get(0).getPlaceId());
        assertEquals(p3.getName(), list.get(0).getName());
        assertEquals(p3.getTel(), list.get(0).getTel());
        assertEquals(p3.getAddress(), list.get(0).getAddress());
        assertEquals(p3.getZipCode(), list.get(0).getZipCode());
        assertEquals(p3.getRegion0Id(), list.get(0).getRegion0Id());
        assertEquals(p3.getRegion1Id(), list.get(0).getRegion1Id());
        assertEquals(p3.getRegion2Id(), list.get(0).getRegion2Id());
        assertEquals(p3.getRegion3Id(), list.get(0).getRegion3Id());
        assertEquals(p3.getStation(), list.get(0).getStation());
        assertEquals(p3.getOpenTime(), list.get(0).getOpenTime());
        assertEquals(p3.getCloseTime(), list.get(0).getCloseTime());
        assertEquals(p3.getSpecialInfo(), list.get(0).getSpecialInfo());
        assertEquals(p3.getNickname(), list.get(0).getNickname());
        assertEquals(p3.getBillCode(), list.get(0).getBillCode());
        assertEquals(p3.getAllnetId(), list.get(0).getAllnetId());
        assertEquals(p3.getCountryCode(), list.get(0).getCountryCode());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.lang.String, int)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsStringIntSuccessLevel0() {
        int allnetId = -99999;
        String countryCode = "TWN";
        int level = 0;
        String billCode = "2Z345678";
        int region1Id0 = 1;
        int region1Id1 = 2;
        int region1Id2 = 3;
        int region1Id3 = 4;
        int region2Id0 = 11;
        int region2Id1 = 12;
        int region2Id2 = 13;
        int region2Id3 = 14;

        Place p1 = createPlace(allnetId, countryCode, billCode, region1Id0,
                region1Id1, region1Id2, region1Id3, 1);
        Place p2 = createPlace(allnetId, countryCode, billCode, region2Id0,
                region2Id1, region2Id2, region2Id3, 2);

        deletePlaceWithRelations(allnetId, countryCode, billCode);
        createTestPlaceRelations(p1);
        createTestRegions(p2);

        List<Region> list = _dao.findRegions(countryCode, level);

        assertEquals(2, list.size());

        assertEquals(p1.getCountryCode(), list.get(0).getCountryCode());
        assertEquals(level, list.get(0).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion0Id()), list
                .get(0).getName());
        assertNull(list.get(0).getParentRegionId());
        assertEquals(region1Id0, list.get(0).getRegionId());

        assertEquals(p2.getCountryCode(), list.get(1).getCountryCode());
        assertEquals(level, list.get(1).getLevel());
        assertEquals(String.format("TestRegion%s", p2.getRegion0Id()), list
                .get(1).getName());
        assertNull(list.get(1).getParentRegionId());
        assertEquals(region2Id0, list.get(1).getRegionId());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.lang.String, int)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsStringIntSuccessLevel1() {
        int allnetId = -99999;
        String countryCode = "TWN";
        int level = 1;
        String billCode = "2Z345678";
        int region1Id0 = 1;
        int region1Id1 = 2;
        int region1Id2 = 3;
        int region1Id3 = 4;
        int region2Id0 = 11;
        int region2Id1 = 12;
        int region2Id2 = 13;
        int region2Id3 = 14;

        Place p1 = createPlace(allnetId, countryCode, billCode, region1Id0,
                region1Id1, region1Id2, region1Id3, 1);
        Place p2 = createPlace(allnetId, countryCode, billCode, region2Id0,
                region2Id1, region2Id2, region2Id3, 2);

        deletePlaceWithRelations(allnetId, countryCode, billCode);
        createTestPlaceRelations(p1);
        createTestRegions(p2);

        List<Region> list = _dao.findRegions(countryCode, level);

        assertEquals(2, list.size());

        assertEquals(p1.getCountryCode(), list.get(0).getCountryCode());
        assertEquals(level, list.get(0).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion1Id()), list
                .get(0).getName());
        assertEquals(p1.getRegion0Id(), list.get(0).getParentRegionId());
        assertEquals(region1Id1, list.get(0).getRegionId());

        assertEquals(p2.getCountryCode(), list.get(1).getCountryCode());
        assertEquals(level, list.get(1).getLevel());
        assertEquals(String.format("TestRegion%s", p2.getRegion1Id()), list
                .get(1).getName());
        assertEquals(p2.getRegion0Id(), list.get(1).getParentRegionId());
        assertEquals(region2Id1, list.get(1).getRegionId());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.lang.String, int)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsStringIntSuccessLevel2() {
        int allnetId = -99999;
        String countryCode = "TWN";
        int level = 2;
        String billCode = "2Z345678";
        int region1Id0 = 1;
        int region1Id1 = 2;
        int region1Id2 = 3;
        int region1Id3 = 4;
        int region2Id0 = 11;
        int region2Id1 = 12;
        int region2Id2 = 13;
        int region2Id3 = 14;

        Place p1 = createPlace(allnetId, countryCode, billCode, region1Id0,
                region1Id1, region1Id2, region1Id3, 1);
        Place p2 = createPlace(allnetId, countryCode, billCode, region2Id0,
                region2Id1, region2Id2, region2Id3, 2);

        deletePlaceWithRelations(allnetId, countryCode, billCode);
        createTestPlaceRelations(p1);
        createTestRegions(p2);

        List<Region> list = _dao.findRegions(countryCode, level);

        assertEquals(2, list.size());

        assertEquals(p1.getCountryCode(), list.get(0).getCountryCode());
        assertEquals(level, list.get(0).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion2Id()), list
                .get(0).getName());
        assertEquals(p1.getRegion1Id(), list.get(0).getParentRegionId());
        assertEquals(region1Id2, list.get(0).getRegionId());

        assertEquals(p2.getCountryCode(), list.get(1).getCountryCode());
        assertEquals(level, list.get(1).getLevel());
        assertEquals(String.format("TestRegion%s", p2.getRegion2Id()), list
                .get(1).getName());
        assertEquals(p2.getRegion1Id(), list.get(1).getParentRegionId());
        assertEquals(region2Id2, list.get(1).getRegionId());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.lang.String, int)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsStringIntSuccessLevel3() {
        int allnetId = -99999;
        String countryCode = "TWN";
        int level = 3;
        String billCode = "2Z345678";
        int region1Id0 = 1;
        int region1Id1 = 2;
        int region1Id2 = 3;
        int region1Id3 = 4;
        int region2Id0 = 11;
        int region2Id1 = 12;
        int region2Id2 = 13;
        int region2Id3 = 14;

        Place p1 = createPlace(allnetId, countryCode, billCode, region1Id0,
                region1Id1, region1Id2, region1Id3, 1);
        Place p2 = createPlace(allnetId, countryCode, billCode, region2Id0,
                region2Id1, region2Id2, region2Id3, 2);

        deletePlaceWithRelations(allnetId, countryCode, billCode);
        createTestPlaceRelations(p1);
        createTestRegions(p2);

        List<Region> list = _dao.findRegions(countryCode, level);

        assertEquals(2, list.size());

        assertEquals(p1.getCountryCode(), list.get(0).getCountryCode());
        assertEquals(level, list.get(0).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion3Id()), list
                .get(0).getName());
        assertEquals(p1.getRegion2Id(), list.get(0).getParentRegionId());
        assertEquals(region1Id3, list.get(0).getRegionId());

        assertEquals(p2.getCountryCode(), list.get(1).getCountryCode());
        assertEquals(level, list.get(1).getLevel());
        assertEquals(String.format("TestRegion%s", p2.getRegion3Id()), list
                .get(1).getName());
        assertEquals(p2.getRegion2Id(), list.get(1).getParentRegionId());
        assertEquals(region2Id3, list.get(1).getRegionId());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.lang.String, int)}
     * のためのテスト・メソッド。
     */

    @Test(expected = IllegalArgumentException.class)
    public final void testFindRegionsStringIntFailLevelMinus() {
        String countryCode = "TWN";
        int level = -1;

        _dao.findRegions(countryCode, level);
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.lang.String, int)}
     * のためのテスト・メソッド。
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testFindRegionsStringIntFailLevelOver() {
        String countryCode = "TWN";
        int level = 4;

        _dao.findRegions(countryCode, level);
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.lang.String, int)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsStringIntNoRegion() {
        String countryCode = "TWN";
        int level = 0;

        deleteRegions(countryCode);

        List<Region> list = _dao.findRegions(countryCode, level);

        assertEquals(0, list.size());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.lang.String, int)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsStringIntArgCountryCodeIsNull() {
        String countryCode = null;
        int level = 0;

        List<Region> list = _dao.findRegions(countryCode, level);

        assertEquals(0, list.size());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.util.List)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsListOfPlaceSuccess() {
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId3 = -99997;
        String countryCode1 = "CHN";
        String countryCode2 = "TWN";
        String countryCode3 = "USA";
        String billCode1 = "2Z345678";
        String billCode2 = "3Z345678";
        String billCode3 = "4Z345678";

        Place p1 = createPlace(allnetId1, countryCode1, billCode1, 1, 2, 3, 4,
                1);
        Place p2 = createPlace(allnetId2, countryCode2, billCode2, 11, 12, 13,
                14, 2);
        Place p3 = createPlace(allnetId3, countryCode3, billCode3, 21, 22, 23,
                24, 3);
        List<Place> places = new ArrayList<Place>();
        places.add(p1);
        places.add(p2);

        deletePlaceWithRelations(allnetId1, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId2, countryCode2, billCode2);
        deletePlaceWithRelations(allnetId3, countryCode3, billCode3);
        createTestPlaceRelations(p1);
        createTestPlaceRelations(p2);
        createTestPlaceRelations(p3);

        List<Region> list = _dao.findRegions(places);

        assertEquals(8, list.size());

        int i = 0;
        // CHN[0]
        assertEquals(p1.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion0Id()), list
                .get(i).getName());
        assertNull(list.get(i).getParentRegionId());
        assertEquals(p1.getRegion0Id().intValue(), list.get(i).getRegionId());
        i++;

        // CHN[1]
        assertEquals(p1.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion1Id()), list
                .get(i).getName());
        assertEquals(p1.getRegion0Id(), list.get(i).getParentRegionId());
        assertEquals(p1.getRegion1Id().intValue(), list.get(i).getRegionId());
        i++;

        // CHN[2]
        assertEquals(p1.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion2Id()), list
                .get(i).getName());
        assertEquals(p1.getRegion1Id(), list.get(i).getParentRegionId());
        assertEquals(p1.getRegion2Id().intValue(), list.get(i).getRegionId());
        i++;

        // CHN[3]
        assertEquals(p1.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion3Id()), list
                .get(i).getName());
        assertEquals(p1.getRegion2Id(), list.get(i).getParentRegionId());
        assertEquals(p1.getRegion3Id().intValue(), list.get(i).getRegionId());
        i++;

        // TWN[0]
        assertEquals(p2.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i - 4, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p2.getRegion0Id()), list
                .get(i).getName());
        assertNull(list.get(i).getParentRegionId());
        assertEquals(p2.getRegion0Id().intValue(), list.get(i).getRegionId());
        i++;

        // TWN[1]
        assertEquals(p2.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i - 4, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p2.getRegion1Id()), list
                .get(i).getName());
        assertEquals(p2.getRegion0Id(), list.get(i).getParentRegionId());
        assertEquals(p2.getRegion1Id().intValue(), list.get(i).getRegionId());
        i++;

        // TWN[2]
        assertEquals(p2.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i - 4, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p2.getRegion2Id()), list
                .get(i).getName());
        assertEquals(p2.getRegion1Id(), list.get(i).getParentRegionId());
        assertEquals(p2.getRegion2Id().intValue(), list.get(i).getRegionId());
        i++;

        // TWN[3]
        assertEquals(p2.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i - 4, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p2.getRegion3Id()), list
                .get(i).getName());
        assertEquals(p2.getRegion2Id(), list.get(i).getParentRegionId());
        assertEquals(p2.getRegion3Id().intValue(), list.get(i).getRegionId());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.util.List)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsListOfPlaceSuccessNoRegion() {
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId3 = -99997;
        String countryCode1 = "CHN";
        String countryCode2 = "TWN";
        String countryCode3 = "USA";
        String billCode1 = "2Z345678";
        String billCode2 = "3Z345678";
        String billCode3 = "4Z345678";

        Place p1 = createPlace(allnetId1, countryCode1, billCode1, 1, 2, 3, 4,
                1);
        Place p2 = createPlace(allnetId2, countryCode2, billCode2, 11, 12, 13,
                14, 2);
        Place p3 = createPlace(allnetId3, countryCode3, billCode3, 21, 22, 23,
                24, 3);
        List<Place> places = new ArrayList<Place>();
        places.add(p1);
        places.add(p2);

        deletePlaceWithRelations(allnetId1, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId2, countryCode2, billCode2);
        deletePlaceWithRelations(allnetId3, countryCode3, billCode3);
        createTestCountry(p1);
        createTestCountry(p2);
        createTestCountry(p3);

        List<Region> list = _dao.findRegions(places);

        assertEquals(0, list.size());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.util.List)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsListOfPlaceSuccessNoRegion123() {
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId3 = -99997;
        String countryCode1 = "CHN";
        String countryCode2 = "TWN";
        String countryCode3 = "USA";
        String billCode1 = "2Z345678";
        String billCode2 = "3Z345678";
        String billCode3 = "4Z345678";

        Place p1 = createPlace(allnetId1, countryCode1, billCode1, 1, 2, 3, 4,
                1);
        Place p2 = createPlace(allnetId2, countryCode2, billCode2, 11, 12, 13,
                14, 2);
        Place p3 = createPlace(allnetId3, countryCode3, billCode3, 21, 22, 23,
                24, 3);
        List<Place> places = new ArrayList<Place>();
        places.add(p1);
        places.add(p2);

        deletePlaceWithRelations(allnetId1, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId2, countryCode2, billCode2);
        deletePlaceWithRelations(allnetId3, countryCode3, billCode3);
        createTestCountry(p1);
        createTestCountry(p2);
        createTestCountry(p3);
        createTestRegion0(p1);

        List<Region> list = _dao.findRegions(places);

        assertEquals(1, list.size());

        int i = 0;
        // CHN[0]
        assertEquals(p1.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion0Id()), list
                .get(i).getName());
        assertNull(list.get(i).getParentRegionId());
        assertEquals(p1.getRegion0Id().intValue(), list.get(i).getRegionId());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.util.List)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsListOfPlaceSuccessNoRegion23() {
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId3 = -99997;
        String countryCode1 = "CHN";
        String countryCode2 = "TWN";
        String countryCode3 = "USA";
        String billCode1 = "2Z345678";
        String billCode2 = "3Z345678";
        String billCode3 = "4Z345678";

        Place p1 = createPlace(allnetId1, countryCode1, billCode1, 1, 2, 3, 4,
                1);
        Place p2 = createPlace(allnetId2, countryCode2, billCode2, 11, 12, 13,
                14, 2);
        Place p3 = createPlace(allnetId3, countryCode3, billCode3, 21, 22, 23,
                24, 3);
        List<Place> places = new ArrayList<Place>();
        places.add(p1);
        places.add(p2);

        deletePlaceWithRelations(allnetId1, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId2, countryCode2, billCode2);
        deletePlaceWithRelations(allnetId3, countryCode3, billCode3);
        createTestCountry(p1);
        createTestCountry(p2);
        createTestCountry(p3);
        createTestRegion0(p1);
        createTestRegion0(p2);
        createTestRegion1(p1);

        List<Region> list = _dao.findRegions(places);

        assertEquals(3, list.size());

        int i = 0;
        // CHN[0]
        assertEquals(p1.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion0Id()), list
                .get(i).getName());
        assertNull(list.get(i).getParentRegionId());
        assertEquals(p1.getRegion0Id().intValue(), list.get(i).getRegionId());
        i++;

        // CHN[1]
        assertEquals(p1.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion1Id()), list
                .get(i).getName());
        assertEquals(p1.getRegion0Id(), list.get(i).getParentRegionId());
        assertEquals(p1.getRegion1Id().intValue(), list.get(i).getRegionId());
        i++;

        // TWN[0]
        assertEquals(p2.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(0, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p2.getRegion0Id()), list
                .get(i).getName());
        assertNull(list.get(i).getParentRegionId());
        assertEquals(p2.getRegion0Id().intValue(), list.get(i).getRegionId());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.util.List)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsListOfPlaceSuccessNoRegion3() {
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId3 = -99997;
        String countryCode1 = "CHN";
        String countryCode2 = "TWN";
        String countryCode3 = "USA";
        String billCode1 = "2Z345678";
        String billCode2 = "3Z345678";
        String billCode3 = "4Z345678";

        Place p1 = createPlace(allnetId1, countryCode1, billCode1, 1, 2, 3, 4,
                1);
        Place p2 = createPlace(allnetId2, countryCode2, billCode2, 11, 12, 13,
                14, 2);
        Place p3 = createPlace(allnetId3, countryCode3, billCode3, 21, 22, 23,
                24, 3);
        List<Place> places = new ArrayList<Place>();
        places.add(p1);
        places.add(p2);

        deletePlaceWithRelations(allnetId1, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId2, countryCode2, billCode2);
        deletePlaceWithRelations(allnetId3, countryCode3, billCode3);
        createTestCountry(p1);
        createTestCountry(p2);
        createTestCountry(p3);
        createTestRegion0(p1);
        createTestRegion0(p2);
        createTestRegion1(p1);
        createTestRegion2(p1);

        List<Region> list = _dao.findRegions(places);

        assertEquals(4, list.size());

        int i = 0;
        // CHN[0]
        assertEquals(p1.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion0Id()), list
                .get(i).getName());
        assertNull(list.get(i).getParentRegionId());
        assertEquals(p1.getRegion0Id().intValue(), list.get(i).getRegionId());
        i++;

        // CHN[1]
        assertEquals(p1.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion1Id()), list
                .get(i).getName());
        assertEquals(p1.getRegion0Id(), list.get(i).getParentRegionId());
        assertEquals(p1.getRegion1Id().intValue(), list.get(i).getRegionId());
        i++;

        // CHN[2]
        assertEquals(p1.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(i, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion2Id()), list
                .get(i).getName());
        assertEquals(p1.getRegion1Id(), list.get(i).getParentRegionId());
        assertEquals(p1.getRegion2Id().intValue(), list.get(i).getRegionId());
        i++;

        // TWN[0]
        assertEquals(p2.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(0, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p2.getRegion0Id()), list
                .get(i).getName());
        assertNull(list.get(i).getParentRegionId());
        assertEquals(p2.getRegion0Id().intValue(), list.get(i).getRegionId());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.util.List)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsListOfPlaceSuccessPlaceDuplicated() {
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        int allnetId3 = -99997;
        String countryCode1 = "CHN";
        String countryCode2 = "TWN";
        String countryCode3 = "CHN";
        String billCode1 = "2Z345678";
        String billCode2 = "3Z345678";
        String billCode3 = "4Z345678";

        Place p1 = createPlace(allnetId1, countryCode1, billCode1, 1, 2, 3, 4,
                1);
        Place p2 = createPlace(allnetId2, countryCode2, billCode2, 11, 12, 13,
                14, 2);
        Place p3 = createPlace(allnetId3, countryCode3, billCode3, 21, 22, 23,
                24, 3);
        List<Place> places = new ArrayList<Place>();
        places.add(p1);
        places.add(p2);
        places.add(p3);

        deletePlaceWithRelations(allnetId1, countryCode1, billCode1);
        deletePlaceWithRelations(allnetId2, countryCode2, billCode2);
        deletePlaceWithRelations(allnetId3, countryCode3, billCode3);
        createTestCountry(p1);
        createTestCountry(p2);
        createTestRegion0(p1);
        createTestRegion0(p2);
        createTestRegion0(p3);

        List<Region> list = _dao.findRegions(places);

        assertEquals(3, list.size());

        int i = 0;
        // CHN(1)[0]
        assertEquals(p1.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(0, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p1.getRegion0Id()), list
                .get(i).getName());
        assertNull(list.get(i).getParentRegionId());
        assertEquals(p1.getRegion0Id().intValue(), list.get(i).getRegionId());
        i++;

        // CHN(2)[0]
        assertEquals(p3.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(0, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p3.getRegion0Id()), list
                .get(i).getName());
        assertNull(list.get(i).getParentRegionId());
        assertEquals(p3.getRegion0Id().intValue(), list.get(i).getRegionId());
        i++;

        // TWN[0]
        assertEquals(p2.getCountryCode(), list.get(i).getCountryCode());
        assertEquals(0, list.get(i).getLevel());
        assertEquals(String.format("TestRegion%s", p2.getRegion0Id()), list
                .get(i).getName());
        assertNull(list.get(i).getParentRegionId());
        assertEquals(p2.getRegion0Id().intValue(), list.get(i).getRegionId());
        i++;
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.util.List)}
     * のためのテスト・メソッド。
     */
    @Test(expected = NullPointerException.class)
    public final void testFindRegionsListOfPlaceSuccessArgPlaceIsNull() {
        int allnetId1 = -99999;
        int allnetId2 = -99998;
        String countryCode1 = "CHN";
        String countryCode2 = "TWN";
        String billCode1 = "2Z345678";
        String billCode2 = "3Z345678";

        Place p1 = createPlace(allnetId1, countryCode1, billCode1, 1, 2, 3, 4,
                1);
        Place p2 = createPlace(allnetId2, countryCode2, billCode2, 11, 12, 13,
                14, 2);
        List<Place> places = new ArrayList<Place>();
        places.add(p1);
        places.add(null);
        places.add(p2);

        _dao.findRegions(places);
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.util.List)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testFindRegionsListOfPlaceSuccessArgListIsEmpty() {
        List<Region> list = _dao.findRegions(Collections.<Place> emptyList());

        assertEquals(0, list.size());
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#findRegions(java.util.List)}
     * のためのテスト・メソッド。
     */
    @Test(expected = NullPointerException.class)
    public final void testFindRegionsListOfPlaceSuccessArgListIsNull() {
        _dao.findRegions(null);
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkBillOpenAllowedGame(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckBillOpenAllowedGameSuccess() {
        String gameId = "SBXX";

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        MapSqlParameterSource params = new MapSqlParameterSource("gameId",
                gameId);
        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM games WHERE game_id = :gameId", params, Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games ( game_id, title, create_user_id, update_user_id ) "
                            + "VALUES ( :gameId, :title, :userId, :userId )",
                    params.addValue("title", "TestGame").addValue("userId",
                            "TestUser"));
        }
        params = new MapSqlParameterSource("gameId", gameId);
        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM bill_open_allowed_games WHERE game_id = :gameId",
                params, Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO bill_open_allowed_games ( game_id, create_user_id ) "
                            + "VALUES ( :gameId, :userId )",
                    params.addValue("userId", "TestUser"));
        }

        assertTrue(_dao.checkBillOpenAllowedGame(gameId));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkBillOpenAllowedGame(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckBillOpenAllowedGameSuccessNoGameId() {
        String gameId = "SBXX";

        JdbcTemplate jdbc = new JdbcTemplate(_ds);
        jdbc.update("DELETE FROM bill_open_allowed_games");

        assertFalse(_dao.checkBillOpenAllowedGame(gameId));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDaoBean#checkBillOpenAllowedGame(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public final void testCheckBillOpenAllowedGameArgGameIdIsNull() {
        String gameId = "SBXX";

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        MapSqlParameterSource params = new MapSqlParameterSource("gameId",
                gameId);
        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM games WHERE game_id = :gameId", params, Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO games ( game_id, title, create_user_id, update_user_id ) "
                            + "VALUES ( :gameId, :title, :userId, :userId )",
                    params.addValue("title", "TestGame").addValue("userId",
                            "TestUser"));
        }
        params = new MapSqlParameterSource("gameId", gameId);
        if (jdbc.queryForObject(
                "SELECT COUNT(1) FROM bill_open_allowed_games WHERE game_id = :gameId",
                params, Integer.class) < 1) {
            jdbc.update(
                    "INSERT INTO bill_open_allowed_games ( game_id, create_user_id ) "
                            + "VALUES ( :gameId, :userId )",
                    params.addValue("userId", "TestUser"));
        }

        assertFalse(_dao.checkBillOpenAllowedGame(null));
    }

    private void deleteMachine(String serial) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM machine_statuses WHERE serial = :serial", new MapSqlParameterSource("serial", serial));
        jdbc.update(
                "DELETE FROM machine_download_orders WHERE serial = :serial", new MapSqlParameterSource("serial", serial));
        jdbc.update("DELETE FROM machines WHERE serial = :serial", new MapSqlParameterSource("serial", serial));
    }

    private void deletePlaceWithRelations(int allnetId, String countryCode,
            String billCode) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        deletePlace(allnetId);
        deleteRegions(countryCode);

        jdbc.update("DELETE FROM country_download_orders", new MapSqlParameterSource());
        jdbc.update("DELETE FROM game_attributes", new MapSqlParameterSource());
        jdbc.update("DELETE FROM countries WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode));
        jdbc.update("DELETE FROM move_denied_bills", new MapSqlParameterSource());
        jdbc.update("DELETE FROM auth_denied_bills", new MapSqlParameterSource());
        jdbc.update("DELETE FROM move_denied_game_bills", new MapSqlParameterSource());
        jdbc.update("DELETE FROM auth_denied_game_bills", new MapSqlParameterSource());
        jdbc.update(
                "DELETE FROM bills WHERE bill_code = :billCode OR bill_code IN (SELECT bill_code FROM places WHERE allnet_id = :allnetId)",
                new MapSqlParameterSource("billCode", billCode).addValue(
                        "allnetId", allnetId));
    }

    private void deleteRegions(String countryCode) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM routers", new MapSqlParameterSource());
        jdbc.update("DELETE FROM auth_allowed_places", new MapSqlParameterSource());
        jdbc.update("DELETE FROM places WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode));
        jdbc.update("DELETE FROM region3 WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode));
        jdbc.update("DELETE FROM region2 WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode));
        jdbc.update("DELETE FROM region1 WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode));
        jdbc.update("DELETE FROM region0 WHERE country_code = :countryCode",
                new MapSqlParameterSource("countryCode", countryCode));

    }

    private void deletePlace(int allnetId) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update("DELETE FROM routers", new MapSqlParameterSource());
        jdbc.update("DELETE FROM auth_allowed_places", new MapSqlParameterSource());
        jdbc.update("DELETE FROM places WHERE allnet_id = :allnetId",
                new MapSqlParameterSource("allnetId", allnetId));
    }

    private void createTestPlaceRelations(Place p) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);

        createTestCountry(p);

        createTestRegions(p);

        jdbc.update(
                "INSERT INTO bills (bill_code, create_user_id) VALUES (:billCode, :userId)",
                new MapSqlParameterSource("billCode", p.getBillCode())
                        .addValue("userId", "TestUser"));

    }

    private void createTestCountry(Place p) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update(
                "INSERT INTO countries (country_code, create_user_id, update_user_id) VALUES (:countryCode, :userId, :userId)",
                new MapSqlParameterSource("countryCode", p.getCountryCode())
                        .addValue("userId", "TestUser"));
    }

    private void createTestRegions(Place p) {
        createTestRegion0(p);
        createTestRegion1(p);
        createTestRegion2(p);
        createTestRegion3(p);
    }

    private void createTestRegion0(Place p) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);

        jdbc.update(
                "INSERT INTO region0 (country_code, region_id, name, create_user_id, update_user_id) VALUES (:countryCode, :regionId, :name, :userId, :userId)",
                new MapSqlParameterSource("countryCode", p.getCountryCode())
                        .addValue("regionId", p.getRegion0Id())
                        .addValue("name",
                                String.format("TestRegion%s", p.getRegion0Id()))
                        .addValue("userId", "TestUser"));
    }

    private void createTestRegion1(Place p) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);

        jdbc.update(
                "INSERT INTO region1 (country_code, region_id, name, parent_region_id, create_user_id, update_user_id) VALUES (:countryCode, :regionId, :name, :parentRegionId, :userId, :userId)",
                new MapSqlParameterSource("countryCode", p.getCountryCode())
                        .addValue("regionId", p.getRegion1Id())
                        .addValue("name",
                                String.format("TestRegion%s", p.getRegion1Id()))
                        .addValue("parentRegionId", p.getRegion0Id())
                        .addValue("userId", "TestUser"));
    }

    private void createTestRegion2(Place p) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);

        jdbc.update(
                "INSERT INTO region2 (country_code, region_id, name, parent_region_id, create_user_id, update_user_id) VALUES (:countryCode, :regionId, :name, :parentRegionId, :userId, :userId)",
                new MapSqlParameterSource("countryCode", p.getCountryCode())
                        .addValue("regionId", p.getRegion2Id())
                        .addValue("name",
                                String.format("TestRegion%s", p.getRegion2Id()))
                        .addValue("parentRegionId", p.getRegion1Id())
                        .addValue("userId", "TestUser"));
    }

    private void createTestRegion3(Place p) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);

        jdbc.update(
                "INSERT INTO region3 (country_code, region_id, name, parent_region_id, create_user_id, update_user_id) VALUES (:countryCode, :regionId, :name, :parentRegionId, :userId, :userId)",
                new MapSqlParameterSource("countryCode", p.getCountryCode())
                        .addValue("regionId", p.getRegion3Id())
                        .addValue("name",
                                String.format("TestRegion%s", p.getRegion3Id()))
                        .addValue("parentRegionId", p.getRegion2Id())
                        .addValue("userId", "TestUser"));
    }

    private void createTestPlace(Place p) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update(
                "INSERT INTO places (allnet_id, place_id, name, tel, address, zip_code, region0_id, region1_id, region2_id, region3_id, station, open_time, "
                        + "close_time, special_info, bill_code, nickname, country_code, create_user_id, update_user_id) "
                        + "VALUES (:allnetId, :placeId, :name, :tel, :address, :zipCode, :region0Id, :region1Id, :region2Id, :region3Id, :station, :openTime, "
                        + ":closeTime, :specialInfo, :billCode, :nickname, :countryCode, :userId, :userId)",
                new MapSqlParameterSource("countryCode", p.getCountryCode())
                        .addValue("userId", "TestUser")
                        .addValue("allnetId", p.getAllnetId())
                        .addValue("placeId", p.getPlaceId())
                        .addValue("name", p.getName())
                        .addValue("tel", p.getTel())
                        .addValue("address", p.getAddress())
                        .addValue("zipCode", p.getZipCode())
                        .addValue("station", p.getStation())
                        .addValue("openTime", p.getOpenTime())
                        .addValue("closeTime", p.getCloseTime())
                        .addValue("specialInfo", p.getSpecialInfo())
                        .addValue("billCode", p.getBillCode())
                        .addValue("nickname", p.getNickname())
                        .addValue("countryCode", p.getCountryCode())
                        .addValue("region0Id", p.getRegion0Id())
                        .addValue("region1Id", p.getRegion1Id())
                        .addValue("region2Id", p.getRegion2Id())
                        .addValue("region3Id", p.getRegion3Id()));
    }

    private void createTestMachine(String serial, int allnetId, String gameId,
            int groupIndex, int setting) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update(
                "INSERT INTO machines (serial, allnet_id, game_id, group_index, setting, create_user_id, update_user_id) VALUES (:serial, :allnetId, :gameId, :groupIndex, :setting, :userId, :userId)",
                new MapSqlParameterSource("serial", serial)
                        .addValue("allnetId", allnetId)
                        .addValue("gameId", gameId)
                        .addValue("groupIndex", groupIndex)
                        .addValue("setting", setting)
                        .addValue("userId", "TestUser"));
    }

    private void createTestMachineStatuses(String serial, Date lastAccess) {
    	NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(_ds);
        jdbc.update(
                "INSERT INTO machine_statuses (serial, last_access, hops, create_user_id, update_user_id) VALUES (:serial, :lastAccess, :hops, :userId, :userId)",
                new MapSqlParameterSource("serial", serial)
                        .addValue("lastAccess", lastAccess).addValue("hops", 2)
                        .addValue("userId", "TestUser"));
    }

    private Place createPlace(int allnetId, String countryCode,
            String billCode, int region0, int region1, int region2,
            int region3, int counter) {
        Place p = new Place();
        p.setAllnetId(allnetId);
        p.setPlaceId("XXX" + counter);
        p.setName("テスト店舗" + counter);
        p.setTel("123456789" + counter);
        p.setAddress("テスト住所" + counter);
        p.setZipCode("123456" + counter);
        p.setRegion0Id(new BigDecimal(region0));
        p.setRegion1Id(new BigDecimal(region1));
        p.setRegion2Id(new BigDecimal(region2));
        p.setRegion3Id(new BigDecimal(region3));
        p.setStation("テスト駅への道案内" + counter);
        p.setOpenTime("10:0" + counter);
        p.setCloseTime("21:0" + counter);
        p.setSpecialInfo("テストPR文" + counter);
        p.setNickname("テストニックネーム" + counter);
        p.setBillCode(billCode);
        p.setCountryCode(countryCode);
        return p;
    }

}
