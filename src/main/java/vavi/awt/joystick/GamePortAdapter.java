/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick;

/**
 * The adapter class for the direct input device.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020420 nsano initial version <br>
 *          0.10 020421 nsano move polling procedure to listener <br>
 *          1.00 020422 nsano complete <br>
 */
public abstract class GamePortAdapter implements GamePortListener {

    @Deprecated
    public void portChange(GamePortEvent ev) {
    }

    @Override
    public void buttonChange(GamePortEvent ev) {
    }

    @Override
    public void positionChange(GamePortEvent ev) {
    }

    @Override
    public void buttonClicked(GamePortEvent ev) {
    }

    @Override
    public void buttonPressed(GamePortEvent ev) {
    }

    @Override
    public void buttonReleased(GamePortEvent ev) {
    }
}

/* */
