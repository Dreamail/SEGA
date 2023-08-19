/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever;

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
import javax.persistence.Query;

import jp.co.sega.allnet.auth.common.entity.GameAttribute;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedGamever;
import jp.co.sega.allnet.auth.common.entity.MoveDeniedGameverPK;
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
@Component("moveDeniedGameVerRegisterService")
@Scope("singleton")
@Transactional
public class MoveDeniedGameVerRegisterServiceBean extends
        AbstractRegisterService implements MoveDeniedGameVerRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(MoveDeniedGameVerRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor _msg;

    @Override
    public String registerMoveDeniedGameVer(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {

                MoveDeniedGameVerRegisterResult r = registerMoveDeniedGameVer(line);

                // レスポンスを構築
                createResponse(res, r.getParam().getGameId(), r.getParam()
                        .getGameVer(), r.getMessage(), num, r.getStatus());
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
    public RegisterServiceResult<MoveDeniedGameVerRegisterResult> registerMoveDeniedGameVerForAdmin(
            String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            List<MoveDeniedGameVerRegisterResult> results = new ArrayList<MoveDeniedGameVerRegisterResult>();
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                MoveDeniedGameVerRegisterResult r = registerMoveDeniedGameVer(line);
                if (r.getStatus() != AdminRegisterConstants.STATUS_SUCCESS) {
                    cnt++;
                }
                results.add(r);
            }

            return new RegisterServiceResult<MoveDeniedGameVerRegisterResult>(
                    cnt, results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 同一包括先内自動移設不許可ゲームバージョン情報を登録する。
     * 
     * @param line
     * @return
     */
    private MoveDeniedGameVerRegisterResult registerMoveDeniedGameVer(
            String[] line) {

        MoveDeniedGameVerRegisterParameter param = new MoveDeniedGameVerRegisterParameter(
                line);

        MoveDeniedGameVerRegisterResult result = new MoveDeniedGameVerRegisterResult(
                param);

        // 入力チェック
        if (!checkParameter(result)) {
            return result;
        }

        // ゲーム情報を取得
        GameAttribute gameAttr = findGameAttribute(param.getGameId(),
                param.getGameVer());
        if (gameAttr == null) {
            result.setMessage(_msg.getMessage("adminregister.status.noGame"));
            result.setStatus(AdminRegisterConstants.STATUS_ERROR_NO_GAME);
            return result;
        }
        // 同一包括先内自動移設不許可ゲームバージョン情報を取得
        MoveDeniedGamever moveDeniedGamever = findMoveDeniedGamever(
                param.getGameId(), param.getGameVer());

        if (param.getRegisterFlag().equals(
                AdminRegisterConstants.REGISTER_FLAG_REG)) {
            if (moveDeniedGamever != null) {
                result.setMessage(_msg
                        .getMessage("adminregister.status.registered"));
                result.setStatus(AdminRegisterConstants.STATUS_EXIST_TARGET);
                return result;
            }
            // 登録
            createMoveDeniedGamever(param.getGameId(), param.getGameVer());
        } else {
            if (moveDeniedGamever == null) {
                result.setMessage(_msg
                        .getMessage("adminregister.status.noRemoveTarget"));
                result.setStatus(AdminRegisterConstants.STATUS_NO_TARGET);
                return result;
            }
            // 削除
            removeMoveDeniedGamever(moveDeniedGamever);
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
    private boolean checkParameter(MoveDeniedGameVerRegisterResult result) {
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
        map.put("gameVer",
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
    @SuppressWarnings("unchecked")
    private GameAttribute findGameAttribute(String gameId, String gameVer) {
        Query query = _em.createNamedQuery("findGameAttrByGameIdVer");
        query.setParameter("gameId", gameId);
        query.setParameter("gameVer", gameVer);
        List<GameAttribute> list = query.getResultList();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 同一包括先内自動移設不許可ゲームバージョン情報を取得する。
     * 
     * @param gameId
     * @param gameVer
     * @return
     */
    private MoveDeniedGamever findMoveDeniedGamever(String gameId,
            String gameVer) {
        MoveDeniedGameverPK pk = new MoveDeniedGameverPK();
        pk.setGameId(gameId);
        pk.setGameVer(gameVer);
        return _em.find(MoveDeniedGamever.class, pk);
    }

    /**
     * 同一包括先内自動移設不許可ゲームバージョン情報を登録する。
     * 
     * @param gameId
     * @param gameVer
     */
    private void createMoveDeniedGamever(String gameId, String gameVer) {
        MoveDeniedGameverPK pk = new MoveDeniedGameverPK();
        pk.setGameId(gameId);
        pk.setGameVer(gameVer);
        MoveDeniedGamever g = new MoveDeniedGamever();
        g.setPk(pk);
        g.setCreateDate(new Date());
        g.setCreateUserId(getCurrentUser());
        persist(g);
    }

    /**
     * 同一包括先内自動移設不許可ゲームバージョン情報を削除する。
     * 
     * @param g
     */
    private void removeMoveDeniedGamever(MoveDeniedGamever g) {
        remove(g);
    }

    /**
     * レスポンス用データを構築する。
     * 
     * @param res
     * @param gameId
     * @param gameVer
     * @param msg
     * @param num
     * @param status
     * @return
     */
    private StringBuilder createResponse(StringBuilder res, String gameId,
            String gameVer, String msg, int num, int status) {

        res.append("\n");

        res.append(num);
        res.append(",");
        if (gameId != null) {
            res.append(gameId);
        }
        res.append(",");
        if (gameVer != null) {
            res.append(gameVer);
        }
        res.append(",");
        res.append(msg);
        res.append(",");
        res.append(status);
        return res;
    }

    private class ParameterProcessor implements InvalidParameterProcessor {

        private final MoveDeniedGameVerRegisterResult _result;

        private final int _status;

        private final String _msg;

        ParameterProcessor(MoveDeniedGameVerRegisterResult result, int status,
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
