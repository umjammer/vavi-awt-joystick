package vavi.awt.joystick;
/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.awt.Panel;

import vavi.awt.joystick.ms.GamePort;


/**
 * Joysticlet
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020421 nsano initial version <br>
 *          0.10 020914 nsano add command class <br>
 */
public abstract class JoySticklet extends Panel {

    /** */
    private GamePort gp;

    /**
     * @param mid
     * @param pid
     * @throws IllegalStateException 対応するデバイスがない場合
     */
    protected JoySticklet(int mid, int pid) {
        this.gp = GamePort.getGamePort(mid, pid);
    }

    /** */
    protected void addGamePortListener(GamePortListener l) {
        gp.addGamePortListener(l);
    }

    //-------------------------------------------------------------------------

    /** */
    protected interface Executable {
        void exec();
    }

    /** */
    protected class Command implements Executable {
        public void exec() {
        }
    }

    /** */
    protected class ShellCommand extends Command {
        private String commandLine;
        public ShellCommand(String commandLine) {
            this.commandLine = commandLine;
System.err.println(this.commandLine);
        }
        public void exec() {
            try {
                Runtime.getRuntime().exec(commandLine);
            } catch (Exception e) {
System.err.println(e);
            }
        }
    }
}

/* */
