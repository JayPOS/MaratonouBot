package com.jaypos.maratonouBot.listener.slash;

import com.jaypos.maratonouBot.utils.CodeforcesUtils;
import com.jaypos.maratonouBot.utils.Util;
import com.softawii.curupira.annotations.IArgument;
import com.softawii.curupira.annotations.ICommand;
import com.softawii.curupira.annotations.IGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.covariance.codeforcesapi.CodeforcesApiException;

import java.awt.*;
import java.util.List;
import java.util.random.RandomGenerator;

@IGroup(name = "codeforcesCommand", description = "Lida com comandos relacionados ao Codeforces", hidden = true)
public class CodeforcesCommandsListener {
    public static RandomGenerator randomGenerator = RandomGenerator.getDefault();
    public CodeforcesCommandsListener() {
    }

    // a single user! add exception or log here
    @ICommand(name = "rating", description = "Boti responde o rating do handle inserido")
    @IArgument(name = "handle", description = "Handle do Codeforces")
    public static void userRatingCommand(SlashCommandInteractionEvent event){
        String handle;
        if (event.getOption("handle") != null)
        {
            handle = event.getOption("handle").getAsString();
            String rating = null;
            try {
                rating = Util.cfUtils.getUserRating(handle);
            } catch (CodeforcesApiException e) {
                e.printStackTrace();
                event.reply("Handle **" + handle + "** not found").queue();

            }
            event.reply("Rating of **" + handle + "** is now **" + rating + "**").queue();
        }
    }
    public static void nextContestsCommand(SlashCommandInteractionEvent event, boolean embed) {
        try {
            event.deferReply().queue();
            Boolean gym = Boolean.FALSE;
            if (event.getOption("gym") != null) {
                gym = event.getOption("gym").getAsBoolean();
            }
            List<String> nextContestsMessages = Util.cfUtils.getNextContextList(gym);
            String response = "";
            for (int i = 0; i < nextContestsMessages.size(); i++) {
                response += nextContestsMessages.get(i);
                if (i < nextContestsMessages.size()-1) response += "\n";
            }
            if(!embed) event.getHook().sendMessage(response).queue();
            else {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTimestamp(event.getTimeCreated());
                eb.setColor(new Color( (int) (randomGenerator.nextDouble() * 0x1000000)));
                eb.setTitle("Proximos Contests");
                eb.setDescription(response);
                event.getHook().sendMessageEmbeds(eb.build()).queue();
            }
        } catch (CodeforcesApiException e) {
            e.printStackTrace();
            event.getHook().sendMessage("No next Contests available").queue();
        }
    }

    public static void onCodeforcesCommand(SlashCommandInteractionEvent event) {
        if (event.getName().equals("nextcontests")) {
            nextContestsCommand(event, true);
        }
    }
}
