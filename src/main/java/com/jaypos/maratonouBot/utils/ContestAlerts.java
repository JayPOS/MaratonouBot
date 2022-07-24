package com.jaypos.maratonouBot.utils;

import com.jaypos.maratonouBot.entity.ContestWatcher;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.covariance.codeforcesapi.entities.Contest;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static com.jaypos.maratonouBot.listener.slash.CodeforcesCommandsListener.randomGenerator;

public class ContestAlerts {
    public static final int interval = 1;

    private static ArrayList<Contest> contestAlertBuffer = new ArrayList<Contest>();

    private static final Logger LOGGER = LogManager.getLogger(ContestAlerts.class);
    public ContestAlerts() {
    }

    private static int searchInBuffer(Contest contest) {
        int id = contest.getId();
        for (int i = 0; i < contestAlertBuffer.size(); i++) {
            if (contestAlertBuffer.get(i).getId() == id) {
                LOGGER.info(String.format("Contest %s found in buffer!", contest.getName()));
                return i;
            }
        }
        return -1;
    }

    private static boolean removeFromBuffer(Contest contest) {
        int removedIndex = searchInBuffer(contest);
        if (removedIndex != -1) {
            contestAlertBuffer.remove(removedIndex);
            return true;
        }
        else {
            LOGGER.info("Contest could not be removed from contestAlertBuffer!");
        }
        return false;
    }

    private static boolean loadToBuffer(Contest contest) {
        return contestAlertBuffer.add(contest);
    }

    public static boolean nextDayAlert(Contest contest) {
        int secondsInHour = 60*60;
        int secondsInDay = secondsInHour*24;
        LOGGER.info(String.format("Contest phase is %s", contest.getPhase()));
        if (contest.getPhase().equals("BEFORE")) {
            int secondsRemainingTillStart = Math.abs(contest.getRelativeTimeSeconds());
            if(secondsRemainingTillStart <= secondsInDay
                    && searchInBuffer(contest) == -1) {
                LOGGER.info("Loading to alert buffer");
                return loadToBuffer(contest);
            }
        }
        return false;
    }

    public static boolean nextHourAlert(Contest contest) {
        int secondsInHour = 60*60;
        LOGGER.info(String.format("Contest phase is %s", contest.getPhase()));
        if (contest.getPhase().equals("BEFORE")) {
            int secondsRemainingTillStart = Math.abs(contest.getRelativeTimeSeconds());
            if(secondsRemainingTillStart <= secondsInHour) {
                LOGGER.info("removing from alert buffer");
                return removeFromBuffer(contest);
            }
        }
        return false;
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

    public static void triggerNextDayAlert (Guild guild, Contest contest, TextChannel msg_channel) {
        EmbedBuilder nextDayEb = ContestAlerts.nextContestTomorrowEmbed(contest);
        if (nextDayEb != null) {
            ContestAlerts.mentionMaratonistas(guild, msg_channel);
            msg_channel.sendMessageEmbeds(nextDayEb.build()).queue();
            LOGGER.info("Day alert sent!");
        }
    }

    public static void triggerNextHourAlert (Guild guild, Contest contest, TextChannel msg_channel) {
        EmbedBuilder nextHourEb = ContestAlerts.nextContestNextHourEmbed(contest);
        if (nextHourEb != null) {
            ContestAlerts.mentionMaratonistas(guild, msg_channel);
            msg_channel.sendMessageEmbeds(nextHourEb.build()).queue();
            LOGGER.info("Day alert sent!");
        }
    }

    public static void mentionMaratonistas(Guild guild, TextChannel channel) {
        channel.sendMessage(Objects.requireNonNull(guild.getRoleById("999342194936774706")).getAsMention()).queue();
    }
}
