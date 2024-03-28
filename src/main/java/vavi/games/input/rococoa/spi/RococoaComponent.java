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

    private final GCControllerElement element;

    /**
     * Protected constructor
     */
    protected RococoaComponent(GCControllerElement element) {
        super(element.localizedName(), nameIdMap.get(element.unmappedLocalizedName()));
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
