package com.hawla.flib.utils;

import org.junit.Test;

import static com.hawla.flib.utils.Util.fillIntegerTwoDigits;
import static junit.framework.TestCase.assertEquals;

public class UtilTest {

    @Test
    public void test_fillIntegerTwoDigits_single_digit1() {
        int initial = 0;
        String expected = "00";
        // create:
        String result = fillIntegerTwoDigits(initial);
        // check
        assertEquals(expected, result);
    }
    @Test
    public void test_fillIntegerTwoDigits_single_digit2() {
        int initial = 7;
        String expected = "07";
        // create:
        String result = fillIntegerTwoDigits(initial);
        // check
        assertEquals(expected, result);
    }
    @Test
    public void test_fillIntegerTwoDigits_double_digit() {
        int initial = 42;
        String expected = "42";
        // create:
        String result = fillIntegerTwoDigits(initial);
        // check
        assertEquals(expected, result);
    }
    @Test
    public void test_fillIntegerTwoDigits_triple_digit() {
        int initial = 420;
        String expected = "420";
        // create:
        String result = fillIntegerTwoDigits(initial);
        // check
        assertEquals(expected, result);
    }
}
