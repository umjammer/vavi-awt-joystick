/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.helper;

import com.sun.jna.Callback;
import org.rococoa.Foundation;
import org.rococoa.ObjCObject;
import org.rococoa.Rococoa;
import org.rococoa.Selector;
import org.rococoa.cocoa.appkit.NSRunningApplication;
import org.rococoa.cocoa.appkit.NSWorkspace;
import org.rococoa.cocoa.foundation.NSNotification;
import org.rococoa.cocoa.foundation.NSNotificationCenter;
import vavi.games.input.listener.GamepadInputEventListener.AppChangeListener;


/**
 * RococoaAppChangeListener.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-26 nsano initial version <br>
 */
public abstract class RococoaAppChangeListener implements AppChangeListener {

    class WorkspaceObserver implements Callback {

        public void applicationWasActivated(NSNotification notification) {
            NSWorkspace workspace = Rococoa.cast(notification.object(), NSWorkspace.class);
            NSRunningApplication a = workspace.frontmostApplication();
//Debug.println("applicationWasActivated: " + a.bundleIdentifier());
            onAppChanged(new RococoaAppInfo(a));
        }

        public void applicationWasDeactivated(NSNotification notification) {
            NSWorkspace workspace = Rococoa.cast(notification.object(), NSWorkspace.class);
            NSRunningApplication a = workspace.frontmostApplication();
//Debug.println("applicationWasDeactivated: " + a.bundleIdentifier());
            onAppChanged(new RococoaAppInfo(a));
        }
    }

    public RococoaAppChangeListener() {
        ObjCObject proxy = Rococoa.proxy(new WorkspaceObserver());
        Selector sel1 = Foundation.selector("applicationWasActivated:");
        Selector sel2 = Foundation.selector("applicationWasDeactivated:");

        NSNotificationCenter notificationCenter = NSWorkspace.sharedWorkspace().notificationCenter();
        notificationCenter.addObserver_selector_name_object(proxy.id(), sel1, NSWorkspace.NSWorkspaceDidActivateApplicationNotification, null);
        notificationCenter.addObserver_selector_name_object(proxy.id(), sel2, NSWorkspace.NSWorkspaceDidDeactivateApplicationNotification, null);
    }
}
