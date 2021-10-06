package net.atlas.SkyblockSandbox.player.pets;


import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.builder.item.SkullBuilder;
import net.atlas.SkyblockSandbox.item.Rarity;
import net.atlas.SkyblockSandbox.item.ability.EnumAbilityData;
import net.atlas.SkyblockSandbox.util.NBTUtil;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PetBuilder {

    private SkullBuilder stack;
    private String name;
    private String petType;
    private String texture;
    private Rarity rarity;
    private int lvl;
    private int xp;
    private List<PetPerk> perks = new ArrayList<>();
    public static int[] petXP = {0, 100, 210, 330, 460, 605, 765, 940, 1130,
            1340, 1570, 1820, 2095, 2395, 2725, 3085, 3485, 3925, 4415, 4955, 5555,
            6215, 6945, 7745, 8625, 9585, 10635, 11785, 13045, 14425, 15935, 17585,
            19385, 21345, 23475, 25785, 28285, 30985, 33905, 37065, 40485, 44185, 48185,
            52535, 57285, 62485, 68185, 74485, 81485, 89285, 97985, 107685, 118485, 130485,
            143785, 158485, 174685, 192485, 211985, 233285, 256485, 281685, 309085, 338885,
            371285, 406485, 444685, 486085, 530885, 579285, 631485, 687685, 748085, 812885,
            882285, 956485, 1035685, 1120385, 1211085, 1308285, 1412485, 1524185, 1643885,
            1772085, 1909285, 2055985, 2212685, 2380385, 2560085, 2752785, 2959485, 3181185,
            3418885, 3673585, 3946285, 4237985, 4549685, 4883385, 5241085, 5624785, 6036485,
            6478185, 6954885, 7471585, 8033285, 8644985, 9311685, 10038385, 10830085, 11691785,
            12628485, 13645185, 14746885, 15938585, 17225285, 18611985, 20108685, 21725385, 23472085, 25358785};

    public PetBuilder() {
        this.stack = ItemBuilder.skull(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3));
    }

    public static PetBuilder init() {
        return new PetBuilder();
    }

    public PetBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public PetBuilder petType(final String petType) {
        this.petType = petType;
        return this;
    }

    public PetBuilder texture(final String texture) {
        String tex;
        tex = new String(Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes()));
        this.texture = tex;
        return this;
    }

    public PetBuilder perk(int perkIndex, String perkName, String... perkDescript) {
        perks.add(new PetPerk(perkIndex, SUtil.colorize(perkName), SUtil.colorize(perkDescript)));
        return this;
    }

    public PetBuilder perkName(int perkIndex, String perkName) {
        for(PetPerk perk:perks) {
            if(perk.getPerkIndex()==perkIndex) {
                perk.setName(SUtil.colorize(perkName));
                return this;
            }
        }
        perks.add(new PetPerk(perkIndex,SUtil.colorize(perkName), null));
        return this;
    }

    public PetBuilder perkDescription(int perkIndex, String... perkDescript) {
        for(PetPerk perk:perks) {
            if(perk.getPerkIndex()==perkIndex) {
                perk.setDescript(SUtil.colorize(Arrays.asList(perkDescript)));
                return this;
            }
        }
        perks.add(new PetPerk(perkIndex,"Default name",SUtil.colorize(Arrays.asList(perkDescript))));
        return this;
    }

    public PetBuilder addPerkDescriptionLine(int perkIndex, String... perkDescript) {
        for(PetPerk perk:perks) {
            if(perk.getPerkIndex()==perkIndex) {
                for(String s:SUtil.colorize(perkDescript)) {
                    perk.addDescript(s);
                }
                return this;
            }
        }
        perks.add(new PetPerk(perkIndex,"Default name",SUtil.colorize(Arrays.asList(perkDescript))));
        return this;
    }

    public PetBuilder rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public PetBuilder level(int lvl) {
        if(lvl>100) {
            lvl = 100;
        }
        this.lvl = lvl;
        return this;
    }

    public PetBuilder xp(int xp) {
        if(lvl==0) {
            xp = 0;
        }
        if(rarity!=null) {
            int modifier;
            switch (rarity) {
                case UNCOMMON:
                    modifier = 6;
                    break;
                case RARE:
                    modifier = 11;
                    break;
                case EPIC:
                    modifier = 16;
                    break;
                case LEGENDARY:
                case MYTHIC:
                    modifier = 19;
                    break;
                default:
                    modifier = 0;
                    break;
            }
            if(xp>petXP[lvl+modifier]) {
                xp = petXP[lvl+modifier];
            }
        }
        this.xp = xp;
        return this;
    }

    public ItemStack build() {
        ItemStack stack = this.stack.name(Component.text(SUtil.colorize(name)))
                .amount(1)
                .texture(texture)
                .lore().build();
        for (PetPerk perk : perks) {
            stack = setPerk(stack, perk);
        }
        stack = NBTUtil.setString(stack, rarity.name(), "RARITY");
        stack = NBTUtil.setInteger(stack, lvl, "pet-level");
        stack = NBTUtil.setInteger(stack,xp,"pet-xp");
        stack = NBTUtil.setString(stack,"true","is-pet");
        stack = NBTUtil.setString(stack,"false","is-equipped");
        stack = NBTUtil.setString(stack,"true","non-legacy");
        stack = NBTUtil.setString(stack,petType,"pet-type");

        return stack;
    }

    private ItemStack setPerk(ItemStack stack, PetPerk perk) {

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound attributes = (tag.getCompound("ExtraAttributes") != null ? tag.getCompound("ExtraAttributes") : new NBTTagCompound());
        NBTTagCompound perks = (attributes.getCompound("pet-perks") != null ? attributes.getCompound("pet-perks") : new NBTTagCompound());
        NBTTagCompound perkSlot = (perks.getCompound("perk_" + perk.getPerkIndex()) != null ? perks.getCompound("perk_" + perk.getPerkIndex()) : new NBTTagCompound());
        perkSlot.setString("name", perk.getName());
        perks.set("perk_" + perk.getPerkIndex(), perkSlot);

        attributes.set("pet-perks", perks);
        tag.set("ExtraAttributes", attributes);
        nmsItem.setTag(tag);

        stack = CraftItemStack.asBukkitCopy(nmsItem);

        for (String s : perk.getDescription()) {
            stack = addPerkDescriptLine(stack, s, perk);
        }

        return stack;

    }

    private ItemStack addPerkDescriptLine(ItemStack item, String line, PetPerk perk) {
        if (item != null) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
            NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
            NBTTagCompound data = tag.getCompound("ExtraAttributes");
            NBTTagCompound abilities = data.getCompound("pet-perks");
            NBTTagCompound ability = abilities.getCompound("perk_" + perk.getPerkIndex());
            NBTTagCompound description = ability.getCompound("description");
            List<String> descriptionStrings = new ArrayList<>();
            for (int j = 0; j < description.c().size(); j++) {
                descriptionStrings.add(description.getString(String.valueOf(j)));
            }
            List<String> descriptionList = new ArrayList<>(descriptionStrings);
            descriptionList.add(line);
            for (int i = 0; i < descriptionList.size(); i++) {
                description.setString(String.valueOf(i), descriptionList.get(i));
            }
            ability.set("description", description);
            abilities.set("perk_" + perk.getPerkIndex(), ability);
            data.set("pet-perks", abilities);
            tag.set("ExtraAttributes", data);

            nmsItem.setTag(tag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        }
        return null;
    }

}
