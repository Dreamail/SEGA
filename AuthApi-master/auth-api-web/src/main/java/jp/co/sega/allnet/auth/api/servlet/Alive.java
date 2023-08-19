/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import jp.co.sega.allnet.auth.log.ApiLogUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * NOCからの死活監視
 * 
 * @author NakanoY
 * 
 */
public class Alive extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger _log = LoggerFactory.getLogger(Alive.class);

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
        boolean interrupted = false;
        try {
            // 開始ログを出力
            _log.info(logUtils.formatStartLog("Alive", req.getRemoteAddr()));
            JdbcTemplate jdbc = new JdbcTemplate(_context.getBean("dataSource",
                    DataSource.class));

            // DBから現在日時を取得
            Date sysdate = jdbc.queryForObject("SELECT current_timestamp",
                    Date.class);
            _log.info(logUtils.format("Alive called! sysdate:%s", sysdate));

            // レスポンス書き出し
            PrintWriter pw = res.getWriter();
            pw.write("OK");
        } catch (Exception e) {
            interrupted = true;
            // エラーログを出力
            _log.error(logUtils.format("Internal server error occured: %s", e));
            throw new ServletException(e);
        } finally {
            // 終了ログを出力
            _log.info(logUtils.formatFinishLog("Alive", interrupted));
        }
    }
}
