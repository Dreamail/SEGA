/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.poweron;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.PowerOnDao;
import jp.co.sega.allnet.auth.api.domain.AuthAllowedComp;
import jp.co.sega.allnet.auth.api.domain.AuthAllowedPlace;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Game;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Machine;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Router;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Status;
import jp.co.sega.allnet.auth.api.util.ResponseData;
import jp.co.sega.allnet.auth.api.util.ResponseUtils;
import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.log.ApiLogUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NakanoY
 * 
 */

@Component("powerOnService")
@Scope("prototype")
public class PowerOnServiceBean implements PowerOnService {

    private static final Logger _log = LoggerFactory
            .getLogger(PowerOnServiceBean.class);

    private static final int OVERSEA_FORMAT_VER = 2;

    private static final int NU_FORMAT_VER = 3;

    private static final int COMP_CODE_LENGTH = 6;

    private static final int BILL_CODE_LENGTH = 9;

    private static final String DEFAULT_URI = "";

    private static final String DEFAULT_HOST = "";

    private static final String DEFAULT_REGION_ID = "0";

    private static final String DEFAULT_REGION_NAME = "";

    private static final String RESPONSE_VALUE_TIMEZONE = "+09:00";

    private static final String RESPONSE_VALUE_RES_CLASS = "PowerOnResponseVer";

    @Resource(name = "powerOnDao")
    private PowerOnDao _dao;

    @Resource(name = "apiLogUtils")
    private ApiLogUtils _logUtils;

    @Transactional
    @Override
    public String powerOn(PowerOnParameter param) {
        _log.info(_logUtils.format("Start authentication process [%s]",
                param.getQueryString()));

        // 共通データ処理
        PowerOnData data = findCommonData(param.getGameId(), param.getVer(),
                param.getSerial(), param.getIp());

        Status stat = data.getStatus();
        Response res;

        if (data.getGame().getGameId() != null) {

            if (data.isExistSerial()) {
                // 基板情報に基板シリアルが存在する
                if (data.isNotMove() || data.isSamePlace()) {
                    // ゲーム情報の認証方式が移設ではない or ルータ情報の店舗と基板情報の店舗が同じ
                    if (data.isExistMachinePlace()) {
                        // 基板情報の店舗が存在するなら通常認証処理
                        authenticateNormal(data, param);
                    } else {
                        stat.setStat(AuthStatus.FAIL_LOC.value());
                        stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 5)
                                * -1);
                    }
                    // レスポンスの作成
                    res = createResponse(data.getGame(), data.getRouter()
                            .getRouterId(), data.getMachine(), param,
                            stat.getStat());

                } else {
                    // 自動移設処理
                    authenticateAndMove(data, param);
                    // レスポンスの作成
                    res = createResponse(data.getGame(), data.getRouter(),
                            data.getMachine(), param, stat.getStat());

                }

            } else {
                if (data.isAutoAuth()) {
                    // ゲーム情報の認証方式が自動認証であるなら自動認証処理
                    authenticateAuto(data, param);
                    // レスポンスの作成
                    res = createResponse(data.getGame(), data.getRouter(),
                            data.getMachine(), param, stat.getStat());
                } else {
                    // 基板情報に関するNG
                    stat.setStat(AuthStatus.FAIL_MACHINE.value());
                    stat.setCause(-1004);

                    res = createResponse(data.getGame(), data.getRouter()
                            .getRouterId(), data.getMachine(), param,
                            stat.getStat());
                }

            }
        } else {
            // レスポンスの作成
            res = createResponse(data.getGame(),
                    data.getRouter().getRouterId(), data.getMachine(), param,
                    stat.getStat());
        }
        // 基板情報の更新
        update(data, param);

        // 認証ログの書き込み
        putLog(param, stat.getStat(), stat.getCause(), data.getRouter(), res);

        // 認証失敗ならステータスをアプリケーションログに出力
        if (stat.getStat() == AuthStatus.SUCCESS.value()) {
            _log.info(_logUtils.format("Authentication was successful"));
        } else {
            _log.warn(_logUtils.format("Authentication was failed: %s", stat));
        }

        return res.response;
    }

    /**
     * 共通データ取得処理
     * 
     * @param gameId
     * @param gameVer
     * @param serial
     * @param ip
     * @return
     */
    private PowerOnData findCommonData(String gameId, String gameVer,
            String serial, String ip) {
        return _dao.find(gameId, gameVer, serial, ip);
    }

    /**
     * 通常認証判定処理
     * 
     * @param data
     * @param param
     */
    private void authenticateNormal(PowerOnData data, PowerOnParameter param) {

        Status stat = data.getStatus();
        if (stat.getStat() != AuthStatus.SUCCESS.value()) {
            // 既に認証エラーとなっている
            return;
        }

        // リクエストのゲームIDが基板情報の予約ゲームIDと一致する場合は基板情報を更新
        boolean exchange = exchangeGameId(data, param);

        if (data.getStatus().getStat() == AuthStatus.FAIL_GAME.value()) {
            return;
        }

        if (data.isNotDebugAuth() && !data.isSamePlace()) {
            // デバッグではなく店舗が不一致
            stat.setStat(AuthStatus.FAIL_LOC.value());
            stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 6)
                    * -1);
            return;
        }

        if (data.isNotAllowedCommSetting()) {
            // 通信が許可されていない
            stat.setStat(AuthStatus.FAIL_MACHINE.value());
            stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 8)
                    * -1);
            return;
        }

        // 認証方式をアプリケーションログに出力
        if (data.isNotDebugAuth()) {
            _log.info(_logUtils.format("Authentication type: NORMAL"));
        } else {
            _log.info(_logUtils.format("Authentication type: NORMAL(DEBUG)"));
        }
        if (exchange) {
            String format = "GameId exchanged [gameId:%s, reservedGameId:%s]";
            _log.info(_logUtils.format(format, data.getMachine().getGameId(),
                    data.getMachine().getReservedGameId()));
        }
    }

    /**
     * 自動移設処理
     * 
     * @param data
     * @param param
     */
    private void authenticateAndMove(PowerOnData data, PowerOnParameter param) {
        Status stat = data.getStatus();
        if (stat.getStat() != AuthStatus.SUCCESS.value()) {
            // 既に認証エラーとなっている
            return;
        }

        if (data.isNotAllowedCommSetting()) {
            // 通信が許可されていない
            stat.setStat(AuthStatus.FAIL_LOC.value());
            stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 8)
                    * -1);
            return;
        }

        if (data.getMachine().getAllnetId() == null) {
            // 基板に紐づく店舗情報がない
            stat.setStat(AuthStatus.FAIL_LOC.value());
            stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 13)
                    * -1);
            return;
        }

        AuthType authType = AuthType.fromValue(data.getGame().getAuth());

        String compCode;
        String routerBillCode = data.getRouter().getBillCode();
        String machineBillCode = data.getMachine().getBillCode();
        boolean authDenied;
        boolean moveDenied;
        AuthAllowedComp authAllowedComp = null;

        if (routerBillCode == null
                || routerBillCode.length() != BILL_CODE_LENGTH) {
            // ルータに紐づく請求先コードが9桁ではない
            stat.setStat(AuthStatus.FAIL_LOC.value());
            stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 14)
                    * -1);
            return;
        }

        // TODO 請求先がnullの場合にエラーになる不具合あり
        if (machineBillCode == null
                || machineBillCode.length() != BILL_CODE_LENGTH) {
            // 基板に紐づく請求先コードが9桁ではない
            stat.setStat(AuthStatus.FAIL_LOC.value());
            stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 15)
                    * -1);
            return;
        }

        // 請求先コードから包括先コードを取得
        compCode = routerBillCode.substring(0, COMP_CODE_LENGTH);
        if (!compCode.equals(machineBillCode.substring(0, COMP_CODE_LENGTH))) {
            // ルータに紐づく包括先コードと基板に紐づく包括先コードが同じではない
            stat.setStat(AuthStatus.FAIL_LOC.value());
            stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 16)
                    * -1);
            return;

        }

        // 自動認証不許可情報を取得
        authDenied = _dao.checkAuthDenied(param.getGameId(), compCode,
                routerBillCode);

        // 同一包括先内移設不許可情報を取得
        moveDenied = _dao.checkMoveDenied(param.getGameId(), param.getVer(),
                compCode, routerBillCode, machineBillCode);

        if (authType == AuthType.COMP_AUTO_MOVE) {
            // 包括先認証情報を取得
            authAllowedComp = _dao.findAuthAllowedComp(param.getGameId(),
                    compCode);
        }

        // 自動移設判定

        if (authType == AuthType.COMP_AUTO_MOVE && authAllowedComp == null) {
            // 包括先自動認証が許可されていない
            stat.setStat(AuthStatus.FAIL_LOC.value());
            stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 12)
                    * -1);
            return;
        }

        if (authDenied) {
            // 自動認証が不許可
            stat.setStat(AuthStatus.FAIL_LOC.value());
            stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 18)
                    * -1);
            return;
        }

        if (moveDenied) {
            // 自動移設が不許可
            stat.setStat(AuthStatus.FAIL_LOC.value());
            stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 17)
                    * -1);
            return;
        }

        // リクエストのゲームIDが基板情報の予約ゲームIDと一致する場合は基板情報を更新
        boolean exchange = exchangeGameId(data, param);

        if (data.getStatus().getStat() == AuthStatus.FAIL_GAME.value()) {
            return;
        }

        // 認証方式をアプリケーションログに出力
        _log.info(_logUtils.format("Authentication type: AUTO MOVE"));
        if (exchange) {
            String format = "GameId Exchanged [gameId:%s, reservedGameId:%s]";
            _log.info(_logUtils.format(format, data.getMachine().getGameId(),
                    data.getMachine().getReservedGameId()));
        }

        // 基板情報にリクエストの基板情報を設定
        data.getMachine().setSerial(param.getSerial());
        data.getMachine().setAllnetId(data.getRouter().getAllnetId());
        data.getMachine().setGameId(data.getGame().getGameId());

        // 基板情報を更新
        _dao.updateMachine(data);
    }

    /**
     * 自動認証処理
     * 
     * @param data
     * @param param
     */
    private void authenticateAuto(PowerOnData data, PowerOnParameter param) {
        Status stat = data.getStatus();
        if (stat.getStat() != AuthStatus.SUCCESS.value()) {
            // 既に認証エラーとなっている
            return;
        }

        if (param.getSerial().length() <= 0) {
            // 登録時のシリアルが0文字
            stat.setStat(AuthStatus.FAIL_MACHINE.value());
            stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 9)
                    * -1);
            return;
        }

        // キーチップ生産情報チェック
        if (!checkKeychipInfo(param.getGameId(), param.getSerial())) {
            // 生産情報チェックに失敗
            stat.setStat(AuthStatus.FAIL_MACHINE.value());
            stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 4)
                    * -1);
            return;
        }

        String compCode;
        boolean authDenied = false;
        AuthAllowedComp authAllowedComp = null;
        AuthAllowedPlace authAllowedPlace = null;

        AuthType authType = AuthType.fromValue(data.getGame().getAuth());
        switch (authType) {
        case PLACE_AUTO:
            // 店舗認証情報を取得
            authAllowedPlace = _dao.findAuthAllowedPlace(param.getGameId(),
                    data.getRouter().getAllnetId());
            break;
        default:
            String billCode = data.getRouter().getBillCode();
            if (billCode != null) {
                if (billCode.length() <= COMP_CODE_LENGTH) {
                    compCode = billCode;
                } else {
                    compCode = billCode.substring(0, COMP_CODE_LENGTH);
                }
            } else {
                compCode = "";
            }
            // 自動認証不許可情報を取得
            authDenied = _dao.checkAuthDenied(param.getGameId(), compCode,
                    billCode);
            if (authType == AuthType.COMP_AUTO_MOVE) {
                // 包括先認証許可情報を取得
                authAllowedComp = _dao.findAuthAllowedComp(param.getGameId(),
                        compCode);
            }
        }

        // 自動認証判定
        switch (authType) {
        case PLACE_AUTO:
            if (authAllowedPlace == null) {
                // 店舗自動認証が許可されていない
                stat.setStat(AuthStatus.FAIL_LOC.value());
                stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 11)
                        * -1);
                return;
            }
            // 認証方式をアプリケーションログに出力
            _log.info(_logUtils.format("Authentication type: AUTO(PLACE)"));
            break;
        case COMP_AUTO_MOVE:
            if (authAllowedComp == null) {
                // 包括先自動認証が許可されていない
                stat.setStat(AuthStatus.FAIL_LOC.value());
                stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 12)
                        * -1);
                return;
            }
        default:
            if (authDenied) {
                // 自動認証が不許可
                stat.setStat(AuthStatus.FAIL_LOC.value());
                stat.setCause(((Math.abs(data.getGame().getAuth()) * 1000) + 10)
                        * -1);
                return;
            }
            // 認証方式をアプリケーションログに出力
            if (authType == AuthType.COMP_AUTO_MOVE) {
                _log.info(_logUtils.format("Authentication type: AUTO(COMP)"));
            } else {
                _log.info(_logUtils.format("Authentication type: AUTO"));
            }
        }

        // 基板情報にリクエストの基板情報を設定
        data.getMachine().setSerial(param.getSerial());
        data.getMachine().setAllnetId(data.getRouter().getAllnetId());
        data.getMachine().setGameId(data.getGame().getGameId());
        data.getMachine().setPlaceId(data.getRouter().getPlaceId());

        // 基板情報をサーバに登録
        _dao.insertMachine(data);

    }

    /**
     * 基板ステータス情報・未登録基板情報更新処理
     * 
     * @param data
     * @param param
     */
    private void update(PowerOnData data, PowerOnParameter param) {

        if (data.isExistSerial()) {
            // 基板ステータス情報更新
            _dao.updateMachineStatus(data, param);
        }

        if (!data.isExistSerial()) {
            if (param.getSerial() != null && !(param.getSerial().isEmpty())) {
                // パラメータがあれば未登録基板情報の登録・更新
                _dao.updateUnRegisteredMachine(param);
            }
        } else {
            // 未登録基板情報の削除
            _dao.deleteUnRegisteredMachine(param);

        }
    }

    /**
     * レスポンスの作成
     * 
     * @param game
     * @param routerId
     * @param machine
     * @param param
     * @param stat
     * @return
     */
    private Response createResponse(Game game, String routerId,
            Machine machine, PowerOnParameter param, int stat) {
        return createResponse(game, routerId, machine.getAllnetId(),
                machine.getPlaceId(), machine.getPlaceName(),
                machine.getPlaceNickName(), machine.getRegion0Id(),
                machine.getRegion0Name(), machine.getRegion1Name(),
                machine.getRegion2Name(), machine.getRegion3Name(),
                machine.getCountryCode(), machine.getSetting(),
                machine.getTimezone(), param, stat);

    }

    /**
     * レスポンスの作成
     * 
     * @param game
     * @param router
     * @param machine
     * @param param
     * @param stat
     * @return
     */
    private Response createResponse(Game game, Router router, Machine machine,
            PowerOnParameter param, int stat) {
        return createResponse(game, router.getRouterId(), router.getAllnetId(),
                router.getPlaceId(), router.getPlaceName(),
                router.getPlaceNickName(), router.getRegion0Id(),
                router.getRegion0Name(), router.getRegion1Name(),
                router.getRegion2Name(), router.getRegion3Name(),
                router.getCountryCode(), machine.getSetting(),
                router.getTimezone(), param, stat);

    }

    /**
     * レスポンスの作成
     * 
     * @param game
     * @param placeId
     * @param name
     * @param nickname
     * @param region0Id
     * @param region0Name
     * @param region1Name
     * @param region2Name
     * @param region3Name
     * @param countryCode
     * @param setting
     * @param timezone
     * @param param
     * @param stat
     * @return
     */
    private Response createResponse(Game game, String routerId,
            BigDecimal allnetId, String placeId, String name, String nickname,
            Integer region0Id, String region0Name, String region1Name,
            String region2Name, String region3Name, String countryCode,
            int setting, String timezone, PowerOnParameter param, int stat) {
        try {
            List<ResponseData> list = new ArrayList<ResponseData>();

            list.add(new ResponseData("stat", stat));

            if (game.getUri() != null && stat > 0) {
                list.add(new ResponseData("uri", game.getUri()));
            } else {
                list.add(new ResponseData("uri", DEFAULT_URI));
            }

            if (game.getHost() != null && stat > 0) {
                list.add(new ResponseData("host", game.getHost()));
            } else {
                list.add(new ResponseData("host", DEFAULT_HOST));
            }

            if (placeId != null) {
                list.add(new ResponseData("place_id", placeId));
            }

            if (name != null) {
                list.add(new ResponseData("name", URLEncoder.encode(name,
                        param.getEncode())));
            }
            if (nickname != null) {
                list.add(new ResponseData("nickname", URLEncoder.encode(
                        nickname, param.getEncode())));
            }

            if (region0Id == null) {
                list.add(new ResponseData("region0", DEFAULT_REGION_ID));
            } else {
                list.add(new ResponseData("region0", region0Id));
            }

            if (region0Name == null) {
                list.add(new ResponseData("region_name0", DEFAULT_REGION_NAME));
            } else {
                list.add(new ResponseData("region_name0", URLEncoder.encode(
                        region0Name, param.getEncode())));
            }

            if (region1Name == null) {
                list.add(new ResponseData("region_name1", DEFAULT_REGION_NAME));
            } else {
                list.add(new ResponseData("region_name1", URLEncoder.encode(
                        region1Name, param.getEncode())));
            }
            if (region2Name == null) {
                list.add(new ResponseData("region_name2", DEFAULT_REGION_NAME));
            } else {
                list.add(new ResponseData("region_name2", URLEncoder.encode(
                        region2Name, param.getEncode())));
            }
            if (region3Name == null) {
                list.add(new ResponseData("region_name3", DEFAULT_REGION_NAME));
            } else {
                list.add(new ResponseData("region_name3", URLEncoder.encode(
                        region3Name, param.getEncode())));
            }

            if (countryCode != null
                    && Float.parseFloat(param.getFormatVer()) >= OVERSEA_FORMAT_VER) {
                list.add(new ResponseData("country", countryCode));
            }

            if (Float.parseFloat(param.getFormatVer()) >= NU_FORMAT_VER) {
                // FormatVerが3以上の場合に処理
                list = createResponceForNu(list, allnetId, timezone, setting,
                        param.getToken());

            } else {
                // FormatVerが2以下の場合に処理
                Calendar cl = Calendar.getInstance();

                list.add(new ResponseData("year", cl.get(Calendar.YEAR)));
                list.add(new ResponseData("month", cl.get(Calendar.MONTH) + 1));
                list.add(new ResponseData("day", cl.get(Calendar.DATE)));
                list.add(new ResponseData("hour", cl.get(Calendar.HOUR_OF_DAY)));
                list.add(new ResponseData("minute", cl.get(Calendar.MINUTE)));
                list.add(new ResponseData("second", cl.get(Calendar.SECOND)));

                list.add(new ResponseData("setting", setting));

                if (Float.parseFloat(param.getFormatVer()) >= OVERSEA_FORMAT_VER) {
                    list.add(new ResponseData("timezone",
                            RESPONSE_VALUE_TIMEZONE));
                    list.add(new ResponseData("res_class",
                            RESPONSE_VALUE_RES_CLASS + OVERSEA_FORMAT_VER));
                }
            }

            // レスポンス文字列を作成
            String res = ResponseUtils.createResponceString(list);
            // ログに格納するデバッグ文字列を作成
            String debugInfo = buildDebugInfo(param, stat, routerId, allnetId,
                    placeId, setting);
            String debugInfoPlace = buildDebugInfoPlace(param, name, nickname);

            return new Response(res, debugInfo, debugInfoPlace);

        } catch (UnsupportedEncodingException e) {
            throw new ApplicationException(e);
        }

    }

    /**
     * NU用のパラメータをレスポンスに追加する。
     * 
     * @param list
     * @param allnetId
     * @param timezone
     * @param setting
     * @return
     */
    private List<ResponseData> createResponceForNu(List<ResponseData> list,
            BigDecimal allnetId, String timezone, int setting, String token) {

        if (allnetId != null) {
            list.add(new ResponseData("allnet_id", allnetId));
        }

        if (timezone != null) {
            list.add(new ResponseData("client_timezone", timezone));
        } else {
            list.add(new ResponseData("client_timezone", ""));
        }

        Calendar cl = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(cl.getTimeZone());
        String utcTime = df.format(cl.getTime());
        list.add(new ResponseData("utc_time", utcTime));

        list.add(new ResponseData("setting", setting));
        list.add(new ResponseData("res_ver", NU_FORMAT_VER));
        list.add(new ResponseData("token", token));

        return list;
    }

    /**
     * 認証ログ書き込み
     * 
     * @param param
     * @param stat
     * @param cause
     * @param router
     * @param res
     */
    private void putLog(PowerOnParameter param, int stat, int cause,
            Router router, Response res) {
        _dao.insertLog(param, stat, cause, router, res.response, res.debugInfo,
                res.debugInfoPlace);
    }

    /**
     * リクエストのゲームIDが基板情報の予約ゲームIDと一致する場合は基板情報を更新
     * 
     * @param data
     * @param param
     * @return
     */
    private boolean exchangeGameId(PowerOnData data, PowerOnParameter param) {
        if (param.getGameId().equals(data.getMachine().getGameId())) {
            return false;
        }
        if (!param.getGameId().equals(data.getMachine().getReservedGameId())) {
            // リクエストのゲームIDが基板情報のゲームID、予約ゲームIDと一致しない
            data.getStatus().setStat(AuthStatus.FAIL_GAME.value());
            data.getStatus().setCause(
                    ((Math.abs(data.getGame().getAuth()) * 1000) + 7) * -1);
            return false;
        }
        // リクエストのゲームIDが基板情報の予約ゲームIDと一致するならゲームIDと交換する
        _dao.exchangeMachineGameId(data);

        if (Float.parseFloat(param.getFormatVer()) >= NU_FORMAT_VER) {
            // 配信レポートを削除する
            _dao.deleteDeliverReport(data);
        } else {
            // 配信PCの稼働状況ログを削除する
            _dao.deleteLoaderStateLogs(data);
        }
        return true;
    }

    /**
     * デバッグ文字列を作成
     * 
     * @param param
     * @param stat
     * @param routerId
     * @param allnetId
     * @param placeId
     * @param name
     * @param nickname
     * @param setting
     * @param response
     * @return
     */
    private String buildDebugInfo(PowerOnParameter param, int stat,
            String routerId, BigDecimal allnetId, String placeId, int setting) {
        StringBuilder buf = new StringBuilder();
        buf.append("game_id=");
        buf.append(param.getGameId());
        buf.append(",ver=");
        buf.append(param.getVer());
        buf.append(",serial=");
        buf.append(param.getSerial());
        if (param.getIp() != null) {
            buf.append(",ip=");
            buf.append(param.getIp());
        }
        buf.append(",place_id=");
        buf.append(placeId);
        buf.append(",allnet_id=");
        buf.append(allnetId);
        if (routerId != null) {
            buf.append(",router_no=");
            buf.append(routerId);
        }
        buf.append(",stat=");
        buf.append(stat);
        buf.append(",setting=");
        buf.append(setting);
        return buf.toString();
    }

    /**
     * 店舗情報に関するデバッグ文字列を作成
     * 
     * @param param
     * @param name
     * @param nickname
     * @return
     */
    private String buildDebugInfoPlace(PowerOnParameter param, String name,
            String nickname) {
        StringBuilder buf = new StringBuilder();

        if (!param.getHops().equals("")) {
            buf.append("hops=");
            buf.append(param.getHops());
        }
        buf.append("[");
        buf.append(name);
        buf.append(",");
        buf.append(nickname);
        buf.append("]");

        return buf.toString();
    }

    /**
     * キーチップ生産情報をチェックする。
     * 
     * @param gameId
     * @param serial
     * @return
     */
    private boolean checkKeychipInfo(String gameId, String serial) {

        // キーチップ生産情報を参照するかチェック
        if (_dao.checkPrdCheckGame(gameId)) {

            Integer stat = _dao.findKeychipStat(serial, gameId);
            if (stat != null) {
                switch (stat) {
                case 1:
                case 2:
                case 3:
                case 4:
                    return true;
                }
            }

            return false;
        }

        return true;
    }

    /**
     * レスポンス保持用クラス
     * 
     * @author TsuboiY
     * 
     */
    private class Response implements Serializable {

        private static final long serialVersionUID = 1L;

        private String response;

        private String debugInfo;

        private String debugInfoPlace;

        private Response(String response, String debugInfo,
                String debugInfoPlace) {
            this.response = response;
            this.debugInfo = debugInfo;
            this.debugInfoPlace = debugInfoPlace;
        }

    }

}
