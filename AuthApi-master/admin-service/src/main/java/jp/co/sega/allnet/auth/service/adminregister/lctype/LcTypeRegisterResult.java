package jp.co.sega.allnet.auth.service.adminregister.lctype;

public class LcTypeRegisterResult {
    private int status;

    private LcTypeRegisterParameter param;

    public LcTypeRegisterResult(LcTypeRegisterParameter param) {
        this.param = param;
    }

    public LcTypeRegisterResult(int status, LcTypeRegisterParameter param) {
        this.status = status;
        this.param = param;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the param
     */
    public LcTypeRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(LcTypeRegisterParameter param) {
        this.param = param;
    }

}
