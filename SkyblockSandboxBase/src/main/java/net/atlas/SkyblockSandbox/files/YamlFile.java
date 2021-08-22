package net.atlas.SkyblockSandbox.files;

import lombok.SneakyThrows;
import net.atlas.SkyblockSandbox.SBX;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class YamlFile {
	private static final SBX INSTANCE = SBX.getInstance();

	private static File file;
	private static FileConfiguration configuration;

	private final String name;

	public YamlFile(String name) {
		this.name = name;
	}

	public String name() {
		return this.name;
	}

	public void create() {
		file = new File(INSTANCE.getDataFolder(), this.name() + ".yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			INSTANCE.saveResource(name() + ".yml", false);
		}

		configuration = new YamlConfiguration();
		try {
			configuration.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	@SneakyThrows
	public void save() {
		configuration.save(file);
	}

	public FileConfiguration getConfiguration() {
		return configuration;
	}
}
