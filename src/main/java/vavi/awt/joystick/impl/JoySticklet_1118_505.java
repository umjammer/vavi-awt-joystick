/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.impl;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.io.InputStream;
import java.util.Properties;

import vavi.awt.joystick.GamePortAdapter;
import vavi.awt.joystick.GamePortEvent;
import vavi.awt.joystick.GamePortListener;
import vavi.awt.joystick.JoySticklet;


/**
 * 6 ボタン ゲームパッド
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020421 nsano initial version <br>
 *          0.10 020914 nsano add command <br>
 */
public class JoySticklet_1118_505 extends JoySticklet {

    private Label up    = new Label();
    private Label right = new Label();
    private Label down  = new Label();
    private Label left  = new Label();

    private Label[] b = new Label[] {
        new Label(), new Label(), new Label(),
        new Label(), new Label(), new Label()
    };

    private Executable leftAction;
    private Executable rightAction;
    private Executable upAction;
    private Executable downAction;
    private Executable b1Action;
    private Executable b2Action;
    private Executable b3Action;
    private Executable b4Action;
    private Executable b5Action;
    private Executable b6Action;

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
     * 6 ボタン ゲームパッドのジョイスティックレットを構築します．
     */
    public JoySticklet_1118_505() {
        super(1118, 505);
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

        final int[] o = new int[] { 0, 1, 4, 2, 3, 5 };
        for (int i = 0; i < 6; i++) {
            panel.add(new Label("B" + (o[i] + 1)));
            panel.add(b[o[i]]);
            b[o[i]].setBackground(offColor2);
        }

        add(panel);

        addGamePortListener(stateListener);
        addGamePortListener(actionListener);
    }

    /** */
    GamePortListener stateListener = new GamePortAdapter() {
        public void buttonChange(GamePortEvent ev) {
//System.err.println(toHex4(ev.buttons));
            for (int i = 0; i < 6; i++) {
                if ((ev.buttons & (0x0001 << i)) != 0) {
                    b[i].setBackground(onColor2);
                } else {
                    b[i].setBackground(offColor2);
                }
            }
        }
        public void positionChange(GamePortEvent ev) {
            int px = ev.xPos * 100 / 65535;
            int py = ev.yPos * 100 / 65535;
            if (px < 30) {
                left .setBackground(onColor1);
            } else {
                left .setBackground(offColor1);
            }
            if (px > 80) {
                right.setBackground(onColor1);
            } else {
                right.setBackground(offColor1);
            }
            if (py < 30) {
                up  .setBackground(onColor1);
            } else {
                up  .setBackground(offColor1);
            }
            if (py > 80) {
                down.setBackground(onColor1);
            } else {
                down.setBackground(offColor1);
            }
        }
        public void buttonReleased(GamePortEvent ev) {
System.err.println(ev.target);
            switch (ev.target) {
            case 0:
                break;
            }
        }
    };

    /** */
    GamePortListener actionListener = new GamePortAdapter() {
        private boolean left = false;
        private boolean right = false;
        private boolean up = false;
        private boolean down = false;

        public void positionChange(GamePortEvent ev) {
            int px = ev.xPos * 100 / 65535;
            int py = ev.yPos * 100 / 65535;
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
        }
        public void buttonReleased(GamePortEvent ev) {
            switch (ev.target) {
            case 0:
                b1Action.exec();
                break;
            case 1:
                b2Action.exec();
                break;
            case 2:
                b3Action.exec();
                break;
            case 3:
                b4Action.exec();
                break;
            case 4:
                b5Action.exec();
                break;
            case 5:
                b6Action.exec();
                break;
            }
        }
    };
}

/* */
