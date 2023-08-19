/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.validator;

import java.io.UnsupportedEncodingException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import jp.co.sega.allnet.auth.annotation.StringByteLength;

/**
 * @author TsuboiY
 * 
 */
public class StringByteLengthValidator implements
        ConstraintValidator<StringByteLength, String> {

    private int _min;

    private int _max;

    private String _encoding;

    @Override
    public void initialize(StringByteLength stringByteLength) {
        _min = stringByteLength.min();
        _max = stringByteLength.max();
        _encoding = stringByteLength.encoding();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            int length = value.getBytes(_encoding).length;
            if (length >= _min && length <= _max) {
                return true;
            }
            return false;
        } catch (UnsupportedEncodingException e) {
            throw new ValidationException(e);
        }

    }

}
