/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.routertype;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.RouterType;
import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.AdminRegisterConstants;
import jp.co.sega.allnet.auth.service.adminregister.InvalidParameterProcessor;

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
@Component("routerTypeRegisterService")
@Scope("singleton")
@Transactional
public class RouterTypeRegisterServiceBean extends AbstractRegisterService
        implements RouterTypeRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(RouterTypeRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Override
    public String registerRouterType(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {

                RouterTypeRegisterResult r = registerRouterType(line);

                createResponse(res, r.getParam().getRouterTypeId(), num,
                        r.getStatus());

                // インクリメント
                num++;
                if (r.getStatus() >= AdminRegisterConstants.STATUS_SUCCESS) {
                    success++;
                }
            }

            res.append("\nEND,,");
            res.append(success);

            return res.toString();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * ルータ種別情報を登録する。
     * 
     * @param line
     * @return
     */
    private RouterTypeRegisterResult registerRouterType(String[] line) {

        RouterTypeRegisterParameter param = new RouterTypeRegisterParameter(
                line);
        RouterTypeRegisterResult result = new RouterTypeRegisterResult(param);

        // 入力チェック
        if (!checkParameter(result)) {
            return result;
        }

        // ルータ種別情報の取得
        RouterType routerType = findRouterType(param.getRouterTypeId());

        if (routerType == null) {

            if (param.getDeleteFlag() != null
                    && param.getDeleteFlag().equals(
                            AdminRegisterConstants.DELETE_FLAG_DEL)) {
                result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_SKIP);
                return result;
            }

            // ルータ種別情報を登録
            createRouterType(param);

            result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_REGISTER);
            return result;
        }

        if (param.getDeleteFlag() != null
                && param.getDeleteFlag().equals(
                        AdminRegisterConstants.DELETE_FLAG_DEL)) {
            // ルータ種別情報を削除
            removeRouterType(routerType);

            result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_DELETE);
            return result;

        }

        if (param.getRouterTypeName().equals(routerType.getName())) {

            result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_SKIP);
            return result;

        }

        // ルータ種別情報を更新
        modifyRouterType(param, routerType);

        result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_MODIFY);
        return result;
    }

    /**
     * パラメータチェックを行う。
     * 
     * @param result
     * @return
     */
    private boolean checkParameter(RouterTypeRegisterResult result) {
        Map<String, InvalidParameterProcessor> map = new HashMap<String, InvalidParameterProcessor>();

        map.put("routerTypeId",
                new ParameterProcessor(
                        result,
                        AdminRegisterConstants.N_MASTER_REGISTER_STATUS_ERROR_ROUTER_TYPE_ID));
        map.put("routerTypeName",
                new ParameterProcessor(
                        result,
                        AdminRegisterConstants.N_MASTER_REGISTER_STATUS_ERROR_ROUTER_TYPE_NAME));
        return checkParameter(result.getParam(), map, false);
    }

    /**
     * ルータ種別情報を取得する。
     * 
     * @param routerTypeId
     * @return
     */
    private RouterType findRouterType(String routerTypeId) {
        return _em.find(RouterType.class, Integer.parseInt(routerTypeId));
    }

    /**
     * ルータ種別情報を登録する。
     * 
     * @param param
     */
    private void createRouterType(RouterTypeRegisterParameter param) {
        Date now = new Date();
        RouterType r = new RouterType();
        r.setRouterTypeId(Integer.parseInt(param.getRouterTypeId()));
        r.setName(param.getRouterTypeName());
        r.setCreateDate(now);
        r.setCreateUserId(getCurrentUser());
        r.setUpdateDate(now);
        r.setUpdateUserId(getCurrentUser());
        persist(r);
    }

    /**
     * ルータ種別情報を削除する。
     * 
     * @param r
     */
    private void removeRouterType(RouterType r) {
        remove(r);
    }

    /**
     * ルータ種別情報を更新する。
     * 
     * @param param
     * @param routerType
     */
    private void modifyRouterType(RouterTypeRegisterParameter param,
            RouterType routerType) {
        Date now = new Date();
        routerType.setName(param.getRouterTypeName());
        routerType.setUpdateDate(now);
        routerType.setUpdateUserId(getCurrentUser());
        merge(routerType);
    }

    /**
     * レスポンス用データを構築する。
     * 
     * @param res
     * @param compCode
     * @param num
     * @param status
     * @return
     */
    private StringBuilder createResponse(StringBuilder res, String compCode,
            int num, int status) {

        res.append("\n");

        res.append(num);
        res.append(",");
        if (compCode != null) {
            res.append(compCode);
        }
        res.append(",");
        res.append(status);
        return res;
    }

    private class ParameterProcessor implements InvalidParameterProcessor {

        private final RouterTypeRegisterResult _result;
        private final int _status;

        ParameterProcessor(RouterTypeRegisterResult result, int status) {
            this._result = result;
            this._status = status;
        }

        @Override
        public void process() {
            _result.setStatus(_status);
        }
    }

}
