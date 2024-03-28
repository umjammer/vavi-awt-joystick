/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.usb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.PollingController;
import net.java.games.input.Rumbler;
import net.java.games.input.usb.HidController;
import vavi.util.Debug;


/**
 * UsbController.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2023-09-18 nsano initial version <br>
 */
public abstract class UsbController extends PollingController implements HidController {

    private static String getMString(UsbDevice device, int mid, int pid) {
        try {
            return device.getProductString();
        } catch (UsbException | UnsupportedEncodingException e) {
            return "Usb:" + mid + ":" + pid;
        }
    }

    /**
     * Protected constructor for a controller containing the specified
     * axes, child controllers, and rumblers
     *
     * @param mid       mid for the controller
     * @param pid       pid for the controller
     * @param components components for the controller
     * @param children   child controllers for the controller
     * @param rumblers   rumblers for the controller
     */
    protected UsbController(UsbDevice device, int mid, int pid, Component[] components, Controller[] children, Rumbler[] rumblers) {
        super(getMString(device, mid, pid), components, children, rumblers);
        this.device = device;
        this.mid = mid;
        this.pid = pid;

        UsbConfiguration configuration = (UsbConfiguration) device.getUsbConfigurations().get(0);
        usbInterface = (UsbInterface) configuration.getUsbInterfaces().get(0);

        UsbEndpoint endpointOut, endpointIn;
Debug.println("endPoints: " + usbInterface.getUsbEndpoints().size());
        for (int i = 0; i < usbInterface.getUsbEndpoints().size(); i++) {
            byte endpointAddr = ((UsbEndpoint) (usbInterface.getUsbEndpoints().get(i))).getUsbEndpointDescriptor().bEndpointAddress();
            if (((endpointAddr & 0x80) == 0x80)) {
                endpointIn = (UsbEndpoint) (usbInterface.getUsbEndpoints().get(i));
Debug.println("IN: " + endpointIn);
            } else if ((endpointAddr & 0x80) == 0x00) {
                endpointOut = (UsbEndpoint) (usbInterface.getUsbEndpoints().get(i));
Debug.println("OUT: " + endpointOut);
            }
        }
        // 0x02 : OUT, 0x081 IN
        endpointOut = usbInterface.getUsbEndpoint((byte) 0x02);
        endpointIn = usbInterface.getUsbEndpoint((byte) 0x81);

        pipeOut = endpointOut.getUsbPipe();
        pipeIn = endpointIn.getUsbPipe();

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public Type getType() {
        return Type.GAMEPAD;
    }

    @Override
    protected boolean getNextDeviceEvent(Event event) throws IOException {
        if (events.isEmpty())
            return false;

        event.set(events.poll());
        return true;
    }

    /** */
    protected abstract boolean fillEvent(byte[] data);

    /** */
    protected Deque<Event> events = new ArrayDeque<>();

    /** The device product id */
    private final int pid;
    /** The device manufacturer id */
    private final int mid;

    /** Returns device manufacturer id */
    public int getManufacturerId() {
        return mid;
    }

    /** Returns device product id */
    @Override
    public int getProductId() {
        return pid;
    }

    /** */
    private final UsbDevice device;
    private final UsbInterface usbInterface;

    private final UsbPipe pipeIn;
    private final UsbPipe pipeOut;

    @Override
    public synchronized void open() {
        // Starts the game port polling.
        try {
            this.usbInterface.claim(usbInterface -> true);
            this.pipeIn.open();
            this.pipeOut.open();
        } catch (UsbException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public synchronized void close() {
        // Stops the game port polling.
        try {
            this.pipeIn.close();
            this.pipeOut.close();
            this.usbInterface.release();
        } catch (UsbException e) {
            throw new IllegalStateException(e);
        }
    }

    /** */
    private static final int PACKET_LENGTH = 64;

    @Override
    protected void pollDevice() throws IOException {
    }
}
