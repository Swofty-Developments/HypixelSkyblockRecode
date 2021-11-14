package net.atlas.SkyblockSandbox.files;

import org.bukkit.configuration.file.FileConfiguration;

import javax.security.auth.login.Configuration;

public class SpawnersFile extends YamlFile {
    public SpawnersFile() {
        super("spawners");
    }

    public FileConfiguration get() {
        return this.getConfiguration();
    }

    public FileConfiguration a() {
        return this.getConfiguration();
    }
}
