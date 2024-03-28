/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.rococoa.spi;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import com.sun.jna.Callback;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerListenerSupport;
import net.java.games.input.DeviceSupportPlugin;
import net.java.games.input.Rumbler;
import net.java.games.input.usb.HidControllerEnvironment;
import org.rococoa.Foundation;
import org.rococoa.ObjCObject;
import org.rococoa.Rococoa;
import org.rococoa.Selector;
import org.rococoa.cocoa.foundation.NSNotification;
import org.rococoa.cocoa.foundation.NSNotificationCenter;
import org.rococoa.cocoa.gamecontroller.GCController;
import org.rococoa.cocoa.gamecontroller.GCControllerAxisInput;
import org.rococoa.cocoa.gamecontroller.GCControllerButtonInput;
import org.rococoa.cocoa.gamecontroller.GCControllerDirectionPad;
import org.rococoa.cocoa.gamecontroller.GCControllerElement;
import org.rococoa.cocoa.gamecontroller.GCControllerTouchpad;
import vavi.util.Debug;


/**
 * The CGController ControllerEnvironment.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-27 nsano initial version <br>
 */
public final class RococoaEnvironmentPlugin extends ControllerListenerSupport implements ControllerEnvironment, Closeable {

    /** */
    private List<RococoaController> controllers;

    private class RococoaObserver implements Callback {

        public void controllerDidConnect(NSNotification notification) {
            GCController controller = Rococoa.cast(notification.object(), GCController.class);
Debug.println("controllerDidConnect: " + controller);
            try {
                attach(controller);
            } catch (IOException e) {
            }
        }

        public void controllerDidDisconnect(NSNotification notification) {
            GCController controller = Rococoa.cast(notification.object(), GCController.class);
Debug.println("controllerDidDisconnect");
            detach(controller);
        }
    }

    /** */
    private void startListening() {
        ObjCObject proxy = Rococoa.proxy(new RococoaObserver());
        Selector sel1 = Foundation.selector("controllerDidConnect:");
        Selector sel2 = Foundation.selector("controllerDidDisconnect:");

        NSNotificationCenter notificationCenter = NSNotificationCenter.CLASS.defaultCenter();
        notificationCenter.addObserver_selector_name_object(proxy.id(), sel1, GCController.GCControllerDidConnectNotification, null);
        notificationCenter.addObserver_selector_name_object(proxy.id(), sel2, GCController.GCControllerDidDisconnectNotification, null);
    }

    private void enumerate() throws IOException {
        boolean r = isSupported(); // don't touch, instantiates hidServices
Debug.println(Level.FINE, "isSupported: " + r);
        controllers = new ArrayList<>();
Debug.println(Level.FINE, "devices: " + GCController.controllers().size());
        GCController.controllers().forEach(controller -> {
            try {
                attach(controller);
            } catch (IOException e) {
                Debug.printStackTrace(e);
            }
        });
    }

    /** */
    private RococoaController attach(GCController device) throws IOException {

        List<Component> components = new ArrayList<>();
        List<Controller> children = new ArrayList<>();
        List<Rumbler> rumblers = new ArrayList<>();

        // extra elements by plugin
        for (DeviceSupportPlugin plugin : DeviceSupportPlugin.getPlugins()) {
//Debug.println(Level.FINER, "plugin: " + plugin + ", " + plugin.match(device));
            if (plugin.match(device)) {
//Debug.println(Level.FINE, "@@@ plugin for extra: " + plugin.getClass().getName());
                components.addAll(plugin.getExtraComponents(device));
                children.addAll(plugin.getExtraChildControllers(device));
                rumblers.addAll(plugin.getExtraRumblers(device));
            }
        }

        device.extendedGamepad().allButtons().allObjects().toList().forEach(o -> {
            GCControllerButtonInput element = Rococoa.cast(o, GCControllerButtonInput.class);
            RococoaComponent component = new RococoaComponent(element);
            components.add(component);
        });
        device.extendedGamepad().allAxes().allObjects().toList().forEach(o -> {
            GCControllerAxisInput element = Rococoa.cast(o, GCControllerAxisInput.class);
            RococoaComponent component = new RococoaComponent(element);
            components.add(component);
        });
        device.extendedGamepad().allDpads().allObjects().toList().forEach(o -> {
            GCControllerDirectionPad element = Rococoa.cast(o, GCControllerDirectionPad.class);
            RococoaComponent component = new RococoaComponent(element);
            components.add(component);
        });
        device.extendedGamepad().allTouchpads().allObjects().toList().forEach(o -> {
            GCControllerTouchpad element = Rococoa.cast(o, GCControllerTouchpad.class);
            RococoaComponent component = new RococoaComponent(element);
            components.add(component);
        });

        RococoaController controller = new RococoaController(device,
                components.toArray(Component[]::new),
                children.toArray(Controller[]::new),
                rumblers.toArray(Rumbler[]::new));
        controllers.add(controller);
Debug.printf(Level.FINE, "    components: %d, %s", components.size(), components);
//Debug.printf(Level.FINE, "    children: %d", children.size());
Debug.printf(Level.FINE, "    rumblers: %d, %s", rumblers.size(), rumblers);
        return controller;
    }

    /** */
    @SuppressWarnings("WhileLoopReplaceableByForEach") // for remove
    private RococoaController detach(GCController controller) {
        Iterator<RococoaController> i = controllers.iterator();
        while (i.hasNext()) {
            RococoaController c = i.next();
            if (controller.id().equals(c.getDevice().id()))
                controllers.remove(c);
        }
        return null;
    }

    @Override
    public RococoaController[] getControllers() {
        if (controllers == null) {
            try {
                enumerate();
                startListening();
            } catch (IOException e) {
Debug.printStackTrace(e);
                return new RococoaController[0];
            }
        }
        return controllers.toArray(RococoaController[]::new);
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public void close() {
    }
}
