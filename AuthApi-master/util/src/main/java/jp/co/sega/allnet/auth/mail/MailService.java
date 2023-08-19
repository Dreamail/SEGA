/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.mail;

/**
 * @author TsuboiY
 * 
 */
public interface MailService {

    String FROM_MAIL_ADDRESS = "Tsuboi.Yosuke@sega.co.jp";

    /**
     * メールを送信する。
     * 
     * @param address
     * @param subject
     * @param body
     */
    void send(String address, String subject, String body);

}
