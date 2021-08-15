package net.atlas.SkyblockSandbox.player;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.net.InetSocketAddress;
import java.util.*;

public abstract class PluginPlayer implements Player {

    private final Player player;

    public PluginPlayer(Player player) {
        this.player = player;
    }

    public String getDisplayName() {
        return player.getDisplayName();
    }

    public void setDisplayName(String s) {
        player.setDisplayName(s);
    }

    public String getPlayerListName() {
        return player.getPlayerListName();
    }

    public void setPlayerListName(String s) {
        player.setPlayerListName(s);
    }

    public void setCompassTarget(Location location) {
        player.setCompassTarget(location);
    }

    public Location getCompassTarget() {
        return player.getCompassTarget();
    }

    public InetSocketAddress getAddress() {
        return player.getAddress();
    }

    public boolean isConversing() {
        return player.isConversing();
    }

    public void acceptConversationInput(String s) {
        player.acceptConversationInput(s);
    }

    public boolean beginConversation(Conversation conversation) {
        return player.beginConversation(conversation);
    }

    public void abandonConversation(Conversation conversation) {
        player.abandonConversation(conversation);
    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent) {
        player.abandonConversation(conversation, conversationAbandonedEvent);
    }

    public void sendRawMessage(String s) {
        player.sendRawMessage(s);
    }

    public void kickPlayer(String s) {
        player.kickPlayer(s);
    }

    public void chat(String s) {
        player.chat(s);
    }

    public boolean performCommand(String s) {
        return player.performCommand(s);
    }

    public boolean isSneaking() {
        return player.isSneaking();
    }

    public void setSneaking(boolean b) {
        player.setSneaking(b);
    }

    public boolean isSprinting() {
        return player.isSprinting();
    }

    public void setSprinting(boolean b) {
        player.setSprinting(b);
    }

    public void saveData() {
        player.saveData();
    }

    public void loadData() {
        player.loadData();
    }

    public void setSleepingIgnored(boolean b) {
        player.setSleepingIgnored(b);
    }

    public boolean isSleepingIgnored() {
        return player.isSleepingIgnored();
    }

    public void playNote(Location location, byte b, byte b1) {
        player.playNote(location, b, b1);
    }

    public void playNote(Location location, Instrument instrument, Note note) {
        player.playNote(location, instrument, note);
    }

    public void playSound(Location location, Sound sound, float v, float v1) {
        player.playSound(location, sound, v, v1);
    }

    public void playSound(Location location, String s, float v, float v1) {
        player.playSound(location, s, v, v1);
    }

    public void playEffect(Location location, Effect effect, int i) {
        player.playEffect(location, effect, i);
    }

    public <T> void playEffect(Location location, Effect effect, T t) {
        player.playEffect(location, effect, t);
    }

    public void sendBlockChange(Location location, Material material, byte b) {
        player.sendBlockChange(location, material, b);
    }

    public boolean sendChunkChange(Location location, int i, int i1, int i2, byte[] bytes) {
        return player.sendChunkChange(location, i, i1, i2, bytes);
    }

    public void sendBlockChange(Location location, int i, byte b) {
        player.sendBlockChange(location, i, b);
    }

    public void sendSignChange(Location location, String[] strings) throws IllegalArgumentException {
        player.sendSignChange(location, strings);
    }

    public void sendMap(MapView mapView) {
        player.sendMap(mapView);
    }

    public void updateInventory() {
        player.updateInventory();
    }

    public void awardAchievement(Achievement achievement) {
        player.awardAchievement(achievement);
    }

    public void removeAchievement(Achievement achievement) {
        player.removeAchievement(achievement);
    }

    public boolean hasAchievement(Achievement achievement) {
        return player.hasAchievement(achievement);
    }

    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        player.incrementStatistic(statistic);
    }

    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        player.decrementStatistic(statistic);
    }

    public void incrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        player.incrementStatistic(statistic, i);
    }

    public void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        player.decrementStatistic(statistic, i);
    }

    public void setStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        player.setStatistic(statistic, i);
    }

    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        return player.getStatistic(statistic);
    }

    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        player.incrementStatistic(statistic, material);
    }

    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        player.decrementStatistic(statistic, material);
    }

    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        return player.getStatistic(statistic, material);
    }

    public void incrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        player.incrementStatistic(statistic, material, i);
    }

    public void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        player.decrementStatistic(statistic, material, i);
    }

    public void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        player.setStatistic(statistic, material, i);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        player.incrementStatistic(statistic, entityType);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        player.decrementStatistic(statistic, entityType);
    }

    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        return player.getStatistic(statistic, entityType);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType, int i) throws IllegalArgumentException {
        player.incrementStatistic(statistic, entityType, i);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType, int i) {
        player.decrementStatistic(statistic, entityType, i);
    }

    public void setStatistic(Statistic statistic, EntityType entityType, int i) {
        player.setStatistic(statistic, entityType, i);
    }

    public void setPlayerTime(long l, boolean b) {
        player.setPlayerTime(l, b);
    }

    public long getPlayerTime() {
        return player.getPlayerTime();
    }

    public long getPlayerTimeOffset() {
        return player.getPlayerTimeOffset();
    }

    public boolean isPlayerTimeRelative() {
        return player.isPlayerTimeRelative();
    }

    public void resetPlayerTime() {
        player.resetPlayerTime();
    }

    public void setPlayerWeather(WeatherType weatherType) {
        player.setPlayerWeather(weatherType);
    }

    public WeatherType getPlayerWeather() {
        return player.getPlayerWeather();
    }

    public void resetPlayerWeather() {
        player.resetPlayerWeather();
    }

    public void giveExp(int i) {
        player.giveExp(i);
    }

    public void giveExpLevels(int i) {
        player.giveExpLevels(i);
    }

    public float getExp() {
        return player.getExp();
    }

    public void setExp(float v) {
        player.setExp(v);
    }

    public int getLevel() {
        return player.getLevel();
    }

    public void setLevel(int i) {
        player.setLevel(i);
    }

    public int getTotalExperience() {
        return player.getTotalExperience();
    }

    public void setTotalExperience(int i) {
        player.setTotalExperience(i);
    }

    public float getExhaustion() {
        return player.getExhaustion();
    }

    public void setExhaustion(float v) {
        player.setExhaustion(v);
    }

    public float getSaturation() {
        return player.getSaturation();
    }

    public void setSaturation(float v) {
        player.setSaturation(v);
    }

    public int getFoodLevel() {
        return player.getFoodLevel();
    }

    public void setFoodLevel(int i) {
        player.setFoodLevel(i);
    }

    public Location getBedSpawnLocation() {
        return player.getBedSpawnLocation();
    }

    public void setBedSpawnLocation(Location location) {
        player.setBedSpawnLocation(location);
    }

    public void setBedSpawnLocation(Location location, boolean b) {
        player.setBedSpawnLocation(location, b);
    }

    public boolean getAllowFlight() {
        return player.getAllowFlight();
    }

    public void setAllowFlight(boolean b) {
        player.setAllowFlight(b);
    }

    public void hidePlayer(Player player) {
        this.player.hidePlayer(player);
    }

    public void showPlayer(Player player) {
        this.player.showPlayer(player);
    }

    public boolean canSee(Player player) {
        return this.player.canSee(player);
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public Location getLocation(Location location) {
        return player.getLocation(location);
    }

    public void setVelocity(Vector vector) {
        player.setVelocity(vector);
    }

    public Vector getVelocity() {
        return player.getVelocity();
    }

    public boolean isOnGround() {
        return player.isOnGround();
    }

    public World getWorld() {
        return player.getWorld();
    }

    public boolean teleport(Location location) {
        return player.teleport(location);
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        return player.teleport(location, teleportCause);
    }

    public boolean teleport(Entity entity) {
        return player.teleport(entity);
    }

    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause) {
        return player.teleport(entity, teleportCause);
    }

    public List<Entity> getNearbyEntities(double v, double v1, double v2) {
        return player.getNearbyEntities(v, v1, v2);
    }

    public int getEntityId() {
        return player.getEntityId();
    }

    public int getFireTicks() {
        return player.getFireTicks();
    }

    public int getMaxFireTicks() {
        return player.getMaxFireTicks();
    }

    public void setFireTicks(int i) {
        player.setFireTicks(i);
    }

    public void remove() {
        player.remove();
    }

    public boolean isDead() {
        return player.isDead();
    }

    public boolean isValid() {
        return player.isValid();
    }

    public void sendMessage(String s) {
        player.sendMessage(s);
    }

    public void sendMessage(String[] strings) {
        player.sendMessage(strings);
    }

    public Server getServer() {
        return player.getServer();
    }

    public Entity getPassenger() {
        return player.getPassenger();
    }

    public boolean setPassenger(Entity entity) {
        return player.setPassenger(entity);
    }

    public boolean isEmpty() {
        return player.isEmpty();
    }

    public boolean eject() {
        return player.eject();
    }

    public float getFallDistance() {
        return player.getFallDistance();
    }

    public void setFallDistance(float v) {
        player.setFallDistance(v);
    }

    public void setLastDamageCause(EntityDamageEvent entityDamageEvent) {
        player.setLastDamageCause(entityDamageEvent);
    }

    public EntityDamageEvent getLastDamageCause() {
        return player.getLastDamageCause();
    }

    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    public boolean isBanned() {
        return player.isBanned();
    }

    public void setBanned(boolean b) {
        player.setBanned(b);
    }

    public boolean isWhitelisted() {
        return player.isWhitelisted();
    }

    public void setWhitelisted(boolean b) {
        player.setWhitelisted(b);
    }

    public Player getPlayer() {
        return player.getPlayer();
    }

    public long getFirstPlayed() {
        return player.getFirstPlayed();
    }

    public long getLastPlayed() {
        return player.getLastPlayed();
    }

    public boolean hasPlayedBefore() {
        return player.hasPlayedBefore();
    }

    public int getTicksLived() {
        return player.getTicksLived();
    }

    public void setTicksLived(int i) {
        player.setTicksLived(i);
    }

    public void playEffect(EntityEffect entityEffect) {
        player.playEffect(entityEffect);
    }

    public EntityType getType() {
        return player.getType();
    }

    public boolean isInsideVehicle() {
        return player.isInsideVehicle();
    }

    public boolean leaveVehicle() {
        return player.leaveVehicle();
    }

    public Entity getVehicle() {
        return player.getVehicle();
    }

    public void setCustomName(String s) {
        player.setCustomName(s);
    }

    public String getCustomName() {
        return player.getCustomName();
    }

    public void setCustomNameVisible(boolean b) {
        player.setCustomNameVisible(b);
    }

    public boolean isCustomNameVisible() {
        return player.isCustomNameVisible();
    }

    public boolean isFlying() {
        return player.isFlying();
    }

    public void setFlying(boolean b) {
        player.setFlying(b);
    }

    public void setFlySpeed(float v) throws IllegalArgumentException {
        player.setFlySpeed(v);
    }

    public void setWalkSpeed(float v) throws IllegalArgumentException {
        player.setWalkSpeed(v);
    }

    public float getFlySpeed() {
        return player.getFlySpeed();
    }

    public float getWalkSpeed() {
        return player.getWalkSpeed();
    }

    public void setTexturePack(String s) {
        player.setTexturePack(s);
    }

    public void setResourcePack(String s) {
        player.setResourcePack(s);
    }

    public Scoreboard getScoreboard() {
        return player.getScoreboard();
    }

    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        player.setScoreboard(scoreboard);
    }

    public boolean isHealthScaled() {
        return player.isHealthScaled();
    }

    public void setHealthScaled(boolean b) {
        player.setHealthScaled(b);
    }

    public void setHealthScale(double v) throws IllegalArgumentException {
        player.setHealthScale(v);
    }

    public double getHealthScale() {
        return player.getHealthScale();
    }

    public Entity getSpectatorTarget() {
        return player.getSpectatorTarget();
    }

    public void setSpectatorTarget(Entity entity) {
        player.setSpectatorTarget(entity);
    }

    public void sendTitle(String s, String s1) {
        player.sendTitle(s, s1);
    }

    public void resetTitle() {
        player.resetTitle();
    }

    public Spigot spigot() {
        return player.spigot();
    }

    public Map<String, Object> serialize() {
        return player.serialize();
    }

    public boolean isOnline() {
        return player.isOnline();
    }

    public String getName() {
        return player.getName();
    }

    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    public Inventory getEnderChest() {
        return player.getEnderChest();
    }

    public boolean setWindowProperty(InventoryView.Property property, int i) {
        return player.setWindowProperty(property, i);
    }

    public InventoryView getOpenInventory() {
        return player.getOpenInventory();
    }

    public InventoryView openInventory(Inventory inventory) {
        return player.openInventory(inventory);
    }

    public InventoryView openWorkbench(Location location, boolean b) {
        return player.openWorkbench(location, b);
    }

    public InventoryView openEnchanting(Location location, boolean b) {
        return player.openEnchanting(location, b);
    }

    public void openInventory(InventoryView inventoryView) {
        player.openInventory(inventoryView);
    }

    public void closeInventory() {
        player.closeInventory();
    }

    public ItemStack getItemInHand() {
        return player.getItemInHand();
    }

    public void setItemInHand(ItemStack itemStack) {
        player.setItemInHand(itemStack);
    }

    public ItemStack getItemOnCursor() {
        return player.getItemOnCursor();
    }

    public void setItemOnCursor(ItemStack itemStack) {
        player.setItemOnCursor(itemStack);
    }

    public boolean isSleeping() {
        return player.isSleeping();
    }

    public int getSleepTicks() {
        return player.getSleepTicks();
    }

    public GameMode getGameMode() {
        return player.getGameMode();
    }

    public void setGameMode(GameMode gameMode) {
        player.setGameMode(gameMode);
    }

    public boolean isBlocking() {
        return player.isBlocking();
    }

    public int getExpToLevel() {
        return player.getExpToLevel();
    }

    public double getEyeHeight() {
        return player.getEyeHeight();
    }

    public double getEyeHeight(boolean b) {
        return player.getEyeHeight(b);
    }

    public Location getEyeLocation() {
        return player.getEyeLocation();
    }

    @Deprecated
    public List<Block> getLineOfSight(HashSet<Byte> hashSet, int i) {
        return player.getLineOfSight(hashSet, i);
    }

    public List<Block> getLineOfSight(Set<Material> set, int i) {
        return player.getLineOfSight(set, i);
    }

    public Block getTargetBlock(HashSet<Byte> hashSet, int i) {
        return player.getTargetBlock(hashSet, i);
    }

    public Block getTargetBlock(Set<Material> set, int i) {
        return player.getTargetBlock(set, i);
    }

    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> hashSet, int i) {
        return player.getLastTwoTargetBlocks(hashSet, i);
    }

    public List<Block> getLastTwoTargetBlocks(Set<Material> set, int i) {
        return player.getLastTwoTargetBlocks(set, i);
    }

    public Egg throwEgg() {
        return player.throwEgg();
    }

    public Snowball throwSnowball() {
        return player.throwSnowball();
    }

    public Arrow shootArrow() {
        return player.shootArrow();
    }

    public int getRemainingAir() {
        return player.getRemainingAir();
    }

    public void setRemainingAir(int i) {
        player.setRemainingAir(i);
    }

    public int getMaximumAir() {
        return player.getMaximumAir();
    }

    public void setMaximumAir(int i) {
        player.setMaximumAir(i);
    }

    public int getMaximumNoDamageTicks() {
        return player.getMaximumNoDamageTicks();
    }

    public void setMaximumNoDamageTicks(int i) {
        player.setMaximumNoDamageTicks(i);
    }

    public double getLastDamage() {
        return player.getLastDamage();
    }

    public void setLastDamage(double v) {
        player.setLastDamage(v);
    }

    public int getNoDamageTicks() {
        return player.getNoDamageTicks();
    }

    public void setNoDamageTicks(int i) {
        player.setNoDamageTicks(i);
    }

    public Player getKiller() {
        return player.getKiller();
    }

    public boolean addPotionEffect(PotionEffect potionEffect) {
        return player.addPotionEffect(potionEffect);
    }

    public boolean addPotionEffect(PotionEffect potionEffect, boolean b) {
        return player.addPotionEffect(potionEffect, b);
    }

    public boolean addPotionEffects(Collection<PotionEffect> collection) {
        return player.addPotionEffects(collection);
    }

    public boolean hasPotionEffect(PotionEffectType potionEffectType) {
        return player.hasPotionEffect(potionEffectType);
    }

    public void removePotionEffect(PotionEffectType potionEffectType) {
        player.removePotionEffect(potionEffectType);
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        return player.getActivePotionEffects();
    }

    public boolean hasLineOfSight(Entity entity) {
        return player.hasLineOfSight(entity);
    }

    public boolean getRemoveWhenFarAway() {
        return player.getRemoveWhenFarAway();
    }

    public void setRemoveWhenFarAway(boolean b) {
        player.setRemoveWhenFarAway(b);
    }

    public EntityEquipment getEquipment() {
        return player.getEquipment();
    }

    public void setCanPickupItems(boolean b) {
        player.setCanPickupItems(b);
    }

    public boolean getCanPickupItems() {
        return player.getCanPickupItems();
    }

    public boolean isLeashed() {
        return player.isLeashed();
    }

    public Entity getLeashHolder() throws IllegalStateException {
        return player.getLeashHolder();
    }

    public boolean setLeashHolder(Entity entity) {
        return player.setLeashHolder(entity);
    }

    public void damage(double v) {
        player.damage(v);
    }

    public void damage(double v, Entity entity) {
        player.damage(v, entity);
    }

    public double getHealth() {
        return player.getHealth();
    }

    public void setHealth(double v) {
        player.setHealth(v);
    }

    public double getMaxHealth() {
        return player.getMaxHealth();
    }


    public void setMaxHealth(double v) {
        player.setMaxHealth(v);
    }

    public void resetMaxHealth() {
        player.resetMaxHealth();
    }

    public void setMetadata(String s, MetadataValue metadataValue) {
        player.setMetadata(s, metadataValue);
    }

    public List<MetadataValue> getMetadata(String s) {
        return player.getMetadata(s);
    }

    public boolean hasMetadata(String s) {
        return player.hasMetadata(s);
    }

    public void removeMetadata(String s, Plugin plugin) {
        player.removeMetadata(s, plugin);
    }

    public boolean isPermissionSet(String s) {
        return player.isPermissionSet(s);
    }

    public boolean isPermissionSet(Permission permission) {
        return player.isPermissionSet(permission);
    }

    public boolean hasPermission(String s) {
        return player.hasPermission(s);
    }

    public boolean hasPermission(Permission permission) {
        return player.hasPermission(permission);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
        return player.addAttachment(plugin, s, b);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return player.addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
        return player.addAttachment(plugin, s, b, i);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        return player.addAttachment(plugin, i);
    }

    public void removeAttachment(PermissionAttachment permissionAttachment) {
        player.removeAttachment(permissionAttachment);
    }

    public void recalculatePermissions() {
        player.recalculatePermissions();
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return player.getEffectivePermissions();
    }

    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {
        player.sendPluginMessage(plugin, s, bytes);
    }

    public Set<String> getListeningPluginChannels() {
        return player.getListeningPluginChannels();
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass) {
        return player.launchProjectile(aClass);
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass, Vector vector) {
        return player.launchProjectile(aClass, vector);
    }

    public boolean isOp() {
        return player.isOp();
    }

    public void setOp(boolean b) {
        player.setOp(b);
    }

}

