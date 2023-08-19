/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authtype;

import java.io.IOException;
import java.io.StringReader;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;

/**
 * @author TsuboiY
 * 
 */
@Component("authTypeRegisterService")
@Scope("singleton")
public class AuthTypeRegisterServiceBean extends AbstractRegisterService implements
        AuthTypeRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(AuthTypeRegisterServiceBean.class);

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor _msg;

    @Override
    public String registerAuthType(String val) {
        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        final StringBuilder res = new StringBuilder("START");

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            String[] line;
            int num = 1;
            while ((line = reader.readNext()) != null) {

                if (line.length < CSV_COLUMN_SIZE) {
                    String[] wk = new String[CSV_COLUMN_SIZE];
                    System.arraycopy(line, 0, wk, 0, line.length);
                    line = wk;
                }

                res.append("\n");

                res.append(num);
                res.append(",");
                if (line[0] != null) {
                    res.append(line[0]);
                }
                res.append(",");
                if (line[1] != null) {
                    res.append(line[1]);
                }
                res.append(",");
                res.append(_msg
                        .getMessage("adminregister.status.unimplemented"));
                res.append(",");
                res.append("0");

                num++;
            }

            res.append("\n");

            res.append("END");
            res.append(",");
            res.append(",");
            res.append(",");
            res.append(",");
            res.append("0");

            return res.toString();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

}
