package com.jaypos.maratonouBot.listener.slash;

import com.jaypos.maratonouBot.controller.CodeforcesControler;
import com.softawii.curupira.annotations.IArgument;
import com.softawii.curupira.annotations.ICommand;
import com.softawii.curupira.annotations.IGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.covariance.codeforcesapi.CodeforcesApiException;

import java.awt.*;
import java.util.List;
import java.util.random.RandomGenerator;

@IGroup(name = "codeforcesCommand", description = "Lida com comandos relacionados ao Codeforces", hidden = true)
public class CodeforcesCommandsListener {
    public static RandomGenerator randomGenerator = RandomGenerator.getDefault();
    private static final CodeforcesControler cfController = CodeforcesControler.getInstance();
    public CodeforcesCommandsListener() {
    }

    // a single user! add exception or log here
    @ICommand(name = "rating", description = "Boti responde o rating do handle inserido")
    @IArgument(name = "handle", description = "handle no Codeforces")
    public static void userRatingCommand(SlashCommandInteractionEvent event){
        String handle;
        if (event.getOption("handle") != null)
        {
            handle = event.getOption("handle").getAsString();
            String rating = null;
            try {
                rating = cfController.getUserRating(handle);
            } catch (CodeforcesApiException e) {
                e.printStackTrace();
                event.reply("Handle **" + handle + "** not found").queue();

            }
            event.reply("Rating of **" + handle + "** is now **" + rating + "**").queue();
        }
    }

    @ICommand(name = "nextcontests", description = "Boti responde quais são os 5 próximos contests")
    @IArgument(name = "gym", description = "Se valor é true, mostra os gyms também")
    public static void nextContestsCommand(SlashCommandInteractionEvent event) {
        try {
            event.deferReply().queue();
            Boolean gym = Boolean.FALSE;
            if (event.getOption("gym") != null) {
                gym = event.getOption("gym").getAsBoolean();
            }
            List<String> nextContestsMessages = cfController.getNextContestsListMessages(gym);
            String response = "";
            for (int i = 0; i < nextContestsMessages.size(); i++) {
                response += nextContestsMessages.get(i);
                if (i < nextContestsMessages.size()-1) response += "\n";
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTimestamp(event.getTimeCreated());
            eb.setColor(new Color( (int) (randomGenerator.nextDouble() * 0x1000000)));
            eb.setTitle("Próximos Contests");
            eb.setDescription(response);
            event.getHook().sendMessageEmbeds(eb.build()).queue();
        } catch (CodeforcesApiException e) {
            e.printStackTrace();
            event.getHook().sendMessage("No next Contests available").queue();
        }
    }
}
