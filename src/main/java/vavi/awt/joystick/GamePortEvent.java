/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick;

import java.util.EventObject;


/**
 * The event object for the direct input device.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020420 nsano initial version <br>
 */
public class GamePortEvent extends EventObject {

    /** Current button number that is pressed. */
    int buttonNumber;
    /**
     * Current state of the 32 joystick buttons.
     * The value of this member can be set to any combination
     * of JOY_BUTTONn flags, where n is a value ranging from
     * 1 to 32. Each value corresponds to the button that is
     * pressed.
     */
    public int buttons;
    /**
     * Flags that indicate if information returned in this
     * class is valid. Members that do not contain valid
     * information are set to 0. These flags can be one or
     * more of the JOY_RETURN* and JOY_CAL_ types.
     */
    int flags;
    /**
     * Current position of the Point-Of-View control.
     * Values for this member range from 0 to 35,900.
     * These values represent each views angle, in degress,
     * multiplied by 100.
     */
    int pov;
    /** Current position of the rudder, or fourth joystick axis. */
    public int rPos;
    /** Current positions of the fifth joystick axis. */
    public int uPos;
    /** Current positions of the sixth joystick axis. */
    public int vPos;
    /**	Current x-coordinate. */
    public int xPos;
    /**	Current y-coordinate. */
    public int yPos;
    /** Current z coordinate. */
    public int zPos;

    /** target button no. */
    public int target;

    /** Creates an event object. */
    public GamePortEvent(Object source) {
        super(source);
    }

    /** */
    public GamePortEvent(Object source, int target) {
        super(source);
        this.target = target;
    }

    /** */
    public int getTarget() {
        return target;
    }
}

/* */
