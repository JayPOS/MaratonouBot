package com.jaypos.maratonouBot.listener.watcher;

import com.jaypos.maratonouBot.entity.MaratonouBot;
import com.jaypos.maratonouBot.utils.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ru.covariance.codeforcesapi.CodeforcesApiException;
import ru.covariance.codeforcesapi.entities.Contest;

import java.awt.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.jaypos.maratonouBot.listener.slash.CodeforcesCommandsListener.randomGenerator;

public class ContestWatcher extends ListenerAdapter {
    public static JDA jda;
    public static ScheduledExecutorService contestWatcher;

    public ContestWatcher(){
    }

    private static String createNextContestTomorrowMessage(Contest nextContest) {
        int secondsInHour = 60*60;
        int secondsInDay = secondsInHour*24;
        if (Math.abs(nextContest.getRelativeTimeSeconds()) < secondsInDay
                && Math.abs(nextContest.getRelativeTimeSeconds()) > secondsInDay-secondsInHour) {
            return nextContest.getName()  + "starts in 1 day";
        }
        return null;
    }

    private static String createNextContestNextHourMessage(Contest nextContest) {
        int secondsInHour = 60*60;
        if (Math.abs(nextContest.getRelativeTimeSeconds()) < secondsInHour) {
            return nextContest.getName()  + "starts in 1 hour";
        }
        return null;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("ReadyEvent triggered!");
        jda = MaratonouBot.jda;
        contestWatcher = Executors.newScheduledThreadPool(1);
        contestWatcher.scheduleAtFixedRate(() -> {
            System.out.println("Disparando scheduler");
            List<Contest> nextContests;
            try {
                nextContests = Util.cfUtils.getNextContests();
            } catch (CodeforcesApiException e) {
                e.printStackTrace();
                System.out.println("There is no contest close to start");
                return;
            }
            if (jda != null) {
                System.out.println("Checking if Boti should send a Contest Alert!");
                for (Contest contest: nextContests) {
                    if (contest.getRelativeTimeSeconds() >= 0 ) {
                        System.out.println("There is no Next Contest");
                    }
                    else {
                        for (Guild guild : jda.getGuilds() ) {
                            for (Category category : guild.getCategories()) {
                                if (category.getName().endsWith("Contests")) {
                                    for (TextChannel msg_channel: category.getTextChannels()) {
                                        if (msg_channel.getName().endsWith("avisos")) {
                                            String tomorrowContestMessage = createNextContestTomorrowMessage(contest);
                                            if (tomorrowContestMessage != null) {
                                                EmbedBuilder eb = new EmbedBuilder();
                                                eb.setColor(new Color( (int) (randomGenerator.nextDouble() * 0x1000000)));
                                                eb.setTitle("Alerta de Contest!!!");
                                                eb.setDescription(tomorrowContestMessage);
                                                msg_channel.sendMessageEmbeds(eb.build()).queue();
                                            }

                                            String nextHourContest = createNextContestNextHourMessage(contest);
                                            if (nextHourContest != null) {
                                                EmbedBuilder eb = new EmbedBuilder();
                                                eb.setColor(new Color( (int) (randomGenerator.nextDouble() * 0x1000000)));
                                                eb.setTitle("Alerta de Contest!!!");
                                                eb.setDescription(nextHourContest);
                                                msg_channel.sendMessageEmbeds(eb.build()).queue();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, 0, 30, TimeUnit.SECONDS);
    }
}
