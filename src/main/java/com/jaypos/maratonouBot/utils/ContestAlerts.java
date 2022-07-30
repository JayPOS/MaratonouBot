package com.jaypos.maratonouBot.utils;

import com.jaypos.maratonouBot.controller.CodeforcesControler;
import com.jaypos.maratonouBot.entity.ContestWatcher;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.covariance.codeforcesapi.entities.Contest;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import static com.jaypos.maratonouBot.listener.slash.CodeforcesCommandsListener.randomGenerator;

public class ContestAlerts {
    public static final int interval = 1;
    private static ArrayList<Contest> contestDayAlertBuffer = new ArrayList<Contest>();
    private static ArrayList<Contest> contestHourAlertBuffer = new ArrayList<Contest>();
    private static CodeforcesControler cfController = CodeforcesControler.getInstance();

    private static final Logger LOGGER = LogManager.getLogger(ContestAlerts.class);
    public ContestAlerts() {
    }

    private static int searchInBuffer(ArrayList<Contest> contestAlertBuffer, Contest contest) {
        int id = contest.getId();
        for (int i = 0; i < contestAlertBuffer.size(); i++) {
            if (contestAlertBuffer.get(i).getId() == id) {
                LOGGER.info(String.format("Contest %s found in buffer!", contest.getName()));
                return i;
            }
        }
        return -1;
    }

    private static boolean removeFromBuffer(ArrayList<Contest> contestAlertBuffer, Contest contest) {
        int removedIndex = searchInBuffer(contestAlertBuffer,contest);
        if (removedIndex != -1) {
            contestAlertBuffer.remove(removedIndex);
            return true;
        }
        else {
            LOGGER.info("Contest could not be removed from contestAlertBuffer!");
        }
        return false;
    }

    private static boolean loadToBuffer(ArrayList<Contest> contestAlertBuffer,Contest contest) {
        return contestAlertBuffer.add(contest);
    }

    public static void updateAlertBuffers() {
        Iterator<Contest> it = contestDayAlertBuffer.iterator();
        while (it.hasNext()) {
            Contest contest = it.next();
            if (!contest.getPhase().equals("BEFORE")) {
                it.remove();
            }
        }
        it = contestHourAlertBuffer.iterator();
        while (it.hasNext()) {
            Contest contest = it.next();
            if (!contest.getPhase().equals("BEFORE")) {
                it.remove();
            }
        }
    }

    public static boolean nextDayAlert(Contest contest) {
        int secondsInHour = 60*60;
        int secondsInDay = secondsInHour*24;
        LOGGER.info(String.format("Contest phase is %s", contest.getPhase()));
        if (contest.getPhase().equals("BEFORE")) {
            int secondsRemainingTillStart = Math.abs(contest.getRelativeTimeSeconds());
            if(secondsRemainingTillStart <= secondsInDay
                    && searchInBuffer(contestDayAlertBuffer, contest) == -1) {
                LOGGER.info("Loading to day alert buffer");
                return loadToBuffer(contestDayAlertBuffer, contest);
            }
        }
        return false;
    }

    public static boolean nextHourAlert(Contest contest) {
        int secondsInHour = 60*60;
        LOGGER.info(String.format("Contest phase is %s", contest.getPhase()));
        if (contest.getPhase().equals("BEFORE")) {
            int secondsRemainingTillStart = Math.abs(contest.getRelativeTimeSeconds());
            if(secondsRemainingTillStart <= secondsInHour
                    && searchInBuffer(contestHourAlertBuffer, contest) == -1) {
                LOGGER.info("Loading to hour alert buffer");
                return loadToBuffer(contestHourAlertBuffer, contest);
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

    private static EmbedBuilder nextContestNextDayEmbed(Contest contest) {
        if (ContestAlerts.nextDayAlert(contest)) {
            EmbedBuilder eb = contestAlertEmbed(contest);
            eb.setDescription(cfController.parseContestMessage(contest));
            return eb;
        }
        return null;
    }

    private static EmbedBuilder nextContestNextHourEmbed(Contest contest) {
        if (ContestAlerts.nextHourAlert(contest)) {
            EmbedBuilder eb = contestAlertEmbed(contest);
            eb.setDescription(cfController.parseContestMessage(contest));
            return eb;
        }
        return null;
    }

    public static void triggerNextDayAlert (Guild guild, Contest contest, TextChannel msg_channel) throws NullPointerException {
        EmbedBuilder nextDayEb = ContestAlerts.nextContestNextDayEmbed(contest);
        if (nextDayEb != null) {
            MessageBuilder messageBuilder = new MessageBuilder(nextDayEb);
            Role maratonistas = guild.getRoleById("999342194936774706");
            if (maratonistas == null ) {
                LOGGER.error("Role Maratonistas does not exists");
                throw new NullPointerException();
            }
            messageBuilder.append((maratonistas.getAsMention()));
            msg_channel.sendMessage(messageBuilder.build()).queue();
            LOGGER.info("Day alert sent!");
        }
    }

    public static void triggerNextHourAlert (Guild guild, Contest contest, TextChannel msg_channel) throws NullPointerException {
        EmbedBuilder nextHourEb = ContestAlerts.nextContestNextHourEmbed(contest);
        if (nextHourEb != null) {
            MessageBuilder messageBuilder = new MessageBuilder(nextHourEb);
            Role maratonistas = guild.getRoleById("999342194936774706");
            if (maratonistas == null ) {
                LOGGER.error("Role Maratonistas does not exists");
                throw new NullPointerException();
            }
            messageBuilder.append((maratonistas.getAsMention()));
            msg_channel.sendMessage(messageBuilder.build()).queue();
            LOGGER.info("Hour alert sent!");
        }
    }
}
