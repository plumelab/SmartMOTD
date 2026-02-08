package fengzhiyu.top.motdPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.List;

public class MotdCommand implements CommandExecutor, TabCompleter {

    private final MotdService service;

    public MotdCommand(Object pluginIgnored, MotdService service) {
        this.service = service;
    }

    private boolean isAllowed(CommandSender sender) {
        return (sender instanceof ConsoleCommandSender) || sender.isOp();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!isAllowed(sender)) {
            sender.sendMessage(ChatColor.RED + "只有 OP 才能使用该指令。");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "用法：");
            sender.sendMessage(ChatColor.GRAY + "/" + label + " <内容...> <7d|12h|30m|90s>");
            sender.sendMessage(ChatColor.GRAY + "/" + label + " clear");
            sender.sendMessage(ChatColor.GRAY + "/" + label + " status");
            sender.sendMessage(ChatColor.GRAY + "/" + label + " reload");
            sender.sendMessage(ChatColor.DARK_GRAY + "提示：内容里可用 \\n 换行；可用 &a 颜色码；可用 {version}/{mspt}/{joinsToday} 占位符。");
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("clear")) {
            service.clearTempMotd();
            sender.sendMessage(ChatColor.GREEN + "已清除临时 MOTD ✅");
            return true;
        }

        if (sub.equals("status")) {
            TempMotd t = service.getTempMotd();
            if (t == null) {
                sender.sendMessage(ChatColor.YELLOW + "当前没有临时 MOTD。");
            } else {
                sender.sendMessage(ChatColor.GREEN + "临时 MOTD 仍有效，剩余：" +
                        ChatColor.AQUA + DurationParser.formatRemaining(t.remainingMillis()));
                sender.sendMessage(ChatColor.GRAY + "内容：" + ChatColor.WHITE + t.content().replace("\\n", "\\n"));
            }
            return true;
        }

        if (sub.equals("reload")) {
            service.reloadFromConfig();
            sender.sendMessage(ChatColor.GREEN + "已重载配置 ✅（临时 MOTD 不会被保存）");
            return true;
        }

        // 默认：/motd <内容...> <duration>
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "缺少时长参数，例如：/motd &e版本{version}\\n&aMSPT{mspt} 7d");
            return true;
        }

        String durationRaw = args[args.length - 1];
        long durationMs;
        try {
            durationMs = DurationParser.parseToMillis(durationRaw);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "时长格式不对！支持：90s / 30m / 12h / 7d");
            return true;
        }

        // 内容 = 除最后一个参数外全部拼接
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length - 1; i++) {
            if (i > 0) sb.append(' ');
            sb.append(args[i]);
        }
        String content = sb.toString();

        service.setTempMotd(content, durationMs);
        sender.sendMessage(ChatColor.GREEN + "已设置临时 MOTD ✅ 有效期：" +
                ChatColor.AQUA + DurationParser.formatRemaining(durationMs));
        sender.sendMessage(ChatColor.DARK_GRAY + "提示：客户端服务器列表刷新后即可看到变化。");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> out = new ArrayList<>();
        if (!isAllowed(sender)) return out;

        if (args.length == 1) {
            out.add("clear");
            out.add("status");
            out.add("reload");
            return out;
        }

        if (args.length >= 2) {
            out.add("30m");
            out.add("12h");
            out.add("7d");
            out.add("90s");
        }
        return out;
    }
}