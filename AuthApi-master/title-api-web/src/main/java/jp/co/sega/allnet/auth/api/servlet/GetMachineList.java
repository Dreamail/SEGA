/**
 * Copyright (C) 2014 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.sega.allnet.auth.api.service.AuthenticationException;
import jp.co.sega.allnet.auth.api.service.machine.GetMachineListService;
import jp.co.sega.allnet.auth.api.util.ResponseUtils;
import jp.co.sega.allnet.auth.log.ApiLogUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * タイトルサーバ通信の基板情報取得API
 * 
 * @author NakanoY
 * 
 */
public class GetMachineList extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger _log = LoggerFactory
            .getLogger(GetMachineList.class);

    private static final String TITLE = "GetMachineList";

    private ApplicationContext _context;

    @Override
    public void init() throws ServletException {
        _context = WebApplicationContextUtils
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
        ApiLogUtils logUtils = _context.getBean(ApiLogUtils.class);
        boolean interrupt = false;
        try {
            // 開始ログを出力
            _log.info(logUtils.formatStartLog(TITLE));

            GetMachineListService service = _context.getBean(
                    "getMachineListService", GetMachineListService.class);
            
            // 基板情報取得処理
            String ret = service.getMachineList(req.getParameter("id"),
                    req.getParameter("pw"), Boolean.parseBoolean(req.getParameter("allMachines")));
            
            // レスポンス書き出し
            ResponseUtils.writeResponse(ret, res,
                    Boolean.parseBoolean(req.getParameter("scramble")));

            if (_log.isDebugEnabled()) {
                _log.debug(logUtils.format("Response: %s", ret));
            }
        } catch (AuthenticationException e) {
            // エラーログを出力
            _log.warn(logUtils.format("Authentication failed: %s", e));
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            interrupt = true;
            // エラーログを出力
            _log.error(logUtils.format("Internal server error occured: %s", e));
            throw new ServletException(e);
        } finally {
            // 終了ログを出力
            _log.info(logUtils.formatFinishLog(TITLE, interrupt));
        }
    }
}
