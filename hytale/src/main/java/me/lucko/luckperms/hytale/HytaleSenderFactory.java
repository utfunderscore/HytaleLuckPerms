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

package me.lucko.luckperms.hytale;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import me.lucko.luckperms.common.locale.TranslationManager;
import me.lucko.luckperms.common.sender.SenderFactory;
import me.lucko.luckperms.hytale.convert.AdventureConversionUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.util.Tristate;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class HytaleSenderFactory extends SenderFactory<LPHytalePlugin, CommandSender> {

    public HytaleSenderFactory(LPHytalePlugin plugin) {
        super(plugin);
    }


    @Override
    protected UUID getUniqueId(CommandSender sender) {
        if (sender instanceof Player player) {
            return player.getUuid();
        } else {
            return ConsoleSender.INSTANCE.getUuid();
        }
    }

    @Override
    protected String getName(CommandSender sender) {
        if (sender instanceof Player player) {
            return player.getPlayerRef().getUsername();
        } else {
            return "Console";
        }
    }

    @Override
    protected void sendMessage(CommandSender sender, Component message) {
        Component rendered = TranslationManager.render(message, Locale.US);
        Message text = AdventureConversionUtils.adapt(rendered);
        sender.sendMessage(text);
    }

    @Override
    protected Tristate getPermissionValue(CommandSender sender, String node) {
        if(sender instanceof Player player) {
            PlayerRef ref = player.getPlayerRef();
            return getPlugin().getApiProvider().getPlayerAdapter(Player.class).getPermissionData(player)
                    .checkPermission(node);
        }
        return Tristate.TRUE;
    }

    @Override
    protected boolean hasPermission(CommandSender sender, String node) {
        if(sender instanceof Player player) {
            PlayerRef ref = player.getPlayerRef();
            return getPlugin().getApiProvider().getPlayerAdapter(PlayerRef.class).getPermissionData(ref)
                    .checkPermission(node)
                    .asBoolean();
        }
        return true;
    }

    @Override
    protected void performCommand(CommandSender sender, String command) {
        CommandManager.get().handleCommands(ConsoleSender.INSTANCE, new ArrayDeque<>(List.of(command))).join();
    }

    @Override
    protected boolean isConsole(CommandSender sender) {
        return false;
    }

}
