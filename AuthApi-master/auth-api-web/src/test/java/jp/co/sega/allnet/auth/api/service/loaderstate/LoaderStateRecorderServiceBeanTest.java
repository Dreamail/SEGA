/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.loaderstate;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.LoaderStateRecorderDao;
import jp.co.sega.allnet.auth.api.domain.LoaderStateLog;
import jp.co.sega.allnet.auth.api.util.InvalidParameterException;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author NakanoY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
public class LoaderStateRecorderServiceBeanTest {

    @Resource(name = "loaderStateRecorderService")
    private LoaderStateRecorderService _service;

    @Resource(name = "loaderStateRecorderDao")
    private LoaderStateRecorderDao _dao;

    @After
    public void after() {
        reset(_dao);
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderSuccessRegister()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "10");

        // LoaderStateRecorderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.checkExist(eq(param.getSerial())))
                .andReturn(false);
        _dao.insertLoaderState(anyObject(LoaderStateLog.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_OK,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderSuccessUpdate()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "10");

        // LoaderStateRecorderで使用するメソッドの振る舞いを設定（当該ケースで使用するものだけ）
        EasyMock.expect(_dao.checkExist(eq(param.getSerial()))).andReturn(true);
        _dao.updateLoaderState(anyObject(LoaderStateLog.class));
        // 振る舞いを記憶
        EasyMock.replay(_dao);

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_OK,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgSerialIsNull()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                null, "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgSerialIsEmpty()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgDvdIsNull()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", null, "2", "3", "4", "5", "6", "7", "8", "9",
                "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgDvdIsEmpty()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgNetIsNull()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", null, "3", "4", "5", "6", "7", "8", "9",
                "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgNetIsEmpty()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "", "3", "4", "5", "6", "7", "8", "9", "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgWorkIsNull()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", null, "4", "5", "6", "7", "8", "9",
                "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgWorkIsEmpty()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "", "4", "5", "6", "7", "8", "9", "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgOldNetIsNull()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", null, "5", "6", "7", "8", "9",
                "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgOldNetIsEmpty()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "", "5", "6", "7", "8", "9", "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgDeliverIsNull()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", null, "6", "7", "8", "9",
                "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgDeliverIsEmpty()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "", "6", "7", "8", "9", "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgFTDIsNull()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "5", null, "7", "8", "9",
                "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgFTDIsEmpty()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "5", "", "7", "8", "9", "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgFDLIsNull()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "5", "6", null, "8", "9",
                "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgFDLIsEmpty()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "5", "6", "", "8", "9", "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgLastAuthIsNull()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "5", "6", "7", null, "9",
                "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgLastAuthIsEmpty()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "5", "6", "7", "", "9", "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgLastAuthStateIsNull()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "5", "6", "7", "8", null,
                "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgLastAuthStateIsEmpty()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "5", "6", "7", "8", "", "10");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgDownloadStateIsNull()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                null);

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }

    /**
     * {@link jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderServiceBean#loaderStateRecorder(jp.co.sega.allnet.auth.api.service.loaderstate.LoaderStateRecorderParameter)}
     * のためのテスト・メソッド。
     * 
     * @throws InvalidParameterException
     */
    @Test
    public final void testLoaderStateRecorderArgDownloadStateIsEmpty()
            throws InvalidParameterException {

        LoaderStateRecorderParameter param = new LoaderStateRecorderParameter(
                "AZZZZZZZZZZ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "");

        assertEquals(LoaderStateRecorderService.RESPONSE_STR_NG,
                _service.loaderStateRecorder(param));
    }
}
