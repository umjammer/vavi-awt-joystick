/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.usb;

import static vavi.usb.UsbSpec.names_huts;
import static vavi.usb.UsbSpec.names_hutus;
import static vavi.usb.UsbSpec.names_reporttag;


/**
 * UsbUtil.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2023-09-21 nsano initial version <br>
 */
public final class UsbUtil {

    private UsbUtil() {
    }

    private static final String[] systems = {
            "None", "SI Linear", "SI Rotation",
            "English Linear", "English Rotation"
    };

    private static final String[][] units = {
            {"None", "None", "None", "None", "None", "None", "None", "None"},
            {"None", "Centimeter", "Gram", "Seconds", "Kelvin", "Ampere", "Candela", "None"},
            {"None", "Radians", "Gram", "Seconds", "Kelvin", "Ampere", "Candela", "None"},
            {"None", "Inch", "Slug", "Seconds", "Fahrenheit", "Ampere", "Candela", "None"},
            {"None", "Degrees", "Slug", "Seconds", "Fahrenheit", "Ampere", "Candela", "None"},
    };

    private static void dump_unit(int data, int len) {
        int i;
        int sys;
        int earlier_unit = 0;

        // First nibble tells us which system we're in.
        sys = data & 0xf;
        data >>= 4;

        if (sys > 4) {
            if (sys == 0xf)
                System.out.println("System: Vendor defined, Unit: (unknown)");
            else
                System.out.println("System: Reserved, Unit: (unknown)");
            return;
        } else {
            System.out.printf("System: %s, Unit: ", systems[sys]);
        }
        for (i = 1; i < len * 2; i++) {
            int nibble = data & 0xf;
            data >>= 4;
            if (nibble != 0) {
                if (earlier_unit++ > 0)
                    System.out.print("*");
                System.out.printf("%s", units[sys][i]);
                if (nibble != 1) {
                    // This is a _signed_ nibble(!)

                    int val = nibble & 0x7;
                    if ((nibble & 0x08) != 0)
                        val = -((0x7 & ~val) + 1);
                    System.out.printf("^%d", val);
                }
            }
        }
        if (earlier_unit == 0)
            System.out.print("(None)");
        System.out.println();
    }

    enum Type {
        Main,
        Global,
        Local,
        reserved
    }

    /*+
     * entry point
     */
    public static void dump_report_desc(byte[] b, int l) {
        int data = 0xffff, hut = 0xffff;
        String indent = "                            ";

        System.out.printf("          Report Descriptor: (length is %d)\n", l);
outer:
        for (int i = 0; i < l; ) {
            int bsize = b[i] & 0x03;
            if (bsize == 3)
                bsize = 4;
            int btype = ((b[i] & 0xff) & (0x03 << 2)) >> 2;
            int btag = (b[i] & 0xff) & ~0x03; // 2 LSB bits encode length
            System.out.printf("            Item(%-6s): %s, data=", Type.values()[btype], names_reporttag(btag));
            if (bsize > 0) {
                System.out.print(" [ ");
                data = 0;
                for (int j = 0; j < bsize; j++) {
                    System.out.printf("0x%02x ", b[i + 1 + j]);
                    data += ((b[i + 1 + j] & 0xff) << (8 * j));
                }
                System.out.printf("] %d", data);
            } else
                System.out.print("none");
            System.out.println();
            switch (btag) {
            case 0x04: // Usage Page
                System.out.printf("%s%s\n", indent, names_huts(data));
                hut = data;
                break;

            case 0x08: // Usage
            case 0x18: // Usage Minimum
            case 0x28: // Usage Maximum
                System.out.printf("%s%s\n", indent, names_hutus((hut << 16) + data));
                break;

            case 0x54: // Unit Exponent
                System.out.printf("%sUnit Exponent: %c\n", indent, data);
                break;

            case 0x64: // Unit
                System.out.printf("%s", indent);
                dump_unit(data, bsize);
                break;

            case 0xa0: // Collection
                System.out.printf("%s", indent);
                switch (data) {
                case 0x00:
                    System.out.println("Physical");
                    break;
                case 0x01:
                    System.out.println("Application");
                    break;
                case 0x02:
                    System.out.println("Logical");
                    break;
                case 0x03:
                    System.out.println("Report");
                    break;
                case 0x04:
                    System.out.println("Named Array");
                    break;
                case 0x05:
                    System.out.println("Usage Switch");
                    break;
                case 0x06:
                    System.out.println("Usage Modifier");
                    break;
                default:
                    if ((data & 0x80) != 0)
                        System.out.println("Vendor defined");
                    else
                        System.out.println("Reserved for future use.");
                }
                break;
            case 0x80: // Input
            case 0x90: // Output
            case 0xb0: // Feature
                System.out.printf("%s%s %s %s %s %s\n%s%s %s %s %s\n",
                        indent,
                        (data & 0x01) != 0 ? "Constant" : "Data",
                        (data & 0x02) != 0 ? "Variable" : "Array",
                        (data & 0x04) != 0 ? "Relative" : "Absolute",
                        (data & 0x08) != 0 ? "Wrap" : "No_Wrap",
                        (data & 0x10) != 0 ? "Non_Linear" : "Linear",
                        indent,
                        (data & 0x20) != 0 ? "No_Preferred_State" : "Preferred_State",
                        (data & 0x40) != 0 ? "Null_State" : "No_Null_Position",
                        (data & 0x80) != 0 ? "Volatile" : "Non_Volatile",
                        (data & 0x100) != 0 ? "Buffered Bytes" : "Bitfield");
                break;
            case 0xc0: // End Collection
                break outer;
            }
            i += 1 + bsize;
        }
    }
}
