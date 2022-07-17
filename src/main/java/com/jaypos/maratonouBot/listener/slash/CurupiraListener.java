package com.jaypos.maratonouBot.listener.slash;

import com.softawii.curupira.annotations.IArgument;
import com.softawii.curupira.annotations.ICommand;
import com.softawii.curupira.annotations.IGroup;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@IGroup(name = "pingCommand", description = "has a command that says pong", hidden = true)
public class CurupiraListener {

    @ICommand(name = "ping", description = "pong")
    @IArgument(name = "user", description = "name of user", required = true, type = OptionType.USER)
    public static void pingCommandListener(SlashCommandInteractionEvent event) {
        User user;
        user = event.getOption("user").getAsUser();
        event.reply("pong " + user.getAsMention() + "!").queue();
    }
}
