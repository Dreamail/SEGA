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

import jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnService;
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
public class PowerOn extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String STR_ENCODED = "DFI";

    private static final Logger _log = LoggerFactory.getLogger(PowerOn.class);

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
        try {
            // 開始ログを出力
            _log.info(logUtils.formatStartLog("PowerOn", req.getRemoteAddr()));

            if (req.getHeader("Pragma") == null
                    || !req.getHeader("Pragma").equals(STR_ENCODED)) {
                // ヘッダのPragmaが無い、もしくは"DFI"ではない場合は何もせずに終了する
                _log.warn(logUtils.format(
                        "Invalid header [Pragma:%s] is found",
                        req.getHeader("Pragma")));
                // 終了ログを出力
                _log.info(logUtils.formatFinishLog("PowerOn", false, ""));
                return;
            }

            PowerOnService service = _context.getBean("powerOnService",
                    PowerOnService.class);

            // リクエストのスクランブル解除
            String plainReq = CodecUtils.decodeStream(req.getInputStream());
            // リクエスト末尾の改行を除去
            plainReq = plainReq.replace("\n", "");
            // パラメータをマッピング
            Map<String, String> map = RequestUtils.mapParameters(plainReq,
                    RequestUtils.DEFAULT_CHARACTER_ENCODING);

            // パラメータをセット
            PowerOnParameter param = new PowerOnParameter(map.get("game_id"),
                    map.get("ver"), map.get("serial"), map.get("ip"),
                    req.getRemoteAddr(), map.get("firm_ver"),
                    map.get("boot_ver"), map.get("encode"),
                    map.get("format_ver"), req.getHeader("User-Agent"),
                    map.get("hops"), plainReq, map.get("token"));

            // 認証処理
            String ret = service.powerOn(param);

            // レスポンス書き出し
            res.setHeader("Pragma", STR_ENCODED);
            res.setContentType("text/html; charset=" + param.getEncode());
            String responseBody = CodecUtils.encodeString(ret,
                    param.getEncode());
            PrintWriter pw = res.getWriter();
            pw.write(responseBody);
            // 終了ログを出力
            _log.info(logUtils.formatFinishLog("PowerOn", false,
                    ret.replace("\n", "")));
        } catch (Exception e) {
            // エラーログを出力
            _log.error(logUtils.format("Internal server error occured: %s", e));
            // 終了ログを出力
            _log.info(logUtils.formatFinishLog("PowerOn", true));
            throw new ServletException(e);
        }
    }
}
