/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.rococoa.spi.plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Rumbler;
import net.java.games.input.plugin.DualShock4PluginBase;
import org.rococoa.cocoa.gamecontroller.GCController;
import vavi.games.input.rococoa.spi.RococoaRumbler;


/**
 * DualShock4Plugin.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-27 nsano initial version <br>
 * @see "https://www.psdevwiki.com/ps4/DS4-USB"
 */
public class DualShock4Plugin extends DualShock4PluginBase {

    /** @param object HidDevice */
    @Override
    public boolean match(Object object) {
        return object instanceof GCController device;
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
}
