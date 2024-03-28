/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.helper;

import java.awt.Rectangle;

import com.sun.jna.platform.mac.CoreFoundation.CFArrayRef;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.appkit.NSRunningApplication;
import org.rococoa.cocoa.foundation.NSDictionary;
import org.rococoa.cocoa.foundation.NSString;
import vavi.games.input.listener.GamepadInputEventListener.AppInfo;
import vavi.util.Debug;

import static org.rococoa.cocoa.coregraphics.CoreGraphicsLibrary.kCGNullWindowID;
import static org.rococoa.cocoa.coregraphics.CoreGraphicsLibrary.kCGWindowListOptionOnScreenOnly;
import static org.rococoa.cocoa.coregraphics.CoreGraphicsLibrary.library;


/**
 * RococoaAppInfo.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-26 nsano initial version <br>
 */
public class RococoaAppInfo implements AppInfo {

    private final NSRunningApplication a;

    public RococoaAppInfo(NSRunningApplication a) {
        this.a = a;
    }

    @Override
    public String id() {
        return a.bundleIdentifier();
    }

    @Override
    public int pid() {
        return a.processIdentifier().intValue();
    }

    /** takes a bit time */
    @Override
    public Rectangle bounds() {
        try {
            CFArrayRef array = library.CGWindowListCopyWindowInfo(kCGWindowListOptionOnScreenOnly, kCGNullWindowID);
//Debug.println("windows: " + array.getCount());
            for (int i = 0; i < array.getCount(); i++) {
                NSDictionary dic = Rococoa.toNSDictionary(array.getValueAtIndex(i));
                if (Integer.parseInt(dic.get(NSString.stringWithString("kCGWindowOwnerPID")).toString()) == pid()) {
                    NSDictionary rect = Rococoa.cast(dic.get(NSString.stringWithString("kCGWindowBounds")), NSDictionary.class);
                    int x = Integer.parseInt(rect.get(NSString.stringWithString("X")).toString());
                    int y = Integer.parseInt(rect.get(NSString.stringWithString("Y")).toString());
                    int width = Integer.parseInt(rect.get(NSString.stringWithString("Width")).toString());
                    int height = Integer.parseInt(rect.get(NSString.stringWithString("Height")).toString());
                    return new Rectangle(x, y, width, height);
                }
            }
        } catch (Exception e) {
            Debug.println(e);
        }
        return null;
    }
}
