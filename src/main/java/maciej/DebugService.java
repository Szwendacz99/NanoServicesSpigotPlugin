package maciej;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class DebugService extends AbstractService implements CommandExecutor {

    private final ArrayList<AbstractService> loadedServices;

    public DebugService(Plugin plugin, ArrayList<AbstractService> loadedServices) {
        this.loadedServices = loadedServices;
        this.plugin = plugin;
        this.serviceName = "Debug";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equals("nanoservicesdebug"))
        {
            if (sender instanceof Player)
                sendDebug((Player) sender);
            else
                sendDebug();
        }
        else if (label.equals("nanoservicessettings")) {
            if (sender instanceof Player)
                showConfig((Player) sender);
            else
                showConfig();
        }

        return true;
    }

    private void sendDebug(Player player) {
        for (AbstractService service: loadedServices){
            String[] debug = service.debugInfo();
            if (debug !=null) {
                player.sendMessage("§6Service: §a§b" + service.serviceName);
                player.sendMessage(debug);
            }
        }
    }
    private void sendDebug() {
        for (AbstractService service: loadedServices){
            String[] debug = service.debugInfo();
            if (debug !=null) {
                plugin.getServer().getConsoleSender().sendMessage("Service: " + service.serviceName + ":");
                plugin.getServer().getConsoleSender().sendMessage(debug);
            }

        }
    }

    private void showConfig(Player player) {
        player.sendMessage(Config.settingsToString());
    }

    private void showConfig() {
        plugin.getServer().getConsoleSender().sendMessage(Config.settingsToString());
    }

}
