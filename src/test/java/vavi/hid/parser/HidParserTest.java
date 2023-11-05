package vavi.hid.parser;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;
import vavi.hid.parser.HidParser.Feature;
import vavi.util.Debug;


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
}