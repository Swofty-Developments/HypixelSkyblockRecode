package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
//import net.atlas.SkyblockSandbox.gui.guis.itemCreator.ItemCreator;
//import net.atlas.SkyblockSandbox.gui.guis.itemCreator.ItemCreatorPage;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.ConfirmGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.auto.ItemCreatorGUIMain;
import net.atlas.SkyblockSandbox.gui.guis.skyblockmenu.SettingsMenu;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static net.atlas.SkyblockSandbox.util.NBTUtil.getString;
import static org.bukkit.Bukkit.getServer;

public class Command_createitem extends SkyblockCommandFramework {

    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_createitem(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "createitem", aliases = {"edit"}, description = "Opens the item creator GUI", usage = "/createitem")
    public void createitemCommand(SBCommandArgs cmd) {
        if (cmd.getSender() instanceof Player) {
            SBPlayer p = new SBPlayer((Player) cmd.getSender());
            if (SettingsMenu.loreGen.containsKey(p.getUniqueId()) && SettingsMenu.loreGen.get(p.getUniqueId())) {
                if (getString(p.getItemInHand(), "non-legacy").equals("true")) {
                    new ItemCreatorGUIMain(p).open();
                } else {
                    new ConfirmGUI(p).open();
                }
            } else {
                new net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.advanced.ItemCreatorGUIMain(p).open();
            }
        }
    }
}
