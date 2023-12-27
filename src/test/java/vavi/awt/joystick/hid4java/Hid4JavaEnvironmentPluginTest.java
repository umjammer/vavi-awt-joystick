package vavi.awt.joystick.hid4java;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.osx.plugin.DualShock4Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import vavi.util.Debug;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


@EnabledIf("localPropertiesExists")
@PropsEntity(url = "file:local.properties")
class Hid4JavaEnvironmentPluginTest {

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
    @DisplayName("rumbler")
    void test1() throws Exception {
        Hid4JavaEnvironmentPlugin plugin = new Hid4JavaEnvironmentPlugin();
        Hid4JavaController controller = plugin.getController(vendorId, productId);
        controller.open();

        Random random = new Random();

        DualShock4Plugin.Report5 report = new DualShock4Plugin.Report5();
        report.smallRumble = 0;
        report.bigRumble = 0;
        report.ledRed = random.nextInt(255);
        report.ledGreen = random.nextInt(255);
        report.ledBlue = random.nextInt(255);
        report.flashLed1 = 80;
        report.flashLed2 = 80;
Debug.printf("R: %02x, G: %02x, B: %02x", report.ledRed, report.ledGreen, report.ledBlue);

        controller.output(report);

        plugin.close();
    }

    @Test
    @DisplayName("event")
    @EnabledIfSystemProperty(named = "vavi.test", matches = "ide")
    void test2() throws Exception {
        Hid4JavaEnvironmentPlugin plugin = new Hid4JavaEnvironmentPlugin();
        Controller controller = plugin.getController(vendorId, productId);
        Event event = new Event();
        controller.addInputEventListener(e -> {
            while (e.getNextEvent(event)) {
                System.out.println(event.getComponent().getName() + ": " + event.getValue());
            }
        });
        controller.open();

        new CountDownLatch(1).await();
    }
}