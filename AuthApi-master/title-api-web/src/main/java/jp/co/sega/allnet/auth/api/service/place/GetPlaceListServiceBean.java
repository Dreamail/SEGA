/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.place;

import java.util.List;

import jp.co.sega.allnet.auth.api.domain.Place;
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
@Component("getPlaceListService")
@Scope("singleton")
public class GetPlaceListServiceBean extends PlaceRegionTableServiceBean
        implements GetPlaceListService {

    private static final Logger _log = LoggerFactory
            .getLogger(GetPlaceListServiceBean.class);

    private static final String TITLE = "place";

    @Override
    public String getPlaceList(String gameId, String password,
            String countryCode, boolean allPlaces, int activeDays,
            String placeType) throws AuthenticationException {
        _log.info(formatLog(
                "Start finding %s [gameId=%s&password=%s&countryCode=%s&allPlaces=%s]",
                TITLE, gameId, password, countryCode, allPlaces));

        List<Place> places;

        if (StringUtils.isEmpty(countryCode)) {
            countryCode = COUNTRY_CODE_DEFAULT;
        }

        boolean billOpenAllowed = false;

        switch (_apiAccountDao.checkApiAccountAndPlaceAll(gameId, password)) {
        case OK_PLACE_ALL:
            places = _placeRegionTableDao.findPlacesAll();
            break;
        case OK:
            places = findPlaces(gameId, countryCode, allPlaces, activeDays,
                    placeType);
            break;
        default:
            throw new AuthenticationException();
        }

        billOpenAllowed = checkBillOpenAllowedGame(gameId);

        _log.info(formatLog("Find places [count:%s]", places.size()));

        StringBuilder res = new StringBuilder();

        for (Place p : places) {
            res.append(createPlacePart(p));
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
            res.append("\n");
        }

        _log.info(formatLog("Finding %s was successful", TITLE));

        return res.toString();
    }
}
