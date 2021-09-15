package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.petbuilder;

import net.atlas.SkyblockSandbox.player.pets.PetBuilder;

import java.util.HashMap;
import java.util.UUID;

public class PetBuilderHelper {
    public static HashMap<UUID, PetBuilder> storedPets = new HashMap<>();
    public static HashMap<UUID,Boolean> isTyping = new HashMap<>();
}
