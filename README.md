# Nano Services
### Spigot Plugin

Votekick, simple censor with logging, chat coloring with SQLite DB for saving user settings and more.
You can specify in config file which services you want to be started, so don't worry about your RAM or features you don't want to use.

|Feature |Commands| Description|
|------|--------|--------|
|Votekick|/votekick \<player><br>/vote yes|Players can start a votekick against specific player and <br>vote to reach over 50% for successful votekick|
|Censor|none|In config file you can specify forbidden words and replacements so when someone use forbidden word on chat it will be replaced, and incident will be logged in text file with exact date and time.<br> Filtering words is done with use of regular expressions so it is effective also if someone try to hide usage of forbidden word, here is extreme example: in text ".Mm'i;nNe'cR\|aFtr." is hidden word "minecraft", and it will be found and replaced if you want.|
|Chat Color|/chatcolor \<color>|With this command player can set his default color of chat, where \<color> is formatting code available in minecraft, more on https://minecraft.gamepedia.com/Formatting_codes <br> This service also allows players to one-time format their message with formatting codes preceded by sign "&"|
|Event broadcast|none|Fully customizable service for broadcasting on global chat specific events like joining/leaving server, with possibility to customize messages in language file. If you miss some events that could be broadcasted like specific achievements of finding rare ore, put your wish on github issue page or write email to developer.|
|Shell command|/shellcommand \<command>|This service gives possibility to execute shell command of operating system that is running your minecraft server and get in return output of it.<br>WARNING: you can enable this only at your own risk. Server hosting providers might do not like this one so you should use it only if you host your server on yours machine. Author of plugin does not take responsibility for your usage of this plugin.|
|Debug|/nanoservicesdebug <br>/nanoservicessettings| Gives possibility to see some interesting information about loaded and working services, and also to see currently loaded settings|

|Command|Permission|
|-----|------|
|/chatcolor \<color>|nanoservices.chatcolor|
|/votekick \<player>|nanoservices.votekick|
|/vote yes|nanoservices.vote|
|/shellcommand \<command>|nanoservices.shellcommand|
|/nanoservicesdebug|nanoservices.debug|
|/nanoservicessettings|nanoservices.settings|

Author that is me, would like to hear what to change/add/correct, so feel free to post it on issue page or send mail with your idea. <br> Pull requests with translations are welcome too.

#### If you want to contribute:
Use maven for build:
```
mvn clean package
```
there are no unit tests, will add them maybe :]
