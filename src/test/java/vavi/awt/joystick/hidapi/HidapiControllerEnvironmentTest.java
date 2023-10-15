/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.hidapi;

import java.util.Arrays;

import org.junit.jupiter.api.Test;


/**
 * HidapiControllerEnvironmentTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2023-10-03 nsano initial version <br>
 */
class HidapiControllerEnvironmentTest {

    @Test
    void test1() throws Exception {
        HidapiControllerEnvironment environment = new HidapiControllerEnvironment();
        Arrays.stream(environment.getControllers()).forEach(System.err::println);
    }
}