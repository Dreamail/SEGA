package jp.co.sega.allnet.auth.service.adminregister.comp;

public class CompRegisterResult {
    private int status;

    private CompRegisterParameter param;

    public CompRegisterResult(CompRegisterParameter param) {
        this.param = param;
    }

    public CompRegisterResult(int status, CompRegisterParameter param) {
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
    public CompRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(CompRegisterParameter param) {
        this.param = param;
    }

}
