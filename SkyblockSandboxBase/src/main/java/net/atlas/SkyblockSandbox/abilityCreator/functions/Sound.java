package net.atlas.SkyblockSandbox.abilityCreator.functions;

import com.google.common.base.Enums;
import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.abilityCreator.Function;
import net.atlas.SkyblockSandbox.abilityCreator.FunctionUtil;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.functionCreator.functionTypes.SoundChooserGUI;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.SUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.atlas.SkyblockSandbox.util.StackUtils.makeColorfulItem;

public class Sound extends Function {

    org.bukkit.Sound sound;

    public Sound(SBPlayer player, ItemStack stack, int abilIndex, int functionIndex) {
        super(player, stack, abilIndex, functionIndex);
        this.sound = Enums.getIfPresent(org.bukkit.Sound.class, FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.SOUND)).orNull();
    }

    @Override
    public void applyFunction() {
        String vol = FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.VOLUME);
        if (vol.isEmpty()) {
            vol = "1";
        }
        float volume = Float.parseFloat(vol);

        String pitc = FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.PITCH);
        if (pitc.isEmpty()) {
            pitc = "1";
        }
        double pitch = Double.parseDouble(pitc);

        String del = FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.DELAY);
        if (del.isEmpty()) {
            del = "0";
        }
        int delay = Integer.parseInt(del);
        new BukkitRunnable() {
            @Override
            public void run() {
                getPlayer().playSound(getPlayer().getLocation(), sound, volume, (float) pitch);
            }
        }.runTaskLater(SBX.getInstance(), delay * 20L);
    }

    @Override
    public AbilityValue.FunctionType getFunctionType() {
        return AbilityValue.FunctionType.SOUND;
    }

    @Override
    public List<Class<? extends Function>> conflicts() {
        return null;
    }

    public enum dataValues implements Function.dataValues {
        SOUND(),
        VOLUME(),
        PITCH(),
        DELAY(),
        AMOUNT();
    }

    @Override
    public void getGuiLayout() {
        if (this.sound == null) {
            setItem(13, makeColorfulItem(Material.NOTE_BLOCK, "&aCurrently edited sound", 1, 0, "&7Currently editing:", "&cNOT SET" + "", "", "&eLeft-Click to set!"));
            setAction(13, event -> {

                new SoundChooserGUI(getPlayer(), getAbilIndex(), getFunctionIndex()).open();

            });
        } else {
            setItem(13, makeColorfulItem(Material.NOTE_BLOCK, "&aCurrently edited sound", 1, 0, "&7Currently editing:", "&b" + sound.name() + "", "", "&eLeft-Click to change!", "&bRight-Click to play!"));
            setAction(13, event -> {
                if (event.getClick().equals(ClickType.RIGHT)) {
                    String vol = FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.VOLUME);
                    if (vol.isEmpty()) {
                        vol = "1";
                    }
                    float volume = Float.parseFloat(vol);

                    String pitc = FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.PITCH);
                    if (pitc.isEmpty()) {
                        pitc = "1";
                    }
                    double pitch = Double.parseDouble(pitc);

                    String del = FunctionUtil.getFunctionData(getStack(), getAbilIndex(), getFunctionIndex(), dataValues.DELAY);
                    if (del.isEmpty()) {
                        del = "0";
                    }
                    int delay = Integer.parseInt(del);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            getPlayer().playSound(getPlayer().getLocation(), sound, volume, (float) pitch);
                        }
                    }.runTaskLater(SBX.getInstance(), delay * 20L);
                } else {
                    new SoundChooserGUI(getPlayer(), getAbilIndex(), getFunctionIndex()).open();
                }
            });
        }
        setItem(11, makeColorfulItem(Material.STICK, "&aSet the pitch", 1, 0, "&7Set the pitch of the", "&7sound played!", "", "&7Maximum: &a2.0", "&7Minimum: &a0.5", "", "&eClick to set!"));
        setItem(12, makeColorfulItem(Material.IRON_BLOCK, "&aSet the volume", 1, 0, "&7Set the volume of the", "&7sound played!", "", "&7Maximum: &a10.0", "&7Minimum: &a1.0", "", "&eClick to set!"));
        setItem(14, makeColorfulItem(Material.BOOK_AND_QUILL, "&aSet the amount", 1, 0, "&7Set the amount of the", "&7sound played!", "", "&7Maximum: &a30", "&7Minimum: &a1", "", "&eClick to set!"));
        setItem(15, makeColorfulItem(Material.WATCH, "&aSet the delay", 1, 0, "&7Set the delay of the", "&7sound played!", "", "&7Maximum: &a5.0", "&7Minimum: &a0.0", "", "&eClick to set!"));

        setAction(11, event -> {
            anvilGUI(dataValues.PITCH);
        });
        setAction(12, event -> {
            anvilGUI(dataValues.VOLUME);
        });
        setAction(14, event -> {
            anvilGUI(dataValues.AMOUNT);
        });
        setAction(15, event -> {
            anvilGUI(dataValues.DELAY);
        });
    }
}
