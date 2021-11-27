package net.atlas.SkyblockSandbox.command.commands;

import net.atlas.SkyblockSandbox.abilityCreator.AbilityHandler;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityUtil;
import net.atlas.SkyblockSandbox.abilityCreator.AbilityValue;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommand;
import net.atlas.SkyblockSandbox.command.abstraction.SBCommandArgs;
import net.atlas.SkyblockSandbox.command.abstraction.SkyblockCommandFramework;
import net.atlas.SkyblockSandbox.gui.guis.itemCreator.pages.AbilityCreator.AbilityCreatorGUI;
import net.atlas.SkyblockSandbox.item.SBItemBuilder;
import net.atlas.SkyblockSandbox.item.SBItemStack;
import net.atlas.SkyblockSandbox.player.SBPlayer;
import net.atlas.SkyblockSandbox.util.NumUtils;
import net.atlas.SkyblockSandbox.util.SUtil;
import net.atlas.SkyblockSandbox.util.StackUtils;
import org.bukkit.plugin.Plugin;

import static net.atlas.SkyblockSandbox.util.SUtil.colorize;

public class Command_ability extends SkyblockCommandFramework {
    /**
     * Initializes the command framework and sets up the command maps
     *
     * @param plugin
     */
    public Command_ability(Plugin plugin) {
        super(plugin);
    }

    @SBCommand(name = "ability", description = "Ability command", usage = "/ability <number> [name/description/<desc/cooldown/cost]", inGameOnly = true)
    public void abilityCMD(SBCommandArgs cmd) {
        SBPlayer player = new SBPlayer(cmd.getPlayer());
        String[] args = cmd.getArgs();
        if ((args.length < 1)) {
            player.sendMessage("&cInvalid usage /ability <number> [name/description/<desc/cooldown/cost]");
            return;
        }
        if (!NumUtils.isInt(args[0])) {
            player.sendMessage("&cAbility number must be a number!");
            return;
        }
        int index = Integer.parseInt(args[0]) - 1;
        if (index < 1) {
            player.sendMessage("&cAbility number must be more than 0!");
            return;
        }
        if (index > 5) {
            player.sendMessage("&cAbility number must be less than 6!");
            return;
        }
        if (player.getSetting(SBPlayer.Settings.LORE_GEN) && !player.getStringFromItemInHand("non-legacy").equals("true")) {
            player.sendMessage("&cYou must set a name before continuing!");
            return;
        }
        if (args.length == 1) {
            new AbilityCreatorGUI(player, index).open();
            return;
        }
        switch (args[1]) {
            case "desc":
            case "description": {
                if (player.getSetting(SBPlayer.Settings.LORE_GEN)) {
                    if (!NumUtils.isInt(args[2])) {
                        player.sendMessage("&cThe description line must be a number!");
                        break;
                    }
                    if (Integer.parseInt(args[2]) <= 0) {
                        player.sendMessage("&cThe description line must be greater than 0!");
                        break;
                    }
                    if (Integer.parseInt(args[2]) >= 15) {
                        player.sendMessage("&cThe description line cannot be more than 15!");
                        break;
                    }
                    if (!(args.length >= 4)) {
                        player.sendMessage("&cInvalid usage /ability <ability number> <desc/description> <line num> <string>");
                        break;
                    }
                    StackUtils.setDescriptionLine(index, Integer.parseInt(args[2]), player, args);
                } else {
                    player.sendMessage("&cYou must have Auto Lore Generation enabled for this to work!");
                    break;
                }
                break;
            }
            case "name": {
                if (!(args.length >= 3)) {
                    player.sendMessage("&cInvalid usage /ability <ability number> <name> <string>");
                    break;
                }
                StringBuilder s = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    if (i == args.length - 1) {
                        s.append(args[i]);
                    } else {
                        s.append(args[i]).append(" ");
                    }
                }
                if (player.getSetting(SBPlayer.Settings.LORE_GEN)) {
                    player.setItemInHand(new SBItemBuilder(player.getItemInHand()).abilities.get(index).name(s.toString()).build().build());
                } else {
                    player.setItemInHand(AbilityUtil.setAbilityData(player.getItemInHand(), index, AbilityValue.NAME, s));
                }
                player.sendMessage("&aSuccessfully set ability name to: &e" + s);
                break;
            }
            case "cooldown": {
                if (!(args.length == 3)) {
                    player.sendMessage("&cInvalid usage /ability <ability number> <name> <number>");
                    break;
                }
                if (!NumUtils.isInt(args[2])) {
                    player.sendMessage("&cThe cooldown must be a number!");
                    break;
                }
                if (Integer.parseInt(args[2]) < 0) {
                    player.sendMessage("&cThe cooldown cannot be less than 0!");
                    break;
                }
                if (Integer.parseInt(args[2]) >= 600) {
                    player.sendMessage("&cThe description line cannot be more than 600!");
                    break;
                }
                if (player.getSetting(SBPlayer.Settings.LORE_GEN)) {
                    player.setItemInHand(new SBItemBuilder(player.getItemInHand()).abilities.get(index).cooldown(Integer.parseInt(args[2])).build().build());
                } else {
                    player.setItemInHand(AbilityUtil.setAbilityData(player.getItemInHand(), index, AbilityValue.COOLDOWN, Integer.parseInt(args[2])));
                }
                player.sendMessage("&aSuccessfully set ability cooldown to: &e" + Integer.parseInt(args[2]));
                break;
            }
            case "cost": {
                if (!(args.length == 3)) {
                    player.sendMessage("&cInvalid usage /ability <ability number> <cost> <number>");
                    break;
                }
                if (!NumUtils.isInt(args[2])) {
                    player.sendMessage("&cThe mana cost must be a number!");
                    break;
                }
                if (Integer.parseInt(args[2]) < 0) {
                    player.sendMessage("&cThe mana cost cannot be less than 0!");
                    break;
                }
                if (Integer.parseInt(args[2]) >= 100000) {
                    player.sendMessage("&cThe description line cannot be more than 100000!");
                    break;
                }
                if (player.getSetting(SBPlayer.Settings.LORE_GEN)) {
                    player.setItemInHand(new SBItemBuilder(player.getItemInHand()).abilities.get(index).manaCost(Integer.parseInt(args[2])).build().build());
                } else {
                    player.setItemInHand(AbilityUtil.setAbilityData(player.getItemInHand(), index, AbilityValue.MANA_COST, Integer.parseInt(args[2])));
                }
                player.sendMessage("&aSuccessfully set ability mana cost to: &e" + Integer.parseInt(args[2]));
                break;
            }
        }
    }
}
