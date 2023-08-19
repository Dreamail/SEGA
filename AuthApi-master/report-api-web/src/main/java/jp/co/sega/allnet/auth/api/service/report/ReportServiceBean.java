/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.report;

import java.util.Calendar;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.ReportDao;
import jp.co.sega.allnet.auth.api.domain.ReportData;
import jp.co.sega.allnet.auth.api.domain.ReportImage;
import jp.co.sega.allnet.auth.api.exception.ImageKeyInvalidException;
import jp.co.sega.allnet.auth.api.exception.InvalidParameterException;
import jp.co.sega.allnet.auth.api.service.AbstractApiService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NakanoY
 * 
 */
@Component("reportService")
@Scope("singleton")
public class ReportServiceBean extends AbstractApiService implements
        ReportService {

    private static final Logger _log = LoggerFactory
            .getLogger(ReportServiceBean.class);

    @Resource(name = "reportDao")
    private ReportDao _dao;

    private static final int CONVERT_MILLI_TO_SECOND = 1000;

    private static final Calendar CALENDER_MAX_DATE = Calendar.getInstance();

    private static final Calendar CALENDER_DEFAULT_DATE = Calendar
            .getInstance();

    static {
        CALENDER_MAX_DATE.set(9999, 11, 31, 23, 59, 59);
        CALENDER_MAX_DATE.set(Calendar.MILLISECOND, 0);
        CALENDER_DEFAULT_DATE.set(1, 0, 1, 0, 0, 0);
        CALENDER_DEFAULT_DATE.set(Calendar.MILLISECOND, 0);
    }

    @Transactional
    @Override
    public void report(ReportData data) throws ImageKeyInvalidException,
            InvalidParameterException {

        if (data.getAppimage() != null) {
            // アプリケーションイメージ登録・更新のログを書き込む
            _log.info(formatLog("Start recording aprication report [%s]", data
                    .getAppimage().getString()));

            // パラメータのチェック
            checkReportParameter(data.getAppimage(), true);

            // アプリケーションイメージのレポートを登録・更新
            _dao.updateAppDeliverReport(data.getAppimage());
        } else if (data.getOptimage() != null) {
            // オプションイメージ登録・更新のログを書き込む
            _log.info(formatLog("Start recording option report [%s]", data
                    .getOptimage().getString()));

            // パラメータのチェック
            checkReportParameter(data.getOptimage(), false);

            // オプションイメージのレポートを登録・更新
            _dao.updateOptDeliverReport(data.getOptimage());
        } else {
            // 不明なキーが来た場合は例外を検知
            throw new ImageKeyInvalidException();
        }

    }

    private void checkReportParameter(ReportImage image, boolean appFlag)
            throws InvalidParameterException {
        // パラメータはすべて必須

        if (StringUtils.isEmpty(image.getSerial())) {
            throw new InvalidParameterException("serial");
        }

        if (image.getDfl().length > 0) {
            for (String s : image.getDfl()) {
                if (StringUtils.isEmpty(s)) {
                    throw new InvalidParameterException("dfl");
                }
            }
        }

        if (image.getWfl().length > 0) {
            for (String s : image.getWfl()) {
                if (StringUtils.isEmpty(s)) {
                    throw new InvalidParameterException("wfl");
                }
            }
        }

        checkNull("tsc", image.getTsc());
        checkNull("tdsc", image.getTdsc());

        image.setAt(convertSecondToMillis(image.getAt()));
        image.setOt(convertSecondToMillis(image.getOt()));
        image.setRt(convertSecondToMillis(image.getRt()));

        if (image.getAs() != AUTH_STATE_FAILED
                && image.getAs() != AUTH_STATE_SUCCESS) {
            throw new InvalidParameterException("as");
        }

        checkNull("rf_state", image.getRfState());
        checkNull("gd", image.getGd());
        checkNull("dav", image.getDav());
        checkNull("dov", image.getDov());

        if (appFlag) {
            // アプリケーションイメージのみ
            checkNull("wdav", image.getWdav());
            checkNull("wdov", image.getWdov());
        }

    }

    private void checkNull(String key, Object value)
            throws InvalidParameterException {
        if (value == null) {
            throw new InvalidParameterException(key);
        }
    }

    private long convertSecondToMillis(long unixTime) {
        if (unixTime < 0
                || unixTime * CONVERT_MILLI_TO_SECOND > CALENDER_MAX_DATE
                        .getTimeInMillis()) {
            return CALENDER_DEFAULT_DATE.getTimeInMillis();
        } else {
            return unixTime * CONVERT_MILLI_TO_SECOND;
        }
    }

}
