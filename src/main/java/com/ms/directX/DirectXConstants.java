/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package com.ms.directX;


/**
 * DirectXConstants.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-12-12 nsano initial version <br>
 */
public class DirectXConstants {

    public static final long JOY_RETURNX = 0x0000_0001L;
    public static final long JOY_RETURNY = 0x0000_0002L;
    public static final long JOY_RETURNZ = 0x0000_0004L;
    public static final long JOY_RETURNR = 0x0000_0008L;
    public static final long JOY_RETURNU = 0x0000_0010L;
    public static final long JOY_RETURNV = 0x0000_0020L;
    public static final long JOY_RETURNPOV = 0x0000_0040L;
    public static final long JOY_RETURNBUTTONS = 0x0000_0080L;
    public static final long JOY_RETURNCENTERED = 0x0000_0400L;

    public static final long JOY_RETURNALL = JOY_RETURNX | JOY_RETURNY | JOY_RETURNZ | JOY_RETURNR |
            JOY_RETURNU | JOY_RETURNV | JOY_RETURNPOV | JOY_RETURNBUTTONS;
}
