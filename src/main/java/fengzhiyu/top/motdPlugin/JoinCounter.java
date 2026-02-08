package fengzhiyu.top.motdPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicInteger;

public class JoinCounter implements Listener {

    private static final ZoneId ZONE = ZoneId.of("Asia/Tokyo");

    private volatile LocalDate currentDay = LocalDate.now(ZONE);
    private final AtomicInteger joinsToday = new AtomicInteger(0);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ensureDayUpToDate();
        joinsToday.incrementAndGet();
    }

    public int getJoinsToday() {
        ensureDayUpToDate();
        return joinsToday.get();
    }

    private void ensureDayUpToDate() {
        LocalDate now = LocalDate.now(ZONE);
        if (!now.equals(currentDay)) {
            currentDay = now;
            joinsToday.set(0);
        }
    }
}