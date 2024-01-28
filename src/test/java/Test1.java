/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.nio.file.Files;
import java.nio.file.Paths;

import net.java.games.input.Controller;
import net.java.games.input.usb.parser.HidParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.lwjgl.glfw.GLFW;
import purejavahidapi.HidDevice;
import purejavahidapi.HidDeviceInfo;
import purejavahidapi.PureJavaHidApi;
import vavi.awt.joystick.usb.UsbEnvironmentPlugin;
import vavi.util.Debug;
import vavi.util.StringUtil;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


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
    void test6() throws Exception {
        int a = 0x1234567;
        int b = Integer.reverseBytes(a);
Debug.printf("reverseBytes: %08x", b);
        assertEquals(0x67452301, b);

        a = 0x12345F7;
        b = Integer.reverseBytes(a);
Debug.printf("reverseBytes: %08x", b);
        assertEquals(0xF7452301, b);
    }
}
