package jp.co.sega.allnet.auth.service.adminregister.order;

public class DownloadOrderRegisterResult {

    private String serial;

    private String gameId;

    private String gameVer;

    private String uri;

    private String message;

    private Integer status;

    public DownloadOrderRegisterResult(DownloadOrderRegisterParameter param) {
        this.serial = param.getSerial();
        this.gameId = param.getGameId();
        this.gameVer = param.getGameVer();
        this.uri = param.getUri();
    }

    public DownloadOrderRegisterResult(DownloadOrderRegisterParameter param,
            String message, Integer status) {
        this(param);
        this.message = message;
        this.status = status;
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial
     *            the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return the gameId
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * @param gameId
     *            the gameId to set
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * @return the gameVer
     */
    public String getGameVer() {
        return gameVer;
    }

    /**
     * @param gameVer
     *            the gameVer to set
     */
    public void setGameVer(String gameVer) {
        this.gameVer = gameVer;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     *            the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
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

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

}
