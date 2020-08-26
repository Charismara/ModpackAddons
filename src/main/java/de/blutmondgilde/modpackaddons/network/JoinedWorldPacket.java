package de.blutmondgilde.modpackaddons.network;

import de.blutmondgilde.modpackaddons.ModpackAddons;
import de.blutmondgilde.modpackaddons.events.UpdateDiscordEvent;
import de.blutmondgilde.modpackaddons.util.LogHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class JoinedWorldPacket {
    public final boolean isServer;


    public JoinedWorldPacket(final PlayerEntity player) {
        this.isServer = ModpackAddons.isMultiplayer;
    }

    public JoinedWorldPacket(boolean isServer) {
        this.isServer = isServer;
    }

    public static void encode(JoinedWorldPacket msg, PacketBuffer buffer) {
        buffer.writeBoolean(msg.isServer);
    }

    public static JoinedWorldPacket decode(PacketBuffer buffer) {
        return new JoinedWorldPacket(buffer.readBoolean());
    }

    public static class Handler {
        public static void handle(final JoinedWorldPacket msg, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(
                    () -> {
                        MinecraftForge.EVENT_BUS.post(new UpdateDiscordEvent(msg.isServer));
                        LogHelper.getLogger("DiscordUpdate").debug("Handeled Packet with information: {isServer: " + msg.isServer + "}");
                    }
            );
            context.get().setPacketHandled(true);
        }
    }
}
