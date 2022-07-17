package com.jaypos.maratonouBot.listener.slash;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class TalkCommandListener extends ListenerAdapter {

    public TalkCommandListener() {
    }

    private static void oiCommand(SlashCommandInteractionEvent event){
        User user;
        if (event.getOption("user") != null){
            user = event.getOption("user").getAsUser();
            event.reply("Oi " + user.getAsMention() + "!").queue();
        }
        else{
            event.reply("Oi " + event.getUser().getAsMention() + "!").queue();
        }
    }

    private static void talkCommand(SlashCommandInteractionEvent event){
        String secreto;
        if (event.getOption("shh") != null){
            secreto = event.getOption("shh").getAsString();
            if (event.getUser().getName().equals("JPOS")) {
                event.reply(secreto).queue();
            }
            else {
                event.reply("shh").queue();
            }
        }
        else{
            event.reply("shh").queue();
        }
    }

    public static void onTalkCommand(SlashCommandInteractionEvent event) {
        if (event.getName().equals("oi")) {
            oiCommand(event);
        }
        else if (event.getName().equals("secret")){
            talkCommand(event);
        }
    }
}
