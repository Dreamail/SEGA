/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.gamecomp;

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

import jp.co.sega.allnet.auth.common.entity.Comp;
import jp.co.sega.allnet.auth.common.entity.Game;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedGameComp;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedGameCompPK;
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
@Component("moveDeniedGameCompRegisterService")
@Scope("singleton")
@Transactional
public class MoveDeniedGameCompRegisterServiceBean extends
        AbstractRegisterService implements MoveDeniedGameCompRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(MoveDeniedGameCompRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor _msg;

    @Override
    public String registerMoveDeniedGameComp(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {

                MoveDeniedGameCompRegisterResult r = registerMoveDeniedGameComp(line);

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
    public RegisterServiceResult<MoveDeniedGameCompRegisterResult> registerMoveDeniedGameCompForAdmin(
            String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            List<MoveDeniedGameCompRegisterResult> results = new ArrayList<MoveDeniedGameCompRegisterResult>();
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                MoveDeniedGameCompRegisterResult r = registerMoveDeniedGameComp(line);
                if (r.getStatus() != AdminRegisterConstants.STATUS_SUCCESS) {
                    cnt++;
                }
                results.add(r);
            }

            return new RegisterServiceResult<MoveDeniedGameCompRegisterResult>(
                    cnt, results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 同一包括先内自動移設不許可ゲーム・包括先情報を登録する。
     * 
     * @param line
     * @return
     */
    private MoveDeniedGameCompRegisterResult registerMoveDeniedGameComp(
            String[] line) {

        MoveDeniedGameCompRegisterParameter param = new MoveDeniedGameCompRegisterParameter(
                line);

        MoveDeniedGameCompRegisterResult result = new MoveDeniedGameCompRegisterResult(
                param);

        // 入力チェック
        if (!checkParameter(result)) {
            return result;
        }

        // 包括先情報の取得
        Comp comp = findComp(param.getCompCode());

        if (comp == null) {
            result.setMessage(_msg.getMessage("adminregister.status.noComp"));
            result.setStatus(AdminRegisterConstants.STATUS_ERROR_NO_COMP);
            return result;
        }

        // ゲーム情報の取得
        Game game = findGame(param.getGameId());

        if (game == null) {
            result.setMessage(_msg.getMessage("adminregister.status.noGame"));
            result.setStatus(AdminRegisterConstants.STATUS_ERROR_NO_GAME);
            return result;
        }

        // 同一包括先内自動移設不許可ゲーム・包括先情報を取得
        MoveDeniedGameComp moveDeniedGameComp = findMoveDeniedGameComp(
                comp.getCompCode(), game.getGameId());

        if (param.getRegisterFlag().equals(
                AdminRegisterConstants.REGISTER_FLAG_REG)) {
            if (moveDeniedGameComp != null) {
                result.setMessage(_msg
                        .getMessage("adminregister.status.registered"));
                result.setStatus(AdminRegisterConstants.STATUS_EXIST_TARGET);
                return result;
            }

            // 同一包括先内自動移設不許可ゲーム・包括先情報を登録
            createMoveDeniedGameComp(comp.getCompCode(), game.getGameId());

        } else {
            if (moveDeniedGameComp == null) {
                result.setMessage(_msg
                        .getMessage("adminregister.status.noRemoveTarget"));
                result.setStatus(AdminRegisterConstants.STATUS_NO_TARGET);
                return result;
            }

            // 同一包括先内自動移設不許可ゲーム・包括先情報を削除
            removeMoveDeniedGameComp(moveDeniedGameComp);
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
    private boolean checkParameter(MoveDeniedGameCompRegisterResult result) {
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
     * 同一包括先内自動移設不許可ゲーム・包括先情報を取得する。
     * 
     * @param compCode
     * @param gameId
     * @return
     */
    private MoveDeniedGameComp findMoveDeniedGameComp(String compCode,
            String gameId) {
        MoveDeniedGameCompPK pk = new MoveDeniedGameCompPK();
        pk.setCompCode(compCode);
        pk.setGameId(gameId);
        return _em.find(MoveDeniedGameComp.class, pk);
    }

    /**
     * 同一包括先内自動移設不許可ゲーム・包括先情報を登録する。
     * 
     * @param compCode
     * @param gameId
     */
    private void createMoveDeniedGameComp(String compCode, String gameId) {

        MoveDeniedGameCompPK pk = new MoveDeniedGameCompPK();
        pk.setCompCode(compCode);
        pk.setGameId(gameId);
        MoveDeniedGameComp c = new MoveDeniedGameComp();
        c.setPk(pk);
        c.setCreateDate(new Date());
        c.setCreateUserId(getCurrentUser());
        persist(c);
    }

    /**
     * 同一包括先内自動移設不許可ゲーム・包括先情報を削除する。
     * 
     * @param c
     */
    private void removeMoveDeniedGameComp(MoveDeniedGameComp c) {
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

        private final MoveDeniedGameCompRegisterResult _result;

        private final int _status;

        private final String _msg;

        ParameterProcessor(MoveDeniedGameCompRegisterResult result, int status,
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
