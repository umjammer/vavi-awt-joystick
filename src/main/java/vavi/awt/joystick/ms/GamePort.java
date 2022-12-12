/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.ms;

import java.awt.event.ActionListener;
import java.util.Vector;

import com.ms.directX.DirectInput;
import com.ms.directX.JoyCaps;
import com.ms.directX.JoyInfo;

import vavi.awt.joystick.GamePortSupport;
import vavi.awt.joystick.Timer;


/**
 * The Diect Input device.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020422 nsano initial version <br>
 */
public final class GamePort extends GamePortSupport {

    /** The device id */
    private int id;
    /** The device product id */
    private int pid;
    /** The device manufacturer id */
    private int mid;
    /** The device name */
    private String name;

    /** You cannot access. */
    private GamePort() {}

    /** Returns device manufacturer id */
    public int getManufacturerId() {
        return mid;
    }

    /** Returns device product id */
    public int getProductId() {
        return pid;
    }

    /** Returns device name */
    public String getName() {
        return name;
    }

    /** Gets the current joy info */
    public void fillJoyInfo(JoyInfo ji) {
        di.getPos(id, ji);
    }

    //-------------------------------------------------------------------------

    /** Default delay time before starting polling */
    private static final int DEFAUT_DELAY = 50;
    /** Polling interval time */
    private int delay = DEFAUT_DELAY;
    /** The timer */
    private Timer timer;
    /** The thread the timer uses */
    private Thread thread;
    /** The default action of polling */
    private ActionListener listener = new GamePortActionAdapter(this);

    /** Replaces the polling action listener */
    public void replaceActionListener(ActionListener l) {
        stop();
        this.listener = l;
        start();
    }

    /**
     * Starts the game port polling.
     */
    public synchronized void start() {
        if (thread == null) {
            timer = new Timer(delay, listener);
            thread = new Thread() {
                public void run() {
                    timer.start();
                }
            };
            thread.start();
        }
    }

    /**
     * Stops the game port polling.
     */
    public synchronized void stop() {
        if (thread != null) {
            timer.stop();
            thread = null;
        }
    }

    //-------------------------------------------------------------------------

    /** Direct Input */
    private static DirectInput di = new DirectInput();
    /** Game ports */
    private static GamePort[] gps;

    /** Returns game ports. */
    public static GamePort[] getGamePorts() {
        return gps;
    }

    /** */
    public static GamePort getGamePort(int mid, int pid) {
        for (int i = 0; i < gps.length; i++) {
            if(gps[i].getManufacturerId() == mid &&
               gps[i].getProductId()      == pid) {
                return gps[i];
            }
        }
        throw new IllegalArgumentException("no device (mid=" + mid + ",pid=" + pid + ")");
    }

    /* Initialize. */
    static {
        Vector tmp = new Vector();
//System.err.println("num:\t" + di.getNumDevs());
        for (int i = 0; i < di.getNumDevs(); i++) {
            try {
                // vxd:	the name of the virtual device driver.
                // name:	The joystick model name.
                // key:	the name of the registry key.
                di.getDevCapsOEMVxd(i);
System.err.println("---- device no: " + i + " ----");
System.err.println("vxd:\t"  + di.getDevCapsOEMVxd(i));
System.err.println("name:\t" + di.getDevCapsProductName(i));
System.err.println("key:\t"  + di.getDevCapsRegKey(i));
                GamePort gp = new GamePort();
                gp.id   = i;
                gp.name = di.getDevCapsProductName(i);

    /*
     * caps:      Joystick capabilities. These may be one of the following:
     *  JOYCAPS_HASPOV  The joystick has Point-Of-View information.
     *  JOYCAPS_HASR    The joystick has rudder (fourth axis) information.
     *  JOYCAPS_HASU    The joystick has u-coordinate information.
     *  JOYCAPS_HASV    The joystick has v-coordinate information.
     *  JOYCAPS_HASZ    The joystick has z-coordinate information
     *  JOYCAPS_POV4DIR The joystick Point-Of-View supports
     *                  discrete values.
     *  JOYCAPS_POVCTS  The joystick Point-Of-View supports
     *                  continuous degree bearings.
     * maxAxes:    The maximum number of axes supported by the joystick.
     * maxButtons: The maximum number of buttons a joystick supports.
     * mid:        Manufacturer identifier.
     * numAxes:    The number of axes currently supported by the joystick.
     * numButtons: The current number of joystick buttons in use.
     * periodMax:  The maximum polling frequency supported
     *             once an application has captured a joystick.
     * periodMin:  The minimum polling frequency supported
     *             once an application has captured a joystick.
     * pid:        Product identifier.
     * rMax:       Maximum r-axis (the fourth, or rudder-axis) coordinate.
     * rMin:       Minimum r-axis (the fourth, or rudder-axis) coordinate.
     * uMax:       Maximum u-axis (fifth axis) coordinate.
     * uMin:       Minimum u-axis (fifth axis) coordinate.
     * vMax:       Maximum v-axis (sixth axis) coordinate.
     * vMin:       Minimum v-axis (sixth axis) coordinate.
     * xMax:       Maximum x-axis coordinate.
     * xMin:       Minimum x-axis coordinate.
     * yMax:       Maximum y-axis coordinate.
     * yMin:       Minimum y-axis coordinate.
     * zMax:       Maximum z-axis coordinate.
     * zMin:       Minimum z-axis coordinate.
     */
                JoyCaps jc = new JoyCaps();
                di.getDevCaps(i, jc);
//System.err.println("---- " + di.getDevCapsProductName(i) + " ----");
System.err.println("caps:\t\t"     + jc.caps      );
System.err.println("maxAxes:\t"    + jc.maxAxes   );
System.err.println("maxButtons:\t" + jc.maxButtons);
System.err.println("mid:\t\t" + (int) jc.mid       );
System.err.println("numAxes:\t"    + jc.numAxes   );
System.err.println("numButtons:\t" + jc.numButtons);
System.err.println("periodMax:\t"  + jc.periodMax );
System.err.println("periodMin:\t"  + jc.periodMin );
System.err.println("pid:\t\t" + (int) jc.pid       );
System.err.println("rMax:\t\t"     + jc.rMax      );
System.err.println("rMin:\t\t"     + jc.rMin      );
System.err.println("uMax:\t\t"     + jc.uMax      );
System.err.println("uMin:\t\t"     + jc.uMin      );
System.err.println("vMax:\t\t"     + jc.vMax      );
System.err.println("vMin:\t\t"     + jc.vMin      );
System.err.println("xMax:\t\t"     + jc.xMax      );
System.err.println("xMin:\t\t"     + jc.xMin      );
System.err.println("yMax:\t\t"     + jc.yMax      );
System.err.println("yMin:\t\t"     + jc.yMin      );
System.err.println("zMax:\t\t"     + jc.zMax      );
System.err.println("zMin:\t\t"     + jc.zMin      );

                gp.mid = jc.mid;
                gp.pid = jc.pid;

                gp.start();

                tmp.addElement(gp);
            } catch (Exception e) {
//System.err.println(e);
            }
        }

        if (tmp.size() < 1) {
            throw new InternalError("no device found.");
        }

        gps = new GamePort[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            gps[i] = (GamePort) tmp.elementAt(i);
        }
    }
}

/* */
