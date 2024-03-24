/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.usb;

import java.util.List;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;

import net.java.games.input.Controller;
import net.java.games.input.ControllerListenerSupport;
import net.java.games.input.usb.HidController;
import net.java.games.input.usb.HidControllerEnvironment;
import vavi.util.Debug;


/**
 * The USB Input device.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 230919 nsano initial version <br>
 */
public final class UsbEnvironmentPlugin extends ControllerListenerSupport implements HidControllerEnvironment {

    /** */
    public UsbEnvironmentPlugin() {
        try {
            UsbHub hub = UsbHostManager.getUsbServices().getRootUsbHub();





        } catch (UsbException e) {
            Debug.printStackTrace(e);
        }
    }

    private List<UsbController> controllers;

    @Override
    public Controller[] getControllers() {
        return controllers.toArray(Controller[]::new);
    }

    @Override
    public boolean isSupported() {
        return false;
    }

    /** */
    private UsbHub hub;






    /**
     * find recursively
     * @return nullable
     */
    @SuppressWarnings("unchecked")
    static UsbDevice getUsbDevice(UsbHub hub, int mid, int pid) {
        for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
            if (device.isUsbHub()) {
                UsbDevice r = getUsbDevice((UsbHub) device, mid, pid);
                if (r != null) return r;
            } else {
                UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
Debug.printf("%1$d, [%2$d,0x%2$x], %3$d, [%4$d,0x%4$x]%n", desc.idVendor(), mid, desc.idProduct(), pid);
                if (desc.idVendor() == mid && desc.idProduct() == pid) return device;
            }
        }

        return null;
    }

    @Override
    public HidController getController(int mid, int pid) {
        for (UsbController controller : controllers) {
            if (controller.getManufacturerId() == mid && controller.getProductId() == pid) {
                return controller;
            }
        }
        throw new IllegalArgumentException("no device (mid=" + mid + ",pid=" + pid + ")");
    }
}

/* */
