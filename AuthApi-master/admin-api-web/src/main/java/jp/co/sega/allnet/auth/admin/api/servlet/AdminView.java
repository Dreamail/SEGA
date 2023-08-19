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
import jp.co.sega.allnet.auth.service.adminview.AdminViewService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 管理サーバから認証ログ取得処理を受けつける。
 * 
 * @author NakanoY
 * 
 */
public class AdminView extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * デフォルトのエンコーディングはShift_JIS(Cp943c)
     */
    private static final String DEFAULT_CHARACTER_ENCODING = "Cp943C";

    private static final Logger _log = LoggerFactory.getLogger(AdminView.class);

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
            _log.info(logUtils.formatStartLog("AdminView"));

            AdminViewService service = _context.getBean("adminViewService",
                    AdminViewService.class);
            // 認証ログ取得処理
            String ret = service.adminView(req.getParameter("game_id"),
                    req.getParameter("date"), req.getParameter("time"));

            // レスポンス書き出し
            res.setCharacterEncoding(DEFAULT_CHARACTER_ENCODING);
            PrintWriter pw = res.getWriter();
            pw.write(ret + "\n");

            if (_log.isDebugEnabled()) {
                _log.debug(logUtils.format("Response: %s", ret + "\n"));
            }
        } catch (Exception e) {
            interrupt = true;
            // エラーログを出力
            _log.error(logUtils.format("Internal server error occured: %s", e));
            throw new ServletException(e);
        } finally {
            // 終了ログを出力
            _log.info(logUtils.formatFinishLog("AdminView", interrupt));
        }
    }

}
