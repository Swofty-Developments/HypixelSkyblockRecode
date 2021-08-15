package net.atlas.SkyblockSandbox.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.atlas.SkyblockSandbox.SBX;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.reflections.Reflections;

import static net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents.EntitySpawnEvent.holoMap2;
import static net.atlas.SkyblockSandbox.listener.sbEvents.entityEvents.EntitySpawnEvent.worldEnderman;

public abstract class SkyblockListener<E extends Event> implements Listener {


    protected void onEvent(E event) {
        if(event instanceof PlayerEvent) {
            callEvent(event);
        }
    }

    public abstract void callEvent(E event);

    public static void registerListeners() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(
                new PacketAdapter(SBX.getInstance(), ListenerPriority.HIGHEST,
                        PacketType.Play.Server.ENTITY_STATUS) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        // Item packets (id: 0x29)
                        if (event.getPacketType() ==
                                PacketType.Play.Server.ENTITY_STATUS) {
                            //System.out.println(event.getPacket().getBytes().getValues().get(0));
                            if(event.getPacket().getBytes().getValues().get(0)==(byte)2) {
                                event.setCancelled(true);
                            }

                        }
                    }
                });

        /*protocolManager.addPacketListener(
                new PacketAdapter(SBX.getInstance(), ListenerPriority.HIGHEST,
                        PacketType.Play.Server.REL_ENTITY_MOVE) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        // Item packets (id: 0x29)
                        if (event.getPacketType() ==
                                PacketType.Play.Server.REL_ENTITY_MOVE) {
                            int entID = event.getPacket().getIntegers().getValues().get(0);
                            if(holoMap2.containsKey(entID)) {
                                Entity en = holoMap2.get(entID).keySet().stream().findFirst().orElse(null);
                                System.out.println(en);
                                EntityArmorStand stand = holoMap2.get(entID).get(en);
                                System.out.println(stand);
                                stand.setLocation(en.getLocation().getX(),en.getLocation().getY()+1,en.getLocation().getZ(),0,0);
                                PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport(stand);
                                for(Player p:Bukkit.getOnlinePlayers()) {
                                    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(tp);
                                }
                                System.out.println("holy shit that worked");
                            }
                        }
                    }
                });*/

        protocolManager.addPacketListener(
                new PacketAdapter(SBX.getInstance(), ListenerPriority.HIGHEST,
                        PacketType.Play.Server.ENTITY_TELEPORT) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        // Item packets (id: 0x29)
                        if (event.getPacketType() ==
                                PacketType.Play.Server.ENTITY_TELEPORT) {
                            for(int id:worldEnderman.keySet()) {
                                if(event.getPacket().getIntegers().getValues().get(0)==id) {
                                    Location loc = worldEnderman.get(id).getLocation();
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            worldEnderman.get(id).teleport(loc);
                                        }
                                    }.runTaskLater(SBX.getInstance(),1L);
                                }
                            }
                            //event.setCancelled(true);
                            //System.out.println(event.getPacket());
                        }
                    }
                });


        Reflections reflection = new Reflections("net.atlas.SkyblockSandbox.listener");
        for(Class<? extends SkyblockListener> l:reflection.getSubTypesOf(SkyblockListener.class)) {
            try {
                SkyblockListener listener = l.newInstance();
                Bukkit.getPluginManager().registerEvents(listener,SBX.getInstance());
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }

        }
    }
}
