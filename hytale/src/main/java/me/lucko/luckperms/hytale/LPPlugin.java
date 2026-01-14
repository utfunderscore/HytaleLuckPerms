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

import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.permissions.provider.PermissionProvider;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import me.lucko.luckperms.common.cacheddata.type.PermissionCache;
import me.lucko.luckperms.common.model.User;
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
