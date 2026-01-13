package me.lucko.luckperms.hytale;

import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import me.lucko.luckperms.common.api.LuckPermsApiProvider;
import me.lucko.luckperms.common.calculator.CalculatorFactory;
import me.lucko.luckperms.common.command.CommandManager;
import me.lucko.luckperms.common.config.generic.adapter.ConfigurationAdapter;
import me.lucko.luckperms.common.dependencies.Dependency;
import me.lucko.luckperms.common.event.AbstractEventBus;
import me.lucko.luckperms.common.messaging.MessagingFactory;
import me.lucko.luckperms.common.model.User;
import me.lucko.luckperms.common.model.manager.group.StandardGroupManager;
import me.lucko.luckperms.common.model.manager.track.StandardTrackManager;
import me.lucko.luckperms.common.model.manager.user.StandardUserManager;
import me.lucko.luckperms.common.plugin.AbstractLuckPermsPlugin;
import me.lucko.luckperms.common.plugin.LuckPermsPlugin;
import me.lucko.luckperms.common.plugin.bootstrap.LuckPermsBootstrap;
import me.lucko.luckperms.common.sender.Sender;
import me.lucko.luckperms.hytale.calculator.HytaleCalculatorFactory;
import me.lucko.luckperms.hytale.context.HytaleContextManager;
import me.lucko.luckperms.hytale.listeners.HytaleConnectionListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.query.QueryOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class LPHytalePlugin extends AbstractLuckPermsPlugin {

    private final LPPlugin lpPlugin;
    private final LPHytaleBootstrap bootstrap;

    public LPHytalePlugin(LPPlugin lpPlugin, LPHytaleBootstrap lpHytaleBootstrap) {
        this.lpPlugin = lpPlugin;
        this.bootstrap = lpHytaleBootstrap;
    }

    private HytaleSenderFactory senderFactory;
    private StandardUserManager userManager;
    private StandardGroupManager groupManager;
    private StandardTrackManager trackManager;
    private HytaleCommandManager commandManager;
    private HytaleConnectionListener connectionListener;
    private HytaleContextManager contextManager;

    @Override
    protected void setupSenderFactory() {
        senderFactory = new HytaleSenderFactory(this);
    }

    @Override
    protected ConfigurationAdapter provideConfigurationAdapter() {

        lpPlugin.getLogger().atInfo().log("THE PATH: " + lpPlugin.getDataDirectory());

        try {
            Files.createDirectories(lpPlugin.getDataDirectory());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path path = resolveConfig("config.yml");

        return new HytaleConfigAdapter(this, path);
    }

    @Override
    protected void registerPlatformListeners() {
        this.connectionListener = new HytaleConnectionListener(this);

        lpPlugin.getEventRegistry().registerGlobal(PlayerSetupConnectEvent.class, playerSetupConnectEvent -> this.connectionListener.onPlayerConnect(playerSetupConnectEvent));
    }

    @Override
    protected MessagingFactory<?> provideMessagingFactory() {
        return new MessagingFactory<LuckPermsPlugin>(this);
    }

    @Override
    protected void registerCommands() {
        this.commandManager = new HytaleCommandManager(this);
        this.commandManager.register();
    }

    @Override
    protected void setupManagers() {
        this.userManager = new StandardUserManager(this);
        this.groupManager = new StandardGroupManager(this);
        this.trackManager = new StandardTrackManager(this);
    }

    @Override
    protected CalculatorFactory provideCalculatorFactory() {
        return new HytaleCalculatorFactory(this);
    }

    @Override
    protected void setupContextManager() {
        contextManager = new HytaleContextManager(this);
    }

    @Override
    protected void setupPlatformHooks() {

    }

    @Override
    protected AbstractEventBus<?> provideEventBus(LuckPermsApiProvider apiProvider) {
        return new HytaleEventBus(this, apiProvider);
    }


    @Override
    protected void registerApiOnPlatform(LuckPerms api) {

    }

    @Override
    protected void performFinalSetup() {

    }

    @Override
    protected Set<Dependency> getGlobalDependencies() {
        Set<Dependency> dependencies = super.getGlobalDependencies();

        dependencies.add(Dependency.CONFIGURATE_CORE);
        dependencies.add(Dependency.CONFIGURATE_YAML);
        dependencies.add(Dependency.SNAKEYAML);
        return dependencies;
    }

    @Override
    public LuckPermsBootstrap getBootstrap() {
        return bootstrap;
    }

    @Override
    public StandardUserManager getUserManager() {
        return userManager;
    }

    @Override
    public StandardGroupManager getGroupManager() {
        return groupManager;
    }

    @Override
    public StandardTrackManager getTrackManager() {
        return trackManager;
    }

    @Override
    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public HytaleConnectionListener getConnectionListener() {
        return this.connectionListener;
    }

    @Override
    public HytaleContextManager getContextManager() {
        return contextManager;
    }

    @Override
    public Optional<QueryOptions> getQueryOptionsForUser(User user) {
        return Optional.empty();
    }

    @Override
    public Stream<Sender> getOnlineSenders() {
        return Stream.empty();
    }

    @Override
    public Sender getConsoleSender() {
        return senderFactory.wrap(ConsoleSender.INSTANCE);
    }

    public HytaleSenderFactory getSenderFactory() {
        return senderFactory;
    }

    public LPPlugin getPlugin() {
        return lpPlugin;
    }
}
