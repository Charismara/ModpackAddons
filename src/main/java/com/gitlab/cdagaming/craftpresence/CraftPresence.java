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
package com.gitlab.cdagaming.craftpresence;

import com.gitlab.cdagaming.craftpresence.utils.SystemUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.DiscordUtils;
import net.minecraft.client.Minecraft;

import java.util.Timer;


public class CraftPresence {

    /**
     * If the Mod is Currently Closing and Clearing Data
     */
    public static boolean closing = false;

    /**
     * Timer Instance for this Class, used for Scheduling Events
     */
    public static Timer timerObj = new Timer(CraftPresence.class.getSimpleName());

    /**
     * The Minecraft Instance attached to this Mod
     */
    public static Minecraft instance = Minecraft.getInstance();

    /**
     * The {@link SystemUtils} Instance for this Mod
     */
    public static SystemUtils SYSTEM = new SystemUtils();

    /**
     * The {@link DiscordUtils} Instance for this Mod
     */
    public static DiscordUtils CLIENT = new DiscordUtils();
}
