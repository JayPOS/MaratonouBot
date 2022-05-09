package com.jaypos.maratonouBot.listener.slash;

import com.jaypos.maratonouBot.utils.CodeforcesUtils;
import com.jaypos.maratonouBot.utils.Util;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.covariance.codeforcesapi.CodeforcesApiException;

import java.util.List;

public class CodeforcesCommandsListener {
    public CodeforcesCommandsListener() {
    }

    // a single user! add exception or log here
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
    public static void nextContestsCommand(SlashCommandInteractionEvent event) {
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
            event.getHook().sendMessage(response).queue();
        } catch (CodeforcesApiException e) {
            e.printStackTrace();
            event.getHook().sendMessage("No next Contests available").queue();
        }
    }

    public static void onCodeforcesCommand(SlashCommandInteractionEvent event) {
        if (event.getName().equals("rating")) {
            userRatingCommand(event);
        }
        else if (event.getName().equals("nextcontests")) {
            nextContestsCommand(event);
        }
    }
}
