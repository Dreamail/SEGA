/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.machine;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jp.co.sega.allnet.auth.common.entity.Machine;
import jp.co.sega.allnet.auth.common.entity.MachineDeletionHistory;
import jp.co.sega.allnet.auth.common.entity.MachineDeletionHistoryPK;
import jp.co.sega.allnet.auth.common.entity.MachineDeletionReason;
import jp.co.sega.allnet.auth.common.entity.Place;
import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVReader;

/**
 * 基板情報の登録処理を実行します。
 * 
 * @author TsuboiY
 * 
 */
@Component("machineRegisterService")
@Scope("singleton")
@Transactional
public class MachineRegisterServiceBean extends AbstractRegisterService
        implements MachineRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(MachineRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Override
    public String registerMachine(String val) {

        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            int success = 0;
            while ((line = reader.readNext()) != null) {

                MachineRegisterParameter param = new MachineRegisterParameter(
                        line, true);

                // 入力チェック
                if (!checkParameter(param)) {
                    createResponse(res, StringUtils.EMPTY, StringUtils.EMPTY,
                            null, StringUtils.EMPTY, StringUtils.EMPTY, num,
                            false);
                    num++;
                    continue;
                }

                // 基板情報を取得する
                Machine machine = findMachine(param.getSerial());
                String oldPlaceId;
                if (machine != null) {
                    oldPlaceId = machine.getPlaceId();
                } else {
                    oldPlaceId = StringUtils.EMPTY;
                }

                MachineRegisterResult r = registerMacine(param, machine, false);
                if (r.isSuccess()) {
                    createResponse(res, r.getParam().getAllnetId(), r
                            .getParam().getSerial(), r.getStatus(),
                            r.getPlaceId(), oldPlaceId, num, r.isSuccess());
                    success++;
                } else if (r.getSuccess() == MachineRegisterResult.RESULT_NG_DELETE_NO_TARGET) {
                    // 削除対象がなかった場合はエラーにせず成功のステータスを返す
                    createResponse(res, param.getAllnetId(), param.getSerial(),
                            STATUS_MODIFY, oldPlaceId, oldPlaceId, num, true);
                    success++;
                } else {
                    createResponse(res, StringUtils.EMPTY, StringUtils.EMPTY,
                            null, StringUtils.EMPTY, StringUtils.EMPTY, num,
                            r.isSuccess());
                }
                // インクリメント
                num++;
            }

            res.append("\nEND,,,,,,");
            res.append(success);

            return res.toString();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public RegisterServiceResult<MachineRegisterResult> registerMachineForAdmin(
            String val, String gameId, boolean reserve) {

        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            List<MachineRegisterResult> results = new ArrayList<MachineRegisterResult>();
            String[] line;
            int cnt = 0;
            while ((line = reader.readNext()) != null) {

                MachineRegisterParameter param = new MachineRegisterParameter(
                        line, reserve);

                // 入力チェック
                if (!checkParameter(param)) {
                    results.add(new MachineRegisterResult(
                            MachineRegisterResult.RESULT_NG_NOT_REGISTER_DATA,
                            param, null, null, null));
                    cnt++;
                    continue;
                }

                // 基板情報を取得する
                Machine machine = findMachine(param.getSerial());

                if (StringUtils.isEmpty(param.getGameId())
                        || !param.getGameId().equals(gameId)) {
                    // 選択されたゲームIDとCSVの中身が異なっていたらエラー
                    results.add(new MachineRegisterResult(
                            MachineRegisterResult.RESULT_NG_DIFFERENT_GAME_ID,
                            param, null, null, null));
                    cnt++;
                    continue;
                }

                Place oldPlace = null;
                if (machine != null) {
                    oldPlace = findPlace(machine.getAllnetId().longValue());
                }

                MachineRegisterResult r = registerMacine(param, machine, true);
                if (!r.isSuccess()) {
                    cnt++;
                }
                if (oldPlace != null) {
                    r.setOldPlaceName(oldPlace.getName());
                    r.setOldPlaceId(oldPlace.getPlaceId());
                }

                results.add(r);
            }

            return new RegisterServiceResult<MachineRegisterResult>(cnt,
                    results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 基板シリアル登録処理を行う。
     * 
     * @param line
     * @return
     */
    private MachineRegisterResult registerMacine(
            MachineRegisterParameter param, Machine machine, boolean deleteOrder) {

        long allnetId = Long.parseLong(param.getAllnetId());

        // 店舗情報を取得する
        Place place = findPlace(allnetId);

        Integer deletionReasonNo = null;
        if (!StringUtils.isEmpty(param.getDeletionReasonNo())) {
            deletionReasonNo = Integer.parseInt(param.getDeletionReasonNo());
        }

        Integer status = null;
        if (deletionReasonNo != null
                && deletionReasonNo >= MIN_DELETION_REASON_NO) {
            if (machine == null) {

                String placeId = "";
                if (place != null && StringUtils.isNotBlank(place.getPlaceId())) {
                    placeId = place.getPlaceId();
                }

                // 削除対象なし
                return new MachineRegisterResult(
                        MachineRegisterResult.RESULT_NG_DELETE_NO_TARGET,
                        param, placeId, StringUtils.EMPTY, null);
            }
            // 削除理由のチェック
            MachineDeletionReason reason = findMachineDeletionReasons(deletionReasonNo);
            if (reason == null) {
                _log.warn(getLogUtils().format(
                        "DeletionReason not found :[%s]", deletionReasonNo));
            }
            // 削除履歴を登録
            createMachineDeletionHistories(machine, place, deletionReasonNo);

            // 基板を削除
            removeMachine(machine);

            // Adminの場合は基板配信指示書設定を削除
            if (deleteOrder) {
                removeMachineDownloadOrder(machine.getSerial());
            }

            // 配信ステータスを削除
            removeLoaderStateLogs(machine.getSerial());
            // NU用の配信レポートを削除
            removeDeliverReports(machine.getSerial());

            // 処理区分を"更新"にする
            status = STATUS_MODIFY;
        } else {
            if (place == null) {
                // 該当する店舗がないので登録しない
                return new MachineRegisterResult(
                        MachineRegisterResult.RESULT_NG_NOT_REGISTER_DATA,
                        param, StringUtils.EMPTY, StringUtils.EMPTY, null);
            }
            if (machine == null) {
                // 基板情報を登録
                machine = createMachine(param, place);
                // 処理区分を"追加"にする
                status = STATUS_REGISTER;
            } else {
                // 基板情報を更新
                machine = updateMachine(machine, param, place,
                        param.isUpdateReservedGameId());
                if (!param.isUpdateReservedGameId()) {
                    // 配信ステータスを削除
                    removeLoaderStateLogs(machine.getSerial());
                    // NU用の配信レポートを削除
                    removeDeliverReports(machine.getSerial());
                }
                // 処理区分を"更新"にする
                status = STATUS_MODIFY;
            }
        }

        MachineRegisterResult r = new MachineRegisterResult(
                MachineRegisterResult.RESULT_OK, param, place.getPlaceId(),
                place.getName(), status);
        r.setMachine(machine);

        return r;
    }

    /**
     * 基板情報を取得する。
     * 
     * @param serial
     * @return
     */
    private Machine findMachine(String serial) {
        return _em.find(Machine.class, serial);
    }

    /**
     * 店舗情報を取得する。
     * 
     * @param allnetId
     * @return
     */
    private Place findPlace(long allnetId) {
        return _em.find(Place.class, allnetId);
    }

    /**
     * 基板削除理由を取得する。
     * 
     * @param reasonId
     * @return
     */
    private MachineDeletionReason findMachineDeletionReasons(int reasonId) {
        return _em.find(MachineDeletionReason.class, reasonId);
    }

    /**
     * 基板削除履歴を登録する。
     * 
     * @param machine
     * @param place
     * @param reasonId
     */
    private void createMachineDeletionHistories(Machine machine, Place place,
            int reasonId) {
        MachineDeletionHistoryPK pk = new MachineDeletionHistoryPK();
        pk.setSerial(machine.getSerial());
        pk.setCreateDate(new Date());
        MachineDeletionHistory h = new MachineDeletionHistory();
        h.setPk(pk);
        h.setGameId(machine.getGameId());
        h.setReservedGameId(machine.getReservedGameId());
        h.setPlaceName(place.getName());
        h.setAllnetId(new BigDecimal(place.getAllnetId()));
        h.setReasonId(new BigDecimal(reasonId));
        h.setCreateUserId(getCurrentUser());
        persist(h);
    }

    /**
     * 基板情報を登録する。
     * 
     * @param param
     * @param place
     */
    private Machine createMachine(MachineRegisterParameter param, Place place) {
        Date now = new Date();
        Machine m = new Machine();
        m.setSerial(param.getSerial());
        m.setAllnetId(new BigDecimal(place.getAllnetId()));
        m.setGameId(param.getGameId());
        m.setReservedGameId(param.getGameId());
        m.setGroupIndex(new BigDecimal(param.getGroupIndex()));
        m.setSetting(new BigDecimal(param.getSetting()));
        m.setPlaceId(place.getPlaceId());
        m.setRegisterTimestamp(now);
        m.setCreateDate(now);
        m.setCreateUserId(getCurrentUser());
        m.setUpdateDate(now);
        m.setUpdateUserId(getCurrentUser());
        persist(m);
        return m;
    }

    /**
     * 基板情報を更新する。
     * 
     * @param machine
     * @param param
     * @param place
     * @param updateReservedGameId
     */
    private Machine updateMachine(Machine machine,
            MachineRegisterParameter param, Place place,
            boolean updateReservedGameId) {
        machine.setAllnetId(new BigDecimal(place.getAllnetId()));
        if (updateReservedGameId) {
            machine.setReservedGameId(param.getGameId());
        } else {
            machine.setGameId(param.getGameId());
        }
        machine.setGroupIndex(new BigDecimal(param.getGroupIndex()));
        machine.setSetting(new BigDecimal(param.getSetting()));
        machine.setPlaceId(place.getPlaceId());
        machine.setRegisterTimestamp(new Date());
        machine.setUpdateDate(new Date());
        machine.setUpdateUserId(getCurrentUser());
        return merge(machine);
    }

    /**
     * 基板情報を削除する。
     * 
     * @param machine
     */
    private void removeMachine(Machine machine) {
        remove(machine);
    }

    /**
     * 基板配信指示書設定を削除する。
     * 
     * @param serial
     */
    private void removeMachineDownloadOrder(String serial) {
        Query query = _em.createNamedQuery("deleteMachineDownloadOrder");
        query.setParameter("serial", serial);
        query.executeUpdate();
    }

    /**
     * 配信PCの稼働状況ログを基板シリアルで削除する。
     * 
     * @param serial
     */
    private void removeLoaderStateLogs(String serial) {
        Query query = _em.createNamedQuery("deleteLoaderStateLogs");
        query.setParameter("serial", serial);
        query.executeUpdate();
    }

    /**
     * NU用の配信レポートを基板シリアルで削除する。
     * 
     * @param serial
     */
    private void removeDeliverReports(String serial) {
        // Appの配信レポートを削除
        Query query = _em.createNamedQuery("deleteAppDeliverReports");
        query.setParameter("serial", serial);
        query.executeUpdate();

        // Optの配信レポートを削除
        query = _em.createNamedQuery("deleteOptDeliverReports");
        query.setParameter("serial", serial);
        query.executeUpdate();
    }

    /**
     * レスポンスを作成する。
     * 
     * @param res
     * @param allnetId
     * @param serial
     * @param status
     * @param placeId
     * @param orgPlaceId
     * @param num
     * @param success
     * @return
     */
    private StringBuilder createResponse(StringBuilder res, String allnetId,
            String serial, Integer status, String placeId, String orgPlaceId,
            int num, boolean success) {

        res.append("\n");

        res.append(num);
        res.append(",");
        res.append(allnetId);
        res.append(",");
        res.append(serial);
        res.append(",");
        if (status == null) {
            res.append("");
        } else {
            res.append(status);
        }
        res.append(",");
        res.append(placeId);
        res.append(",");
        res.append(orgPlaceId);
        res.append(",");
        if (success) {
            res.append("1");
        } else {
            res.append("0");
        }
        return res;
    }

}
