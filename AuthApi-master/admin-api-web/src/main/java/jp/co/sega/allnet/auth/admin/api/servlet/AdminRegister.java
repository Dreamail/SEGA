/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.admin.api.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.sega.allnet.auth.log.ApiLogUtils;
import jp.co.sega.allnet.auth.service.adminregister.authallowed.comp.AuthAllowedCompRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.authallowed.place.AuthAllowedPlaceRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.authdenied.bill.AuthDeniedBillRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.authdenied.comp.AuthDeniedCompRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.authdenied.gamebill.AuthDeniedGameBillRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.authdenied.gamecomp.AuthDeniedGameCompRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.authtype.AuthTypeRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.comp.CompRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.lctype.LcTypeRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.machine.MachineRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.movedenied.bill.MoveDeniedBillRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.movedenied.comp.MoveDeniedCompRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.movedenied.game.MoveDeniedGameRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.movedenied.gamebill.MoveDeniedGameBillRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.movedenied.gamecomp.MoveDeniedGameCompRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever.MoveDeniedGameVerRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.place.PlaceRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.router.RouterRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.routertype.RouterTypeRegisterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author TsuboiY
 * 
 */
public class AdminRegister extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * デフォルトのエンコーディングはShift_JIS(Cp943c)
     */
    private static final String DEFAULT_CHARACTER_ENCODING = "Cp943C";

    private static final Logger _log = LoggerFactory
            .getLogger(AdminRegister.class);

    private ApplicationContext _app;

    @Override
    public void init() throws ServletException {
        _app = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding(DEFAULT_CHARACTER_ENCODING);
        ApiLogUtils logUtils = _app.getBean(ApiLogUtils.class);
        boolean interrupt = false;
        try {
            String cmd = req.getParameter("cmd");
            if (cmd == null) {
                cmd = "";
            }
            String val = req.getParameter("val");
            if (val == null) {
                val = "";
            }

            // 開始ログを出力
            _log.info(logUtils.formatStartLog("AdminRegister"));
            _log.info(logUtils.format("Requested cmd:%s, val:%s", cmd, val));

            String ret = null;
            switch (Command.fromValue(cmd)) {
            case GAME:
                _log.warn(logUtils.format("Requested cmd is not implemented"));
                break;
            case PLACE:
                PlaceRegisterService placeRegisterService = _app.getBean(
                        "placeRegisterService", PlaceRegisterService.class);
                ret = placeRegisterService.registerPlace(val);
                break;
            case ROUTER:
                RouterRegisterService routerRegisterService = _app.getBean(
                        "routerRegisterService", RouterRegisterService.class);
                ret = routerRegisterService.registerRouter(val);
                break;
            case MACHINE:
                MachineRegisterService machineRegisterService = _app.getBean(
                        "machineRegisterService", MachineRegisterService.class);
                ret = machineRegisterService.registerMachine(val);
                break;
            case PLACE_AUTH:
                AuthAllowedPlaceRegisterService authAllowedPlaceRegisterService = _app
                        .getBean("authAllowedPlaceRegisterService",
                                AuthAllowedPlaceRegisterService.class);
                ret = authAllowedPlaceRegisterService
                        .registerAuthAllowedPlace(val);
                break;
            case COMP:
                CompRegisterService compRegisterService = _app.getBean(
                        "compRegisterService", CompRegisterService.class);
                ret = compRegisterService.registerComp(val);
                break;
            case COMP_AUTH:
                AuthAllowedCompRegisterService authAllowedCompRegisterService = _app
                        .getBean("authAllowedCompRegisterService",
                                AuthAllowedCompRegisterService.class);
                ret = authAllowedCompRegisterService
                        .registerAuthAllowedComp(val);
                break;
            case AUTH_TYPE:
                // 登録処理はサポートしないので常にエラーメッセージをCSVで返却する
                AuthTypeRegisterService authTypeRegisterService = _app.getBean(
                        "authTypeRegisterService",
                        AuthTypeRegisterService.class);
                ret = authTypeRegisterService.registerAuthType(val);
                break;
            case AUTO_AUTH_COMP:
                AuthDeniedCompRegisterService authDeniedCompRegisterService = _app
                        .getBean("authDeniedCompRegisterService",
                                AuthDeniedCompRegisterService.class);
                ret = authDeniedCompRegisterService.registerAuthDeniedComp(val);
                break;
            case AUTO_AUTH_STORE:
                AuthDeniedBillRegisterService authDeniedBillRegisterService = _app
                        .getBean("authDeniedBillRegisterService",
                                AuthDeniedBillRegisterService.class);
                ret = authDeniedBillRegisterService.registerAuthDeniedBill(val);
                break;
            case AUTO_AUTH_GAMEID_COMP:
                AuthDeniedGameCompRegisterService authDeniedGameCompRegisterService = _app
                        .getBean("authDeniedGameCompRegisterService",
                                AuthDeniedGameCompRegisterService.class);
                ret = authDeniedGameCompRegisterService
                        .registerAuthDeniedGameComp(val);
                break;
            case AUTO_AUTH_GAMEID_STORE:
                AuthDeniedGameBillRegisterService authDeniedGameBillRegisterService = _app
                        .getBean("authDeniedGameBillRegisterService",
                                AuthDeniedGameBillRegisterService.class);
                ret = authDeniedGameBillRegisterService
                        .registerAuthDeniedGameBill(val);
                break;
            case AUTO_MOVE_GAMEID:
                MoveDeniedGameRegisterService moveDeniedGameRegisterService = _app
                        .getBean("moveDeniedGameRegisterService",
                                MoveDeniedGameRegisterService.class);
                ret = moveDeniedGameRegisterService.registerMoveDeniedGame(val);
                break;
            case AUTO_MOVE_GAMEID_VER:
                MoveDeniedGameVerRegisterService moveDeniedGameVerRegisterService = _app
                        .getBean("moveDeniedGameVerRegisterService",
                                MoveDeniedGameVerRegisterService.class);
                ret = moveDeniedGameVerRegisterService
                        .registerMoveDeniedGameVer(val);
                break;
            case AUTO_MOVE_COMP:
                MoveDeniedCompRegisterService moveDeniedCompRegisterService = _app
                        .getBean("moveDeniedCompRegisterService",
                                MoveDeniedCompRegisterService.class);
                ret = moveDeniedCompRegisterService.registerMoveDeniedComp(val);
                break;
            case AUTO_MOVE_STORE:
                MoveDeniedBillRegisterService moveDeniedBillRegisterService = _app
                        .getBean("moveDeniedBillRegisterService",
                                MoveDeniedBillRegisterService.class);
                ret = moveDeniedBillRegisterService.registerMoveDeniedBill(val);
                break;
            case AUTO_MOVE_GAMEID_COMP:
                MoveDeniedGameCompRegisterService moveDeniedGameCompRegisterService = _app
                        .getBean("moveDeniedGameCompRegisterService",
                                MoveDeniedGameCompRegisterService.class);
                ret = moveDeniedGameCompRegisterService
                        .registerMoveDeniedGameComp(val);
                break;
            case AUTO_MOVE_GAMEID_STORE:
                MoveDeniedGameBillRegisterService moveDeniedGameBillRegisterService = _app
                        .getBean("moveDeniedGameBillRegisterService",
                                MoveDeniedGameBillRegisterService.class);
                ret = moveDeniedGameBillRegisterService
                        .registerMoveDeniedGameBill(val);
                break;
            case ROUTER_TYPE:
                RouterTypeRegisterService routerTypeRegisterService = _app
                        .getBean("routerTypeRegisterService",
                                RouterTypeRegisterService.class);
                ret = routerTypeRegisterService.registerRouterType(val);
                break;
            case LC_TYPE:
                LcTypeRegisterService lcTypeRegisterService = _app.getBean(
                        "lcTypeRegisterService", LcTypeRegisterService.class);
                ret = lcTypeRegisterService.registerLcType(val);
                break;

            }

            // レスポンス書き出し
            res.setCharacterEncoding(DEFAULT_CHARACTER_ENCODING);
            PrintWriter pw = res.getWriter();
            // クライアントにレスポンスの末尾を認識させるために最後尾に改行を挿入する
            pw.write(ret + "\n");

            // レスポンスをログに出力
            _log.info(logUtils.format("Response: %s", ret + "\n"));

        } catch (Exception e) {
            interrupt = true;
            // エラーログを出力
            _log.error(logUtils.format("Internal server error occured: %s", e));
            throw new ServletException(e);
        } finally {
            // 終了ログを出力
            _log.info(logUtils.formatFinishLog("AdminRegister", interrupt));
        }
    }

    private enum Command {

        GAME("game"),
        PLACE("place"),
        ROUTER("router"),
        MACHINE("machine"),
        PLACE_AUTH("place_auth"),
        COMP("comp"),
        COMP_AUTH("comp_auth"),
        AUTH_TYPE("auth_type"),
        AUTO_AUTH_COMP("auto_auth_comp"),
        AUTO_AUTH_STORE("auto_auth_store"),
        AUTO_AUTH_GAMEID_COMP("auto_auth_gameid_comp"),
        AUTO_AUTH_GAMEID_STORE("auto_auth_gameid_store"),
        AUTO_MOVE_GAMEID("auto_move_gameid"),
        AUTO_MOVE_GAMEID_VER("auto_move_gameid_ver"),
        AUTO_MOVE_COMP("auto_move_comp"),
        AUTO_MOVE_STORE("auto_move_store"),
        AUTO_MOVE_GAMEID_COMP("auto_move_gameid_comp"),
        AUTO_MOVE_GAMEID_STORE("auto_move_gameid_store"),
        ROUTER_TYPE("router_type"),
        LC_TYPE("lc_type"),
        UNKNOWN("");

        private String value;

        private Command(String v) {
            value = v;
        }

        public static Command fromValue(String v) {
            for (Command c : Command.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            return Command.UNKNOWN;
        }
    }

}
