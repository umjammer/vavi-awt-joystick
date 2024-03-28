/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.joysticklet;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.io.InputStream;
import java.util.Properties;

import net.java.games.input.Component;
import net.java.games.input.Event;
import vavi.awt.joystick.Joysticklet;


/**
 * 6 button unknown gamepad which i have.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020421 nsano initial version <br>
 *          0.10 020914 nsano add command <br>
 */
public class JoySticklet_1118_505 extends Joysticklet {

    private final Label up    = new Label();
    private final Label right = new Label();
    private final Label down  = new Label();
    private final Label left  = new Label();

    private final Label[] b = new Label[] {
        new Label(), new Label(), new Label(),
        new Label(), new Label(), new Label()
    };

    private final Executable leftAction;
    private final Executable rightAction;
    private final Executable upAction;
    private final Executable downAction;
    private final Executable b1Action;
    private final Executable b2Action;
    private final Executable b3Action;
    private final Executable b4Action;
    private final Executable b5Action;
    private final Executable b6Action;

    private static final Color onColor2 = Color.red;
    private static final Color onColor1 = Color.yellow;
    private static final Color offColor2 = Color.red.darker().darker().darker();
    private static final Color offColor1 = Color.yellow.darker().darker().darker();

    /* */
    {
        try {
            Properties props = new Properties();
            InputStream is = JoySticklet_1118_505.class.getResourceAsStream("JoySticklet_1118_505.properties");
            props.load(is);

            rightAction = new ShellCommand(props.getProperty("action.right"));
            leftAction  = new ShellCommand(props.getProperty("action.left"));
            upAction    = new ShellCommand(props.getProperty("action.up"));
            downAction  = new ShellCommand(props.getProperty("action.down"));
            b1Action = new ShellCommand(props.getProperty("action.b1"));
            b2Action = new ShellCommand(props.getProperty("action.b2"));
            b3Action = new ShellCommand(props.getProperty("action.b3"));
            b4Action = new ShellCommand(props.getProperty("action.b4"));
            b5Action = new ShellCommand(props.getProperty("action.b5"));
            b6Action = new ShellCommand(props.getProperty("action.b6"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Construct 6 button gamepad Joysticlet.
     */
    public JoySticklet_1118_505() {
        super("Ms:1118:505");
//System.err.println(buttonNumber);
        setLayout(null);
        setSize(640, 400);

        Panel panel = new Panel(new GridLayout(2, 6, 5, 5));
        panel.setLayout(null);
        panel.setSize(120, 120);
        panel.setLocation(80, 120);

        panel.add(up);
        up.setSize(20, 40);
        up.setLocation(50, 10);
        up.setBackground(offColor1);
        panel.add(right);
        right.setSize(40, 20);
        right.setLocation(70, 50);
        right.setBackground(offColor1);
        panel.add(down);
        down.setSize(20, 40);
        down.setLocation(50, 70);
        down.setBackground(offColor1);
        panel.add(left);
        left.setSize(40, 20);
        left.setLocation(10, 50);
        left.setBackground(offColor1);

        add(panel);

        panel = new Panel(new GridLayout(2, 6, 5, 5));
        panel.setSize(200, 50);
        panel.setLocation(290, 170);

        int[] o = new int[] { 0, 1, 4, 2, 3, 5 };
        for (int i = 0; i < 6; i++) {
            panel.add(new Label("B" + (o[i] + 1)));
            panel.add(b[o[i]]);
            b[o[i]].setBackground(offColor2);
        }

        add(panel);

        Event ev = new Event();
        controller.addInputEventListener(e -> {
//Debug.printf("%04x%n", ev.buttons);
//Debug.println(StringUtil.toBits(ev.buttons));
            while (e.getNextEvent(ev)) {
//System.err.println(toHex4(ev.buttons));

                for (int i = 0; i < 6; i++) {
                    if (ev.getComponent().getIdentifier() == Component.Identifier.Button.valueOf(String.valueOf(i))) {
                        if (ev.getValue() != 0) {
                            b[i].setBackground(onColor2);
                        } else {
                            b[i].setBackground(offColor2);
                        }
                    }
                }

                int px = 0;
                if (ev.getComponent().getIdentifier() ==  Component.Identifier.Axis.X)
                    px = (int) (ev.getValue() * 100 / 65535);
                int py = 0;
                if (ev.getComponent().getIdentifier() ==  Component.Identifier.Axis.Y)
                    py = (int) (ev.getValue() * 100 / 65535);
                if (px < 30) {
                    left.setBackground(onColor1);
                } else {
                    left.setBackground(offColor1);
                }
                if (px > 80) {
                    right.setBackground(onColor1);
                } else {
                    right.setBackground(offColor1);
                }
                if (py < 30) {
                    up.setBackground(onColor1);
                } else {
                    up.setBackground(offColor1);
                }
                if (py > 80) {
                    down.setBackground(onColor1);
                } else {
                    down.setBackground(offColor1);
                }

                boolean left = false;
                boolean right = false;
                boolean up = false;
                boolean down = false;

                if (ev.getComponent().getIdentifier() ==  Component.Identifier.Axis.X)
                    px = (int) (ev.getValue() * 100 / 65535);
                if (ev.getComponent().getIdentifier() ==  Component.Identifier.Axis.Y)
                    py = (int) (ev.getValue() * 100 / 65535);
                if (px < 30) {
                    left = true;
                } else {
                    if (left) {
                        leftAction.exec();
                    }
                    left = false;
                }
                if (px > 80) {
                    right = true;
                } else {
                    if (right) {
                        rightAction.exec();
                    }
                    right = false;
                }
                if (py < 30) {
                    up = true;
                } else {
                    if (up) {
                        upAction.exec();
                    }
                    up = false;
                }
                if (py > 80) {
                    down = true;
                } else {
                    if (down) {
                        downAction.exec();
                    }
                    down = false;
                }

//                switch (ev.target) {
//                case 0:
//                    b1Action.exec();
//                    break;
//                case 1:
//                    b2Action.exec();
//                    break;
//                case 2:
//                    b3Action.exec();
//                    break;
//                case 3:
//                    b4Action.exec();
//                    break;
//                case 4:
//                    b5Action.exec();
//                    break;
//                case 5:
//                    b6Action.exec();
//                    break;
//                }
            }
        });
    }
}

/* */
