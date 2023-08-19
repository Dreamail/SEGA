/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.prdcheck.game;

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
import jp.co.sega.allnet.auth.common.entity.PrdCheckGame;
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
@Component("prdCheckGameRegisterService")
@Scope("singleton")
@Transactional
public class PrdCheckGameRegisterServiceBean extends AbstractRegisterService
        implements PrdCheckGameRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(PrdCheckGameRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor _msg;

    @Override
    public RegisterServiceResult<PrdCheckGameRegisterResult> registerPrdCheckGameForAdmin(
            String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            List<PrdCheckGameRegisterResult> results = new ArrayList<PrdCheckGameRegisterResult>();
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                PrdCheckGameRegisterResult r = registerPrdCheckGame(line);
                if (r.getStatus() != AdminRegisterConstants.STATUS_SUCCESS) {
                    cnt++;
                }
                results.add(r);
            }

            return new RegisterServiceResult<PrdCheckGameRegisterResult>(cnt,
                    results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * キーチップ生産情報チェック対象ゲーム情報を登録する。
     * 
     * @param line
     * @return
     */
    private PrdCheckGameRegisterResult registerPrdCheckGame(String[] line) {

        PrdCheckGameRegisterParameter param = new PrdCheckGameRegisterParameter(
                line);

        PrdCheckGameRegisterResult result = new PrdCheckGameRegisterResult(
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

        // キーチップ生産情報チェック対象ゲーム情報を取得
        PrdCheckGame prdCheckGame = findPrdCheckGame(param.getGameId());

        if (param.getRegisterFlag().equals(
                AdminRegisterConstants.REGISTER_FLAG_REG)) {
            if (prdCheckGame != null) {
                result.setMessage(_msg
                        .getMessage("adminregister.status.registered"));
                result.setStatus(AdminRegisterConstants.STATUS_EXIST_TARGET);
                return result;
            }
            // 登録
            createPrdCheckGame(param.getGameId());
        } else {
            if (prdCheckGame == null) {
                result.setMessage(_msg
                        .getMessage("adminregister.status.noRemoveTarget"));
                result.setStatus(AdminRegisterConstants.STATUS_NO_TARGET);
                return result;
            }
            // 削除
            removePrdCheckGame(prdCheckGame);
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
    private boolean checkParameter(PrdCheckGameRegisterResult result) {
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
     * キーチップ生産情報チェック対象ゲーム情報を取得する。
     * 
     * @param gameId
     * @return
     */
    private PrdCheckGame findPrdCheckGame(String gameId) {
        return _em.find(PrdCheckGame.class, gameId);
    }

    /**
     * キーチップ生産情報チェック対象ゲーム情報を登録する。
     * 
     * @param gameId
     */
    private void createPrdCheckGame(String gameId) {
        PrdCheckGame p = new PrdCheckGame();
        p.setGameId(gameId);
        p.setCreateDate(new Date());
        p.setCreateUserId(getCurrentUser());
        persist(p);
    }

    /**
     * キーチップ生産情報チェック対象ゲーム情報を削除する。
     * 
     * @param b
     */
    private void removePrdCheckGame(PrdCheckGame p) {
        remove(p);
    }

    private class ParameterProcessor implements InvalidParameterProcessor {

        private final PrdCheckGameRegisterResult _result;

        private final int _status;

        private final String _msg;

        ParameterProcessor(PrdCheckGameRegisterResult result, int status,
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
