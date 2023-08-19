package jp.co.sega.allnet.auth.service.adminregister.authdenied.bill;

public class AuthDeniedBillRegisterResult {

    private int status;

    private AuthDeniedBillRegisterParameter param;

    private String message;

    public AuthDeniedBillRegisterResult(AuthDeniedBillRegisterParameter param) {
        this.param = param;
    }

    public AuthDeniedBillRegisterResult(int status,
            AuthDeniedBillRegisterParameter param, String message) {
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
    public AuthDeniedBillRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(AuthDeniedBillRegisterParameter param) {
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
