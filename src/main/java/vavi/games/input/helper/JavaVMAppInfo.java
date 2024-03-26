/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.helper;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;


/**
 * JavaVMAppInfo.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-26 nsano initial version <br>
 */
public class JavaVMAppInfo {

    /**
     * @return pid which main class name contains one of those
     * @throws NoSuchElementException when not found.
     */
    public static int getPidByMainClassName(String[] mains) {
        for (VirtualMachineDescriptor descriptor : VirtualMachine.list()) {
            if (Arrays.asList(mains).contains(descriptor.displayName())) {
                return Integer.decode(descriptor.id());
            }
        }
        throw new NoSuchElementException("target is not in " + Arrays.toString(mains));
    }
}
