/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.place;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jp.co.sega.allnet.auth.common.entity.Bill;
import jp.co.sega.allnet.auth.common.entity.Country;
import jp.co.sega.allnet.auth.common.entity.Place;
import jp.co.sega.allnet.auth.common.entity.Region0;
import jp.co.sega.allnet.auth.common.entity.Region0PK;
import jp.co.sega.allnet.auth.common.entity.Region1;
import jp.co.sega.allnet.auth.common.entity.Region1PK;
import jp.co.sega.allnet.auth.common.entity.Region2;
import jp.co.sega.allnet.auth.common.entity.Region2PK;
import jp.co.sega.allnet.auth.common.entity.Region3;
import jp.co.sega.allnet.auth.common.entity.Region3PK;
import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.AdminRegisterConstants;
import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;
import jp.co.sega.allnet.auth.service.util.SequenceGenerator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVReader;

/**
 * @author NakanoY
 * 
 */
@Component("placeRegisterService")
@Scope("singleton")
@Transactional
public class PlaceRegisterServiceBean extends AbstractRegisterService implements
        PlaceRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(PlaceRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "sequenceGenerator")
    private SequenceGenerator _sg;

    @Override
    public String registerPlace(String val) {

        _log.info(formatCsvValueLog(val));

        StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {
                PlaceRegisterResult result = registerPlace(line, false);
                if (result.isSuccess()) {
                    boolean exist = true;
                    if (result.getFlag() == PlaceRegisterResult.Flag.REGISTER) {
                        exist = false;
                    }
                    // 登録した店舗情報と処理結果をレスポンスに追加
                    createResponce(res, result.getParam().getAllnetId(), result
                            .getPlace().getPlaceId(), num, exist);
                    success++;
                } else {
                    createResponce(res, result.getParam().getAllnetId(), "",
                            num, null);
                }
                num++;
            }

            res.append("\nEND,,,,");
            res.append(success);

            return res.toString();

        } catch (IOException e) {
            throw new ApplicationException(e);
        }

    }

    @Override
    public RegisterServiceResult<PlaceRegisterResult> registerPlaceForAdmin(
            String val, boolean allowAddA0) {

        _log.info(formatCsvValueLog(val));

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            List<PlaceRegisterResult> results = new ArrayList<PlaceRegisterResult>();
            String[] line;
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                PlaceRegisterResult r = registerPlace(line, allowAddA0);
                if (!r.isSuccess()) {
                    cnt++;
                }
                results.add(r);
            }
            return new RegisterServiceResult<PlaceRegisterResult>(cnt, results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 店舗情報の登録を行う。
     * 
     * @param line
     * @param allowAddA0
     * @return
     */
    private PlaceRegisterResult registerPlace(String[] line, boolean allowAddA0) {

        PlaceRegisterParameter param = new PlaceRegisterParameter(line);

        // 入力チェック
        if (!checkParameter(param)) {
            return new PlaceRegisterResult(false,
                    PlaceRegisterResult.Flag.NOTHING, param, null);
        }

        // ニックネームのチェック
        String[] nicknameSp = param.getNickname().split(DELIMITER_NICKNAME);
        try {
            if (nicknameSp[0].getBytes("EUC-JP").length > MAX_BYTES_NICKNAME_SEP_EUC_JP
                    || (nicknameSp.length > 1 && nicknameSp[1]
                            .getBytes("EUC-JP").length > MAX_BYTES_NICKNAME_SEP_EUC_JP)) {
                return new PlaceRegisterResult(false,
                        PlaceRegisterResult.Flag.NOTHING, param, null);
            }
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationException(e);
        }

        // 国コードのチェック
        Country country = _em.find(Country.class, param.getCountryCode());
        if (country == null) {
            return new PlaceRegisterResult(false,
                    PlaceRegisterResult.Flag.NOTHING, param, null);
        }

        // 地域0情報のチェック
        Region0 region0 = findRegion0(param.getCountryCode(),
                param.getRegion0());
        if (region0 == null && !StringUtils.isEmpty(param.getRegion0())) {
            if (!allowAddA0) {
                return new PlaceRegisterResult(false,
                        PlaceRegisterResult.Flag.NOTHING, param, null);
            }
            // A0に追加が許可されていたら地域情報0を登録する
            region0 = createRegion0(param);
        }

        // 請求先情報のチェックと登録
        Bill bill = checkAndCreateBill(param);

        // 店舗情報のチェック
        long allnetId = Long.parseLong(param.getAllnetId());
        Place place = findPlace(allnetId);

        // 店舗情報の登録・更新
        Date now = new Date();
        boolean exist = true;
        if (place == null) {
            exist = false;
            // 新規作成
            place = createPlace(allnetId, now);
        }

        if (region0 != null) {
            // 地域1-3情報のチェックと登録
            Region1 region1 = checkAndCreateRegion1(param, region0);
            Region2 region2 = checkAndCreateRegion2(param, region1);
            Region3 region3 = checkAndCreateRegion3(param, region2);
            // 店舗情報を反映
            place = buildPlace(param, place, bill, region0, region1, region2,
                    region3, now);
        } else {
            // 地域情報抜きで店舗情報を反映
            place = buildPlace(param, place, bill, null, null, null, null, now);
        }

        if (exist) {
            // 更新
            place = merge(place);
            return new PlaceRegisterResult(true,
                    PlaceRegisterResult.Flag.MODIFY, param, place);
        } else {
            // 登録
            persist(place);
            return new PlaceRegisterResult(true,
                    PlaceRegisterResult.Flag.REGISTER, param, place);
        }

    }

    /**
     * 国コードと地域名から地域0情報を取得する。
     * 
     * @param countryCode
     * @param regionName
     * @return
     */
    private Region0 findRegion0(String countryCode, String regionName) {
        Query query = _em.createNamedQuery("findRegion0ByName");
        query.setParameter("name", regionName);
        query.setParameter("countryCode", countryCode);

        try {
            return (Region0) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * 国コード、地域名、親地域IDから地域1情報を取得する。
     * 
     * @param countryCode
     * @param regionName
     * @param region0Id
     * @return
     */
    private Region1 findRegion1(String countryCode, String regionName,
            BigDecimal region0Id) {
        Query query = _em.createNamedQuery("findRegion1ByName");
        query.setParameter("name", regionName);
        query.setParameter("countryCode", countryCode);
        query.setParameter("region0Id", region0Id);

        try {
            return (Region1) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * 国コード、地域名、親地域IDから地域2情報を取得する。
     * 
     * @param countryCode
     * @param regionName
     * @param region1Id
     * @return
     */
    private Region2 findRegion2(String countryCode, String regionName,
            BigDecimal region1Id) {
        Query query = _em.createNamedQuery("findRegion2ByName");
        query.setParameter("name", regionName);
        query.setParameter("countryCode", countryCode);
        query.setParameter("region1Id", region1Id);

        try {
            return (Region2) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * 国コード、地域名、親地域IDから地域3情報を取得する。
     * 
     * @param countryCode
     * @param regionName
     * @param region2Id
     * @return
     */
    private Region3 findRegion3(String countryCode, String regionName,
            BigDecimal region2Id) {
        Query query = _em.createNamedQuery("findRegion3ByName");
        query.setParameter("name", regionName);
        query.setParameter("countryCode", countryCode);
        query.setParameter("region2Id", region2Id);

        try {
            return (Region3) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * 地域0情報を登録する。
     * 
     * @param param
     * @return
     */
    private Region0 createRegion0(PlaceRegisterParameter param) {
        // 地域情報を設定
        Region0 r = new Region0();
        Region0PK pk = new Region0PK();
        pk.setCountryCode(param.getCountryCode());
        pk.setRegionId(new BigDecimal(_sg.generateRegionId(
                param.getCountryCode(), REGION_LEVEL_0)));
        r.setPk(pk);
        r.setName(param.getRegion0());
        Date date = new Date();
        r.setCreateDate(new Timestamp(date.getTime()));
        r.setUpdateDate(new Timestamp(date.getTime()));

        // 認証情報をセットする
        String userId = getCurrentUser();
        r.setCreateUserId(userId);
        r.setUpdateUserId(userId);

        // 地域情報の登録
        persist(r);

        return r;
    }

    /**
     * 地域1情報が登録されているかチェックし、登録されていない場合は新たに登録する。
     * 
     * @param param
     * @param parentRegion
     * @return
     */
    private Region1 checkAndCreateRegion1(PlaceRegisterParameter param,
            Region0 parentRegion) {

        if (param.getRegion1().isEmpty()) {
            return null;
        }

        Region1 region1 = findRegion1(param.getCountryCode(),
                param.getRegion1(), parentRegion.getPk().getRegionId());

        if (region1 != null) {
            return region1;
        }

        // 地域情報を設定
        Region1 r = new Region1();
        Region1PK pk = new Region1PK();
        pk.setCountryCode(param.getCountryCode());
        pk.setRegionId(new BigDecimal(_sg.generateRegionId(
                param.getCountryCode(), REGION_LEVEL_1)));
        r.setPk(pk);
        r.setName(param.getRegion1());
        r.setParentRegionId(parentRegion.getPk().getRegionId());
        Date date = new Date();
        r.setCreateDate(new Timestamp(date.getTime()));
        r.setUpdateDate(new Timestamp(date.getTime()));

        // 認証情報をセットする
        String userId = getCurrentUser();
        r.setCreateUserId(userId);
        r.setUpdateUserId(userId);

        // 地域情報の登録
        persist(r);

        return r;

    }

    /**
     * 地域2情報が登録されているかチェックし、登録されていない場合は新たに登録する。
     * 
     * @param param
     * @param parentRegion
     * @return
     */
    private Region2 checkAndCreateRegion2(PlaceRegisterParameter param,
            Region1 parentRegion) {

        if (param.getRegion2().isEmpty()) {
            return null;
        }

        Region2 region2 = findRegion2(param.getCountryCode(),
                param.getRegion2(), parentRegion.getPk().getRegionId());

        if (region2 != null) {
            return region2;
        }

        // 地域情報を設定
        Region2 r = new Region2();
        Region2PK pk = new Region2PK();
        pk.setCountryCode(param.getCountryCode());
        pk.setRegionId(new BigDecimal(_sg.generateRegionId(
                param.getCountryCode(), REGION_LEVEL_2)));
        r.setPk(pk);
        r.setName(param.getRegion2());
        r.setParentRegionId(parentRegion.getPk().getRegionId());
        Date date = new Date();
        r.setCreateDate(new Timestamp(date.getTime()));
        r.setUpdateDate(new Timestamp(date.getTime()));

        // 認証情報をセットする
        String userId = getCurrentUser();
        r.setCreateUserId(userId);
        r.setUpdateUserId(userId);

        // 地域情報の登録
        persist(r);

        return r;
    }

    /**
     * 地域3情報が登録されているかチェックし、登録されていない場合は新たに登録する。
     * 
     * @param param
     * @param parentRegion
     * @return
     */
    private Region3 checkAndCreateRegion3(PlaceRegisterParameter param,
            Region2 parentRegion) {

        if (param.getRegion3().isEmpty()) {
            return null;
        }

        Region3 region3 = findRegion3(param.getCountryCode(),
                param.getRegion3(), parentRegion.getPk().getRegionId());

        if (region3 != null) {
            return region3;
        }

        // 地域情報を設定
        Region3 r = new Region3();
        Region3PK pk = new Region3PK();
        pk.setCountryCode(param.getCountryCode());
        pk.setRegionId(new BigDecimal(_sg.generateRegionId(
                param.getCountryCode(), REGION_LEVEL_3)));
        r.setPk(pk);
        r.setName(param.getRegion3());
        r.setParentRegionId(parentRegion.getPk().getRegionId());
        Date date = new Date();
        r.setCreateDate(new Timestamp(date.getTime()));
        r.setUpdateDate(new Timestamp(date.getTime()));

        // 認証情報をセットする
        String userId = getCurrentUser();
        r.setCreateUserId(userId);
        r.setUpdateUserId(userId);

        // 地域情報の登録
        persist(r);

        return r;
    }

    /**
     * 請求先情報が登録されているかチェックし、登録されていない場合は新たに登録する。
     * 
     * @param param
     * @return
     */
    private Bill checkAndCreateBill(PlaceRegisterParameter param) {

        if (param.getBillCode().isEmpty()) {
            return null;
        }

        Bill bill = _em.find(Bill.class, param.getBillCode());

        if (bill != null) {
            return bill;
        }

        bill = new Bill();
        bill.setBillCode(param.getBillCode());
        Date date = new Date();
        bill.setCreateDate(new Timestamp(date.getTime()));

        // 認証情報をセットする
        String userId = getCurrentUser();
        bill.setCreateUserId(userId);

        // 請求先情報の登録
        persist(bill);

        return bill;

    }

    /**
     * 店舗情報を取得する。
     * 
     * @param param
     * @return
     */
    private Place findPlace(long allnetId) {
        return _em.find(Place.class, allnetId);
    }

    /**
     * 店舗情報の作成を行う。
     * 
     * @param allnetId
     * @param now
     * @return
     */
    private Place createPlace(long allnetId, Date now) {
        Place place = new Place();
        place.setAllnetId(allnetId);

        // placeIdの自動生成
        place.setPlaceId(_sg.generatePlaceId());

        place.setCreateDate(now);

        // 認証情報をセットする
        String userId = getCurrentUser();
        place.setCreateUserId(userId);
        return place;
    }

    /**
     * 店舗情報を構築する。
     * 
     * @param place
     * @param param
     * @param bill
     * @param region0
     * @param region1
     * @param region2
     * @param region3
     * @return
     */
    private Place buildPlace(PlaceRegisterParameter param, Place place,
            Bill bill, Region0 region0, Region1 region1, Region2 region2,
            Region3 region3, Date now) {

        Country country = _em.find(Country.class, param.getCountryCode());

        place.setName(param.getName());
        place.setTel(param.getTel());
        place.setAddress(param.getAddress());
        place.setZipCode(param.getZipCode());
        place.setStation(param.getStation());

        if (!StringUtils.isEmpty(param.getOpenTimeHour())
                && !StringUtils.isEmpty(param.getOpenTimeMinute())) {
            String hour = String.format("%02d",
                    Integer.parseInt(param.getOpenTimeHour()));
            String minute = String.format("%02d",
                    Integer.parseInt(param.getOpenTimeMinute()));
            place.setOpenTime(hour + ":" + minute);
        } else {
            place.setOpenTime(null);
        }
        if (!StringUtils.isEmpty(param.getCloseTimeHour())
                && !StringUtils.isEmpty(param.getCloseTimeMinute())) {
            String hour = String.format("%02d",
                    Integer.parseInt(param.getCloseTimeHour()));
            String minute = String.format("%02d",
                    Integer.parseInt(param.getCloseTimeMinute()));
            place.setCloseTime(hour + ":" + minute);
        } else {
            place.setCloseTime(null);
        }
        place.setSpecialInfo(param.getSpecialInfo());
        if (bill != null) {
            place.setBillCode(bill.getBillCode());
        }
        place.setNickname(param.getNickname());
        place.setCountryCode(country.getCountryCode());
        if (region0 != null) {
            place.setRegion0Id(region0.getPk().getRegionId());
        } else {
            place.setRegion0Id(null);
        }
        if (region1 != null) {
            place.setRegion1Id(region1.getPk().getRegionId());
        } else {
            place.setRegion1Id(null);
        }
        if (region2 != null) {
            place.setRegion2Id(region2.getPk().getRegionId());
        } else {
            place.setRegion2Id(null);
        }
        if (region3 != null) {
            place.setRegion3Id(region3.getPk().getRegionId());
        } else {
            place.setRegion3Id(null);
        }
        place.setUpdateDate(now);

        // 認証情報をセットする
        String userId = getCurrentUser();
        place.setUpdateUserId(userId);

        return place;

    }

    /**
     * レスポンスを作成する。
     * 
     * @param res
     * @param allnetId
     * @param placeId
     * @param num
     * @param exist
     * @return
     */
    private StringBuilder createResponce(StringBuilder res, String allnetId,
            String placeId, int num, Boolean exist) {

        res.append("\n");

        res.append(num);
        res.append(",");
        res.append(allnetId);
        res.append(",");
        res.append(placeId);
        res.append(",");
        if (exist != null) {
            if (exist) {
                res.append(AdminRegisterConstants.MASTER_REGISTER_STATUS_MODIFY);
            } else {
                res.append(AdminRegisterConstants.MASTER_REGISTER_STATUS_REGISTER);
            }
        }
        res.append(",");

        if (exist == null) {
            res.append(AdminRegisterConstants.MASTER_REGISTER_RESULT_FAIL);
        } else {
            res.append(AdminRegisterConstants.MASTER_REGISTER_RESULT_SUCCESS);
        }
        return res;
    }
}
