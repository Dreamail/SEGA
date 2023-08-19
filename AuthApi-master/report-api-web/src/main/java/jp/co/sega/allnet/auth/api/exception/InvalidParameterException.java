/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.exception;

/**
 * @author TsuboiY
 * 
 */
@SuppressWarnings("serial")
public class InvalidParameterException extends Exception {

    private static final String MESSAGE_TEMPLATE = "%s is not specified";

    private final String element;

    public InvalidParameterException(String element) {
        super(String.format(MESSAGE_TEMPLATE, element));
        this.element = element;
    }

    /**
     * @return the element
     */
    public String getElement() {
        return element;
    }

}
