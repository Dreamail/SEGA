/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TsuboiY
 * 
 */
public class MailServiceBean implements MailService {

    private static final Logger _log = LoggerFactory
            .getLogger(MailServiceBean.class);

    private Session session;

    @Override
    public void send(String address, String subject, String body) {
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(FROM_MAIL_ADDRESS));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(address));
            // 件名をセット
            msg.setSubject(subject);
            // 本文をセット
            msg.setText(body);
            // メール送信
            Transport.send(msg);
        } catch (AddressException e) {
            _log.error(String.format(
                    "Cannot send mail {address:[%s] subject[%s] body[%s]}",
                    address, subject, body));
        } catch (MessagingException e) {
            _log.error(String.format(
                    "Cannot send mail {address:[%s] subject[%s] body[%s]}",
                    address, subject, body));
        }
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @param session
     *            the session to set
     */
    public void setSession(Session session) {
        this.session = session;
    }
}
