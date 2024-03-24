/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.listener;

import net.java.games.input.Event;


/**
 * GamepadAdapter.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-20 nsano initial version <br>
 */
public abstract class GamepadAdapter implements GamepadListener {

    @Override
    public void before() {}

    @Override
    public void onX(Event e) {}
    @Override
    public void onY(Event e) {}
    @Override
    public void onZ(Event e) {}
    @Override
    public void onRZ(Event e) {}
    @Override
    public void onHatSwitch(Event e) {}
    @Override
    public void onButton1(Event e) {}
    @Override
    public void onButton2(Event e) {}
    @Override
    public void onButton3(Event e) {}
    @Override
    public void onButton4(Event e) {}
    @Override
    public void onButton5(Event e) {}
    @Override
    public void onButton6(Event e) {}
    @Override
    public void onButton7(Event e) {}
    @Override
    public void onButton8(Event e) {}
    @Override
    public void onButton9(Event e) {}
    @Override
    public void onButton10(Event e) {}
    @Override
    public void onButton11(Event e) {}
    @Override
    public void onButton12(Event e) {}
    @Override
    public void onButton13(Event e) {}
    @Override
    public void onButton14(Event e) {}
    @Override
    public void onRX(Event e) {}
    @Override
    public void onRY(Event e) {}

    @Override
    public void after() {}

    /** delaying utility */
    protected static void sleep(long l) {
        try { Thread.sleep(l); } catch (InterruptedException ignore) {}
    }
}
