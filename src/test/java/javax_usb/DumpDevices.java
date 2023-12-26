/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package javax_usb;

import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbPort;
import javax.usb.UsbServices;


/**
 * Dumps all devices by using the javax-javax_usb API.
 *
 * ⚠⚠⚠ 'javax.usb.properties' must be contained in classpath ⚠⚠⚠
 *
 * @author Klaus Reimer <k@ailis.de>
 */
public class DumpDevices {

    /**
     * Dumps the specified USB device to stdout.
     *
     * @param device The USB device to dump.
     */
    @SuppressWarnings("unchecked")
    private static void dumpDevice(UsbDevice device) throws UsbException, UnsupportedEncodingException {
        // Dump information about the device itself
        System.out.println(device);
        UsbPort port = device.getParentUsbPort();
        if (port != null) {
            System.out.println("Connected to port: " + port.getPortNumber());
            System.out.println("Parent: " + port.getUsbHub());
        }

        // Dump device descriptor
        System.out.println(device.getUsbDeviceDescriptor());

        // Process all configurations
        for (UsbConfiguration configuration : (List<UsbConfiguration>) device.getUsbConfigurations()) {
            // Dump configuration descriptor
            System.out.println("■ Device: -------------------------------");
            System.out.println("  Manufacturer\t" + configuration.getUsbDevice().getManufacturerString());
            System.out.println("  Product\t\t" + configuration.getUsbDevice().getProductString());
            System.out.println("  ID:\t\t" + configuration.getUsbDevice().getUsbDeviceDescriptor().idVendor() + ":" + configuration.getUsbDevice().getUsbDeviceDescriptor().idProduct());
            System.out.println();
            System.out.println(configuration.getUsbConfigurationDescriptor());

            // Process all interfaces
            for (UsbInterface iface : (List<UsbInterface>) configuration.getUsbInterfaces()) {
                // Dump the interface descriptor
                System.out.println(iface.getUsbInterfaceDescriptor());

                // Process all endpoints
                for (UsbEndpoint endpoint : (List<UsbEndpoint>) iface.getUsbEndpoints()) {
                    // Dump the endpoint descriptor
                    System.out.println(endpoint.getUsbEndpointDescriptor());
                }
            }
        }

        System.out.println();

        // Dump child devices if device is a hub
        if (device.isUsbHub()) {
            UsbHub hub = (UsbHub) device;
            for (UsbDevice child : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
                dumpDevice(child);
            }
        }
    }

    /**
     * usb4java.Main method.
     *
     * @param args Command-line arguments (Ignored)
     * @throws UsbException When a USB error was reported which wasn't handled by this
     *                      program itself.
     */
    public static void main(String[] args) throws Exception {
        // Get the USB services and dump information about them
        UsbServices services = UsbHostManager.getUsbServices();
        System.out.println("USB Service Implementation: " + services.getImpDescription());
        System.out.println("Implementation version: " + services.getImpVersion());
        System.out.println("Service API version: " + services.getApiVersion());
        System.out.println();

        // Dump the root USB hub
        dumpDevice(services.getRootUsbHub());
    }
}