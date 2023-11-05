/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2020 Gary Rowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.hid4java.examples;

import java.io.IOException;

import net.java.games.input.osx.plugin.DualShock4Plugin;
import net.java.games.input.usb.UsagePage;
import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesSpecification;
import vavi.util.Debug;


/**
 * Demonstrate the USB HID interface using a Satoshi Labs Trezor
 *
 * @since 0.0.1
 */
public class UsbHidEnumerationExample extends BaseExample {

    // hid4java api sample
    public static void main(String[] args) throws Exception {
        UsbHidEnumerationExample example = new UsbHidEnumerationExample();
        example.executeExample();
    }

    private void executeExample() throws IOException {

        printPlatform();

        // Configure to use custom specification
        HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
        // Use the v0.7.0 manual start feature to get immediate attach events
        hidServicesSpecification.setAutoStart(false);
        hidServicesSpecification.setAutoShutdown(false);

        // Get HID services using custom specification
        HidServices hidServices = HidManager.getHidServices(hidServicesSpecification);
        hidServices.addHidServicesListener(this);

        // Manually start the services to get attachment event
        System.out.println(ANSI_GREEN + "Manually starting HID services." + ANSI_RESET);
        hidServices.start();

        System.out.println(ANSI_GREEN + "Enumerating attached devices..." + ANSI_RESET);

        // Provide a list of attached devices
        for (HidDevice hidDevice : hidServices.getAttachedHidDevices()) {
            System.out.println(hidDevice.getManufacturer() + "/" + hidDevice.getProduct() + " ... usagePage: " + UsagePage.map(hidDevice.getUsagePage()));
        }

        HidDevice device = hidServices.getHidDevice(0x54c, 0x9cc, null);
Debug.println(device + "\nopen?: " + device.isOpen());
//        boolean r = dualShock4.open();
//Debug.println("open dualShock4: " + r);

        device.addInputReportListener(e -> display(e.getReport()));

        waitAndShutdown(hidServices, 180);
    }

    static void display(byte[] data) {
        DualShock4Plugin.display(data);
    }

    static void display1(byte[] data) {
        System.out.print("< [");
        for (int i = 0; i < 25; i++) {
            System.out.printf(" %02x", data[i]);
        }
        System.out.println("]");
    }
}