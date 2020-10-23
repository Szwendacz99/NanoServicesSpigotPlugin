package maciej;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.logging.Level;

public class VotekickService extends AbstractService implements CommandExecutor {

    private static final long PERIOD = 5000;

    private int votekickTime = 0;
    private boolean isVotekickOn = false;
    private String targetPlayerToKick = "";
    private final ArrayList<String> playersWhoVote = new ArrayList<>();
    private Timer timer;

    public VotekickService(Plugin plugin){
        this.plugin = plugin;
        this.serviceName = "Votekick";
    }
    public boolean onCommand(CommandSender commandSender, Command command, String commandString, String[] arg3) {

        try {
            if(commandSender instanceof Player)
            {
                if(commandString.equals("votekick") &&
                        arg3.length==1 &&
                        !isVotekickOn &&
                        plugin.getServer().getPlayer(arg3[0])!=null)
                {
                    startVotekick(commandSender, arg3);
                    return true;

                }else if(commandString.equals("yes") && isVotekickOn && !(playersWhoVote.contains(commandSender.getName())))
                {
                    voteYes(commandSender);
                    return true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void startVotekick(CommandSender arg0, String[] arg3){
        plugin.getServer().broadcastMessage(Config.lang.getVotekickStart((Player)arg0,
                Objects.requireNonNull(plugin.getServer().getPlayer(arg3[0]))));
        plugin.getServer().broadcastMessage(Config.lang.getToVotekickYes());
        targetPlayerToKick = arg3[0];
        playersWhoVote.add(arg0.getName());
        votekickTime=35000;
        isVotekickOn=true;
        startTimer();
    }

    private void voteYes(CommandSender arg0){
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        playersWhoVote.add(arg0.getName());
        System.out.println(playersWhoVote.size());
        if(players.size()/2<playersWhoVote.size())
        {
            finishVotekick(true);
        }else
            plugin.getServer().broadcastMessage(Config.lang.getHeWantToKickToo((Player)arg0));
    }

    private void startTimer(){
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                timerTask();
            }
        }, 0, PERIOD);
    }

    public void finishVotekick(boolean succes)
    {
        Player target = plugin.getServer().getPlayer(targetPlayerToKick);
        if (target !=null)
        {
            if(succes)
                target.kickPlayer(Config.lang.getInfoForKickedPlayer());
            else
                plugin.getServer().broadcastMessage(Config.lang.getFailedVotekick(target));
        }

        isVotekickOn=false;
        playersWhoVote.clear();
        votekickTime=0;
        targetPlayerToKick="";
        timer.cancel();
        timer = null;
    }

    private void timerTask(){
        if (isVotekickOn){
            Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
            if (votekickTime<=0)
            {
                finishVotekick(false);
            }else {
                plugin.getServer().broadcastMessage(Config.lang.getVotekickTimeLeft(votekickTime/1000));
                plugin.getServer().broadcastMessage(Config.lang.getMoreVotesNeeded((players.size()/2+1-playersWhoVote.size())));
                votekickTime -= PERIOD;
            }
        }
    }

}
