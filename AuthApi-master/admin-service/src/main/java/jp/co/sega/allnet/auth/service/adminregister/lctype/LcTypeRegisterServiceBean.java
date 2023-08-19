/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.lctype;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.LcType;
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
@Component("lcTypeRegisterService")
@Scope("singleton")
@Transactional
public class LcTypeRegisterServiceBean extends AbstractRegisterService
        implements LcTypeRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(LcTypeRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Override
    public String registerLcType(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {

                LcTypeRegisterResult r = registerLcType(line);
                createResponse(res, r.getParam().getLcTypeId(), num,
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
     * 回線種別情報を登録する。
     * 
     * @param line
     * @return
     */
    private LcTypeRegisterResult registerLcType(String[] line) {

        LcTypeRegisterParameter param = new LcTypeRegisterParameter(line);
        LcTypeRegisterResult result = new LcTypeRegisterResult(param);

        // 入力チェック
        if (!checkParameter(result)) {
            return result;
        }

        // 回線種別情報の取得
        LcType lcType = findLcType(param.getLcTypeId());

        if (lcType == null) {

            if (param.getDeleteFlag() != null
                    && param.getDeleteFlag().equals(
                            AdminRegisterConstants.DELETE_FLAG_DEL)) {
                result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_SKIP);
                return result;
            }

            // 回線種別情報を登録
            createLcType(param);

            result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_REGISTER);
            return result;

        }

        if (param.getDeleteFlag() != null
                && param.getDeleteFlag().equals(
                        AdminRegisterConstants.DELETE_FLAG_DEL)) {
            // 回線種別情報を削除
            removeLcType(lcType);

            result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_DELETE);
            return result;

        }

        if (param.getLcTypeName().equals(lcType.getName())) {
            result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_SKIP);
            return result;
        }

        // 回線種別情報を更新
        modifyLcType(param, lcType);

        result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_MODIFY);
        return result;
    }

    /**
     * パラメータチェックを行う。
     * 
     * @param result
     * @return
     */
    private boolean checkParameter(LcTypeRegisterResult result) {
        Map<String, InvalidParameterProcessor> map = new HashMap<String, InvalidParameterProcessor>();
        map.put("lcTypeId",
                new ParameterProcessor(
                        result,
                        AdminRegisterConstants.N_MASTER_REGISTER_STATUS_ERROR_LC_TYPE_ID));
        map.put("lcTypeName",
                new ParameterProcessor(
                        result,
                        AdminRegisterConstants.N_MASTER_REGISTER_STATUS_ERROR_LC_TYPE_NAME));
        return checkParameter(result.getParam(), map, false);
    }

    /**
     * 回線種別情報を取得する。
     * 
     * @param lcTypeId
     * @return
     */
    private LcType findLcType(String lcTypeId) {
        return _em.find(LcType.class, Integer.parseInt(lcTypeId));
    }

    /**
     * 回線種別情報を登録する。
     * 
     * @param param
     */
    private void createLcType(LcTypeRegisterParameter param) {
        Date now = new Date();
        LcType l = new LcType();
        l.setLcTypeId(Integer.parseInt(param.getLcTypeId()));
        l.setName(param.getLcTypeName());
        l.setCreateDate(now);
        l.setCreateUserId(getCurrentUser());
        l.setUpdateDate(now);
        l.setUpdateUserId(getCurrentUser());
        persist(l);
    }

    /**
     * 回線種別情報を削除する。
     * 
     * @param l
     */
    private void removeLcType(LcType l) {
        remove(l);
    }

    /**
     * 回線種別情報を更新する。
     * 
     * @param param
     * @param lcType
     */
    private void modifyLcType(LcTypeRegisterParameter param, LcType lcType) {
        Date now = new Date();
        lcType.setName(param.getLcTypeName());
        lcType.setUpdateDate(now);
        lcType.setUpdateUserId(getCurrentUser());
        merge(lcType);
    }

    /**
     * レスポンス用データを構築する。
     * 
     * @param res
     * @param lcType
     * @param num
     * @param status
     * @return
     */
    private StringBuilder createResponse(StringBuilder res, String lcType,
            int num, int status) {

        res.append("\n");

        res.append(num);
        res.append(",");
        if (lcType != null) {
            res.append(lcType);
        }
        res.append(",");
        res.append(status);
        return res;
    }

    private class ParameterProcessor implements InvalidParameterProcessor {

        private final LcTypeRegisterResult _result;
        private final int _status;

        ParameterProcessor(LcTypeRegisterResult result, int status) {
            this._result = result;
            this._status = status;
        }

        @Override
        public void process() {
            _result.setStatus(_status);
        }
    }

}
