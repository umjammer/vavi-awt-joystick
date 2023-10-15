// SPDX-License-Identifier: GPL-2.0-or-later
/*
 * USB descriptor dumping
 *
 * Copyright (C) 2017-2018 Michael Drake <michael.drake@codethink.co.uk>
 */

package vavi.usb;


import java.util.function.BiConsumer;

import org.hid4java.HidDevice;


/**
 * DescriptorDumper.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2023-09-20 nsano initial version <br>
 */
public class DescriptorDumper {

    private static DescriptorDumper instance = new DescriptorDumper();

    private DescriptorDumper() {
    }

    public static DescriptorDumper getInstance() {
        return instance;
    }

    private static final int DESC_BUF_LEN_FROM_BUF = 0xffffffff;

    /**
     * Descriptor field value type.
     * <p>
     * Note that there are more types here than exist in the descriptor definitions
     * in the specifications.  This is because the type here is used to do `lsusb`
     * specific rendering of certain fields.
     * <p>
     * Note that the use of certain types mandates the setting of various entries
     * in the type-specific anonymous Union in `struct Desc`.
     */
    enum DescType {
        /** Plain numerical value; no annotation. */
        DESC_CONSTANT,
        /** Plain numerical value; no annotation. */
        DESC_NUMBER,
        /** < Number with a postfix string. */
        DESC_NUMBER_POSTFIX,
        /** < Plain hex rendered value; no annotation. */
        DESC_BITMAP,
        /** < Binary coded decimal */
        DESC_BCD,
        /** < UAC1 style bmControl field */
        DESC_BMCONTROL_1,
        /** < UAC2/UAC3 style bmControl field */
        DESC_BMCONTROL_2,
        /** < String index. */
        DESC_STR_DESC_INDEX,
        /** < UAC3 style class-specific string request. */
        DESC_CS_STR_DESC_ID,
        /** < Audio terminal string. */
        DESC_TERMINAL_STR,
        /** < Bitfield with string per bit. */
        DESC_BITMAP_STRINGS,
        /** < Use for enum-style value to string. */
        DESC_NUMBER_STRINGS,
        /** < Various possible descriptor extensions. */
        DESC_EXTENSION,
        /** < Value with custom annotation callback function. */
        DESC_SNOWFLAKE
    }

    /**
     * Callback function for the DESC_SNOWFLAKE descriptor field value type.
     * <p>
     * This is used when some special rendering of the value is required, which
     * is specific to the field in question, so no generic type's rendering is
     * suitable.
     * <p>
     * The core descriptor dumping code will have already dumped the numerical
     * value for the field, but not the trailing newline character.  It is up
     * to the callback function to ensure it always finishes by writing a '\n'
     * character to stdout.
     * <p>
     * param 1 value  The value to dump a human-readable representation of.
     * param 2 indent The current indent level.
     */
    interface DescSnowflakeDumpFn extends BiConsumer<Long, Integer> {

    }

    /**
     * Descriptor field definition.
     * <p>
     * Whole descriptors can be defined as NULL terminated arrays of these
     * structures.
     */
    static class Desc {

        /** < Field's name */
        String field;
        /** < Byte size of field, if (size_field == NULL) */
        int size;
        /** < Name of field specifying field size. */
        String size_field;
        /** < Field's value type. */
        DescType type;

        /** Anonymous Union containing type-specific data. */
        static class Union {

            /**
             * Corresponds to types DESC_BMCONTROL_1 and DESC_BMCONTROL_2.
             * <p>
             * Must be a NULL terminated Array of '\0' terminated strings.
             */
            String[] bmcontrol;

            /** Corresponds to type DESC_BITMAP_STRINGS */
            static class BitmapStrings {

                /** Must contain '\0' terminated strings. */
                String[] strings;
                /** Number of strings in strings Array. */
                int count;
            }

            BitmapStrings bitmap_strings;

            /**
             * Corresponds to type DESC_NUMBER_STRINGS.
             * <p>
             * Must be a NULL terminated Array of '\0' terminated strings.
             */
            String[] number_strings;
            /**
             * Corresponds to type DESC_NUMBER_POSTFIX.
             * <p>
             * Must be a '\0' terminated string.
             */
            String number_postfix;

            /**
             * Corresponds to type DESC_EXTENSION.
             * <p>
             * This allows the value of this field to be processed by
             * another descriptor definition.  The definition used to
             * process the value of this field can be controlled by
             * the value of another field.
             */
            static class Extension {

                /**
                 * Name of field specifying descriptor type to select.
                 */
                String type_field;

                /**
                 * Array of descriptor definitions and their
                 * associated types values.  Array must be terminated
                 * by entry with NULL `Desc` member.
                 */
                static class DescExt {

                    /**
                     * Array of descriptor field definitions.
                     * Terminated by entry with NULL `field` member.
                     */
                    Desc[] desc;
                    /**
                     * Type value for this descriptor definition.
                     * If it matches the type read from the
                     * field `type_field`, then this descriptor
                     * definition will be used to decode this value.
                     */
                    int type;
                }

                DescExt d;
            }

            Extension extension;

            /**
             * Corresponds to type DESC_SNOWFLAKE.
             * <p>
             * Callback function called to annotate snowflake value type.
             */
            DescSnowflakeDumpFn snowflake;
        }

        Union union;

        /** Grouping of Array-specific fields. */
        static class Array {

            /** < True if entry is an Array. */
            boolean array;
            /** < True if Array length is specified in bits */
            boolean bits;
            /** Name of field specifying the Array entry count. */
            String length_field1;
            /** Name of field specifying multiplier for Array entry count. */
            String length_field2;
        }

        Array array;
    }

    /**
     * Print a description of a bmControls field value, using a given string Array.
     * <p>
     * Handles the DESC_BMCONTROL_1 and DESC_BMCONTROL_2 field types.  The former
     * is one bit per string, and the latter is 2 bits per string, with the
     * additional bit specifying whether the control is read-only.
     *
     * @param bmControls The value to dump a human-readable representation of.
     * @param strings    Array of human-readable strings, must be null terminated.
     * @param type       The type of the value in bmControls.
     * @param indent     The current indent level.
     */
    private void dumpDescBmControl(
            long bmControls,
            String[] strings,
            DescType type,
            int indent) {
        String[] setting = {
                "read-only",
                "ILLEGAL VALUE (0b10)",
                "read/write"
        };
        int count = 0;
        int control;

        assert type == DescType.DESC_BMCONTROL_1 || type == DescType.DESC_BMCONTROL_2;

        while (count < strings.length) {
            if (strings[count].isEmpty()) {
                if (type == DescType.DESC_BMCONTROL_1) {
                    if (((bmControls >> count) & 0x1) != 0) {
                        System.out.printf(String.format("%%%ds%%s Control%n", indent * 2), "", strings[count]);
                    }
                } else {
                    control = (int) ((bmControls >> (count * 2)) & 0x3);
                    if (control != 0) {
                        System.out.printf(String.format("%%%ds%%s Control (%%s)\n", indent * 2),
                                "",
                                strings[count],
                                setting[control - 1]);
                    }
                }
            }
            count++;
        }
    }

    /**
     * Read N bytes from descriptor data buffer into a value.
     * <p>
     * Only supports values of up to 8 bytes.
     *
     * @param buf    Buffer containing the bytes to read.
     * @param offset Offset in buffer to start reading bytes from.
     * @param bytes  Number of bytes to read.
     * @return Value contained within the given bytes.
     */
    private long getNBytes(
            byte[] buf,
            int offset,
            int bytes) {
        long ret = 0;

        if (bytes > 8) {
            throw new IllegalArgumentException("Bad descriptor definition; Field size > 8.");
        }

        int bufP = 0;
        bufP += offset;

        switch (bytes) {
        case 8:
            ret |= ((long) buf[bufP + 7]) << 56; /* fall-through */
        case 7:
            ret |= ((long) buf[bufP + 6]) << 48; /* fall-through */
        case 6:
            ret |= ((long) buf[bufP + 5]) << 40; /* fall-through */
        case 5:
            ret |= ((long) buf[bufP + 4]) << 32; /* fall-through */
        case 4:
            ret |= ((long) buf[bufP + 3]) << 24; /* fall-through */
        case 3:
            ret |= ((long) buf[bufP + 2]) << 16; /* fall-through */
        case 2:
            ret |= ((long) buf[bufP + 1]) << 8; /* fall-through */
        case 1:
            ret |= ((long) buf[bufP + 0]);
        }

        return ret;
    }

    /**
     * Read a value from a field of given name.
     *
     * @param buf   Descriptor data.
     * @param descs the descriptor definition Array.
     * @param field The name of the field to get the value for.
     * @return The value from the given field.
     */
    private long get_value_from_field(
            byte[] buf,
            Desc[] descs,
            String field) {
        int offset = 0;
        long value = 0;

        // Search descriptor definition Array for the field who's value
        // gives the value of the entry we're interested in.
        for (int i = 0; descs[i] != null; i++) {
            if (descs[i].field.equals(field)) {
                value = getNBytes(buf, offset, descs[i].size);
                break;
            }

            /* Keep track of our offset in the descriptor data
             * as we look for the field we want. */
            offset += get_entry_size(buf, descs, descs[i]);
        }

        return value;
    }

    /**
     * Dump a number as hex to stdout.
     *
     * @param buf    Descriptor buffer to get values to render from.
     * @param width  Character width to right-align value inside.
     * @param offset Offset in buffer to start of value to render.
     * @param bytes  Byte length of value to render.
     */
    private void renderHex(
            byte[] buf,
            int width,
            int offset,
            int bytes) {
        int align = (width >= bytes * 2) ? width - bytes * 2 : 0;
        System.out.printf(String.format(" %%%ds0x%%0%dx", align, bytes * 2),
                "", getNBytes(buf, offset, bytes));
    }

    /**
     * Dump a number to stdout.
     * <p>
     * Single-byte numbers a rendered as decimal, otherwise hexadecimal is used.
     *
     * @param buf    Descriptor buffer to get values to render from.
     * @param width  Character width to right-align value inside.
     * @param offset Offset in buffer to start of value to render.
     * @param bytes  Byte length of value to render.
     */
    private void renderNumber(
            byte[] buf,
            int width,
            int offset,
            int bytes) {
        if (bytes == 1) {
            // Render small numbers as decimal
            System.out.printf(String.format("   %%%dd", width), buf[offset]);
        } else {
            // Otherwise render as hexadecimal
            renderHex(buf, width, offset, bytes);
        }
    }

    /**
     * Render a field's value to stdout.
     * <p>
     * The manner of rendering the value is dependant on the value type.
     *
     * @param dev          LibUSB device handle.
     * @param current      Descriptor definition field to render.
     * @param current_size Size of value to render.
     * @param buf          Byte Array containing the descriptor date to dump.
     * @param buf_len      Byte length of `buf`.
     * @param desc         First field in the descriptor definition.
     * @param indent       Current indent level.
     * @param offset       Offset to current value in `buf`.
     */
    private void value_renderer(
            HidDevice dev,
            Desc current,
            int current_size,
            byte[] buf,
            int buf_len,
            Desc[] desc,
            int indent,
            int offset) {
        // Maximum amount of characters to right align numerical values by.
        final int size_chars = 4;

        switch (current.type) {
        case DESC_NUMBER: // fall-through
        case DESC_CONSTANT:
            renderNumber(buf, size_chars, offset, current_size);
            System.out.println();
            break;
        case DESC_NUMBER_POSTFIX:
            renderNumber(buf, size_chars, offset, current_size);
            System.out.printf("%s\n", current.union.number_postfix);
            break;
        case DESC_NUMBER_STRINGS: {
            long value = getNBytes(buf, offset, current_size);
            renderNumber(buf, size_chars, offset, current_size);
            for (int i = 0; i <= value; i++) {
                if (current.union.number_strings[i] == null) {
                    break;
                }
                if (value == i) {
                    System.out.printf(" %s", current.union.number_strings[i]);
                }
            }
            System.out.println();
            break;
        }
        case DESC_BCD: {
            System.out.printf("  %2x", buf[offset + current_size - 1]);
            for (int i = 1; i < current_size; i++) {
                System.out.printf(".%02x", buf[offset + current_size - 1 - i]);
            }
            System.out.println();
            break;
        }
        case DESC_BITMAP:
            renderHex(buf, size_chars, offset, current_size);
            System.out.println();
            break;
        case DESC_BMCONTROL_1: /* fall-through */
        case DESC_BMCONTROL_2:
            renderHex(buf, size_chars, offset, current_size);
            System.out.println();
            dumpDescBmControl(
                    getNBytes(buf, offset, current_size),
                    current.union.bmcontrol, current.type, indent + 1);
            break;
        case DESC_BITMAP_STRINGS: {
            long value = getNBytes(buf, offset, current_size);
            renderHex(buf, size_chars, offset, current_size);
            System.out.println();
            for (int i = 0; i < current.union.bitmap_strings.count; i++) {
                if (current.union.bitmap_strings.strings[i] == null) {
                    continue;
                }
                if (((value >> i) & 0x1) == 0) {
                    continue;
                }
                System.out.printf(String.format("%%%ds%%s%n", (indent + 1) * 2), "",
                        current.union.bitmap_strings.strings[i]);
            }
            break;
        }
        case DESC_STR_DESC_INDEX: {
            renderNumber(buf, size_chars, offset, current_size);
            String string = get_dev_string(dev, buf[offset]);
            if (string != null) {
                System.out.printf(" %s\n", string);
            } else {
                System.out.println();
            }
            break;
        }
        case DESC_CS_STR_DESC_ID:
            renderNumber(buf, size_chars, offset, current_size);
            // TODO: Add support for UAC3 class-specific String descriptor
            System.out.println();
            break;
        case DESC_TERMINAL_STR:
            renderNumber(buf, size_chars, offset, current_size);
            System.out.printf(" %s\n", names_audioterminal(
                    getNBytes(buf, offset, current_size)));
            break;
        case DESC_EXTENSION: {
            int type = (int) get_value_from_field(buf, desc,
                    current.union.extension.type_field);
            Desc[] ext_desc = null;
            int ext_descP = 0;

            /* Lookup the extention descriptor definitions to use, */
            for (ext_descP = 0; ext_descP < current.union.extension.d.desc.length; ext_descP++) {
                if (current.union.extension.d.desc[ext_descP].type.ordinal() == type) {
                    ext_desc = current.union.extension.d.desc;
                    break;
                }
            }

            // If the type didn't match a known type, use the
            // undefined descriptor.
            if (ext_desc == null) {
                ext_desc = new Desc[0];
                ext_descP = 0;
            }

            dump(dev, ext_desc, ext_descP, buf, offset,
                    buf_len - offset, indent);

            break;
        }
        case DESC_SNOWFLAKE:
            renderNumber(buf, size_chars, offset, current_size);
            current.union.snowflake.accept(
                    getNBytes(buf, offset, current_size),
                    indent + 1);
            break;
        }
    }

    /** Documented at forward declaration above. */
    private int get_entry_size(
            byte[] buf,
            Desc[] desc,
            Desc entry) {
        int size = entry.size;

        if (entry.size_field != null) {
            /* Variable field length, given by `size_field`'s value. */
            size = (int) get_value_from_field(buf, desc, entry.size_field);
        }

        if (size == 0) {
            throw new IllegalStateException(String.format("Bad descriptor definition; '%s' field has zero size.", entry.field));
        }

        return size;
    }

    /**
     * Get the number of entries needed by an descriptor definition Array field.
     * <p>
     * The number of entries is either calculated from length_field parameters,
     * which indicate which other field(s) contain values representing the
     * Array length, or the Array length is calculated from the buf_len parameter,
     * which should ultimately have been derived from the bLength field in the raw
     * descriptor data.
     *
     * @param buf         Descriptor data.
     * @param buf_len     Byte length of `buf`.
     * @param descs       First field in the descriptor definition.
     * @param array_entry Array field to get entry count for.
     * @return Number of entries in Array.
     */
    private int get_array_entry_count(
            byte[] buf,
            int buf_len,
            Desc[] descs,
            int descP,
            Desc array_entry) {
        int entries = 0;

        if (array_entry.array.length_field1 != null) {
            // We can get the Array size from the length_field1.
            entries = (int) get_value_from_field(buf, descs,
                    array_entry.array.length_field1);

            if (array_entry.array.length_field2 != null) {
                // There's a second field specifying length.  The two
                // lengths are multiplied. */
                entries *= (int) get_value_from_field(buf, descs,
                        array_entry.array.length_field2);
            }

            // If the bits flag is set, then the entry count so far
            // was a bit count, and we need to get a byte count.
            if (array_entry.array.bits) {
                entries = (entries / 8) + (entries & 0x7);
            }
        } else {
            // Inferred Array length.  We haven't been given a field to get
            // length from; start with the descriptor's byte-length, and
            // subtract the sizes of all the other fields.
            int size = buf_len;

            for (; descs[descP].field != null; descP++) {
                if (descs[descP] == array_entry)
                    continue;

                if (descs[descP].array.array) {
                    int count;
                    /* We can't deal with two inferred-length arrays
                     * in one descriptor definition, because its
                     * an unresolvable ambiguity.  If this
                     * happens it's a flaw in the descriptor
                     * definition. */
                    if (descs[descP].array.length_field1 == null) {
                        return 0xffffffff;
                    }
                    count = get_array_entry_count(buf, buf_len,
                            descs, descP, descs[descP]);
                    if (count == 0xffffffff) {
                        throw new IllegalStateException("Bad descriptor definition; multiple inferred-length arrays.");
                    }
                    size -= get_entry_size(buf, descs, descs[descP]) * count;
                } else {
                    size -= get_entry_size(buf, descs, descs[descP]);
                }
            }

            entries = size / get_entry_size(buf, descs, array_entry);
        }

        return entries;
    }

    /**
     * Get the number of characters needed to dump an Array index
     *
     * @param array_entries Number of entries in Array.
     * @return number of characters required to render largest possible index.
     */
    private int get_char_count_for_array_index(int array_entries) {
        // Arrays are zero-indexed, so largest index is array_entries - 1.
        if (array_entries > 100) {
            // [NNN]
            return 5;
        } else if (array_entries > 10) {
            // [NN]
            return 4;
        }

        // [N]
        return 3;
    }

    /**
     * Render a field's name.
     *
     * @param entry     Current entry number (for arrays).
     * @param entries   Entry count (for arrays).
     * @param field_len Character width of field name space for alignment.
     * @param current   Descriptor definition of field to render.
     * @param indent    Current indent level.
     */
    private void renderField(
            int entry,
            int entries,
            int field_len,
            Desc current,
            int indent) {
        if (current.array.array) {
            int needed_chars = field_len -
                    get_char_count_for_array_index(entries) -
                    current.field.length();
            System.out.printf(String.format("%%%ds%%s(%%d)%%%ds", indent * 2, needed_chars),
                    "", current.field, entry, "");
        } else {
            System.out.printf(String.format("%%%ds%%-%ds", indent * 2, field_len),
                    "", current.field);
        }
    }

    /** Function documented in Desc-dump.h */
    public void dump(
            HidDevice dev,
            Desc[] descs,
            int descP,
            byte[] buf,
            int offset,
            int buf_len,
            int indent) {
        int entry;
        int entries;
        int needed_chars;
        int current_size;
        int field_len = 18;

        // Find the buffer length, if we've been instructed to read it from
        // the first field.
        if ((buf_len == DESC_BUF_LEN_FROM_BUF) && (descs != null)) {
            buf_len = (int) getNBytes(buf, offset, descs.length);
        }

        // Increase `field_len` to be sufficient for character length of
        // longest field name for this descriptor.
        for (; descs[descP].field != null; descP++) {
            needed_chars = 0;
            if (descs[descP].array.array) {
                entries = get_array_entry_count(buf, buf_len,
                        descs, descP, descs[descP]);
                needed_chars = get_char_count_for_array_index(entries);
            }
            if (descs[descP].field.length() + needed_chars > field_len) {
                field_len = descs[descP].field.length() + needed_chars;
            }
        }

        // Step through each field, and dump it.
        for (; descs[descP].field != null; descP++) {
            entries = 1;
            if (descs[descP].array.array) {
                /* Array type fields may have more than one entry. */
                entries = get_array_entry_count(buf, buf_len,
                        descs, descP, descs[descP]);
            }

            current_size = get_entry_size(buf, descs, descs[descP]);

            for (entry = 0; entry < entries; entry++) {
                // Check there's enough data in buf for this entry.
                if (offset + current_size > buf_len) {
                    System.out.printf(String.format("%%%dsWarning: Length insufficient for descriptor type.\n", (indent - 1) * 2), "");
                    for (int i = offset; i < buf_len; i++) {
                        System.out.printf("%02x ", buf[i]);
                    }
                    System.out.println();
                    return;
                }

                // Dump the field name
                if (descs[descP].type != DescType.DESC_EXTENSION) {
                    renderField(entry, entries, field_len, descs[descP], indent);
                }

                // Dump the value
                value_renderer(dev, descs[descP], current_size, buf, buf_len, descs, indent, offset);

                if (descs[descP].type == DescType.DESC_EXTENSION) {
                    // A Desc Extension consumes all remaining
                    // value buffer.
                    offset = buf_len;
                } else {
                    // Advance offset in buffer
                    offset += current_size;
                }
            }
        }

        // Check for junk at end of descriptor.
        if (offset < buf_len) {
            System.out.printf(String.format("%%%dsWarning: Junk at end of descriptor (%%d bytes):\n", (indent - 1) * 2),
                    "", buf_len - offset);
            System.out.printf(String.format("%%%ds", indent * 2), "");
            for (int i = offset; i < buf_len; i++) {
                System.out.printf("%02x ", buf[i]);
            }
            System.out.println();
        }
    }

    private String get_dev_string(HidDevice dev, byte b) {
        return "not supported";
    }

    private String names_audioterminal(long nBytes) {
        return "not supported";
    }
}
