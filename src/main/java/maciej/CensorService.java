package maciej;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;

public class CensorService extends AbstractService implements Listener {

    public final HashMap<String, String> forbiddenWords = new LinkedHashMap<>();

    private PrintWriter logWriter = null;

    private int incidentsCounter = 0;

    public CensorService(Plugin plugin) {
        this.plugin = plugin;
        this.serviceName = "Censor";
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        String originalMsg = e.getMessage();
        String msg = censorText(originalMsg);
        if (!msg.equals(originalMsg)) {
            incidentsCounter++;
            log(e.getPlayer().getName() + ": " + originalMsg);
        }
        e.setMessage(msg);
    }

    private String censorText(String text) {
        for (String pattern:forbiddenWords.keySet()) {
            text = replaceStrings(text, pattern, forbiddenWords.get(pattern));
        }
        return text;
    }

    private void loadForbiddenWords() {
        ArrayList<LinkedHashMap<String, String>> words = Config.getForbiddenWords();
        if (words != null)
        for (LinkedHashMap<String, String> map:words) {
            forbiddenWords.put(createRegex((String) map.keySet().toArray()[0]),(String) map.values().toArray()[0]);
        }
        else
            plugin.getLogger().log(Level.WARNING, "Unable to load forbidden words in Censor service");
    }

    private String createRegex(String text) {
        String[] tab = text.split("");

        StringBuilder result = new StringBuilder();

        for (String character:tab) {
            result.append("[");
            result.append(character.toUpperCase());
            result.append(character.toLowerCase());
            result.append("]{1,2}\\W{0,1}");
        }
        return result.substring(0,result.toString().length()-7);
    }

    private String replaceStrings(String msg,String badString,String goodString)
    {
        msg = msg.replaceAll(badString, goodString);
        return msg;
    }

    private void openLogFile() throws IOException {
        makeDirectory();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String filename = "Censor_log_" + formatter.format(date) + ".txt";
        FileWriter writer = new FileWriter(plugin.getDataFolder().getPath()+
                File.separator  +"censor_logs" + File.separator + filename, true);
        logWriter = new PrintWriter(writer);

    }

    public void makeDirectory(){
        File directory = new File(plugin.getDataFolder() + File.separator + "censor_logs");
        if (!directory.isDirectory() ) {
            if (!directory.mkdirs()) {
                plugin.getLogger().log(Level.WARNING, "Can't create Censor log files directory at "
                        + directory.getAbsolutePath() + " !!!");
            }
        }
    }

    private void closeLogFile() {
        logWriter.flush();
        logWriter.close();
    }

    private void log(String msg) {
        Date date = new Date();
        logWriter.println("["+date.toString()+"]"+msg);
        logWriter.flush();
    }

    public String[] debugInfo() {
        String[] data = new String[1];
        data[0] = "Incidents since last server startup: §3" + incidentsCounter;
        return data;
    }

    public void registerEvents()
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void enable() {
        registerEvents();
        loadForbiddenWords();
        try {
            openLogFile();
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.WARNING, serviceName +" was unable to open log file!!");
        }
        plugin.getLogger().log(Level.INFO, serviceName +" service enabled");
    }

    public void disable() {
        closeLogFile();
    }

}
