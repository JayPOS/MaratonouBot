package com.jaypos.maratonouBot.listener.slash;

import com.softawii.curupira.annotations.IArgument;
import com.softawii.curupira.annotations.ICommand;
import com.softawii.curupira.annotations.IGroup;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

@IGroup(name = "talkCommands", description = "Oi command and secret command (shh)", hidden = true)
public class TalkCommandListener {

    public TalkCommandListener() {
    }

    @ICommand(name = "oi", description = "Diz oi para o usuário informado se houver, senão responde para o autor")
    @IArgument(name = "user",  description = "Nome do usuário para dizer oi", required = false)
    public static void oiCommand(SlashCommandInteractionEvent event){
        User user;
        if (event.getOption("user") != null){
            user = event.getOption("user").getAsUser();
            event.reply("Oi " + user.getAsMention() + "!").queue();
        }
        else{
            event.reply("Oi " + event.getUser().getAsMention() + "!").queue();
        }
    }

    @ICommand(name = "secret", description = "Boti faz algo secreto!")
    @IArgument(name = "shh", description = "???")
    public static void talkCommand(SlashCommandInteractionEvent event){
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
}
