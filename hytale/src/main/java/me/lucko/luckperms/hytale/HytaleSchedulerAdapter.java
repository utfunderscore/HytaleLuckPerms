package me.lucko.luckperms.hytale;

import me.lucko.luckperms.common.plugin.bootstrap.LuckPermsBootstrap;
import me.lucko.luckperms.common.plugin.scheduler.AbstractJavaScheduler;
import me.lucko.luckperms.common.plugin.scheduler.SchedulerAdapter;

import java.util.concurrent.Executor;

public class HytaleSchedulerAdapter extends AbstractJavaScheduler implements SchedulerAdapter {

    private final Executor sync;

    public HytaleSchedulerAdapter(LuckPermsBootstrap bootstrap) {
        super(bootstrap);
        this.sync = command -> {
            //TODO: Schedule sync task
        };
    }

    @Override
    public Executor sync() {
        return null;
    }
}
