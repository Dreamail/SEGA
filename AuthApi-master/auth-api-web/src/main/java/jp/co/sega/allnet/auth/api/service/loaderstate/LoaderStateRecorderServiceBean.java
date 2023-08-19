/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.loaderstate;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.LoaderStateRecorderDao;
import jp.co.sega.allnet.auth.api.domain.LoaderStateLog;
import jp.co.sega.allnet.auth.api.service.AbstractApiService;
import jp.co.sega.allnet.auth.api.util.InvalidParameterException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TsuboiY
 * 
 */
@Component("loaderStateRecorderService")
@Scope("singleton")
public class LoaderStateRecorderServiceBean extends AbstractApiService
        implements LoaderStateRecorderService {

    private static final Logger _log = LoggerFactory
            .getLogger(LoaderStateRecorderServiceBean.class);

    @Resource(name = "loaderStateRecorderDao")
    private LoaderStateRecorderDao _dao;

    @Transactional
    @Override
    public String loaderStateRecorder(LoaderStateRecorderParameter param)
            throws InvalidParameterException {
        _log.info(formatLog("Start recording loader state [%s]",
                param.buildQueryString()));
        try {
            // パラメータをチェック
            LoaderStateLog loaderStateLog = buildLoaderStateLog(param);
            // 配信レポートログに存在するかチェック
            if (checkExist(loaderStateLog)) {
                // 配信レポートログを更新
                update(loaderStateLog);
            } else {
                // 配信レポートログに追加
                register(loaderStateLog);
            }

            _log.info(formatLog("Recording loader state was successful"));

            return RESPONSE_STR_OK;

        } catch (InvalidParameterException e) {
            // パラメータが不正の場合
            _log.warn(formatLog("Recording loader state was failed: %s",
                    e.getMessage()));
            return RESPONSE_STR_NG;
        }
    }

    /**
     * パラメータをチェックして {@link LoaderStateLog} を構築して返却する。
     * 
     * @param param
     * @return
     * @throws InvalidParameterException
     */
    private LoaderStateLog buildLoaderStateLog(
            LoaderStateRecorderParameter param)
            throws InvalidParameterException {
        // パラメータは全て必要
        if (StringUtils.isEmpty(param.getSerial())) {

            throw new InvalidParameterException("serial");

        }
        if (StringUtils.isEmpty(param.getDvd())) {

            throw new InvalidParameterException("dvd");

        }
        if (StringUtils.isEmpty(param.getNet())) {

            throw new InvalidParameterException("net");
        }
        if (StringUtils.isEmpty(param.getWork())) {

            throw new InvalidParameterException("work");

        }
        if (StringUtils.isEmpty(param.getOldNet())) {

            throw new InvalidParameterException("old_net");

        }
        if (StringUtils.isEmpty(param.getDeliver())) {

            throw new InvalidParameterException("deliver");

        }
        if (StringUtils.isEmpty(param.getNbFtd())) {

            throw new InvalidParameterException("nb_ftd");

        }
        if (StringUtils.isEmpty(param.getNbDld())) {

            throw new InvalidParameterException("nb_dld");

        }
        if (StringUtils.isEmpty(param.getLastSysa())) {

            throw new InvalidParameterException("last_sysa");

        }
        if (StringUtils.isEmpty(param.getSysaSt())) {

            throw new InvalidParameterException("sysa_st");

        }
        if (StringUtils.isEmpty(param.getDldSt())) {

            throw new InvalidParameterException("dld_st");

        }

        // LoaderStateLogにセット
        LoaderStateLog log = new LoaderStateLog();
        log.setSerial(param.getSerial());
        log.setDvd(new BigDecimal(param.getDvd()));
        log.setNet(new BigDecimal(param.getNet()));
        log.setWork(new BigDecimal(param.getWork()));
        log.setOldNet(new BigDecimal(param.getOldNet()));
        log.setDeliver(new BigDecimal(param.getDeliver()));
        log.setFilesToDownload(new BigDecimal(param.getNbFtd()));
        log.setFilesDownloaded(new BigDecimal(param.getNbDld()));
        // リクエストはunix秒で渡ってくる
        log.setLastAuth(new Timestamp(
                Long.parseLong(param.getLastSysa()) * 1000));
        log.setLastAuthState(new BigDecimal(param.getSysaSt()));
        log.setDownloadState(new BigDecimal(param.getDldSt()));
        return log;
    }

    /**
     * 配信レポートログに存在するかチェックする。
     * 
     * @param param
     * @return
     */
    private boolean checkExist(LoaderStateLog loaderStateLog) {
        return _dao.checkExist(loaderStateLog.getSerial());
    }

    /**
     * 配信レポートに追加する。
     * 
     * @param param
     */
    private void register(LoaderStateLog loaderStateLog) {
        _dao.insertLoaderState(loaderStateLog);
    }

    /**
     * 配信レポートを更新する。
     * 
     * @param param
     */
    private void update(LoaderStateLog loaderStateLog) {
        _dao.updateLoaderState(loaderStateLog);
    }

}
