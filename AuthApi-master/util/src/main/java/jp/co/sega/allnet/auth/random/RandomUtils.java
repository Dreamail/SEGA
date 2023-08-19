/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.random;

import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author TsuboiY
 * 
 */
public class RandomUtils {

    /**
     * 指定された桁数のアルファベット+数値で構成されるランダム文字列を返却する。
     * 
     * @param length
     *            桁数
     * @param generatedSet
     *            渡されたSetの要素値と重複した場合、再生成を行う
     * @param retryLimit
     *            重複した場合の再生成回数の限度
     * @return
     */
    public static String generateAlphanumeric(int length,
            Set<String> generatedSet, int retryLimit) {
        if (retryLimit < 0) {
            throw new IllegalArgumentException("retryLimit must be 0+");
        }
        String generated;
        int i = 0;
        do {
            if (i > retryLimit) {
                throw new OverRetryLimitException(retryLimit);
            }
            generated = RandomStringUtils.randomAlphanumeric(length);
            if (generatedSet == null) {
                return generated;
            }
            i++;
        } while (generatedSet.contains(generated));
        return generated;
    }

}
