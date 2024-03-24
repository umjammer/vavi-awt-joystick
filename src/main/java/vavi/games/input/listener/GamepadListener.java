/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.listener;

import java.util.List;

import net.java.games.input.Event;
import org.rococoa.cocoa.appkit.NSRunningApplication;


/**
 * GamepadListener.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-20 nsano initial version <br>
 */
public interface GamepadListener {

    boolean match(NSRunningApplication a);

    void before();

    void onX(Event e);
    void onY(Event e);
    void onZ(Event e);
    void onRZ(Event e);
    void onHatSwitch(Event e);
    void onButton1(Event e);
    void onButton2(Event e);
    void onButton3(Event e);
    void onButton4(Event e);
    void onButton5(Event e);
    void onButton6(Event e);
    void onButton7(Event e);
    void onButton8(Event e);
    void onButton9(Event e);
    void onButton10(Event e);
    void onButton11(Event e);
    void onButton12(Event e);
    void onButton13(Event e);
    void onButton14(Event e);
    void onRX(Event e);
    void onRY(Event e);

    void after();
}
