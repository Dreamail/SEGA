package jp.co.sega.allnet.auth.service.adminregister.gameauth;

import jp.co.sega.allnet.auth.common.entity.GameAttribute;

public class GameAuthRegisterResult {
    private boolean success;

    private GameAuthRegisterParameter param;

    private GameAttribute gameAttr;

    public GameAuthRegisterResult(boolean success, GameAuthRegisterParameter param,
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
    public GameAuthRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(GameAuthRegisterParameter param) {
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
