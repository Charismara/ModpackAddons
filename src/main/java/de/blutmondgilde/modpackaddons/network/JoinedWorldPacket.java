package de.blutmondgilde.modpackaddons.network;

import de.blutmondgilde.modpackaddons.ModpackAddons;
import de.blutmondgilde.modpackaddons.discord.Discord;
import de.blutmondgilde.modpackaddons.discord.RPPresets;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class JoinedWorldPacket {
    public final String dimensionName, dimensionIcon, address;
    public final boolean isServer;


    public JoinedWorldPacket(Discord.Dimensions dimension, boolean isServer) {
        this.isServer = isServer;
        switch (dimension) {
            case Nether:
                this.dimensionName = "Nether";
                this.dimensionIcon = "nether";
                break;
            case End:
                this.dimensionName = "End";
                this.dimensionIcon = "end";
                break;
            default:
                this.dimensionName = "Overworld";
                this.dimensionIcon = "overworld";
                break;
        }

        this.address = "";
    }

    public JoinedWorldPacket(Discord.Dimensions dimension, boolean isServer, String address) {
        this.isServer = isServer;
        switch (dimension) {
            case Nether:
                this.dimensionName = "Nether";
                this.dimensionIcon = "nether";
                break;
            case End:
                this.dimensionName = "End";
                this.dimensionIcon = "end";
                break;
            default:
                this.dimensionName = "Overworld";
                this.dimensionIcon = "overworld";
                break;
        }

        this.address = address;
    }

    public JoinedWorldPacket(boolean isServer, String dimensionName, String dimensionIcon, String address) {
        this.isServer = isServer;
        this.dimensionIcon = dimensionIcon;
        this.dimensionName = dimensionName;
        this.address = address;
    }

    public static void encode(JoinedWorldPacket msg, PacketBuffer buffer) {
        buffer.writeBoolean(msg.isServer);
        buffer.writeString(msg.dimensionName);
        buffer.writeString(msg.dimensionIcon);
        buffer.writeString(msg.address);
    }

    public static JoinedWorldPacket decode(PacketBuffer buffer) {
        return new JoinedWorldPacket(buffer.readBoolean(), buffer.readString(), buffer.readString(), buffer.readString());
    }

    public static class Handler {
        public static void handle(final JoinedWorldPacket msg, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(
                    () -> {
                        if (msg.isServer) {
                            ModpackAddons.discord.sendRichPresence(
                                    RPPresets.MultiPlayer.getRichPresence()
                                            .setLargeImage(msg.dimensionIcon, "In the " + msg.dimensionName)
                                            .setSmallImage("multiplayer", Minecraft.getInstance().getCurrentServerData().serverIP)
                                            .build()
                            );
                        } else {
                            ModpackAddons.discord.sendRichPresence(
                                    RPPresets.SinglePlayer.getRichPresence()
                                            .setLargeImage(msg.dimensionIcon, "In the " + msg.dimensionName)
                                            .setSmallImage("singleplayer", "In singleplayer")
                                            .build()
                            );
                        }
                    }
            );
            context.get().setPacketHandled(true);
        }
    }
}
