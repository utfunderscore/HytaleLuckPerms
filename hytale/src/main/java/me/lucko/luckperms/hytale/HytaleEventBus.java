package me.lucko.luckperms.hytale;

import me.lucko.luckperms.common.api.LuckPermsApiProvider;
import me.lucko.luckperms.common.event.AbstractEventBus;
import me.lucko.luckperms.common.plugin.LuckPermsPlugin;

public class HytaleEventBus extends AbstractEventBus<Object> {

    protected HytaleEventBus(LuckPermsPlugin plugin, LuckPermsApiProvider apiProvider) {
        super(plugin, apiProvider);
    }

    @Override
    protected Object checkPlugin(Object plugin) throws IllegalArgumentException {
        return plugin;
    }
}
