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

import jp.co.sega.allnet.auth.api.domain.ReportData;
import jp.co.sega.allnet.auth.api.exception.ImageKeyInvalidException;
import jp.co.sega.allnet.auth.api.exception.InvalidParameterException;
import jp.co.sega.allnet.auth.api.service.report.ReportService;
import jp.co.sega.allnet.auth.api.util.JsonParser;
import jp.co.sega.allnet.auth.log.ApiLogUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * @author NakanoY
 * 
 */
public class Report extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final Logger _log = LoggerFactory.getLogger(Report.class);

    private ApplicationContext _context;

    private static final String RESPONSE_STR_OK = "OK";

    private static final String RESPONSE_STR_NG = "NG";

    @Override
    public void init() throws ServletException {
        _context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding(DEFAULT_ENCODING);

        ApiLogUtils logUtils = _context.getBean(ApiLogUtils.class);
        boolean interrupt = false;

        try {
            // 開始ログを出力
            _log.info(logUtils.formatStartLog("Report", req.getRemoteAddr()));

            ReportService service = _context.getBean("reportService",
                    ReportService.class);

            String ret = RESPONSE_STR_NG;
            try {
                // リクエストをパース
                ReportData data = JsonParser.readJsonStr(logUtils,
                        req.getInputStream(), ReportData.class);

                // 配信レポートの登録・更新
                service.report(data);
                ret = RESPONSE_STR_OK;
            } catch (ImageKeyInvalidException e) {
                // 不正なキーが入っている
                _log.warn(logUtils.format("ImageKey is invalid value"));
            } catch (JsonParseException e) {
                // JSONのパースに失敗
                _log.warn(logUtils.format("Json parse failed: %s",
                        e.getMessage()));
            } catch (InvalidParameterException e) {
                // 必須パラメータに値がない
                _log.warn(logUtils.format("%s is invalid value", e.getElement()));
            }

            // レスポンス書き出し
            PrintWriter pw = res.getWriter();
            pw.write(ret);

            if (_log.isDebugEnabled()) {
                _log.debug(logUtils.format("Response: %s", ret + "\n"));
            }
        } catch (Exception e) {
            interrupt = true;
            // エラーログを出力
            _log.error(
                    logUtils.format("Internal server error occured: %s",
                            e.getMessage()), e);
            throw new ServletException(e);
        } finally {
            // 終了ログを出力
            _log.info(logUtils.formatFinishLog("Report", interrupt));
        }
    }
}
