# 🔨 BanPlus

**BanPlus** is a lightweight and easy-to-use ban management plugin for Minecraft servers. It provides a simple and reliable way to manage player bans, with support for permanent bans, temporary bans, automatic unbanning, customizable messages, and multilingual support.

Designed with simplicity in mind, BanPlus focuses on the essential features server administrators need while keeping configuration and daily usage straightforward.

## ✨ Features

* 🔨 **Permanent Bans** — Permanently prevent players from joining your server.
* ⏱️ **Temporary Bans** — Ban players for a specific amount of time.
* 🔓 **Automatic Unbanning** — Temporary bans are automatically removed when they expire.
* 📝 **Custom Ban Reasons** — Provide a reason when banning a player.
* 📅 **Expiration Information** — Players can see when their temporary ban will expire.
* 💾 **Persistent Ban Data** — Ban records are stored in `bans.yml` and remain available after server restarts.
* 🆔 **UUID-Based Storage** — Player ban information is stored using UUIDs for reliable identification.
* 🚪 **Instant Kick** — Players are immediately removed from the server when banned.
* 🛡️ **Join Protection** — Banned players cannot join the server while their ban is active.
* 💻 **Console Support** — Ban and unban commands can also be executed from the server console.
* 💡 **Command Suggestions** — Built-in tab completion makes commands easier to use.
* 📢 **Clear Feedback** — Helpful messages are displayed when commands are executed.
* 🚫 **Self-Ban Protection** — Players cannot use the system to ban themselves.
* 👑 **Operator Protection** — Unauthorized players cannot ban server operators.

## 🌍 Multilingual Support

BanPlus currently supports **Chinese and English**, with the language selected through the plugin configuration.

Available languages:

* 🇨🇳 **简体中文 (`zh_CN`)**
* 🇺🇸 **English (`en_US`)**

More languages can be added in future updates.

## ⚙️ Simple Configuration

BanPlus uses a simple configuration system that makes it easy to customize the plugin for your server.

The main configuration file is:

```text
plugins/BanPlus/config.yml
```

Ban information is stored in:

```text
plugins/BanPlus/bans.yml
```

This keeps configuration and ban data separate and easy to manage.

## 🎮 Commands

BanPlus provides straightforward commands for server administration:

```text
/ban <player> [duration] [reason]
/unban <player>
/betterban help
```

Command suggestions and built-in feedback make the commands easier to use, even for administrators who are new to the plugin.

## 🚀 Lightweight & Reliable

BanPlus is built around a simple idea:

> **Ban players easily. Manage punishments reliably.**

It focuses on the core ban experience instead of adding unnecessary features or complicated configuration.

Whether you run a small private server, a community server, or a larger public Minecraft server, BanPlus provides a clean and dependable way to handle player bans.

## ❤️ Why BanPlus?

* ⚡ Lightweight
* 🛠️ Easy to configure
* 🔨 Permanent & temporary bans
* ⏱️ Automatic expiration
* 💾 Persistent ban storage
* 🌍 Chinese & English support
* 💻 Console support
* 🎯 Simple and straightforward

**BanPlus — Simple bans, better server management.**
