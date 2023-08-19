package jp.co.sega.allnet.auth.service.adminregister.routertype;

public class RouterTypeRegisterResult {
    private int status;

    private RouterTypeRegisterParameter param;

    public RouterTypeRegisterResult(RouterTypeRegisterParameter param) {
        this.param = param;
    }

    public RouterTypeRegisterResult(int status,
            RouterTypeRegisterParameter param) {
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
    public RouterTypeRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(RouterTypeRegisterParameter param) {
        this.param = param;
    }

}
