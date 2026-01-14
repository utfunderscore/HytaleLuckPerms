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

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.interface_.ChatMessage;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.io.adapter.PlayerPacketFilter;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import me.lucko.luckperms.common.command.CommandManager;
import me.lucko.luckperms.common.command.utils.ArgumentTokenizer;
import me.lucko.luckperms.common.sender.Sender;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.hypixel.hytale.server.core.command.system.CommandManager.*;

public class HytaleCommandManager extends CommandManager {

    private final LPHytalePlugin plugin;

    public HytaleCommandManager(LPHytalePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public void register() {
        get().register(new HytaleCommand());

//        PacketAdapters.registerInbound((PlayerPacketFilter) (playerRef, packet) -> {
//            if(packet instanceof ChatMessage chatMessage) {
//                String message = chatMessage.message;
//
//                Player player = playerRef.getComponent(Player.getComponentType());
//                Sender wrapped = plugin.getSenderFactory().wrap(player);
//
//                if(message != null && message.startsWith("/luckperms")) {
//                    List<String> arguments = ArgumentTokenizer.EXECUTE.tokenizeInput(message.replace("/luckperms ", ""));
//                    executeCommand(wrapped, "lpv", arguments);
//                }
//
//                return true;
//            }
//
//            return false;
//        });

    }

    public void unregister() {
    }


    public class HytaleCommand extends AbstractCommand {

        private final RequiredArg<String> messageArg = this.withRequiredArg("args", "The message to send to the user of this command", ArgTypes.STRING);


        protected HytaleCommand() {
            //"luckperms", "lp", "perm", "perms", "permission", "permissions"
            super("luckperms", "Luckperms base command");
            addAliases("lp", "perm", "perms", "permission", "permissions", "lp");
            setAllowsExtraArguments(true);
        }

        @Override
        protected @Nullable CompletableFuture<Void> execute(@NonNull CommandContext commandContext) {

            String input = commandContext.getInputString();
            plugin.getLogger().info("COMMANd: " + input);

            int i = input.indexOf(" ");
            String args = input.substring(i+1);

            plugin.getLogger().info(args);

            Sender wrapped = plugin.getSenderFactory().wrap(commandContext.sender());
            executeCommand(wrapped, commandContext.getCalledCommand().getName(), ArgumentTokenizer.EXECUTE.tokenizeInput(args));

            return new CompletableFuture<>();
        }
    }

}
