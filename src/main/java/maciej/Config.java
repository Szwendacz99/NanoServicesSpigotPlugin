package maciej;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;

public class Config {

    private static Plugin plugin;

    private static ConfigFilesIO diskIO;

    public static Lang lang;

    private static final YamlConfiguration config = new YamlConfiguration();
    private static final YamlConfiguration langConfig = new YamlConfiguration();

    private static void loadDefaults() throws IOException, InvalidConfigurationException {
        YamlConfiguration configuration = new YamlConfiguration();
        diskIO.loadConfigFromJar(configuration, "messages_en.yml");
        langConfig.setDefaults(configuration);
        configuration = new YamlConfiguration();
        diskIO.loadConfigFromJar(configuration, "config.yml");
        config.setDefaults(configuration);
    }

    private static void loadConfigsFromFiles() {
        plugin.getLogger().log(Level.INFO, "Loading configuration from files.");
        try {
            diskIO.loadConfigFromDisk(langConfig, new File(plugin.getDataFolder().getAbsoluteFile(), "messages.yml").getAbsolutePath());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.WARNING, "Broken messages.yml file, your settings are not loaded fully !");
        }
        try {
            diskIO.loadConfigFromDisk(config, new File(plugin.getDataFolder().getAbsoluteFile(), ConfigFilesIO.CONF_FILE_NAME).getAbsolutePath());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.WARNING, "Broken config.yml file, your settings are not loaded fully !");
        }
    }

    public static String settingsToString() {
        return config.saveToString();
    }

    public static void load() {

        try {
            loadDefaults();
            diskIO.unpack();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.WARNING, "Broken plugin file, cannot load defaults !");
        }

        loadConfigsFromFiles();


        lang = new Lang(langConfig);
    }

    public static void setup(Plugin plugin) {
        Config.plugin = plugin;
        diskIO = new ConfigFilesIO(plugin);
    }

    public static boolean getBcPlayerFoundDiamond() {
        return config.getBoolean("bc-player-found-diamond");
    }

    public static boolean getBcPlayerFoundEmerald() {
        return config.getBoolean("bc-player-found-emerald");
    }

    public static boolean getBcPlayerPlacedTNT() {
        return config.getBoolean("bc-player-placed-tnt");
    }

    public static boolean getBcPlayerSetFireTNT() {
        return config.getBoolean("bc-player-set-fire-tnt");
    }

    public static boolean getBcPlayerJoined() {
        return config.getBoolean("bc-player-joined");
    }

    public static boolean getBcPlayerLeft() {
        return config.getBoolean("bc-player-left");
    }

    public static boolean getVotekickEnabled() {
        return config.getBoolean("votekick-service");
    }

    public static boolean getBroadcastEnabled() {
        return config.getBoolean("event-broadcast-service");
    }

    public static boolean getShellCommandEnabled() {
        return config.getBoolean("shell-command-service");
    }

    public static boolean getChatColorEnabled() {
        return config.getBoolean("chat-color-service");
    }

    public static boolean getDebugEnabled() {
        return config.getBoolean("debug-service");
    }

    public static boolean getCensorEnabled() {
        return config.getBoolean("censor-service");
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<LinkedHashMap<String,String>> getForbiddenWords() {
        Object words = config.get("forbidden-words");
        if (words instanceof ArrayList) {
            return (ArrayList<LinkedHashMap<String,String>>) words;
        }
        return null;
    }
}
