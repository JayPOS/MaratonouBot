package com.jaypos.maratonouBot.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import ru.covariance.codeforcesapi.entities.Contest;

import java.util.Objects;

public class ContestAlerts {
    public static final int interval = 30;

    public ContestAlerts() {
    }
    public static boolean nextDayAlert(Contest contest) {
        int secondsInHour = 60*60;
        int secondsInDay = secondsInHour*24;
        if (contest.getPhase().equals("BEFORE")) {
            int secondsRemainingTillStart = Math.abs(contest.getRelativeTimeSeconds());
            return secondsRemainingTillStart <= secondsInDay
                    && secondsRemainingTillStart > secondsInHour * 23;
        }
        return false;
    }

    public static boolean nextHourAlert(Contest contest) {
        int secondsInHour = 60*60;
        if (contest.getPhase().equals("BEFORE")) {
            int secondsRemainingTillStart = Math.abs(contest.getRelativeTimeSeconds());
            return secondsRemainingTillStart <= secondsInHour;
        }
        return false;
    }

    public static void mentionMaratonistas(Guild guild, TextChannel channel) {
        channel.sendMessage(Objects.requireNonNull(guild.getRoleById("999342194936774706")).getAsMention()).queue();
    }
}
