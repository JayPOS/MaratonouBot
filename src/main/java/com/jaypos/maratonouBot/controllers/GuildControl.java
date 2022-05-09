package com.jaypos.maratonouBot.controllers;

import com.jaypos.maratonouBot.entity.MaratonouBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class GuildControl {

    private final Map<String, Guild> guilds = new HashMap<String, Guild>();

    public GuildControl() {
    }

    public void addGuildById(String guildName, String guildId) {
        guilds.put(guildName ,MaratonouBot.jda.getGuildById(guildId));
    }

    private void addOiCommandToGuild(String guildName) {
        Guild guild = guilds.get(guildName);
        if (guild != null) {
            guild.upsertCommand("oi", "Boti diz oi!")
                .addOptions(new OptionData(USER, "user", "name of the user you want to greet")
                ).queue();
        }
        else {
            System.out.println("Guild not added!");
        }
    }

    private void addTalkCommandToGuild(String guildName){
        Guild guild = guilds.get(guildName);
        if (guild != null) {
            guild.upsertCommand("secret", "Boti faz algo secreto!"
                    ).addOptions(new OptionData(STRING, "shh", "shh")
                    .setRequired(true)
            ).queue();
        }
    }

    private void addCodeforcesRatingCommand(String guildName) {
        Guild guild = guilds.get(guildName);
        if(guild != null){
            guild.upsertCommand("rating", "Boti resgata rating do handle inserido!")
                    .addOptions(new OptionData(STRING, "handle", "handle do usuário a ser pesquisado!")
                            .setRequired(true)
                    ).queue();
        }
    }
    private void addCodeforcesNextContestsCommand(String guildName) {
        Guild guild = guilds.get(guildName);
        if (guild != null){
            guild.upsertCommand("nextcontests", "Boti mostra próximos 5 contests")
                    .addOptions(new OptionData(BOOLEAN, "gym",
                            "true para pesquisar gyms e false para não pesquisar, se não preenchido por padrão é false"))
                    .queue();
        }
    }

    private void addCodeforcesCommands(String guildName) {
        addCodeforcesRatingCommand(guildName);
        addCodeforcesNextContestsCommand(guildName);
    }

    public void addSlashCommandsToGuild(String guildName) {
        addOiCommandToGuild(guildName);
        addCodeforcesCommands(guildName);
        addTalkCommandToGuild(guildName);
    }
}
