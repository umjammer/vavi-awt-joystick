/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;
import net.java.games.input.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.lwjgl.glfw.GLFW;
import purejavahidapi.HidDevice;
import purejavahidapi.HidDeviceInfo;
import purejavahidapi.PureJavaHidApi;
import vavi.awt.joystick.hid4java.Hid4JavaController;
import vavi.awt.joystick.hid4java.Hid4JavaEnvironmentPlugin;
import vavi.awt.joystick.usb.UsbEnvironmentPlugin;
import vavi.hid.parser.HidParser;
import vavi.util.Debug;
import vavi.util.StringUtil;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


/**
 * Test1.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2023-05-09 nsano initial version <br>
 */
@EnabledIf("localPropertiesExists")
@PropsEntity(url = "file:local.properties")
public class Test1 {

    static boolean localPropertiesExists() {
        return Files.exists(Paths.get("local.properties"));
    }

    @Property(name = "mid")
    String mid;
    @Property(name = "pid")
    String pid;

    int vendorId;
    int productId;

    @BeforeEach
    void setup() throws Exception {
        if (localPropertiesExists()) {
            PropsEntity.Util.bind(this);

            vendorId = Integer.decode(mid);
            productId = Integer.decode(pid);
        }
    }

    @Test
    @Disabled("java.usb over libusb cannot detect dualshock4")
    void test1() throws Exception {
        Controller controller = new UsbEnvironmentPlugin().getController(vendorId, productId);
    }

    @Test
    @Disabled("lwjgl over libusb cannot detect dualshock4")
    void test2() throws Exception {
        GLFW.glfwInit();
        for (int jid = GLFW.GLFW_JOYSTICK_1; jid <= GLFW.GLFW_JOYSTICK_LAST; jid++) {
            if (GLFW.glfwJoystickPresent(jid)) {
Debug.println(jid);
            }
        }
        GLFW.glfwTerminate();
    }

    @Test
    @Disabled("cannot parse")
    @DisplayName("PureJavaHidApi")
    void test4() throws Exception {
        HidDeviceInfo deviceInfo = PureJavaHidApi.enumerateDevices().stream()
                .filter(d -> d.getVendorId() == 0x54c && d.getProductId() == 0x9cc)
                .findFirst().get();
        HidDevice device = PureJavaHidApi.openDevice(deviceInfo);
        device.open();

Debug.printf("device '%s' ----", device.getHidDeviceInfo().getProductString());
        byte[] data = new byte[132];
        int len = device.getFeatureReport(2, data, data.length);
Debug.printf("getFeatureReport: len: %d", len);
        if (len > 0) {
Debug.printf("getFeatureReport:%n%s", StringUtil.getDump(data, len));
            HidParser hidParser = new HidParser();
            hidParser.parse(data, len);
        }
    }

    @Test
    @DisplayName("hid4java spi directly")
    @EnabledIfSystemProperty(named = "vavi.test", matches = "ide")
    void test3() throws Exception {
        CountDownLatch cdl = new CountDownLatch(1);

        ControllerEnvironment ce = new Hid4JavaEnvironmentPlugin();
        ce.addControllerListener(new ControllerListener() {
            @Override
            public void controllerRemoved(ControllerEvent ev) {
Debug.println("➖ controllerRemoved: " + ev.getController());
            }

            @Override
            public void controllerAdded(ControllerEvent ev) {
Debug.println("➕ controllerAdded: " + ev.getController());
            }
        });

        Hid4JavaController controller = Arrays.stream(ce.getControllers())
                .filter(c -> c instanceof Hid4JavaController)
                .map(c -> (Hid4JavaController) c)
                .filter(c -> c.getVendorId() == vendorId && c.getProductId() == productId)
                .findFirst().get();

        // Create an event object for the underlying plugin to populate
        Event event = new Event();

        controller.addInputEventListener(e -> {

            // For each object in the queue
            while (e.getNextEvent(event)) {

                // Create a string buffer and put in it, the controller name,
                // the time stamp of the event, the name of the component
                // that changed and the new value.
                //
                // Note that the timestamp is a relative thing, not
                // absolute, we can tell what order events happened in
                // across controllers this way. We can not use it to tell
                // exactly *when* an event happened just the order.
                StringBuilder sb = new StringBuilder(controller.getName());
                sb.append(" at ");
                sb.append(event.getNanos()).append(", ");
                Component component = event.getComponent();
                sb.append(component.getName()).append(" changed to ");
                float value = event.getValue();

                // Check the type of the component and display an
                // appropriate value
                if (component.isAnalog()) {
                    sb.append(value);
                } else {
                    if (value == 1.0f) {
                        sb.append("On");
                    } else {
                        sb.append("Off");
                    }
                }
                System.out.println(sb);
            }
        });

        controller.open();

        new CountDownLatch(1).await();
    }

    @Test
    @DisplayName("hid4java spi directly")
    void test5() throws Exception {
        CountDownLatch cdl = new CountDownLatch(1);

        ControllerEnvironment ce = new Hid4JavaEnvironmentPlugin();
        ce.addControllerListener(new ControllerListener() {
            @Override
            public void controllerRemoved(ControllerEvent ev) {
Debug.println("➖ controllerRemoved: " + ev.getController());
            }

            @Override
            public void controllerAdded(ControllerEvent ev) {
Debug.println("➕ controllerAdded: " + ev.getController());
            }
        });

        Hid4JavaController controller = Arrays.stream(ce.getControllers())
                .filter(c -> c instanceof Hid4JavaController)
                .map(c -> (Hid4JavaController) c)
                .filter(c -> c.getVendorId() == vendorId && c.getProductId() == productId)
                .findFirst().get();

Debug.println("controller: " + controller);
    }
}
