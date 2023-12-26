package vavi.hid.parser;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;
import vavi.hid.parser.HidParser.Feature;
import vavi.util.Debug;
import vavi.util.StringUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;


class HidParserTest {

    @Test
    void test1() {
        int[] testData = HidParserTestData.cheapGamepad;
        byte[] descriptor = new byte[testData.length];
        for (int i = 0; i < descriptor.length; i++)
            descriptor[i] = (byte) testData[i];
        HidParser parser = new HidParser();
        parser.parse(descriptor, descriptor.length);
        parser.dump();
    }

    @Test
    void test2() {
        EnumSet<Feature> feature = HidParser.Feature.valueOf(4);
Debug.println("enumSet: " + Feature.asString(feature));
    }

    @Test
    void test3() {
        Field field = new Field(10, 3);
Debug.printf("offsetByte: %d, startBit: %d", field.offsetByte, field.startBit);
Debug.printf("%s, 0x%02x, %s", Field.toBit(field.startBit, field.reportSize), field.mask, StringUtil.toBits(field.mask));
        assertEquals("__***___", Field.toBit(field.startBit, field.reportSize));
        assertEquals(1, field.offsetByte);
        assertEquals(0x1c, field.mask);

        byte[] data = { 0x00, 0x56 }; // _X*_*_X_ .X.*.*X.
                                      //   ~~~       ~~~

        int v = field.getValue(data);
        assertEquals(5, v);

        field.setValue(data, (byte) 0x03); // _X**__X_ .X..**X.
                                           //   ~~~       ~~~
        assertEquals(0x4e, data[1]);
    }
}