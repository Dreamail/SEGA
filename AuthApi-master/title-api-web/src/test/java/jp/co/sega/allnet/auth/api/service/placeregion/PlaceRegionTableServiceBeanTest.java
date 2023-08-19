/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.placeregion;

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
import jp.co.sega.allnet.auth.api.domain.Region;
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
public class PlaceRegionTableServiceBeanTest {

    private static final Logger _log = LoggerFactory
            .getLogger(PlaceRegionTableServiceBeanTest.class);

    @Resource(name = "placeRegionTableService")
    private PlaceRegionTableService _service;

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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableAllRegion() throws IOException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean all = false;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));

        List<Region> regions0 = new ArrayList<Region>();
        regions0.add(createRegion(1, null, "地域\"1,1\"\n"));
        regions0.add(createRegion(11, null, "地域\"2,1\"\n"));
        List<Region> regions1 = new ArrayList<Region>();
        regions1.add(createRegion(2, 1, "地域\"1,2\"\n"));
        regions1.add(createRegion(12, 11, "地域\"2,2\"\n"));
        List<Region> regions2 = new ArrayList<Region>();
        regions2.add(createRegion(3, 2, "地域\"1,3\"\n"));
        regions2.add(createRegion(13, 12, "地域\"2,3\"\n"));
        List<Region> regions3 = new ArrayList<Region>();
        regions3.add(createRegion(4, 3, "地域\"1,4\"\n"));
        regions3.add(createRegion(14, 13, "地域\"2,4\"\n"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq(countryCode),
                        eq(0), (String) eq(null))).andReturn(places);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
				case 2:
					assertEquals(15, line.length);
					Place p = places.get(i - 1);
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
					break;
				case 3:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 6:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 9:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 12:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
					break;
				case 4:
				case 5:
					assertEquals(2, line.length);
					Region r = regions0.get(i - 4);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[1]);
					break;
				case 7:
				case 8:
					assertEquals(3, line.length);
					r = regions1.get(i - 7);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 10:
				case 11:
					assertEquals(3, line.length);
					r = regions2.get(i - 10);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 13:
				case 14:
					assertEquals(3, line.length);
					r = regions3.get(i - 13);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableAllPlace() throws IOException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean all = true;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));
        places.add(createPlaceOpenTimeNull(-99997, countryCode, "4Z3456789",
                11, 12, 13, 14, 2));

        List<Region> regions0 = new ArrayList<Region>();
        regions0.add(createRegion(1, null, "地域\"1,1\"\n"));
        regions0.add(createRegion(11, null, "地域\"2,1\"\n"));
        List<Region> regions1 = new ArrayList<Region>();
        regions1.add(createRegion(2, 1, "地域\"1,2\"\n"));
        regions1.add(createRegion(12, 11, "地域\"2,2\"\n"));
        List<Region> regions2 = new ArrayList<Region>();
        regions2.add(createRegion(3, 2, "地域\"1,3\"\n"));
        regions2.add(createRegion(13, 12, "地域\"2,3\"\n"));
        List<Region> regions3 = new ArrayList<Region>();
        regions3.add(createRegion(4, 3, "地域\"1,4\"\n"));
        regions3.add(createRegion(14, 13, "地域\"2,4\"\n"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlacesIncludeAuthDenied(eq(gameId),
                        eq(countryCode), eq(0), (String) eq(null))).andReturn(
                places);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
				case 2:
					assertEquals(15, line.length);
					Place p = places.get(i - 1);
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
					break;
				case 3:
					assertEquals(15, line.length);
					p = places.get(i - 1);
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
					break;
				case 4:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 7:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 10:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 13:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
					break;
				case 5:
				case 6:
					assertEquals(2, line.length);
					Region r = regions0.get(i - 5);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[1]);
					break;
				case 8:
				case 9:
					assertEquals(3, line.length);
					r = regions1.get(i - 8);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 11:
				case 12:
					assertEquals(3, line.length);
					r = regions2.get(i - 11);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 14:
				case 15:
					assertEquals(3, line.length);
					r = regions3.get(i - 14);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableNoRegion() throws IOException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean all = false;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));

        List<Region> regions0 = new ArrayList<Region>();
        List<Region> regions1 = new ArrayList<Region>();
        List<Region> regions2 = new ArrayList<Region>();
        List<Region> regions3 = new ArrayList<Region>();

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq(countryCode),
                        eq(0), (String) eq(null))).andReturn(places);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
				case 2:
					assertEquals(15, line.length);
					Place p = places.get(i - 1);
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
					break;
				case 3:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 4:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 5:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 6:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableParcialRegion() throws IOException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean all = false;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));

        List<Region> regions0 = new ArrayList<Region>();
        regions0.add(createRegion(1, null, "地域\"1,1\"\n"));
        regions0.add(createRegion(11, null, "地域\"2,1\"\n"));
        List<Region> regions1 = new ArrayList<Region>();
        regions1.add(createRegion(2, 1, "地域\"1,2\"\n"));
        regions1.add(createRegion(12, 11, "地域\"2,2\"\n"));
        List<Region> regions2 = new ArrayList<Region>();
        regions2.add(createRegion(3, 2, "地域\"1,3\"\n"));
        List<Region> regions3 = new ArrayList<Region>();

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq(countryCode),
                        eq(0), (String) eq(null))).andReturn(places);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
				case 2:
					assertEquals(15, line.length);
					Place p = places.get(i - 1);
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
					break;
				case 3:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 6:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 9:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 11:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
					break;
				case 4:
				case 5:
					assertEquals(2, line.length);
					Region r = regions0.get(i - 4);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[1]);
					break;
				case 7:
				case 8:
					assertEquals(3, line.length);
					r = regions1.get(i - 7);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 10:
					assertEquals(3, line.length);
					r = regions2.get(i - 10);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableNoPlaces() throws IOException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean all = false;

        List<Place> places = new ArrayList<Place>();

        List<Region> regions0 = new ArrayList<Region>();
        regions0.add(createRegion(1, null, "地域\"1,1\"\n"));
        regions0.add(createRegion(11, null, "地域\"2,1\"\n"));
        List<Region> regions1 = new ArrayList<Region>();
        regions1.add(createRegion(2, 1, "地域\"1,2\"\n"));
        regions1.add(createRegion(12, 11, "地域\"2,2\"\n"));
        List<Region> regions2 = new ArrayList<Region>();
        regions2.add(createRegion(3, 2, "地域\"1,3\"\n"));
        regions2.add(createRegion(13, 12, "地域\"2,3\"\n"));
        List<Region> regions3 = new ArrayList<Region>();
        regions3.add(createRegion(4, 3, "地域\"1,4\"\n"));
        regions3.add(createRegion(14, 13, "地域\"2,4\"\n"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq(countryCode),
                        eq(0), (String) eq(null))).andReturn(places);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 4:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 7:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 10:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
					break;
				case 2:
				case 3:
					assertEquals(2, line.length);
					Region r = regions0.get(i - 2);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[1]);
					break;
				case 5:
				case 6:
					assertEquals(3, line.length);
					r = regions1.get(i - 5);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 8:
				case 9:
					assertEquals(3, line.length);
					r = regions2.get(i - 8);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 11:
				case 12:
					assertEquals(3, line.length);
					r = regions3.get(i - 11);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableCountryIsEmpty() throws IOException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "";
        boolean all = false;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, "TWN", "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, "TWN", "3Z3456789", 11, 12, 13, 14, 1));

        List<Region> regions0 = new ArrayList<Region>();
        List<Region> regions1 = new ArrayList<Region>();
        List<Region> regions2 = new ArrayList<Region>();
        List<Region> regions3 = new ArrayList<Region>();

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq(0),
                        (String) eq(null))).andReturn(places);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
				case 2:
					assertEquals(15, line.length);
					Place p = places.get(i - 1);
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
					break;
				case 3:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 4:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 5:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 6:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableCountryIsNull() throws IOException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = null;
        boolean all = false;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, "JPN", "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, "JPN", "3Z3456789", 11, 12, 13, 14, 1));

        List<Region> regions0 = new ArrayList<Region>();
        regions0.add(createRegion(1, null, "地域\"1,1\"\n"));
        regions0.add(createRegion(11, null, "地域\"2,1\"\n"));
        List<Region> regions1 = new ArrayList<Region>();
        regions1.add(createRegion(2, 1, "地域\"1,2\"\n"));
        regions1.add(createRegion(12, 11, "地域\"2,2\"\n"));
        List<Region> regions2 = new ArrayList<Region>();
        regions2.add(createRegion(3, 2, "地域\"1,3\"\n"));
        regions2.add(createRegion(13, 12, "地域\"2,3\"\n"));
        List<Region> regions3 = new ArrayList<Region>();
        regions3.add(createRegion(4, 3, "地域\"1,4\"\n"));
        regions3.add(createRegion(14, 13, "地域\"2,4\"\n"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq("JPN"), eq(0),
                        (String) eq(null))).andReturn(places);
        EasyMock.expect(_placeRegionTableDao.findRegions(eq("JPN"), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(_placeRegionTableDao.findRegions(eq("JPN"), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(_placeRegionTableDao.findRegions(eq("JPN"), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(_placeRegionTableDao.findRegions(eq("JPN"), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
				case 2:
					assertEquals(15, line.length);
					Place p = places.get(i - 1);
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
					break;
				case 3:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 6:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 9:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 12:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
					break;
				case 4:
				case 5:
					assertEquals(2, line.length);
					Region r = regions0.get(i - 4);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[1]);
					break;
				case 7:
				case 8:
					assertEquals(3, line.length);
					r = regions1.get(i - 7);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 10:
				case 11:
					assertEquals(3, line.length);
					r = regions2.get(i - 10);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 13:
				case 14:
					assertEquals(3, line.length);
					r = regions3.get(i - 13);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableAuthFailed() throws IOException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean all = false;

        List<Region> regions0 = new ArrayList<Region>();
        regions0.add(createRegion(1, null, "地域\"1,1\"\n"));
        regions0.add(createRegion(11, null, "地域\"2,1\"\n"));
        List<Region> regions1 = new ArrayList<Region>();
        regions1.add(createRegion(2, 1, "地域\"1,2\"\n"));
        regions1.add(createRegion(12, 11, "地域\"2,2\"\n"));
        List<Region> regions2 = new ArrayList<Region>();
        regions2.add(createRegion(3, 2, "地域\"1,3\"\n"));
        regions2.add(createRegion(13, 12, "地域\"2,3\"\n"));
        List<Region> regions3 = new ArrayList<Region>();
        regions3.add(createRegion(4, 3, "地域\"1,4\"\n"));
        regions3.add(createRegion(14, 13, "地域\"2,4\"\n"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(false);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 4:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 7:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 10:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
					break;
				case 2:
				case 3:
					assertEquals(2, line.length);
					Region r = regions0.get(i - 2);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[1]);
					break;
				case 5:
				case 6:
					assertEquals(3, line.length);
					r = regions1.get(i - 5);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 8:
				case 9:
					assertEquals(3, line.length);
					r = regions2.get(i - 8);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 11:
				case 12:
					assertEquals(3, line.length);
					r = regions3.get(i - 11);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableCountryAll() throws IOException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "aLl";
        boolean all = false;

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
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq(0),
                        (String) eq(null))).andReturn(places);
        EasyMock.expect(_placeRegionTableDao.findRegions(places)).andReturn(
                regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
				case 2:
					assertEquals(15, line.length);
					Place p = places.get(i - 1);
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
					break;
				case 3:
					assertEquals("START:REGION", line[0]);
					break;
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
					assertEquals(5, line.length);
					Region r = regions.get(i - 4);
					assertEquals(r.getCountryCode(), line[0]);
					assertEquals(String.valueOf(r.getRegionId()), line[1]);
					assertEquals(String.valueOf(r.getLevel()), line[2]);
					BigDecimal b = r.getParentRegionId();
					if (b == null) {
						b = new BigDecimal(0);
					}
					assertEquals(b.toString(), line[3]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[4]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableAllPlaceCountryAll()
            throws IOException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "aLl";
        boolean all = true;

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
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlacesIncludeAuthDenied(eq(gameId),
                        eq(0), (String) eq(null))).andReturn(places);
        EasyMock.expect(_placeRegionTableDao.findRegions(places)).andReturn(
                regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
				case 2:
					assertEquals(15, line.length);
					Place p = places.get(i - 1);
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
					break;
				case 3:
					assertEquals("START:REGION", line[0]);
					break;
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
					assertEquals(5, line.length);
					Region r = regions.get(i - 4);
					assertEquals(r.getCountryCode(), line[0]);
					assertEquals(String.valueOf(r.getRegionId()), line[1]);
					assertEquals(String.valueOf(r.getLevel()), line[2]);
					BigDecimal b = r.getParentRegionId();
					if (b == null) {
						b = new BigDecimal(0);
					}
					assertEquals(b.toString(), line[3]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[4]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableCountryAllNoPlace()
            throws IOException {

        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "ALL";
        boolean all = false;

        List<Place> places = new ArrayList<Place>();

        List<Region> regions = new ArrayList<Region>();

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq(0),
                        (String) eq(null))).andReturn(places);
        EasyMock.expect(_placeRegionTableDao.findRegions(places)).andReturn(
                regions);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals(1, line.length);
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
					assertEquals(1, line.length);
					assertEquals("START:REGION", line[0]);
					break;
				case 2:
					assertEquals(1, line.length);
					assertEquals("", line[0]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableArgGameIdIsNull() throws IOException {

        String gameId = null;
        String password = "pass";
        String countryCode = "TWN";
        boolean all = false;

        List<Region> regions0 = new ArrayList<Region>();
        regions0.add(createRegion(1, null, "地域\"1,1\"\n"));
        regions0.add(createRegion(11, null, "地域\"2,1\"\n"));
        List<Region> regions1 = new ArrayList<Region>();
        regions1.add(createRegion(2, 1, "地域\"1,2\"\n"));
        regions1.add(createRegion(12, 11, "地域\"2,2\"\n"));
        List<Region> regions2 = new ArrayList<Region>();
        regions2.add(createRegion(3, 2, "地域\"1,3\"\n"));
        regions2.add(createRegion(13, 12, "地域\"2,3\"\n"));
        List<Region> regions3 = new ArrayList<Region>();
        regions3.add(createRegion(4, 3, "地域\"1,4\"\n"));
        regions3.add(createRegion(14, 13, "地域\"2,4\"\n"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(false);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 4:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 7:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 10:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
					break;
				case 2:
				case 3:
					assertEquals(2, line.length);
					Region r = regions0.get(i - 2);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[1]);
					break;
				case 5:
				case 6:
					assertEquals(3, line.length);
					r = regions1.get(i - 5);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 8:
				case 9:
					assertEquals(3, line.length);
					r = regions2.get(i - 8);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 11:
				case 12:
					assertEquals(3, line.length);
					r = regions3.get(i - 11);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTable(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableArgPasswordIsNull()
            throws IOException {

        String gameId = "SBXX";
        String password = null;
        String countryCode = "TWN";
        boolean all = false;

        List<Region> regions0 = new ArrayList<Region>();
        regions0.add(createRegion(1, null, "地域\"1,1\"\n"));
        regions0.add(createRegion(11, null, "地域\"2,1\"\n"));
        List<Region> regions1 = new ArrayList<Region>();
        regions1.add(createRegion(2, 1, "地域\"1,2\"\n"));
        regions1.add(createRegion(12, 11, "地域\"2,2\"\n"));
        List<Region> regions2 = new ArrayList<Region>();
        regions2.add(createRegion(3, 2, "地域\"1,3\"\n"));
        regions2.add(createRegion(13, 12, "地域\"2,3\"\n"));
        List<Region> regions3 = new ArrayList<Region>();
        regions3.add(createRegion(4, 3, "地域\"1,4\"\n"));
        regions3.add(createRegion(14, 13, "地域\"2,4\"\n"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(false);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTable(gameId, password, countryCode,
                all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 4:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 7:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 10:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
					break;
				case 2:
				case 3:
					assertEquals(2, line.length);
					Region r = regions0.get(i - 2);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[1]);
					break;
				case 5:
				case 6:
					assertEquals(3, line.length);
					r = regions1.get(i - 5);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 8:
				case 9:
					assertEquals(3, line.length);
					r = regions2.get(i - 8);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 11:
				case 12:
					assertEquals(3, line.length);
					r = regions3.get(i - 11);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTableAll(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableAll() throws IOException {
        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean all = false;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));
        // Region0-3が全て未設定のケース
        places.add(createPlace(-99997, countryCode, "4Z3456789", null, null,
                null, null, 2));

        List<Region> regions0 = new ArrayList<Region>();
        regions0.add(createRegion(1, null, "地域\"1,1\"\n"));
        regions0.add(createRegion(11, null, "地域\"2,1\"\n"));
        List<Region> regions1 = new ArrayList<Region>();
        regions1.add(createRegion(2, 1, "地域\"1,2\"\n"));
        regions1.add(createRegion(12, 11, "地域\"2,2\"\n"));
        List<Region> regions2 = new ArrayList<Region>();
        regions2.add(createRegion(3, 2, "地域\"1,3\"\n"));
        regions2.add(createRegion(13, 12, "地域\"2,3\"\n"));
        List<Region> regions3 = new ArrayList<Region>();
        regions3.add(createRegion(4, 3, "地域\"1,4\"\n"));
        regions3.add(createRegion(14, 13, "地域\"2,4\"\n"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq(countryCode),
                        eq(0), (String) eq(null))).andReturn(places);
        EasyMock.expect(
                _placeRegionTableDao.checkBillOpenAllowedGame(eq(gameId)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTableAll(gameId, password,
                countryCode, all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
				case 2:
					assertEquals(18, line.length);
					Place p = places.get(i - 1);
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
				case 3:
					assertEquals(18, line.length);
					p = places.get(i - 1);
					assertEquals(p.getPlaceId().replaceAll("\r|\n", ""), line[0]);
					assertEquals(PlaceRegionTableService.DUMMY_PLACE_IP, line[1]);
					assertEquals(p.getName().replaceAll("\r|\n", ""), line[2]);
					assertEquals(p.getTel().replaceAll("\r|\n", ""), line[3]);
					assertEquals(p.getAddress().replaceAll("\r|\n", ""), line[4]);
					assertEquals(p.getZipCode().replaceAll("\r|\n", ""), line[5]);
					assertEquals("0", line[6]);
					assertEquals("0", line[7]);
					assertEquals("0", line[8]);
					assertEquals("0", line[9]);
					assertEquals(p.getStation().replaceAll("\r|\n", ""), line[10]);
					assertEquals(p.getOpenTime().replaceAll("\r|\n", ""), line[11]);
					assertEquals(p.getCloseTime().replaceAll("\r|\n", ""), line[12]);
					assertEquals(p.getSpecialInfo().replaceAll("\r|\n", ""), line[13]);
					assertEquals(p.getNickname().replaceAll("\r|\n", ""), line[14]);
					assertEquals(p.getBillCode().replaceAll("\r|\n", ""), line[15]);
					assertEquals(String.valueOf(p.getAllnetId()), line[16]);
					assertEquals(p.getCountryCode().replaceAll("\r|\n", ""), line[17]);
					break;
				case 4:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 7:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 10:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 13:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
					break;
				case 5:
				case 6:
					assertEquals(2, line.length);
					Region r = regions0.get(i - 5);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[1]);
					break;
				case 8:
				case 9:
					assertEquals(3, line.length);
					r = regions1.get(i - 8);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 11:
				case 12:
					assertEquals(3, line.length);
					r = regions2.get(i - 11);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 14:
				case 15:
					assertEquals(3, line.length);
					r = regions3.get(i - 14);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTableAll(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableAllBillOpenDenied()
            throws IOException {
        String gameId = "SBXX";
        String password = "pass";
        String countryCode = "TWN";
        boolean all = false;

        List<Place> places = new ArrayList<Place>();
        places.add(createPlace(-99999, countryCode, "2Z3456789", 1, 2, 3, 4, 0));
        places.add(createPlace(-99998, countryCode, "3Z3456789", 11, 12, 13,
                14, 1));

        List<Region> regions0 = new ArrayList<Region>();
        regions0.add(createRegion(1, null, "地域\"1,1\"\n"));
        regions0.add(createRegion(11, null, "地域\"2,1\"\n"));
        List<Region> regions1 = new ArrayList<Region>();
        regions1.add(createRegion(2, 1, "地域\"1,2\"\n"));
        regions1.add(createRegion(12, 11, "地域\"2,2\"\n"));
        List<Region> regions2 = new ArrayList<Region>();
        regions2.add(createRegion(3, 2, "地域\"1,3\"\n"));
        regions2.add(createRegion(13, 12, "地域\"2,3\"\n"));
        List<Region> regions3 = new ArrayList<Region>();
        regions3.add(createRegion(4, 3, "地域\"1,4\"\n"));
        regions3.add(createRegion(14, 13, "地域\"2,4\"\n"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(true);
        EasyMock.expect(
                _placeRegionTableDao.findPlaces(eq(gameId), eq(countryCode),
                        eq(0), (String) eq(null))).andReturn(places);
        EasyMock.expect(
                _placeRegionTableDao.checkBillOpenAllowedGame(eq(gameId)))
                .andReturn(false);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTableAll(gameId, password,
                countryCode, all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
				case 2:
					assertEquals(18, line.length);
					Place p = places.get(i - 1);
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
					assertEquals(PlaceRegionTableService.DUMMY_BILL_CODE_STR, line[15]);
					assertEquals(String.valueOf(p.getAllnetId()), line[16]);
					assertEquals(p.getCountryCode().replaceAll("\r|\n", ""), line[17]);
					break;
				case 3:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 6:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 9:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 12:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
					break;
				case 4:
				case 5:
					assertEquals(2, line.length);
					Region r = regions0.get(i - 4);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[1]);
					break;
				case 7:
				case 8:
					assertEquals(3, line.length);
					r = regions1.get(i - 7);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 10:
				case 11:
					assertEquals(3, line.length);
					r = regions2.get(i - 10);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 13:
				case 14:
					assertEquals(3, line.length);
					r = regions3.get(i - 13);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTableAll(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableAllArgGameIdIsNull()
            throws IOException {
        String gameId = null;
        String password = "pass";
        String countryCode = "TWN";
        boolean all = false;
        List<Region> regions0 = new ArrayList<Region>();
        regions0.add(createRegion(1, null, "地域\"1,1\"\n"));
        regions0.add(createRegion(11, null, "地域\"2,1\"\n"));
        List<Region> regions1 = new ArrayList<Region>();
        regions1.add(createRegion(2, 1, "地域\"1,2\"\n"));
        regions1.add(createRegion(12, 11, "地域\"2,2\"\n"));
        List<Region> regions2 = new ArrayList<Region>();
        regions2.add(createRegion(3, 2, "地域\"1,3\"\n"));
        regions2.add(createRegion(13, 12, "地域\"2,3\"\n"));
        List<Region> regions3 = new ArrayList<Region>();
        regions3.add(createRegion(4, 3, "地域\"1,4\"\n"));
        regions3.add(createRegion(14, 13, "地域\"2,4\"\n"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(false);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTableAll(gameId, password,
                countryCode, all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 4:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 7:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 10:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
					break;
				case 2:
				case 3:
					assertEquals(2, line.length);
					Region r = regions0.get(i - 2);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[1]);
					break;
				case 5:
				case 6:
					assertEquals(3, line.length);
					r = regions1.get(i - 5);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 8:
				case 9:
					assertEquals(3, line.length);
					r = regions2.get(i - 8);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 11:
				case 12:
					assertEquals(3, line.length);
					r = regions3.get(i - 11);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
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
     * {@link jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean#placeRegionTableAll(java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testPlaceRegionTableAllArgPasswordIsNull()
            throws IOException {
        String gameId = "SBXX";
        String password = null;
        String countryCode = "TWN";
        boolean all = false;
        List<Region> regions0 = new ArrayList<Region>();
        regions0.add(createRegion(1, null, "地域\"1,1\"\n"));
        regions0.add(createRegion(11, null, "地域\"2,1\"\n"));
        List<Region> regions1 = new ArrayList<Region>();
        regions1.add(createRegion(2, 1, "地域\"1,2\"\n"));
        regions1.add(createRegion(12, 11, "地域\"2,2\"\n"));
        List<Region> regions2 = new ArrayList<Region>();
        regions2.add(createRegion(3, 2, "地域\"1,3\"\n"));
        regions2.add(createRegion(13, 12, "地域\"2,3\"\n"));
        List<Region> regions3 = new ArrayList<Region>();
        regions3.add(createRegion(4, 3, "地域\"1,4\"\n"));
        regions3.add(createRegion(14, 13, "地域\"2,4\"\n"));

        // PlaceRegionTableで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(
                _apiAccountDao.checkApiAccount(eq(gameId), eq(password)))
                .andReturn(false);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(0)))
                .andReturn(regions0);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(1)))
                .andReturn(regions1);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(2)))
                .andReturn(regions2);
        EasyMock.expect(
                _placeRegionTableDao.findRegions(eq(countryCode), eq(3)))
                .andReturn(regions3);
        // 振る舞いを記憶
        EasyMock.replay(_apiAccountDao);
        EasyMock.replay(_placeRegionTableDao);

        String ret = _service.placeRegionTableAll(gameId, password,
                countryCode, all);
        _log.info(ret);

		try (CSVReader reader = new CSVReader(new BufferedReader(new StringReader(ret)))) {
			String[] line;
			int i = 0;
			while ((line = reader.readNext()) != null) {
				switch (i) {
				case 0:
					assertEquals("START:PLACES0", line[0]);
					break;
				case 1:
					assertEquals("START:REGION_SUBINDEX0", line[0]);
					break;
				case 4:
					assertEquals("START:REGION_SUBINDEX1", line[0]);
					break;
				case 7:
					assertEquals("START:REGION_SUBINDEX2", line[0]);
					break;
				case 10:
					assertEquals("START:REGION_SUBINDEX3", line[0]);
					break;
				case 2:
				case 3:
					assertEquals(2, line.length);
					Region r = regions0.get(i - 2);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[1]);
					break;
				case 5:
				case 6:
					assertEquals(3, line.length);
					r = regions1.get(i - 5);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 8:
				case 9:
					assertEquals(3, line.length);
					r = regions2.get(i - 8);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
					break;
				case 11:
				case 12:
					assertEquals(3, line.length);
					r = regions3.get(i - 11);
					assertEquals(String.valueOf(r.getRegionId()), line[0]);
					assertEquals(String.valueOf(r.getParentRegionId()), line[1]);
					assertEquals(r.getName().replaceAll("\r|\n", ""), line[2]);
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
