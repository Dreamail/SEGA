package jp.co.sega.allnet.auth.service.adminregister.place;

import jp.co.sega.allnet.auth.common.entity.Place;

public class PlaceRegisterResult {
    private boolean success;

    private Flag flag;

    private PlaceRegisterParameter param;

    private Place place;

    public PlaceRegisterResult(boolean success, Flag flag,
            PlaceRegisterParameter param, Place place) {
        this.success = success;
        this.flag = flag;
        this.param = param;
        this.place = place;
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
     * @return the flag
     */
    public Flag getFlag() {
        return flag;
    }

    /**
     * @param flag
     *            the flag to set
     */
    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    /**
     * @return the param
     */
    public PlaceRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(PlaceRegisterParameter param) {
        this.param = param;
    }

    /**
     * @return the place
     */
    public Place getPlace() {
        return place;
    }

    /**
     * @param place
     *            the place to set
     */
    public void setPlace(Place place) {
        this.place = place;
    }

    public enum Flag {
        NOTHING, REGISTER, MODIFY
    }

}
