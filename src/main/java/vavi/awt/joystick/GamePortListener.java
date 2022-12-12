/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick;

/**
 * The listener interface for the direct input device.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020420 nsano initial version <br>
 *          0.10 020421 nsano move polling procedure here <br>
 *          1.00 020422 nsano complete <br>
 */
public interface GamePortListener {

    /** @deprecated */
    @Deprecated
    void portChange(GamePortEvent ev);

    /** Called when buttons state changed. */
    void buttonChange(GamePortEvent ev);

    /** Called when position state changed. */
    void positionChange(GamePortEvent ev);

    /** @deprecated */
    void buttonClicked(GamePortEvent ev);

    /** Called when a button pressed. */
    void buttonPressed(GamePortEvent ev);

    /** Called when a button released. */
    void buttonReleased(GamePortEvent ev);
}

/* */
