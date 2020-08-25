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
package com.gitlab.cdagaming.craftpresence.utils.discord;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.RichPresence;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.User;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.pipe.PipeStatus;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Variables and Methods used to update the RPC Presence States to display within Discord
 *
 * @author CDAGaming
 */
public class DiscordUtils {
    /**
     * A Mapping of the Arguments available to use as RPC Message Placeholders
     */
    private final List<Tuple<String, String>> messageData = Lists.newArrayList();
    /**
     * A Mapping of the Arguments available to use as Icon Key Placeholders
     */
    private final List<Tuple<String, String>> iconData = Lists.newArrayList();
    /**
     * A Mapping of the Arguments attached to the &MODS& RPC Message placeholder
     */
    private final List<Tuple<String, String>> modsArgs = Lists.newArrayList();
    /**
     * A Mapping of the Arguments attached to the &IGN& RPC Message Placeholder
     */
    private final List<Tuple<String, String>> playerInfoArgs = Lists.newArrayList();
    /**
     * The Current User, tied to the Rich Presence
     */
    public User CURRENT_USER;
    /**
     * The Join Request User Data, if any
     */
    public User REQUESTER_USER;
    /**
     * The current RPC Status (Ex: ready, errored, disconnected)
     */
    public String STATUS;
    /**
     * The Current Message tied to the Party/Game Status Field of the RPC
     */
    public String GAME_STATE;
    /**
     * The Current Message tied to the current action / Details Field of the RPC
     */
    public String DETAILS;
    /**
     * The Current Small Image Icon being displayed in the RPC, if any
     */
    public String SMALLIMAGEKEY;
    /**
     * The Current Message tied to the Small Image, if any
     */
    public String SMALLIMAGETEXT;
    /**
     * The Current Large Image Icon being displayed in the RPC, if any
     */
    public String LARGEIMAGEKEY;
    /**
     * The Current Message tied to the Large Image, if any
     */
    public String LARGEIMAGETEXT;
    /**
     * The 18-character Client ID Number, tied to the game profile data attached to the RPC
     */
    public String CLIENT_ID;
    /**
     * The Current Starting Unix Timestamp from Epoch, used for Elapsed Time
     */
    public long START_TIMESTAMP;
    /**
     * The Party Session ID that's tied to the RPC, if any
     */
    public String PARTY_ID;
    /**
     * The Current Size of the Party Session, if in a Party
     */
    public int PARTY_SIZE;
    /**
     * The Maximum Size of the Party Session, if in a Party
     */
    public int PARTY_MAX;
    /**
     * The Current Party Join Secret Key, if in a Party
     */
    public String JOIN_SECRET;
    /**
     * The Current Ending Unix Timestamp from Epoch, used for Time Until if combined with {@link DiscordUtils#START_TIMESTAMP}
     */
    public long END_TIMESTAMP;
    /**
     * The Current Match Secret Key tied to the RPC, if any
     */
    public String MATCH_SECRET;
    /**
     * The Current Spectate Secret Key tied to the RPC, if any
     */
    public String SPECTATE_SECRET;
    /**
     * The Instance Code attached to the RPC, if any
     */
    public byte INSTANCE;
    /**
     * A Mapping of the General RPC Arguments allowed in adjusting Presence Messages
     */
    public List<Tuple<String, String>> generalArgs = Lists.newArrayList();
    /**
     * An Instance of the {@link IPCClient}, responsible for sending and receiving RPC Events
     */
    public IPCClient ipcInstance;
    /**
     * Whether Discord is currently awaiting a response to a Ask to Join or Spectate Request, if any
     */
    public boolean awaitingReply = false;
    /**
     * A Mapping of the Last Requested Image Data
     * <p>Used to prevent sending duplicate packets and cache data for repeated images in other areas
     * <p>Format: lastAttemptedKey, lastResultingKey
     */
    private Tuple<String, String> lastRequestedImageData = new Tuple<>();
    /**
     * An Instance containing the Current Rich Presence Data
     * <p>Also used to prevent sending duplicate packets with the same presence data, if any
     */
    private RichPresence currentPresence;

    /**
     * Setup any Critical Methods needed for the RPC
     * <p>In this case, ensures a Thread is in place to shut down the RPC onExit
     */
    public synchronized void setup() {
        final Thread shutdownThread = new Thread("CraftPresence-ShutDown-Handler") {
            @Override
            public void run() {
                CraftPresence.closing = true;
                CraftPresence.timerObj.cancel();

                shutDown();
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }


    /**
     * Synchronizes the Specified Argument as an RPC Message or an Icon Placeholder
     *
     * @param argumentName The Specified Argument to Synchronize for
     * @param insertString The String to attach to the Specified Argument
     * @param isIconData   Whether the Argument is an RPC Message or an Icon Placeholder
     */
    public void syncArgument(String argumentName, String insertString, boolean isIconData) {
        // Remove and Replace Placeholder Data, if the placeholder needs Updates
        if (!StringUtils.isNullOrEmpty(argumentName)) {
            if (isIconData) {
                synchronized (iconData) {
                    if (iconData.removeIf(e -> e.getFirst().equalsIgnoreCase(argumentName) && !e.getSecond().equalsIgnoreCase(insertString))) {
                        iconData.add(new Tuple<>(argumentName, insertString));
                    }
                }
            } else {
                synchronized (messageData) {
                    if (messageData.removeIf(e -> e.getFirst().equalsIgnoreCase(argumentName) && !e.getSecond().equalsIgnoreCase(insertString))) {
                        messageData.add(new Tuple<>(argumentName, insertString));
                    }
                }
            }
        }
    }

    /**
     * Initialize the Specified Arguments as Empty Data
     *
     * @param isIconData Whether the Argument is an RPC Message or an Icon Placeholder
     * @param args       The Arguments to Initialize
     */
    public void initArgument(boolean isIconData, String... args) {
        // Initialize Specified Arguments to Empty Data
        if (isIconData) {
            for (String argumentName : args) {
                synchronized (iconData) {
                    iconData.removeIf(e -> e.getFirst().equalsIgnoreCase(argumentName));
                    iconData.add(new Tuple<>(argumentName, ""));
                }
            }
        } else {
            for (String argumentName : args) {
                synchronized (messageData) {
                    messageData.removeIf(e -> e.getFirst().equalsIgnoreCase(argumentName));
                    messageData.add(new Tuple<>(argumentName, ""));
                }
            }
        }
    }

    /**
     * Initialize the Specified Arguments in all regards
     *
     * @param args The Arguments to Initialize as Empty Data for both Icons and RPC Messages
     */
    public void initArgument(String... args) {
        initArgument(false, args);
        initArgument(true, args);
    }

    /**
     * Synchronizes and Updates the Rich Presence Data, if needed and connected
     *
     * @param presence The New Presence Data to apply
     */
    public void updatePresence(final RichPresence presence) {
        if (presence != null &&
                (currentPresence == null || !presence.toJson().toString().equals(currentPresence.toJson().toString())) &&
                ipcInstance.getStatus() == PipeStatus.CONNECTED) {
            ipcInstance.sendRichPresence(presence);
            currentPresence = presence;
        }
    }

    /**
     * Shutdown the RPC and close related resources, as well as Clearing any remaining Runtime Client Data
     */
    public synchronized void shutDown() {
        try {
            ipcInstance.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Clear User Data before final clear and shutdown
        STATUS = "disconnected";
    }
}
