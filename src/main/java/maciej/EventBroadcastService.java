package maciej;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.logging.Level;

public class EventBroadcastService extends AbstractService implements Listener {

    public EventBroadcastService(Plugin plugin){
        this.plugin = plugin;
        this.serviceName = "Event Broadcast";
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e)
    {
        if(Config.getBcPlayerFoundDiamond() && e.getBlock().getType().equals(Material.DIAMOND_ORE))
            plugin.getServer().broadcastMessage(Config.lang.getplayerFoundDiamond(e.getPlayer()));
        else if(Config.getBcPlayerFoundEmerald() && e.getBlock().getType().equals(Material.EMERALD_ORE))
            plugin.getServer().broadcastMessage(Config.lang.getplayerFoundEmerald(e.getPlayer()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        try{
            Objects.requireNonNull(event.getClickedBlock()).getType();
        }catch (NullPointerException e) {return;}

        if(Config.getBcPlayerSetFireTNT() &&
                event.getClickedBlock().getType().equals(Material.TNT) &&
                event.getMaterial().equals(Material.FLINT_AND_STEEL) &&
                event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            plugin.getServer().broadcastMessage(Config.lang.getPlayerSetFireOnTNT(event.getPlayer(),
                    event.getClickedBlock().getLocation()));
        }
        if(Config.getBcPlayerPlacedTNT() &&
                event.getMaterial().equals(Material.TNT) &&
                event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            plugin.getServer().broadcastMessage(Config.lang.getPlayerPlacedTNT(event.getPlayer(),
                    event.getClickedBlock().getLocation()));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (!Config.getBcPlayerJoined())
            return;
        Player player = event.getPlayer();
        plugin.getServer().broadcastMessage(Config.lang.getPlayerJoined(player));

        player.sendMessage(Config.lang.getChatColorOnJoinTip());
        player.sendMessage(Config.lang.getChatColorOnJoinTip());

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        if (!Config.getBcPlayerLeft())
            return;
        plugin.getServer().broadcastMessage(Config.lang.getPlayerLeft(event.getPlayer()));
    }


    @Override
    public void enable() {
        plugin.getLogger().log(Level.INFO, serviceName + " service enabled");
        registerEvents();
    }

    public void registerEvents()
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}
