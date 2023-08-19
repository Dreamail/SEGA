/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authdenied.bill;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jp.co.sega.allnet.auth.common.entity.AuthDeniedBill;
import jp.co.sega.allnet.auth.common.entity.Bill;
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
 * @author TsuboiY
 * 
 */
@Component("authDeniedBillRegisterService")
@Scope("singleton")
@Transactional
public class AuthDeniedBillRegisterServiceBean extends AbstractRegisterService
        implements AuthDeniedBillRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(AuthDeniedBillRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor _msg;

    @Override
    public String registerAuthDeniedBill(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {

                AuthDeniedBillRegisterResult r = registerAuthDeniedBill(line);

                // レスポンスを構築
                createResponse(res, r.getParam().getBillCode(), r.getMessage(),
                        num, r.getStatus());
                // インクリメント
                num++;
                if (r.getStatus() >= AdminRegisterConstants.STATUS_SUCCESS) {
                    if (r.getStatus() == AdminRegisterConstants.STATUS_SUCCESS) {
                        success++;
                    }
                    _log.info(formatCsvStatusLog(r.getStatus()));
                } else {
                    _log.warn(formatCsvStatusLog(r.getStatus()));
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
    public RegisterServiceResult<AuthDeniedBillRegisterResult> registerAuthDeniedBillForAdmin(
            String val) {
        _log.info(formatCsvValueLog(val));

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            List<AuthDeniedBillRegisterResult> results = new ArrayList<AuthDeniedBillRegisterResult>();
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                AuthDeniedBillRegisterResult r = registerAuthDeniedBill(line);
                if (r.getStatus() != AdminRegisterConstants.STATUS_SUCCESS) {
                    cnt++;
                }
                results.add(r);
            }

            return new RegisterServiceResult<AuthDeniedBillRegisterResult>(cnt,
                    results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 自動認証不許可請求先情報を登録する。
     * 
     * @param line
     * @return
     */
    private AuthDeniedBillRegisterResult registerAuthDeniedBill(String[] line) {

        AuthDeniedBillRegisterParameter param = new AuthDeniedBillRegisterParameter(
                line);
        AuthDeniedBillRegisterResult result = new AuthDeniedBillRegisterResult(
                param);

        // 入力チェック
        if (!checkParameter(result)) {
            return result;
        }

        // 請求先を取得
        Bill bill = findBill(param.getBillCode());
        if (bill == null) {
            result.setStatus(AdminRegisterConstants.STATUS_ERROR_NO_PLACE);
            result.setMessage(_msg.getMessage("adminregister.status.noPlace"));
            return result;
        }
        // 自動認証不許可請求先情報を取得
        AuthDeniedBill authDeniedBill = findAuthDeniedBill(param.getBillCode());

        if (param.getRegisterFlag().equals(
                AdminRegisterConstants.REGISTER_FLAG_REG)) {
            if (authDeniedBill != null) {
                result.setStatus(AdminRegisterConstants.STATUS_EXIST_TARGET);
                result.setMessage(_msg
                        .getMessage("adminregister.status.registered"));
                return result;
            }
            // 登録
            createAuthDeniedBill(param.getBillCode());
        } else {
            if (authDeniedBill == null) {
                result.setStatus(AdminRegisterConstants.STATUS_NO_TARGET);
                result.setMessage(_msg
                        .getMessage("adminregister.status.noRemoveTarget"));
                return result;
            }
            // 削除
            removeAuthDeniedBill(authDeniedBill);
        }

        result.setStatus(AdminRegisterConstants.STATUS_SUCCESS);
        result.setMessage(_msg.getMessage("adminregister.status.success"));
        return result;

    }

    /**
     * パラメータチェックを行う。
     * 
     * @param result
     * @return
     */
    private boolean checkParameter(AuthDeniedBillRegisterResult result) {
        Map<String, InvalidParameterProcessor> map = new HashMap<String, InvalidParameterProcessor>();
        map.put("registerFlag",
                new ParameterProcessor(
                        result,
                        AdminRegisterConstants.STATUS_ERROR_INVALID_REGISTER_FLAG,
                        _msg.getMessage("adminregister.status.invalidRegisterFlag")));
        map.put("billCode",
                new ParameterProcessor(
                        result,
                        AdminRegisterConstants.STATUS_ERROR_INVALID_PARAMETER,
                        _msg.getMessage("adminregister.status.invalidParameterFormat")));
        return checkParameter(result.getParam(), map, false);
    }

    /**
     * 店舗情報に存在する請求先を取得する。
     * 
     * @param billCode
     * @return
     */
    private Bill findBill(String billCode) {
        try {
            Query query = _em.createNamedQuery("findBillWherePlaceExist");
            query.setParameter("billCode", billCode);
            return (Bill) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 自動認証不許可請求先情報を取得する。
     * 
     * @param billCode
     * @return
     */
    private AuthDeniedBill findAuthDeniedBill(String billCode) {
        return _em.find(AuthDeniedBill.class, billCode);
    }

    /**
     * 自動認証不許可請求先情報を登録する。
     * 
     * @param billCode
     */
    private void createAuthDeniedBill(String billCode) {
        AuthDeniedBill b = new AuthDeniedBill();
        b.setBillCode(billCode);
        b.setCreateDate(new Date());
        b.setCreateUserId(getCurrentUser());
        persist(b);
    }

    /**
     * 自動認証不許可請求先情報を削除する。
     * 
     * @param b
     */
    private void removeAuthDeniedBill(AuthDeniedBill b) {
        remove(b);
    }

    /**
     * レスポンス用データを構築する。
     * 
     * @param res
     * @param billCode
     * @param msg
     * @param num
     * @param status
     * @return
     */
    private StringBuilder createResponse(StringBuilder res, String billCode,
            String msg, int num, int status) {

        res.append("\n");

        res.append(num);
        res.append(",");
        if (billCode != null) {
            res.append(billCode);
        }
        res.append(",");
        res.append(msg);
        res.append(",");
        res.append(status);
        return res;
    }

    private class ParameterProcessor implements InvalidParameterProcessor {

        private final AuthDeniedBillRegisterResult _result;
        private final int _status;
        private final String _msg;

        ParameterProcessor(AuthDeniedBillRegisterResult result, int status,
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
