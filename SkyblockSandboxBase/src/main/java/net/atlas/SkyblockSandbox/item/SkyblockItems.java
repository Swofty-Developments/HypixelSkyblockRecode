package net.atlas.SkyblockSandbox.item;

import net.atlas.SkyblockSandbox.item.ability.itemAbilities.HellShatter;
import net.atlas.SkyblockSandbox.item.ability.itemAbilities.SoulCry;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface SkyblockItems {
    ItemStack item();

    boolean isPublic();

    static List<ItemStack> getAllItems() {
        List<ItemStack> items = new ArrayList<>();
        for(SkyblockItems.Default i: SkyblockItems.Default.values()) {
            items.add(i.item());
        }
        for(SkyblockItems.Sword i: SkyblockItems.Sword.values()) {
            items.add(i.item());
        }
        for(SkyblockItems.Axe i: SkyblockItems.Axe.values()) {
            items.add(i.item());
        }
        for(SkyblockItems.Dungeon i: SkyblockItems.Dungeon.values()) {
            items.add(i.item());
        }
        for(SkyblockItems.Pickaxe i: SkyblockItems.Pickaxe.values()) {
            items.add(i.item());
        }
        for(SkyblockItems.Unobtainable i: SkyblockItems.Unobtainable.values()) {
            items.add(i.item());
        }

        return items;
    }
    static List<ItemStack> getAllPublicItems() {
        List<ItemStack> items = new ArrayList<>();
        for(SkyblockItems.Default i: SkyblockItems.Default.values()) {
            if (!i.isPublic()) continue;
            items.add(i.item());
        }
        for(SkyblockItems.Sword i: SkyblockItems.Sword.values()) {
            if (!i.isPublic()) continue;
            items.add(i.item());
        }
        for(SkyblockItems.Axe i: SkyblockItems.Axe.values()) {
            if (!i.isPublic()) continue;
            items.add(i.item());
        }
        for(SkyblockItems.Dungeon i: SkyblockItems.Dungeon.values()) {
            if (!i.isPublic()) continue;
            items.add(i.item());
        }
        for(SkyblockItems.Pickaxe i: SkyblockItems.Pickaxe.values()) {
            if(!i.isPublic()) continue;;
            items.add(i.item());
        }
        for(SkyblockItems.Unobtainable i: SkyblockItems.Unobtainable.values()) {
            if (!i.isPublic()) continue;
            items.add(i.item());
        }
        return items;
    }

    enum Default implements SkyblockItems {
        ;
        private final ItemStack item;

        Default(ItemStack item) {
            this.item = item;
        }

        Default(ItemStack item, String nbtMsg,String nbtKey) {
            this.item = item;
        }

        @Override
        public ItemStack item() {
            return item;
        }

        @Override
        public boolean isPublic() {
            return false;
        }


        public static ItemType type() {
            return ItemType.ITEM;
        }
    }

    enum Sword implements SkyblockItems {
        HYPERION(new net.atlas.SkyblockSandbox.item.SBItemBuilder().material(Material.IRON_SWORD).type(ItemType.SWORD).rarity(Rarity.LEGENDARY).name("Hyperion").id("hyperion").stat(SBPlayer.PlayerStat.GEAR_SCORE, 615).stat(SBPlayer.PlayerStat.DAMAGE, 260).stat(SBPlayer.PlayerStat.STRENGTH, 150).stat(SBPlayer.PlayerStat.INTELLIGENCE, 350).stat(SBPlayer.PlayerStat.FEROCITY, 30)
                .addDescriptionLine("&7Deals +&a50% &7damage to")
                .addDescriptionLine("&7Withers. Grants &c+1 Damage")
                .addDescriptionLine("&7and &a+2 &bIntelligence")
                .addDescriptionLine("&7per &cCatacombs &7level.")
                .addDescriptionLine("")
                .addDescriptionLine("&7Your Catacombs level: &c0")
                .flag(ItemFlag.HIDE_ATTRIBUTES).build(), true);

        private final ItemStack item;
        private boolean ispublic = true;

        Sword(ItemStack item) {
            this.item = item;
        }

        Sword(ItemStack item,boolean bool) {
            this.item = item;
            this.ispublic = bool;
        }

        @Override
        public ItemStack item() {
            return item;
        }

        @Override
        public boolean isPublic() {
            return ispublic;
        }

        public static ItemType type() {
            return ItemType.SWORD;
        }
    }

    enum Axe implements SkyblockItems {
        ;

        private final ItemStack item;

        Axe(ItemStack item) {
            this.item = item;
        }

        @Override
        public ItemStack item() {
            return item;
        }

        @Override
        public boolean isPublic() {
            return true;
        }

        public static ItemType type() {
            return ItemType.AXE;
        }
    }

    enum Pickaxe implements SkyblockItems {
        ;
        private final ItemStack item;

        Pickaxe(ItemStack item) {
            this.item = item;
        }

        @Override
        public ItemStack item() {
            return item;
        }

        @Override
        public boolean isPublic() {
            return true;
        }

        public static ItemType type() {
            return ItemType.AXE;
        }
    }

    enum Drill implements SkyblockItems {
        ;

        private final ItemStack item;

        Drill(ItemStack item) {
            this.item = item;
        }

        @Override
        public ItemStack item() {
            return item;
        }

        @Override
        public boolean isPublic() {
            return true;
        }

        public static ItemType type() {
            return ItemType.AXE;
        }
    }



    enum Dungeon implements SkyblockItems {
        ;

        private final ItemStack item;

        Dungeon(ItemStack item) {
            this.item = item;
        }

        @Override
        public ItemStack item() {
            return item;
        }

        @Override
        public boolean isPublic() {
            return true;
        }

        public static ItemType type() {
            return ItemType.DUNGEON_ITEM;
        }
    }

    ;enum Unobtainable implements SkyblockItems {
        ;

        private final ItemStack item;

        Unobtainable(ItemStack item) {
            this.item = item;
        }

        @Override
        public ItemStack item() {
            return item;
        }

        @Override
        public boolean isPublic() {
            return false;
        }

        public static ItemType type() {
            return ItemType.ITEM;
        }
    }
}
