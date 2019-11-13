package org.northcoder.titlewebdemo.util;

/**
 * Removes or replaces selected characters from an input string.
 */
public class StringCleanser {

    /**
     * Removes or replaces selected characters from the user-provided string, to
     * ensure standardized data is saved - for example, the Microsoft "smart"
     * apostrophe (’) is replaced by the standard ASCII apostrophe ('). One
     * common source of such characters is data which is copy/pasted from
     * Microsoft Office documents.
     *
     * <p>
     * If the input is null, then null is returned.</p>
     *
     * <p>
     * The following are cleansed:</p>
     *
     * <p>
     * <b>Whitespace characters</b> replaced by standard space character:</p>
     *
     * <ul>
     * <li>U+00A0 (non-breaking space)
     * </ul>
     *
     * <p>
     * <b>Microsoft smart characters</b> replaced by standard equivalents:</p>
     *
     * <ul>
     * <li>U+201A (curved single quote) --&gt; U+0027 (apostrophe)
     * <li>U+2018 (curved single quote) --&gt; U+0027 (apostrophe)
     * <li>U+2019 (curved single quote) --&gt; U+0027 (apostrophe)
     * <li>U+201E (curved double quote) --&gt; U+0022 (quotation)
     * <li>U+201C (curved double quote) --&gt; U+0022 (quotation)
     * <li>U+201D (curved double quote) --&gt; U+0022 (quotation).
     * <li>U+2013 (dash) --&gt; U+002D (hyphen)
     * <li>U+2014 (dash) --&gt; U+002D (hyphen)
     * </ul>
     *
     * <p>
     * <b>Control characters</b> removed:</p>
     *
     * <ul>
     * <li>U+007F (delete)
     * <li>U+0080 through U+009F (C1 controls)
     * </ul>
     *
     * <p>
     * <b>Misc. Others</b> --&gt; Replacement Character</p>
     *
     * <ul>
     * <li>U+2015 (horizontal bar) --&gt; U+002D (hyphen)
     * </ul>
     *
     * <p>
     * Finally, the string is trimmed using the standard Java
     * {@link String#trim() trim} method.</p>
     *
     * @param input the string prior to cleansing
     * @return the cleansed string
     */
    public static String cleanse(String input) {
        if (input == null) {
            return null;
        }
        String cleansed = input
                .replaceAll("\u00A0", " ") // non-breaking space
                // Microsoft smart characters:
                .replaceAll("[\u201A\u2018\u2019]", "'")
                .replaceAll("[\u201E\u201C\u201D]", "\"")
                .replaceAll("[\u2013\u2014]", "-")
                // Selected control characters:
                .replaceAll("[\u0080-\u009F]", "")
                .replaceAll("\u007F", "")
                // Misc others:
                .replaceAll("[\u2015]", "-")
                .trim();

        if (!cleansed.equals(input)) {
            LoggerUtils.LOGGER.info(String.format("Input was cleansed:\nBefore: [%s]\nAfter : [%s].",
                    input, cleansed));
        }

        return cleansed;
    }

}
