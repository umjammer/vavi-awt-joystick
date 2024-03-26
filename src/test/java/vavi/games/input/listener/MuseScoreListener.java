/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.listener;

import net.java.games.input.Event;
import vavi.games.input.listener.GamepadInputEventListener.AppInfo;
import vavi.games.input.robot.RococaRobot;
import vavi.util.Debug;

import static org.rococoa.carbon.CarbonCoreLibrary.kVK_ANSI_Backslash;
import static org.rococoa.carbon.CarbonCoreLibrary.kVK_ANSI_RightBracket;
import static org.rococoa.carbon.CarbonCoreLibrary.kVK_Command;


/**
 * MuseScoreListener prototype.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-20 nsano initial version <br>
 */
public class MuseScoreListener extends GamepadAdapter {

    RococaRobot robot = new RococaRobot();

    long prev = System.currentTimeMillis();

    @Override
    public boolean match(AppInfo a) {
        return a.id().equals("org.musescore.MuseScore");
    }

    @Override
    public void onZ(Event e) {
        if (System.currentTimeMillis() - prev > 333) { // interval
            float v = e.getValue() - 128;
            float a = Math.abs(v);

            if (a > 30) { // threshold
                robot.keyPress(kVK_Command);
                if (Math.signum(v) > 0) {
                    robot.keyPress(kVK_ANSI_Backslash); // TODO name is not match. this is right bracket
                    robot.keyRelease(kVK_ANSI_Backslash);
Debug.println("⌘ + ]");
                } else {
                    robot.keyPress(kVK_ANSI_RightBracket); // TODO name is not match. this is left bracket
                    robot.keyRelease(kVK_ANSI_RightBracket);
Debug.println("⌘ + [");
                }
                robot.keyRelease(kVK_Command);

                prev = System.currentTimeMillis();
            }
        }
    }
}
