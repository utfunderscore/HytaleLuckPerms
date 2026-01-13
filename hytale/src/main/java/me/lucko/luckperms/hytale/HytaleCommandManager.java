package me.lucko.luckperms.hytale;

import me.lucko.luckperms.common.command.CommandManager;
import me.lucko.luckperms.common.command.utils.ArgumentTokenizer;

import java.util.List;

public class HytaleCommandManager extends CommandManager {

    private final LPHytalePlugin plugin;

    public HytaleCommandManager(LPHytalePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public void register() {

    }

    public void unregister() {
        // TODO: Register command
    }


    public void handleCommand(Object issuer, String commandName, String commandArgs) {
        List<String> arguments = ArgumentTokenizer.EXECUTE.tokenizeInput(commandArgs);

        executeCommand(plugin.getSenderFactory().wrap(issuer), commandArgs, arguments);
    }

}
