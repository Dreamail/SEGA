/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import jp.co.sega.allnet.auth.exception.ApplicationException;

import org.apache.commons.codec.binary.Base64;

/**
 * @author TsuboiY
 * 
 */
public class ObjectSerializeUtils {

    private static ObjectSerializeUtils _instance;

    private ObjectSerializeUtils() {
    }

    /**
     * オブジェクトをシリアライズしてBASE64文字列を返却する。
     * 
     * @param serializable
     * @return
     */
    public static String serialize(Serializable serializable) {
        return serialize(serializable, true);
    }

    /**
     * オブジェクトをシリアライズしてBASE64文字列を返却する。
     * 
     * @param serializable
     * @return
     */
    public static String serialize(Serializable serializable, boolean compress) {
        if (_instance == null) {
            _instance = new ObjectSerializeUtils();
        }
        if (compress) {
            return _instance.serializeGzip(serializable);
        }
        return _instance.serializePlain(serializable);
    }

    /**
     * BASE64文字列からオブジェクトに戻す。
     * 
     * @param base64
     * @param clazz
     * @return
     */
    public static <T> T deserialize(String base64, Class<T> clazz) {
        return deserialize(base64, clazz, true);
    }

    /**
     * BASE64文字列からオブジェクトに戻す。
     * 
     * @param base64
     * @param clazz
     * @return
     */
    public static <T> T deserialize(String base64, Class<T> clazz,
            boolean compressed) {
        if (_instance == null) {
            _instance = new ObjectSerializeUtils();
        }
        if (compressed) {
            return _instance.deserializeCompressed(base64, clazz);
        }
        return _instance.deserializePlain(base64, clazz);
    }

    /**
     * オブジェクトをシリアライズしたものをBASE64文字列として返却する。
     * 
     * @param serializable
     * @return
     */
    private String serializePlain(Serializable serializable) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(serializable);
            return Base64.encodeBase64String(baos.toByteArray());
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * オブジェクトをシリアライズ＆GZIP圧縮したものをBASE64文字列を返却する。
     * 
     * @param serializable
     * @return
     */
    private String serializeGzip(Serializable serializable) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzos = new GZIPOutputStream(baos);
            ObjectOutputStream oos = new ObjectOutputStream(gzos);
            oos.writeObject(serializable);
            gzos.finish();
            gzos.close();
            return Base64.encodeBase64String(baos.toByteArray());
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * BASE64文字列からオブジェクトに戻す。
     * 
     * @param base64
     * @param clazz
     * @return
     */
    private <T> T deserializePlain(String base64, Class<T> clazz) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(Base64.decodeBase64(base64)));
            return clazz.cast(ois.readObject());
        } catch (IOException e) {
            throw new ApplicationException(e);
        } catch (ClassNotFoundException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * GZIP圧縮されたBASE64文字列からオブジェクトに戻す。
     * 
     * @param base64
     * @param clazz
     * @return
     */
    private <T> T deserializeCompressed(String base64, Class<T> clazz) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(
                    new ByteArrayInputStream(Base64.decodeBase64(base64))));
            return clazz.cast(ois.readObject());
        } catch (IOException e) {
            throw new ApplicationException(e);
        } catch (ClassNotFoundException e) {
            throw new ApplicationException(e);
        }
    }

}
