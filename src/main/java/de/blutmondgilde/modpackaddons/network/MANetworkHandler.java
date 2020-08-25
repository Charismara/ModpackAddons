package de.blutmondgilde.modpackaddons.network;

import de.blutmondgilde.modpackaddons.ModpackAddons;
import de.blutmondgilde.modpackaddons.discord.Discord;
import de.blutmondgilde.modpackaddons.discord.RPPresets;
import de.blutmondgilde.modpackaddons.util.Constants;
import de.blutmondgilde.modpackaddons.util.LogHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MANetworkHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Constants.MOD_ID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        int disc = 0;

        HANDLER.registerMessage(disc++, LeftWorldPacket.class, LeftWorldPacket::encode, LeftWorldPacket::decode, LeftWorldPacket.Handler::handle);
        HANDLER.registerMessage(disc++, JoinedWorldPacket.class, JoinedWorldPacket::encode, JoinedWorldPacket::decode, JoinedWorldPacket.Handler::handle);


        LogHelper.getLogger("NetworkHandler").debug("Registered " + disc + " network packets.");
    }

    public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
        HANDLER.send(target, message);
    }

    @OnlyIn(Dist.CLIENT)
    public static <MSG> void sendToServer(MSG message) {
        HANDLER.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, final PlayerEntity player) {
        send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
    }

    @SubscribeEvent
    public static void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent e) {
        final PlayerEntity player = e.getPlayer();
        final World world = player.getEntityWorld();
        final Discord.Dimensions dim;
        switch (world.func_230315_m_().field_242709_C.getPath()) {
            case "the_nether":
                dim = Discord.Dimensions.Nether;
                break;
            case "the_end":
                dim = Discord.Dimensions.End;
                break;
            default:
                dim = Discord.Dimensions.Overworld;
                break;
        }

        sendToPlayer(new JoinedWorldPacket(dim, world.isRemote), player);
        LogHelper.getLogger("NetworkHandler").debug("Send a DiscordDataPack. (Join)");
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onPlayerLeave(final ClientPlayerNetworkEvent.LoggedOutEvent e) {
        //sendToPlayer(new LeftWorldPacket(), e.getPlayer());
        ModpackAddons.discord.sendRichPresence(RPPresets.Mainmenu.getRichPresence().build());
        LogHelper.getLogger("NetworkHandler").debug("Send a DiscordDataPack. (Leave)");
    }
}
