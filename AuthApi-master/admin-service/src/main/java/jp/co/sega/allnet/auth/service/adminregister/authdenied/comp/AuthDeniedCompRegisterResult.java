package jp.co.sega.allnet.auth.service.adminregister.authdenied.comp;

public class AuthDeniedCompRegisterResult {

    private int status;

    private AuthDeniedCompRegisterParameter param;

    private String message;

    public AuthDeniedCompRegisterResult(AuthDeniedCompRegisterParameter param) {
        this.param = param;
    }

    public AuthDeniedCompRegisterResult(int status,
            AuthDeniedCompRegisterParameter param, String message) {
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
    public AuthDeniedCompRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(AuthDeniedCompRegisterParameter param) {
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
