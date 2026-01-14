/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

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
