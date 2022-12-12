/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.awt.CheckboxMenuItem;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.Hashtable;

import vavi.awt.CheckboxMenuItemGroup;
import vavi.awt.joystick.JoySticklet;
import vavi.awt.joystick.ms.GamePort;


/**
 * direct input.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020419 nsano initial version <br>
 */
public class DirectInputTest {

    /**
     * The main entry point for the application.
     *
     * @param args Array of parameters passed to the application
     *             via the command line.
     */
    public static void main(String[] args) {
        int index = -1;
        if (args.length == 1) {
            index = Integer.parseInt(args[0]);
        }
        new DirectInputTest(index);
    }

    /** accessed by inner class */
    Frame frame = new Frame("JoyStick");
    /** Joysticklets, accessed by inner class */
    Hashtable cache = new Hashtable();
    /** accessed by inner class */
    JoySticklet backup;

    /** */
    DirectInputTest(int index) {

        frame.setSize(640, 400);

        GamePort[] gps = GamePort.getGamePorts();

        for (int i = 0; i < gps.length; i++) {
            int mid = gps[i].getManufacturerId();
            int pid = gps[i].getProductId();
            String name = gps[i].getName();
            try {
                String className = "JoySticklet_" + mid + "_" + pid;
                Class clazz = Class.forName(className);
                JoySticklet jsl = (JoySticklet) clazz.newInstance();
                cache.put(name, jsl);
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        MenuBar mb = new MenuBar();

        Menu menu = new Menu("Application");
        MenuItem mi = new MenuItem("Exit");
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                System.exit(0);
            }
        });
        menu.add(mi);

        mb.add(menu);

        CheckboxMenuItemGroup g = new CheckboxMenuItemGroup();
        g.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String name = ((MenuItem) ev.getSource()).getLabel();
                JoySticklet jsl = (JoySticklet) cache.get(name);
                if (backup != null && jsl != backup) {
//System.err.println(ev.getSource());
                    frame.remove(backup);
                    frame.add(jsl);
                    jsl.validate();
                    backup = jsl;
                }
            }
        });

        menu = new Menu("Device");
        Enumeration e = cache.keys();
        while (e.hasMoreElements()) {
            mi = new CheckboxMenuItem((String) e.nextElement());
            g.add((CheckboxMenuItem) mi);
            menu.add(mi);
        }
        mb.add(menu);

        frame.setMenuBar(mb);

        e = cache.elements();
        JoySticklet jsl = (JoySticklet) e.nextElement();
        frame.add(jsl);
        backup = jsl;

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });

        frame.setVisible(true);

        if (cache.size() - 1 < index) {
            index = cache.size() - 1;
        }

        if (index != -1) {
            g.setSelectedIndex(index);
        }
    }
}

/* */
