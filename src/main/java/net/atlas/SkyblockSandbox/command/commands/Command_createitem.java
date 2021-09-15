package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
//import net.atlas.SkyblockSandbox.gui.guis.itemCreator.ItemCreator;
//import net.atlas.SkyblockSandbox.gui.guis.itemCreator.ItemCreatorPage;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.ItemCreatorGUIMain;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Bukkit;
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
import java.util.UUID;

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
            String[] args = cmd.getArgs();
            new ItemCreatorGUIMain(p).open();
        } else if (cmd.getSender() instanceof ConsoleCommandSender) {
            try {
                File inventoryFile = new File(getServer().getWorldContainer().getAbsolutePath().replace(".", "")
                        + "Hypixel" + File.separator
                        + "playerdata", "ad80d7cf-8115-4e2a-b15d-e5cc0bf6a9a2" + ".dat");
                NBTTagCompound nbt = NBTCompressedStreamTools.a((InputStream) (new FileInputStream(inventoryFile)));
                NBTTagList inventory = (NBTTagList) nbt.get("Inventory");
                Inventory inv = new CraftInventoryCustom(null, inventory.size());
                for (int i = 0; i < inventory.size() - 1; i++) {
                    NBTTagCompound compound = (NBTTagCompound) inventory.get(i);
                    if (!compound.isEmpty()) {
                        ItemStack stack = CraftItemStack.asBukkitCopy(net.minecraft.server.v1_8_R3.ItemStack.createStack(compound));
                        inv.setItem(i, stack);
                    }
                }
                AbilityData.setFunctionData(inv.getItem(0), 1, "Implosion_range", 1, 10);
                for (String string: AbilityData.listFunctionData(inv.getItem(0), 1, 1)) {
                    if(!string.contains("Implosion") || !string.contains("id")) {
                        System.out.println(string);
                        AbilityData.removeFunction(inv.getItem(0), 1, 1);
                        return;
                    }
                }
                System.out.println("Works fine!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
