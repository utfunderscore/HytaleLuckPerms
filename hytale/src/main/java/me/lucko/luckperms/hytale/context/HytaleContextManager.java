package me.lucko.luckperms.hytale.context;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import me.lucko.luckperms.common.context.manager.SimpleContextManager;
import me.lucko.luckperms.common.plugin.LuckPermsPlugin;

import java.util.UUID;

public class HytaleContextManager extends SimpleContextManager<PlayerRef, PlayerRef> {
    public HytaleContextManager(LuckPermsPlugin plugin) {
        super(plugin, PlayerRef.class, PlayerRef.class);
    }

    @Override
    public UUID getUniqueId(PlayerRef player) {
        return player.getUuid();
    }
}
