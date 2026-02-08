# smartMotd

---

## 中文

一个轻量的动态 MOTD 插件：支持颜色码与占位符，并提供指令临时切换 MOTD（适合临时公告/验证/节日信息等）。

### 功能
- 两行 MOTD（`line1` / `line2`）
- 颜色码支持：`§` 与 `&`
- 占位符：
  - `{version}`：服务器版本
  - `{mspt}`：MSPT（可配置小数位）
  - `{joinsToday}`：今日登录人次
- 临时 MOTD：可设置持续时间，到期自动恢复
- OP 指令：状态查看、清除临时 MOTD、重载配置

### 安装
1. 将插件 jar 放入 `plugins/`
2. 重启服务器
3. 修改 `plugins/MotdPlugin/config.yml` 后执行：`/motd reload`

### 配置
- `motd.line1` / `motd.line2`：两行 MOTD 文本（支持颜色码与占位符）
- `motd.mspt-decimals`：MSPT 显示保留小数位

### 指令（仅 OP）
- `/motd <content...> <7d|12h|30m|90s>`：设置临时 MOTD（内容支持空格）
- `/motd clear`：清除临时 MOTD，恢复默认配置
- `/motd status`：查看临时 MOTD 状态与剩余时间
- `/motd reload`：重载配置文件

### 兼容性
- `api-version: 1.21`
- 适用于 Paper/Spigot 兼容服务端（不同核心表现可能略有差异）

---

## English

A lightweight dynamic MOTD plugin with color codes, placeholders, and temporary MOTD switching via command.

### Features
- Two-line MOTD (`line1` / `line2`)
- Color codes: `§` and `&`
- Placeholders:
  - `{version}` server version
  - `{mspt}` MSPT (decimals configurable)
  - `{joinsToday}` joins today
- Temporary MOTD with duration (auto revert)
- OP-only commands: status / clear / reload

### Installation
1. Put the jar into `plugins/`
2. Restart the server
3. Edit `plugins/MotdPlugin/config.yml`, then run: `/motd reload`

### Config
- `motd.line1` / `motd.line2`: MOTD lines (color codes + placeholders)
- `motd.mspt-decimals`: MSPT decimal places

### Commands (OP only)
- `/motd <content...> <7d|12h|30m|90s>`
- `/motd clear`
- `/motd status`
- `/motd reload`

### Compatibility
- `api-version: 1.21`
- Works on Paper/Spigot compatible servers (behavior may vary)

---

## Placeholders
- `{version}` - server version
- `{mspt}` - milliseconds per tick
- `{joinsToday}` - joins today

---

## License
MIT
