/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import net.java.games.input.Component;
import net.java.games.input.Event;
import net.java.games.input.InputEvent;
import net.java.games.input.InputEventListener;
import org.rococoa.cocoa.appkit.NSRunningApplication;
import org.rococoa.cocoa.appkit.NSWorkspace;
import vavi.util.Debug;


/**
 * GamepadInputEventListener.
 *
 * system properties
 * <ul>
 *  <li>vavi.games.input.listener.period ... top most application detection interval, default 1000</li>
 *  <li>vavi.games.input.listener.warmup ... delay time to start sending events to an application, default 1500</li>
 * </ul>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-21 nsano initial version <br>
 */
public class GamepadInputEventListener implements InputEventListener {

    private static final List<GamepadListener> listeners = new ArrayList<>();

    private static int period;
    private static int warmup;

    static {
        ServiceLoader.load(GamepadListener.class).forEach(listeners::add);

        period = Integer.parseInt(System.getProperty("vavi.games.input.listener.period", "1000"));
        warmup = Integer.parseInt(System.getProperty("vavi.games.input.listener.warmup", "1500"));
    }

    private final AtomicReference<GamepadListener> listenerR = new AtomicReference<>();

    private long activationTime;

    {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            // TODO notification needed for realtime detection
            //  https://stackoverflow.com/a/33395422
            NSRunningApplication a = NSWorkspace.sharedWorkspace().frontmostApplication();
//Debug.println(a);
            Optional<GamepadListener> o =  listeners.stream().filter(l -> l.match(a)).findFirst();
            if (o.isEmpty()) {
                if (listenerR.get() != null) {
Debug.println(">>FRONTMOST: none");
                    listenerR.set(null);
                }
            } else {
                if (listenerR.get() != o.get()) {
Debug.println(">>FRONTMOST: " + o.get());
                    listenerR.set(o.get());
                    activationTime = System.currentTimeMillis();
                }
            }
        }, 0, period, TimeUnit.MICROSECONDS);
    }

    @Override
    public void onInput(InputEvent event) {
        GamepadListener listener = listenerR.get();
        if (listener == null) {
            return;
        }
        if (System.currentTimeMillis() - activationTime < warmup) {
//Debug.println("warmup: " + listener);
            return;
        }

        Event e = new Event();
        listener.before();
        while (event.getNextEvent(e)) {
            Component c =  e.getComponent();
            switch (c.getName()) {
                case "X(48)" -> listener.onX(e);
                case "Y(49)" -> listener.onY(e);
                case "RZ(53)" -> listener.onRZ(e);
                case "Z(50)" -> listener.onZ(e);
                case "HATSWITCH(57)" -> listener.onHatSwitch(e);
                case "ButtonUsageId(1)" -> listener.onButton1(e);
                case "ButtonUsageId(2)" -> listener.onButton2(e);
                case "ButtonUsageId(3)" -> listener.onButton3(e);
                case "ButtonUsageId(4)" -> listener.onButton4(e);
                case "ButtonUsageId(5)" -> listener.onButton5(e);
                case "ButtonUsageId(6)" -> listener.onButton6(e);
                case "ButtonUsageId(7)" -> listener.onButton7(e);
                case "ButtonUsageId(8)" -> listener.onButton8(e);
                case "ButtonUsageId(9)" -> listener.onButton9(e);
                case "ButtonUsageId(10)" -> listener.onButton10(e);
                case "ButtonUsageId(11)" -> listener.onButton11(e);
                case "ButtonUsageId(12)" -> listener.onButton12(e);
                case "ButtonUsageId(13)" -> listener.onButton13(e);
                case "ButtonUsageId(14)" -> listener.onButton14(e);
                case "RX(51)" -> listener.onRX(e);
                case "RY(52)" -> listener.onRY(e);
            }
        }
        listener.after();
    }
}
