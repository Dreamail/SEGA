/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.region;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import jp.co.sega.allnet.auth.api.domain.Region;
import jp.co.sega.allnet.auth.api.service.AuthenticationException;
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
public class GetRegionListServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(GetRegionListServiceBeanTest.class);

    @Resource(name = "getRegionListService")
    private GetRegionListService _service;

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
     * {@link jp.co.sega.allnet.auth.api.service.region.GetRegionListServiceBean#getRgionList(java.lang.String, java.lang.String, boolean, java.lang.String)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetRegionList() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));

        List<Region> regions = new ArrayList<Region>();
        regions.add(createRegion(1, null, "地域\"1,1\"\n", 0, countryCode));
        regions.add(createRegion(11, null, "地域\"2,1\"\n", 0, countryCode));
        regions.add(createRegion(2, 1, "地域\"1,2\"\n", 1, countryCode));
        regions.add(createRegion(12, 11, "地域\"2,2\"\n", 1, countryCode));
        regions.add(createRegion(3, 2, "地域\"1,3\"\n", 2, countryCode));
        regions.add(createRegion(13, 12, "地域\"2,3\"\n", 2, countryCode));
        regions.add(createRegion(4, 3, "地域\"1,4\"\n", 3, countryCode));
        regions.add(createRegion(14, 13, "地域\"2,4\"\n", 3, countryCode));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.findPlacesIncludeAuthDenied(eq(gameId),
                        eq(countryCode), eq(0), (String) eq(null))).andReturn(
                places);
        EasyMock.expect(_placeRegionTableDao.findRegions(eq(places)))
                .andReturn(regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getRegionList(gameId, password, countryCode);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				if (i >= 0 && i < regions.size() + 1) {
					assertEquals(5, line.length);
					Region r = regions.get(i);
					assertEquals(r.getCountryCode(), line[0]);
					assertEquals(String.valueOf(r.getRegionId()), line[1]);
					assertEquals(String.valueOf(r.getLevel()), line[2]);
					BigDecimal b = r.getParentRegionId();
					if (b == null) {
						b = new BigDecimal(0);
					}
					assertEquals(b.toString(), line[3]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[4]);
				} else {
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetRegionListAllRegion() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));
        places.add(createPlaceOpenTimeNull(-99997, countryCode, "4Z3456789",
                11, 12, 13, 14, 2));

        List<Region> regions = new ArrayList<Region>();
        regions.add(createRegion(1, null, "地域\"1,1\"\n", 0, countryCode));
        regions.add(createRegion(11, null, "地域\"2,1\"\n", 0, countryCode));
        regions.add(createRegion(2, 1, "地域\"1,2\"\n", 1, countryCode));
        regions.add(createRegion(12, 11, "地域\"2,2\"\n", 1, countryCode));
        regions.add(createRegion(3, 2, "地域\"1,3\"\n", 2, countryCode));
        regions.add(createRegion(13, 12, "地域\"2,3\"\n", 2, countryCode));
        regions.add(createRegion(4, 3, "地域\"1,4\"\n", 3, countryCode));
        regions.add(createRegion(14, 13, "地域\"2,4\"\n", 3, countryCode));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.findPlacesIncludeAuthDenied(eq(gameId),
                        eq(countryCode), eq(0), (String) eq(null))).andReturn(
                places);
        EasyMock.expect(_placeRegionTableDao.findRegions(eq(places)))
                .andReturn(regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getRegionList(gameId, password, countryCode);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				if (i >= 0 && i < regions.size() + 1) {
					assertEquals(5, line.length);
					Region r = regions.get(i);
					assertEquals(r.getCountryCode(), line[0]);
					assertEquals(String.valueOf(r.getRegionId()), line[1]);
					assertEquals(String.valueOf(r.getLevel()), line[2]);
					BigDecimal b = r.getParentRegionId();
					if (b == null) {
						b = new BigDecimal(0);
					}
					assertEquals(b.toString(), line[3]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[4]);
				} else {
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetRegionListNoRegion() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));

        List<Region> regions = new ArrayList<Region>();

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.findPlacesIncludeAuthDenied(eq(gameId),
                        eq(countryCode), eq(0), (String) eq(null))).andReturn(
                places);
        EasyMock.expect(_placeRegionTableDao.findRegions(eq(places)))
                .andReturn(regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getRegionList(gameId, password, countryCode);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetRegionListParcialRegion() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));

        List<Region> regions = new ArrayList<Region>();
        regions.add(createRegion(1, null, "地域\"1,1\"\n", 0, countryCode));
        regions.add(createRegion(11, null, "地域\"2,1\"\n", 0, countryCode));
        regions.add(createRegion(2, 1, "地域\"1,2\"\n", 1, countryCode));
        regions.add(createRegion(12, 11, "地域\"2,2\"\n", 1, countryCode));
        regions.add(createRegion(3, 2, "地域\"1,3\"\n", 2, countryCode));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.findPlacesIncludeAuthDenied(eq(gameId),
                        eq(countryCode), eq(0), (String) eq(null))).andReturn(
                places);
        EasyMock.expect(_placeRegionTableDao.findRegions(eq(places)))
                .andReturn(regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getRegionList(gameId, password, countryCode);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				if (i >= 0 && i < regions.size() + 1) {
					assertEquals(5, line.length);
					Region r = regions.get(i);
					assertEquals(r.getCountryCode(), line[0]);
					assertEquals(String.valueOf(r.getRegionId()), line[1]);
					assertEquals(String.valueOf(r.getLevel()), line[2]);
					BigDecimal b = r.getParentRegionId();
					if (b == null) {
						b = new BigDecimal(0);
					}
					assertEquals(b.toString(), line[3]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[4]);
				} else {
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetRegionListNoPlaces() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";

        List<Place> places = new ArrayList<Place>();

        List<Region> regions = new ArrayList<Region>();

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.findPlacesIncludeAuthDenied(eq(gameId),
                        eq(countryCode), eq(0), (String) eq(null))).andReturn(
                places);
        EasyMock.expect(_placeRegionTableDao.findRegions(eq(places)))
                .andReturn(regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getRegionList(gameId, password, countryCode);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			assertNull(reader.readNext());
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetRegionListCountryIsEmpty() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "";

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, "JPN", "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, "JPN", "3Z3456789", 11, 12, 13, 14, 1));

        List<Region> regions = new ArrayList<Region>();
        regions.add(createRegion(1, null, "地域\"1,1\"\n", 0, "JPN"));
        regions.add(createRegion(11, null, "地域\"2,1\"\n", 0, "JPN"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.findPlacesIncludeAuthDenied(eq(gameId),
                        eq("JPN"), eq(0), (String) eq(null))).andReturn(places);
        EasyMock.expect(_placeRegionTableDao.findRegions(eq(places)))
                .andReturn(regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getRegionList(gameId, password, countryCode);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				if (i >= 0 && i < regions.size() + 1) {
					assertEquals(5, line.length);
					Region r = regions.get(i);
					assertEquals(r.getCountryCode(), line[0]);
					assertEquals(String.valueOf(r.getRegionId()), line[1]);
					assertEquals(String.valueOf(r.getLevel()), line[2]);
					BigDecimal b = r.getParentRegionId();
					if (b == null) {
						b = new BigDecimal(0);
					}
					assertEquals(b.toString(), line[3]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[4]);
				} else {
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetRegionListCountryIsNull() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = null;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, "JPN", "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, "JPN", "3Z3456789", 11, 12, 13, 14, 1));

        List<Region> regions = new ArrayList<Region>();
        regions.add(createRegion(1, null, "地域\"1,1\"\n", 0, "JPN"));
        regions.add(createRegion(11, null, "地域\"2,1\"\n", 0, "JPN"));
        regions.add(createRegion(2, 1, "地域\"1,2\"\n", 1, "JPN"));
        regions.add(createRegion(12, 11, "地域\"2,2\"\n", 1, "JPN"));
        regions.add(createRegion(3, 2, "地域\"1,3\"\n", 2, "JPN"));
        regions.add(createRegion(13, 12, "地域\"2,3\"\n", 2, "JPN"));
        regions.add(createRegion(4, 3, "地域\"1,4\"\n", 3, "JPN"));
        regions.add(createRegion(14, 13, "地域\"2,4\"\n", 3, "JPN"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.findPlacesIncludeAuthDenied(eq(gameId),
                        eq("JPN"), eq(0), (String) eq(null))).andReturn(places);
        EasyMock.expect(_placeRegionTableDao.findRegions(eq(places)))
                .andReturn(regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getRegionList(gameId, password, countryCode);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				if (i >= 0 && i < regions.size() + 1) {
					assertEquals(5, line.length);
					Region r = regions.get(i);
					assertEquals(r.getCountryCode(), line[0]);
					assertEquals(String.valueOf(r.getRegionId()), line[1]);
					assertEquals(String.valueOf(r.getLevel()), line[2]);
					BigDecimal b = r.getParentRegionId();
					if (b == null) {
						b = new BigDecimal(0);
					}
					assertEquals(b.toString(), line[3]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[4]);
				} else {
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetRegionListCountryIsAll() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "aLl";

        String returnCountryCode1 = "TWN";
        String returnCountryCode2 = "CHN";

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, returnCountryCode1, "2Z3456789", 1, 2,
                3, 4, 0));
        places.add(createPlace(-99998, returnCountryCode2, "3Z3456789", 11, 12,
                13, 14, 1));

        List<Region> regions = new ArrayList<Region>();
        regions.add(createRegion(1, null, "地域\"1,1\"\n", 0, returnCountryCode1));
        regions.add(createRegion(11, null, "地域\"2,1\"\n", 0, returnCountryCode2));
        regions.add(createRegion(2, 1, "地域\"1,2\"\n", 1, returnCountryCode1));
        regions.add(createRegion(12, 11, "地域\"2,2\"\n", 1, returnCountryCode2));
        regions.add(createRegion(3, 2, "地域\"1,3\"\n", 2, returnCountryCode1));
        regions.add(createRegion(13, 12, "地域\"2,3\"\n", 2, returnCountryCode2));
        regions.add(createRegion(4, 3, "地域\"1,4\"\n", 3, returnCountryCode1));
        regions.add(createRegion(14, 13, "地域\"2,4\"\n", 3, returnCountryCode2));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.findPlacesIncludeAuthDenied(eq(gameId),
                        eq(0), (String) eq(null))).andReturn(places);
        EasyMock.expect(_placeRegionTableDao.findRegions(places)).andReturn(
                regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getRegionList(gameId, password, countryCode);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				if (i >= 0 && i < regions.size() + 1) {
					assertEquals(5, line.length);
					Region r = regions.get(i);
					assertEquals(r.getCountryCode(), line[0]);
					assertEquals(String.valueOf(r.getRegionId()), line[1]);
					assertEquals(String.valueOf(r.getLevel()), line[2]);
					BigDecimal b = r.getParentRegionId();
					if (b == null) {
						b = new BigDecimal(0);
					}
					assertEquals(b.toString(), line[3]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[4]);
				} else {
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test
    public final void testGetRegionListAllRegionCountryIsAll()
            throws IOException, AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "aLl";

        String returnCountryCode1 = "TWN";
        String returnCountryCode2 = "CHN";

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, returnCountryCode1, "2Z3456789", 1, 2,
                3, 4, 0));
        places.add(createPlace(-99998, returnCountryCode2, "3Z3456789", 11, 12,
                13, 14, 1));

        List<Region> regions = new ArrayList<Region>();
        regions.add(createRegion(1, null, "地域\"1,1\"\n", 0, returnCountryCode1));
        regions.add(createRegion(11, null, "地域\"2,1\"\n", 0, returnCountryCode2));
        regions.add(createRegion(2, 1, "地域\"1,2\"\n", 1, returnCountryCode1));
        regions.add(createRegion(12, 11, "地域\"2,2\"\n", 1, returnCountryCode2));
        regions.add(createRegion(3, 2, "地域\"1,3\"\n", 2, returnCountryCode1));
        regions.add(createRegion(13, 12, "地域\"2,3\"\n", 2, returnCountryCode2));
        regions.add(createRegion(4, 3, "地域\"1,4\"\n", 3, returnCountryCode1));
        regions.add(createRegion(14, 13, "地域\"2,4\"\n", 3, returnCountryCode2));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK);
        EasyMock.expect(
                _placeRegionTableDao.findPlacesIncludeAuthDenied(eq(gameId),
                        eq(0), (String) eq(null))).andReturn(places);
        EasyMock.expect(_placeRegionTableDao.findRegions(places)).andReturn(
                regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getRegionList(gameId, password, countryCode);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				if (i >= 0 && i < regions.size() + 1) {
					assertEquals(5, line.length);
					Region r = regions.get(i);
					assertEquals(r.getCountryCode(), line[0]);
					assertEquals(String.valueOf(r.getRegionId()), line[1]);
					assertEquals(String.valueOf(r.getLevel()), line[2]);
					BigDecimal b = r.getParentRegionId();
					if (b == null) {
						b = new BigDecimal(0);
					}
					assertEquals(b.toString(), line[3]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[4]);
				} else {
					fail("予期しないレスポンスが追加されています");
				}
				i++;
			}
		}
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test(expected = AuthenticationException.class)
    public final void testGetRegionListAuthFailed() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";

        List<Region> regions = new ArrayList<Region>();
        regions.add(createRegion(1, null, "地域\"1,1\"\n", 0, countryCode));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.NG);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);

        _service.getRegionList(gameId, password, countryCode);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test(expected = AuthenticationException.class)
    public final void testGetRegionListArgGameIdIsNull() throws IOException,
            AuthenticationException {

        String gameId = null;
        String password = "pass";
        String countryCode = "TWN";

        List<Region> regions = new ArrayList<Region>();
        regions.add(createRegion(1, null, "地域\"1,1\"\n", 0, countryCode));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.NG);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);

        _service.getRegionList(gameId, password, countryCode);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     * @throws AuthenticationException
     */
    @Test(expected = AuthenticationException.class)
    public final void testGetRegionListArgPasswordIsNull() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = null;
        String countryCode = "TWN";

        List<Region> regions = new ArrayList<Region>();
        regions.add(createRegion(1, null, "地域\"1,1\"\n", 0, countryCode));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.NG);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);

        _service.getRegionList(gameId, password, countryCode);
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
    public final void testGetRegionListPlaceAll() throws IOException,
            AuthenticationException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));

        List<Region> regions = new ArrayList<Region>();
        regions.add(createRegion(1, null, "地域\"1,1\"\n", 0, countryCode));
        regions.add(createRegion(11, null, "地域\"2,1\"\n", 0, countryCode));
        regions.add(createRegion(2, 1, "地域\"1,2\"\n", 1, countryCode));
        regions.add(createRegion(12, 11, "地域\"2,2\"\n", 1, countryCode));
        regions.add(createRegion(3, 2, "地域\"1,3\"\n", 2, countryCode));
        regions.add(createRegion(13, 12, "地域\"2,3\"\n", 2, countryCode));
        regions.add(createRegion(4, 3, "地域\"1,4\"\n", 3, countryCode));
        regions.add(createRegion(14, 13, "地域\"2,4\"\n", 3, countryCode));

        // GetPlaceListで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccountAndPlaceAll(eq(gameId),
                        eq(password))).andReturn(
                ApiAccountDao.ApiAccountStatus.OK_PLACE_ALL);
        EasyMock.expect(_placeRegionTableDao.findPlacesAll()).andReturn(places);
        EasyMock.expect(_placeRegionTableDao.findRegions(eq(places)))
                .andReturn(regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.getRegionList(gameId, password, countryCode);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				if (i >= 0 && i < regions.size() + 1) {
					assertEquals(5, line.length);
					Region r = regions.get(i);
					assertEquals(r.getCountryCode(), line[0]);
					assertEquals(String.valueOf(r.getRegionId()), line[1]);
					assertEquals(String.valueOf(r.getLevel()), line[2]);
					BigDecimal b = r.getParentRegionId();
					if (b == null) {
						b = new BigDecimal(0);
					}
					assertEquals(b.toString(), line[3]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[4]);
				} else {
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

    private Region createRegion(int regionId, Integer parentRegionId,
            String name) {
        Region region = new Region();
        region.setRegionId(regionId);
        if (parentRegionId != null) {
            region.setParentRegionId(new BigDecimal(parentRegionId));
        }
        region.setName(name);
        return region;
    }

    private Region createRegion(int regionId, Integer parentRegionId,
            String name, int level, String countryCode) {
        Region region = createRegion(regionId, parentRegionId, name);
        region.setLevel(level);
        region.setCountryCode(countryCode);
        return region;
    }

}
