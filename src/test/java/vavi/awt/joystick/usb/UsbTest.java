/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.usb;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.hid4java.HidDevice;
import org.hid4java.HidDevices;
import org.hid4java.HidSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import vavi.usb.UsbUtil;
import vavi.util.Debug;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


/**
 * Hid4JavaTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2023-09-20 nsano initial version <br>
 */
@EnabledIf("localPropertiesExists")
@PropsEntity(url = "file:local.properties")
public class UsbTest {

    static boolean localPropertiesExists() {
        return Files.exists(Paths.get("local.properties"));
    }

    @Property(name = "mid")
    String mid;
    @Property(name = "pid")
    String pid;

    int vendorId;
    int productId;

    HidDevices hidDevices;

    @BeforeEach
    void setup() throws Exception {
        if (localPropertiesExists()) {
            PropsEntity.Util.bind(this);

            vendorId = Integer.decode(mid);
            productId = Integer.decode(pid);
        }

        HidSpecification hidServicesSpecification = new HidSpecification();
        // Use the v0.7.0 manual start feature to get immediate attach events
        hidServicesSpecification.setAutoStart(false);
        hidServicesSpecification.setAutoShutdown(false);

        // Get HID services using custom specification
        hidDevices = new HidDevices(hidServicesSpecification);

        // Manually start the services to get attachment event
        hidDevices.start();
    }

    @AfterEach
    void teardown() throws Exception {
        hidDevices.shutdown();
    }

    @Test
    @DisplayName("dump REPORT descriptor by UsbUtil")
    void test3() throws Exception {
        HidDevice dualShock4 = hidDevices.getHidDevice(vendorId, productId, null);

        byte[] d = new byte[4096];
        int r = dualShock4.getReportDescriptor(d);
Debug.println("r: " + r);

        UsbUtil.dump_report_desc(d, r);
    }

    @Test
    @DisplayName("dump input descriptor by UsbUtil")
    void test4() throws Exception {
        HidDevice device = hidDevices.getHidDevice(vendorId, productId, null);

        byte[] d = new byte[512];
        int r = device.getInputDescriptor(d, 2);
Debug.println("r: " + r);

        UsbUtil.dump_report_desc(d, r);
    }
}
