package vavi.hid.parser;

import org.junit.jupiter.api.Test;


class HidParserTest {

    @Test
    void test1() {
        //byte[] desctor = new byte[HIDParserTestData.standardMouseDescriptor.length];
        int[] testData = HidParserTestData.cheapGamepad;
        byte[] descriptor = new byte[testData.length];
        for (int i = 0; i < descriptor.length; i++)
            descriptor[i] = (byte) testData[i];
        HidParser parser = new HidParser();
        parser.parse(descriptor, descriptor.length);
        parser.dump();
    }
}