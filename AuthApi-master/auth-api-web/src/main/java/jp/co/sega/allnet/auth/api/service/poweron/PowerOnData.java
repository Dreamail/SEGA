/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.poweron;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author TsuboiY
 * 
 */
public class PowerOnData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自動認証を許可する認証方式
     */
    private static final int[] AUTO_AUTH_ALLOWED_GAME_AUTH = new int[] { 2, 3,
            4, 5 };

    /**
     * 移設を許可する認証方式
     */
    private static final int[] MOVE_ALLOWED_GAME_AUTH = new int[] { 4, 5 };

    /**
     * デバッグ用認証方式
     */
    private static final int DEBUG_GAME_AUTH = -1;

    /**
     * 通信が許可される通信セッティング
     */
    private static final int COMMUNICATION_ALLOWED_SETTING = 1;

    private Router router;
    private Game game;
    private Machine machine;
    private Status status;

    /**
     * コンストラクタ
     * 
     * @param router
     * @param game
     * @param machine
     */
    public PowerOnData(Router router, Game game, Machine machine, Status status) {
        this.router = router;
        this.game = game;
        this.machine = machine;
        this.status = status;
    }

    /**
     * 基板情報に基板シリアルが存在するかどうか
     * 
     * @return
     */
    public boolean isExistSerial() {
        if (machine.getSerial() == null) {
            return false;
        }
        return true;
    }

    /**
     * ゲーム情報の認証方式が自動認証を許可する方式か
     * 
     * @return
     */
    public boolean isAutoAuth() {
        for (int i : AUTO_AUTH_ALLOWED_GAME_AUTH) {
            if (i == game.auth) {
                return true;
            }
        }
        return false;
    }

    /**
     * ゲーム情報の認証方式が移設を許可しない方式か
     * 
     * @return
     */
    public boolean isNotMove() {
        for (int i : MOVE_ALLOWED_GAME_AUTH) {
            if (i == game.auth) {
                return false;
            }
        }
        return true;
    }

    /**
     * ルータ情報から紐づく店舗と基板情報の店舗は同じか
     * 
     * @return
     */
    public boolean isSamePlace() {
        if (router.getRouterId() == null || router.allnetId == null) {
            if (machine.allnetId == null) {
                return true;
            }
            return false;
        }
        if (router.allnetId.equals(machine.allnetId)) {
            return true;
        }
        return false;
    }

    /**
     * 基板情報に店舗が存在するか
     * 
     * @return
     */
    public boolean isExistMachinePlace() {
        if (machine.allnetId == null) {
            return false;
        }
        return true;
    }

    /**
     * ゲーム情報の認証方式がデバッグはないか
     * 
     * @return
     */
    public boolean isNotDebugAuth() {
        if (game.auth == DEBUG_GAME_AUTH) {
            return false;
        }
        return true;
    }

    /**
     * 通信セッティングが通信不許可かどうか
     * 
     * @return
     */
    public boolean isNotAllowedCommSetting() {
        if (machine.setting == COMMUNICATION_ALLOWED_SETTING) {
            return false;
        }
        return true;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static class Router implements Serializable {
        private static final long serialVersionUID = 1L;

        private String routerId;
        private BigDecimal allnetId;
        private String placeId;
        private String placeName;
        private String billCode;
        private String placeNickName;
        private int region0Id;
        private String countryCode;
        private String region0Name;
        private String region1Name;
        private String region2Name;
        private String region3Name;
        private String timezone;

        public String getRouterId() {
            return routerId;
        }

        public void setRouterId(String routerId) {
            this.routerId = routerId;
        }

        public BigDecimal getAllnetId() {
            return allnetId;
        }

        public void setAllnetId(BigDecimal allnetId) {
            this.allnetId = allnetId;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }

        public String getBillCode() {
            return billCode;
        }

        public void setBillCode(String billCode) {
            this.billCode = billCode;
        }

        public String getPlaceNickName() {
            return placeNickName;
        }

        public void setPlaceNickName(String placeNickName) {
            this.placeNickName = placeNickName;
        }

        public int getRegion0Id() {
            return region0Id;
        }

        public void setRegion0Id(int region0Id) {
            this.region0Id = region0Id;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getRegion0Name() {
            return region0Name;
        }

        public void setRegion0Name(String region0Name) {
            this.region0Name = region0Name;
        }

        public String getRegion1Name() {
            return region1Name;
        }

        public void setRegion1Name(String region1Name) {
            this.region1Name = region1Name;
        }

        public String getRegion2Name() {
            return region2Name;
        }

        public void setRegion2Name(String region2Name) {
            this.region2Name = region2Name;
        }

        public String getRegion3Name() {
            return region3Name;
        }

        public void setRegion3Name(String region3Name) {
            this.region3Name = region3Name;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

    }

    public static class Game implements Serializable {
        private static final long serialVersionUID = 1L;

        private String gameId;
        private String gameVer;
        private int auth;
        private String uri;
        private String host;

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public String getGameVer() {
            return gameVer;
        }

        public void setGameVer(String gameVer) {
            this.gameVer = gameVer;
        }

        public int getAuth() {
            return auth;
        }

        public void setAuth(int auth) {
            this.auth = auth;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

    }

    public static class Machine implements Serializable {
        private static final long serialVersionUID = 1L;

        private String serial;
        private String gameId;
        private String reservedGameId;
        private int setting;
        private BigDecimal allnetId;
        private String placeId;
        private String placeName;
        private String billCode;
        private String placeNickName;
        private Integer region0Id;
        private String countryCode;
        private String region0Name;
        private String region1Name;
        private String region2Name;
        private String region3Name;
        private String timezone;

        private boolean existStatus;

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public String getReservedGameId() {
            return reservedGameId;
        }

        public void setReservedGameId(String reservedGameId) {
            this.reservedGameId = reservedGameId;
        }

        public int getSetting() {
            return setting;
        }

        public void setSetting(int setting) {
            this.setting = setting;
        }

        public BigDecimal getAllnetId() {
            return allnetId;
        }

        public void setAllnetId(BigDecimal allnetId) {
            this.allnetId = allnetId;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }

        public String getBillCode() {
            return billCode;
        }

        public void setBillCode(String billCode) {
            this.billCode = billCode;
        }

        public String getPlaceNickName() {
            return placeNickName;
        }

        public void setPlaceNickName(String placeNickName) {
            this.placeNickName = placeNickName;
        }

        public Integer getRegion0Id() {
            return region0Id;
        }

        public void setRegion0Id(Integer region0Id) {
            this.region0Id = region0Id;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getRegion0Name() {
            return region0Name;
        }

        public void setRegion0Name(String region0Name) {
            this.region0Name = region0Name;
        }

        public String getRegion1Name() {
            return region1Name;
        }

        public void setRegion1Name(String region1Name) {
            this.region1Name = region1Name;
        }

        public String getRegion2Name() {
            return region2Name;
        }

        public void setRegion2Name(String region2Name) {
            this.region2Name = region2Name;
        }

        public String getRegion3Name() {
            return region3Name;
        }

        public void setRegion3Name(String region3Name) {
            this.region3Name = region3Name;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public boolean isExistStatus() {
            return existStatus;
        }

        public void setExistStatus(boolean existStatus) {
            this.existStatus = existStatus;
        }

    }

    public static class Status implements Serializable {

        private static final long serialVersionUID = 1L;

        private int stat = 1;

        private int cause = 1;

        public int getStat() {
            return stat;
        }

        public void setStat(int stat) {
            this.stat = stat;
        }

        public int getCause() {
            return cause;
        }

        public void setCause(int cause) {
            this.cause = cause;
        }

        @Override
        public String toString() {
            return "Status [stat=" + stat + ", cause=" + cause + "]";
        }
    }

}
