/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.ms;

import java.util.ArrayList;
import java.util.List;

import com.ms.directX.DirectInput;
import com.ms.directX.JoyCaps;
import com.ms.directX.JoyInfo;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Rumbler;
import vavi.util.Debug;


/**
 * DirectInputControllerEnvironment.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2023-09-18 nsano initial version <br>
 */
public final class DirectInputControllerEnvironment extends ControllerEnvironment {

    /** */
    private final List<DirectInputController> controllers = new ArrayList<>();

    /** create all DirectInputController objects */
    public DirectInputControllerEnvironment() {
        if (directInput == null) {
//System.err.println("num:\t" + di.getNumDevs());
            try {
                directInput = new DirectInput();

                for (int i = 0; i < directInput.getNumDevs(); i++) {
                    try {
                        // vxd:	the name of the virtual device driver.
                        // name:	The joystick model name.
                        // key:	the name of the registry key.
                        directInput.getDevCapsOEMVxd(i);
                        System.err.println("---- device no: " + i + " ----");
                        System.err.println("vxd:\t" + directInput.getDevCapsOEMVxd(i));
                        System.err.println("name:\t" + directInput.getDevCapsProductName(i));
                        System.err.println("key:\t" + directInput.getDevCapsRegKey(i));

                        Component[] components = new Component[] {
                        // Current button number that is pressed.
//                         buttonNumber;
                        /*
                         * Current state of the 32 joystick buttons.
                         * The value of this member can be set to any combination
                         * of JOY_BUTTON# flags, where n is a value ranging from
                         * 1 to 32. Each value corresponds to the button that is
                         * pressed.
                         */
//                         buttons;
                        /*
                         * Flags that indicate if information returned in this
                         * class is valid. Members that do not contain valid
                         * information are set to 0. These flags can be one or
                         * more of the JOY_RETURN* and JOY_CAL_ types.
                         */
//                         flags;
                        /*
                         * Current position of the Point-Of-View control.
                         * Values for this member range from 0 to 35,900.
                         * These values represent each views angle, in degrees,
                         * multiplied by 100.
                         */
//                         pov;
                        // Current position of the rudder, or fourth joystick axis.
//                         rPos;
                        // Current positions of the fifth joystick axis.
//                         uPos;
                        // Current positions of the sixth joystick axis.
//                         vPos;
                        //	Current x-coordinate.
//                         xPos;
                        //	Current y-coordinate.
//                         yPos;
                        // Current z coordinate.
//                         zPos;
                        };




                        DirectInputController device = new DirectInputController(
                                i,
                                0, 0, components, new Controller[]{}, new Rumbler[]{});

                        /*
                         * caps:      Joystick capabilities. These may be one of the following:
                         *  JOYCAPS_HASPOV  The joystick has Point-Of-View information.
                         *  JOYCAPS_HASR    The joystick has rudder (fourth axis) information.
                         *  JOYCAPS_HASU    The joystick has u-coordinate information.
                         *  JOYCAPS_HASV    The joystick has v-coordinate information.
                         *  JOYCAPS_HASZ    The joystick has z-coordinate information
                         *  JOYCAPS_POV4DIR The joystick Point-Of-View supports
                         *                  discrete values.
                         *  JOYCAPS_POVCTS  The joystick Point-Of-View supports
                         *                  continuous degree bearings.
                         * maxAxes:    The maximum number of axes supported by the joystick.
                         * maxButtons: The maximum number of buttons a joystick supports.
                         * mid:        Manufacturer identifier.
                         * numAxes:    The number of axes currently supported by the joystick.
                         * numButtons: The current number of joystick buttons in use.
                         * periodMax:  The maximum polling frequency supported
                         *             once an application has captured a joystick.
                         * periodMin:  The minimum polling frequency supported
                         *             once an application has captured a joystick.
                         * pid:        Product identifier.
                         * rMax:       Maximum r-axis (the fourth, or rudder-axis) coordinate.
                         * rMin:       Minimum r-axis (the fourth, or rudder-axis) coordinate.
                         * uMax:       Maximum u-axis (fifth axis) coordinate.
                         * uMin:       Minimum u-axis (fifth axis) coordinate.
                         * vMax:       Maximum v-axis (sixth axis) coordinate.
                         * vMin:       Minimum v-axis (sixth axis) coordinate.
                         * xMax:       Maximum x-axis coordinate.
                         * xMin:       Minimum x-axis coordinate.
                         * yMax:       Maximum y-axis coordinate.
                         * yMin:       Minimum y-axis coordinate.
                         * zMax:       Maximum z-axis coordinate.
                         * zMin:       Minimum z-axis coordinate.
                         */
                        JoyCaps joyCaps = new JoyCaps();
                        directInput.getDevCaps(i, joyCaps);
                        //System.err.println("---- " + di.getDevCapsProductName(i) + " ----");
                        System.err.println("caps:\t\t" + joyCaps.caps);
                        System.err.println("maxAxes:\t" + joyCaps.maxAxes);
                        System.err.println("maxButtons:\t" + joyCaps.maxButtons);
                        System.err.println("mid:\t\t" + (int) joyCaps.mid);
                        System.err.println("numAxes:\t" + joyCaps.numAxes);
                        System.err.println("numButtons:\t" + joyCaps.numButtons);
                        System.err.println("periodMax:\t" + joyCaps.periodMax);
                        System.err.println("periodMin:\t" + joyCaps.periodMin);
                        System.err.println("pid:\t\t" + (int) joyCaps.pid);
                        System.err.println("rMax:\t\t" + joyCaps.rMax);
                        System.err.println("rMin:\t\t" + joyCaps.rMin);
                        System.err.println("uMax:\t\t" + joyCaps.uMax);
                        System.err.println("uMin:\t\t" + joyCaps.uMin);
                        System.err.println("vMax:\t\t" + joyCaps.vMax);
                        System.err.println("vMin:\t\t" + joyCaps.vMin);
                        System.err.println("xMax:\t\t" + joyCaps.xMax);
                        System.err.println("xMin:\t\t" + joyCaps.xMin);
                        System.err.println("yMax:\t\t" + joyCaps.yMax);
                        System.err.println("yMin:\t\t" + joyCaps.yMin);
                        System.err.println("zMax:\t\t" + joyCaps.zMax);
                        System.err.println("zMin:\t\t" + joyCaps.zMin);

                        controllers.add(device);
                    } catch (Exception e) {
Debug.printStackTrace(e);
                    }
                }
            } catch (Exception e) {
Debug.printStackTrace(e);
            }
        }
    }

    @Override
    public Controller[] getControllers() {
        return controllers.toArray(Controller[]::new);
    }

    @Override
    public boolean isSupported() {
        return directInput != null;
    }

    /** Direct Input */
    private static DirectInput directInput;










    /** Returns game ports. */
    public static void getPos(int id, JoyInfo joyInfo) {
        directInput.getPos(id, joyInfo);
    }

    public Controller getController(int mid, int pid) {
        for (DirectInputController controller : controllers) {
            if (controller.getVendorId() == mid && controller.getProductId() == pid) {
                return controller;
            }
        }
        throw new IllegalArgumentException("no device (mid=" + mid + ",pid=" + pid + ")");
    }
}
