package net.atlas.SkyblockSandbox.util;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import lombok.experimental.UtilityClass;
import net.atlas.SkyblockSandbox.SBX;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class WorldEditUtil {
	public void loadSchemFromFile(File file, Location loc) {
		WorldEditPlugin worldEditPlugin = SBX.getWorldEdit();
		EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(loc.getWorld()),
				100000);
		try {
			CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);
			clipboard.paste(session, new Vector(loc.getX(), loc.getY(), loc.getZ()), false);
		} catch (DataException | IOException | MaxChangedBlocksException e) {
			e.printStackTrace();
		}
	}

	public Integer getSchemLength(File file) {
		try {
			CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);
			return clipboard.getLength();
		} catch (DataException | IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Integer getSchemWidth(File file) {
		try {
			CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);
			return clipboard.getWidth();
		} catch (DataException | IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Integer getSchemHeight(File file) {
		try {
			CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);
			return clipboard.getHeight();
		} catch (DataException | IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
