/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.comp;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.Comp;
import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.AdminRegisterConstants;
import jp.co.sega.allnet.auth.service.adminregister.InvalidParameterProcessor;
import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

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
@Component("compRegisterService")
@Scope("singleton")
@Transactional
public class CompRegisterServiceBean extends AbstractRegisterService implements
        CompRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(CompRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Override
    public String registerComp(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {

                CompRegisterParameter param = new CompRegisterParameter(line);

                CompRegisterResult r = new CompRegisterResult(param);

                // 入力チェック
                Map<String, InvalidParameterProcessor> map = new HashMap<String, InvalidParameterProcessor>();
                map.put("compCode",
                        new ParameterProcessor(
                                r,
                                AdminRegisterConstants.N_MASTER_REGISTER_STATUS_ERROR_COMP_CODE));
                map.put("compName",
                        new ParameterProcessor(
                                r,
                                AdminRegisterConstants.N_MASTER_REGISTER_STATUS_ERROR_COMP_NAME));
                if (!checkParameter(param, map, false)) {
                    createResponse(res, param.getCompCode(), num, r.getStatus());
                    num++;
                    continue;
                }

                registerComp(r);

                // レスポンスの作成
                createResponse(res, param.getCompCode(), num, r.getStatus());

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

    @Override
    public RegisterServiceResult<CompRegisterResult> registerCompForAdmin(
            String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;

            List<CompRegisterResult> results = new ArrayList<CompRegisterResult>();
            int cnt = 0;
            while ((line = reader.readNext()) != null) {

                AdminCompRegisterParameter param = new AdminCompRegisterParameter(
                        line);

                CompRegisterResult r = new CompRegisterResult(param);

                // 入力チェック
                Map<String, InvalidParameterProcessor> map = new HashMap<String, InvalidParameterProcessor>();
                map.put("compCode", new ParameterProcessor(r,
                        AdminRegisterConstants.STATUS_ERROR_INVALID_PARAMETER));
                map.put("compName", new ParameterProcessor(r,
                        AdminRegisterConstants.STATUS_ERROR_INVALID_PARAMETER));
                map.put("registerFlag",
                        new ParameterProcessor(
                                r,
                                AdminRegisterConstants.STATUS_ERROR_INVALID_REGISTER_FLAG));

                if (!checkParameter(param, map, false)) {
                    results.add(r);
                    cnt++;
                    continue;
                }

                registerComp(r);
                results.add(r);

                if (r.getStatus() < 1) {
                    cnt++;
                }

            }

            return new RegisterServiceResult<CompRegisterResult>(cnt, results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 包括先情報を登録する。
     * 
     * @param result
     */
    private void registerComp(CompRegisterResult result) {

        // 包括先情報の取得
        Comp comp = findComp(result.getParam().getCompCode());

        if (comp == null) {

            if (result.getParam().getDeleteFlag() != null
                    && result.getParam().getDeleteFlag()
                            .equals(AdminRegisterConstants.DELETE_FLAG_DEL)) {
                result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_SKIP);
                return;
            }

            // 包括先情報を登録
            createComp(result.getParam());

            result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_REGISTER);
            return;

        }

        if (result.getParam().getDeleteFlag() != null
                && result.getParam().getDeleteFlag()
                        .equals(AdminRegisterConstants.DELETE_FLAG_DEL)) {
            // 包括先情報を削除
            removeComp(comp);

            result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_DELETE);
            return;

        }

        if (result.getParam().getCompName().equals(comp.getName())) {
            result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_SKIP);
            return;
        }

        // 包括先情報を更新
        modifyComp(result.getParam(), comp);

        result.setStatus(AdminRegisterConstants.N_MASTER_REGISTER_STATUS_MODIFY);
        return;

    }

    /**
     * 包括先情報を取得する。
     * 
     * @param compCode
     * @return
     */
    private Comp findComp(String compCode) {
        return _em.find(Comp.class, compCode);

    }

    /**
     * 包括先情報を登録する。
     * 
     * @param param
     */
    private void createComp(CompRegisterParameter param) {
        Date now = new Date();
        Comp c = new Comp();
        c.setCompCode(param.getCompCode());
        c.setName(param.getCompName());
        c.setCreateDate(now);
        c.setCreateUserId(getCurrentUser());
        c.setUpdateDate(now);
        c.setUpdateUserId(getCurrentUser());
        persist(c);
    }

    /**
     * 包括先情報を削除する。
     * 
     * @param c
     */
    private void removeComp(Comp c) {
        remove(c);
    }

    /**
     * 包括先情報を更新する。
     * 
     * @param param
     * @param comp
     */
    private void modifyComp(CompRegisterParameter param, Comp comp) {
        Date now = new Date();
        comp.setName(param.getCompName());
        comp.setUpdateDate(now);
        comp.setUpdateUserId(getCurrentUser());
        merge(comp);
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

        private final CompRegisterResult _result;
        private final int _status;

        ParameterProcessor(CompRegisterResult result, int status) {
            this._result = result;
            this._status = status;
        }

        @Override
        public void process() {
            _result.setStatus(_status);

        }
    }

}
