package org.northcoder.titlewebdemo.util;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.util.Arrays;

/**
 *
 * @author andrew
 */
@RunWith(Parameterized.class)
public class StringCleanserTest {

    private final String dirty;
    private final String cleansed;

    public StringCleanserTest(String dirty, String cleansed) {
        this.dirty = dirty;
        this.cleansed = cleansed;
    }

    @Parameters(name = "Cleanser test {index}: dirty={0}, cleansed={1}")
    public static Iterable<String[]> inputs() {
        return Arrays.asList(new String[][]{
            {"	tabs	tabs	", "tabs	tabs"},
            {"\u0009tab", "tab"},
            {" foo ", "foo"},
            {"Hyphen – test", "Hyphen - test"},
            {"“Double” quotes", "\"Double\" quotes"},
            {"It’s an apostrophe", "It's an apostrophe"},
            {" 		big ““mess““ —   x \u0000", "big \"\"mess\"\" -   x"}
        });
    }

    /**
     * Test of cleanse method, of class StringCleanser. We run this test
     * multiple times using jUnit's parameterized test class.
     */
    @Test
    public void testCleanse() {
        assertThat(StringCleanser.cleanse(dirty)).isEqualTo(cleansed);
    }

}
