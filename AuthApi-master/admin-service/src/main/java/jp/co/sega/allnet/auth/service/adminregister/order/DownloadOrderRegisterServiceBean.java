/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.order;

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

import jp.co.sega.allnet.auth.common.entity.DownloadOrder;
import jp.co.sega.allnet.auth.common.entity.DownloadOrderPK;
import jp.co.sega.allnet.auth.common.entity.Machine;
import jp.co.sega.allnet.auth.common.entity.MachineDownloadOrder;
import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.InvalidParameterProcessor;
import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

import org.apache.commons.lang3.StringUtils;
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
@Component("downloadOrderRegisterService")
@Scope("singleton")
@Transactional
public class DownloadOrderRegisterServiceBean extends AbstractRegisterService
        implements DownloadOrderRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(DownloadOrderRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor _msg;

    @Override
    public RegisterServiceResult<DownloadOrderRegisterResult> registerDownloadOrder(
            String val, String gameId) {

        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            List<DownloadOrderRegisterResult> results = new ArrayList<DownloadOrderRegisterResult>();
            String[] line;
            int cnt = 0;
            boolean allFlag = false;

            while ((line = reader.readNext()) != null) {

                DownloadOrderRegisterParameter param = new DownloadOrderRegisterParameter(
                        line);

                if (results.size() == 0 && param.getSerial().equals("ALL")) {
                    // 一行目の基板シリアルがALLだった場合はゲーム別配信指示書の設定を行う
                    allFlag = true;
                }

                // 1行のカラム数チェック
                if (param.isLengthNotEnough()) {
                    results.add(new DownloadOrderRegisterResult(param, _msg
                            .getMessage("adminregister.error.csv.column.size"),
                            -5));
                    cnt++;
                    continue;
                }

                // 入力チェック
                DownloadOrderRegisterResult r = new DownloadOrderRegisterResult(
                        param);
                if (!checkParameter(param, r)) {
                    results.add(r);
                    cnt++;
                    continue;
                }

                if (!param.getGameId().equals(gameId)) {
                    // 選択されたゲームIDとCSVのゲームIDが一致しない
                    String message = _msg
                            .getMessage("adminregister.error.no.match.gameId");
                    r.setMessage(message);
                    r.setStatus(-8);
                    results.add(r);
                    cnt++;
                    continue;
                }

                if (allFlag) {
                    // ゲーム別配信指示書を登録
                    downloadOrderRegister(param, results, r, cnt);
                } else {
                    // 基板別配信指示書を登録
                    machineDownloadOrderRegister(param, results, r, cnt);
                }

            }

            return new RegisterServiceResult<DownloadOrderRegisterResult>(cnt,
                    results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private void downloadOrderRegister(DownloadOrderRegisterParameter param,
            List<DownloadOrderRegisterResult> results,
            DownloadOrderRegisterResult r, int cnt) {

        if (!param.getSerial().equals("ALL")) {
            // 基板シリアルの値がALLではない
            String message = _msg
                    .getMessage("adminregister.status.invalidParameterFormat");
            r.setMessage(message);
            r.setStatus(-9);
            results.add(r);
            cnt++;
            return;
        }

        if (StringUtils.isEmpty(param.getGameVer())) {
            // ゲームVerが不正
            String message = _msg
                    .getMessage("adminregister.status.invalidParameterFormat");
            r.setMessage(message);
            r.setStatus(-9);
            results.add(r);
            cnt++;
            return;
        }

        if (StringUtils.isEmpty(param.getUri())) {
            // 削除
            DownloadOrderPK pk = new DownloadOrderPK(param.getGameId(),
                    param.getGameVer());
            DownloadOrder order = _em.find(DownloadOrder.class, pk);
            if (order == null) {
                // 削除対象のゲーム別配信指示書情報が存在しない
                String message = _msg
                        .getMessage(
                                "adminregister.error.no.register",
                                new Object[] { _msg
                                        .getMessage("adminregister.downloadorder.info") });
                r.setMessage(message);
                r.setStatus(-7);
                results.add(r);
                cnt++;
                return;
            }
            remove(order);
        } else {
            // 基板がもつ配信指示書URI情報をクリアする
            deleteMachineDownloadOrder(param.getGameId());

            // 配信指示書情報を登録・更新する
            updateDownloadOrder(param.getGameId(), param.getGameVer(),
                    param.getUri());
        }

        r.setMessage(_msg.getMessage("adminregister.status.success"));
        r.setStatus(1);
        results.add(r);
    }

    private void machineDownloadOrderRegister(
            DownloadOrderRegisterParameter param,
            List<DownloadOrderRegisterResult> results,
            DownloadOrderRegisterResult r, int cnt) {

        Machine machine = findMachineBySerialGameId(param.getSerial(),
                param.getGameId());
        if (machine == null) {
            // 条件に一致する基板情報が存在しない
            String message = _msg.getMessage("adminregister.error.no.register",
                    new Object[] { _msg
                            .getMessage("adminregister.machine.info") });
            r.setMessage(message);
            r.setStatus(-7);
            results.add(r);
            cnt++;
            return;
        }

        if (StringUtils.isEmpty(param.getUri())) {
            // 削除
            MachineDownloadOrder order = _em.find(MachineDownloadOrder.class,
                    machine.getSerial());
            if (order == null) {
                // 基板配信指示書情報が存在しない
                String message = _msg
                        .getMessage(
                                "adminregister.error.no.register",
                                new Object[] { _msg
                                        .getMessage("adminregister.machine.downloadorder.info") });
                r.setMessage(message);
                r.setStatus(-7);
                results.add(r);
                cnt++;
                return;
            }
            remove(order);
        } else {
            // 更新
            updateMachineDownloadOrder(param);
        }

        r.setMessage(_msg.getMessage("adminregister.status.success"));
        r.setStatus(1);
        results.add(r);
    }

    /**
     * 基板シリアルとゲームIDから基板情報を取得する。
     * 
     * @param serial
     * @param gameId
     * @return
     */
    private Machine findMachineBySerialGameId(String serial, String gameId) {
        try {
            Query query = _em.createNamedQuery("findMachineBySerialGameId");
            query.setParameter("serial", serial);
            query.setParameter("gameId", gameId);
            return (Machine) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 基板配信指示書情報を登録・更新する。
     * 
     * @param serial
     * @param gameId
     * @param uri
     * @return
     */
    private void updateMachineDownloadOrder(DownloadOrderRegisterParameter param) {
        Date now = new Date();

        MachineDownloadOrder o = _em.find(MachineDownloadOrder.class,
                param.getSerial());
        if (o == null) {
            o = new MachineDownloadOrder();
            o.setSerial(param.getSerial());
            o.setGameId(param.getGameId());
            o.setUri(param.getUri());
            o.setCreateDate(now);
            o.setCreateUserId(getCurrentUser());
            o.setUpdateDate(now);
            o.setUpdateUserId(getCurrentUser());
            persist(o);
        } else {
            o.setGameId(param.getGameId());
            o.setUri(param.getUri());
            o.setUpdateDate(now);
            o.setUpdateUserId(getCurrentUser());
            merge(o);
        }
    }

    /**
     * パラメータチェックを行う。
     * 
     * @param param
     * @param result
     * @return
     */
    private boolean checkParameter(DownloadOrderRegisterParameter param,
            DownloadOrderRegisterResult result) {
        Map<String, InvalidParameterProcessor> map = new HashMap<String, InvalidParameterProcessor>();
        map.put("serial",
                new ParameterProcessor(
                        result,
                        -9,
                        _msg.getMessage("adminregister.status.invalidParameterFormat")));
        map.put("gameId",
                new ParameterProcessor(
                        result,
                        -9,
                        _msg.getMessage("adminregister.status.invalidParameterFormat")));
        map.put("gameVer",
                new ParameterProcessor(
                        result,
                        -9,
                        _msg.getMessage("adminregister.status.invalidParameterFormat")));
        map.put("uri",
                new ParameterProcessor(
                        result,
                        -9,
                        _msg.getMessage(
                                "adminregister.error.byte.size.over",
                                new Object[] {
                                        _msg.getMessage("adminregister.downloadorder.uri"),
                                        128 })));
        return checkParameter(param, map, false);
    }

    /**
     * 配信指示書情報を登録・更新する。
     * 
     * @param gameId
     * @param gameVer
     * @param uri
     */
    private void updateDownloadOrder(String gameId, String gameVer, String uri) {

        Date now = new Date();
        DownloadOrderPK pk = new DownloadOrderPK(gameId, gameVer);
        DownloadOrder o = _em.find(DownloadOrder.class, pk);

        if (o == null) {
            o = new DownloadOrder();
            o.setPk(pk);
            o.setUri(uri);
            o.setCreateDate(now);
            o.setCreateUserId(getCurrentUser());
            o.setUpdateDate(now);
            o.setUpdateUserId(getCurrentUser());
            persist(o);
        } else {
            o.setUri(uri);
            o.setUpdateDate(now);
            o.setUpdateUserId(getCurrentUser());
            merge(o);
        }

    }

    /**
     * 基板配信指示書情報を削除する。
     * 
     * @param gameId
     */
    private void deleteMachineDownloadOrder(String gameId) {
        // 予約ゲームIDを含む削除は行わない
        Query query = _em
                .createNamedQuery("deleteMachineDownloadOrderByGameId");
        query.setParameter("gameId", gameId);
        int count = query.executeUpdate();

        if (count > 0) {
            _log.info(formatBulkDeleteLog("MachineDownloadOrder", "gameId:%s",
                    gameId));
        }
    }

    /**
     * 基板配信指示書CSVアップロードで使用する{@link InvalidParameterProcessor}の実装です。
     * 
     * @author TsuboiY
     * 
     */
    private class ParameterProcessor implements InvalidParameterProcessor {

        private final DownloadOrderRegisterResult _result;
        private final int _status;
        private final String _msg;

        ParameterProcessor(DownloadOrderRegisterResult result, int status,
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
