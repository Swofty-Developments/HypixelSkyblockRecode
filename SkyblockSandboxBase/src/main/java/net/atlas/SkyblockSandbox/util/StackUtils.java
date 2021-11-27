package net.atlas.SkyblockSandbox.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.sk89q.worldedit.blocks.SkullBlock;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.builder.item.SkullBuilder;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class StackUtils {

    public static String textureFromSkullMeta(SkullMeta skullMeta, ItemStack item) {
        if (item.getType().equals(Material.SKULL_ITEM) || item.getType().equals(Material.SKULL))
            new NullPointerException("That is not a skull!");

        Field profileField = null;
        try {
            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                GameProfile profile = (GameProfile) profileField.get(skullMeta);
                Collection<Property> textures = profile.getProperties().get("textures");
                for (Property property : textures) {
                    return property.getValue();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack makeColorfulItem(Material material, String displayName, int amount, int durability, String... lore) {
        ItemStack item = new ItemStack(material, amount, (short) durability);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SUtil.colorize(displayName));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        meta.setLore(SUtil.colorize(Arrays.asList(lore)));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack makeColorfulItem(Material material, String displayName, int amount, int durability, String lore) {
        ItemStack item = new ItemStack(material, amount, (short) durability);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SUtil.colorize(displayName));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        String[] lore1 = lore.split("\n");
        meta.setLore(SUtil.colorize(Arrays.asList(lore1)));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack makeColorfulItem(Material material, String displayName, int amount, int durability, String lore, boolean glowing) {
        ItemStack item = new ItemStack(material, amount, (short) durability);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SUtil.colorize(displayName));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        if (glowing) {
            meta.addEnchant(Enchantment.LUCK, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        String[] lore1 = lore.split("\n");
        meta.setLore(SUtil.colorize(Arrays.asList(lore1)));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack makeColorfulItem(Material material, String displayName, int amount, int durability, List<String> lore) {
        ItemStack item = new ItemStack(material, amount, (short) durability);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SUtil.colorize(displayName));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        String[] lore1 = lore.toArray(new String[0]);
        meta.setLore(SUtil.colorize(Arrays.asList(lore1)));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack makeColorfulSkullItem(String displayname, String owner, int amount, String lore) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (byte) 3);

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (Bukkit.getPlayer(owner) != null && Bukkit.getPlayer(owner).isValid()) {
            meta.setOwner(owner);
        } else {
            SBItemStack i = new SBItemStack(item);
            List<Component> lore2 = new ArrayList<>();
            String[] loreArr = lore.split("\n");
            for(String s:loreArr) {
                lore2.add(Component.text(SUtil.colorize(s)));
            }

            owner = new String(Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", owner).getBytes()));
            item = ItemBuilder.skull(item).texture(owner).name(Component.text(SUtil.colorize(displayname))).amount(amount).lore(lore2).build();
            return item;
        }
        String[] lore1 = lore.split("\n");
        meta.setLore(SUtil.colorize(Arrays.asList(lore1)));
        meta.setDisplayName(SUtil.colorize(displayname));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack makeColorfulSkullItem(String displayname, String owner, int amount, List<String> lore) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (Bukkit.getPlayer(owner) != null && Bukkit.getPlayer(owner).isValid()) {
            meta.setOwner(owner);
        } else {
            SBItemStack i = new SBItemStack(item);
            List<Component> lore2 = new ArrayList<>();
            for(String s:lore) {
                lore2.add(Component.text(SUtil.colorize(s)));
            }

            owner = new String(Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", owner).getBytes()));
            item = ItemBuilder.skull(item).texture(owner).name(Component.text(SUtil.colorize(displayname))).amount(amount).lore(lore2).build();
            return item;
        }
        meta.setLore(SUtil.colorize(lore));
        meta.setDisplayName(SUtil.colorize(displayname));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack makeColorfulSkullItem(String displayname, String owner, int amount, String... lore) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (Bukkit.getPlayer(owner) != null && Bukkit.getPlayer(owner).isValid()) {
            meta.setOwner(owner);
        } else {
            SBItemStack i = new SBItemStack(item);
            List<Component> lore2 = new ArrayList<>();
            for(String s:lore) {
                lore2.add(Component.text(SUtil.colorize(s)));
            }

            owner = new String(Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", owner).getBytes()));
            item = ItemBuilder.skull(item).texture(owner).name(Component.text(SUtil.colorize(displayname))).amount(amount).lore(lore2).build();
            return item;
        }
        meta.setLore(SUtil.colorize(lore));
        meta.setDisplayName(SUtil.colorize(displayname));
        item.setItemMeta(meta);

        return item;
    }
    public static List<String> stringToLore(String string, int characterLimit, ChatColor prefixColor) {
        String[] words = string.split(" ");
        List<String> lines = new ArrayList();
        StringBuilder currentLine = new StringBuilder();
        String[] var6 = words;
        int var7 = words.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String word = var6[var8];
            if (!word.equals("/newline")) {
                if (currentLine.toString().equals("")) {
                    currentLine = new StringBuilder(word);
                } else {
                    currentLine.append(" ").append(word);
                }
            }

            if (word.equals("/newline") || currentLine.length() + word.length() >= characterLimit) {
                String newLine = currentLine.toString();
                lines.add("" + prefixColor + newLine);
                currentLine = new StringBuilder();
            }
        }

        if (currentLine.length() > 0) {
            lines.add("" + prefixColor + currentLine);
        }

        return lines;
    }

    public static void setLoreLine(int lineNumber, SBPlayer player, String[] args) {
        StringBuilder builder = new StringBuilder("");
        ItemStack inHand = player.getItemInHand();
        lineNumber = lineNumber - 1;
        for (int i = 1; i < args.length; i++) {
            if (i == args.length - 1) {
                builder.append(args[i]);
            } else {
                builder.append(args[i]).append(" ");
            }
        }
        String loreToBeSet = builder.toString().trim();
        List<String> newLore = new ArrayList<String>();
        loreToBeSet = SUtil.colorize(loreToBeSet);
        if (player.getSetting(SBPlayer.Settings.LORE_GEN)) {
            SBItemBuilder item = new SBItemBuilder(inHand);
            if (!item.description.isEmpty()) {
                List<String> oldLore = item.description;
                try {
                    oldLore.set(lineNumber, loreToBeSet); // ERROR WILL BE CAUSED IF BIGGER
                    newLore = oldLore;
                } catch (IndexOutOfBoundsException e) {
                    // Fill new lore with old stuff
                    newLore.addAll(oldLore);
                    for (int i = oldLore.size() - 1; i < lineNumber; i++) { // Expand new lore to proper size
                        newLore.add("");
                    }
                    newLore.set(lineNumber, loreToBeSet);
                }
                item.setDescription((ArrayList<String>) newLore);
                player.setItemInHand(item.build());
            } else { // Item has no lore
                for (int i = 0; i <= lineNumber; i++) {
                    newLore.add("");
                }
                newLore.set(lineNumber, loreToBeSet);
                item.setDescription((ArrayList<String>) newLore);
                player.setItemInHand(item.build());
            }
        } else {
            ItemMeta im = inHand.getItemMeta();
            if (im.hasLore()) {
                List<String> oldLore = im.getLore();
                try {
                    oldLore.set(lineNumber, loreToBeSet); // ERROR WILL BE CAUSED IF BIGGER
                    newLore = oldLore;
                } catch (IndexOutOfBoundsException e) {
                    // Fill new lore with old stuff
                    newLore.addAll(oldLore);
                    for (int i = oldLore.size() - 1; i < lineNumber; i++) { // Expand new lore to proper size
                        newLore.add("");
                    }
                    newLore.set(lineNumber, loreToBeSet);
                }
                im.setLore(newLore);
                inHand.setItemMeta(im);
                player.setItemInHand(inHand);
            } else { // Item has no lore
                for (int i = 0; i <= lineNumber; i++) {
                    newLore.add("");
                }
                newLore.set(lineNumber, loreToBeSet);
                im.setLore(newLore);
                inHand.setItemMeta(im);
                player.setItemInHand(inHand);
            }
            player.sendMessage("&aSuccessfully set lore line to: &e" + builder.toString());
        }
    }

    public static void setDescriptionLine(int index, int lineNumber, SBPlayer player, String[] args) {
        StringBuilder builder = new StringBuilder("");
        ItemStack inHand = player.getItemInHand();
        lineNumber = lineNumber - 1;
        for (int i = 3; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        String loreToBeSet = builder.toString().trim();
        List<String> newLore = new ArrayList<String>();
        loreToBeSet = SUtil.colorize(loreToBeSet);
        if (player.getSetting(SBPlayer.Settings.LORE_GEN)) {
            SBItemBuilder item = new SBItemBuilder(inHand);
            if (!item.abilities.get(index).description.isEmpty()) {
                List<String> oldLore = item.abilities.get(index).description;
                try {
                    oldLore.set(lineNumber, loreToBeSet); // ERROR WILL BE CAUSED IF BIGGER
                    newLore = oldLore;
                } catch (IndexOutOfBoundsException e) {
                    // Fill new lore with old stuff
                    newLore.addAll(oldLore);
                    for (int i = oldLore.size() - 1; i < lineNumber; i++) { // Expand new lore to proper size
                        newLore.add("");
                    }
                    newLore.set(lineNumber, loreToBeSet);
                }
                player.setItemInHand(item.abilities.get(index).setWholeDescription((ArrayList<String>) newLore).build().build());
            } else { // Item has no lore
                for (int i = 0; i <= lineNumber; i++) {
                    newLore.add("");
                }
                newLore.set(lineNumber, loreToBeSet);
                player.setItemInHand(item.abilities.get(index).setWholeDescription((ArrayList<String>) newLore).build().build());
            }
        }
        player.sendMessage("&aSuccessfully set lore line to: &e" + builder);
    }
}
