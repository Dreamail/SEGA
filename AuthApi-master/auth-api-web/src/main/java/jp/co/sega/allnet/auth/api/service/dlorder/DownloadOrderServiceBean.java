/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.dlorder;

import java.util.Iterator;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.DownloadOrderDao;
import jp.co.sega.allnet.auth.api.service.AbstractApiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NakanoY
 * 
 */
@Component("downloadOrderService")
@Scope("singleton")
public class DownloadOrderServiceBean extends AbstractApiService implements
        DownloadOrderService {

    private static final int IMAGE_TYPE_APPLY = 0;

    private static final int IMAGE_TYPE_OPTION = 1;
    
    private static final Logger _log = LoggerFactory
            .getLogger(DownloadOrderServiceBean.class);

    @Resource(name = "downloadOrderDao")
    private DownloadOrderDao _dao;

    @Transactional
    @Override
    public String downloadOrder(DownloadOrderParameter param) {
        _log.info(formatLog("Start finding download order [%s]",
                param.buildQueryString()));

        // アプリケーションイメージの基板配信指示書情報を取得
        DownloadOrder appOrder = findMachineDownloadOrder(param, IMAGE_TYPE_APPLY);

        if (appOrder.getSetting() == SETTING_COMM_OK) {
            // 基板の通信が許可されている

            // 同一店舗に設置されている他の基板情報を取得
            appOrder = findGroupSerials(appOrder, param);

            // 配信指示書URIを取得
            appOrder = findUri(appOrder, param, IMAGE_TYPE_APPLY);
            
        }
        
        // オプションイメージの基板配信指示書情報を取得
        DownloadOrder optOrder = findMachineDownloadOrder(param, IMAGE_TYPE_OPTION);
        if (optOrder.getSetting() == SETTING_COMM_OK) {
            // 基板の通信が許可されている
            optOrder = findUri(optOrder, param, IMAGE_TYPE_OPTION);
            
        }
        
        // レスポンスの作成
        String res = createResponse(appOrder, optOrder);

        _log.info(formatLog("Finding download order was successful [%s]", res));

        return res;
    }

    /**
     * 基板配信指示書情報を取得する。
     * 
     * @param param
     * @return
     */
    private DownloadOrder findMachineDownloadOrder(DownloadOrderParameter param, int type) {
        return _dao.findMachineDownloadOrder(param.getSerial(), type);
    }

    /**
     * グループ内のほかの基板シリアルを取得する。
     * 
     * @param order
     * @param param
     * @return
     */
    private DownloadOrder findGroupSerials(DownloadOrder order,
            DownloadOrderParameter param) {
        order.setGroupSerials(_dao.findGroupSerials(order.getSerial(),
                order.getAllnetId(), order.getGroupIndex(), param.getGameId()));
        return order;
    }

    /**
     * 配信指示書URIを設定する。
     * 
     * @param order
     * @param param
     * @param type
     * @return
     */
    private DownloadOrder findUri(DownloadOrder order,
            DownloadOrderParameter param, int type) {
        
        String typeStr = "Apply";
        if(type >= IMAGE_TYPE_OPTION){
            typeStr = "Option";
        }
        
        if (order.getUri() != null) {
            // 基板配信指示書を使用する
            _log.info(formatLog("%s machine download order as a download order uri", typeStr));
            return order;
        }
        // 国別配信指示書からURIを設定
        order.setUri(_dao.findUriByCountry(param.getGameId(), param.getVer(),
                order.getCountryCode(), type));
        if (order.getUri() != null) {
            _log.info(formatLog("%s country download order as a download order uri", typeStr));
            return order;
        }
        // 配信指示書からURIを設定
        order.setUri(_dao.findUriByGame(param.getGameId(), param.getVer(), type));
        if (order.getUri() != null) {
            _log.info(formatLog("%s basic download order as a download order uri", typeStr));
        }
        return order;
    }
    
    /**
     * レスポンスで返却する文字列を作成する。
     * 
     * @param appOrder
     * @param optOrder
     * @return
     */
    private String createResponse(DownloadOrder appOrder, DownloadOrder optOrder) {
        StringBuilder sb = new StringBuilder();

        sb.append("stat=");
        sb.append(appOrder.getSetting());

        if (appOrder.getSetting() == DownloadOrderService.SETTING_COMM_OK) {
            if (!appOrder.getGroupSerials().isEmpty()) {
                sb.append("&");
                sb.append("serial=");
                Iterator<String> ite = appOrder.getGroupSerials().iterator();
                for (int i = 0; ite.hasNext(); i++) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append(ite.next());
                }
            }

            sb.append("&");
            sb.append("uri=");
            if(appOrder.getUri() != null || optOrder.getUri() != null){
            
                if(appOrder.getUri() != null){
                    // アプリイメージのURIを追記
                    sb.append(appOrder.getUri());
                }
                if(optOrder.getUri() != null){
                    // オプションイメージのURIを追記
                    sb.append("|");
                    sb.append(optOrder.getUri());
                }
            }else{
                // アプリもオプションもない場合はnullという文字列を返す
                sb.append("null");
            }
        }
        return sb.toString();
    }
}
