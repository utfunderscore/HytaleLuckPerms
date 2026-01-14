package me.lucko.luckperms.hytale;

import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.permissions.provider.PermissionProvider;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import me.lucko.luckperms.common.cacheddata.type.PermissionCache;
import me.lucko.luckperms.common.model.User;
import me.lucko.luckperms.common.query.QueryOptionsImpl;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.query.QueryOptions;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class LPPlugin extends JavaPlugin {

    private final LPHytaleBootstrap bootstrap;

    public LPPlugin(@NonNull JavaPluginInit init) {
        super(init);
        this.bootstrap = new LPHytaleBootstrap(this);
    }

    @Override
    protected void start() {
        bootstrap.init();

        PermissionsModule permissionsModule = PermissionsModule.get();
        permissionsModule.addProvider(new PermissionProvider() {
            @Override
            public @NonNull String getName() {
                return "luckperms";
            }

            @Override
            public void addUserPermissions(@NonNull UUID uuid, @NonNull Set<String> set) {

            }

            @Override
            public void removeUserPermissions(@NonNull UUID uuid, @NonNull Set<String> set) {

            }

            @Override
            public Set<String> getUserPermissions(@NonNull UUID uuid) {
                bootstrap.getPlugin().getLogger().info("Testing for permission " + uuid);

                User user = bootstrap.getPlugin().getUserManager().getOrMake(uuid);
                PermissionCache permissionData = user.getCachedData().getPermissionData();
                System.out.println(permissionData.getPermissionMap());

                HashSet<String> permissions = new HashSet<>();

                for (Map.Entry<String, Boolean> stringBooleanEntry : permissionData.getPermissionMap().entrySet()) {
                    if (stringBooleanEntry.getValue()) {
                        permissions.add(stringBooleanEntry.getKey());
                    } else {
                        permissions.add("-" + stringBooleanEntry.getKey());
                    }
                }


                return permissions;
            }

            @Override
            public void addGroupPermissions(@NonNull String s, @NonNull Set<String> set) {

            }

            @Override
            public void removeGroupPermissions(@NonNull String s, @NonNull Set<String> set) {

            }

            @Override
            public Set<String> getGroupPermissions(@NonNull String s) {
                return Set.of();
            }

            @Override
            public void addUserToGroup(@NonNull UUID uuid, @NonNull String s) {

            }

            @Override
            public void removeUserFromGroup(@NonNull UUID uuid, @NonNull String s) {

            }

            @Override
            public Set<String> getGroupsForUser(@NonNull UUID uuid) {
                return Set.of();
            }
        });
    }

    @Override
    protected void shutdown() {
        bootstrap.shutdown();
    }
}
