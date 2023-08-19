/**
 * Copyright (C) 2014 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.machine;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.ApiAccountDao;
import jp.co.sega.allnet.auth.api.dao.MachineTableDao;
import jp.co.sega.allnet.auth.api.domain.Machine;
import jp.co.sega.allnet.auth.api.service.AuthenticationException;
import jp.co.sega.allnet.auth.api.service.machinetable.MachineTableServiceBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NakanoY
 * 
 */
@Component("getMachineListService")
@Scope("singleton")
public class GetMachineListServiceBean extends MachineTableServiceBean implements
        GetMachineListService {

    private static final Logger _log = LoggerFactory
            .getLogger(GetMachineListServiceBean.class);

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    @Resource(name = "apiAccountDao")
    private ApiAccountDao _apiAccountDao;

    @Resource(name = "machineTableDao")
    private MachineTableDao _machineTableDao;

    @Transactional
    @Override
    public String getMachineList(String gameId, String password, boolean allMachine) 
            throws AuthenticationException {
        _log.info(formatLog("Start finding machine table [id=%s&pw=%s&allMachines=%s]",
                gameId, password, allMachine));

        List<Machine> machines;
        if (_apiAccountDao.checkApiAccount(gameId, password)) {
            machines = findMachines(gameId, allMachine);
        } else {
            // アカウントが不正
            throw new AuthenticationException();
        }

        StringBuilder res = new StringBuilder();

        DateFormat df = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        for (Machine m : machines) {
            res.append(m.getAllnetId());
            res.append(",");
            res.append(m.getPlaceId());
            res.append(",");
            res.append(m.getSerial());
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
            res.append("\n");
        }

        _log.info(formatLog("Finding machines was successful [count:%s]",
                machines.size()));

        return res.toString();
    }

    /**
     * 基板情報を取得する。
     * 
     * @param gameId
     * @param all
     * @return
     */
    private List<Machine> findMachines(String gameId, boolean allMachine) {
        if (allMachine) {
            return _machineTableDao.findMachinesIncludeAuthDenied(gameId);
        }
        return _machineTableDao.findMachines(gameId);
    }
}
