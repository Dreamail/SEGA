package jp.co.sega.allnet.auth.service.adminregister.authallowed.place;

public class AuthAllowedPlaceRegisterResult {

    private int status;

    private AuthAllowedPlaceRegisterParameter param;

    public AuthAllowedPlaceRegisterResult(
            AuthAllowedPlaceRegisterParameter param) {
        this.param = param;
    }

    public AuthAllowedPlaceRegisterResult(int status,
            AuthAllowedPlaceRegisterParameter param) {
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
    public AuthAllowedPlaceRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(AuthAllowedPlaceRegisterParameter param) {
        this.param = param;
    }

}
