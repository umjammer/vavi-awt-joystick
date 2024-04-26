/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import com.sun.tools.attach.VirtualMachine;
import net.java.games.input.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.lwjgl.glfw.GLFW;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSArray;
import vavi.awt.joystick.usb.UsbEnvironmentPlugin;
import vavi.games.input.helper.JavaVMAppInfo;
import vavi.games.input.listener.MinecraftListener;
import vavi.util.Debug;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.rococoa.cocoa.coregraphics.CoreGraphicsLibrary.kCGNullWindowID;
import static org.rococoa.cocoa.coregraphics.CoreGraphicsLibrary.kCGWindowListOptionOnScreenOnly;
import static org.rococoa.cocoa.coregraphics.CoreGraphicsLibrary.library;


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

    @Test
    void test7() throws Exception {
        NSArray array = Rococoa.toNSArray(library.CGWindowListCopyWindowInfo(kCGWindowListOptionOnScreenOnly, kCGNullWindowID).getPointer());
Debug.println(array.get(0).getClass());
    }

    @Test
    @EnabledIfSystemProperty(named = "vavi.test", matches = "ide")
    public void test8() throws Exception {
        VirtualMachine.list().forEach(System.err::println);

        Properties props = new Properties();
        props.load(MinecraftListener.class.getResourceAsStream("/minecraft.properties"));
        String[] mcLaunchers = props.stringPropertyNames().toArray(String[]::new);
Debug.println(JavaVMAppInfo.getPidByMainClassName(mcLaunchers));
    }
}
