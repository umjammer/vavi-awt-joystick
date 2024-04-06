/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.listener;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import net.java.games.input.Component;
import net.java.games.input.Event;
import net.java.games.input.InputEvent;
import net.java.games.input.InputEventListener;
import vavi.games.input.helper.RococoaAppChangeListener;
import vavi.util.Debug;
import vavi.util.event.GenericEvent;
import vavi.util.event.GenericListener;
import vavi.util.event.GenericSupport;


/**
 * GamepadInputEventListener.
 *
 * system properties
 * <ul>
 *  <li>vavi.games.input.listener.warmup ... delay time to start sending events to an application, default 1500</li>
 * </ul>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-21 nsano initial version <br>
 */
public class GamepadInputEventListener implements InputEventListener {

    private final List<GamepadListener> listeners = new ArrayList<>();

    /** delay time to start sending events to an application */
    private static int warmup;

    static {
        warmup = Integer.parseInt(System.getProperty("vavi.games.input.listener.warmup", "1500"));
    }

    /** holds current listener */
    private final AtomicReference<GamepadListener> currentListener = new AtomicReference<>();

    /** store when time of application changed */
    private long warmupTime;

    private final GenericSupport observers = new GenericSupport();

    public void addObserver(GenericListener observer) {
        observers.addGenericListener(observer);
    }

    public class Context {
        private Context() {}
        public void fireEventHappened(GenericEvent event) {
            observers.fireEventHappened(event);
        }
    }

    private final Context context = new Context();

    public interface AppInfo {
        String id();
        int pid();
        Rectangle bounds();
    }

    public interface AppChangeListener {
        void onAppChanged(AppInfo a);
    }

    /** */
    public GamepadInputEventListener() {
        ServiceLoader.load(GamepadListener.class).forEach(listener -> {
            listeners.add(listener);
            listener.init(context);
        });

        new RococoaAppChangeListener() {
            /** @see "https://stackoverflow.com/a/33395422" */
            @Override public void onAppChanged(AppInfo a) {
try {
//Debug.println(a.bundleIdentifier() + ":" + a.processIdentifier());
                Optional<GamepadListener> o =  listeners.stream().filter(l -> l.match(a)).findFirst();
                if (o.isEmpty()) {
                    if (currentListener.get() != null) {
Debug.println(Level.FINE, ">>FRONTMOST: none");
                        currentListener.get().deactive();
                        currentListener.set(null);
                    }
                } else {
                    if (currentListener.get() != o.get()) {

Debug.println(Level.FINE, ">>FRONTMOST: " + o.get());
                        currentListener.set(o.get());
                        o.get().active();
                        warmupTime = System.currentTimeMillis();
                    }
                }
} catch (Throwable t) {
 Debug.printStackTrace(t);
}
            }
        };
    }

    /** for reuse */
    private final Event inputEvent = new Event();

    @Override
    public void onInput(InputEvent event) {
        GamepadListener listener = currentListener.get();
        if (listener == null) {
            return;
        }
        if (System.currentTimeMillis() - warmupTime < warmup) {
//Debug.println("warmup: " + listener);
            return;
        }

        listener.before();
        while (event.getNextEvent(inputEvent)) {
            Component c =  inputEvent.getComponent();
            switch (c.getName()) {
                case "X(48)" -> listener.onX(inputEvent);
                case "Y(49)" -> listener.onY(inputEvent);
                case "RZ(53)" -> listener.onRZ(inputEvent);
                case "Z(50)" -> listener.onZ(inputEvent);
                case "HATSWITCH(57)" -> listener.onHatSwitch(inputEvent);
                case "ButtonUsageId(1)" -> listener.onButton1(inputEvent);
                case "ButtonUsageId(2)" -> listener.onButton2(inputEvent);
                case "ButtonUsageId(3)" -> listener.onButton3(inputEvent);
                case "ButtonUsageId(4)" -> listener.onButton4(inputEvent);
                case "ButtonUsageId(5)" -> listener.onButton5(inputEvent);
                case "ButtonUsageId(6)" -> listener.onButton6(inputEvent);
                case "ButtonUsageId(7)" -> listener.onButton7(inputEvent);
                case "ButtonUsageId(8)" -> listener.onButton8(inputEvent);
                case "ButtonUsageId(9)" -> listener.onButton9(inputEvent);
                case "ButtonUsageId(10)" -> listener.onButton10(inputEvent);
                case "ButtonUsageId(11)" -> listener.onButton11(inputEvent);
                case "ButtonUsageId(12)" -> listener.onButton12(inputEvent);
                case "ButtonUsageId(13)" -> listener.onButton13(inputEvent);
                case "ButtonUsageId(14)" -> listener.onButton14(inputEvent);
                case "RX(51)" -> listener.onRX(inputEvent);
                case "RY(52)" -> listener.onRY(inputEvent);
            }
        }
        listener.after();
    }
}
