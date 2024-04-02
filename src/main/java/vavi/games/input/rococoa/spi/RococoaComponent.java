/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.rococoa.spi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Component.Identifier.Button;
import net.java.games.input.PollingComponent;
import net.java.games.input.WrappedComponent;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.gamecontroller.GCControllerAxisInput;
import org.rococoa.cocoa.gamecontroller.GCControllerButtonInput;
import org.rococoa.cocoa.gamecontroller.GCControllerDirectionPad;
import org.rococoa.cocoa.gamecontroller.GCControllerElement;
import org.rococoa.cocoa.gamecontroller.GCControllerTouchpad;
import vavi.util.Debug;

import static net.java.games.input.Component.Identifier.Value;


/**
 * RococoaComponent.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-27 nsano initial version <br>
 */
public class RococoaComponent extends PollingComponent implements WrappedComponent<GCControllerElement> {

    private final GCControllerElement element;

    /**
     * Protected constructor
     */
    protected RococoaComponent(GCControllerElement element, Identifier id) {
        super(id.getName(), id);
        this.element = element;
    }

    @Override
    protected float poll() throws IOException {
//Debug.println(element + ", " + getIdentifier().getName());
        if (element instanceof GCControllerDirectionPad directionPad) {
            return 0; // TODO
        } else if (element instanceof GCControllerButtonInput buttonInput) {
            GCControllerButtonInput input = Rococoa.cast(element, GCControllerButtonInput.class);
            return buttonInput.value();
        } else if (element instanceof GCControllerAxisInput axisInput) {
            return axisInput.value();
        } else if (element instanceof GCControllerTouchpad touchpad) {
            return 0;
        } else {
            //assert false : "unsupported: " + getIdentifier();
            return 0;
        }
    }

    @Override
    public boolean isRelative() {
        return false;
    }

    @Override
    public GCControllerElement getWrappedObject() {
        return element;
    }
}
