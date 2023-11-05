/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.ms;

import java.awt.CheckboxMenuItem;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import vavi.awt.CheckboxMenuItemGroup;
import vavi.awt.joystick.Joysticklet;


/**
 * direct input.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020419 nsano initial version <br>
 */
@EnabledOnOs(OS.WINDOWS)
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
    Map<String, Joysticklet> cache = new HashMap<>();
    /** accessed by inner class */
    Joysticklet backup;

    /** */
    DirectInputTest(int index) {

        frame.setSize(640, 400);

        Arrays.stream(new DirectInputControllerEnvironment().getControllers())
                .map(c -> (DirectInputController) c)
                .forEach(gp -> {
            int mid = gp.getVendorId();
            int pid = gp.getProductId();
            String name = gp.getName();
            try {
                String className = "vavi.awt.joystick.joysticklet." + "JoySticklet_" + mid + "_" + pid;
                Class<?> clazz = Class.forName(className);
                Joysticklet jsl = (Joysticklet) clazz.getDeclaredConstructor().newInstance();
                cache.put(name, jsl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        MenuBar mb = new MenuBar();

        Menu menu = new Menu("Application");
        MenuItem mi = new MenuItem("Exit");
        mi.addActionListener(ev -> System.exit(0));
        menu.add(mi);

        mb.add(menu);

        CheckboxMenuItemGroup g = new CheckboxMenuItemGroup();
        g.addActionListener(ev -> {
            String name = ((MenuItem) ev.getSource()).getLabel();
            Joysticklet jsl = cache.get(name);
            if (backup != null && jsl != backup) {
//System.err.println(ev.getSource());
                frame.remove(backup);
                frame.add(jsl);
                jsl.validate();
                backup = jsl;
            }
        });

        menu = new Menu("Device");
        for (Map.Entry<String, Joysticklet> e : cache.entrySet()) {
            mi = new CheckboxMenuItem(e.getKey());
            g.add((CheckboxMenuItem) mi);
            menu.add(mi);
        }
        mb.add(menu);

        frame.setMenuBar(mb);

        Joysticklet jsl = cache.values().stream().findFirst().get();
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
