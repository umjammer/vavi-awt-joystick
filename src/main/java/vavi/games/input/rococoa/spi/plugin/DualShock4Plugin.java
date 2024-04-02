/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.rococoa.spi.plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Component.Identifier.Button;
import net.java.games.input.Controller;
import net.java.games.input.Rumbler;
import net.java.games.input.plugin.DualShock4PluginBase;
import org.rococoa.cocoa.gamecontroller.GCController;
import vavi.games.input.rococoa.spi.IdConvertible;
import vavi.games.input.rococoa.spi.RococoaRumbler;
import vavi.util.Debug;

import static net.java.games.input.Component.Identifier.Value;


/**
 * DualShock4Plugin.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-27 nsano initial version <br>
 * @see "https://www.psdevwiki.com/ps4/DS4-USB"
 */
public class DualShock4Plugin extends DualShock4PluginBase implements IdConvertible {

    /** @param object HidDevice */
    @Override
    public boolean match(Object object) {
        if (object instanceof GCController device) {
Debug.println(device.extendedGamepad().description());
            return device.extendedGamepad() != null && device.extendedGamepad().description().contains("GCDualShockGamepad");
        } else {
            return false;
        }
    }

    @Override
    public Collection<Component> getExtraComponents(Object object) {
        return Collections.emptyList();
    }

    @Override
    public Collection<Controller> getExtraChildControllers(Object object) {
        return Collections.emptyList();
    }

    @Override
    public Collection<Rumbler> getExtraRumblers(Object object) {
        return List.of(
                new RococoaRumbler(5, DualShock4Output.SMALL_RUMBLE),
                new RococoaRumbler(5, DualShock4Output.BIG_RUMBLE),
                new RococoaRumbler(5, DualShock4Output.LED_RED),
                new RococoaRumbler(5, DualShock4Output.LED_BLUE),
                new RococoaRumbler(5, DualShock4Output.LED_GREEN),
                new RococoaRumbler(5, DualShock4Output.FLASH_LED1),
                new RococoaRumbler(5, DualShock4Output.FLASH_LED2)
        );
    }

    private static final Map<String, Identifier> nameIdMap = new HashMap<>();

    static {
        // those are seemed to be dualshock4 specific
        nameIdMap.put("Square Button", Button._1);
        nameIdMap.put("Cross Button", Button._2);
        nameIdMap.put("Circle Button", Button._3);
        nameIdMap.put("Triangle Button", Button._4);
        nameIdMap.put("L1 Button", Button._5);
        nameIdMap.put("R1 Button", Button._6);
        nameIdMap.put("L2 Button", Button._7);
        nameIdMap.put("R2 Button", Button._8);
        nameIdMap.put("SHARE Button", Button._9);
        nameIdMap.put("OPTIONS Button", Button._10);
        nameIdMap.put("L3 Button", Button._11);
        nameIdMap.put("R3 Button", Button._12);
        nameIdMap.put("PS Button", Button._13);
        nameIdMap.put("Touchpad Button", Button._14);

        nameIdMap.put("Direction Pad", Axis.POV);

        nameIdMap.put("Left Stick (Horizontal)", Axis.X);
        nameIdMap.put("Left Stick (Vertical)", Axis.Y);
        nameIdMap.put("Right Stick (Horizontal)", Axis.RZ);
        nameIdMap.put("Right Stick (Vertical)", Axis.Z);

        nameIdMap.put("Left Stick", Value); // axis
        nameIdMap.put("Left Stick (Left)", Value);
        nameIdMap.put("Left Stick (Right)", Value);
        nameIdMap.put("Left Stick (Up)", Value);
        nameIdMap.put("Left Stick (Down)", Value);

        nameIdMap.put("Right Stick", Value); // axis
        nameIdMap.put("Right Stick (Left)", Value);
        nameIdMap.put("Right Stick (Right)", Value);
        nameIdMap.put("Right Stick (Up)", Value);
        nameIdMap.put("Right Stick (Down)", Value);

        nameIdMap.put("Touchpad (First Finger)", Value); // axis
        nameIdMap.put("Touchpad (First Finger) (Horizontal)", Axis.UNKNOWN);
        nameIdMap.put("Touchpad (First Finger) (Vertical)", Axis.UNKNOWN);
        nameIdMap.put("Touchpad (First Finger) (Left)", Value);
        nameIdMap.put("Touchpad (First Finger) (Right)", Value);
        nameIdMap.put("Touchpad (First Finger) (Up)", Value);
        nameIdMap.put("Touchpad (First Finger) (Down)", Value);

        nameIdMap.put("Touchpad (Second Finger)", Value); // value
        nameIdMap.put("Touchpad (Second Finger) (Horizontal)", Axis.UNKNOWN);
        nameIdMap.put("Touchpad (Second Finger) (Vertical)", Axis.UNKNOWN);
        nameIdMap.put("Touchpad (Second Finger) (Left)", Value);
        nameIdMap.put("Touchpad (Second Finger) (Right)", Value);
        nameIdMap.put("Touchpad (Second Finger) (Up)", Value);
        nameIdMap.put("Touchpad (Second Finger) (Down)", Value);

        nameIdMap.put("Direction Pad (Horizontal)", Axis.UNKNOWN);
        nameIdMap.put("Direction Pad (Vertical)", Axis.UNKNOWN);
        nameIdMap.put("Direction Pad (Left)", Value);
        nameIdMap.put("Direction Pad (Right)", Value);
        nameIdMap.put("Direction Pad (Up)", Value);
        nameIdMap.put("Direction Pad (Down)", Value);
    }

    @Override
    public Identifier normalize(String id) {
        return nameIdMap.get(id);
    }
}
