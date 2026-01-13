package me.lucko.luckperms.hytale.listeners;

import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import me.lucko.luckperms.common.config.ConfigKeys;
import me.lucko.luckperms.common.locale.Message;
import me.lucko.luckperms.common.locale.TranslationManager;
import me.lucko.luckperms.common.model.User;
import me.lucko.luckperms.common.plugin.util.AbstractConnectionListener;
import me.lucko.luckperms.hytale.LPHytalePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HytaleConnectionListener extends AbstractConnectionListener {

    private final LPHytalePlugin hytalePlugin;

    public HytaleConnectionListener(LPHytalePlugin hytalePlugin) {
        super(hytalePlugin);
        this.hytalePlugin = hytalePlugin;

    }

    public void onPlayerConnect(PlayerSetupConnectEvent e) {
        UUID playerId = e.getUuid();
        String username = e.getUsername();
        
        final User user = hytalePlugin.getUserManager().getIfLoaded(playerId);

        if (hytalePlugin.getConfiguration().get(ConfigKeys.DEBUG_LOGINS)) {
            hytalePlugin.getLogger().info("Processing post-login for " + playerId + " - " + username);
        }

        if (user == null) {
            if (!getUniqueConnections().contains(playerId)) {
                this.hytalePlugin.getLogger().warn("User " + playerId + " - " + username +
                        " doesn't have data pre-loaded, they have never been processed during pre-login in this session.");
            } else {
                hytalePlugin.getLogger().warn("User " + playerId + " - " + username +
                        " doesn't currently have data pre-loaded, but they have been processed before in this session.");
            }

            String errorText = PlainTextComponentSerializer.plainText().serialize(TranslationManager.render(Message.LOADING_STATE_ERROR.build(), Locale.US));
            if (this.hytalePlugin.getConfiguration().get(ConfigKeys.CANCEL_FAILED_LOGINS)) {

                // disconnect the user
                e.setCancelled(true);
                e.setReason(errorText);

            } else {
                // just send a message
                this.hytalePlugin.getBootstrap().getScheduler().asyncLater(() -> {
                    PlayerRef player = Universe.get().getPlayer(playerId);
                    if(player != null) {
                        player.sendMessage(com.hypixel.hytale.server.core.Message.raw(errorText));
                    }

                }, 1, TimeUnit.SECONDS);
            }
        }
    }
}
