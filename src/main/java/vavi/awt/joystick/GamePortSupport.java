/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick;

import java.util.Vector;


/**
 * The game port listener support.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020422 nsano initial version <br>
 *          0.10 020914 nsano multipule listeners support <br>
 */
public class GamePortSupport {

    /** GamePort のリスナー */
    private Vector listeners = new Vector();

    /** */
    public void addGamePortListener(GamePortListener l) {
        listeners.addElement(l);
    }

    /** */
    public void removeGamePortListener(GamePortListener l) {
        listeners.removeElement(l);
    }

    /** */
    public synchronized void fireButtonChange(GamePortEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            ((GamePortListener) listeners.elementAt(i)).buttonChange(ev);
        }
    }

    /** */
    public synchronized void firePositionChange(GamePortEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            ((GamePortListener) listeners.elementAt(i)).positionChange(ev);
        }
    }

    /** */
    public synchronized void fireButtonPressed(GamePortEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            ((GamePortListener) listeners.elementAt(i)).buttonPressed(ev);
        }
    }

    /** */
    public synchronized void fireButtonReleased(GamePortEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            ((GamePortListener) listeners.elementAt(i)).buttonReleased(ev);
        }
    }
}

/* */
