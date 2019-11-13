package org.northcoder.titlewebdemo.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.northcoder.titlewebdemo.beans.DemoBean;
import org.northcoder.titlewebdemo.validation.ValidationError;

/**
 * Miscellaneous utilities for handling HTML form data, related TitleDemo
 * JavaBean objects, and textual data.
 *
 * <p>
 * <b>IMPORTANT:</b> Form-related utilities assume that the name of the HTML
 * form field is identical to the name of the related bean field.
 */
public class Utils {

    /**
     * Loads data from an HTML form into a TitleDemo bean.
     * <p>
     * <b>IMPORTANT:</b> Assumes there is no multipart data or field arrays -
     * just a simple form submission. For example, if a form has a set radio
     * buttons all bound to one name, then that data is not currently handled by
     * this method. Similarly, attachments etc. are not currently handled.
     *
     * @param <T> Any TitleDemo object - the target into which the form data
     * will be loaded
     * @param formData a string from an HTTP form submission in the format:
     * {@literal "foo=bar&baz=bat&..."}. Each field name and data value are URL
     * encoded, and concatenated with '&amp;' and '=' by the http POST action.
     * Here, the string is provided by <a href="http://sparkjava.com/">the Spark
     * framework</a>'s
     * <a href="https://static.javadoc.io/com.sparkjava/spark-core/2.9.1/spark/Request.html#body--">
     * spark.Request.body()</a> - using a wrapper around the HTTP servlet
     * request object.
     * @param bean a TitleDemo bean prior to the form data being loaded
     * @return the {@link DemoBean TitleDemo JavaBean} after the form data has
     * been loaded
     */
    public static <T extends DemoBean> T formBodyToBean(String formData, T bean) {
        Map<String, String> formDataMap = parseFormData(formData);
        return setBeanProperties(formDataMap, bean);
    }

    private static Map<String, String> parseFormData(String rawFormData) {
        Map<String, String> formData = new HashMap<>();
        if (rawFormData == null) {
            return formData;
        } else {
            for (String entry : rawFormData.split("&")) {
                // discard data with no key value:
                if (entry.contains("=") && !entry.startsWith("=")) {
                    String[] keyVal = entry.split("=");
                    if (keyVal.length == 2 && !keyVal[0].isBlank()) {
                        formData.put(decodeValue(keyVal[0]),
                                StringCleanser.cleanse(decodeValue(keyVal[1])));
                    }
                }
            }
        }
        return formData;
    }

    /**
     * Decodes data which has been urlencoded.
     */
    private static String decodeValue(String encodedValue) {
        String decodedValue = null;
        try {
            decodedValue = URLDecoder.decode(encodedValue, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException ex) {
            // do nothing - quietly discard the data.
            LoggerUtils.LOGGER.error(String.format("Error URL-decoding the following text: [%s]", encodedValue), ex);
        }
        return decodedValue;
    }

    private static <T extends DemoBean> T setBeanProperties(Map<String, String> formMap, T bean) {
        // Here we are doing an initial validation check of our form data: Does the
        // value provided by the user in the web form match the data type of the 
        // related field in the data bean? Or is the user trying to save a string
        // to an Integer field, and so on? Later on we will do more specific data 
        // validations (using Hibernate Validator).
        final List<ValidationError> validationErrors = new ArrayList();
        formMap.forEach((fieldName, value) -> {
            setBeanProperty(bean, fieldName, value, validationErrors);
        });
        bean.getFormValidationErrors().clear();
        bean.getFormValidationErrors().addAll(validationErrors);
        return bean;
    }

    private static <T extends DemoBean> void setBeanProperty(T bean,
            String fieldName, String value, List<ValidationError> validationErrors) {
        Field field = null;
        try {
            field = FieldUtils.getDeclaredField(bean.getClass(), fieldName, true);
            if (field == null) {
                // do nothing, just return at the end.
            } else if (field.getType().equals(String.class)) {
                BeanUtils.setProperty(bean, fieldName, value);
            } else if (field.getType().equals(Integer.TYPE)
                    || field.getType().equals(Integer.class)) {
                BeanUtils.setProperty(bean, fieldName, Integer.parseInt(value));
            } else if (field.getType().equals(Float.TYPE)
                    || field.getType().equals(Float.class)) {
                BeanUtils.setProperty(bean, fieldName, Float.parseFloat(value));
            } else if (field.getType().equals(Double.TYPE)
                    || field.getType().equals(Double.class)) {
                BeanUtils.setProperty(bean, fieldName, Double.parseDouble(value));
            } else if (field.getType().equals(Boolean.TYPE)
                    || field.getType().equals(Boolean.class)) {
                BeanUtils.setProperty(bean, fieldName, Double.parseDouble(value));
            } else if (field.getType().equals(java.util.UUID.class)) {
                BeanUtils.setProperty(bean, fieldName, java.util.UUID.fromString(value));
            } else if (field.getType().equals(char[].class)) {
                BeanUtils.setProperty(bean, fieldName, value.toCharArray());
            } else {
                LoggerUtils.LOGGER.error(String.format(
                        "Unexpected data type in Utils.setBeanProperty for field [%s] in bean [%s]",
                        fieldName, bean.getClass().getName()));
            }
        } catch (IllegalAccessException | InvocationTargetException | NumberFormatException ex) {
            // Handle the unusable form data item.
            if (field != null) {
                LoggerUtils.LOGGER.error(String.format(
                        "Error setting field [%s] of type [%s] in bean [%s] to value [%s]",
                        fieldName, field.getType().getSimpleName(),
                        bean.getClass().getSimpleName(), value), ex);
                validationErrors.add(new ValidationError(fieldName, value,
                        field.getType().getSimpleName()));
            } else {
                // Ignore it. Tthe form data contained a field not found in the related
                // bean. This can legitimately happen - e.g. checkbox handling. Or it 
                // can happen maliciously - in which case, also ignore it!
            }
        }
    }

    /**
     * A method which uses {@link java.text.Collator Collator}'s string compare
     * method. The collator is configured to consider base letters when
     * performing a comparison, but to ignore accents, diacritics and
     * upper/lower case differences. This is typically used when sorting
     * collections of beans (which in turn are generated by SQL result sets), to
     * ensure that words such as "eglise" and "église" are sorted alongside each
     * other. Standard binary sorting would otherwise sort words such as
     * "église" after words such as "zoo".
     *
     * @param string One of the strings used in the string comparison.
     * @param other The other string used in the string comparison.
     * @return The comparison result from the Collator's compare method.
     */
    public static int compareUsingCollator(String string, String other) {
        if (string == null && other == null) {
            return 0;
        }
        if (string == null) {
            return 1;
        }
        if (other == null) {
            return -1;
        }
        final Collator collator = Collator.getInstance();
        // consider base letters, but ignore diacritics and upper/lower case:
        collator.setStrength(Collator.PRIMARY);
        // accented characters are not decomposed:
        collator.setDecomposition(Collator.NO_DECOMPOSITION);
        return collator.compare(string, other);
    }

    /**
     * Primarily used by the {@link org.northcoder.titlewebdemo.dao.JdbiDAO}
     * methods, to sort result sets. This is done, in case the SQL does not do
     * the sort already.
     *
     * @param <T> the type of DemoBeans to be sorted
     * @param unsortedList the unsorted list of DemoBeans
     * @return the sorted list
     */
    public static <T extends DemoBean> List<T> sortList(List<T> unsortedList) {
        return unsortedList.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Removes combining marks, such as diacritics, accents, etc. from a string.
     * For example, "église" will become "eglise". This is done to support data
     * searches on the client side (e.g. in DataTables). If a data value on a
     * web page is "église", then a hidden, but searchable, field in the web
     * page will also contain "eglise".
     *
     * <p>
     * When a search is performed - for example, using the DataTables filter box
     * - the client also similarly removes combining marks from the search term
     * (using JavaScript). This way, it no longer matters whether a search term
     * is entered as "église" or "eglise". Either term will find a result
     * containing either of the following: "église", "eglise". For DataTables,
     * searches are case insensitive, so the words "Église" and "Eglise" can
     * also be used as search terms interchangeably.</p>
     *
     * <p>
     * Technically: the NFD normalizer does the decomposition, by separating a
     * single accented character into the base letter and the separate accent
     * mark. So, for "é", this is the Unicode character:</p>
     *
     * <p>
     * Ll - LATIN SMALL LETTER E WITH ACUTE</p>
     *
     * <p>
     * After NFD decomposition, this becomes the following two separate Unicode
     * characters:</p>
     *
     * Ll - LATIN SMALL LETTER E<br>Mn - COMBINING ACUTE ACCENT
     *
     * <p>
     * Then, the regular expression p{M} is used to find characters which belong
     * to any of the Unicode "M" (mark) categories:</p>
     *
     * Character.COMBINING_SPACING_MARK "Mc"<br>Character.ENCLOSING_MARK
     * "Me"<br>
     * Character.NON_SPACING_MARK, "Mn"
     *
     * <p>
     * Any such characters are removed from the string.</p>
     *
     * <p>
     * Graphemes which do not contain any marks are left unchanged.</p>
     *
     * <p>
     * NOTE that some graphemes, such as "æ" and "ø" are not decomposed: "æ"
     * does not become "ae"; "ø" does not have the same base letter as "o". The
     * German character "ß" does not become "ss", and so on... These would need
     * to be handled as special cases, if required. See also
     * {@link #splitLigatures(java.util.String)}.</p>
     *
     * @param input the string prior to removal of any combining marks
     * @return the string with combining marks removed
     */
    public static String removeCombiningMarks(String input) {
        if (input == null) {
            return null;
        }
        return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    /**
     * Splits selected glyphs into their constituent graphemes, for example, "œ"
     * into "oe". This is done to support searching and filtering, for example
     * to allow a user to search for "coeur" or "cœur" without worrying whether
     * the searched text contains one or the other value (or both).
     *
     * In the context of searching, case is not relevant - searches are
     * case-insensitive, which is why all the targets are lowercase.
     *
     * <p>
     * From Wikipedia: "...a ligature occurs where two or more graphemes or
     * letters are joined as a single glyph. An example is the character æ as
     * used in English, in which the letters a and e are joined."</p>
     *
     * <p>
     * See also {@link #removeCombiningMarks(java.lang.String)}.
     *
     * @param input the unsplit string
     * @return the string after splitting
     */
    public static String splitLigatures(String input) {
        if (input == null) {
            return null;
        }
        return input
                .replaceAll("æ", "ae")
                .replaceAll("Æ", "ae")
                .replaceAll("ꜵ", "ao")
                .replaceAll("Ꜵ", "ao")
                .replaceAll("ꜷ", "au")
                .replaceAll("Ꜷ", "au")
                .replaceAll("ﬀ", "ff")
                .replaceAll("ﬃ", "ffi")
                .replaceAll("ﬄ", "ffl")
                .replaceAll("ﬁ", "fi")
                .replaceAll("ﬂ", "fl")
                .replaceAll("œ", "oe")
                .replaceAll("Œ", "oe")
                .replaceAll("ꝏ", "oo")
                .replaceAll("Ꝏ", "oo")
                .replaceAll("ß", "ss")
                .replaceAll("ẞ", "ss")
                .replaceAll("ᵫ", "ue");
    }

    /**
     * For example, "ø" is mapped to "o". Mappings included here may not be
     * valid linguistic mappings (ø is a different base letter from o). But the
     * mapping can help searching and filtering on the web application.
     *
     * @param input the text before mapping
     * @return the text after mapping
     */
    public static String mapMiscellaneous(String input) {
        return input
                .replaceAll("ø", "o");
    }

    /**
     * Finds words which contain marks and ligatures, and creates versions of
     * those words without the marks and ligatures. This is useful to support
     * searching and filtering in datatables. Only those words which are changed
     * by this method are included in the return string.
     *
     * @param input words to be cleansed
     * @return words after being cleansed
     */
    public static String buildExtraSearchTerms(String input) {
        if (input == null) {
            return null;
        }
        Set<String> outputTokens = new LinkedHashSet();
        // A space is best for our needs here, since it reflects the 
        // way filtering works in datatables.  For example, do not
        // split hyphenated words here!
        Arrays.asList(input.split(" ")).forEach((inputToken) -> {
            String outputToken = mapMiscellaneous(splitLigatures(
                    removeCombiningMarks(inputToken)));
            if (!outputToken.equalsIgnoreCase(inputToken)) {
                outputTokens.add(outputToken);
            }
        });
        return String.join(" ", outputTokens);
    }
}
