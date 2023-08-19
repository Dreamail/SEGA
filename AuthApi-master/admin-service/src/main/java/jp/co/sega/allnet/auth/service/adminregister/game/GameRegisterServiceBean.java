/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.game;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jp.co.sega.allnet.auth.common.entity.Country;
import jp.co.sega.allnet.auth.common.entity.Game;
import jp.co.sega.allnet.auth.common.entity.GameAttribute;
import jp.co.sega.allnet.auth.common.entity.GameAttributePK;
import jp.co.sega.allnet.auth.common.entity.TitleApiAccount;
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
@Component("gameRegisterService")
@Scope("singleton")
@Transactional
public class GameRegisterServiceBean extends AbstractRegisterService implements
        GameRegisterService {

    private static final Logger _log = LoggerFactory
            .getLogger(GameRegisterServiceBean.class);

    @PersistenceContext
    private EntityManager _em;

    @Override
    public String registerGame(String val) {
        return "";
    }

    @Override
    public RegisterServiceResult<GameRegisterResult> registerGameForAdmin(
            String val) {
        if (_log.isInfoEnabled()) {
            _log.info(getLogUtils().format("Received csv:[%s]", val));
        }

        try (CSVReader reader = new CSVReader(new StringReader(val))) {
            List<GameRegisterResult> results = new ArrayList<GameRegisterResult>();
            String[] line;
            int cnt = 0;
            while ((line = reader.readNext()) != null) {

                GameRegisterResult r = registerGame(line);
                if (!r.isSuccess()) {
                    cnt++;
                }
                results.add(r);
            }
            return new RegisterServiceResult<GameRegisterResult>(cnt, results);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * ゲーム情報を登録する。
     * 
     * @param line
     * @return
     */
    private GameRegisterResult registerGame(String[] line) {

        GameRegisterParameter param = new GameRegisterParameter(line);

        // 入力チェック
        if (!checkParameter(param)) {
            return new GameRegisterResult(false, param, null);
        }

        GameAttribute gameAttr = findGameAttribute(param);

        if (param.getAuth().equals(AUTH_TYPE_DELETE)) {
            if (gameAttr == null) {
                return new GameRegisterResult(false, param, null);
            }
            // ゲーム情報の削除
            removeGame(gameAttr);
        } else {
            // ゲーム情報の登録・更新
            if (gameAttr == null) {

                Country country = findCountry(param.getCountryCode());
                if (country == null) {
                    return new GameRegisterResult(false, param, null);
                }
                // ゲーム情報の登録
                gameAttr = createGame(param, country);

                TitleApiAccount titleAccount = _em.find(TitleApiAccount.class,
                        param.getGameId());
                if (titleAccount == null) {
                    // タイトルAPIアカウント情報の登録
                    createTitleApiAccount(param);
                }
            } else {
                // ゲーム情報の更新
                gameAttr = modifyGame(param, gameAttr);
            }
        }

        return new GameRegisterResult(true, param, gameAttr);

    }

    /**
     * ゲーム情報を取得する。
     * 
     * @param param
     * @return
     */
    private GameAttribute findGameAttribute(GameRegisterParameter param) {
        GameAttributePK pk = new GameAttributePK();
        pk.setGameId(param.getGameId());
        pk.setGameVer(param.getGameVer());
        pk.setCountryCode(param.getCountryCode());
        return _em.find(GameAttribute.class, pk);
    }

    /**
     * ゲーム情報の削除を行う。
     * 
     * @param gameAttr
     */
    @SuppressWarnings("unchecked")
    private void removeGame(GameAttribute gameAttr) {
        remove(gameAttr);

        Query query = _em.createNamedQuery("findGameAttrByGameId");
        query.setParameter("gameId", gameAttr.getPk().getGameId());
        List<GameAttribute> list = query.getResultList();

        if (list.size() == 0) {
            Game game = _em.getReference(Game.class, gameAttr.getPk()
                    .getGameId());
            remove(game);
        }
    }

    /**
     * ゲーム情報の登録を行う。
     * 
     * @param param
     * @param country
     * @return
     */
    private GameAttribute createGame(GameRegisterParameter param,
            Country country) {

        Date now = new Date();

        Game game = _em.find(Game.class, param.getGameId());

        if (game == null) {
            game = new Game();
            game.setGameId(param.getGameId());
            game.setTitle(param.getTitle());
            game.setCreateDate(now);
            game.setCreateUserId(getCurrentUser());
            game.setUpdateDate(now);
            game.setUpdateUserId(getCurrentUser());
            persist(game);
        }

        GameAttributePK pk = new GameAttributePK();
        pk.setGameId(param.getGameId());
        pk.setGameVer(param.getGameVer());
        pk.setCountryCode(param.getCountryCode());
        GameAttribute gameAttr = new GameAttribute();
        gameAttr.setPk(pk);
        gameAttr.setTitle(param.getTitle());
        gameAttr.setUri(param.getUri());
        gameAttr.setHost(param.getHost());
        gameAttr.setAuth(new BigDecimal(param.getAuth()));
        gameAttr.setCountry(country);
        gameAttr.setCreateDate(now);
        gameAttr.setCreateUserId(getCurrentUser());
        gameAttr.setUpdateDate(now);
        gameAttr.setUpdateUserId(getCurrentUser());
        persist(gameAttr);

        return gameAttr;
    }

    /**
     * 国情報を取得する。
     * 
     * @param countryCode
     * @return
     */
    private Country findCountry(String countryCode) {
        return _em.find(Country.class, countryCode);
    }

    /**
     * ゲーム情報の更新を行う。
     * 
     * @param param
     * @param gameAttr
     * @return
     */
    private GameAttribute modifyGame(GameRegisterParameter param,
            GameAttribute gameAttr) {

        Date now = new Date();
        gameAttr.setTitle(param.getTitle());
        gameAttr.setUri(param.getUri());
        gameAttr.setHost(param.getHost());
        gameAttr.setAuth(new BigDecimal(param.getAuth()));
        gameAttr.setUpdateDate(now);
        gameAttr.setUpdateUserId(getCurrentUser());
        gameAttr = merge(gameAttr);

        return gameAttr;
    }

    /**
     * 認証APIアカウント情報の登録を行う。
     * 
     * @param gameId
     */
    private void createTitleApiAccount(GameRegisterParameter param) {
        Date now = new Date();
        TitleApiAccount a = new TitleApiAccount();
        a.setGameId(param.getGameId());
        a.setPassword(TitleApiPasswordGenerator.generate(param.getGameId()));
        a.setCreateDate(now);
        a.setCreateUserId(getCurrentUser());
        a.setUpdateDate(now);
        a.setUpdateUserId(getCurrentUser());
        persist(a);
    }

}
