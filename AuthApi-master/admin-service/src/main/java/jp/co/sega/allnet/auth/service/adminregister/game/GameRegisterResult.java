package jp.co.sega.allnet.auth.service.adminregister.game;

import jp.co.sega.allnet.auth.common.entity.GameAttribute;

public class GameRegisterResult {
    private boolean success;

    private GameRegisterParameter param;

    private GameAttribute gameAttr;

    public GameRegisterResult(boolean success, GameRegisterParameter param,
            GameAttribute gameAttr) {
        this.success = success;
        this.param = param;
        this.gameAttr = gameAttr;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success
     *            the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return the param
     */
    public GameRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(GameRegisterParameter param) {
        this.param = param;
    }

    /**
     * @return the gameAttr
     */
    public GameAttribute getGameAttr() {
        return gameAttr;
    }

    /**
     * @param gameAttr
     *            the gameAttr to set
     */
    public void setGameAttr(GameAttribute gameAttr) {
        this.gameAttr = gameAttr;
    }

}
