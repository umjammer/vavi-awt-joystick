/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick;

import java.awt.Panel;
import java.util.Arrays;
import java.util.Optional;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


/**
 * Joysticlet
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020421 nsano initial version <br>
 *          0.10 020914 nsano add command class <br>
 */
public abstract class Joysticklet extends Panel {

    /** */
    protected final Controller controller;

    /**
     * @param name controller mame
     * @throws IllegalStateException no such controller
     */
    protected Joysticklet(String name) {
        Optional<Controller> oc = Arrays.stream(ControllerEnvironment.getDefaultEnvironment().getControllers())
                .filter(c -> c.getName().equals(name))
                .findFirst();
        if (oc.isPresent()) {
            controller = oc.get();
        } else {
            throw new IllegalStateException(name);
        }
    }

    //-------------------------------------------------------------------------

    /** */
    protected interface Executable {
        void exec();
    }

    /** */
    protected static class Command implements Executable {
        public void exec() {
        }
    }

    /** */
    protected static class ShellCommand extends Command {
        private String commandLine;
        public ShellCommand(String commandLine) {
            this.commandLine = commandLine;
System.err.println(this.commandLine);
        }
        public void exec() {
            try {
                Runtime.getRuntime().exec(commandLine.split("\\s+"));
            } catch (Exception e) {
System.err.println(e);
            }
        }
    }
}

/* */
