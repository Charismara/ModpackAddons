package de.blutmondgilde.modpackaddons.network;

import de.blutmondgilde.modpackaddons.ModpackAddons;
import de.blutmondgilde.modpackaddons.discord.RPPresets;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class LeftWorldPacket {
    public static void encode(LeftWorldPacket msg, PacketBuffer buffer) {
    }

    public static LeftWorldPacket decode(PacketBuffer buffer) {
        return new LeftWorldPacket();
    }

    public static class Handler {
        public static void handle(final LeftWorldPacket msg, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(
                    () -> {
                        ModpackAddons.discord.sendRichPresence(RPPresets.Mainmenu.getRichPresence().build());
                    }
            );
            context.get().setPacketHandled(true);
        }
    }
}
