package net.atlas.SkyblockSandbox.database.sql;

import net.atlas.SkyblockSandbox.SBX;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;

public class MySQL {
    private static SBX main = SBX.getInstance();
    public MySQL() {
    }

    private static final String host = main.getConfig().getString("mysql.host");
    private static final String port = main.getConfig().getString("mysql.port");
    private static final String database = main.getConfig().getString("mysql.database");
    private static final String username = main.getConfig().getString("mysql.username");
    private static final String password = main.getConfig().getString("mysql.password");

    private static Connection connection;

    public static boolean isConnected() {
        return (connection != null);
    }

    public static void connect() throws SQLException {
        if (!(isConnected())) {
            connection = DriverManager.getConnection("jdbc:mysql://" +
                            host + ":" + port + "/" + database + "?useSSL=false",
                    username, password);
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException error) {
                error.printStackTrace();
            }
        }
    }
    public Connection getConnection(){
        return connection;
    }
    public ResultSet query(String command) {
        if (command == null)
            return null;
        ResultSet rs = null;
        try {
            Statement st = this.getConnection().createStatement();
            rs = st.executeQuery(command);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message != null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SQL Query:");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Command: " + command);
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + message);
            }
        }
        return rs;
    }
}
