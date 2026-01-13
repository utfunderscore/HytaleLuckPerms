package me.lucko.luckperms.hytale;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import me.lucko.luckperms.common.sender.SenderFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.util.Tristate;

import java.util.ArrayDeque;
import java.util.List;
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
        String text = PlainTextComponentSerializer.plainText().serialize(message);
        sender.sendMessage(Message.raw(text));
    }

    @Override
    protected Tristate getPermissionValue(CommandSender sender, String node) {
        return sender instanceof Player player ? getPlugin().getApiProvider().getPlayerAdapter(Player.class).getPermissionData(player)
                .checkPermission(node) : Tristate.TRUE;
    }

    @Override
    protected boolean hasPermission(CommandSender sender, String node) {
        return !(sender instanceof Player player) || getPlugin().getApiProvider().getPlayerAdapter(Player.class).getPermissionData(player)
                .checkPermission(node)
                .asBoolean();
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
