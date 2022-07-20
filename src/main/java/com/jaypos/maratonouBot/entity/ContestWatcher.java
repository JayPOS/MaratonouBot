package com.jaypos.maratonouBot.entity;

import com.jaypos.maratonouBot.utils.ContestAlerts;
import com.jaypos.maratonouBot.utils.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.covariance.codeforcesapi.CodeforcesApiException;
import ru.covariance.codeforcesapi.entities.Contest;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.jaypos.maratonouBot.listener.slash.CodeforcesCommandsListener.randomGenerator;

public class ContestWatcher extends ListenerAdapter {
    public static JDA jda;
    public static ScheduledExecutorService contestWatcher;
    private static final Logger LOGGER = LogManager.getLogger(ContestWatcher.class);

    public ContestWatcher(){
    }

    private static EmbedBuilder contestAlertEmbed(Contest contest) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color( (int) (randomGenerator.nextDouble() * 0x1000000)));
        eb.setTitle("Maratonou! Bot Announcement!");
        try {
            eb.setImage(new URL("https://i.imgur.com/VqRIMdG.png").toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return eb;
    }

    private static EmbedBuilder nextContestTomorrowEmbed(Contest contest) {
        if (ContestAlerts.nextDayAlert(contest)) {
            EmbedBuilder eb = contestAlertEmbed(contest);
            eb.setDescription(contest.getName() + " começa em 1 dia!");
            return eb;
        }
        return null;
    }

    private static EmbedBuilder nextContestNextHourEmbed(Contest contest) {
        if (ContestAlerts.nextHourAlert(contest)) {
            EmbedBuilder eb = contestAlertEmbed(contest);
            eb.setDescription(contest.getName() + " começa em 1 hora!");
            return eb;
        }
        return null;
    }

    private static void triggerAlert(Guild guild, Contest contest) {
        for (Category category : guild.getCategories()) {
            if (category.getName().endsWith("Contests")) {
                for (TextChannel msg_channel: category.getTextChannels()) {
                    if (msg_channel.getName().endsWith("avisos")) {
                        LOGGER.info("Found target channel at guild " + msg_channel.getGuild().getName());
                        EmbedBuilder nextDayEb = nextContestTomorrowEmbed(contest);
                        if (nextDayEb != null) {
                            ContestAlerts.mentionMaratonistas(guild, msg_channel);
                            msg_channel.sendMessageEmbeds(nextDayEb.build()).queue();
                        }
                        EmbedBuilder nextHourEb = nextContestNextHourEmbed(contest);
                        if (nextHourEb != null) {
                            ContestAlerts.mentionMaratonistas(guild, msg_channel);
                            msg_channel.sendMessageEmbeds(nextHourEb.build()).queue();
                        }
                    }
                }
            }
        }
    }

    private static void triggerMaratonouAlert(Contest contest) {
        Guild maratonou = jda.getGuildById(Util.ENV.getTestingGuildId());
        triggerAlert(maratonou, contest);
    }

    private static void triggerAllGuildsAlert(Contest contest) {
        for (Guild guild : jda.getGuilds() ) {
            LOGGER.info("Runnuing for guild " + guild.getName());
            triggerAlert(guild, contest);
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("ReadyEvent triggered!");
        contestWatcher = Executors.newScheduledThreadPool(1);
        contestWatcher.scheduleAtFixedRate(() -> {
            LOGGER.info("Triggering Contest Watcher scheduler");
            jda = MaratonouBot.jda;
            List<Contest> nextContests;
            try {
                nextContests = Util.cfUtils.getContestsStartingSoon();
            } catch (CodeforcesApiException e) {
                LOGGER.error("Bad request in Util.cfUtils.getNextContests() function - CodeforcesApiException");
                return;
            }
            if (jda != null) {
                LOGGER.info("jda is not null");
                if (nextContests == null) {
                    LOGGER.info("There is no next contests available!");
                }
                else {
                    for (Contest contest: nextContests) {
                        triggerMaratonouAlert(contest);
                    }
                }
            }
            else {
                LOGGER.error("jda is null at ContestWatcher");
            }
        }, 0, ContestAlerts.interval, TimeUnit.MINUTES);
    }
}
