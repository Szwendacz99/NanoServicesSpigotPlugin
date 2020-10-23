package maciej;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;

public class Main extends JavaPlugin{

	ArrayList<AbstractService> loadedServices = new ArrayList<>();

	
	public void onEnable()
	{
		Config.setup(this);
		Config.load();
		if (Config.getVotekickEnabled()) {
			VotekickService votekickModule = new VotekickService(this);
			loadedServices.add(votekickModule);
			Objects.requireNonNull(this.getCommand("votekick")).setExecutor(votekickModule);
		}

		if (Config.getBroadcastEnabled()) {
			EventBroadcastService eventBroadcastModule = new EventBroadcastService(this);
			loadedServices.add(eventBroadcastModule);
		}

		if (Config.getShellCommandEnabled()) {
			ShellCommandService shellCommandModule = new ShellCommandService(this);
			loadedServices.add(shellCommandModule);
			Objects.requireNonNull(this.getCommand("shellcommand")).setExecutor(shellCommandModule);
		}

		if (Config.getChatColorEnabled()) {
			ChatColorService chatColorService = new ChatColorService(this);
			loadedServices.add(chatColorService);
			Objects.requireNonNull(this.getCommand("chatcolor")).setExecutor(chatColorService);
		}

		if (Config.getCensorEnabled()) {
			CensorService censorService = new CensorService(this);
			loadedServices.add(censorService);
		}

		if (Config.getDebugEnabled()) {
			DebugService debugService = new DebugService(this, loadedServices);
			loadedServices.add(debugService);
			Objects.requireNonNull(this.getCommand("nanoservicesdebug")).setExecutor(debugService);
			Objects.requireNonNull(this.getCommand("nanoservicessettings")).setExecutor(debugService);
		}


		for (AbstractService service: loadedServices){
			service.enable();
		}

	
	}

	public void onDisable()
	{
		for (AbstractService service: loadedServices){
			service.disable();
		}
	}
	
	

}
