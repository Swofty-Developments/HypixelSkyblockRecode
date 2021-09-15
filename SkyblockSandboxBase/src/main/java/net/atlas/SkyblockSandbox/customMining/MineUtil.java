package net.atlas.SkyblockSandbox.customMining;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.atlas.SkyblockSandbox.SBX;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Set;

public class MineUtil {

    private static final ProtocolManager protocolManager = SBX.getProtcolManager();
    public static HashMap<Player, Block> diggingBlocks = new HashMap<>();
    public static void setupPacketListeners() {
        protocolManager.addPacketListener(new PacketAdapter(SBX.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event){
                PacketContainer packet = event.getPacket();
                EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().getValues().get(0);
                if(digType.equals(EnumWrappers.PlayerDigType.START_DESTROY_BLOCK)) {
                    diggingBlocks.put(event.getPlayer(),event.getPlayer().getTargetBlock((Set<Material>) null, 5));
                }
                if(digType.equals(EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK)) {
                    if (event.getPlayer().getTargetBlock((Set<Material>) null, 5).getLocation().equals(diggingBlocks.get(event.getPlayer()).getLocation())) {
                        BreakListener.setClickCanceled(event.getPlayer());
                    } else {
                        diggingBlocks.put(event.getPlayer(), event.getPlayer().getTargetBlock((Set<Material>) null,5));
                    }
                }
            }
        });


    }
}
