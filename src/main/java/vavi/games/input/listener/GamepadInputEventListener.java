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
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import com.sun.jna.Callback;
import net.java.games.input.Component;
import net.java.games.input.Event;
import net.java.games.input.InputEvent;
import net.java.games.input.InputEventListener;
import org.rococoa.Foundation;
import org.rococoa.ObjCObject;
import org.rococoa.Rococoa;
import org.rococoa.Selector;
import org.rococoa.cocoa.appkit.NSRunningApplication;
import org.rococoa.cocoa.appkit.NSWorkspace;
import org.rococoa.cocoa.foundation.NSNotification;
import org.rococoa.cocoa.foundation.NSNotificationCenter;
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

    public class WorkspaceObserver implements Callback {
        public void applicationWasActivated(NSNotification notification) {
            NSWorkspace workspace = Rococoa.cast(notification.object(), NSWorkspace.class);
            NSRunningApplication a = workspace.frontmostApplication();
//Debug.println("applicationWasActivated: " + a.bundleIdentifier());
            process(a);
        }

        public void applicationWasDeactivated(NSNotification notification) {
            NSWorkspace workspace = Rococoa.cast(notification.object(), NSWorkspace.class);
            NSRunningApplication a = workspace.frontmostApplication();
//Debug.println("applicationWasDeactivated: " + a.bundleIdentifier());
            process(a);
        }
    }

    /** */
    public GamepadInputEventListener() {
        ServiceLoader.load(GamepadListener.class).forEach(listener -> {
            listeners.add(listener);
            listener.init(context);
        });

        ObjCObject proxy = Rococoa.proxy(new WorkspaceObserver());
        Selector sel1 = Foundation.selector("applicationWasActivated:");
        Selector sel2 = Foundation.selector("applicationWasDeactivated:");

        NSNotificationCenter notificationCenter = NSWorkspace.sharedWorkspace().notificationCenter();
        notificationCenter.addObserver_selector_name_object(proxy.id(), sel1, NSWorkspace.NSWorkspaceDidActivateApplicationNotification, null);
        notificationCenter.addObserver_selector_name_object(proxy.id(), sel2, NSWorkspace.NSWorkspaceDidDeactivateApplicationNotification, null);
    }

    /** @see "https://stackoverflow.com/a/33395422" */
    void process(NSRunningApplication a) {
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
    }

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
