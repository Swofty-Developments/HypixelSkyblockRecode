package net.atlas.SkyblockSandbox.util.signGUI;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
public class SignGUI {
    protected ProtocolManager protocolManager;
    protected PacketAdapter packetListener;
    protected Map<String, SignGUIListener> listeners;
    protected Map<String, Vector> signLocations;

    public SignGUI(Plugin plugin) {
        protocolManager = ProtocolLibrary.getProtocolManager();
        listeners = new ConcurrentHashMap<String, SignGUIListener>();
        signLocations = new ConcurrentHashMap<String, Vector>();

        ProtocolLibrary.getProtocolManager().addPacketListener(
                packetListener =  new PacketAdapter(plugin, PacketType.Play.Client.UPDATE_SIGN) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        final Player player = event.getPlayer();
                        Vector v = signLocations.remove(player.getName());
                        BlockPosition bp = event.getPacket().getBlockPositionModifier().getValues().get(0);
                        final WrappedChatComponent[] chatarray = event.getPacket().getChatComponentArrays().getValues().get(0);
                        final String[] lines = {chatarray[0].getJson(), chatarray[1].getJson(), chatarray[2].getJson(), chatarray[3].getJson()};
                        final SignGUIListener response = listeners.remove(event.getPlayer().getName());

                        if (v == null) return;
                        if (bp.getX() != v.getBlockX()) return;
                        if (bp.getY() != v.getBlockY()) return;
                        if (bp.getZ() != v.getBlockZ()) return;
                        if (response != null) {
                            event.setCancelled(true);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                public void run() {
                                    response.onSignDone(player, lines);
                                }
                            });
                        }
                    }
                }
        );
    }


    public void open(Player player, String[] defaultText, SignGUIListener response) {
        List<PacketContainer> packets = new ArrayList<PacketContainer>();
        int x = player.getLocation().getBlockX();
        int y = 0;
        int z = player.getLocation().getBlockZ();
        BlockPosition bpos = new BlockPosition(x, y, z);
        PacketContainer packet133 = protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_ENTITY);

        if (defaultText != null) {
            PacketContainer packet53 = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
            PacketContainer packet130 = protocolManager.createPacket(PacketType.Play.Server.UPDATE_SIGN);
            WrappedBlockData iblock = WrappedBlockData.createData(Material.SIGN_POST);
            WrappedChatComponent[] cc = {WrappedChatComponent.fromText(defaultText[0]), WrappedChatComponent.fromText(defaultText[1]), WrappedChatComponent.fromText(defaultText[2]), WrappedChatComponent.fromText(defaultText[3])};

            packet53.getBlockPositionModifier().write(0, bpos);
            packet53.getBlockData().write(0, iblock);
            packet130.getBlockPositionModifier().write(0, bpos);
            packet130.getChatComponentArrays().write(0, cc);
            packets.add(packet53);
            packets.add(packet130);
        }


        packet133.getBlockPositionModifier().write(0, bpos);
        packets.add(packet133);

        if (defaultText != null) {
            PacketContainer packet53 = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
            WrappedBlockData iblock = WrappedBlockData.createData(Material.BEDROCK);

            packet53.getBlockPositionModifier().write(0, bpos);
            packet53.getBlockData().write(0, iblock);
            packets.add(packet53);
        }

        try {
            for (PacketContainer packet : packets) {
                protocolManager.sendServerPacket(player, packet);
            }

            signLocations.put(player.getName(), new Vector(x, y, z));
            listeners.put(player.getName(), response);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void destroy() {
        protocolManager.removePacketListener(packetListener);
        listeners.clear();
        signLocations.clear();
    }

    public interface SignGUIListener {
        public void onSignDone(Player player, String[] lines);
    }
}