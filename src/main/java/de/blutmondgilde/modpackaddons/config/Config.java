package de.blutmondgilde.modpackaddons.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import de.blutmondgilde.modpackaddons.ModpackAddons;
import de.blutmondgilde.modpackaddons.util.LogHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    /* ============== Categories ============== */
    public static final String CATEGORY_DISPLAY = "Display";
    public static final String CATEGORY_SERVER = "Server";
    /* ============ Forge Elements ============ */
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec CLIENT_CONFIG;
    /* =========== Server Category ============ */
    public static ForgeConfigSpec.BooleanValue isServerManagementEnabled;
    public static ForgeConfigSpec.ConfigValue<String> serverNameArray;
    public static ForgeConfigSpec.ConfigValue<String> serverAddressArray;
    /* =========== Display Category =========== */
    public static ForgeConfigSpec.BooleanValue isTitleChangerEnabled;
    public static ForgeConfigSpec.ConfigValue<String> windowTitle;
    public static ForgeConfigSpec.BooleanValue isIconChangerEnabled;
    public static ForgeConfigSpec.ConfigValue<String> pathOrUrl;

    static {
        CLIENT_BUILDER.comment("Display").push(CATEGORY_DISPLAY);
        isTitleChangerEnabled = CLIENT_BUILDER
                .comment("Enables the Windowname Changer Feature")
                .define("customWindowTitel", false);
        windowTitle = CLIENT_BUILDER
                .comment("The new name of the MineCraft window", "Placeholders: %mcversion%, %forgeversion%")
                .define("windowTitle", "MineCraft %mcversion%");
        isIconChangerEnabled = CLIENT_BUILDER
                .comment("Enables the Icon changer Feature")
                .define("customIcon", false);
        pathOrUrl = CLIENT_BUILDER
                .comment("Path or URL to the PNG File", "Example Path: resources/icon.png. Don't use \\!", "Example URL: http://www.example.com/icon.png")
                .define("url", "https://icons.iconarchive.com/icons/papirus-team/papirus-apps/32/minecraft-icon.png");
        CLIENT_BUILDER.pop();


        CLIENT_BUILDER.comment("ServerList Management").push(CATEGORY_SERVER);
        isServerManagementEnabled = CLIENT_BUILDER
                .comment("Set to true to enable the ServerList Management Feature")
                .define("ServerListManagement", false);
        serverNameArray = CLIENT_BUILDER
                .comment("The name of the Servers to add. Multiple values has to be separated by ;.", "Example: ServerName1;ServerName2")
                .define("ServerNames", "localhost");
        serverAddressArray = CLIENT_BUILDER
                .comment("The Address / IP of the Servers to add. Multiple values has to be separated by ;.", "Example: Server1.com;Server2.com")
                .define("ServerAddresses", "127.0.0.1:25565");
        CLIENT_BUILDER.pop();

        //Build Config
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static void load(ForgeConfigSpec spec, final Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onReload(ModConfig.Reloading e) {
        if (!e.getConfig().getFileName().equals("modpackaddons-client.toml")) return;
        final Logger logger = LogHelper.getLogger("ConfigReloader");
        ModpackAddons.changeWindowTitle();
    }
}
