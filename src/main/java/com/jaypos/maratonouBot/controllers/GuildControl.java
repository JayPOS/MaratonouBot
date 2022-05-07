package com.jaypos.maratonouBot.controllers;

import com.jaypos.maratonouBot.entity.MaratonouBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.dv8tion.jda.api.interactions.commands.OptionType.USER;

public class GuildControl {

    private final Map<String, Guild> guilds = new HashMap<String, Guild>();

    public GuildControl() {
    }

    public void addGuildById(String guildName, String guildId) {
        guilds.put(guildName ,MaratonouBot.jda.getGuildById(guildId));
    }

    public void addSlashCommandToGuild(String guildName, String commandName, String commandDescription) {
        Guild guild = guilds.get(guildName);
        if (guild != null) {
            guild.upsertCommand(commandName, commandDescription)
                .addOptions(new OptionData(USER, "user", "name of the user you want to greet")
                ).queue();
        }
        else {
            System.out.println("Guild not added!");
        }
    }
}
