package jp.co.sega.allnet.auth.service.adminregister.authallowed.comp;

public class AuthAllowedCompRegisterResult {

    private int status;

    private AuthAllowedCompRegisterParameter param;

    private String message;

    public AuthAllowedCompRegisterResult(AuthAllowedCompRegisterParameter param) {
        this.param = param;
    }

    public AuthAllowedCompRegisterResult(int status,
            AuthAllowedCompRegisterParameter param, String message) {
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
    public AuthAllowedCompRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(AuthAllowedCompRegisterParameter param) {
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
