package fengzhiyu.top.motdPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdListener implements Listener {

    private final MotdService motdService;

    public MotdListener(MotdService motdService) {
        this.motdService = motdService;
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMotd(motdService.buildMotd());
    }
}