/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.joysticklet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.java.games.input.Component;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import vavi.awt.joystick.Joysticklet;


/**
 * PSX-USB Adapter
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020421 nsano initial version <br>
 */
public class JoySticklet_2883_1 extends Joysticklet {

    /**
     * position display
     */
    static class Panel3 extends Panel {

        int px = 50;
        int py = 50;

        {
            setBackground(Color.yellow);
        }

        public void paint(Graphics g) {
            super.paint(g);
            int width = getSize().width;
            int height = getSize().height;
            g.fillRect(width * px / 100 - 5, height * py / 100 - 5, 10, 10);
            g.drawString(px + ", " + py, 10, 10);
        }
    }

    /**
     * image panel
     */
    static class Panel2 extends Panel {

        Image image;

        Panel2(Image image) {
            this.image = image;
        }

        public void paint(Graphics g) {
            super.paint(g);
            if (image == null || !g.drawImage(image, 0, 0, this))
                g.drawString("image N/A", 10, 10);
        }

        public boolean imageUpdate(Image img, int infoflags,
                                   int x, int y, int width, int height) {
            if ((infoflags & ImageObserver.ALLBITS) == ImageObserver.ALLBITS) {
                repaint();
                return false;
            } else {
                return true;
            }
        }
    }

    MediaTracker tracker = new MediaTracker(this);

    /**
     * image utility
     */
    static class Image2 {

        String name;
        int x;
        int y;
        Panel2 panel;
    }

    static final String[] names = new String[] {
            "triangle", "circle", "eks", "rectangle",
            "l2", "r2", "l1", "r1",
            "select", "start", "l3", "r3",
            "up", "right", "down", "left",
            "l3up", "l3right", "l3down", "l3left",
            "r3up", "r3right", "r3down", "r3left",
            "ds", "ds2"
    };

    Image2[] images = new Image2[names.length];

    {
        try {
            Toolkit t = Toolkit.getDefaultToolkit();

            Properties props = new Properties();
            InputStream is = JoySticklet_2883_1.class.getResourceAsStream("JoySticklet_2883_1.properties");
            props.load(is);

            for (int i = 0; i < names.length; i++) {
                String name = names[i];

                images[i] = new Image2();
                images[i].name = name;

                Image image = t.getImage(JoySticklet_2883_1.class.getResource("images/" + name + ".gif"));
                images[i].panel = new Panel2(image);
                tracker.addImage(image, i);

                String value = props.getProperty("joystick." + name + ".x");
                images[i].x = Integer.parseInt(value);
                value = props.getProperty("joystick." + name + ".y");
                images[i].y = Integer.parseInt(value);
            }

            tracker.waitForAll();

            Panel panel = images[25].panel;
            Image image = images[25].panel.image;
            panel.setLayout(null);
            panel.setSize(image.getWidth(this), image.getHeight(this));
            for (int i = 0; i < names.length - 2; i++) {
                panel.add(images[i].panel);
                images[i].panel.setLocation(images[i].x, images[i].y);
                image = images[i].panel.image;
                images[i].panel.setSize(image.getWidth(this),
                        image.getHeight(this));
                images[i].panel.setVisible(false);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    Label rPos = new Label();
    Label xPos = new Label();
    Label yPos = new Label();
    Label zPos = new Label();

    Panel3 l3 = new Panel3();
    Panel3 r3 = new Panel3();

    public JoySticklet_2883_1() {
        super("Usb:2883:1");

        this.setLayout(null);
        this.add(images[25].panel);
        images[25].panel.setLocation(24, 16);

//        Panel panel = new Panel(new GridLayout(4, 2));
//        panel.add(new Label("xPos"));
//        panel.add(xPos);
//        panel.add(new Label("yPos"));
//        panel.add(yPos);
//        panel.add(new Label("rPos"));
//        panel.add(rPos);
//        panel.add(new Label("zPos"));
//        panel.add(zPos);
//        this.add(panel);
//        panel.setSize(200, 160);
//        panel.setLocation(360, 200);

        this.add(l3);
        l3.setSize(100, 100);
        l3.setLocation(360, 240);

        this.add(r3);
        r3.setSize(100, 100);
        r3.setLocation(480, 240);

        Event ev = new Event();
        controller.addInputEventListener(e -> {
//Debug.printf("%04x%n", ev.buttons);
//Debug.println(StringUtil.toBits(ev.buttons));
            while (e.getNextEvent(ev)) {
                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._0)
                    images[0].panel.setVisible(ev.getValue() != 0); // △
                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._1)
                    images[1].panel.setVisible(ev.getValue() != 0); // ○
                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._2)
                    images[2].panel.setVisible(ev.getValue() != 0); // ×
                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._3)
                    images[3].panel.setVisible(ev.getValue() != 0); // □

                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._4)
                    images[4].panel.setVisible(ev.getValue() != 0); // l2
                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._5)
                    images[5].panel.setVisible(ev.getValue() != 0); // r2
                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._6)
                    images[6].panel.setVisible(ev.getValue() != 0); // l1
                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._7)
                    images[7].panel.setVisible(ev.getValue() != 0); // r1

                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._8)
                    images[8].panel.setVisible(ev.getValue() != 0); // sl
                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._9)
                    images[9].panel.setVisible(ev.getValue() != 0); // st

                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._10)
                    images[10].panel.setVisible(ev.getValue() != 0); // l3
                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._11)
                    images[11].panel.setVisible(ev.getValue() != 0); // r3

                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._12)
                    images[12].panel.setVisible(ev.getValue() != 0); // ↑
                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._13)
                    images[13].panel.setVisible(ev.getValue() != 0); // →
                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._14)
                    images[14].panel.setVisible(ev.getValue() != 0); // ↓
                if (ev.getComponent().getIdentifier() == Component.Identifier.Button._15)
                    images[15].panel.setVisible(ev.getValue() != 0); // ←
/*
                xPos.setText(String.valueOf(ev.xPos));
                yPos.setText(String.valueOf(ev.yPos));

                rPos.setText(String.valueOf(ev.rPos));
                zPos.setText(String.valueOf(ev.zPos));
*/
                if (ev.getComponent().getIdentifier() ==  Component.Identifier.Axis.X)
                    l3.px = (int) (ev.getValue() * 100 / 65535);
                if (ev.getComponent().getIdentifier() ==  Component.Identifier.Axis.Y)
                     l3.py = (int) (ev.getValue() * 100 / 65535);
                l3.repaint();
                images[16].panel.setVisible(l3.py < 46); // ↑
                images[17].panel.setVisible(l3.px > 54); // →
                images[18].panel.setVisible(l3.py > 54); // ↓
                images[19].panel.setVisible(l3.px < 46); // ←


                if (ev.getComponent().getIdentifier() ==  Component.Identifier.Axis.RX)
                     r3.px = (int) (ev.getValue() * 100 / 65535);
                if (ev.getComponent().getIdentifier() ==  Component.Identifier.Axis.RY)
                     r3.py = (int) (ev.getValue() * 100 / 65535);
                r3.repaint();
                images[20].panel.setVisible(r3.py < 46); // ↑
                images[21].panel.setVisible(r3.px > 54); // →
                images[22].panel.setVisible(r3.py > 54); // ↓
                images[23].panel.setVisible(r3.px < 46); // ←
            }
        });
    }
}

/* */
