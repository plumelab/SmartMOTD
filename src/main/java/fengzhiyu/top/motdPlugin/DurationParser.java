package fengzhiyu.top.motdPlugin;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DurationParser {

    private static final Pattern P = Pattern.compile("^(\\d+)([smhd])$", Pattern.CASE_INSENSITIVE);

    private DurationParser() {}

    /**
     * 支持：90s / 30m / 12h / 7d
     * 返回毫秒
     */
    public static long parseToMillis(String input) throws IllegalArgumentException {
        String s = input.trim().toLowerCase(Locale.ROOT);
        Matcher m = P.matcher(s);
        if (!m.matches()) {
            throw new IllegalArgumentException("Invalid duration: " + input);
        }
        long num = Long.parseLong(m.group(1));
        String unit = m.group(2);

        return switch (unit) {
            case "s" -> num * 1000L;
            case "m" -> num * 60_000L;
            case "h" -> num * 3_600_000L;
            case "d" -> num * 86_400_000L;
            default -> throw new IllegalArgumentException("Invalid duration unit: " + unit);
        };
    }

    public static String formatRemaining(long ms) {
        long sec = ms / 1000L;
        long d = sec / 86400; sec %= 86400;
        long h = sec / 3600;  sec %= 3600;
        long m = sec / 60;    sec %= 60;

        StringBuilder sb = new StringBuilder();
        if (d > 0) sb.append(d).append("d ");
        if (h > 0) sb.append(h).append("h ");
        if (m > 0) sb.append(m).append("m ");
        sb.append(sec).append("s");
        return sb.toString().trim();
    }
}