/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.machinetable;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.ApiAccountDao;
import jp.co.sega.allnet.auth.api.dao.MachineTableDao;
import jp.co.sega.allnet.auth.api.domain.MachineTable;
import jp.co.sega.allnet.auth.api.service.AbstractApiService;
import jp.co.sega.allnet.auth.csv.CsvUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TsuboiY
 * 
 */
@Component("machineTableService")
@Scope("singleton")
public class MachineTableServiceBean extends AbstractApiService implements
        MachineTableService {

    private static final Logger _log = LoggerFactory
            .getLogger(MachineTableServiceBean.class);

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    @Resource(name = "apiAccountDao")
    private ApiAccountDao _apiAccountDao;

    @Resource(name = "machineTableDao")
    private MachineTableDao _machineTableDao;

    @Transactional
    @Override
    public String machineTable(String gameId, String password, boolean all) {
        return machineTable(gameId, password, all, false);
    }

    @Transactional
    @Override
    public String machineTable(String gameId, String password, boolean all,
            boolean auth) {
        _log.info(formatLog("Start finding machine table [id=%s&pw=%s&all=%s]",
                gameId, password, all));

        List<MachineTable> machineTables;
        if (checkApiAccount(gameId, password)) {
            machineTables = findMachineTables(gameId, all);
        } else if (auth) {
            return null;
        } else {
            machineTables = Collections.emptyList();
        }

        StringBuilder res = new StringBuilder();
        res.append("START:MACHINES");

        DateFormat df = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        for (MachineTable m : machineTables) {
            res.append("\n");
            if (m.getRegion0Id() == null) {
                // 論理削除された店舗の場合は0を返す
                res.append(0);
            } else {
                res.append(m.getRegion0Id());
            }
            res.append(",");
            res.append(m.getPlaceId());
            res.append(",");
            res.append(escape(m.getName()));
            res.append(",");
            res.append(m.getSerial());
            res.append(",");
            res.append(m.getGroupIndex());
            res.append(",");
            Timestamp lastAccess = m.getLastAccess();
            if (lastAccess == null) {
                res.append("null");
            } else {
                res.append(df.format(m.getLastAccess()));
            }
            res.append(",");
            Timestamp lastAuth = m.getLastAuth();
            if (lastAuth == null) {
                res.append("null");
            } else {
                res.append(df.format(lastAuth));
            }
        }

        _log.info(formatLog("Finding machine table was successful [count:%s]",
                machineTables.size()));

        return res.toString();
    }

    /**
     * ゲームアカウントをチェックする。
     * 
     * @param gameId
     * @param password
     * @return
     */
    private boolean checkApiAccount(String gameId, String password) {
        return _apiAccountDao.checkApiAccount(gameId, password);
    }

    /**
     * 基板情報を取得する。
     * 
     * @param gameId
     * @param all
     * @return
     */
    private List<MachineTable> findMachineTables(String gameId, boolean all) {
        if (all) {
            return _machineTableDao.findMachineTablesIncludeAuthDenied(gameId);
        }
        return _machineTableDao.findMachineTables(gameId);
    }

    /**
     * エスケープ処理を行う。
     * 
     * @param s
     * @return
     */
    private String escape(String s) {
        return CsvUtils.escapeForCsv(s);
    }

}
