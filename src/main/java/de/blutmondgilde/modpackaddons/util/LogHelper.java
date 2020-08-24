package de.blutmondgilde.modpackaddons.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {
    public static Logger getLogger(final String name) {
        return LogManager.getLogger(Constants.MOD_ID + "/" + name);
    }
}
