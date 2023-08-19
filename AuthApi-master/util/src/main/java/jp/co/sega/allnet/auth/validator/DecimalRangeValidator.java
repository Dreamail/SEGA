/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.validator;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jp.co.sega.allnet.auth.annotation.DecimalRange;

import org.apache.commons.lang3.StringUtils;

/**
 * @author TsuboiY
 * 
 */
public class DecimalRangeValidator implements
        ConstraintValidator<DecimalRange, Object> {

    private BigDecimal _min;

    private BigDecimal _max;

    private boolean _required;

    @Override
    public void initialize(DecimalRange decimalRange) {
        if (decimalRange.min() != null) {
            _min = new BigDecimal(decimalRange.min());
        }
        if (decimalRange.max() != null) {
            _max = new BigDecimal(decimalRange.max());
        }
        _required = decimalRange.required();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof String) {
            if (StringUtils.isEmpty((String) value)) {
                return !_required;
            }
        } else {
            if (value == null) {
                return !_required;
            }
        }

        try {
            BigDecimal dec = new BigDecimal(value.toString());
            if (_min != null && dec.compareTo(_min) < 0) {
                return false;
            }
            if (_max != null && dec.compareTo(_max) > 0) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
