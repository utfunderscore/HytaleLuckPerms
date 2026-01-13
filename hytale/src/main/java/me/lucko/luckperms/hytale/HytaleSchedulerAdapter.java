package me.lucko.luckperms.hytale;

import com.hypixel.hytale.server.core.HytaleServer;
import me.lucko.luckperms.common.plugin.bootstrap.LuckPermsBootstrap;
import me.lucko.luckperms.common.plugin.scheduler.AbstractJavaScheduler;
import me.lucko.luckperms.common.plugin.scheduler.SchedulerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class HytaleSchedulerAdapter extends AbstractJavaScheduler implements SchedulerAdapter {

    private @NotNull final Executor sync;

    public HytaleSchedulerAdapter(LuckPermsBootstrap bootstrap) {
        super(bootstrap);
        this.sync = HytaleServer.SCHEDULED_EXECUTOR;
    }

    @Override
    public Executor sync() {
        return sync;
    }
}
