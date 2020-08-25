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
package com.gitlab.cdagaming.craftpresence.utils.discord.assets;

import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Utilities related to locating and Parsing available Discord Assets
 * <p>Uses the current Client ID in use to locate Discord Icons and related Assets
 *
 * @author CDAGaming
 */
public class DiscordAssetUtils {
    /**
     * Mapping storing the Icon Keys and Asset Data attached to the Current Client ID
     */
    private static Map<String, DiscordAsset> ASSET_LIST = Maps.newHashMap();

    /**
     * Determines if the Specified Icon Key is present under the Current Client ID
     *
     * @param key The Specified Icon Key to Check
     * @return {@code true} if the Icon Key is present and able to be used
     */
    public static boolean contains(final String key) {
        final String formattedKey = StringUtils.isNullOrEmpty(key) ? "" : StringUtils.formatPackIcon(key.replace(" ", "_"));
        return ASSET_LIST.containsKey(formattedKey);
    }
}
