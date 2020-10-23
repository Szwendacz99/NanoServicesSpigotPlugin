package maciej;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Objects;
import java.util.logging.Level;

public class ConfigFilesIO {

    public static final String CONF_FILE_NAME = "config.yml";
    public static final String LANG_FILE_NAME = "messages.yml";

    private static final String[] LANG_CONFS = {"messages_en.yml", "messages_pl.yml"};

    private final Plugin plugin;

    public ConfigFilesIO(Plugin plugin) {
        this.plugin = plugin;
    }


    public void updateFile(File file, String filename) throws IOException, InvalidConfigurationException {
        YamlConfiguration currentConf = new YamlConfiguration();
        loadConfigFromDisk(currentConf, file.getAbsolutePath());
        int currentVersion = versionToInt(currentConf.getString("version"));

        YamlConfiguration latestConf = new YamlConfiguration();
        loadConfigFromJar(latestConf, filename);
        int latestVersion = versionToInt(Objects.requireNonNull(latestConf.getString("version")));
        if (latestVersion > currentVersion)
            saveFromJarToDisk(filename);
    }

    public boolean makeDirectory(){
        if (!plugin.getDataFolder().isDirectory() ) {
            if (!plugin.getDataFolder().mkdirs()) {
                plugin.getLogger().log(Level.WARNING, "Can't create plugin directory at "
                        + plugin.getDataFolder().getAbsolutePath() + " !!!");
                return false;
            }
        }
        return true;
    }

    public void loadConfigFromJar(YamlConfiguration target, String filename) throws IOException, InvalidConfigurationException {
        InputStream file = Main.class.getClassLoader().getResourceAsStream(filename);
        assert file != null;
        target.load(new InputStreamReader(file));
    }

    public void loadConfigFromDisk(YamlConfiguration target, String filepath) throws IOException, InvalidConfigurationException {
        FileInputStream file = new FileInputStream(filepath);
        try {
            target.load(new InputStreamReader(file));
        }catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Broken file"+ filepath + " !! Repairing...");
            target.loadFromString("");
        }
    }

    public void saveFromJarToDisk(String filename) throws IOException {
        plugin.getLogger().log(Level.INFO, "Saving new "+ filename + " to disk");
        InputStream loadedFile = Main.class.getClassLoader().getResourceAsStream(filename);
        assert loadedFile != null;

        File file = new File(plugin.getDataFolder(), filename);
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buff = new byte[100];
        int num = loadedFile.read(buff);
        while (num  > 0)
        {
            outputStream.write(buff, 0, num);
            num = loadedFile.read(buff);
        }
        outputStream.flush();
        outputStream.close();
    }

    private static int versionToInt(String version) {
        if (version == null)
            return -1;
        String[] num = version.split("\\.");
        if (num.length < 3)
            return -1;

        return Integer.parseInt(num[0])*10000000 +
                Integer.parseInt(num[1])*1000 +
                Integer.parseInt(num[2]);
    }

    public void unpack() throws IOException {
        if (!makeDirectory())
            return;

        for (String filename: ConfigFilesIO.LANG_CONFS) {
            File file = new File(plugin.getDataFolder().getAbsolutePath(), filename);
            if (file.exists()) {
                try {
                    updateFile(file, filename);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                    plugin.getLogger().log(Level.WARNING, "Unable to update file "
                            + file.getAbsolutePath() + " !!!");
                }
            }
            else if (!file.exists()) {
                saveFromJarToDisk(filename);
            }
        }
        File configFile = new File(plugin.getDataFolder(), ConfigFilesIO.CONF_FILE_NAME);
        if (!configFile.exists()) {
            saveFromJarToDisk(ConfigFilesIO.CONF_FILE_NAME);
        }
        File langFile = new File(plugin.getDataFolder(), ConfigFilesIO.LANG_FILE_NAME);
        if (!langFile.exists()) {
            saveFromJarToDisk(ConfigFilesIO.LANG_FILE_NAME);
        }

    }

}
