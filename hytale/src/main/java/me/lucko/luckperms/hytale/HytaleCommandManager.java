package me.lucko.luckperms.hytale;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import me.lucko.luckperms.common.command.CommandManager;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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
    }

    public void unregister() {
    }

    public class HytaleCommand extends AbstractCommand {

        protected HytaleCommand() {
            //"luckperms", "lp", "perm", "perms", "permission", "permissions"
            super("luckperms", "Luckperms base command");
            addAliases("lp", "perm", "perms", "permission", "permissions");
        }

        @Override
        protected @Nullable CompletableFuture<Void> execute(@NonNull CommandContext commandContext) {
            String command = commandContext.getInputString();

            commandContext.sendMessage(Message.raw(command));

            return new CompletableFuture<>();
        }
    }

}
