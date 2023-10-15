/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.hid4java.examples;

import java.nio.file.Files;
import java.nio.file.Paths;

import bsh.commands.dir;
import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesSpecification;
import org.hid4java.jna.HidDeviceStructure;
import org.hid4java.jna.WideStringBuffer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import vavi.awt.joystick.hid4java.HidApiLibraryEX;
import vavi.hid.parser.HidParser;
import vavi.usb.UsbUtil;
import vavi.beans.BeanUtil;
import vavi.util.Debug;
import vavi.util.StringUtil;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


/**
 * HidApiLibraryEXTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2023-09-20 nsano initial version <br>
 */
@EnabledIf("localPropertiesExists")
@PropsEntity(url = "file:local.properties")
public class HidApiLibraryEXTest {

    static {
        // use brew but not in hid4java
        System.setProperty("jna.library.path", "/opt/homebrew/lib");
    }

    static boolean localPropertiesExists() {
        return Files.exists(Paths.get("local.properties"));
    }

    @Property(name = "mid")
    String mid;
    @Property(name = "pid")
    String pid;

    int vendorId;
    int productId;

    @BeforeEach
    void setup() throws Exception {
        if (localPropertiesExists()) {
            PropsEntity.Util.bind(this);

            vendorId = Integer.decode(mid);
            productId = Integer.decode(pid);
        }
    }

    static HidServices hidServices;

    @BeforeAll
    static void setupAll() {
        HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
        // Use the v0.7.0 manual start feature to get immediate attach events
        hidServicesSpecification.setAutoStart(false);

        // Get HID services using custom specification
        hidServices = HidManager.getHidServices(hidServicesSpecification);

        // Manually start the services to get attachment event
        hidServices.start();
    }

    @AfterAll
    static void teardown() {
        hidServices.stop();
        hidServices.shutdown();
    }

    @Test
    void test1() throws Exception {

        HidDevice device = hidServices.getHidDevice(vendorId, productId, null);

        HidDeviceStructure deviceStructure = (HidDeviceStructure) BeanUtil.getValue("hidDeviceStructure", device);
Debug.println(deviceStructure);

        // Avoid index out of bounds exception
        byte[] d = new byte[HidApiLibraryEX.HID_API_MAX_REPORT_DESCRIPTOR_SIZE];

        WideStringBuffer report = new WideStringBuffer(HidApiLibraryEX.HID_API_MAX_REPORT_DESCRIPTOR_SIZE);
        int r = HidApiLibraryEX.INSTANCE.hid_get_report_descriptor(deviceStructure.ptr(), report, d.length);
        assertNotEquals(-1, r, "hid_get_report_descriptor");

        System.arraycopy(report.buffer, 0, d, 0, Math.min(r, d.length));
Debug.println(device.getManufacturer() + ":" + device.getProduct() + "\n" + StringUtil.getDump(d));
    }

    @Test
    void test2() throws Exception {

        HidDevice device = hidServices.getHidDevice(vendorId, productId, null);

        byte[] d = HidApiLibraryEX.getDescriptor(device);
Debug.println(device.getManufacturer() + ":" + device.getProduct() + "\n" + StringUtil.getDump(d));
    }

    @Test
    void test3() throws Exception {
        HidDevice dualShock4 = hidServices.getHidDevice(vendorId, productId, null);

        byte[] d = HidApiLibraryEX.getDescriptor(dualShock4);

        UsbUtil.dump_report_desc(d, d.length);
    }

    @Test
    void test4() throws Exception {
        HidDevice device = hidServices.getHidDevice(vendorId, productId, null);

        byte[] d = HidApiLibraryEX.getInputDescriptor(device, (byte) 2);

        UsbUtil.dump_report_desc(d, d.length);
    }

    @Test
    void test5() throws Exception {
        HidDevice device = hidServices.getHidDevice(vendorId, productId, null);

        byte[] d = HidApiLibraryEX.getDescriptor(device);

        HidParser parser = new HidParser();
        parser.parse(d, d.length);
        parser.dump();
        parser.dump();
    }
}
