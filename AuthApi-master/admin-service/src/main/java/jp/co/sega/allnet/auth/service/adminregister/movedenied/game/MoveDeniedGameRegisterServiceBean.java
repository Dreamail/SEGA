/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.game;

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

import jp.co.sega.allnet.auth.common.entity.Game;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedGame;
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
@Component("moveDeniedGameRegisterService")
@Scope("singleton")
@Transactional
public class MoveDeniedGameRegisterServiceBean extends AbstractRegisterService
        implements MoveDeniedGameRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(MoveDeniedGameRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor _msg;

    @Override
    public String registerMoveDeniedGame(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {

                MoveDeniedGameRegisterResult r = registerMoveDeniedGame(line);

                // レスポンスを構築
                createResponse(res, r.getParam().getGameId(), r.getMessage(),
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
    public RegisterServiceResult<MoveDeniedGameRegisterResult> registerMoveDeniedGameForAdmin(
            String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            List<MoveDeniedGameRegisterResult> results = new ArrayList<MoveDeniedGameRegisterResult>();
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                MoveDeniedGameRegisterResult r = registerMoveDeniedGame(line);
                if (r.getStatus() != AdminRegisterConstants.STATUS_SUCCESS) {
                    cnt++;
                }
                results.add(r);
            }

            return new RegisterServiceResult<MoveDeniedGameRegisterResult>(cnt,
                    results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 同一包括先内自動移設不許可ゲーム情報を登録する。
     * 
     * @param line
     * @return
     */
    private MoveDeniedGameRegisterResult registerMoveDeniedGame(String[] line) {

        MoveDeniedGameRegisterParameter param = new MoveDeniedGameRegisterParameter(
                line);

        MoveDeniedGameRegisterResult result = new MoveDeniedGameRegisterResult(
                param);

        // 入力チェック
        if (!checkParameter(result)) {
            return result;
        }

        // ゲーム情報を取得
        Game game = findGame(param.getGameId());
        if (game == null) {
            result.setMessage(_msg.getMessage("adminregister.status.noGame"));
            result.setStatus(AdminRegisterConstants.STATUS_ERROR_NO_GAME);
            return result;
        }
        // 同一包括先内自動移設不許可ゲーム情報を取得
        MoveDeniedGame moveDeniedGame = findMoveDeniedGame(param.getGameId());

        if (param.getRegisterFlag().equals(
                AdminRegisterConstants.REGISTER_FLAG_REG)) {
            if (moveDeniedGame != null) {
                result.setMessage(_msg
                        .getMessage("adminregister.status.registered"));
                result.setStatus(AdminRegisterConstants.STATUS_EXIST_TARGET);
                return result;
            }
            // 登録
            createMoveDeniedGame(param.getGameId());
        } else {
            if (moveDeniedGame == null) {
                result.setMessage(_msg
                        .getMessage("adminregister.status.noRemoveTarget"));
                result.setStatus(AdminRegisterConstants.STATUS_NO_TARGET);
                return result;
            }
            // 削除
            removeMoveDeniedGame(moveDeniedGame);
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
    private boolean checkParameter(MoveDeniedGameRegisterResult result) {
        Map<String, InvalidParameterProcessor> map = new HashMap<String, InvalidParameterProcessor>();
        map.put("registerFlag",
                new ParameterProcessor(
                        result,
                        AdminRegisterConstants.STATUS_ERROR_INVALID_REGISTER_FLAG,
                        _msg.getMessage("adminregister.status.invalidRegisterFlag")));
        map.put("gameId",
                new ParameterProcessor(
                        result,
                        AdminRegisterConstants.STATUS_ERROR_INVALID_PARAMETER,
                        _msg.getMessage("adminregister.status.invalidParameterFormat")));
        return checkParameter(result.getParam(), map, false);
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
     * 同一包括先内自動移設不許可ゲーム情報を取得する。
     * 
     * @param gameId
     * @return
     */
    private MoveDeniedGame findMoveDeniedGame(String gameId) {
        return _em.find(MoveDeniedGame.class, gameId);
    }

    /**
     * 同一包括先内自動移設不許可ゲーム情報を登録する。
     * 
     * @param gameId
     */
    private void createMoveDeniedGame(String gameId) {
        MoveDeniedGame g = new MoveDeniedGame();
        g.setGameId(gameId);
        g.setCreateDate(new Date());
        g.setCreateUserId(getCurrentUser());
        persist(g);
    }

    /**
     * 同一包括先内自動移設不許可ゲーム情報を削除する。
     * 
     * @param b
     */
    private void removeMoveDeniedGame(MoveDeniedGame g) {
        remove(g);
    }

    /**
     * レスポンス用データを構築する。
     * 
     * @param res
     * @param gameId
     * @param msg
     * @param num
     * @param status
     * @return
     */
    private StringBuilder createResponse(StringBuilder res, String gameId,
            String msg, int num, int status) {

        res.append("\n");

        res.append(num);
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

        private final MoveDeniedGameRegisterResult _result;

        private final int _status;

        private final String _msg;

        ParameterProcessor(MoveDeniedGameRegisterResult result, int status,
                String msg) {
            this._result = result;
            this._status = status;
            this._msg = msg;
        }

        @Override
        public void process() {
            _result.setStatus(_status);
            _result.setMessage(_msg);
        }
    }

}
