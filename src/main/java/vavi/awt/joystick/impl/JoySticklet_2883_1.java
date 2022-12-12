/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.impl;

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

import vavi.awt.joystick.GamePortAdapter;
import vavi.awt.joystick.GamePortEvent;
import vavi.awt.joystick.JoySticklet;


/**
 * PSX-USB Adapter
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020421 nsano initial version <br>
 */
public class JoySticklet_2883_1 extends JoySticklet {

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

                String value = props.getProperty("joystick."+name+".x");
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
        super(2883, 1);

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

        addGamePortListener(new GamePortAdapter() {
            public void buttonChange(GamePortEvent ev) {
//System.err.println(toHex4(ev.buttons));
//System.err.println(toBits(ev.buttons));
                images[ 0].panel.setVisible((ev.buttons & 0x0001) != 0); // △
                images[ 1].panel.setVisible((ev.buttons & 0x0002) != 0); // ○
                images[ 2].panel.setVisible((ev.buttons & 0x0004) != 0); // ×
                images[ 3].panel.setVisible((ev.buttons & 0x0008) != 0); // □

                images[ 4].panel.setVisible((ev.buttons & 0x0010) != 0); // l2
                images[ 5].panel.setVisible((ev.buttons & 0x0020) != 0); // r2
                images[ 6].panel.setVisible((ev.buttons & 0x0040) != 0); // l1
                images[ 7].panel.setVisible((ev.buttons & 0x0080) != 0); // r1

                images[ 8].panel.setVisible((ev.buttons & 0x0100) != 0); // sl
                images[ 9].panel.setVisible((ev.buttons & 0x0200) != 0); // st

                images[10].panel.setVisible((ev.buttons & 0x0400) != 0); // l3
                images[11].panel.setVisible((ev.buttons & 0x0800) != 0); // r3

                images[12].panel.setVisible((ev.buttons & 0x1000) != 0); // ↑
                images[13].panel.setVisible((ev.buttons & 0x2000) != 0); // →
                images[14].panel.setVisible((ev.buttons & 0x4000) != 0); // ↓
                images[15].panel.setVisible((ev.buttons & 0x8000) != 0); // ←
            }

            public void buttonReleased(GamePortEvent ev) {
System.err.println("released: " + ev.target);
            }

            public void positionChange(GamePortEvent ev) {
/*
                xPos.setText(String.valueOf(ev.xPos));
                yPos.setText(String.valueOf(ev.yPos));

                rPos.setText(String.valueOf(ev.rPos));
                zPos.setText(String.valueOf(ev.zPos));
*/
                l3.px = ev.xPos * 100 / 65535;
                l3.py = ev.yPos * 100 / 65535;
                l3.repaint();

                images[16].panel.setVisible(l3.py < 46); // ↑
                images[17].panel.setVisible(l3.px > 54); // →
                images[18].panel.setVisible(l3.py > 54); // ↓
                images[19].panel.setVisible(l3.px < 46); // ←

                r3.px = ev.rPos * 100 / 65535;
                r3.py = ev.zPos * 100 / 65535;
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
