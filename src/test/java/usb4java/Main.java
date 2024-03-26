package usb4java;

import java.nio.ByteBuffer;

import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;
import vavi.util.Debug;


public class Main {

    private static final short VID = 0x054c;
    private static final short PID = 0x9cc;

    Context context;

    public Main() {
        context = new Context();
        int result = LibUsb.init(context);

        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to initialize libusb.", result);
        }

        ByteBuffer data = ByteBuffer.allocateDirect(49);
        DeviceHandle ds3Handle = getDeviceHandle(findDevice(VID, PID));
        LibUsb.controlTransfer(ds3Handle, (byte) 0xa1, (byte) 0x1, (short) 0x101, (short) 0, data, 1000L);

        LibUsb.exit(context);
    }

    private Device findDevice(int vid, int pid) {
        Device usbDevice = null;
        DeviceList list = new DeviceList();
        int result = LibUsb.getDeviceList(context, list);

        if (result < 0) {
            throw new LibUsbException("Unable to get device list", result);
        }

        try {
            for (Device device : list) {
                DeviceDescriptor descriptor = new DeviceDescriptor();
                result = LibUsb.getDeviceDescriptor(device, descriptor);

                if (result != LibUsb.SUCCESS) {
                    throw new LibUsbException("Unable to read device descriptor", result);
                }

                if (descriptor.idVendor() == vid && descriptor.idProduct() == pid) {
                    usbDevice = device;
Debug.printf("found: %04x:%04x%n", vid, pid);
                }
            }
        } finally {
            LibUsb.freeDeviceList(list, false);
        }

        if (usbDevice != null) {
            // Device found
            LibUsb.refDevice(usbDevice);
        }

        return usbDevice;
    }

    private static DeviceHandle getDeviceHandle(Device device) {
        DeviceHandle handle = new DeviceHandle();
        int result = LibUsb.open(device, handle);

        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to open USB device", result);
        }

        return handle;
    }

    public static void main(String[] args) {
        new Main();
    }
}
