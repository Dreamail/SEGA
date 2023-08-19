/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.router;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jp.co.sega.allnet.auth.common.entity.LcType;
import jp.co.sega.allnet.auth.common.entity.Place;
import jp.co.sega.allnet.auth.common.entity.Router;
import jp.co.sega.allnet.auth.common.entity.RouterType;
import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.AdminRegisterConstants;
import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVReader;

/**
 * ルータ情報の登録処理を実行します。
 * 
 * @author TsuboiY
 * 
 */
@Component("routerRegisterService")
@Scope("singleton")
@Transactional
public class RouterRegisterServiceBean extends AbstractRegisterService
        implements RouterRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(RouterRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Override
    public String registerRouter(String val) {

        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {
                RouterRegisterResult result = registerRouter(line);
                createResponce(res, result.getParam().getRouterId(), result
                        .getParam().getAllnetId(), result.getPlaceId(),
                        result.getOldPlaceId(), num, result.isExist(),
                        result.isSuccess());

                if (result.isSuccess()) {
                    success++;
                }

                num++;
            }

            res.append("\nEND,,,,,,");
            res.append(success);

            return res.toString();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public RegisterServiceResult<RouterRegisterResult> registerRouterForAdmin(
            String val) {

        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int cnt = 0;
            List<RouterRegisterResult> results = new ArrayList<RouterRegisterResult>();

            while ((line = reader.readNext()) != null) {
                RouterRegisterResult r = registerRouter(line);
                if (!r.isSuccess()) {
                    cnt++;
                }
                results.add(r);
            }

            return new RegisterServiceResult<RouterRegisterResult>(cnt, results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }

    }

    /**
     * ルータ情報の登録を行う。
     * 
     * @param line
     * @return
     */
    private RouterRegisterResult registerRouter(String[] line) {

        RouterRegisterParameter param = new RouterRegisterParameter(line);

        // 入力チェック
        if (!checkParameter(param)) {
            return new RouterRegisterResult(false, param, true);
        }

        int allnetId = Integer.parseInt(param.getAllnetId());
        if (allnetId == DELETE_ALLNET_ID) {
            // ルータIDに紐づく店舗情報を取得する
            Place orgPlace = findPlaceByRouterId(param.getRouterId());
            // ルータ情報を削除する
            if (!removeRouter(param.getRouterId())) {
                return new RouterRegisterResult(false, param, true);
            }
            return new RouterRegisterResult(true, param, StringUtils.EMPTY,
                    orgPlace.getPlaceId(), true);
        } else {
            // ALL.Net IDで店舗情報を取得する
            Place place = findPlace(allnetId);
            if (place == null) {
                // 店舗が存在しないので終了
                return new RouterRegisterResult(false, param, false);
            }

            // TODO 管理サーバ側の実装が完了するまでチェックしない
            // // ルータ種別マスタを確認する
            // if (!checkRouterTypes(param)) {
            // return new RouterRegisterResult(false, param, true);
            // }
            //
            // // 回線種別マスタを確認する
            // if (!checkLcTypes(param)) {
            // return new RouterRegisterResult(false, param, true);
            // }

            // ルータIDに紐づく店舗情報を取得する
            Place orgPlace = findPlaceByRouterId(param.getRouterId());
            if (orgPlace == null) {

                // 重複した店舗IPを持つルータが存在するか確認する
                if (!checkPlaceIp(param)) {
                    return new RouterRegisterResult(false, param, false);
                }

                // 新規追加
                createRouter(param, place);
                return new RouterRegisterResult(true, param,
                        place.getPlaceId(), StringUtils.EMPTY, false);

            } else {

                // 重複した店舗IPを持つルータが存在するか確認する
                if (!checkPlaceIp(param)) {
                    return new RouterRegisterResult(false, param, true);
                }

                // 更新
                updateRouter(param, place);
                return new RouterRegisterResult(true, param,
                        place.getPlaceId(), orgPlace.getPlaceId(), true);
            }

        }

    }

    /**
     * ルータIDに紐づく店舗情報を取得する。
     * 
     * @param routerId
     * @return
     */
    private Place findPlaceByRouterId(String routerId) {
        try {
            Query query = _em.createNamedQuery("findPlaceByRouterId");
            query.setParameter("routerId", routerId);
            return (Place) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 店舗情報を取得する。
     * 
     * @param allnetId
     * @return
     */
    private Place findPlace(int allnetId) {
        return _em.find(Place.class, (long) allnetId);
    }

    /**
     * ルータ種別の存在を確認し、なければ新規追加する。
     * 
     * @param param
     */
    @SuppressWarnings("unused")
	private boolean checkRouterTypes(RouterRegisterParameter param) {
        int routerTypeId = Integer.parseInt(param.getRouterTypeId());
        RouterType rt = _em.find(RouterType.class, routerTypeId);
        if (rt == null) {
            return false;
        }
        return true;
    }

    /**
     * 回線種別の存在を確認し、なければ新規追加する。
     * 
     * @param param
     */
    @SuppressWarnings("unused")
	private boolean checkLcTypes(RouterRegisterParameter param) {
        if (StringUtils.isEmpty(param.getLcTypeId())) {
            // TODO 管理サーバ側が実装されたら空チェックはしなくていいはず
            return true;
        }

        int lcTypeId = Integer.parseInt(param.getLcTypeId());
        LcType lt = _em.find(LcType.class, lcTypeId);
        if (lt == null) {

            return false;
        }
        return true;
    }

    /**
     * 店舗IPが重複しているルータがないか確認する。
     * 
     * @param param
     * @return
     */
    private boolean checkPlaceIp(RouterRegisterParameter param) {
        Query query = _em.createNamedQuery("findRouterCountByIp");
        query.setParameter("placeIp", param.getPlaceIp());
        query.setParameter("routerId", param.getRouterId());
        int cnt = ((Long) query.getSingleResult()).intValue();
        if (cnt > 0) {
            return false;
        }
        return true;
    }

    /**
     * ルータ情報を新規追加する。
     * 
     * @param param
     * @param place
     */
    private void createRouter(RouterRegisterParameter param, Place place) {
        Date now = new Date();
        Router r = new Router();
        r.setRouterId(param.getRouterId());
        r.setRouterTypeId(new BigDecimal(param.getRouterTypeId()));
        if (param.getLcTypeId() != null) {
            r.setLcTypeId(new BigDecimal(param.getLcTypeId()));
        }
        r.setPlaceIp(param.getPlaceIp());
        r.setCreateDate(now);
        r.setCreateUserId(getCurrentUser());
        r.setUpdateDate(now);
        r.setUpdateUserId(getCurrentUser());
        r.setAllnetId(new BigDecimal(place.getAllnetId()));
        persist(r);
    }

    /**
     * ルータ情報を更新する。
     * 
     * @param param
     * @param place
     */
    private void updateRouter(RouterRegisterParameter param, Place place) {
        Router r = _em.getReference(Router.class, param.getRouterId());
        r.setRouterTypeId(new BigDecimal(param.getRouterTypeId()));
        if (param.getLcTypeId() != null) {
            r.setLcTypeId(new BigDecimal(param.getLcTypeId()));
        }
        r.setPlaceIp(param.getPlaceIp());
        r.setUpdateDate(new Date());
        r.setUpdateUserId(getCurrentUser());
        r.setAllnetId(new BigDecimal(place.getAllnetId()));
        merge(r);
    }

    /**
     * ルータ情報を削除する。
     * 
     * @param routerId
     * @return
     */
    private boolean removeRouter(String routerId) {
        Query query = _em.createNamedQuery("deleteRouter");
        query.setParameter("routerId", routerId);
        if (query.executeUpdate() > 0) {
            return true;
        }
        return false;
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
    private StringBuilder createResponce(StringBuilder res, String routerId,
            String allnetId, String placeId, String orgPlaceId, int num,
            boolean exist, boolean success) {

        res.append("\n");

        res.append(num);
        res.append(",");
        res.append(routerId);
        res.append(",");
        res.append(allnetId);
        res.append(",");
        res.append(placeId);
        res.append(",");
        res.append(orgPlaceId);
        res.append(",");
        if (exist) {
            res.append(AdminRegisterConstants.MASTER_REGISTER_STATUS_MODIFY);
        } else {
            res.append(AdminRegisterConstants.MASTER_REGISTER_STATUS_REGISTER);
        }
        res.append(",");

        if (success) {
            res.append(AdminRegisterConstants.MASTER_REGISTER_RESULT_SUCCESS);
        } else {
            res.append(AdminRegisterConstants.MASTER_REGISTER_RESULT_FAIL);
        }
        return res;
    }
}
