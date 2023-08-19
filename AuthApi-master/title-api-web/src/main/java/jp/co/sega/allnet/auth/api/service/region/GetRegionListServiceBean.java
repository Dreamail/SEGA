/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.region;

import java.math.BigDecimal;
import java.util.List;

import jp.co.sega.allnet.auth.api.domain.Place;
import jp.co.sega.allnet.auth.api.domain.Region;
import jp.co.sega.allnet.auth.api.service.AuthenticationException;
import jp.co.sega.allnet.auth.api.service.placeregion.PlaceRegionTableServiceBean;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author TsuboiY
 * 
 */
@Component("getRegionListService")
@Scope("singleton")
public class GetRegionListServiceBean extends PlaceRegionTableServiceBean
        implements GetRegionListService {

    private static final Logger _log = LoggerFactory
            .getLogger(GetRegionListServiceBean.class);

    private static final String TITLE = "region";

    @Override
    public String getRegionList(String gameId, String password,
            String countryCode) throws AuthenticationException {

        _log.info(formatLog(
                "Start finding %s [gameId=%s&password=%s&countryCode=%s]",
                TITLE, gameId, password, countryCode));

        StringBuilder res = new StringBuilder();

        if (StringUtils.isEmpty(countryCode)) {
            countryCode = COUNTRY_CODE_DEFAULT;
        }

        List<Place> places;
        switch (_apiAccountDao.checkApiAccountAndPlaceAll(gameId, password)) {
        case OK_PLACE_ALL:
            places = _placeRegionTableDao.findPlacesAll();
            break;
        case OK:
            places = findPlaces(gameId, countryCode, true, 0, null);
            break;
        default:
            throw new AuthenticationException();
        }
        res.append(createRegionPart(places));

        _log.info(formatLog("Finding %s was successful", TITLE));

        return res.toString();
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
        for (Region r : regions) {
            sb.append(escape(r.getCountryCode())).append(",");
            sb.append(r.getRegionId()).append(",");
            sb.append(r.getLevel()).append(",");
            BigDecimal parentRegionId = r.getParentRegionId();
            if (parentRegionId == null) {
                parentRegionId = BigDecimal.ZERO;
            }
            sb.append(parentRegionId).append(",");
            sb.append(escape(r.getName()));
            sb.append("\n");
        }
        return sb.toString();
    }
}
