/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authallowed.place;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.AuthAllowedPlace;
import jp.co.sega.allnet.auth.common.entity.AuthAllowedPlacePK;
import jp.co.sega.allnet.auth.common.entity.Game;
import jp.co.sega.allnet.auth.common.entity.Place;
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
 * @author TsuboiY
 * 
 */
@Component("authAllowedPlaceRegisterService")
@Scope("singleton")
@Transactional
public class AuthAllowedPlaceRegisterServiceBean extends
        AbstractRegisterService implements AuthAllowedPlaceRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(AuthAllowedPlaceRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Override
    public String registerAuthAllowedPlace(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {
                AuthAllowedPlaceRegisterResult r = regsterAuthAllowedPlace(line);
                // レスポンスを構築
                createResponse(res, r.getParam().getAllnetId(), r.getParam()
                        .getGameId(), num, r.getStatus());
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
    public RegisterServiceResult<AuthAllowedPlaceRegisterResult> registerAuthAllowedPlaceForAdmin(
            String val) {
        _log.info(formatCsvValueLog(val));

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            List<AuthAllowedPlaceRegisterResult> results = new ArrayList<AuthAllowedPlaceRegisterResult>();
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                AuthAllowedPlaceRegisterResult r = regsterAuthAllowedPlace(line);
                if (r.getStatus() != AdminRegisterConstants.STATUS_SUCCESS) {
                    cnt++;
                }
                results.add(r);
            }

            return new RegisterServiceResult<AuthAllowedPlaceRegisterResult>(
                    cnt, results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 店舗認証情報を登録する。
     * 
     * @param line
     * @return
     */
    private AuthAllowedPlaceRegisterResult regsterAuthAllowedPlace(String[] line) {

        AuthAllowedPlaceRegisterParameter param = new AuthAllowedPlaceRegisterParameter(
                line);

        AuthAllowedPlaceRegisterResult result = new AuthAllowedPlaceRegisterResult(
                param);

        // 入力チェック
        if (!checkParameter(result)) {
            return result;
        }

        long allnetId = Long.parseLong(param.getAllnetId());

        // 店舗情報の存在をチェック
        if (!checkPlace(allnetId)) {
            result.setStatus(AdminRegisterConstants.STATUS_ERROR_NO_PLACE);
            return result;
        }

        // ゲーム情報の存在をチェック
        if (!checkGame(param.getGameId())) {
            result.setStatus(AdminRegisterConstants.STATUS_ERROR_NO_GAME);
            return result;
        }

        // 店舗認証情報を取得
        AuthAllowedPlace authAllowedPlace = findAuthAllowedPlace(allnetId,
                param.getGameId());

        if (param.getMode() != null
                && param.getMode().equals(MODE_FLAG_REGISTER)) {
            // 登録処理
            if (authAllowedPlace != null) {
                result.setStatus(AdminRegisterConstants.STATUS_EXIST_TARGET);
                return result;
            }
            createAuthAllowedPlace(allnetId, param.getGameId());
        } else {
            // 削除処理
            if (authAllowedPlace == null) {
                result.setStatus(AdminRegisterConstants.STATUS_NO_TARGET);
                return result;
            }
            removeAuthAllowedPlace(authAllowedPlace);
        }

        result.setStatus(AdminRegisterConstants.STATUS_SUCCESS);
        return result;
    }

    /**
     * パラメータチェックを行う。
     * 
     * @param result
     * @return
     */
    private boolean checkParameter(AuthAllowedPlaceRegisterResult result) {
        Map<String, InvalidParameterProcessor> map = new HashMap<String, InvalidParameterProcessor>();
        map.put("registerFlag", new ParameterProcessor(result,
                AdminRegisterConstants.STATUS_ERROR_INVALID_REGISTER_FLAG));
        map.put("allnetId", new ParameterProcessor(result,
                AdminRegisterConstants.STATUS_ERROR_NO_PLACE));
        map.put("gameId", new ParameterProcessor(result,
                AdminRegisterConstants.STATUS_ERROR_NO_GAME));
        return checkParameter(result.getParam(), map, false);
    }

    /**
     * 店舗情報の存在をチェックする。
     * 
     * @param allnetId
     * @return
     */
    private boolean checkPlace(long allnetId) {
        Place p = _em.find(Place.class, allnetId);
        if (p == null) {
            return false;
        }
        return true;
    }

    /**
     * ゲーム情報の存在をチェックする。
     * 
     * @param gameId
     * @return
     */
    private boolean checkGame(String gameId) {
        Game g = _em.find(Game.class, gameId);
        if (g == null) {
            return false;
        }
        return true;
    }

    /**
     * 店舗認証情報を取得する。
     * 
     * @param allnetId
     * @param gameId
     * @return
     */
    private AuthAllowedPlace findAuthAllowedPlace(long allnetId, String gameId) {
        AuthAllowedPlacePK pk = new AuthAllowedPlacePK();
        pk.setAllnetId(allnetId);
        pk.setGameId(gameId);
        return _em.find(AuthAllowedPlace.class, pk);
    }

    /**
     * 店舗認証情報を登録する。
     * 
     * @param allnetId
     * @param gameId
     */
    private void createAuthAllowedPlace(long allnetId, String gameId) {
        AuthAllowedPlacePK pk = new AuthAllowedPlacePK();
        pk.setAllnetId(allnetId);
        pk.setGameId(gameId);
        AuthAllowedPlace p = new AuthAllowedPlace();
        p.setPk(pk);
        p.setCreateDate(new Date());
        p.setCreateUserId(getCurrentUser());
        persist(p);
    }

    /**
     * 店舗認証情報を削除する。
     * 
     * @param p
     */
    private void removeAuthAllowedPlace(AuthAllowedPlace p) {
        remove(p);
    }

    /**
     * レスポンスを作成する。
     * 
     * @param res
     * @param allnetId
     * @param gameId
     * @param num
     * @param status
     * @return
     */
    private StringBuilder createResponse(StringBuilder res, String allnetId,
            String gameId, int num, int status) {

        res.append("\n");

        res.append(num);
        res.append(",");
        res.append(allnetId);
        res.append(",");
        res.append(gameId);
        res.append(",");
        res.append(status);
        return res;
    }

    private class ParameterProcessor implements InvalidParameterProcessor {

        private final AuthAllowedPlaceRegisterResult _result;
        private final int _status;

        ParameterProcessor(AuthAllowedPlaceRegisterResult result, int status) {
            this._result = result;
            this._status = status;
        }

        @Override
        public void process() {
            _result.setStatus(_status);
        }
    }
}
