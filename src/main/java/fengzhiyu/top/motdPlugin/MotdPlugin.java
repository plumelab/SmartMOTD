package fengzhiyu.top.motdPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public class MotdPlugin extends JavaPlugin {

    private MotdService motdService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        JoinCounter joinCounter = new JoinCounter();
        this.motdService = new MotdService(this, joinCounter);

        getServer().getPluginManager().registerEvents(new MotdListener(motdService), this);
        getServer().getPluginManager().registerEvents(joinCounter, this);

        MotdCommand cmd = new MotdCommand(this, motdService);
        getCommand("motd").setExecutor(cmd);
        getCommand("motd").setTabCompleter(cmd);

        getLogger().info("DynamicMotd enabled.");
    }

    @Override
    public void onDisable() {
        // 不持久化：重启即清零，不需要保存任何东西
        getLogger().info("DynamicMotd disabled.");
    }
}