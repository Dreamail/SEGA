/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authallowed.comp;

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

import jp.co.sega.allnet.auth.common.entity.AuthAllowedComp;
import jp.co.sega.allnet.auth.common.entity.AuthAllowedCompPK;
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
@Component("authAllowedCompRegisterService")
@Scope("singleton")
@Transactional
public class AuthAllowedCompRegisterServiceBean extends AbstractRegisterService
        implements AuthAllowedCompRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(AuthAllowedCompRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor _msg;

    @Override
    public String registerAuthAllowedComp(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {

                AuthAllowedCompRegisterResult r = registerAuthAllowedComp(line);

                // レスポンスを構築
                createResponse(res, r.getParam().getCompCode(), r.getParam()
                        .getGameId(), r.getMessage(), num, r.getStatus());

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

            res.append("\nEND,,,,");
            res.append(success);

            return res.toString();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public RegisterServiceResult<AuthAllowedCompRegisterResult> registerAuthAllowedCompForAdmin(
            String val) {
        _log.info(formatCsvValueLog(val));

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            List<AuthAllowedCompRegisterResult> results = new ArrayList<AuthAllowedCompRegisterResult>();
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                AuthAllowedCompRegisterResult r = registerAuthAllowedComp(line);
                if (r.getStatus() != AdminRegisterConstants.STATUS_SUCCESS) {
                    cnt++;
                }
                results.add(r);
            }
            return new RegisterServiceResult<AuthAllowedCompRegisterResult>(
                    cnt, results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 包括先認証情報を登録する。
     * 
     * @param line
     * @return
     */
    private AuthAllowedCompRegisterResult registerAuthAllowedComp(String[] line) {

        AuthAllowedCompRegisterParameter param = new AuthAllowedCompRegisterParameter(
                line);

        AuthAllowedCompRegisterResult result = new AuthAllowedCompRegisterResult(
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

        // 包括先認証情報を取得
        AuthAllowedComp authAllowedComp = findAuthAllowedComp(game.getGameId(),
                comp.getCompCode());

        if (param.getRegisterFlag().equals(
                AdminRegisterConstants.REGISTER_FLAG_REG)) {
            if (authAllowedComp != null) {
                result.setStatus(AdminRegisterConstants.STATUS_EXIST_TARGET);
                result.setMessage(_msg
                        .getMessage("adminregister.status.registered"));
                return result;
            }

            // 包括先認証情報を登録
            createAuthAllowedComp(game.getGameId(), comp.getCompCode());

        } else {
            if (authAllowedComp == null) {
                result.setStatus(AdminRegisterConstants.STATUS_NO_TARGET);
                result.setMessage(_msg
                        .getMessage("adminregister.status.noRemoveTarget"));
                return result;
            }

            // 包括先認証情報を削除
            removeAuthAllowedComp(authAllowedComp);
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
    private boolean checkParameter(AuthAllowedCompRegisterResult result) {
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
     * @param gameId
     * @return
     */
    private Game findGame(String gameId) {
        return _em.find(Game.class, gameId);

    }

    /**
     * 包括先認証情報を取得する。
     * 
     * @param gameId
     * @param compCode
     * @return
     */
    private AuthAllowedComp findAuthAllowedComp(String gameId, String compCode) {
        AuthAllowedCompPK pk = new AuthAllowedCompPK();
        pk.setGameId(gameId);
        pk.setCompCode(compCode);
        return _em.find(AuthAllowedComp.class, pk);
    }

    /**
     * 包括先認証情報を登録する。
     * 
     * @param gameId
     * @param compCode
     */
    private void createAuthAllowedComp(String gameId, String compCode) {
        AuthAllowedCompPK pk = new AuthAllowedCompPK();
        pk.setGameId(gameId);
        pk.setCompCode(compCode);
        AuthAllowedComp c = new AuthAllowedComp();
        c.setPk(pk);
        c.setCreateDate(new Date());
        c.setCreateUserId(getCurrentUser());
        persist(c);
    }

    /**
     * 包括先認証情報を削除する。
     * 
     * @param c
     */
    private void removeAuthAllowedComp(AuthAllowedComp c) {
        remove(c);
    }

    /**
     * レスポンスを作成する。
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

        private final AuthAllowedCompRegisterResult _result;
        private final int _status;
        private final String _msg;

        ParameterProcessor(AuthAllowedCompRegisterResult result, int status,
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
