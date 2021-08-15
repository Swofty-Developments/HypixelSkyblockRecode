package net.atlas.SkyblockSandbox.item.ability.itemAbilities;

public class WitherImpact {}/*extends Ability {
    @Override
    public String getAbilityName() {
        return "Wither Impact";
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.RIGHT_CLICK;
    }

    @Override
    public double getManaCost() {
        return 250;
    }

    @Override
    public List<String> getAbilDescription() {
        return new ArrayList<>(Arrays.asList("§7Teleport §a10 blocks §7ahead of","§7you. Then implode dealing","§c10,000 §7damage to nearby","§7enemies. Also applies the wither","§7shield scroll ability reducing","§7damage taken and granting an absorption shield for §e5","§7seconds."));
    }

    @Override
    public void onSwapAction(Player player, PlayerItemHeldEvent event, ItemStack item) {

    }

    @Override
    public void leftClickAirAction(Player player, ItemStack item) {

    }

    @Override
    public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void rightClickAirAction(Player player, PlayerInteractEvent event, ItemStack item) {
        SBItemStack sbitem = new SBItemStack(item);
        SBPlayer p = new SBPlayer(player);
        for(int j = 1; j<sbitem.getAbilAmount()+1;j++) {
            if(sbitem.getAbilData(EnumAbilityData.NAME,j).equals(getAbilityName())) {
                if(p.getStat(SBPlayer.PlayerStat.INTELLIGENCE) <= getManaCost()) {
                    player.sendMessage("§cYou do not have enough mana to do that.");
                    return;
                }
                p.setStat(SBPlayer.PlayerStat.INTELLIGENCE,p.getStat(SBPlayer.PlayerStat.INTELLIGENCE)-getManaCost());

                double d = 10000 * (1+(p.getStat(SBPlayer.PlayerStat.INTELLIGENCE)/100) * 0.3);

                Location l = player.getLocation().clone();
                Set<Material> TRANSPARENT = new HashSet<Material>();
                TRANSPARENT.add(Material.AIR);
                TRANSPARENT.add(Material.STATIONARY_WATER);
                TRANSPARENT.add(Material.VINE);
                Teleportability teleportability = new Teleportability();
                Block targetBlock = teleportability.getTeleportBlock(player,TRANSPARENT,10);
                if(targetBlock !=null) {
                    if(targetBlock.getLocation().distance(player.getLocation())<10) {
                        player.sendMessage(ChatColor.RED + "There are blocks in the way!");
                    }
                    Location targetLocation = targetBlock.getLocation();
                    targetLocation.setPitch(player.getLocation().getPitch());
                    targetLocation.setYaw(player.getLocation().getYaw());
                    player.teleport(targetLocation.add(0.5D, 0.0D, 0.5D));
                    player.playSound(player.getLocation(), Sound.EXPLODE, 5, 1);
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    ManaEvent manaEvent = new ManaEvent();
                    IUtil.abilityUsed.put(player,true);
                    IUtil.sendActionText(player, "§c" + Math.round(user.getHealth()) + "/" + Math.round(user.getTotalHealth()) + "❤§b    §b-250 Mana (§6Wither Impact§b)    " + Math.round(user.getIntelligence()) + "/" + Math.round(user.getTotalIntelligence()) + "✎ Mana");
                    PacketParticle particle = new PacketParticle(EnumParticle.EXPLOSION_HUGE, player.getLocation().add(0,0,0), true, 0.75f, 0.75f, 0.75f, 0, 100);
                    particle.sendPlayer(player);
                }
                int i = 0;
                double totalDmg = 0;
                for (Entity en : player.getNearbyEntities(6, 6, 6)) {
                    if (/* en.getType().equals(EntityType.PLAYER) || *//*en.getType().equals(EntityType.ARMOR_STAND)) {*/
                    /*} else {
                        if (!en.getType().equals(EntityType.PLAYER)) {
                            if (en instanceof LivingEntity) {
                                i++;
                                totalDmg+=d;
                                if(en.isDead()) {
                                    ((LivingEntity) en).damage(0);
                                } else {
                                    if (en instanceof EnderDragon) {
                                        if (StartFight.fightActive) {
                                            DragonScoreboard dragonScoreboard = new DragonScoreboard(SBX.getInstance());
                                            dragonScoreboard.updateDragonDMG(player, d);
                                            LootListener.damage.put(player, StartFight.playerDMG.get(player));
                                            ((LivingEntity) en).damage(d);
                                        }
                                    } else {
                                        ((LivingEntity) en).damage(d);
                                    }
                                }
                            }
                        }

                    }
                }
                if(i >= 1) {
                    DecimalFormat format = new DecimalFormat("#,###");
                    player.sendMessage(SUtil.colorize("&7Your Implosion hit &c" + i + "&7 enemies dealing &c" + format.format(totalDmg) + " damage&7."));
                }

            }
        }
    }

    @Override
    public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block paramBlock, ItemStack item) {
        rightClickAirAction(player,event,item);
    }

    @Override
    public void shiftLeftClickAirAction(Player player, ItemStack item) {

    }

    @Override
    public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void shiftRightClickAirAction(Player player, PlayerInteractEvent event, ItemStack item) {

    }

    @Override
    public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity paramEntity, ItemStack item) {

    }

    @Override
    public void breakBlockAction(Player player, BlockBreakEvent event, Block paramBlock, ItemStack item) {

    }

    @Override
    public void clickedInInventoryAction(Player player, InventoryClickEvent event) {

    }

    @Override
    public void shiftEvent(Player p, PlayerToggleSneakEvent e) {

    }

    @Override
    public void rightClickEntityEvent(Player p, PlayerInteractAtEntityEvent e) {

    }

    @Override
    public void playerFishAction(Player player, PlayerFishEvent event, ItemStack item) {

    }

    @Override
    public void playerShootAction(Player player, EntityShootBowEvent event, ItemStack item) {

    }

    private void playSoundWithDelay(Player player, long delay, float c) {
        new BukkitRunnable() {

            @Override
            public void run() {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, c);
            }
        }.runTaskLater(Items.getInstance(), delay);
    }
}*/
