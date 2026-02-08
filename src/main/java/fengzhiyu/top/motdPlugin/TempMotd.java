package fengzhiyu.top.motdPlugin;

public record TempMotd(String content, long expireAtMillis) {

    public boolean isExpired() {
        return System.currentTimeMillis() >= expireAtMillis;
    }

    public long remainingMillis() {
        return Math.max(0, expireAtMillis - System.currentTimeMillis());
    }
}