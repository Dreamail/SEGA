/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authdenied.gamebill;

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

import jp.co.sega.allnet.auth.common.entity.AuthDeniedGameBill;
import jp.co.sega.allnet.auth.common.entity.AuthDeniedGameBillPK;
import jp.co.sega.allnet.auth.common.entity.Bill;
import jp.co.sega.allnet.auth.common.entity.Game;
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
@Component("authDeniedGameBillRegisterService")
@Scope("singleton")
@Transactional
public class AuthDeniedGameBillRegisterServiceBean extends
        AbstractRegisterService implements AuthDeniedGameBillRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(AuthDeniedGameBillRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor _msg;

    @Override
    public String registerAuthDeniedGameBill(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {

                AuthDeniedGameBillRegisterResult r = registerAuthDeniedGameBill(line);

                // レスポンスを構築
                createResponse(res, r.getParam().getBillCode(), r.getParam()
                        .getGameId(), r.getMessage(), num, r.getStatus());
                // インクリメント
                num++;
                if (r.getStatus() == AdminRegisterConstants.STATUS_SUCCESS) {
                    success++;
                }
            }

            res.append("\nEND,,,,");
            res.append(success);

            return res.toString();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public RegisterServiceResult<AuthDeniedGameBillRegisterResult> registerAuthDeniedGameBillForAdmin(
            String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            List<AuthDeniedGameBillRegisterResult> results = new ArrayList<AuthDeniedGameBillRegisterResult>();
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                AuthDeniedGameBillRegisterResult r = registerAuthDeniedGameBill(line);
                if (r.getStatus() != AdminRegisterConstants.STATUS_SUCCESS) {
                    cnt++;
                }
                results.add(r);
            }

            return new RegisterServiceResult<AuthDeniedGameBillRegisterResult>(
                    cnt, results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 自動認証不許可ゲーム・請求先情報を登録する。
     * 
     * @param line
     * @return
     */
    private AuthDeniedGameBillRegisterResult registerAuthDeniedGameBill(
            String[] line) {

        AuthDeniedGameBillRegisterParameter param = new AuthDeniedGameBillRegisterParameter(
                line);
        AuthDeniedGameBillRegisterResult result = new AuthDeniedGameBillRegisterResult(
                param);

        // 入力チェック
        if (!checkParameter(result)) {
            return result;
        }

        // 請求先情報の取得
        Bill bill = findBill(param.getBillCode());

        if (bill == null) {
            result.setStatus(AdminRegisterConstants.STATUS_ERROR_NO_PLACE);
            result.setMessage(_msg.getMessage("adminregister.status.noPlace"));
            return result;
        }

        // ゲーム情報の取得
        Game game = findGame(param.getGameId());

        if (game == null) {
            result.setStatus(AdminRegisterConstants.STATUS_ERROR_NO_GAME);
            result.setMessage(_msg.getMessage("adminregister.status.noGame"));
            return result;
        }

        // 自動認証不許可ゲーム・請求先情報を取得
        AuthDeniedGameBill authDeniedGameBill = findAuthDeniedGameBill(
                bill.getBillCode(), game.getGameId());

        if (param.getRegisterFlag().equals(
                AdminRegisterConstants.REGISTER_FLAG_REG)) {
            if (authDeniedGameBill != null) {
                result.setStatus(AdminRegisterConstants.STATUS_EXIST_TARGET);
                result.setMessage(_msg
                        .getMessage("adminregister.status.registered"));
                return result;
            }

            // 自動認証不許可ゲーム・請求先情報を登録
            createAuthDeniedGameBill(bill.getBillCode(), game.getGameId());

        } else {
            if (authDeniedGameBill == null) {
                result.setStatus(AdminRegisterConstants.STATUS_NO_TARGET);
                result.setMessage(_msg
                        .getMessage("adminregister.status.noRemoveTarget"));
                return result;
            }

            // 自動認証不許可ゲーム・請求先情報を削除
            removeAuthDeniedGameBill(authDeniedGameBill);
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
    private boolean checkParameter(AuthDeniedGameBillRegisterResult result) {
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
        map.put("gameId",
                new ParameterProcessor(
                        result,
                        AdminRegisterConstants.STATUS_ERROR_INVALID_PARAMETER,
                        _msg.getMessage("adminregister.status.invalidParameterFormat")));
        return checkParameter(result.getParam(), map, false);
    }

    /**
     * 請求先情報を取得する。
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
     * ゲーム情報を取得する。
     * 
     * @param compGame
     * @return
     */
    private Game findGame(String gameId) {
        return _em.find(Game.class, gameId);

    }

    /**
     * 自動認証不許可ゲーム・請求先情報を取得する。
     * 
     * @param billCode
     * @param gameId
     * @return
     */
    private AuthDeniedGameBill findAuthDeniedGameBill(String billCode,
            String gameId) {
        AuthDeniedGameBillPK pk = new AuthDeniedGameBillPK();
        pk.setBillCode(billCode);
        pk.setGameId(gameId);
        return _em.find(AuthDeniedGameBill.class, pk);
    }

    /**
     * 自動認証不許可ゲーム・請求先情報を登録する。
     * 
     * @param billCode
     * @param gameId
     */
    private void createAuthDeniedGameBill(String billCode, String gameId) {

        AuthDeniedGameBillPK pk = new AuthDeniedGameBillPK();
        pk.setBillCode(billCode);
        pk.setGameId(gameId);
        AuthDeniedGameBill b = new AuthDeniedGameBill();
        b.setPk(pk);
        b.setCreateDate(new Date());
        b.setCreateUserId(getCurrentUser());
        persist(b);
    }

    /**
     * 自動認証不許可ゲーム・請求先情報を削除する。
     * 
     * @param b
     */
    private void removeAuthDeniedGameBill(AuthDeniedGameBill b) {
        remove(b);
    }

    /**
     * レスポンス用データを構築する。
     * 
     * @param res
     * @param compCode
     * @param gameId
     * @param msg
     * @param num
     * @param status
     * @return
     */
    private StringBuilder createResponse(StringBuilder res, String compCode,
            String gameId, String msg, int num, int status) {

        res.append("\n");

        res.append(num);
        res.append(",");
        if (compCode != null) {
            res.append(compCode);
        }
        res.append(",");
        if (gameId != null) {
            res.append(gameId);
        }
        res.append(",");
        res.append(msg);
        res.append(",");
        res.append(status);
        return res;
    }

    private class ParameterProcessor implements InvalidParameterProcessor {

        private final AuthDeniedGameBillRegisterResult _result;
        private final int _status;
        private final String _msg;

        ParameterProcessor(AuthDeniedGameBillRegisterResult result, int status,
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
