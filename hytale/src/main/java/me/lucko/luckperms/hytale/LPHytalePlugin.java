package me.lucko.luckperms.hytale;

import me.lucko.luckperms.common.api.LuckPermsApiProvider;
import me.lucko.luckperms.common.calculator.CalculatorFactory;
import me.lucko.luckperms.common.command.CommandManager;
import me.lucko.luckperms.common.config.generic.adapter.ConfigurationAdapter;
import me.lucko.luckperms.common.context.manager.ContextManager;
import me.lucko.luckperms.common.dependencies.Dependency;
import me.lucko.luckperms.common.event.AbstractEventBus;
import me.lucko.luckperms.common.messaging.MessagingFactory;
import me.lucko.luckperms.common.model.Group;
import me.lucko.luckperms.common.model.Track;
import me.lucko.luckperms.common.model.User;
import me.lucko.luckperms.common.model.manager.group.GroupManager;
import me.lucko.luckperms.common.model.manager.group.StandardGroupManager;
import me.lucko.luckperms.common.model.manager.track.StandardTrackManager;
import me.lucko.luckperms.common.model.manager.track.TrackManager;
import me.lucko.luckperms.common.model.manager.user.StandardUserManager;
import me.lucko.luckperms.common.model.manager.user.UserManager;
import me.lucko.luckperms.common.plugin.AbstractLuckPermsPlugin;
import me.lucko.luckperms.common.plugin.LuckPermsPlugin;
import me.lucko.luckperms.common.plugin.bootstrap.LuckPermsBootstrap;
import me.lucko.luckperms.common.plugin.util.AbstractConnectionListener;
import me.lucko.luckperms.common.sender.Sender;
import me.lucko.luckperms.hytale.calculator.HytaleCalculatorFactory;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.query.QueryOptions;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class LPHytalePlugin extends AbstractLuckPermsPlugin {

    private MinestomSenderFactory senderFactory;
    private StandardUserManager userManager;
    private StandardGroupManager groupManager;
    private StandardTrackManager trackManager;

    @Override
    protected void setupSenderFactory() {
        senderFactory = new MinestomSenderFactory(this);
    }

    @Override
    protected ConfigurationAdapter provideConfigurationAdapter() {
        return new HytaleConfigAdapter(this, resolveConfig("config.yml"));
    }

    @Override
    protected void registerPlatformListeners() {
        // TODO: Add platform specific listeners
    }

    @Override
    protected MessagingFactory<?> provideMessagingFactory() {
        return new MessagingFactory<LuckPermsPlugin>(this);
    }

    @Override
    protected void registerCommands() {

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

    }

    @Override
    protected void setupPlatformHooks() {

    }

    @Override
    protected AbstractEventBus<?> provideEventBus(LuckPermsApiProvider apiProvider) {
        return null;
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
        return null;
    }

    @Override
    public UserManager<? extends User> getUserManager() {
        return null;
    }

    @Override
    public GroupManager<? extends Group> getGroupManager() {
        return null;
    }

    @Override
    public TrackManager<? extends Track> getTrackManager() {
        return null;
    }

    @Override
    public CommandManager getCommandManager() {
        return null;
    }

    @Override
    public AbstractConnectionListener getConnectionListener() {
        return null;
    }

    @Override
    public ContextManager<?, ?> getContextManager() {
        return null;
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
        return null;
    }

    public MinestomSenderFactory getSenderFactory() {
        return senderFactory;
    }
}
