/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.rococoa.spi;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

import net.java.games.input.AbstractController;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.Event;
import net.java.games.input.PollingController;
import net.java.games.input.Rumbler;
import net.java.games.input.plugin.DualShock4PluginBase;
import net.java.games.input.plugin.DualShock4PluginBase.DualShock4Output;
import net.java.games.input.usb.HidController.HidReport;
import net.java.games.input.usb.HidRumbler;
import org.rococoa.cocoa.gamecontroller.GCController;
import vavi.util.Debug;

import static vavix.rococoa.iokit.IOKitLib.kIOHIDReportTypeOutput;


/**
 * RococoaController.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-27 nsano initial version <br>
 */
public class RococoaController extends PollingController {

    /** */
    private final GCController device;

    /**
     * Protected constructor for a controller containing the specified
     * axes, child controllers, and rumblers
     *
     * @param device     the controller
     * @param components components for the controller
     * @param children   child controllers for the controller
     * @param rumblers   rumblers for the controller
     */
    protected RococoaController(GCController device, Component[] components, Controller[] children, Rumbler[] rumblers) {
        super(device.description(), components, children, rumblers);
        this.device = device;
        setEventQueueSize(components.length);
    }

    /** */
    public GCController getDevice() {
        return device;
    }

    @Override
    public void open() throws IOException {
        super.open();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public Type getType() {
        return Type.GAMEPAD;
    }

    /** */
    private Deque<Event> deque = new ArrayDeque<>();

    @Override
    public synchronized void pollDevice() throws IOException {
        deque.clear();
        long nanos = System.nanoTime();
        for (Component component : getComponents()) {
            Event event = new Event();
            event.set(component, ((RococoaComponent) component).poll(), nanos);
            deque.offer(event);
        }
//Debug.println("deque: " + deque.size());
    }

    @Override
    protected boolean getNextDeviceEvent(Event event) throws IOException {
        if (deque.peek() != null) {
            event.set(deque.poll());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void output(Report report) throws IOException {
        report.cascadeTo(getRumblers());

        for (Rumbler rumbler : getRumblers()) {
            if (rumbler.getOutputIdentifier() == DualShock4Output.LED_BLUE) {

            }
        }
    }
}
