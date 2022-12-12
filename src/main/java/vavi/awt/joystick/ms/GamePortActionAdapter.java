/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.joystick.ms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import com.ms.directX.DirectXConstants;
import com.ms.directX.JoyInfo;
import vavi.awt.joystick.GamePortEvent;
import vavi.util.Debug;


/**
 * The listener interface for the direct input device.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020422 nsano initial version <br>
 */
public class GamePortActionAdapter implements ActionListener {

    /** */
    private GamePort gp;

    /** */
    public GamePortActionAdapter(GamePort gp) {
        this.gp = gp;
    }

    /** */
    private JoyInfo ji = new JoyInfo();
    /** */
    private JoyInfo backup = new JoyInfo();

    /**
     * Called when polling action process.
     *
     * @throws	NullPointerException	if GamePortListeners have not set
     */
    public void actionPerformed(ActionEvent e) {

        ji.flags = DirectXConstants.JOY_RETURNALL;

        try {
            gp.fillJoyInfo(ji);
/*
            ev.buttonNumber = ji.buttonNumber;
            ev.buttons      = ji.buttons     ;
            ev.flags        = ji.flags       ;
            ev.pov          = ji.pov         ;
            ev.rPos         = ji.rPos        ;
            ev.uPos         = ji.uPos        ;
            ev.vPos         = ji.vPos        ;
            ev.xPos         = ji.xPos        ;
            ev.yPos         = ji.yPos        ;
            ev.zPos         = ji.zPos        ;

            portChange(ev);
*/
            if (backup.buttons != ji.buttons) {
                GamePortEvent ev = new GamePortEvent(this);
                ev.buttons = ji.buttons;
                gp.fireButtonChange(ev);

                for (int i = 0; i < 16; i++) {
                    if ((backup.buttons & (0x0001 << i)) == 0 &&
                        (    ji.buttons & (0x0001 << i)) != 0) {
                        ev = new GamePortEvent(this);
                        ev.target = i;
                        gp.fireButtonPressed(ev);
                    }
                    else if ((backup.buttons & (0x0001 << i)) != 0 &&
                             (    ji.buttons & (0x0001 << i)) == 0) {
                        ev = new GamePortEvent(this);
                        ev.target = i;
                        gp.fireButtonReleased(ev);
                    }
                }
            }

            if (backup.rPos != ji.rPos ||
                backup.uPos != ji.uPos ||
                backup.vPos != ji.vPos ||
                backup.xPos != ji.xPos ||
                backup.yPos != ji.yPos ||
                backup.zPos != ji.zPos) {

                GamePortEvent ev = new GamePortEvent(this);
                ev.rPos = ji.rPos;
                ev.uPos = ji.uPos;
                ev.vPos = ji.vPos;
                ev.xPos = ji.xPos;
                ev.yPos = ji.yPos;
                ev.zPos = ji.zPos;
                gp.firePositionChange(ev);
            }

            backup.buttonNumber = ji.buttonNumber;
            backup.buttons      = ji.buttons     ;
            backup.flags        = ji.flags       ;
            backup.pov          = ji.pov         ;
            backup.rPos         = ji.rPos        ;
            backup.uPos         = ji.uPos        ;
            backup.vPos         = ji.vPos        ;
            backup.xPos         = ji.xPos        ;
            backup.yPos         = ji.yPos        ;
            backup.zPos         = ji.zPos        ;

        } catch (Exception f) {
Debug.printStackTrace(Level.FINE, f);
        }
    }
}

/* */
