package com.jaypos.maratonouBot.controller;

import com.jaypos.maratonouBot.utils.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.covariance.codeforcesapi.CodeforcesApi;
import ru.covariance.codeforcesapi.CodeforcesApiException;
import ru.covariance.codeforcesapi.entities.Contest;
import ru.covariance.codeforcesapi.entities.ContestStandings;
import ru.covariance.codeforcesapi.entities.RanklistRow;
import ru.covariance.codeforcesapi.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CodeforcesControler {

    private static final CodeforcesApi api = new CodeforcesApi(Util.ENV.getKey(), Util.ENV.getSecret());
    private static final CodeforcesControler cfController = new CodeforcesControler();
    private static ArrayList<Contest> contestsBuffer = null;

    public static final Logger LOGGER = LogManager.getLogger(CodeforcesControler.class);
    private CodeforcesControler() {
    }

    public static CodeforcesControler getInstance() {
        return cfController;
    }

    private static void updateContestBuffer(ArrayList<Contest> contestList) {
        if (contestList != null) {
            contestsBuffer = null;
            contestsBuffer = new ArrayList<Contest>(contestList);
        }
    }

    private static ArrayList<Contest> getContestBuffer() {
        return contestsBuffer;
    }

    public String getUserRating(String handle) throws CodeforcesApiException {
        User user = api.userInfo(List.of(handle)).get(0);
        return String.valueOf(user.getRating());
    }

    private ContestStandings getContestStandings(final int contestId, final Integer from,
                                                 final Integer count, final List<String> handles, final Integer room,
                                                 final Boolean showUnofficial) throws CodeforcesApiException {
        return api.contestStanding(contestId, from ,count, handles, room, showUnofficial);
    }

    public String getContestStandingsMessage(final int contestId, final Integer from,
                                             final Integer count, final List<String> handles, final Integer room,
                                             final Boolean showUnofficial) throws CodeforcesApiException {
        ContestStandings standings = getContestStandings(contestId, from ,count, handles, room, showUnofficial);
        List<RanklistRow> standingsRows = standings.getRows();
        standingsRows.forEach(ranklistRow -> {
            int rank = ranklistRow.getRank();
            String teamName = ranklistRow.getParty().getTeamName();
            System.out.println(String.valueOf(rank) + " - " + teamName);
        });
        return "";
    }


    public String parseContestMessage(Contest contest) {
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
    public List<String> getNextContestsListMessages(boolean gym) throws CodeforcesApiException {
        List<Contest> contestsList = getContestsStartingSoon(gym);
        List<String> contestListMessages = new ArrayList<String>();
        for (Contest c: contestsList) {
            if (c.getPhase().equals("BEFORE"))
            {
                contestListMessages.add(parseContestMessage(c));
            }
        }
        if (!contestListMessages.isEmpty()) Collections.reverse(contestListMessages);
        return contestListMessages;
    }

    public List<Contest> getContestsStartingSoon(boolean gym) throws CodeforcesApiException {
        List<Contest> contestsList = api.contestList(gym);
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
}
