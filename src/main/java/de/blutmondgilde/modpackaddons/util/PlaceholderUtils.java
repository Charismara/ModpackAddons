package de.blutmondgilde.modpackaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.versions.forge.ForgeVersion;

public class PlaceholderUtils {
    public static String replacePlaceholders(String string) {
        string = string.replaceAll("%mcversion%", Minecraft.getInstance().getVersion());
        string = string.replaceAll("%forgeversion%", ForgeVersion.getVersion());
        //string.replaceAll("%player_name%", Minecraft.getInstance().player.getDisplayName().getString());

        return string;
    }
}
