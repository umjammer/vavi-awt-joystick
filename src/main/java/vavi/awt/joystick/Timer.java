/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * The timer class for the JDK under version 1.2 .
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020420 nsano initial version <br>
 */
public class Timer {
    /** the interval time */
    private long delay;
    /** The action for the timer event */
    private ActionListener listener;
    /** the flag for timer working */
    private boolean flag = false;
    /** the time for waiting before the timer stars */
    private int initialDelay = 100;

    /** Creates a timer. */
    public Timer(int delay, ActionListener listener) {
        this.delay = delay;
        this.listener = listener;
    }

    /** Starts this timer. */
    public void start() {
        try {Thread.sleep(initialDelay);} catch (InterruptedException e) {}

        flag = true;

        long tb = System.currentTimeMillis();

        while (flag) {

            listener.actionPerformed(new ActionEvent(this, 0, null));

            long tc = System.currentTimeMillis();
            long w = delay - (tc - tb);
            if (w > 0) {
                try { Thread.sleep(w); } catch (InterruptedException e) {}
            }
            tb = tc;
        }
    }

    /** Stops this timer. */
    public void stop() {
        flag = false;
    }
}

/* */
