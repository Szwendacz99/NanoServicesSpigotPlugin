package maciej;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class ShellCommandService extends AbstractService implements CommandExecutor {

    private static final long PERIOD = 100;


    private boolean isCommandProcessOn = false;
    private Process commandProcess = null;
    private CommandSender player;
    private BufferedReader output;
    private BufferedReader errOutput;
    private Timer timer;

    public ShellCommandService(Plugin plugin){
        this.plugin = plugin;
        this.serviceName = "Shell Command";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandString, String[] strings) {
        if(commandString.equals("shellcommand") && strings.length > 0)
        {
            if(commandProcess!=null)
            {
                timer.cancel();
                timer = null;
                commandProcess.destroyForcibly();
            }

            try {
                player = commandSender;
                commandProcess = new ProcessBuilder(strings).start();
                output = new BufferedReader(new InputStreamReader(commandProcess.getInputStream()));
                errOutput = new BufferedReader(new InputStreamReader(commandProcess.getErrorStream()));
                isCommandProcessOn=true;
                startTimer();
            } catch (Exception e) {
                player.sendMessage(ChatColor.DARK_RED+Config.lang.getShellCouldNotExecute());
                return false;
            }

            return true;
        }
        return false;
    }

    private void startTimer(){
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                timerTask();
            }
        }, 0, PERIOD);
    }

    private void timerTask(){
        if (isCommandProcessOn)
        {
            if(!commandProcess.isAlive())
            {
                isCommandProcessOn=false;
                player.sendMessage(ChatColor.DARK_GREEN+Config.lang.getShellFinishedWithResult(commandProcess.exitValue()));
                commandProcess=null;
            }

            try {
                String line;
                while ((line = output.readLine()) != null) {
                    plugin.getServer().broadcastMessage(line);
                }
                while ((line = errOutput.readLine()) != null) {
                    plugin.getServer().broadcastMessage(line);
                }
            } catch (Exception e1) {
                player.sendMessage(ChatColor.DARK_RED + Config.lang.getShellErrOnReadOutput());
            }
        }
        else timer.cancel();
    }
}
