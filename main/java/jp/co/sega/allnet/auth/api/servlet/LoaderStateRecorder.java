/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter;
import jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderService;
import jp.co.sega.allnet.auth.log.ApiLogUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author TsuboiY
 * 
 */
public class LoaderStateRecorder extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger _log = LoggerFactory
            .getLogger(LoaderStateRecorder.class);

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
            _log.info(logUtils.formatStartLog("LoaderStateRecorder",
                    req.getRemoteAddr()));

            LoaderStateRecorderService service = _context.getBean(
                    "loaderStateRecorderService",
                    LoaderStateRecorderService.class);

            // パラメータをセット
            LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                    req.getParameter("serial"), req.getParameter("dvd"),
                    req.getParameter("net"), req.getParameter("work"),
                    req.getParameter("old_net"), req.getParameter("deliver"),
                    req.getParameter("nb_ftd"), req.getParameter("nb_dld"),
                    req.getParameter("last_sysa"), req.getParameter("sysa_st"),
                    req.getParameter("dld_st"));

            // 配信指示書URI取得処理
            String ret = service.loaderStateRecorder(param);

            // レスポンス書き出し
            PrintWriter pw = res.getWriter();
            pw.write(ret);

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
            _log.info(logUtils
                    .formatFinishLog("LoaderStateRecorder", interrupt));
        }
    }

}
