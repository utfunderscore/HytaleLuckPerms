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

    public void onPlayerSetupConnect(PlayerSetupConnectEvent e) {
        try {
            this.hytalePlugin.getBootstrap().getEnableLatch().await(60, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        UUID uuid = e.getUuid();
        String username = e.getUsername();

        if (this.hytalePlugin.getConfiguration().get(ConfigKeys.DEBUG_LOGINS)) {
            this.hytalePlugin.getLogger().info("Processing pre-login for " + uuid + " - " + username);
        }

        try {
            User user = loadUser(uuid, username);
            recordConnection(uuid);
            this.hytalePlugin.getEventDispatcher().dispatchPlayerLoginProcess(uuid, username, user);
        } catch (Exception ex) {
            this.hytalePlugin.getLogger().severe("Exception occurred whilst loading data for " + uuid + " - " + username, ex);

            Component reason = TranslationManager.render(Message.LOADING_DATABASE_ERROR.build());
            PlainTextComponentSerializer.plainText().serialize(reason);
            this.hytalePlugin.getEventDispatcher().dispatchPlayerLoginProcess(uuid, username, null);
        }
    }

    public void onPlayerConnect(PlayerConnectEvent e) {
        PlayerRef ref = e.getPlayerRef();

        if (this.hytalePlugin.getConfiguration().get(ConfigKeys.DEBUG_LOGINS)) {
            this.hytalePlugin.getLogger().info("Processing login for " + ref.getUuid() + " - " + ref.getUsername());
        }

        final User user = this.hytalePlugin.getUserManager().getIfLoaded(ref.getUuid());

        if (user == null) {
            if (!getUniqueConnections().contains(ref.getUuid())) {
                this.hytalePlugin.getLogger().warn("User " + ref.getUuid() + " - " + ref.getUsername() +
                        " doesn't have data pre-loaded, they have never been processed during pre-login in this session." +
                        " - denying login.");
            } else {
                this.hytalePlugin.getLogger().warn("User " + ref.getUuid() + " - " + ref.getUsername() +
                        " doesn't currently have data pre-loaded, but they have been processed before in this session." +
                        " - denying login.");
            }

            Component reason = TranslationManager.render(Message.LOADING_STATE_ERROR.build(), Locale.US);
            ref.getPacketHandler().disconnect(PlainTextComponentSerializer.plainText().serialize(reason));
            return;
        }

        this.hytalePlugin.getContextManager().signalContextUpdate(ref);
    }

}
