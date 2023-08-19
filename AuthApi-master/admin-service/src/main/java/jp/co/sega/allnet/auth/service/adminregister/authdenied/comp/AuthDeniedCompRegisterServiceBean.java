/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authdenied.comp;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.AuthDeniedComp;
import jp.co.sega.allnet.auth.common.entity.Comp;
import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.AdminRegisterConstants;
import jp.co.sega.allnet.auth.service.adminregister.InvalidParameterProcessor;
import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVReader;

/**
 * @author NakanoY
 * 
 */
@Component("authDeniedCompRegisterService")
@Scope("singleton")
@Transactional
public class AuthDeniedCompRegisterServiceBean extends AbstractRegisterService
        implements AuthDeniedCompRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(AuthDeniedCompRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor _msg;

    @Override
    public String registerAuthDeniedComp(String val) {

        _log.info(formatCsvValueLog(val));

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {

                AuthDeniedCompRegisterResult r = registerAuthDeniedComp(line);

                // レスポンスを構築
                createResponse(res, r.getParam().getCompCode(), r.getMessage(),
                        num, r.getStatus());
                // インクリメント
                num++;
                if (r.getStatus() == AdminRegisterConstants.STATUS_SUCCESS) {
                    success++;
                }
            }

            res.append("\nEND,,,");
            res.append(success);

            return res.toString();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public RegisterServiceResult<AuthDeniedCompRegisterResult> registerAuthDeniedCompForAdmin(
            String val) {

        _log.info(formatCsvValueLog(val));

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            List<AuthDeniedCompRegisterResult> results = new ArrayList<AuthDeniedCompRegisterResult>();
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                AuthDeniedCompRegisterResult r = registerAuthDeniedComp(line);
                if (r.getStatus() != AdminRegisterConstants.STATUS_SUCCESS) {
                    cnt++;
                }
                results.add(r);
            }

            return new RegisterServiceResult<AuthDeniedCompRegisterResult>(cnt,
                    results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 自動認証不許可包括先情報を登録する。
     * 
     * @param line
     * @return
     */
    private AuthDeniedCompRegisterResult registerAuthDeniedComp(String[] line) {

        AuthDeniedCompRegisterParameter param = new AuthDeniedCompRegisterParameter(
                line);
        AuthDeniedCompRegisterResult result = new AuthDeniedCompRegisterResult(
                param);

        // 入力チェック
        if (!checkParameter(result)) {
            return result;
        }

        // 包括先情報の取得
        Comp comp = findComp(param.getCompCode());

        if (comp == null) {
            result.setStatus(AdminRegisterConstants.STATUS_ERROR_NO_COMP);
            result.setMessage(_msg.getMessage("adminregister.status.noComp"));
            return result;
        }

        // 自動認証不許可包括先情報を取得
        AuthDeniedComp authDeniedComp = findAuthDeniedComp(comp.getCompCode());

        if (param.getRegisterFlag().equals(
                AdminRegisterConstants.REGISTER_FLAG_REG)) {
            if (authDeniedComp != null) {
                result.setStatus(AdminRegisterConstants.STATUS_EXIST_TARGET);
                result.setMessage(_msg
                        .getMessage("adminregister.status.registered"));
                return result;
            }

            // 自動認証不許可包括先情報を登録
            createAuthDeniedComp(comp.getCompCode());

        } else {
            if (authDeniedComp == null) {
                result.setStatus(AdminRegisterConstants.STATUS_NO_TARGET);
                result.setMessage(_msg
                        .getMessage("adminregister.status.noRemoveTarget"));
                return result;
            }

            // 包括先認証情報を削除
            removeAuthDeniedComp(authDeniedComp);

        }

        result.setStatus(AdminRegisterConstants.STATUS_SUCCESS);
        result.setMessage(_msg.getMessage("adminregister.status.success"));
        return result;
    }

    private boolean checkParameter(AuthDeniedCompRegisterResult result) {
        Map<String, InvalidParameterProcessor> map = new HashMap<String, InvalidParameterProcessor>();
        map.put("registerFlag",
                new ParameterProcessor(
                        result,
                        AdminRegisterConstants.STATUS_ERROR_INVALID_REGISTER_FLAG,
                        _msg.getMessage("adminregister.status.invalidRegisterFlag")));
        map.put("compCode",
                new ParameterProcessor(
                        result,
                        AdminRegisterConstants.STATUS_ERROR_INVALID_PARAMETER,
                        _msg.getMessage("adminregister.status.invalidParameterFormat")));
        return checkParameter(result.getParam(), map, false);
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
     * 自動認証不許可包括先情報を取得する。
     * 
     * @param compCode
     * @return
     */
    private AuthDeniedComp findAuthDeniedComp(String compCode) {
        return _em.find(AuthDeniedComp.class, compCode);
    }

    /**
     * 自動認証不許可包括先情報を登録する。
     * 
     * @param compCode
     */
    private void createAuthDeniedComp(String compCode) {

        AuthDeniedComp c = new AuthDeniedComp();
        c.setCompCode(compCode);
        c.setCreateDate(new Date());
        c.setCreateUserId(getCurrentUser());
        persist(c);
    }

    /**
     * 自動認証不許可包括先情報を削除する。
     * 
     * @param c
     */
    private void removeAuthDeniedComp(AuthDeniedComp c) {
        remove(c);
    }

    /**
     * レスポンス用データを構築する。
     * 
     * @param res
     * @param compCode
     * @param msg
     * @param num
     * @param status
     * @return
     */
    private StringBuilder createResponse(StringBuilder res, String compCode,
            String msg, int num, int status) {

        res.append("\n");

        res.append(num);
        res.append(",");
        if (compCode != null) {
            res.append(compCode);
        }
        res.append(",");
        res.append(msg);
        res.append(",");
        res.append(status);
        return res;
    }

    private class ParameterProcessor implements InvalidParameterProcessor {

        private final AuthDeniedCompRegisterResult _result;
        private final int _status;
        private final String _msg;

        ParameterProcessor(AuthDeniedCompRegisterResult result, int status,
                String msg) {
            this._result = result;
            this._status = status;
            this._msg = msg;
        }

        @Override
        public void process() {
            _result.setMessage(_msg);
            _result.setStatus(_status);
        }
    }

}
