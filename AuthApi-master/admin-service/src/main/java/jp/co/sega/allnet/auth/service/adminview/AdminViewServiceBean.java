/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jp.co.sega.allnet.auth.common.entity.Log;
import jp.co.sega.allnet.auth.exception.ApplicationException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NakanoY
 * 
 */
@Component("adminViewService")
@Scope("singleton")
@Transactional
public class AdminViewServiceBean implements AdminViewService {

    private static final String DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    private static final String ACCESS_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS";

    @PersistenceContext
    private EntityManager _em;

    @Override
    public String adminView(String gameId, String dateString, String timeString) {

        StringBuilder res = new StringBuilder("START\n");
        boolean timeSpecified = false;

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        SimpleDateFormat accessDateFormat = new SimpleDateFormat(
                ACCESS_DATE_FORMAT);

        if (checkGameId(gameId)) {

            Date date = checkDate(dateString, timeString, dateTimeFormat);
            List<Log> logs = null;

            if (date == null) {
                // ゲームIDから最新の認証ログを取得
                logs = findLogs(gameId);

            } else {
                // ゲームID、日時から認証ログを取得
                logs = findLogs(gameId, date);
                timeSpecified = true;
            }

            for (Log l : logs) {
                if (l.getDebugInfo() != null) {
                    res.append(l.getDebugInfo());
                    res.append(",time=");
                    res.append(accessDateFormat.format(l.getAccessDate()));
                    res.append("\n");
                }
            }
        }

        if (timeSpecified) {
            res.append("END,");
            res.append(dateTimeFormat.format(new Date()));
        } else {
            res.append("END");
        }

        return res.toString();
    }

    /**
     * リクエストのゲームIDのチェックを行う。
     * 
     * @param gameId
     * @return
     */
    private boolean checkGameId(String gameId) {
        if (gameId != null && gameId.matches(GAMEID_MATCH_PATTERN)) {
            return true;
        }
        return false;
    }

    /**
     * リクエストの認証日と認証時刻のチェックを行う。
     * 
     * @param date
     * @param time
     * @param sd
     * @return
     */
    private Date checkDate(String date, String time, SimpleDateFormat sd) {

        if (date == null || time == null) {
            return null;
        }

        if (date.matches(DATE_MATCH_PATTERN)
                && time.matches(TIME_MATCH_PATTERN)) {
            try {
                return sd.parse(date + time);
            } catch (ParseException e) {
                throw new ApplicationException(e);
            }
        }

        return null;
    }

    /**
     * ゲームIDから認証ログを取得する。
     * 
     * @param gameId
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Log> findLogs(String gameId) {
        Query query = _em.createNamedQuery("findLogsByGameId");
        query.setParameter("gameId", gameId);
        return query.getResultList();
    }

    /**
     * ゲームID、日時から認証ログを取得する。
     * 
     * @param gameId
     * @param date
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Log> findLogs(String gameId, Date date) {
        Query query = _em.createNamedQuery("findLogsByGameIdAndAccessDate");
        query.setParameter("gameId", gameId);
        query.setParameter("date", date);
        return query.getResultList();
    }
}
