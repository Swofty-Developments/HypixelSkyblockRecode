package net.atlas.SkyblockSandbox.playerIsland;

import lombok.Getter;

import java.util.UUID;

public final class IslandId {
	@Getter
	private final UUID base;

	public IslandId(UUID base) {
		this.base = base;
	}

	@Override
	public String toString() {
		return base.toString();
	}

	public static IslandId randomIslandId() {
		return new IslandId(UUID.randomUUID());
	}

	public static IslandId fromString(String base) {
		return new IslandId(UUID.fromString(base));
	}
}
