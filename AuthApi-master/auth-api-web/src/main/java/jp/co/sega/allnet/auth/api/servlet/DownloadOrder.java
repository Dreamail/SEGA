/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderParameter;
import jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrderService;
import jp.co.sega.allnet.auth.api.util.RequestUtils;
import jp.co.sega.allnet.auth.codec.CodecUtils;
import jp.co.sega.allnet.auth.log.ApiLogUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author NakanoY
 * 
 */
public class DownloadOrder extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String STR_ENCODED = "DFI";

    private static final String DEFAULT_HEADER_PRAGMA = "Pragma";

    private static final Logger _log = LoggerFactory
            .getLogger(DownloadOrder.class);

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
            _log.info(logUtils.formatStartLog("DownloadOrder",
                    req.getRemoteAddr()));

            DownloadOrderService service = _context.getBean(
                    "downloadOrderService", DownloadOrderService.class);
            DownloadOrderParameter param;
            if (req.getHeader(DEFAULT_HEADER_PRAGMA) != null
                    && req.getHeader(DEFAULT_HEADER_PRAGMA).equals(STR_ENCODED)) {
                // リクエストがスクランブルされていた場合、リクエストのスクランブル解除
                String plainReq = CodecUtils.decodeStream(req.getInputStream());
                // リクエスト末尾の改行を除去
                plainReq = plainReq.replace("\n", "");
                // パラメータをマッピング
                Map<String, String> map = RequestUtils.mapParameters(plainReq,
                        RequestUtils.DEFAULT_CHARACTER_ENCODING);

                param = new DownloadOrderParameter(map.get("game_id"),
                        map.get("ver"), map.get("serial"), map.get("ip"),
                        map.get("encode"));
            } else {
                // リクエストがスクランブルされていない場合、そのままパラメータに保存
                param = new DownloadOrderParameter(req.getParameter("game_id"),
                        req.getParameter("ver"), req.getParameter("serial"),
                        req.getParameter("ip"), req.getParameter("encode"));
            }

            // 配信指示書URI取得処理
            String ret = service.downloadOrder(param);

            // レスポンス書き出し
            res.setContentType("text/html; charset=" + param.getEncode());
            PrintWriter pw = res.getWriter();

            if (req.getHeader(DEFAULT_HEADER_PRAGMA) != null
                    && req.getHeader(DEFAULT_HEADER_PRAGMA).equals(STR_ENCODED)) {
                // リクエストがスクランブルされていた場合、レスポンスもスクランブルする
                res.setHeader(DEFAULT_HEADER_PRAGMA, STR_ENCODED);
                pw.write(CodecUtils.encodeString(ret, param.getEncode()));
            } else {
                // リクエストがスクランブルされていない場合、平文を返す
                pw.write(ret);
            }
        } catch (Exception e) {
            interrupted = true;
            // エラーログを出力
            _log.error(logUtils.format("Internal server error occured: %s", e));
            throw new ServletException(e);
        } finally {
            // 終了ログを出力
            _log.info(logUtils.formatFinishLog("DownloadOrder", interrupted));
        }
    }

}
