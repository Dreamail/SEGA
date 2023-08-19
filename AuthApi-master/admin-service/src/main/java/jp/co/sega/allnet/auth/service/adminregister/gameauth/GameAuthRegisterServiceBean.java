/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.gameauth;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jp.co.sega.allnet.auth.common.entity.GameAttribute;
import jp.co.sega.allnet.auth.common.entity.GameAttributePK;
import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterService;
import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVReader;

/**
 * @author NakanoY
 * 
 */
@Component("gameAuthRegisterService")
@Scope("singleton")
@Transactional
public class GameAuthRegisterServiceBean extends AbstractRegisterService
        implements GameAuthRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(GameAuthRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Override
    public RegisterServiceResult<GameAuthRegisterResult> registerGameAuthForAdmin(
            String val) {

        if (_log.isInfoEnabled()) {
            _log.info(formatCsvValueLog(val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            List<GameAuthRegisterResult> results = new ArrayList<GameAuthRegisterResult>();
            String[] line;
            int cnt = 0;
            while ((line = reader.readNext()) != null) {
                GameAuthRegisterResult r = registerGameAuth(line);
                if (!r.isSuccess()) {
                    cnt++;
                }
                results.add(r);
            }
            return new RegisterServiceResult<GameAuthRegisterResult>(cnt,
                    results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * ゲーム認証情報を登録する。
     * 
     * @param line
     * @return
     */
    private GameAuthRegisterResult registerGameAuth(String[] line) {

        GameAuthRegisterParameter param = new GameAuthRegisterParameter(line);

        // 入力チェック
        if (!checkParameter(param)) {
            return new GameAuthRegisterResult(false, param, null);
        }

        if (param.getAuth().equals(AUTH_TYPE_DELETE)) {
            // 認証方式が"0"なら処理しない
            return new GameAuthRegisterResult(false, param, null);
        }

        GameAttribute gameAttr = findGameAttribute(param);

        if (gameAttr == null) {
            // ゲーム属性情報が存在しなければ処理しない
            return new GameAuthRegisterResult(false, param, null);
        }

        // ゲーム属性情報の認証方式を更新
        gameAttr = modifyGame(param, gameAttr);

        return new GameAuthRegisterResult(true, param, gameAttr);

    }

    /**
     * ゲーム属性情報を取得する。
     * 
     * @param param
     * @return
     */
    private GameAttribute findGameAttribute(GameAuthRegisterParameter param) {
        GameAttributePK pk = new GameAttributePK();
        pk.setGameId(param.getGameId());
        pk.setGameVer(param.getGameVer());
        pk.setCountryCode(param.getCountryCode());
        return _em.find(GameAttribute.class, pk);
    }

    /**
     * ゲーム属性情報の認証方式の更新を行う。
     * 
     * @param param
     * @param gameAttr
     * @return
     */
    private GameAttribute modifyGame(GameAuthRegisterParameter param,
            GameAttribute gameAttr) {
        Date now = new Date();
        gameAttr.setAuth(new BigDecimal(param.getAuth()));
        gameAttr.setUpdateDate(now);
        gameAttr.setUpdateUserId(getCurrentUser());
        gameAttr = merge(gameAttr);
        return gameAttr;
    }

}
