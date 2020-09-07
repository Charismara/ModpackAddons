package de.blutmondgilde.modpackaddons.discord;

import de.blutmondgilde.modpackaddons.discord.utils.discord.rpc.entities.RichPresence;
import de.blutmondgilde.modpackaddons.config.Config;

public enum RPPresets {
    Mainmenu(new RichPresence.Builder()
            .setState("In the main menu")
            .setDetails(Config.modpackName.get())
            .setStartTimestamp(System.currentTimeMillis())
            .setLargeImage("icon", "Minecraft Forge")
            .setSmallImage("mainmenu", "In the main menu")
    ),
    SinglePlayer(new RichPresence.Builder()
            .setState("In singleplayer")
            .setDetails(Config.modpackName.get())
            .setStartTimestamp(System.currentTimeMillis())
            .setLargeImage("overworld", "In the Overworld")
            .setSmallImage("singleplayer", "In singleplayer")
    ),
    MultiPlayer(new RichPresence.Builder()
            .setState("In multiplayer")
            .setDetails(Config.modpackName.get())
            .setStartTimestamp(System.currentTimeMillis())
            .setLargeImage("overworld", "In the Overworld")
            .setSmallImage("multiplayer", "In multiplayer") //TODO show Server IP
    );

    private final RichPresence.Builder richPresence;

    RPPresets(RichPresence.Builder richPresence) {
        this.richPresence = richPresence;
    }

    public RichPresence.Builder getRichPresence() {
        return richPresence;
    }
}
