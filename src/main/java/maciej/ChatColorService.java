package maciej;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ChatColorService extends AbstractService implements CommandExecutor, Listener {

    private final Map<String, String> Colors = new HashMap<>();

    private final String DB_NAME = "Colors.db";

    private Database database;

    private PreparedStatement addColorStatement;
    private PreparedStatement getColorStatement;
    private PreparedStatement updateColorStatement;

    private final Plugin plugin;

    public ChatColorService(Plugin plugin) {
        this.plugin = plugin;
        this.serviceName = "Chat Color";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player)
        {
            if(strings.length==1 && strings[0].length()==1)
            {
                Player player = ((Player) commandSender).getPlayer();
                assert player != null;
                updatePlayerColor(commandSender.getName(),strings[0]);
                player.sendMessage("§"+strings[0]+Config.lang.getChatColorNewColor());
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        try {
            String color = getColorFromDB(event.getPlayer().getName());
            if (color != null) {
                Colors.put(event.getPlayer().getName(), color);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            plugin.getLogger().log(Level.WARNING, "Error on retrieving Color data from database!");
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        if (checkForPlayerColor(event.getPlayer().getName()))
            Colors.remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {

        String msg = e.getMessage().replace("&", "§");

        String color = getPlayerColor(e.getPlayer().getName());
        if(color!=null)
        {
            msg="§"+color+msg;
        }
        e.setMessage(msg);
    }

    private void addColorToDB(String nickname, String color) throws SQLException {
        addColorStatement.setString(1, nickname);
        addColorStatement.setString(2, color);
        addColorStatement.executeUpdate();
    }

    private String getColorFromDB(String nickname) throws SQLException {
        getColorStatement.setString(1, nickname);
        ResultSet result = getColorStatement.executeQuery();
        if (result.next())
            return result.getString("Color");
        else
            return null;
    }

    private void updateColorInDB(String nickname, String color) throws SQLException {
        updateColorStatement.setString(1, color);
        updateColorStatement.setString(2, nickname);
        updateColorStatement.executeUpdate();
    }

    private int getNumberOfRowsInDB() {
        String sql = "SELECT COUNT(Nickname) FROM Colors";
        try {
            PreparedStatement statement = database.getPreparedStatement(sql);
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }

    }

    private boolean checkForPlayerColor(String nickname)
    {
        return Colors.get(nickname) != null;
    }

    private String getPlayerColor(String nickname)
    {
        return Colors.get(nickname);
    }

    private void updatePlayerColor(String nickname, String color)
    {
        Colors.put(nickname, color);
        try {
            String DBColor = getColorFromDB(nickname);
            if (DBColor == null)
            {
                addColorToDB(nickname, color);
            }else
                updateColorInDB(nickname, color);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
            plugin.getLogger().log(Level.WARNING, "Unable to save Color data to database!");
        }
    }

    public void prepareDatabase() throws SQLException {
        database = new Database(DB_NAME, plugin.getDataFolder().getPath());

        String sql = "CREATE TABLE IF NOT EXISTS Colors (Nickname VARCHAR PRIMARY KEY, Color CHAR NOT NULL)";
        PreparedStatement preparedStatement = database.getPreparedStatement(sql);
        preparedStatement.execute();

        sql = "INSERT INTO Colors (Nickname, Color) VALUES (?, ?)";
        addColorStatement = database.getPreparedStatement(sql);

        sql = "UPDATE Colors SET Color = ? WHERE Nickname = ?";
        updateColorStatement = database.getPreparedStatement(sql);

        sql = "SELECT Color FROM Colors WHERE Nickname = ?";
        getColorStatement = database.getPreparedStatement(sql);
    }

    public String[] debugInfo() {
        String[] data = new String[2];
        data[0] = "Currently loaded player setups: §3" + Colors.size();
        data[1] = "Player setups in database: §3" + getNumberOfRowsInDB();

        return data;
    }

    @Override
    public void enable() {
        try {
            prepareDatabase();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            plugin.getLogger().log(Level.WARNING, serviceName + " service was unable to open its database!");
        }
        registerEvents();
        plugin.getLogger().log(Level.INFO, "Chat Color service enabled");
    }

    @Override
    public void disable() {
        try {
            database.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            plugin.getLogger().log(Level.WARNING, serviceName + " service was unable to close its database!");
        }
        plugin.getLogger().log(Level.INFO, "Chat Color service disabled");
    }

    public void registerEvents()
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


}
