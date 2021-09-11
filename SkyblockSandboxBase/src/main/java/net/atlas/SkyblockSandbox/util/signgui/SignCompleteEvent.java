package signgui;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;

/**
 * File <b>SignCompleteEvent</b> located on fr.cleymax.signgui SignCompleteEvent is a part of SignGUI.
 * <p>
 * Copyright (c) 2019 SignGUI .
 * <p>
 *
 * @author Clément P. (Cleymax), {@literal <cleymaxpro@gmail.com>} Created the 11/09/2019 at 23:31
 */

public final class SignCompleteEvent {

	private final Player        player;
	private final BlockPosition location;
	private final String[]      lines;

	@ConstructorProperties({"player", "location", "lines"})
	public SignCompleteEvent(Player player, BlockPosition location, String[] lines)
	{
		this.player = player;
		this.location = location;
		this.lines = lines;
	}

	public final Player getPlayer()
	{
		return this.player;
	}

	public final BlockPosition getLocation()
	{
		return this.location;
	}

	public final String[] getLines()
	{
		return this.lines;
	}
}
