package de.blutmondgilde.modpackaddons.events;

import de.blutmondgilde.modpackaddons.ModpackAddons;
import de.blutmondgilde.modpackaddons.discord.RPPresets;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.Event;

public class UpdateDiscordEvent extends Event {
    public UpdateDiscordEvent(final boolean isServer) {
        final String[] data = getDimName();

        if (isServer) {

            try {
                ModpackAddons.discord.ifPresent(
                        (d) -> d.sendRichPresence(
                                RPPresets.MultiPlayer.getRichPresence()
                                        .setLargeImage(data[0], data[1])
                                        .setState("On " + Minecraft.getInstance().getCurrentServerData().serverIP)
                                        .build()
                        )
                );
            } catch (Exception ignored) {
                ModpackAddons.discord.ifPresent((d) -> d.sendRichPresence(
                        RPPresets.SinglePlayer.getRichPresence()
                                .setLargeImage(data[0], data[1])
                                .build()
                ));
            }
        } else {
            ModpackAddons.discord.ifPresent((d) -> d.sendRichPresence(
                    RPPresets.SinglePlayer.getRichPresence()
                            .setLargeImage(data[0], data[1])
                            .build()
            ));
        }
    }

    private String[] getDimName() {
        final String[] data = new String[2];
        switch (Minecraft.getInstance().player.world.func_230315_m_().func_242725_p().getPath()) {
            default:
                data[0] = "overworld";
                data[1] = "In the Overworld";
                break;
            case "the_nether":
                data[0] = "nether";
                data[1] = "In the Nether";
                break;
            case "the_end":
                data[0] = "end";
                data[1] = "In the End";
                break;
        }
        return data;
    }
}
