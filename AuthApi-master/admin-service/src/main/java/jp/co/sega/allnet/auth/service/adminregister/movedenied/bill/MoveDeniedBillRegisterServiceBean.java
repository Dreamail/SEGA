/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.bill;

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

import jp.co.sega.allnet.auth.common.entity.Bill;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedBill;
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
@Component("moveDeniedBillRegisterService")
@Scope("singleton")
@Transactional
public class MoveDeniedBillRegisterServiceBean extends AbstractRegisterService
        implements MoveDeniedBillRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(MoveDeniedBillRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor _msg;

    @Override
    public String registerMoveDeniedBill(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {
                MoveDeniedBillRegisterResult r = registerMoveDeniedBill(line);
                createResponse(res, r.getParam().getBillCode(), r.getMessage(),
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
    public RegisterServiceResult<MoveDeniedBillRegisterResult> registerMoveDeniedBillForAdmin(
            String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            List<MoveDeniedBillRegisterResult> results = new ArrayList<MoveDeniedBillRegisterResult>();
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                MoveDeniedBillRegisterResult r = registerMoveDeniedBill(line);
                if (r.getStatus() != AdminRegisterConstants.STATUS_SUCCESS) {
                    cnt++;
                }
                results.add(r);
            }

            return new RegisterServiceResult<MoveDeniedBillRegisterResult>(cnt,
                    results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 同一包括先内自動移設不許可請求先情報を登録する。
     * 
     * @param line
     * @return
     */
    private MoveDeniedBillRegisterResult registerMoveDeniedBill(String[] line) {

        MoveDeniedBillRegisterParameter param = new MoveDeniedBillRegisterParameter(
                line);
        MoveDeniedBillRegisterResult result = new MoveDeniedBillRegisterResult(
                param);

        // 入力チェック
        if (!checkParameter(result)) {
            return result;
        }

        // 請求先を取得
        Bill bill = findBill(param.getBillCode());
        if (bill == null) {
            result.setMessage(_msg.getMessage("adminregister.status.noPlace"));
            result.setStatus(AdminRegisterConstants.STATUS_ERROR_NO_PLACE);
            return result;
        }

        // 同一包括先内自動移設不許可請求先情報を取得
        MoveDeniedBill moveDeniedBill = findMoveDeniedBill(param.getBillCode());

        if (param.getRegisterFlag().equals(
                AdminRegisterConstants.REGISTER_FLAG_REG)) {
            if (moveDeniedBill != null) {
                result.setMessage(_msg
                        .getMessage("adminregister.status.registered"));
                result.setStatus(AdminRegisterConstants.STATUS_EXIST_TARGET);
                return result;
            }
            // 登録
            createMoveDeniedBill(param.getBillCode());
        } else {
            if (moveDeniedBill == null) {
                result.setMessage(_msg
                        .getMessage("adminregister.status.noRemoveTarget"));
                result.setStatus(AdminRegisterConstants.STATUS_NO_TARGET);
                return result;
            }
            // 削除
            removeMoveDeniedBill(moveDeniedBill);
        }

        result.setMessage(_msg.getMessage("adminregister.status.success"));
        result.setStatus(AdminRegisterConstants.STATUS_SUCCESS);
        return result;
    }

    /**
     * パラメータチェックを行う。
     * 
     * @param result
     * @return
     */
    private boolean checkParameter(MoveDeniedBillRegisterResult result) {
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
     * 同一包括先内自動移設不許可請求先情報を取得する。
     * 
     * @param billCode
     * @return
     */
    private MoveDeniedBill findMoveDeniedBill(String billCode) {
        return _em.find(MoveDeniedBill.class, billCode);
    }

    /**
     * 同一包括先内自動移設不許可請求先情報を登録する。
     * 
     * @param billCode
     */
    private void createMoveDeniedBill(String billCode) {
        MoveDeniedBill b = new MoveDeniedBill();
        b.setBillCode(billCode);
        b.setCreateDate(new Date());
        b.setCreateUserId(getCurrentUser());
        persist(b);
    }

    /**
     * 同一包括先内自動移設不許可請求先情報を削除する。
     * 
     * @param b
     */
    private void removeMoveDeniedBill(MoveDeniedBill b) {
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

        private MoveDeniedBillRegisterResult _result;

        private final int _status;

        private final String _msg;

        ParameterProcessor(MoveDeniedBillRegisterResult result, int status,
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
