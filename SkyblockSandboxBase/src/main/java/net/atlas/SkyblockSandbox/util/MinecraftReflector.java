package net.atlas.SkyblockSandbox.util;

import org.bukkit.Bukkit;

public class MinecraftReflector
{
	private String user = "%%__USER__%%";
	private String username = "%%__USERNAME__%%";

	private static String MINECRAFT_SERVER_VERSION=getMinecraftServerVersion();

	private static String getMinecraftServerVersion()
	{
		String bukkitPackageName = Bukkit.getServer().getClass().getPackage().getName();
		return bukkitPackageName.substring(bukkitPackageName.lastIndexOf('.') + 1);
	}

	public static Class<?> getMinecraftServerClass(String className) throws ClassNotFoundException
	{
		return Class.forName("net.minecraft.server." + MINECRAFT_SERVER_VERSION + "." + className);
	}
}
