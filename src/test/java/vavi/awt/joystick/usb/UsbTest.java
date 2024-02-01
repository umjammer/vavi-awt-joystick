/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.usb;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesSpecification;
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

    HidServices hidServices;

    @BeforeEach
    void setup() throws Exception {
        if (localPropertiesExists()) {
            PropsEntity.Util.bind(this);

            vendorId = Integer.decode(mid);
            productId = Integer.decode(pid);
        }

        HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
        // Use the v0.7.0 manual start feature to get immediate attach events
        hidServicesSpecification.setAutoStart(false);
        hidServicesSpecification.setAutoShutdown(false);

        // Get HID services using custom specification
        hidServices = HidManager.getHidServices(hidServicesSpecification);

        // Manually start the services to get attachment event
        hidServices.start();
    }

    @AfterEach
    void teardown() throws Exception {
        hidServices.shutdown();
    }

    @Test
    @DisplayName("dump REPORT descriptor by UsbUtil")
    void test3() throws Exception {
        HidDevice dualShock4 = hidServices.getHidDevice(vendorId, productId, null);

        byte[] d = new byte[4096];
        int r = dualShock4.getReportDescriptor(d);
Debug.println("r: " + r);

        UsbUtil.dump_report_desc(d, r);
    }

    @Test
    @DisplayName("dump input descriptor by UsbUtil")
    void test4() throws Exception {
        HidDevice device = hidServices.getHidDevice(vendorId, productId, null);

        byte[] d = new byte[512];
        int r = device.getInputDescriptor(d, 2);
Debug.println("r: " + r);

        UsbUtil.dump_report_desc(d, r);
    }
}
