package me.lucko.luckperms.hytale.logger;

import com.hypixel.hytale.logger.HytaleLogger;
import me.lucko.luckperms.common.plugin.logging.PluginLogger;

public class HytalePluginLogger implements PluginLogger {

    private final HytaleLogger logger;

    public HytalePluginLogger(HytaleLogger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String s) {
        logger.atInfo().log(s);
    }

    @Override
    public void warn(String s) {
        logger.atWarning().log(s);
    }

    @Override
    public void warn(String s, Throwable t) {
        logger.atWarning().withCause(t).log(s);
    }

    @Override
    public void severe(String s) {
        logger.atSevere().log(s);
    }

    @Override
    public void severe(String s, Throwable t) {
        logger.atSevere().withCause(t).log(s);
    }
}
