package me.lucko.luckperms.hytale;

import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import me.lucko.luckperms.common.plugin.bootstrap.LuckPermsBootstrap;
import me.lucko.luckperms.common.plugin.classpath.ClassPathAppender;
import me.lucko.luckperms.common.plugin.logging.PluginLogger;
import me.lucko.luckperms.common.plugin.scheduler.SchedulerAdapter;
import me.lucko.luckperms.hytale.logger.HytalePluginLogger;
import net.luckperms.api.platform.Platform;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Collection;
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

    public LPHytaleBootstrap(@NonNull LPPlugin lpPlugin) {
        this.plugin = new LPHytalePlugin(lpPlugin, this);
        this.dataDirectory = lpPlugin.getDataDirectory();
        this.logger = new HytalePluginLogger(lpPlugin.getLogger());
        this.schedulerAdapter = new HytaleSchedulerAdapter(this);
        this.classPathAppender = new HytaleClassPathAppender();
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
        return "1.0";
    }

    @Override
    public Path getDataDirectory() {
        return this.dataDirectory.toAbsolutePath();
    }

    @Override
    public Optional<Object> getPlayer(UUID uniqueId) {
        return Optional.ofNullable(Universe.get().getPlayer(uniqueId));
    }

    @Override
    public Optional<UUID> lookupUniqueId(String username) {
        return Optional.ofNullable(Universe.get().getPlayer(username, NameMatching.EXACT_IGNORE_CASE)).map(PlayerRef::getUuid);
    }

    @Override
    public Optional<String> lookupUsername(UUID uniqueId) {
        return Optional.ofNullable(Universe.get().getPlayer(uniqueId)).map(PlayerRef::getUsername);
    }

    @Override
    public int getPlayerCount() {
        return Universe.get().getPlayerCount();
    }

    @Override
    public Collection<String> getPlayerList() {
        return Universe.get().getPlayers().stream().map(PlayerRef::getUsername).toList();
    }

    @Override
    public Collection<UUID> getOnlinePlayers() {
        return Universe.get().getPlayers().stream().map(PlayerRef::getUuid).toList();
    }

    @Override
    public boolean isPlayerOnline(UUID uniqueId) {
        return Universe.get().getPlayer(uniqueId) != null;
    }

    public LPHytalePlugin getPlugin() {
        return plugin;
    }
}
