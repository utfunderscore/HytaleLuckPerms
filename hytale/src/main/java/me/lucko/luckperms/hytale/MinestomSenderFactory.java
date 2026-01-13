package me.lucko.luckperms.hytale;

import me.lucko.luckperms.common.sender.SenderFactory;
import net.kyori.adventure.text.Component;
import net.luckperms.api.util.Tristate;

import java.util.UUID;

public class MinestomSenderFactory extends SenderFactory<LPHytalePlugin, Object> {

    private final LPHytalePlugin hytalePlugin;

    public MinestomSenderFactory(LPHytalePlugin plugin) {
        super(plugin);
        this.hytalePlugin = plugin;
    }

    @Override
    protected UUID getUniqueId(Object sender) {
        // TODO: Get UUID from command issuer
        return null;
    }

    @Override
    protected String getName(Object sender) {
        // TODO: Get name from command issuer
        return "";
    }

    @Override
    protected void sendMessage(Object sender, Component message) {
        // TODO: Convert message to whatever hytale uses
    }

    @Override
    protected Tristate getPermissionValue(Object sender, String node) {
        // TODO: USE actual player object instead of just Object
        return this.hytalePlugin.getApiProvider().getPlayerAdapter(Object.class).getPermissionData(sender).checkPermission(node);
    }

    @Override
    protected boolean hasPermission(Object sender, String node) {
        return this.hytalePlugin.getApiProvider().getPlayerAdapter(Object.class).getPermissionData(sender)
                .checkPermission(node)
                .asBoolean();
    }

    @Override
    protected void performCommand(Object sender, String command) {
        // TODO: Execute command on behalf of sender
    }

    @Override
    protected boolean isConsole(Object sender) {
        // TODO: Check if command sender is console
        return false;
    }
}
