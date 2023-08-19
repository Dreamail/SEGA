/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.placeregion;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.ApiAccountDao;
import jp.co.sega.allnet.auth.api.dao.PlaceRegionTableDao;
import jp.co.sega.allnet.auth.api.domain.Place;
import jp.co.sega.allnet.auth.api.domain.Region;
import jp.co.sega.allnet.auth.api.service.AbstractApiService;
import jp.co.sega.allnet.auth.csv.CsvUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TsuboiY
 * 
 */
@Component("placeRegionTableService")
@Scope("singleton")
public class PlaceRegionTableServiceBean extends AbstractApiService implements
        PlaceRegionTableService {

    private static final Logger _log = LoggerFactory
            .getLogger(PlaceRegionTableServiceBean.class);

    private static final int REGION_SIZE = 4;

    @Resource(name = "apiAccountDao")
    protected ApiAccountDao _apiAccountDao;

    @Resource(name = "placeRegionTableDao")
    protected PlaceRegionTableDao _placeRegionTableDao;

    @Transactional
    @Override
    public String placeRegionTable(String gameId, String password,
            String countryCode, boolean all) {
        return placeRegionTable(gameId, password, countryCode, all, false);
    }

    @Transactional
    @Override
    public String placeRegionTableAll(String gameId, String password,
            String countryCode, boolean all) {
        return placeRegionTable(gameId, password, countryCode, all, true);
    }

    /**
     * ゲームアカウントをチェックする。
     * 
     * @param gameId
     * @param password
     * @return
     */
    protected boolean checkApiAccount(String gameId, String password) {
        return _apiAccountDao.checkApiAccount(gameId, password);
    }

    /**
     * 店舗情報を取得する。
     * 
     * @param gameId
     * @param countryCode
     * @param allFlag
     * @return
     */
    protected List<Place> findPlaces(String gameId, String countryCode,
            boolean allFlag, int activeDays, String placeType) {
        if (countryCode.isEmpty()) {
            countryCode = COUNTRY_CODE_ALL;
        }
        if (allFlag) {
            if (countryCode.equalsIgnoreCase(COUNTRY_CODE_ALL)) {
                return _placeRegionTableDao.findPlacesIncludeAuthDenied(gameId,
                        activeDays, placeType);
            }
            return _placeRegionTableDao.findPlacesIncludeAuthDenied(gameId,
                    countryCode, activeDays, placeType);
        }
        if (countryCode.equalsIgnoreCase(COUNTRY_CODE_ALL)) {
            return _placeRegionTableDao.findPlaces(gameId, activeDays,
                    placeType);
        }
        return _placeRegionTableDao.findPlaces(gameId, countryCode, activeDays,
                placeType);
    }

    /**
     * 請求先の公開が許可されているかチェックする。
     * 
     * @param gameId
     * @return
     */
    protected boolean checkBillOpenAllowedGame(String gameId) {
        return _placeRegionTableDao.checkBillOpenAllowedGame(gameId);
    }

    /**
     * 店舗情報部分のレスポンスを作成する。
     * 
     * @param place
     * @return
     */
    protected String createPlacePart(Place place) {
        StringBuilder sb = new StringBuilder();
        sb.append(escape(place.getPlaceId())).append(",");
        sb.append(escape(DUMMY_PLACE_IP)).append(",");
        sb.append(escape(place.getName())).append(",");
        sb.append(escape(place.getTel())).append(",");
        sb.append(escape(place.getAddress())).append(",");
        sb.append(escape(place.getZipCode())).append(",");
        int region0Id = 0;
        if (place.getRegion0Id() != null) {
            region0Id = place.getRegion0Id().intValue();
        }
        sb.append(region0Id).append(",");
        int region1Id = 0;
        if (place.getRegion1Id() != null) {
            region1Id = place.getRegion1Id().intValue();
        }
        sb.append(region1Id).append(",");
        int region2Id = 0;
        if (place.getRegion2Id() != null) {
            region2Id = place.getRegion2Id().intValue();
        }
        sb.append(region2Id).append(",");
        int region3Id = 0;
        if (place.getRegion3Id() != null) {
            region3Id = place.getRegion3Id().intValue();
        }
        sb.append(region3Id).append(",");
        sb.append(escape(place.getStation())).append(",");
        sb.append(escape(place.getOpenTime())).append(",");
        sb.append(escape(place.getCloseTime())).append(",");
        sb.append(escape(place.getSpecialInfo())).append(",");
        sb.append(escape(place.getNickname()));
        return sb.toString();
    }

    /**
     * 国コードから地域情報部分を取得してレスポンスを作成する。
     * 
     * @param countryCode
     * @return
     */
    protected String createRegionPart(String countryCode) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < REGION_SIZE; i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(String.format("START:REGION_SUBINDEX%s", i));
            List<Region> regions = _placeRegionTableDao.findRegions(
                    countryCode, i);
            _log.info(formatLog("Find region%s [count:%s]", i, regions.size()));
            for (Region r : regions) {
                sb.append("\n");
                sb.append(r.getRegionId()).append(",");
                if (i > 0) {
                    sb.append(r.getParentRegionId()).append(",");
                }
                sb.append(escape(r.getName()));
            }
        }
        return sb.toString();
    }

    /**
     * 該当店舗の国コードから地域情報部分を取得してレスポンスを作成する。
     * 
     * @param places
     * @return
     */
    protected String createRegionPart(List<Place> places) {
        StringBuilder sb = new StringBuilder();
        List<Region> regions = _placeRegionTableDao.findRegions(places);
        _log.info(formatLog("Find regions [count:%s]", regions.size()));
        sb.append("START:REGION");
        for (Region r : regions) {
            sb.append("\n");
            sb.append(escape(r.getCountryCode())).append(",");
            sb.append(r.getRegionId()).append(",");
            sb.append(r.getLevel()).append(",");
            BigDecimal parentRegionId = r.getParentRegionId();
            if (parentRegionId == null) {
                parentRegionId = BigDecimal.ZERO;
            }
            sb.append(parentRegionId).append(",");
            sb.append(escape(r.getName()));
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * エスケープ処理を行う。
     * 
     * @param s
     * @return
     */
    protected String escape(String s) {
        if (s == null) {
            return "";
        }
        return CsvUtils.escapeForCsv(s);
    }

    /**
     * 店舗地域情報取得処理の本処理を行う。
     * 
     * @param gameId
     * @param password
     * @param countryCode
     * @param all
     * @param i18n
     * @return
     */
    private String placeRegionTable(String gameId, String password,
            String countryCode, boolean all, boolean i18n) {
        String title = "place region table";
        if (i18n) {
            title += " (All)";
        }
        _log.info(formatLog("Start finding %s [id=%s&pw=%s&country=%s&all=%s]",
                title, gameId, password, countryCode, all));

        List<Place> places;

        if (countryCode == null) {
            countryCode = COUNTRY_CODE_DEFAULT;
        }

        boolean billOpenAllowed = false;

        if (checkApiAccount(gameId, password)) {
            places = findPlaces(gameId, countryCode, all, 0, null);
            if (i18n) {
                billOpenAllowed = checkBillOpenAllowedGame(gameId);
            }
        } else {
            places = Collections.emptyList();
        }
        _log.info(formatLog("Find places [count:%s]", places.size()));

        StringBuilder res = new StringBuilder();
        res.append("START:PLACES0\n");

        for (Place p : places) {
            res.append(createPlacePart(p));
            if (i18n) {
                String billCode;
                if (billOpenAllowed) {
                    billCode = p.getBillCode();
                } else {
                    billCode = DUMMY_BILL_CODE_STR;
                }
                res.append(",");
                res.append(escape(billCode));
                res.append(",");
                res.append(p.getAllnetId());
                res.append(",");
                res.append(escape(p.getCountryCode()));
            }
            res.append("\n");
        }

        if (countryCode.equalsIgnoreCase(COUNTRY_CODE_ALL)) {
            res.append(createRegionPart(places));
        } else {
            res.append(createRegionPart(countryCode));
        }

        _log.info(formatLog("Finding %s was successful", title));

        return res.toString();

    }

}
