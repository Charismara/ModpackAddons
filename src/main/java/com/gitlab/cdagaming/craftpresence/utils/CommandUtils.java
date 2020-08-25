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
package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Command Utilities for Synchronizing and Initializing Data
 *
 * @author CDAGaming
 */
public class CommandUtils {
    /**
     * Whether you are on the Main Menu in Minecraft
     */
    public static boolean isInMainMenu = false;

    /**
     * Whether you are on the Loading Stage in Minecraft
     */
    public static boolean isLoadingGame = false;


    /**
     * Initializes Essential Data<p>
     * (In this case, Pack Data and Available RPC Icons)
     */
    public static void init() {
        DiscordAssetUtils.loadAssets();
    }

    /**
     * Synchronizes RPC Data towards that of being in a Loading State
     */
    public static void setLoadingPresence() {
        // Form Argument Lists
        List<Tuple<String, String>> loadingArgs = Lists.newArrayList();

        // Add All Generalized Arguments, if any
        if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
            loadingArgs.addAll(CraftPresence.CLIENT.generalArgs);
        }

        CraftPresence.CLIENT.STATUS = "ready";

        isLoadingGame = true;
    }

    /**
     * Synchronizes RPC Data towards that of being in the Main Menu
     */
    public static void setMainMenuPresence() {
        // Form Argument Lists
        List<Tuple<String, String>> mainMenuArgs = Lists.newArrayList();

        // Add All Generalized Arguments, if any
        if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
            mainMenuArgs.addAll(CraftPresence.CLIENT.generalArgs);
        }

        // Clear Loading Game State, if applicable
        if (isLoadingGame) {
            CraftPresence.CLIENT.initArgument("&MAINMENU&");

            isLoadingGame = false;
        }

        isInMainMenu = true;
    }
}
