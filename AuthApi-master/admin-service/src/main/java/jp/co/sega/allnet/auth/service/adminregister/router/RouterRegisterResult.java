package jp.co.sega.allnet.auth.service.adminregister.router;

import org.apache.commons.lang3.StringUtils;

public class RouterRegisterResult {
    private boolean success;

    private RouterRegisterParameter param;

    private String placeId;

    private String oldPlaceId;

    private boolean exist;

    public RouterRegisterResult(boolean success, RouterRegisterParameter param,
            boolean exist) {
        this(success, param, StringUtils.EMPTY, StringUtils.EMPTY, exist);
    }

    public RouterRegisterResult(boolean success, RouterRegisterParameter param,
            String placeId, String oldPlaceId, boolean exist) {
        this.success = success;
        this.param = param;
        this.placeId = placeId;
        this.oldPlaceId = oldPlaceId;
        this.exist = exist;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success
     *            the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return the param
     */
    public RouterRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(RouterRegisterParameter param) {
        this.param = param;
    }

    /**
     * @return the placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * @param placeId
     *            the placeId to set
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * @return the oldPlaceId
     */
    public String getOldPlaceId() {
        return oldPlaceId;
    }

    /**
     * @param oldPlaceId
     *            the oldPlaceId to set
     */
    public void setOldPlaceId(String oldPlaceId) {
        this.oldPlaceId = oldPlaceId;
    }

    /**
     * @return the exist
     */
    public boolean isExist() {
        return exist;
    }

    /**
     * @param exist
     *            the exist to set
     */
    public void setExist(boolean exist) {
        this.exist = exist;
    }

}
