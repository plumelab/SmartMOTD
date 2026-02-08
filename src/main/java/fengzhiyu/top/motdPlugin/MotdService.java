package fengzhiyu.top.motdPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class MotdService {

    private final JavaPlugin plugin;
    private final JoinCounter joinCounter;

    private final AtomicReference<TempMotd> tempMotdRef = new AtomicReference<>();

    // 配置缓存（避免每次 ping 读文件）
    private volatile String line1;
    private volatile String line2;
    private volatile int msptDecimals;

    // 版本缓存（运行中不会变）
    private final String mcVersion = Bukkit.getMinecraftVersion();

    public MotdService(JavaPlugin plugin, JoinCounter joinCounter) {
        this.plugin = plugin;
        this.joinCounter = joinCounter;
        reloadFromConfig();
    }

    public void reloadFromConfig() {
        plugin.reloadConfig();
        FileConfiguration cfg = plugin.getConfig();

        this.line1 = cfg.getString("motd.line1", "&e版本 &f{version}");
        this.line2 = cfg.getString("motd.line2", "&aMSPT &f{mspt}ms &7| &b本日登录 &f{joinsToday}");
        this.msptDecimals = Math.max(0, cfg.getInt("motd.mspt-decimals", 1));
    }

    public String buildMotd() {
        // 临时 MOTD 优先
        TempMotd temp = tempMotdRef.get();
        if (temp != null) {
            if (!temp.isExpired()) {
                return colorize(renderPlaceholders(temp.content()));
            } else {
                tempMotdRef.compareAndSet(temp, null);
            }
        }

        // 默认 MOTD
        String rendered1 = renderPlaceholders(line1);
        String rendered2 = renderPlaceholders(line2);
        return colorize(rendered1) + "\n" + colorize(rendered2);
    }

    public void setTempMotd(String contentRaw, long durationMillis) {
        long expireAt = Instant.now().toEpochMilli() + Math.max(0, durationMillis);
        tempMotdRef.set(new TempMotd(contentRaw, expireAt));
    }

    public void clearTempMotd() {
        tempMotdRef.set(null);
    }

    public TempMotd getTempMotd() {
        TempMotd t = tempMotdRef.get();
        if (t != null && t.isExpired()) {
            tempMotdRef.compareAndSet(t, null);
            return null;
        }
        return tempMotdRef.get();
    }

    private String renderPlaceholders(String template) {
        String msptText = formatMspt(getAverageTickTimeMs());
        int joins = joinCounter.getJoinsToday();

        return template
                .replace("{version}", mcVersion)
                .replace("{mspt}", msptText)
                .replace("{joinsToday}", Integer.toString(joins));
    }

    /**
     * Paper API：Bukkit.getAverageTickTime() / Server#getAverageTickTime()
     * 返回平均 tick time（毫秒）
     */
    private double getAverageTickTimeMs() {
        try {
            return Bukkit.getAverageTickTime();
        } catch (NoSuchMethodError ignored) {
            // 如果不是 Paper 或版本太老，可能没有这个方法
        } catch (Throwable ignored) {
        }
        return -1.0;
    }

    private String formatMspt(double mspt) {
        if (mspt < 0) return "N/A";
        return String.format(Locale.ROOT, "%." + msptDecimals + "f", mspt);
    }

    /**
     * 支持：
     *  - &a 颜色码
     *  - 输入的 \n 或 \\n 变成换行
     */
    private String colorize(String s) {
        String withNewline = s.replace("\\n", "\n");
        return ChatColor.translateAlternateColorCodes('&', withNewline);
    }
}