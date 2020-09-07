package de.blutmondgilde.modpackaddons;

import de.blutmondgilde.modpackaddons.config.Config;
import de.blutmondgilde.modpackaddons.discord.Discord;
import de.blutmondgilde.modpackaddons.network.MANetworkHandler;
import de.blutmondgilde.modpackaddons.util.Constants;
import de.blutmondgilde.modpackaddons.util.ImageUtils;
import de.blutmondgilde.modpackaddons.util.LogHelper;
import de.blutmondgilde.modpackaddons.util.PlaceholderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.progress.StartupMessageManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod(Constants.MOD_ID)
public class ModpackAddons {
    public static Optional<Discord> discord;
    public static boolean isMultiplayer = false;

    public ModpackAddons() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        Config.load(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID + "-client.toml"));

    }

    private void setup(final FMLCommonSetupEvent event) {
        MANetworkHandler.register();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        loadingMessage("Processing ModpackAddons Client");
        if (Config.isTitleChangerEnabled.get()) {
            changeWindowTitle();
        }

        if (Config.isIconChangerEnabled.get()) {
            changeIcon();
        }

        if (Config.isServerManagementEnabled.get()) {
            setupServerList();
        }

        if (Config.isDiscordRichPresenceEnabled.get()) {
            isMultiplayer = false;
            discord = Optional.of(new Discord());
        }
    }

    private void serverSetup(final FMLDedicatedServerSetupEvent e) {
        isMultiplayer = true;
    }

    private static void changeIcon() {
        if (Config.pathOrUrl.get().startsWith("http")) {
            try {
                ImageUtils.loadFromUrl();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ImageUtils.loadFromPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void changeWindowTitle() {
        final Logger logger = LogHelper.getLogger("WindowTitle");
        final Minecraft minecraft = Minecraft.getInstance();
        final String newTitle = PlaceholderUtils.replacePlaceholders(Config.windowTitle.get());

        GLFW.glfwSetWindowTitle(minecraft.getMainWindow().getHandle(), newTitle);
        logger.debug("Changed window title to: " + newTitle);
    }

    private static void setupServerList() {
        final Logger logger = LogHelper.getLogger("ServerList");
        final Minecraft minecraft = Minecraft.getInstance();
        ServerList serverList = new ServerList(minecraft);

        //Get Servers from Config File
        Map<String, String> servers = new HashMap<>();
        final String[] names, addresses;
        names = Config.serverNameArray.get().split(";");
        addresses = Config.serverAddressArray.get().split(";");

        for (int i = 0; i < names.length; i++) {
            servers.put(names[i], addresses[i]);
        }

        //Adds found Servers to the ServerList if the don't exist
        servers.forEach((name, address) -> {
            boolean foundServer = false;
            final int c = serverList.countServers();

            for (int i = 0; i < c; i++) {
                ServerData data = serverList.getServerData(i);

                if (data.serverIP.equals(address)) {
                    foundServer = true;
                    logger.debug("Found Server: " + name);
                    break;
                }
            }

            if (!foundServer) {
                ServerData data = new ServerData(name, address, false);
                serverList.addServerData(data);
                logger.debug("Added Server: " + name);
                serverList.saveServerList();
            }
        });
    }

    private static void loadingMessage(final String message) {
        StartupMessageManager.modLoaderConsumer().ifPresent((c) -> c.accept(message));
    }
}
