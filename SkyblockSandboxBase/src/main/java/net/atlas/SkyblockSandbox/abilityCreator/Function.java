package net.atlas.SkyblockSandbox.abilityCreator;

import lombok.Getter;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public abstract class Function {

    private final SBPlayer player;
    private ItemStack stack;
    private final int abilIndex;
    private final int functionIndex;

    public Function(SBPlayer player, ItemStack stack, int abilIndex, int functionIndex) {
        this.player = player;
        this.stack = stack;
        this.abilIndex = abilIndex;
        this.functionIndex = functionIndex;
    }

    public abstract void applyFunction();

    public abstract List<Class<? extends Function>> conflicts();

    public interface dataValues {
    }

}
