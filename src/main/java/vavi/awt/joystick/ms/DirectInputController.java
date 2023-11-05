/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.ms;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;

import com.ms.directX.DirectXConstants;
import com.ms.directX.JoyInfo;
import net.java.games.input.AbstractController;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Rumbler;
import net.java.games.input.usb.HidController;
import vavi.util.Debug;


/**
 * The Direct Input device.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020422 nsano initial version <br>
 */
public class DirectInputController extends AbstractController implements HidController {

    /**
     * Protected constructor for a controller containing the specified
     * axes, child controllers, and rumblers
     *
     * @param id        sid for the controller
     * @param mid       mid for the controller
     * @param pid       pid for the controller
     * @param components components for the controller
     * @param children   child controllers for the controller
     * @param rumblers   rumblers for the controller
     */
    protected DirectInputController(int id, int mid, int pid, Component[] components, Controller[] children, Rumbler[] rumblers) {
        super("Ms:" + mid + ":" + pid, components, children, rumblers);
        this.id = id;
        this.mid = mid;
        this.pid = pid;
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
    protected Deque<Event> events = new ArrayDeque<>();

    /** The device id */
    private int id;
    /** The device product id */
    private int pid;
    /** The device manufacturer id */
    private int mid;

    @Override
    public int getVendorId() {
        return mid;
    }

    @Override
    public int getProductId() {
        return pid;
    }

    /** */
    private JoyInfo backup = new JoyInfo();

    @Override
    protected void pollDevice() throws IOException {

        JoyInfo joyInfo = new JoyInfo();
        joyInfo.flags = DirectXConstants.JOY_RETURNALL;

        try {
            DirectInputControllerEnvironment.getPos(id, joyInfo);
/*
            ev.buttonNumber = joyInfo.buttonNumber;
            ev.buttons      = joyInfo.buttons     ;
            ev.flags        = joyInfo.flags       ;
            ev.pov          = joyInfo.pov         ;
            ev.rPos         = joyInfo.rPos        ;
            ev.uPos         = joyInfo.uPos        ;
            ev.vPos         = joyInfo.vPos        ;
            ev.xPos         = joyInfo.xPos        ;
            ev.yPos         = joyInfo.yPos        ;
            ev.zPos         = joyInfo.zPos        ;

            portChange(ev);
*/
            if (backup.buttons != joyInfo.buttons) {

                for (int i = 0; i < 16; i++) {
                    if ((backup.buttons & (0x0001 << i)) == 0 &&
                            (joyInfo.buttons & (0x0001 << i)) != 0) {
                        Event event = new Event();
                        event.set(getComponent(Component.Identifier.Button.valueOf(String.valueOf(i))), 0, System.nanoTime());
                        events.add(event);
                    }
                    else if ((backup.buttons & (0x0001 << i)) != 0 &&
                            (joyInfo.buttons & (0x0001 << i)) == 0) {
                        Event event = new Event();
                        event.set(getComponent(Component.Identifier.Button.valueOf(String.valueOf(i))), 0, System.nanoTime());
                        events.add(event);
                    }
                }
            }

            if (backup.rPos != joyInfo.rPos) {
                Event ev = new Event();
                ev.set(getComponent(Component.Identifier.Axis.RX), joyInfo.rPos, System.nanoTime());
                events.add(ev);
            }
            if (backup.uPos != joyInfo.uPos) {
                Event ev = new Event();
                ev.set(getComponent(Component.Identifier.Axis.RY), joyInfo.uPos, System.nanoTime());
                events.add(ev);

            }
            if (backup.vPos != joyInfo.vPos) {
                Event ev = new Event();
                ev.set(getComponent(Component.Identifier.Axis.RZ), joyInfo.vPos, System.nanoTime());
                events.add(ev);

            }
            if (backup.xPos != joyInfo.xPos) {
                Event ev = new Event();
                ev.set(getComponent(Component.Identifier.Axis.X), joyInfo.xPos, System.nanoTime());
                events.add(ev);

            }
            if (backup.yPos != joyInfo.yPos) {
                Event ev = new Event();
                ev.set(getComponent(Component.Identifier.Axis.Y), joyInfo.yPos, System.nanoTime());
                events.add(ev);

            }
            if (backup.zPos != joyInfo.zPos) {
                Event ev = new Event();
                ev.set(getComponent(Component.Identifier.Axis.Z), joyInfo.zPos, System.nanoTime());
                events.add(ev);
            }

            backup.buttonNumber = joyInfo.buttonNumber;
            backup.buttons      = joyInfo.buttons     ;
            backup.flags        = joyInfo.flags       ;
            backup.pov          = joyInfo.pov         ;
            backup.rPos         = joyInfo.rPos        ;
            backup.uPos         = joyInfo.uPos        ;
            backup.vPos         = joyInfo.vPos        ;
            backup.xPos         = joyInfo.xPos        ;
            backup.yPos         = joyInfo.yPos        ;
            backup.zPos         = joyInfo.zPos        ;

        } catch (Exception f) {
Debug.printStackTrace(Level.FINE, f);
        }
    }
}

/* */
