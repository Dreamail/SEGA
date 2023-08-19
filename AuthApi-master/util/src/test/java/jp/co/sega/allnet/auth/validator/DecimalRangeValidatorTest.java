/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;

import javax.validation.Payload;

import jp.co.sega.allnet.auth.annotation.DecimalRange;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author TsuboiY
 * 
 */
public class DecimalRangeValidatorTest {

    /**
     * Test method for
     * {@link jp.co.sega.allnet.auth.validator.DecimalRangeValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)}
     * .
     */
    @Test
    public void testIsValid() {
        DecimalRangeValidator validator = new DecimalRangeValidator();
        validator.initialize(createDecimalRange("1.1", "3", true));
        assertTrue(validator.isValid(3, null));
        assertTrue(validator.isValid(1.1, null));
        assertTrue(validator.isValid(new BigDecimal(3), null));
        assertTrue(validator.isValid(new BigDecimal(1.1), null));
        assertTrue(validator.isValid("3", null));
        assertTrue(validator.isValid("1.1", null));
        assertFalse(validator.isValid(3.1, null));
        assertFalse(validator.isValid(1.0, null));
        assertFalse(validator.isValid(new BigDecimal(3.1), null));
        assertFalse(validator.isValid(new BigDecimal(1.0), null));
        assertFalse(validator.isValid("3.000000000000000001", null));
        assertFalse(validator.isValid("1.000000000000000009", null));
        assertFalse(validator.isValid("1.1.1", null));
        assertFalse(validator.isValid("test", null));

        validator.initialize(createDecimalRange(String.valueOf(Long.MIN_VALUE),
                String.valueOf(Long.MAX_VALUE), true));
        assertTrue(validator.isValid(new BigDecimal(Long.MAX_VALUE), null));
        assertTrue(validator.isValid(new BigDecimal(Long.MIN_VALUE), null));
        assertFalse(validator.isValid(
                new BigDecimal(Long.MAX_VALUE).add(new BigDecimal(1)), null));
        assertFalse(validator.isValid(
                new BigDecimal(Long.MIN_VALUE).subtract(new BigDecimal(1)),
                null));

        assertFalse(validator.isValid(null, null));
        assertFalse(validator.isValid(StringUtils.EMPTY, null));

        validator.initialize(createDecimalRange("0", "1", false));

        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid(StringUtils.EMPTY, null));
        assertTrue(validator.isValid("0.1", null));
        assertFalse(validator.isValid("0.1.1", null));
        assertFalse(validator.isValid("1.1", null));
        assertFalse(validator.isValid("test", null));
    }

    private DecimalRange createDecimalRange(final String min, final String max,
            final boolean required) {
        return new DecimalRange() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return null;
            }

            @Override
            public String min() {
                return min;
            }

            @Override
            public String message() {
                return null;
            }

            @Override
            public String max() {
                return max;
            }

            @Override
            public Class<?>[] groups() {
                return null;
            }

            @Override
            public boolean required() {
                return required;
            }
        };
    }
}
