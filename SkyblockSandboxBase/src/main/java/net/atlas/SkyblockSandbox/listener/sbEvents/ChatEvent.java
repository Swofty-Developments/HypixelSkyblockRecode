package net.atlas.SkyblockSandbox.listener.sbEvents;

import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.petbuilder.PetBuilderGUI;
import net.atlas.SkyblockSandbox.listener.SkyblockListener;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.petbuilder.PetBuilderHelper.isTyping;
import static net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.petbuilder.PetBuilderHelper.storedPets;

public class ChatEvent extends SkyblockListener<AsyncPlayerChatEvent> {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void callEvent(AsyncPlayerChatEvent event) {
        if(isTyping.containsKey(event.getPlayer().getUniqueId())) {
            if(isTyping.get(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
                storedPets.get(event.getPlayer().getUniqueId()).texture("http://textures.minecraft.net/texture/" + event.getMessage());
                new PetBuilderGUI(new SBPlayer(event.getPlayer())).open();
                isTyping.put(event.getPlayer().getUniqueId(),false);
            }
        }
    }
}
