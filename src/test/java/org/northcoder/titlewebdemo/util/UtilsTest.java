package org.northcoder.titlewebdemo.util;

import java.net.URLEncoder;
import org.northcoder.titlewebdemo.beans.Title;
import org.northcoder.titlewebdemo.beans.Talent;
import org.junit.Test;
import org.junit.Before;
import static com.google.common.truth.Truth.assertThat;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 */
public class UtilsTest {

    public UtilsTest() {
    }

    private Title title;
    private String titleIDString;
    private String primaryTitleName;

    @Before
    public void setUp() {
        titleIDString = "tt123456";
        primaryTitleName = "Never-Ending Superhero Movie";
        title = new Title();
    }

    //@Test
    public void testLoadTitleIDFromForm() {
        // basic case
        String formData = String.join("", "titleID=", titleIDString, "&foo=bar",
                "&primaryTitle=", primaryTitleName);
        Utils.formBodyToBean(formData, title);
        assertThat(title.getTitleID()).isEqualTo(titleIDString);
        assertThat(title.getPrimaryTitle()).isEqualTo(primaryTitleName);
    }

    //@Test
    public void testLoadBeanFromForm2() {
        // null form values
        String formData = String.join("", "titleID=", titleIDString, "&foo=bar",
                "&baz=", "&primaryTitle=");
        Utils.formBodyToBean(formData, title);
        assertThat(title.getPrimaryTitle()).isNull();
    }

    //@Test
    public void testNullForm() {
        String formData = null;
        Utils.formBodyToBean(formData, title);
        assertThat(title.getPrimaryTitle()).isNull();
    }

    //@Test
    public void testEmptyForm() {
        String formData = "";
        Utils.formBodyToBean(formData, title);
        assertThat(title.getPrimaryTitle()).isNull();
    }

    //@Test
    public void testBadForm1() {
        String formData = "$$##^^&&&";
        Utils.formBodyToBean(formData, title);
        assertThat(title.getPrimaryTitle()).isNull();
    }

    //@Test
    public void testBadForm2() {
        String formData = " = foo";
        Utils.formBodyToBean(formData, title);
        assertThat(title.getPrimaryTitle()).isNull();
    }

    //@Test
    public void testUrlEncodedForm() {
        String titleText = "Never-Ending Superhéro & Movie";
        String formData = String.join("", "titleID=tt123456&primaryTitle=",
                URLEncoder.encode(titleText, StandardCharsets.UTF_8),
                "&", URLEncoder.encode("bad = field & name", StandardCharsets.UTF_8), "=foo");
        Utils.formBodyToBean(formData, title);
        assertThat(title.getPrimaryTitle()).isEqualTo(titleText);
    }

    @Test
    public void testCompareWithCollator1() {
        String string = "zebra";
        String other = "église";
        // without collation, "zebra" sorts before "église":
        assertThat(string.compareTo(other) < 0).isTrue();
        // ...but with our collator, it's a dictionary sort:
        assertThat(Utils.compareUsingCollator(string, other) > 0).isTrue();
    }

    @Test
    public void testCompareWithCollator2() {
        // are nulls handled?
        String string = null;
        String other = null;
        assertThat(Utils.compareUsingCollator(string, other) == 0).isTrue();
    }

    @Test
    public void testCompareWithCollator3() {
        // is case ignored?
        String string = "église";
        String other = "Église";
        assertThat(string.compareTo(other) != 0);
        assertThat(Utils.compareUsingCollator(string, other) == 0).isTrue();
    }

    @Test
    public void testCompareWithCollator4SortList() {

        // helper class for test:
        class Foo implements Comparable<Foo> {

            Foo(String string) {
                this.string = string;
            }

            final String string;

            @Override
            public int compareTo(Foo other) {
                return Utils.compareUsingCollator(string, other.string);
            }

            @Override
            public String toString() {
                return string;
            }
        }

        final List<Foo> myList = new ArrayList();
        myList.add(new Foo("nave"));
        myList.add(new Foo("Église 3"));
        myList.add(new Foo("Zoo"));
        myList.add(new Foo("Église 2"));
        myList.add(new Foo("abbey"));
        myList.add(new Foo("église 1"));

        String expectedResult = "[abbey, église 1, Église 2, Église 3, nave, Zoo]";

        final List<Foo> mySortedList = myList.stream().sorted().collect(Collectors.toList());

        assertThat(Arrays.toString(mySortedList.toArray())).isEqualTo(expectedResult);
    }

    @Test
    public void testSortList() {
        Talent t1 = new Talent();
        t1.setTalentName("Charlie");
        Talent t2 = new Talent();
        t2.setTalentName("Baker");
        Talent t3 = new Talent();
        t3.setTalentName("Abel");

        List<Talent> myUnsortedList = Arrays.asList(t1, t2, t3);

        String expected = "Abel, Baker, Charlie";

        String unsorted = myUnsortedList.stream()
                .map(talent -> talent.getTalentName())
                .collect(Collectors.joining(", "));

        // before we do the sort:
        assertThat(unsorted).isNotEqualTo(expected);

        List<Talent> mySortedList = Utils.sortList(myUnsortedList);
        String sorted = mySortedList.stream().map(talent -> talent.getTalentName())
                .collect(Collectors.joining(", "));

        // ...but after the sort:
        assertThat(sorted).isEqualTo(expected);
    }

    @Test
    public void testRemoveCombiningMarksNull() {
        String input = null;
        String result = Utils.removeCombiningMarks(input);
        assertThat(result).isNull();
    }
    
    @Test
    public void testRemoveCombiningMarksEmpty() {
        String input = "";
        String result = Utils.removeCombiningMarks(input);
        assertThat(result).isEqualTo("");
    }

    @Test
    public void testRemoveCombiningMarks() {
        String input = "aç 8\teé事ëåï orčpžsíáýd";
        String expected = "ac 8\tee事eai orcpzsiayd";
        System.out.println("Before: " + input);
        getUnicodeNames(input);
        String result = Utils.removeCombiningMarks(input);
        assertThat(result).isEqualTo(expected);
        System.out.println("After: " + result);
        getUnicodeNames(result);
    }

    @Test
    public void testRemoveCombiningMarksNoChange() {
        String input = "æ ø ƕ ꜩ ß ﬀ";
        String result = Utils.removeCombiningMarks(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void testSplitLigatures() {
        String input = "cœur straße";
        String result = Utils.splitLigatures(input);
        assertThat(result).isEqualTo("coeur strasse");
    }
    
    @Test
    public void testRemoveMarksAndSplitLigatures() {
        String input = "anaïs cœur straße";
        String result = Utils.splitLigatures(Utils.removeCombiningMarks(input));
        assertThat(result).isEqualTo("anais coeur strasse");
    }
    
    @Test
    public void testBuildExtraSearchTermsOne() {
        String input = "anaïs cœur straße";
        String result = Utils.buildExtraSearchTerms(input);
        assertThat(result).isEqualTo("anais coeur strasse");
    }
    
    @Test
    public void testBuildExtraSearchTermsTwo() {
        String input = null;
        String result = Utils.buildExtraSearchTerms(input);
        assertThat(result).isNull();
    }
    
    private static void getUnicodeNames(String input) {
        Map<Byte, String> uniCats = getUnicodeCategories();
        if (input == null) {
            return;
        }
        for (char c : input.toCharArray()) {
            System.out.println(uniCats.get((byte) (Character.getType(c)))
                    + " - " + Character.getName(c));
        }
        System.out.println("");
    }
    
    @Test
    public void testBuildExtraSearchTermsThree() {
        String input = " ";
        String result = Utils.buildExtraSearchTerms(input);
        assertThat(result).isEqualTo("");
    }
    
    @Test
    public void testBuildExtraSearchTermsFour() {
        String input = "anaïs cœur straße anaïs cœur straße foo bar baz";
        String result = Utils.buildExtraSearchTerms(input);
        assertThat(result).isEqualTo("anais coeur strasse");
    }
    

    private static Map<Byte, String> getUnicodeCategories() {
        // a helper method: it attaches names to Character constants, 
        // to help make test results easier to inspect:
        Map<Byte, String> unicodeCategories = new HashMap<>();
        unicodeCategories.put(Character.COMBINING_SPACING_MARK, "Mc");
        unicodeCategories.put(Character.CONNECTOR_PUNCTUATION, "Pc");
        unicodeCategories.put(Character.CONTROL, "Cc");
        unicodeCategories.put(Character.CURRENCY_SYMBOL, "Sc");
        unicodeCategories.put(Character.DASH_PUNCTUATION, "Pd");
        unicodeCategories.put(Character.DECIMAL_DIGIT_NUMBER, "Nd");
        unicodeCategories.put(Character.ENCLOSING_MARK, "Me");
        unicodeCategories.put(Character.END_PUNCTUATION, "Pe");
        unicodeCategories.put(Character.FINAL_QUOTE_PUNCTUATION, "Pf");
        unicodeCategories.put(Character.FORMAT, "Cf");
        unicodeCategories.put(Character.INITIAL_QUOTE_PUNCTUATION, "Pi");
        unicodeCategories.put(Character.LETTER_NUMBER, "Nl");
        unicodeCategories.put(Character.LINE_SEPARATOR, "Zl");
        unicodeCategories.put(Character.LOWERCASE_LETTER, "Ll");
        unicodeCategories.put(Character.MATH_SYMBOL, "Sm");
        unicodeCategories.put(Character.MODIFIER_LETTER, "Lm");
        unicodeCategories.put(Character.MODIFIER_SYMBOL, "Sk");
        unicodeCategories.put(Character.NON_SPACING_MARK, "Mn");
        unicodeCategories.put(Character.OTHER_LETTER, "Lo");
        unicodeCategories.put(Character.OTHER_NUMBER, "No");
        unicodeCategories.put(Character.OTHER_PUNCTUATION, "Po");
        unicodeCategories.put(Character.OTHER_SYMBOL, "So");
        unicodeCategories.put(Character.PARAGRAPH_SEPARATOR, "Zp");
        unicodeCategories.put(Character.PRIVATE_USE, "Co");
        unicodeCategories.put(Character.SPACE_SEPARATOR, "Zs");
        unicodeCategories.put(Character.START_PUNCTUATION, "Ps");
        unicodeCategories.put(Character.SURROGATE, "Cs");
        unicodeCategories.put(Character.TITLECASE_LETTER, "Lt");
        unicodeCategories.put(Character.UNASSIGNED, "Cn");
        unicodeCategories.put(Character.UPPERCASE_LETTER, "Lu");
        return unicodeCategories;
    }
}
