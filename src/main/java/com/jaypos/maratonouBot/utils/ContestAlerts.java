package com.jaypos.maratonouBot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import ru.covariance.codeforcesapi.entities.Contest;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static com.jaypos.maratonouBot.listener.slash.CodeforcesCommandsListener.randomGenerator;

public class ContestAlerts {
    public static final int interval = 1;

    private static ArrayList<Contest> contestBuffer = new ArrayList<Contest>();

    public ContestAlerts() {
    }

    private static boolean removeFromBuffer(Contest contest) {
        int id = contest.getId();
        int removedIndex = -1;
        for (int i = 0; i < contestBuffer.size(); i++) {
            if (contestBuffer.get(i).getId() == id) {
                removedIndex = i;
                break;
            }
        }
        if (removedIndex != -1) {
            contestBuffer.remove(removedIndex);
            return true;
        }
        return false;
    }

    private static boolean loadToBuffer(Contest contest) {
        return contestBuffer.add(contest);
    }

    public static boolean nextDayAlert(Contest contest) {
        int secondsInHour = 60*60;
        int secondsInDay = secondsInHour*24;
        if (contest.getPhase().equals("BEFORE")) {
            int secondsRemainingTillStart = Math.abs(contest.getRelativeTimeSeconds());
            if(secondsRemainingTillStart <= secondsInDay*3) return loadToBuffer(contest);
        }
        return false;
    }

    public static boolean nextHourAlert(Contest contest) {
        int secondsInHour = 60*60;
        if (contest.getPhase().equals("BEFORE")) {
            int secondsRemainingTillStart = Math.abs(contest.getRelativeTimeSeconds());
            if(secondsRemainingTillStart <= secondsInHour*2) return removeFromBuffer(contest);
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

    public static boolean triggerNextDayAlert (Guild guild, Contest contest, TextChannel msg_channel) {
        EmbedBuilder nextDayEb = ContestAlerts.nextContestTomorrowEmbed(contest);
        if (nextDayEb != null) {
            ContestAlerts.mentionMaratonistas(guild, msg_channel);
            msg_channel.sendMessageEmbeds(nextDayEb.build()).queue();
        }
        return nextDayEb != null;
    }

    public static boolean triggerNextHourAlert (Guild guild, Contest contest, TextChannel msg_channel) {
        EmbedBuilder nextHourEb = ContestAlerts.nextContestNextHourEmbed(contest);
        if (nextHourEb != null) {
            ContestAlerts.mentionMaratonistas(guild, msg_channel);
            msg_channel.sendMessageEmbeds(nextHourEb.build()).queue();
        }
        return nextHourEb != null;
    }

    public static void mentionMaratonistas(Guild guild, TextChannel channel) {
        channel.sendMessage(Objects.requireNonNull(guild.getRoleById("999342194936774706")).getAsMention()).queue();
    }
}
