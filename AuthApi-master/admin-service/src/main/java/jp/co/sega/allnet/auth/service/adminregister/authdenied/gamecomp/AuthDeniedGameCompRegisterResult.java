package jp.co.sega.allnet.auth.service.adminregister.authdenied.gamecomp;

public class AuthDeniedGameCompRegisterResult {

    private int status;

    private AuthDeniedGameCompRegisterParameter param;

    private String message;

    public AuthDeniedGameCompRegisterResult(
            AuthDeniedGameCompRegisterParameter param) {
        this.param = param;
    }

    public AuthDeniedGameCompRegisterResult(int status,
            AuthDeniedGameCompRegisterParameter param, String message) {
        this.status = status;
        this.param = param;
        this.message = message;
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
    public AuthDeniedGameCompRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(AuthDeniedGameCompRegisterParameter param) {
        this.param = param;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
