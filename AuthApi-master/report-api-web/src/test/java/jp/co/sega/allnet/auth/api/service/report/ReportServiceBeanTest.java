/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.report;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import jp.co.sega.allnet.auth.api.dao.ReportDao;
import jp.co.sega.allnet.auth.api.domain.ReportData;
import jp.co.sega.allnet.auth.api.domain.ReportImage;
import jp.co.sega.allnet.auth.api.exception.ImageKeyInvalidException;
import jp.co.sega.allnet.auth.api.exception.InvalidParameterException;
import jp.co.sega.allnet.auth.test.ExtSpringJUnit4ClassRunner;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author TsuboiY
 * 
 */
@RunWith(ExtSpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ReportServiceBeanTest {

    @Resource
    private ReportService _service;

    @Resource
    private ReportDao _dao;

    @After
    public void after() {
        reset(_dao);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccess() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(Long.MAX_VALUE);
        image.setOt(Long.MAX_VALUE);
        image.setRt(Long.MAX_VALUE);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     */
    @Test
    public void testReportOptSuccess() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(1);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _dao.updateOptDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessWithOpt() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);
        data.setOptimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessWithWdavWdov()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setOptimage(image);

        _dao.updateOptDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     */
    @Test(expected = ImageKeyInvalidException.class)
    public void testReportFailureImageKeyInvalid()
            throws ImageKeyInvalidException, InvalidParameterException {
        ReportData data = new ReportData();

        _service.report(data);
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailSerialIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailSerialIsNull()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial(null);
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessDflIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(new String[0]);
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessDflIsNull()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailOneOfDflIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = ",AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailOneOfDflIsNull()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(new String[] { "SBXX_1.00.00_20130201120000_0.app", null });
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessWflIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(new String[0]);
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessWflIsNull()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailOneOfWflIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = ",SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailOneOfWflIsNull()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(new String[] { "SBXX_0.00", null });
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailTscIsNull() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(null);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");
    
        data.setAppimage(image);
    
        _service.report(data);
    
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailTdscIsNull() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(null);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");
    
        data.setAppimage(image);
    
        _service.report(data);
    
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessAtIsZero() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(0);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");
    
        data.setAppimage(image);
    
        _dao.updateAppDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);
    
        assertEquals(0, data.getAppimage().getAt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessAtIsOverMax()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Calendar at = Calendar.getInstance();
        at.set(9999, 12, 31, 23, 59, 59);
        at.add(Calendar.SECOND, 1);
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTimeInMillis() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

        Calendar c = Calendar.getInstance();
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);

        assertEquals(c.getTimeInMillis(), data.getAppimage().getAt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessAtIsUnderMin()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(-1);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");
    
        data.setAppimage(image);
    
        _dao.updateAppDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);
    
        Calendar c = Calendar.getInstance();
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
    
        assertEquals(c.getTimeInMillis(), data.getAppimage().getAt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessOtIsZero() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(0);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");
    
        data.setAppimage(image);
    
        _dao.updateAppDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);

        assertEquals(0, data.getAppimage().getOt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessOtIsOverMax()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Calendar ot = Calendar.getInstance();
        ot.set(9999, 12, 31, 23, 59, 59);
        ot.add(Calendar.SECOND, 1);
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTimeInMillis() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

        Calendar c = Calendar.getInstance();
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);

        assertEquals(c.getTimeInMillis(), data.getAppimage().getOt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessOtIsUnderMin()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(-1);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");
    
        data.setAppimage(image);
    
        _dao.updateAppDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);
    
        Calendar c = Calendar.getInstance();
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
    
        assertEquals(c.getTimeInMillis(), data.getAppimage().getOt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessRtIsZero() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(0);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");
    
        data.setAppimage(image);
    
        _dao.updateAppDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);

        assertEquals(0, data.getAppimage().getRt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessRtIsOverMax()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Calendar rt = Calendar.getInstance();
        rt.set(9999, 12, 31, 23, 59, 59);
        rt.add(Calendar.SECOND, 1);

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTimeInMillis() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");
    
        data.setAppimage(image);
    
        _dao.updateAppDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);
    
        Calendar c = Calendar.getInstance();
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
    
        assertEquals(c.getTimeInMillis(), data.getAppimage().getRt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessRtIsUnderMin()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(-1);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");
    
        data.setAppimage(image);
    
        _dao.updateAppDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);
    
        Calendar c = Calendar.getInstance();
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
    
        assertEquals(c.getTimeInMillis(), data.getAppimage().getRt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailRfStateIsNull()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(null);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");
    
        data.setAppimage(image);
    
        _service.report(data);
    
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailGdIsNull() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd(null);
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessGdIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailDavIsNull() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav(null);
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessDavIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailWdavIsNull() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav(null);
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessWdavIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailDovIsNull() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov(null);
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessDovIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailWdovIsNull() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov(null);

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportAppSuccessWdovIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _dao.updateAppDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailAsIsUnder() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(0);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportAppFailAsIsOver() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(3);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setWdav("1.01.00");
        image.setDov("0001.01.01");
        image.setWdov("0001.01.01");

        data.setAppimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailSerialIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailSerialIsNull()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial(null);
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessDflIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(new String[0]);
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _dao.updateOptDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessDflIsNull()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _dao.updateOptDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailOneOfDflIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = ",AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailOneOfDflIsNull()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(new String[] { "SBXX_1.00.00_20130201120000_0.app", null });
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessWflIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(new String[0]);
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _dao.updateOptDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessWflIsNull()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _dao.updateOptDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailOneOfWflIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = ",SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailOneOfWflIsNull()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(new String[] { "SBXX_0.00", null });
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailTscIsNull() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(null);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");
    
        data.setOptimage(image);
    
        _service.report(data);
    
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailTdscIsNull() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(null);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");
    
        data.setOptimage(image);
    
        _service.report(data);
    
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessAtIsZero() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(0);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");
    
        data.setOptimage(image);
    
        _dao.updateOptDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);

        assertEquals(0, data.getOptimage().getAt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessAtIsOverMax()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Calendar at = Calendar.getInstance();
        at.set(9999, 12, 31, 23, 59, 59);
        at.add(Calendar.SECOND, 1);
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTimeInMillis() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");
    
        data.setOptimage(image);
    
        _dao.updateOptDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);
    
        Calendar c = Calendar.getInstance();
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
    
        assertEquals(c.getTimeInMillis(), data.getOptimage().getAt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessAtIsUnderMin()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(-1);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");
    
        data.setOptimage(image);
    
        _dao.updateOptDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);
    
        Calendar c = Calendar.getInstance();
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
    
        assertEquals(c.getTimeInMillis(), data.getOptimage().getAt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessOtIsZero() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(0);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");
    
        data.setOptimage(image);
    
        _dao.updateOptDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);

        assertEquals(0, data.getOptimage().getOt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessOtIsOverMax()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Calendar ot = Calendar.getInstance();
        ot.set(9999, 12, 31, 23, 59, 59);
        ot.add(Calendar.SECOND, 1);
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTimeInMillis() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");
    
        data.setOptimage(image);
    
        _dao.updateOptDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);
    
        Calendar c = Calendar.getInstance();
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
    
        assertEquals(c.getTimeInMillis(), data.getOptimage().getOt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessOtIsUnderMin()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(-1);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");
    
        data.setOptimage(image);
    
        _dao.updateOptDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);
    
        Calendar c = Calendar.getInstance();
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
    
        assertEquals(c.getTimeInMillis(), data.getOptimage().getOt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessRtIsZero() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(0);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");
    
        data.setOptimage(image);
    
        _dao.updateOptDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);

        assertEquals(0, data.getOptimage().getRt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessRtIsOverMax()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Calendar rt = Calendar.getInstance();
        rt.set(9999, 12, 31, 23, 59, 59);
        rt.add(Calendar.SECOND, 1);
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTimeInMillis() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");
    
        data.setOptimage(image);
    
        _dao.updateOptDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);
    
        Calendar c = Calendar.getInstance();
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
    
        assertEquals(c.getTimeInMillis(), data.getOptimage().getRt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessRtIsUnderMin()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(-1);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");
    
        data.setOptimage(image);
    
        _dao.updateOptDeliverReport(eq(image));
    
        EasyMock.replay(_dao);
    
        _service.report(data);
    
        Calendar c = Calendar.getInstance();
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
    
        assertEquals(c.getTimeInMillis(), data.getOptimage().getRt());
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailRfStateIsNull()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();
    
        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";
    
        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");
    
        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(null);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");
    
        data.setOptimage(image);
    
        _service.report(data);
    
    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailGdIsNull() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd(null);
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessGdIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _dao.updateOptDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailDavIsNull() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav(null);
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessDavIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _dao.updateOptDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailDovIsNull() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov(null);

        data.setOptimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test
    public void testReportOptSuccessDovIsEmpty()
            throws ImageKeyInvalidException, InvalidParameterException,
            ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(2);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("");

        data.setOptimage(image);

        _dao.updateOptDeliverReport(eq(image));

        EasyMock.replay(_dao);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailAsIsUnder() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(0);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _service.report(data);

    }

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.api.service.report.ReportServiceBean#report(jp.co.sega.allnet.auth.api.domain.ReportData)}
     * .
     * 
     * @throws InvalidParameterException
     * @throws ParseException
     */
    @Test(expected = InvalidParameterException.class)
    public void testReportOptFailAsIsOver() throws ImageKeyInvalidException,
            InvalidParameterException, ParseException {
        ReportData data = new ReportData();

        String dflstr = "SBXX_1.00.00_20130201120000_0.app,AAX_0001.01.01_20130201120000_0.pack";
        String wflstr = "SBXX_1.01.00_20130301120000_1.00.00_1.app";

        Date at = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130513153321");
        Date ot = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20130501091309");
        Date rt = new SimpleDateFormat("yyyyMMddHHmmss")
                .parse("20141231235959");

        ReportImage image = new ReportImage();
        image.setSerial("AZZZZZZZZZZ");
        image.setDfl(dflstr.split(","));
        image.setWfl(wflstr.split(","));
        image.setTsc(1000);
        image.setTdsc(900);
        image.setAt(at.getTime() / 1000);
        image.setOt(ot.getTime() / 1000);
        image.setRt(rt.getTime() / 1000);
        image.setAs(3);
        image.setRfState(3);
        image.setGd("2013年4月アップデート");
        image.setDav("1.00.00");
        image.setDov("0001.01.01");

        data.setOptimage(image);

        _service.report(data);

    }

}
