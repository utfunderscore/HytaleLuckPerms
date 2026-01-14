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
