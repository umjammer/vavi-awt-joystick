import java.io.IOException;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.osx.plugin.DualShock4Plugin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vavi.awt.joystick.hid4java.Hid4JavaController;
import vavi.awt.joystick.hid4java.Hid4JavaInputEvent;


/**
 * This class shows how to use the event queue system in JInput. It will show
 * how to get the controllers, how to get the event queue for a controller, and
 * how to read and process events from the queue.
 *
 * @author Endolf
 */
public class ReadAllEvents {

    static {
        // to avoid conflict with jinput's spis, currently we have hid4java's spis.
        System.setProperty("net.java.games.input.ControllerEnvironment.excludes", "net.java.games.input");
    }

    @Test
    @DisplayName("jinput sample powered by hid4java spi")
    void readAllEvents() throws Exception {
        /* Get the available controllers */
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        if (controllers.length == 0) {
            throw new IllegalStateException("Found no controllers.");
        }
        for (Controller controller : controllers) {
            // hid4java controller needs to start event listener system
            if (!controller.isOpen()) {
                controller.addInputEventListener(e -> {
                    if (e instanceof Hid4JavaInputEvent he) {
                        DualShock4Plugin.display(he.getData());
                    }
                });
                controller.open();
            }
        }
        Thread.sleep(180 * 1000);
    }
}
