package de.blutmondgilde.modpackaddons.discord;

import de.blutmondgilde.modpackaddons.discord.utils.discord.ModIPCListener;
import de.blutmondgilde.modpackaddons.discord.utils.discord.rpc.IPCClient;
import de.blutmondgilde.modpackaddons.discord.utils.discord.rpc.entities.RichPresence;
import de.blutmondgilde.modpackaddons.util.Constants;
import de.blutmondgilde.modpackaddons.util.LogHelper;
import org.apache.logging.log4j.Logger;

public class Discord {
    private IPCClient ipcInstance;
    private Logger logger = LogHelper.getLogger("Discord/IPC");

    public Discord() {
        try {
            // Create IPC Instance and Listener and Make a Connection if possible
            logger.debug("Create Instance...");
            ipcInstance = new IPCClient(Long.parseLong(Constants.DISCORD_CLIENT_ID));
            logger.debug("Add Listener...");
            ipcInstance.setListener(new ModIPCListener());
            logger.debug("Connect to Discord...");
            ipcInstance.connect();

            // Subscribe to RPC Events after Connection
            ipcInstance.subscribe(IPCClient.Event.ACTIVITY_JOIN);
            ipcInstance.subscribe(IPCClient.Event.ACTIVITY_JOIN_REQUEST);
            ipcInstance.subscribe(IPCClient.Event.ACTIVITY_SPECTATE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendRichPresence(RichPresence richPresence) {
        this.ipcInstance.sendRichPresence(richPresence);
    }

    public enum Dimensions {
        Overworld,
        Nether,
        End;
    }

    public enum GameMode {
        Singeplayer,
        Multiplayer;
    }


}
