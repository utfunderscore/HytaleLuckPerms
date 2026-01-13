package me.lucko.luckperms.hytale;

import me.lucko.luckperms.common.plugin.bootstrap.LuckPermsBootstrap;
import me.lucko.luckperms.common.plugin.classpath.ClassPathAppender;
import me.lucko.luckperms.common.plugin.logging.PluginLogger;
import me.lucko.luckperms.common.plugin.scheduler.SchedulerAdapter;
import net.luckperms.api.platform.Platform;

import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class LPHytaleBootstrap implements LuckPermsBootstrap {

    private final LPHytalePlugin plugin;
    private final Path dataDirectory;
    private final PluginLogger logger;
    private final SchedulerAdapter schedulerAdapter;
    private final ClassPathAppender classPathAppender;

    private final CountDownLatch loadLatch = new CountDownLatch(1);
    private final CountDownLatch enableLatch = new CountDownLatch(1);

    private Instant startTime;

    public LPHytaleBootstrap(LPHytalePlugin plugin, Path dataDirectory, PluginLogger logger, SchedulerAdapter schedulerAdapter, ClassPathAppender classPathAppender) {
        this.plugin = plugin;
        this.dataDirectory = dataDirectory;
        this.logger = logger;
        this.schedulerAdapter = schedulerAdapter;
        this.classPathAppender = classPathAppender;
    }

    @Override
    public PluginLogger getPluginLogger() {
        return this.logger;
    }

    @Override
    public SchedulerAdapter getScheduler() {
        return this.schedulerAdapter;
    }

    @Override
    public ClassPathAppender getClassPathAppender() {
        return this.classPathAppender;
    }

    @Override
    public CountDownLatch getLoadLatch() {
        return loadLatch;
    }

    @Override
    public CountDownLatch getEnableLatch() {
        return enableLatch;
    }

    @Override
    public String getVersion() {
        return "@VERSION@";
    }

    //TODO: Add event hook to initialise the plugin
    public void init() {
        this.startTime = Instant.now();
        try {
            this.plugin.load();
        } finally {
            this.loadLatch.countDown();
        }

        try {
            this.plugin.enable();
        } finally {
            this.enableLatch.countDown();
        }
    }

    //TODO: Add event hook to shutdown the plugin
    public void shutdown() {
        this.plugin.disable();
    }

    @Override
    public Instant getStartupTime() {
        return this.startTime;
    }

    @Override
    public Platform.Type getType() {
        return Platform.Type.HYTALE;
    }

    @Override
    public String getServerBrand() {
        return "hytale";
    }

    @Override
    public String getServerVersion() {
        // TODO: Get hytale server version
        return "";
    }

    @Override
    public Path getDataDirectory() {
        return this.dataDirectory;
    }

    @Override
    public Optional<Object> getPlayer(UUID uniqueId) {
        // TODO: Replace 'Object' with player object and configure look ups by uuid
        return Optional.empty();
    }

    @Override
    public Optional<UUID> lookupUniqueId(String username) {
        // TODO: Look up player uuid by username
        return Optional.empty();
    }

    @Override
    public Optional<String> lookupUsername(UUID uniqueId) {
        // TODO: Look up player username by uuid
        return Optional.empty();
    }

    @Override
    public int getPlayerCount() {
        // TODO: Get online player count
        return 0;
    }

    @Override
    public Collection<String> getPlayerList() {
        // TODO: Get online player names
        return Collections.emptyList();
    }

    @Override
    public Collection<UUID> getOnlinePlayers() {
        // TODO: Get online player IDS
        return Collections.emptyList();
    }

    @Override
    public boolean isPlayerOnline(UUID uniqueId) {
        //TODO: Check if player is online
        return false;
    }

}
