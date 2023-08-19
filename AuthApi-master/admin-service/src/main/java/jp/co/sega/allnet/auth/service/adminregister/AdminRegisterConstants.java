/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister;

/**
 * @author TsuboiY
 * 
 */
public interface AdminRegisterConstants {

    int STATUS_SUCCESS = 1;

    int STATUS_EXIST_TARGET = 2;

    int STATUS_NO_TARGET = 3;

    int STATUS_ERROR_NO_PLACE = -1;

    int STATUS_ERROR_NO_GAME = -2;

    int STATUS_ERROR_INVALID_PARAMETER = -3;

    int STATUS_ERROR_INVALID_REGISTER_FLAG = -4;

    int STATUS_ERROR_NO_COMP = -6;

    int MASTER_REGISTER_STATUS_REGISTER = 1;

    int MASTER_REGISTER_STATUS_MODIFY = 2;

    int MASTER_REGISTER_RESULT_SUCCESS = 1;

    int MASTER_REGISTER_RESULT_FAIL = 0;

    int N_MASTER_REGISTER_STATUS_SKIP = 0;

    int N_MASTER_REGISTER_STATUS_REGISTER = 1;

    int N_MASTER_REGISTER_STATUS_MODIFY = 2;

    int N_MASTER_REGISTER_STATUS_DELETE = 3;

    int N_MASTER_REGISTER_STATUS_ERROR_COMP_CODE = -1;

    int N_MASTER_REGISTER_STATUS_ERROR_COMP_NAME = -2;

    int N_MASTER_REGISTER_STATUS_ERROR_ROUTER_TYPE_ID = -1;

    int N_MASTER_REGISTER_STATUS_ERROR_ROUTER_TYPE_NAME = -2;

    int N_MASTER_REGISTER_STATUS_ERROR_LC_TYPE_ID = -1;

    int N_MASTER_REGISTER_STATUS_ERROR_LC_TYPE_NAME = -2;

    String REGISTER_FLAG_REG = "1";

    String REGISTER_FLAG_DEL = "0";

    String DELETE_FLAG_DEL = "1";

}
