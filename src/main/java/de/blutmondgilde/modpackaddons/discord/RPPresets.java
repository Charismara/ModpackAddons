package de.blutmondgilde.modpackaddons.discord;

import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.RichPresence;
import de.blutmondgilde.modpackaddons.config.Config;

public enum RPPresets {
    Mainmenu(new RichPresence.Builder()
            .setState("In the main menu")
            .setDetails(Config.modpackName.get())
            .setStartTimestamp(System.currentTimeMillis())
            .setLargeImage("mainmenu", "In the main menu")
            .setSmallImage("icon", "Minecraft Forge")
            .build()
    ),
    SinglePlayer(new RichPresence.Builder()
            .setState("In singleplayer")
            .setDetails(Config.modpackName.get())
            .setStartTimestamp(System.currentTimeMillis())
            .setLargeImage("overworld", "In the Overworld") //TODO change Dimension to current Player dim
            .setSmallImage("singleplayer", "In singleplayer")
            .build()
    ),
    MultiPlayer(new RichPresence.Builder()
            .setState("In multiplayer")
            .setDetails(Config.modpackName.get())
            .setStartTimestamp(System.currentTimeMillis())
            .setLargeImage("overworld", "In the Overworld") //TODO change Dimension to current Player dim
            .setSmallImage("multiplayer", "In multiplayer") //TODO show Server IP
            .build()
    );

    private RichPresence richPresence;

    RPPresets(RichPresence richPresence) {
        this.richPresence = richPresence;
    }

    public RichPresence getRichPresence() {
        return richPresence;
    }
}
