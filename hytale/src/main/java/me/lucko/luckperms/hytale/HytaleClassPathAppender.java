package me.lucko.luckperms.hytale;

import me.lucko.luckperms.common.plugin.classpath.ClassPathAppender;

import java.nio.file.Path;

public class HytaleClassPathAppender implements ClassPathAppender {
    @Override
    public void addJarToClasspath(Path file) {
        //TODO: Add class path appending
        // Velocity does this: this.bootstrap.getProxy().getPluginManager().addToClasspath(this.bootstrap, file);
    }
}
