/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authdenied.gamecomp;

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

import jp.co.sega.allnet.auth.common.entity.AuthDeniedGameComp;
import jp.co.sega.allnet.auth.common.entity.AuthDeniedGameCompPK;
import jp.co.sega.allnet.auth.common.entity.Comp;
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
@Component("authDeniedGameCompRegisterService")
@Scope("singleton")
@Transactional
public class AuthDeniedGameCompRegisterServiceBean extends
        AbstractRegisterService implements AuthDeniedGameCompRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(AuthDeniedGameCompRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor _msg;

    @Override
    public String registerAuthDeniedGameComp(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {
                AuthDeniedGameCompRegisterResult r = registerAuthDeniedGameComp(line);

                // レスポンスを構築
                createResponse(res, r.getParam().getCompCode(), r.getParam()
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
    public RegisterServiceResult<AuthDeniedGameCompRegisterResult> registerAuthDeniedGameCompForAdmin(
            String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            List<AuthDeniedGameCompRegisterResult> results = new ArrayList<AuthDeniedGameCompRegisterResult>();
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                AuthDeniedGameCompRegisterResult r = registerAuthDeniedGameComp(line);
                if (r.getStatus() != AdminRegisterConstants.STATUS_SUCCESS) {
                    cnt++;
                }
                results.add(r);
            }

            return new RegisterServiceResult<AuthDeniedGameCompRegisterResult>(
                    cnt, results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 自動認証不許可ゲーム・包括先情報の登録を行う。
     * 
     * @param line
     * @return
     */
    private AuthDeniedGameCompRegisterResult registerAuthDeniedGameComp(
            String[] line) {

        AuthDeniedGameCompRegisterParameter param = new AuthDeniedGameCompRegisterParameter(
                line);

        AuthDeniedGameCompRegisterResult result = new AuthDeniedGameCompRegisterResult(
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

        // ゲーム情報の取得
        Game game = findGame(param.getGameId());

        if (game == null) {
            result.setStatus(AdminRegisterConstants.STATUS_ERROR_NO_GAME);
            result.setMessage(_msg.getMessage("adminregister.status.noGame"));
            return result;
        }

        // 自動認証不許可ゲーム・包括先情報を取得
        AuthDeniedGameComp authDeniedGameComp = findAuthDeniedGameComp(
                comp.getCompCode(), game.getGameId());

        if (param.getRegisterFlag().equals(
                AdminRegisterConstants.REGISTER_FLAG_REG)) {
            if (authDeniedGameComp != null) {
                result.setStatus(AdminRegisterConstants.STATUS_EXIST_TARGET);
                result.setMessage(_msg
                        .getMessage("adminregister.status.registered"));
                return result;
            }

            // 自動認証不許可ゲーム・包括先情報を登録
            createAuthDeniedGameComp(comp.getCompCode(), game.getGameId());

        } else {
            if (authDeniedGameComp == null) {
                result.setStatus(AdminRegisterConstants.STATUS_NO_TARGET);
                result.setMessage(_msg
                        .getMessage("adminregister.status.noRemoveTarget"));
                return result;
            }

            // 自動認証不許可ゲーム・包括先情報を削除
            removeAuthDeniedGameComp(authDeniedGameComp);
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
    private boolean checkParameter(AuthDeniedGameCompRegisterResult result) {
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
        map.put("gameId",
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
     * ゲーム情報を取得する。
     * 
     * @param compGame
     * @return
     */
    private Game findGame(String gameId) {
        return _em.find(Game.class, gameId);

    }

    /**
     * 自動認証不許可ゲーム・包括先情報を取得する。
     * 
     * @param compCode
     * @param gameId
     * @return
     */
    private AuthDeniedGameComp findAuthDeniedGameComp(String compCode,
            String gameId) {
        AuthDeniedGameCompPK pk = new AuthDeniedGameCompPK();
        pk.setCompCode(compCode);
        pk.setGameId(gameId);
        return _em.find(AuthDeniedGameComp.class, pk);
    }

    /**
     * 自動認証不許可ゲーム・包括先情報を登録する。
     * 
     * @param compCode
     * @param gameId
     */
    private void createAuthDeniedGameComp(String compCode, String gameId) {

        AuthDeniedGameCompPK pk = new AuthDeniedGameCompPK();
        pk.setCompCode(compCode);
        pk.setGameId(gameId);
        AuthDeniedGameComp c = new AuthDeniedGameComp();
        c.setPk(pk);
        c.setCreateDate(new Date());
        c.setCreateUserId(getCurrentUser());
        persist(c);
    }

    /**
     * 自動認証不許可ゲーム・包括先情報を削除する。
     * 
     * @param c
     */
    private void removeAuthDeniedGameComp(AuthDeniedGameComp c) {
        remove(c);
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

        private final AuthDeniedGameCompRegisterResult _result;
        private final int _status;
        private final String _msg;

        ParameterProcessor(AuthDeniedGameCompRegisterResult result, int status,
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
