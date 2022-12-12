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

    public static final long JOY_RETURNX = 0x00000001L;
    public static final long JOY_RETURNY = 0x00000002L;
    public static final long JOY_RETURNZ = 0x00000004L;
    public static final long JOY_RETURNR = 0x00000008L;
    public static final long JOY_RETURNU = 0x00000010L;
    public static final long JOY_RETURNV = 0x00000020L;
    public static final long JOY_RETURNPOV = 0x00000040L;
    public static final long JOY_RETURNBUTTONS = 0x00000080L;
    public static final long JOY_RETURNCENTERED = 0x00000400L;

    public static final long JOY_RETURNALL = JOY_RETURNX | JOY_RETURNY | JOY_RETURNZ | JOY_RETURNR | JOY_RETURNU | JOY_RETURNV | JOY_RETURNPOV | JOY_RETURNBUTTONS;
}
