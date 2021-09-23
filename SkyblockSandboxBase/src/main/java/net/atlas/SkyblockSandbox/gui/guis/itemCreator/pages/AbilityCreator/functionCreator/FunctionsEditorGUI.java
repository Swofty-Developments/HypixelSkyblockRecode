package net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.abilityCreator.Function;
import net.atlas.SkyblockSandbox.abilityCreator.FunctionUtil;
import net.atlas.SkyblockSandbox.abilityCreator.functions.Particle;
import net.atlas.SkyblockSandbox.gui.AnvilGUI;
import net.atlas.SkyblockSandbox.gui.NormalGUI;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.AbilityCreatorGUI;
import net.atlas.SkyblockSandbox.item.ability.AbilityData;
import net.atlas.SkyblockSandbox.item.ability.functions.EnumFunctionsData;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FunctionsEditorGUI extends NormalGUI {
    private final int index;
    private final AbilityValue.FunctionType functionType;
    private final int funcIndex;
    private final Sound sound;
    private final Particle.Particles particle;

    public FunctionsEditorGUI(SBPlayer owner, AbilityValue.FunctionType functionType, int index, int funcIndex) {
        super(owner);
        this.index = index;
        this.functionType = functionType;
        this.funcIndex = funcIndex;
        this.particle = Enums.getIfPresent(Particle.Particles.class, FunctionUtil.getFunctionData(owner.getItemInHand(),index,funcIndex,Particle.dataValues.PARTICLE_TYPE)).orNull();
        this.sound = Enums.getIfPresent(Sound.class, FunctionUtil.getFunctionData(owner.getItemInHand(),index,funcIndex, net.atlas.SkyblockSandbox.abilityCreator.functions.Sound.dataValues.SOUND)).orNull();
    }



    @Override
    public String getTitle() {
        return "Editing - " + SUtil.firstLetterUpper(functionType.name()).replace("_"," ");
    }

    @Override
    public int getRows() {
        return 4;
    }


    @Override
    public void handleMenu(InventoryClickEvent event) {
        if (event.getClickedInventory().equals(getOwner().getInventory())) return;
        event.setCancelled(true);
        SBPlayer player = getOwner();
        setAction(31,event1 -> new FunctionsMainGUI(getOwner(), index).open());

    }

    @Override
    public boolean setClickActions() {
        Function function = functionType.getFunction(getOwner(),getOwner().getItemInHand(),index,funcIndex);
        function.getGuiLayout();
        for(int i:function.getClickActions().keySet()) {
            setAction(i,function.getClickActions().get(i));
        }
        setAction(31,event -> {
            new AbilityCreatorGUI(getOwner(),index).open();
        });
        return true;
    }

    @Override
    public void setItems() {
        setMenuGlass();
        setItem(31, makeColorfulItem(Material.ARROW, "§aGo Back", 1, 0, "§7To Create Ability #" + index));
        Function function = functionType.getFunction(getOwner(),getOwner().getItemInHand(),index,funcIndex);
        function.getGuiLayout();
        for(int i:function.getGuiItems().keySet()) {
            setItem(i,function.getGuiItems().get(i));
        }

        switch (functionType) {
            case HEAD: {
                ItemStack playerItem = getOwner().getItemInHand();
                if (AbilityData.stringFromFunction(playerItem, EnumFunctionsData.HEAD_SHOOTER_TYPE, index, funcIndex, null) == null) {
                    setItem(13, makeColorfulItem(Material.SKULL_ITEM, "&aSet the head", 1, 3, "&7Set the head of the\n&bhead shooter function&7!\n&eClick to set!"));
                } else {
                    setItem(13, makeColorfulSkullItem(AbilityData.stringFromFunction(playerItem, EnumFunctionsData.HEAD_SHOOTER_TYPE, index, funcIndex, null), "&aSet the head", 1, "&7Set the head of the\n&bhead shooter function&7!\n\n&eClick to set!"));
                }
                setItem(11, makeColorfulItem(Material.IRON_SWORD, "&aToggle Entity Damage", 1, 0, "&7Toggle if the spawned\n&7head will damage an\n&7entity\n\n&eLeft-Click to enable!\n&bRight-Click to disable!"));
                setItem(12, makeColorfulItem(Material.STICK, "&aSet the range", 1, 0, "&7Set the range of the\n&7head spawned!\n&7Maximum: &a30\n&7Minimum: &a0\n\n&eClick to set!"));
                setItem(14, makeColorfulItem(Material.COMMAND, "&aToggle kickback", 1, 0, "&7Toggle if the head,\n&7when it hits the ground\n&7to cause kickback!\n\n&eLeft-Click to enable\n&bRight-Click to disable!"));
                if (AbilityData.booleanFromFunction(playerItem, EnumFunctionsData.HEAD_SHOOTER_KICKBACK, index, funcIndex, false)) {
                    setItem(15, makeColorfulItem(Material.ENDER_PORTAL_FRAME, "&aSet the kickback range", 1, 0, "&7Set the range of the\n&7kickback produced!\n&7Maximum: &a15\n&7Minimum: &a0\n\n&eClick to set!"));
                } else {
                    setItem(15, FILLER_GLASS);
                }
                if (AbilityData.booleanFromFunction(playerItem, EnumFunctionsData.HEAD_SHOOTER_DAMAGE_ENTITY, index, funcIndex, false)) {
                    setItem(20, makeColorfulItem(Material.IRON_BLOCK, "&aSet the base damage", 1, 0, "&7Set the base damage of the\n&7head spawned!\n&7Maximum: &a100,000\n&7Minimum: &a1000\n\n&eClick to set!"));
                } else {
                    setItem(20, FILLER_GLASS);
                }
                break;
            }
            case PROJECTILE: {
                ItemStack playerItem = getOwner().getItemInHand();
                if (AbilityData.stringFromFunction(playerItem, EnumFunctionsData.PROJECTILE_SHOOTER_TYPE, index, funcIndex, null) == null) {
                    setItem(13, makeColorfulItem(Material.IRON_SWORD, "&aSet the projectile", 1, 0, "&7Set the projectile of the\n&bprojectile shooter function&7!\n&eClick to set!"));
                } else {
                    setItem(13, makeColorfulItem(Material.valueOf(AbilityData.stringFromFunction(playerItem, EnumFunctionsData.PROJECTILE_SHOOTER_TYPE, index, funcIndex, null)), "&aSet the projectile", 1, 0, "&7Set the projectile of the\n&bprojectile shooter function&7!\n\n&eClick to set!"));
                }
                setItem(11, makeColorfulItem(Material.IRON_SWORD, "&aToggle Entity Damage", 1, 0, "&7Toggle if the spawned\n&7projectile will damage an\n&7entity\n\n&eLeft-Click to enable!\n&bRight-Click to disable!"));
                setItem(15, FILLER_GLASS);

                if (AbilityData.booleanFromFunction(playerItem, EnumFunctionsData.PROJECTILE_SHOOTER_DAMAGE_ENTITY, index, funcIndex, false)) {
                    setItem(20, makeColorfulItem(Material.IRON_BLOCK, "&aSet the base damage", 1, 0, "&7Set the base damage of the\n&7projectile spawned!\n&7Maximum: &a100,000\n&7Minimum: &a1000\n\n&eClick to set!"));
                } else {
                    setItem(20, FILLER_GLASS);
                }
                break;
            }
        }
    }

    private void invalidNumberError(AnvilGUI.AnvilClickEvent event, Player player) {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{\"text\":\"§cThat's not a valid number!\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"§fYour input: §e" + event.getName() + "\"}}");
        PacketPlayOutChat c = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(c);

        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
        player.closeInventory();
    }
}
