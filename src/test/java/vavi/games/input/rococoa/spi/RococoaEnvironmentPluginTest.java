/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.rococoa.spi;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.PollingController;
import org.junit.jupiter.api.Test;
import vavi.util.Debug;

import static org.junit.jupiter.api.Assertions.*;


/**
 * RococoaEnvironmentPluginTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-28 nsano initial version <br>
 */
class RococoaEnvironmentPluginTest {

    @Test
    void test() throws Exception {
        RococoaEnvironmentPlugin environment = new RococoaEnvironmentPlugin();
        RococoaController controller = environment.getControllers()[0];
Debug.println(controller);
        environment.close();
    }

    @Test
    void test2() throws Exception {
        Event event = new Event();

        // Get the available controllers
        Controller[] controllers = new RococoaEnvironmentPlugin().getControllers();
        assertNotEquals(controllers.length, 0);

        PollingController controller = (PollingController) controllers[0];
Debug.println(controller);

        CountDownLatch cdl = new CountDownLatch(1);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            // Remember to poll each one
            controller.poll();

            // Get the controllers event queue
            EventQueue queue = controller.getEventQueue();
//Debug.println(queue);

            // For each object in the queue
            while (queue.getNextEvent(event)) {
Debug.println(event.getComponent() + ": " + event.getValue());
            }
        }, 0, 17, TimeUnit.MILLISECONDS);

        cdl.await();
    }
}