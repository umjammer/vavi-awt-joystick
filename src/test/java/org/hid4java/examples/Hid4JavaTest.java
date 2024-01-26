/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.hid4java.examples;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import vavi.hid.parser.Collection;
import vavi.hid.parser.Field;
import vavi.hid.parser.HidParser;
import vavi.usb.UsbUtil;
import vavi.util.Debug;
import vavi.util.StringUtil;
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
public class Hid4JavaTest {

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
    @DisplayName("hex dump REPORT descriptor")
    void test2() throws Exception {

        HidDevice device = hidServices.getHidDevice(vendorId, productId, null);

        byte[] d = new byte[4096];
        int r = device.getReportDescriptor(d);
Debug.println("r: " + r);
Debug.println(device.getManufacturer() + ":" + device.getProduct() + "\n" + StringUtil.getDump(d, r));
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

    @Test
    @DisplayName("dump REPORT descriptor by HidParser")
    void test5() throws Exception {
        HidDevice device = hidServices.getHidDevice(vendorId, productId, null);

        byte[] d = new byte[4096];
        int r = device.getReportDescriptor(d);
Debug.println("r: " + r);
//OutputStream os = Files.newOutputStream(Path.of("src/test/resources/ds4_ir_desc.dat"));
//os.write(d, 0, r);
//os.flush();
//os.close();

        HidParser parser = new HidParser();
        parser.parse(d, r);
        parser.dump();
    }

    @Test
    @DisplayName("try to use parse result")
    void test6() throws Exception {
        HidDevice device = hidServices.getHidDevice(vendorId, productId, null);

        byte[] d = new byte[4096];
        int r = device.getReportDescriptor(d);
Debug.println("r: " + r);

        HidParser parser = new HidParser();
        Collection root = parser.parse(d, r);
Debug.println("root: " + root + ", " + root.getUsagePair());
        Collection child = root.getChildren().get(0);
Debug.println("child: " + child + ", " + child.getUsagePair());
    }

    @Test
    @DisplayName("write: led, rumbling")
    void test7() throws Exception {
        HidDevice device = hidServices.getHidDevice(vendorId, productId, null);

        byte[] d = new byte[31];
        d[0] = (byte) 255;
        int offset = 0;
        int reportId = 0x05;

        int smallRumble = 255;
        int bigRumble = 0;
        int ledRed = 0;
        int ledGreen = 0;
        int ledBlue = 0;
        int flashLed1 = 0;
        int flashLed2 = 0;

        // Rumble
        d[offset + 3] = (byte) smallRumble;
        d[offset + 4] = (byte) bigRumble;

        // LED (red, green, blue)
        d[offset + 5] = (byte) ledRed;
        d[offset + 6] = (byte) ledGreen;
        d[offset + 7] = (byte) ledBlue;

        // Time to flash bright (255 = 2.5 seconds)
        d[offset + 8] = (byte) flashLed1;

        // Time to flash dark (255 = 2.5 seconds)
        d[offset + 9] = (byte) flashLed2;

        int r = device.write(d, d.length, reportId);
Debug.println("r: " + r);
    }

    @Test
    void test8() throws Exception {
        HidDevice device = hidServices.getHidDevice(vendorId, productId, null);

        byte[] d = new byte[4096];
        int r = device.getReportDescriptor(d);
Debug.println("r: " + r);

        HidParser parser = new HidParser();
        Collection root = parser.parse(d, r);
        List<Field> fields = root.enumerateFields();
fields.forEach(System.out::println);
    }
}
