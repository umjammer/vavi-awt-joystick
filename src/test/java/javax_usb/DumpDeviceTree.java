/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package javax_usb;

import java.util.List;
import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;


/**
 * Dumps a tree of all found USB devices.
 *
 * ⚠⚠⚠ 'javax.usb.properties' must be contained in classpath ⚠⚠⚠
 *
 * @author Klaus Reimer <k@ailis.de>
 */
public class DumpDeviceTree {

    /**
     * Dumps the specified device and its sub devices.
     *
     * @param device The USB device to dump.
     * @param level  The indentation level.
     */
    @SuppressWarnings("unchecked")
    public static void dump(UsbDevice device, int level) {
        for (int i = 0; i < level; i += 1)
            System.out.print("  ");
        System.out.println(device);
        if (device.isUsbHub()) {
            final UsbHub hub = (UsbHub) device;
            for (UsbDevice child : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
                dump(child, level + 1);
            }
        }
    }

    /**
     * usb4java.Main method.
     *
     * @param args Command-line arguments (Ignored).
     * @throws UsbException When USB communication fails.
     */
    public static void main(String[] args) throws UsbException {
        UsbServices services = UsbHostManager.getUsbServices();
        dump(services.getRootUsbHub(), 0);
    }
}