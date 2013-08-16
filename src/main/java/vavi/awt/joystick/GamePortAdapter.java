package vavi.awt.joystick;
/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */


/**
 * The adapter class for the direct input device.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020420 nsano initial version <br>
 *          0.10 020421 nsano move polling procedure to listener <br>
 *          1.00 020422 nsano complete <br>
 */
public abstract class GamePortAdapter implements GamePortListener {

    /** @deprecated */
    public void portChange(GamePortEvent ev) {
    }

    /** Called when buttons state changed. */
    public void buttonChange(GamePortEvent ev) {
    }

    /** Called when position state changed. */
    public void positionChange(GamePortEvent ev) {
    }

    /** @deprecated */
    public void buttonClicked(GamePortEvent ev) {
    }

    /** Called when a button pressed. */
    public void buttonPressed(GamePortEvent ev) {
    }

    /** Called when a button released. */
    public void buttonReleased(GamePortEvent ev) {
    }
}

/* */
