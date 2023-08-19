/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jp.co.sega.allnet.auth.annotation.CheckNumberValue;

/**
 * @author TsuboiY
 * 
 */
public class CheckNumberValueValidator implements
        ConstraintValidator<CheckNumberValue, Integer> {

    private int _value;

    @Override
    public void initialize(CheckNumberValue checkNumberValue) {
        _value = checkNumberValue.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (_value == value) {
            return false;
        }

        return true;
    }

}
