/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package javax_usb;

import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;


/**
 * Dumps the names of all USB devices by using the javax-javax_usb API. On
 * Linux this can only work when your user has write permissions on all the USB
 * device files in /dev/bus/javax_usb (Running this example as root will work). On
 * Windows this can only work for devices which have a libusb-compatible driver
 * installed. On OSX this usually works without problems.
 *
 * ⚠⚠⚠ 'javax.usb.properties' must be contained in classpath ⚠⚠⚠
 *
 * @author Klaus Reimer <k@ailis.de>
 */
public class DumpNames {

    /**
     * Dumps the name of the specified device to stdout.
     *
     * @param device The USB device.
     * @throws UnsupportedEncodingException When string descriptor could not be parsed.
     * @throws UsbException                 When string descriptor could not be read.
     */
    private static void dumpName(UsbDevice device)
            throws UnsupportedEncodingException, UsbException {
        // Read the string descriptor indices from the device descriptor.
        // If they are missing then ignore the device.
        UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
        byte iManufacturer = desc.iManufacturer();
        byte iProduct = desc.iProduct();
        if (iManufacturer == 0 || iProduct == 0) return;

        // Dump the device name
        System.out.println(device.getString(iManufacturer) + " " + device.getString(iProduct));
    }

    /**
     * Processes the specified USB device.
     *
     * @param device The USB device to process.
     */
    @SuppressWarnings("unchecked")
    private static void processDevice(UsbDevice device) {
        // When device is a hub then process all child devices
        if (device.isUsbHub()) {
            UsbHub hub = (UsbHub) device;
            for (UsbDevice child : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
                processDevice(child);
            }
        }

        // When device is not a hub then dump its name.
        else {
            try {
                dumpName(device);
            } catch (Exception e) {
                // On Linux this can fail because user has no write permission
                // on the USB device file. On Windows it can fail because
                // no libusb device driver is installed for the device
                System.err.println("Ignoring problematic device: " + e);
            }
        }
    }

    /**
     * usb4java.Main method.
     *
     * @param args Command-line arguments (Ignored)
     * @throws UsbException When an USB error was reported which wasn't handled by this
     *                      program itself.
     */
    public static void main(String[] args) throws UsbException {
        // Get the USB services and dump information about them
        UsbServices services = UsbHostManager.getUsbServices();

        // Dump the root USB hub
        processDevice(services.getRootUsbHub());
    }
}