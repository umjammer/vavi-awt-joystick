/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.helper;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Arrays;
import java.util.NoSuchElementException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import static java.lang.System.getLogger;


/**
 * JavaVMAppInfo.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-26 nsano initial version <br>
 */
public class JavaVMAppInfo {

    private static final Logger logger = getLogger(JavaVMAppInfo.class.getName());

    /**
     * @return pid which main class name contains one of those
     * @throws NoSuchElementException when not found.
     */
    public static int getPidByMainClassName(String[] mains) {
        for (VirtualMachineDescriptor descriptor : VirtualMachine.list()) {
            if (Arrays.asList(mains).contains(descriptor.displayName().split("\\s")[0])) {
logger.log(Level.DEBUG, descriptor);
                return Integer.decode(descriptor.id());
            }
        }
        throw new NoSuchElementException("target is not in " + Arrays.toString(mains));
    }
}
