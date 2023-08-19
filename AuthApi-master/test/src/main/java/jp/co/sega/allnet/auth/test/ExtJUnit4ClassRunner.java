/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.test;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * @author TsuboiY
 * 
 */
public class ExtJUnit4ClassRunner extends BlockJUnit4ClassRunner {

    static {
        JulUtils.configureForUnitTest();
    }

    public ExtJUnit4ClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

}
