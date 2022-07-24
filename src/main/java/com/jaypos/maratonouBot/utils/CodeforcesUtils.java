package com.jaypos.maratonouBot.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.covariance.codeforcesapi.CodeforcesApiException;
import ru.covariance.codeforcesapi.entities.Contest;
import ru.covariance.codeforcesapi.entities.ContestStandings;
import ru.covariance.codeforcesapi.CodeforcesApi;
import ru.covariance.codeforcesapi.entities.RanklistRow;
import ru.covariance.codeforcesapi.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CodeforcesUtils {
    private CodeforcesApi api;
    public static final Logger LOGGER = LogManager.getLogger(CodeforcesUtils.class);
    public CodeforcesUtils(final String key, final String secret) {
        api = new CodeforcesApi(key, secret);
    }

    private String parseContestMessage(Contest contest, int index) {
        int relativeTimeSeconds = Math.abs(contest.getRelativeTimeSeconds());
        int seconds = relativeTimeSeconds % 60;
        int minutes =  (relativeTimeSeconds / 60)%60;
        int hour = (relativeTimeSeconds/60/60) % 24;
        int days = (relativeTimeSeconds/60/60)/24;
        return ("**" + contest.getName() + "** starts in **"
                + days + " day" + ( days != 1 ? "s, " : ", ")
                + hour + " hour" + ( hour != 1 ? "s, " : ", ")
                + minutes + " minute" + ( minutes != 1 ? "s and " : " and ")
                + seconds + " second" + ( seconds != 1 ? "s**" : "**"));
    }

    public List<Contest> getContestList(boolean gym) throws CodeforcesApiException {
        return api.contestList(gym);
    }

    public List<Contest> getContestsStartingSoon() throws CodeforcesApiException {
        List<Contest> contestsList = api.contestList(false);
        Collections.sort(contestsList, new Comparator<Contest>() {
            @Override
            public int compare(Contest lhs, Contest rhs) {
                return lhs.getRelativeTimeSeconds().compareTo(rhs.getRelativeTimeSeconds());
            }
        });
        List<Contest> nextContests = null;
        for (Contest contest: contestsList) {
            if(contest.getPhase().equals("BEFORE")) {
                LOGGER.info("Time remaining till contest " + contest.getName() + ": " + contest.getRelativeTimeSeconds());
                if (nextContests == null) nextContests = new ArrayList<Contest>();
                nextContests.add(contest);
            }
        }
        return nextContests;
    }

    public List<String> getNextContestsListMessages(final Boolean gym) throws CodeforcesApiException {
        List<Contest> contestsList = api.contestList(gym);
        List<String> contestListMessages = new ArrayList<String>();
        int contests_replyed = 5;
        for (Contest c: contestsList) {
            if (contests_replyed < 1) break;
            if (c.getPhase().equals("BEFORE"))
            {
                contestListMessages.add(parseContestMessage(c, contests_replyed));
                contests_replyed--;
            }
        }
        if (!contestListMessages.isEmpty()) Collections.reverse(contestListMessages);
        return contestListMessages;
    }

}
