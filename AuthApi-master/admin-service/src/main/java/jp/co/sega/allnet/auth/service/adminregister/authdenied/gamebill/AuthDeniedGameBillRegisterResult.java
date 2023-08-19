package jp.co.sega.allnet.auth.service.adminregister.authdenied.gamebill;

public class AuthDeniedGameBillRegisterResult {

    private int status;

    private AuthDeniedGameBillRegisterParameter param;

    private String message;

    public AuthDeniedGameBillRegisterResult(
            AuthDeniedGameBillRegisterParameter param) {
        this.param = param;
    }

    public AuthDeniedGameBillRegisterResult(int status,
            AuthDeniedGameBillRegisterParameter param, String message) {
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
    public AuthDeniedGameBillRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(AuthDeniedGameBillRegisterParameter param) {
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
