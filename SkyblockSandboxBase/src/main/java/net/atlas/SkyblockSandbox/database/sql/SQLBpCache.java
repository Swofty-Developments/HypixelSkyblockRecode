package net.atlas.SkyblockSandbox.database.sql;

import net.atlas.SkyblockSandbox.SBX;
import net.atlas.SkyblockSandbox.gui.guis.backpacks.Backpack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SQLBpCache {

    private final SBX plugin;

    public SQLBpCache(final SBX plugin) {
        this.plugin = plugin;
    }

    public static void init() {
        try {
            MySQL.connect();
            BackpackData.restore();
            SQLBpCache.createBPCache();
            System.out.println("SQL Successfully connected.");
        } catch (SQLException ignored) {
            System.out.println("SQL Connection failed.");
        }
    }

    public static void createBPCache() {
        final SBX plugin = SBX.getInstance();
        try {

            final PreparedStatement statement = plugin.sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `Backpack_Cache` (`BPUUID` varchar(50),`BACKPACKDATA` LONGTEXT NOT NULL,PRIMARY KEY (BPUUID))");
            statement.executeUpdate();
        }
        catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void setBackpack(final String bpUUID,final String serializedBP) {
        try {
            final PreparedStatement statement = this.plugin.sql.getConnection().prepareStatement("REPLACE INTO `Backpack_Cache` (`BPUUID`,`BACKPACKDATA`) VALUES (?, ?)");
            final PreparedStatement statement1 = this.plugin.sql.getConnection().prepareStatement("SELECT * FROM `Backpack_Cache`");
            statement.setString(1, bpUUID);
            statement.setString(2, serializedBP);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String,String> getBackpack() {
        Map<String,String> backpacks = new HashMap<>();
        try {
            final PreparedStatement statement = this.plugin.sql.getConnection().prepareStatement("SELECT * FROM `Backpack_Cache`");
            try (final ResultSet rs = statement.executeQuery()) {
                String bpUUID = null;
                String bpData = null;
                if (rs.next()) {
                    bpUUID = rs.getString("BPUUID");
                    bpData = rs.getString("BACKPACKDATA");
                    backpacks.put(bpUUID,bpData);
                }
                return backpacks;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
