package me.lucko.luckperms.hytale;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.jspecify.annotations.NonNull;

public class LPPlugin extends JavaPlugin {

    private final LPHytaleBootstrap bootstrap;

    public LPPlugin(@NonNull JavaPluginInit init) {
        super(init);
        this.bootstrap = new LPHytaleBootstrap(this);
    }

    @Override
    protected void start() {
        bootstrap.init();
    }

    @Override
    protected void shutdown() {
        bootstrap.shutdown();
    }
}
