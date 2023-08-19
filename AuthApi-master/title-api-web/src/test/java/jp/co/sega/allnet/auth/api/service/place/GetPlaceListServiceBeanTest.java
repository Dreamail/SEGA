/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.place;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.ApiAccountDao;
import jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDao;
import jp.co.sega.allnet.auth.api.domain.Place;
import jp.co.sega.allnet.auth.api.service.AuthenticationException;
import jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableService;
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
 * @author TsuboiY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
public class GetPlaceListServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(GetPlaceListServiceBeanTest.class);

    @Resource(name = "getPlaceListService")
    private GetPlaceListService _service;

    @Resource(name = "apiAccountDao")
    private ApiAccountDao _apiAccountDao;

    @Resource(name = "placeRegionTableDao")
    private PlaceRegionTableDao _placeRegionTableDao;

    @After
    public void after() {
        reset(_apiAccountDao);
        reset(_placeRegionTableDao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.place.GetPlaceListServiceBean#getPlaceList(java.lang.String, java.lang.String, boolean, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetPlaceList() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean allMachine = false;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));

        // GetPlaceListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.checkBillOpenAllowedGame(eq(gameId)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq(countryCode),
                        eq(0), (String) eq(null))).andReturn(places);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getPlaceList(gameId, password, countryCode,
                allMachine, 0, null);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
				case 1:
					assertEquals(18, line.length);
					Place p = places.get(i);
					assertEquals(p.getPlaceId().replaceAll("\r|\n", ""), line[0]);
					assertEquals(PlaceRegionTableService.DUMMY_PLACE_IP, line[1]);
					assertEquals(p.getName().replaceAll("\r|\n", ""), line[2]);
					assertEquals(p.getTel().replaceAll("\r|\n", ""), line[3]);
					assertEquals(p.getAddress().replaceAll("\r|\n", ""), line[4]);
					assertEquals(p.getZipCode().replaceAll("\r|\n", ""), line[5]);
					assertEquals(p.getRegion0Id().toString(), line[6]);
					assertEquals(p.getRegion1Id().toString(), line[7]);
					assertEquals(p.getRegion2Id().toString(), line[8]);
					assertEquals(p.getRegion3Id().toString(), line[9]);
					assertEquals(p.getStation().replaceAll("\r|\n", ""), line[10]);
					assertEquals(p.getOpenTime().replaceAll("\r|\n", ""), line[11]);
					assertEquals(p.getCloseTime().replaceAll("\r|\n", ""), line[12]);
					assertEquals(p.getSpecialInfo().replaceAll("\r|\n", ""), line[13]);
					assertEquals(p.getNickname().replaceAll("\r|\n", ""), line[14]);
					assertEquals(p.getBillCode().replaceAll("\r|\n", ""), line[15]);
					assertEquals(String.valueOf(p.getAllnetId()), line[16]);
					assertEquals(p.getCountryCode().replaceAll("\r|\n", ""), line[17]);
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
     * {@link jp.co.sega.allnet.auth.api.service.place.GetPlaceListServiceBean#getPlaceList(java.lang.String, java.lang.String, boolean, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetPlaceListBillOpenNotAllowed() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean allMachine = false;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));

        // GetPlaceListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.checkBillOpenAllowedGame(eq(gameId)))
                .andReturn(false);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq(countryCode),
                        eq(0), (String) eq(null))).andReturn(places);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getPlaceList(gameId, password, countryCode,
                allMachine, 0, null);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
				case 1:
					assertEquals(18, line.length);
					Place p = places.get(i);
					assertEquals(p.getPlaceId().replaceAll("\r|\n", ""), line[0]);
					assertEquals(PlaceRegionTableService.DUMMY_PLACE_IP, line[1]);
					assertEquals(p.getName().replaceAll("\r|\n", ""), line[2]);
					assertEquals(p.getTel().replaceAll("\r|\n", ""), line[3]);
					assertEquals(p.getAddress().replaceAll("\r|\n", ""), line[4]);
					assertEquals(p.getZipCode().replaceAll("\r|\n", ""), line[5]);
					assertEquals(p.getRegion0Id().toString(), line[6]);
					assertEquals(p.getRegion1Id().toString(), line[7]);
					assertEquals(p.getRegion2Id().toString(), line[8]);
					assertEquals(p.getRegion3Id().toString(), line[9]);
					assertEquals(p.getStation().replaceAll("\r|\n", ""), line[10]);
					assertEquals(p.getOpenTime().replaceAll("\r|\n", ""), line[11]);
					assertEquals(p.getCloseTime().replaceAll("\r|\n", ""), line[12]);
					assertEquals(p.getSpecialInfo().replaceAll("\r|\n", ""), line[13]);
					assertEquals(p.getNickname().replaceAll("\r|\n", ""), line[14]);
					assertEquals("DUMMY", line[15]);
					assertEquals(String.valueOf(p.getAllnetId()), line[16]);
					assertEquals(p.getCountryCode().replaceAll("\r|\n", ""), line[17]);
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
     * {@link jp.co.sega.allnet.auth.api.service.place.GetPlaceListServiceBean#getPlaceList(java.lang.String, java.lang.String, boolean, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetPlaceListIsAllMachine() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean allMachine = true;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));
        places.add(createPlaceOpenTimeNull(-99997, countryCode, "4Z3456789",
                11, 12, 13, 14, 2));

        // GetPlaceListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.checkBillOpenAllowedGame(eq(gameId)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlacesIncludeAuthDenied(eq(gameId),
                        eq(countryCode), eq(0), (String) eq(null))).andReturn(
                places);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getPlaceList(gameId, password, countryCode,
                allMachine, 0, null);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
				case 1:
					assertEquals(18, line.length);
					Place p = places.get(i);
					assertEquals(p.getPlaceId().replaceAll("\r|\n", ""), line[0]);
					assertEquals(PlaceRegionTableService.DUMMY_PLACE_IP, line[1]);
					assertEquals(p.getName().replaceAll("\r|\n", ""), line[2]);
					assertEquals(p.getTel().replaceAll("\r|\n", ""), line[3]);
					assertEquals(p.getAddress().replaceAll("\r|\n", ""), line[4]);
					assertEquals(p.getZipCode().replaceAll("\r|\n", ""), line[5]);
					assertEquals(p.getRegion0Id().toString(), line[6]);
					assertEquals(p.getRegion1Id().toString(), line[7]);
					assertEquals(p.getRegion2Id().toString(), line[8]);
					assertEquals(p.getRegion3Id().toString(), line[9]);
					assertEquals(p.getStation().replaceAll("\r|\n", ""), line[10]);
					assertEquals(p.getOpenTime().replaceAll("\r|\n", ""), line[11]);
					assertEquals(p.getCloseTime().replaceAll("\r|\n", ""), line[12]);
					assertEquals(p.getSpecialInfo().replaceAll("\r|\n", ""), line[13]);
					assertEquals(p.getNickname().replaceAll("\r|\n", ""), line[14]);
					assertEquals(p.getBillCode().replaceAll("\r|\n", ""), line[15]);
					assertEquals(String.valueOf(p.getAllnetId()), line[16]);
					assertEquals(p.getCountryCode().replaceAll("\r|\n", ""), line[17]);
					break;
				case 2:
					assertEquals(18, line.length);
					p = places.get(i);
					assertEquals(p.getPlaceId().replaceAll("\r|\n", ""), line[0]);
					assertEquals(PlaceRegionTableService.DUMMY_PLACE_IP, line[1]);
					assertEquals(p.getName().replaceAll("\r|\n", ""), line[2]);
					assertEquals(p.getTel().replaceAll("\r|\n", ""), line[3]);
					assertEquals(p.getAddress().replaceAll("\r|\n", ""), line[4]);
					assertEquals(p.getZipCode().replaceAll("\r|\n", ""), line[5]);
					assertEquals(p.getRegion0Id().toString(), line[6]);
					assertEquals(p.getRegion1Id().toString(), line[7]);
					assertEquals(p.getRegion2Id().toString(), line[8]);
					assertEquals(p.getRegion3Id().toString(), line[9]);
					assertEquals(p.getStation().replaceAll("\r|\n", ""), line[10]);
					assertEquals("", line[11]);
					assertEquals(p.getCloseTime().replaceAll("\r|\n", ""), line[12]);
					assertEquals(p.getSpecialInfo().replaceAll("\r|\n", ""), line[13]);
					assertEquals(p.getNickname().replaceAll("\r|\n", ""), line[14]);
					assertEquals(p.getBillCode().replaceAll("\r|\n", ""), line[15]);
					assertEquals(String.valueOf(p.getAllnetId()), line[16]);
					assertEquals(p.getCountryCode().replaceAll("\r|\n", ""), line[17]);
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
     * {@link jp.co.sega.allnet.auth.api.service.place.GetPlaceListServiceBean#getPlaceList(java.lang.String, java.lang.String, boolean, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetPlaceListNoPlaces() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean allMachine = false;

        List<Place> places = new ArrayList<Place>();

        // GetPlaceListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.checkBillOpenAllowedGame(eq(gameId)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq(countryCode),
                        eq(0), (String) eq(null))).andReturn(places);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getPlaceList(gameId, password, countryCode,
                allMachine, 0, null);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
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
     * {@link jp.co.sega.allnet.auth.api.service.place.GetPlaceListServiceBean#getPlaceList(java.lang.String, java.lang.String, boolean, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetPlaceListCountryIsEmpty() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "";
        boolean allMachine = false;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, "TWN", "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, "TWN", "3Z3456789", 11, 12, 13, 14, 1));

        // GetPlaceListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.checkBillOpenAllowedGame(eq(gameId)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq("JPN"), eq(0),
                        (String) eq(null))).andReturn(places);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getPlaceList(gameId, password, countryCode,
                allMachine, 0, null);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
				case 1:
					assertEquals(18, line.length);
					Place p = places.get(i);
					assertEquals(p.getPlaceId().replaceAll("\r|\n", ""), line[0]);
					assertEquals(PlaceRegionTableService.DUMMY_PLACE_IP, line[1]);
					assertEquals(p.getName().replaceAll("\r|\n", ""), line[2]);
					assertEquals(p.getTel().replaceAll("\r|\n", ""), line[3]);
					assertEquals(p.getAddress().replaceAll("\r|\n", ""), line[4]);
					assertEquals(p.getZipCode().replaceAll("\r|\n", ""), line[5]);
					assertEquals(p.getRegion0Id().toString(), line[6]);
					assertEquals(p.getRegion1Id().toString(), line[7]);
					assertEquals(p.getRegion2Id().toString(), line[8]);
					assertEquals(p.getRegion3Id().toString(), line[9]);
					assertEquals(p.getStation().replaceAll("\r|\n", ""), line[10]);
					assertEquals(p.getOpenTime().replaceAll("\r|\n", ""), line[11]);
					assertEquals(p.getCloseTime().replaceAll("\r|\n", ""), line[12]);
					assertEquals(p.getSpecialInfo().replaceAll("\r|\n", ""), line[13]);
					assertEquals(p.getNickname().replaceAll("\r|\n", ""), line[14]);
					assertEquals(p.getBillCode().replaceAll("\r|\n", ""), line[15]);
					assertEquals(String.valueOf(p.getAllnetId()), line[16]);
					assertEquals(p.getCountryCode().replaceAll("\r|\n", ""), line[17]);
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
     * {@link jp.co.sega.allnet.auth.api.service.place.GetPlaceListServiceBean#getPlaceList(java.lang.String, java.lang.String, boolean, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetPlaceListCountryIsNull() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = null;
        boolean allMachine = false;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, "JPN", "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, "JPN", "3Z3456789", 11, 12, 13, 14, 1));

        // GetPlaceListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.checkBillOpenAllowedGame(eq(gameId)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq("JPN"), eq(0),
                        (String) eq(null))).andReturn(places);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getPlaceList(gameId, password, countryCode,
                allMachine, 0, null);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
				case 1:
					assertEquals(18, line.length);
					Place p = places.get(i);
					assertEquals(p.getPlaceId().replaceAll("\r|\n", ""), line[0]);
					assertEquals(PlaceRegionTableService.DUMMY_PLACE_IP, line[1]);
					assertEquals(p.getName().replaceAll("\r|\n", ""), line[2]);
					assertEquals(p.getTel().replaceAll("\r|\n", ""), line[3]);
					assertEquals(p.getAddress().replaceAll("\r|\n", ""), line[4]);
					assertEquals(p.getZipCode().replaceAll("\r|\n", ""), line[5]);
					assertEquals(p.getRegion0Id().toString(), line[6]);
					assertEquals(p.getRegion1Id().toString(), line[7]);
					assertEquals(p.getRegion2Id().toString(), line[8]);
					assertEquals(p.getRegion3Id().toString(), line[9]);
					assertEquals(p.getStation().replaceAll("\r|\n", ""), line[10]);
					assertEquals(p.getOpenTime().replaceAll("\r|\n", ""), line[11]);
					assertEquals(p.getCloseTime().replaceAll("\r|\n", ""), line[12]);
					assertEquals(p.getSpecialInfo().replaceAll("\r|\n", ""), line[13]);
					assertEquals(p.getNickname().replaceAll("\r|\n", ""), line[14]);
					assertEquals(p.getBillCode().replaceAll("\r|\n", ""), line[15]);
					assertEquals(String.valueOf(p.getAllnetId()), line[16]);
					assertEquals(p.getCountryCode().replaceAll("\r|\n", ""), line[17]);
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
     * {@link jp.co.sega.allnet.auth.api.service.place.GetPlaceListServiceBean#getPlaceList(java.lang.String, java.lang.String, boolean, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test(expected = AuthenticationException.class)
    public final void testGetPlaceListAuthFailed() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean allMachine = false;

        // GetPlaceListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.NG);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        _service.getPlaceList(gameId, password, countryCode, allMachine, 0,
                null);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.place.GetPlaceListServiceBean#getPlaceList(java.lang.String, java.lang.String, boolean, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test(expected = AuthenticationException.class)
    public final void testGetPlaceListArgGameIdIsNull() throws IOException,
            AuthenticationException {

        String gameId = null;
        String password = "pass";
        String countryCode = "TWN";
        boolean allMachine = false;

        // GetPlaceListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.NG);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        _service.getPlaceList(gameId, password, countryCode, allMachine, 0,
                null);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.place.GetPlaceListServiceBean#getPlaceList(java.lang.String, java.lang.String, boolean, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test(expected = AuthenticationException.class)
    public final void testGetPlaceListArgPasswordIsNull() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = null;
        String countryCode = "TWN";
        boolean allMachine = false;

        // GetPlaceListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.NG);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        _service.getPlaceList(gameId, password, countryCode, allMachine, 0,
                null);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.place.GetPlaceListServiceBean#getPlaceList(java.lang.String, java.lang.String, boolean, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetPlaceListPlaceAll() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean allMachine = false;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));

        // GetPlaceListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK_PLACE_ALL);
        EasyMock.expect(
                _placeRegionTableDao.checkBillOpenAllowedGame(eq(gameId)))
                .andReturn(true);
        EasyMock.expect(_placeRegionTableDao.findPlacesAll()).andReturn(places);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getPlaceList(gameId, password, countryCode,
                allMachine, 0, null);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
				case 1:
					assertEquals(18, line.length);
					Place p = places.get(i);
					assertEquals(p.getPlaceId().replaceAll("\r|\n", ""), line[0]);
					assertEquals(PlaceRegionTableService.DUMMY_PLACE_IP, line[1]);
					assertEquals(p.getName().replaceAll("\r|\n", ""), line[2]);
					assertEquals(p.getTel().replaceAll("\r|\n", ""), line[3]);
					assertEquals(p.getAddress().replaceAll("\r|\n", ""), line[4]);
					assertEquals(p.getZipCode().replaceAll("\r|\n", ""), line[5]);
					assertEquals(p.getRegion0Id().toString(), line[6]);
					assertEquals(p.getRegion1Id().toString(), line[7]);
					assertEquals(p.getRegion2Id().toString(), line[8]);
					assertEquals(p.getRegion3Id().toString(), line[9]);
					assertEquals(p.getStation().replaceAll("\r|\n", ""), line[10]);
					assertEquals(p.getOpenTime().replaceAll("\r|\n", ""), line[11]);
					assertEquals(p.getCloseTime().replaceAll("\r|\n", ""), line[12]);
					assertEquals(p.getSpecialInfo().replaceAll("\r|\n", ""), line[13]);
					assertEquals(p.getNickname().replaceAll("\r|\n", ""), line[14]);
					assertEquals(p.getBillCode().replaceAll("\r|\n", ""), line[15]);
					assertEquals(String.valueOf(p.getAllnetId()), line[16]);
					assertEquals(p.getCountryCode().replaceAll("\r|\n", ""), line[17]);
					break;
				default:
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    private Place createPlace(int allnetId, String countryCode,
            String billCode, Integer region0, Integer region1, Integer region2,
            Integer region3, int counter) {
        Place p = new Place();
        p.setAllnetId(allnetId);
        p.setPlaceId("XXX" + counter);
        p.setName("テスト,店舗" + counter);
        p.setTel("123456789" + counter);
        p.setAddress("テス\"ト\"住所" + counter);
        p.setZipCode("123456" + counter);
        if (region0 == null) {
            p.setRegion0Id(null);
        } else {
            p.setRegion0Id(new BigDecimal(region0));
        }
        if (region1 == null) {
            p.setRegion1Id(null);
        } else {
            p.setRegion1Id(new BigDecimal(region1));
        }
        if (region2 == null) {
            p.setRegion2Id(null);
        } else {
            p.setRegion2Id(new BigDecimal(region2));
        }
        if (region3 == null) {
            p.setRegion3Id(null);
        } else {
            p.setRegion3Id(new BigDecimal(region3));
        }
        p.setStation("テスト駅\nへの道案内" + counter);
        p.setOpenTime("10:0" + counter);
        p.setCloseTime("21:0" + counter);
        p.setSpecialInfo("テストPR文" + counter);
        p.setNickname("テストニックネーム" + counter);
        p.setBillCode(billCode);
        p.setCountryCode(countryCode);
        return p;
    }

    private Place createPlaceOpenTimeNull(int allnetId, String countryCode,
            String billCode, int region0, int region1, int region2,
            int region3, int counter) {
        Place p = new Place();
        p.setAllnetId(allnetId);
        p.setPlaceId("XXX" + counter);
        p.setName("テスト,店舗" + counter);
        p.setTel("123456789" + counter);
        p.setAddress("テス\"ト\"住所" + counter);
        p.setZipCode("123456" + counter);
        p.setRegion0Id(new BigDecimal(region0));
        p.setRegion1Id(new BigDecimal(region1));
        p.setRegion2Id(new BigDecimal(region2));
        p.setRegion3Id(new BigDecimal(region3));
        p.setStation("テスト駅\nへの道案内" + counter);
        p.setCloseTime("21:0" + counter);
        p.setSpecialInfo("テストPR文" + counter);
        p.setNickname("テストニックネーム" + counter);
        p.setBillCode(billCode);
        p.setCountryCode(countryCode);
        return p;
    }

}
