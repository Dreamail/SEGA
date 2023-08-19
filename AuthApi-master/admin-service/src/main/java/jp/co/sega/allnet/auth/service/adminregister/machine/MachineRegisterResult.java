package jp.co.sega.allnet.auth.service.adminregister.machine;

import jp.co.sega.allnet.auth.common.entity.Machine;

public class MachineRegisterResult {

    public static final int RESULT_OK = 1;

    public static final int RESULT_NG_DIFFERENT_GAME_ID = -1;

    public static final int RESULT_NG_NOT_REGISTER_DATA = -2;
    
    public static final int RESULT_NG_DELETE_NO_TARGET = -3;

    private int success;

    private MachineRegisterParameter param;

    private String placeId;

    private String placeName;

    private String oldPlaceId;

    private String oldPlaceName;

    private Integer status;

    private Machine machine;

    public MachineRegisterResult(int success, MachineRegisterParameter param,
            String placeId, String placeName, Integer status) {
        this.success = success;
        this.param = param;
        this.placeId = placeId;
        this.placeName = placeName;
        this.status = status;
    }

    public boolean isSuccess() {
        if (success > 0) {
            return true;
        }
        return false;
    }

    public int getResult() {
        return success;
    }

    /**
     * @return the success
     */
    public int getSuccess() {
        return success;
    }

    /**
     * @param success
     *            the success to set
     */
    public void setSuccess(int success) {
        this.success = success;
    }

    /**
     * @return the param
     */
    public MachineRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(MachineRegisterParameter param) {
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
     * @return the placeName
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * @param placeName
     *            the placeName to set
     */
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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
     * @return the oldPlaceName
     */
    public String getOldPlaceName() {
        return oldPlaceName;
    }

    /**
     * @param oldPlaceName
     *            the oldPlaceName to set
     */
    public void setOldPlaceName(String oldPlaceName) {
        this.oldPlaceName = oldPlaceName;
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

    /**
     * @return the machine
     */
    public Machine getMachine() {
        return machine;
    }

    /**
     * @param machine
     *            the machine to set
     */
    public void setMachine(Machine machine) {
        this.machine = machine;
    }

}
