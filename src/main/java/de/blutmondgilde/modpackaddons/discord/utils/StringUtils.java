/*
 * MIT License
 *
 * Copyright (c) 2018 - 2020 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.blutmondgilde.modpackaddons.discord.utils;

import java.util.regex.Pattern;

/**
 * String Utilities for interpreting Strings and Basic Data Types
 *
 * @author CDAGaming
 */
public class StringUtils {
    /**
     * The Character to be interpreted as the start to a Formatting Character
     */
    private static final char COLOR_CHAR = '\u00A7';

    /**
     * Regex Pattern for Color and Formatting Codes
     */
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]");

    /**
     * Regex Pattern for Brackets containing Digits
     */
    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\([^0-9]*\\d+[^0-9]*\\)");

    /**
     * Determines whether a String classifies as NULL or EMPTY
     *
     * @param entry The String to evaluate
     * @return {@code true} if Entry is classified as NULL or EMPTY
     */
    public static boolean isNullOrEmpty(final String entry) {
        return entry == null || entry.isEmpty() || entry.equalsIgnoreCase("null");
    }

    /**
     * Converts a String into a Valid and Acceptable Icon Format
     *
     * @param original The original String to evaluate
     * @return The converted and valid String, in an iconKey Format
     */
    public static String formatPackIcon(final String original) {
        String formattedKey = original;
        if (isNullOrEmpty(formattedKey)) {
            return formattedKey;
        } else {
            if (formattedKey.contains("\\s")) {
                formattedKey = formattedKey.replaceAll("\\s+", "");
            }
            if (formattedKey.contains("'")) {
                formattedKey = formattedKey.replaceAll("'", "");
            }
            if (formattedKey.contains(".")) {
                formattedKey = formattedKey.replaceAll("\\.", "_");
            }
            if (BRACKET_PATTERN.matcher(formattedKey).find()) {
                formattedKey = BRACKET_PATTERN.matcher(formattedKey).replaceAll("");
            }
            if (STRIP_COLOR_PATTERN.matcher(formattedKey).find()) {
                formattedKey = STRIP_COLOR_PATTERN.matcher(formattedKey).replaceAll("");
            }
            return formattedKey.toLowerCase().trim();
        }
    }
}
