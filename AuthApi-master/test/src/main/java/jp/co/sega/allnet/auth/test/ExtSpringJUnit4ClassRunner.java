/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.test;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author TsuboiY
 * 
 */
public class ExtSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {

    static {
        JulUtils.configureForUnitTest();
    }

    public ExtSpringJUnit4ClassRunner(Class<?> clazz)
            throws InitializationError {
        super(clazz);
    }

}
