package com.jaypos.maratonouBot.utils;

import ru.covariance.codeforcesapi.entities.Contest;

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
}
