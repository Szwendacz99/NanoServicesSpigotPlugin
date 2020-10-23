package maciej;

import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public abstract class AbstractService {

    Plugin plugin;
    public String serviceName = "Unnamed";

    public void enable() {
        plugin.getLogger().log(Level.INFO, serviceName +" service enabled");
    }

    public void disable() {
        plugin.getLogger().log(Level.INFO, serviceName +" service disabled");
    }
    public String[] debugInfo() {
        return null;
    }

}
