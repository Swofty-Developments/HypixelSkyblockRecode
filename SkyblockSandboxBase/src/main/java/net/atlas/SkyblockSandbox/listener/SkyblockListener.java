package net.atlas.SkyblockSandbox.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.atlas.SkyblockSandbox.SBX;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
                        }
                    }
                });


        Reflections reflection = new Reflections("net.atlas.SkyblockSandbox.listener");
        for(Class<? extends SkyblockListener> l:reflection.getSubTypesOf(SkyblockListener.class)) {
            try {
                Constructor<? extends SkyblockListener> ctor = l.getDeclaredConstructor();
                SkyblockListener listener = ctor.newInstance();
                Bukkit.getPluginManager().registerEvents(listener,SBX.getInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                ex.printStackTrace();
            }

        }
    }
}
