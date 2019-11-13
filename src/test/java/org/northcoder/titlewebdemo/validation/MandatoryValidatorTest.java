package org.northcoder.titlewebdemo.validation;

import static com.google.common.truth.Truth.assertThat;
import java.util.Arrays;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author andrew
 */
@RunWith(Parameterized.class)
public class MandatoryValidatorTest {
    
    private final Object input;
    private final boolean expected;
    
    public MandatoryValidatorTest(Object input, boolean expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "Mandatory test {index}: input={0}, expected={1}")
    public static Iterable<Object[]> inputs() {
        return Arrays.asList(new Object[][]{
            {null, false}, // this is indexed as "test 0"
            {"", false},
            {" ", false}, // one space
            {"	", false}, // one tab
            {"\u0009", false}, // one tab using unicode
            {" \u00A0 ", false}, // non-breaking space
            {"x", true},
            {UUID.randomUUID(), true},
            {new char[5], false}
        });
    }
    
    /**
     * Test of isValid method, of class MandatoryValidator.
     */
    @Test
    public void testIsValid() {
        boolean result = new MandatoryValidator().isValid(input, null);
        assertThat(result).isEqualTo(expected);
    }
    
}
