package com.jaypos.maratonouBot.utils;

import ru.covariance.codeforcesapi.CodeforcesApiException;
import ru.covariance.codeforcesapi.entities.Contest;
import ru.covariance.codeforcesapi.entities.ContestStandings;
import ru.covariance.codeforcesapi.CodeforcesApi;
import ru.covariance.codeforcesapi.entities.RanklistRow;
import ru.covariance.codeforcesapi.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeforcesUtils {
    private CodeforcesApi api;
    public CodeforcesUtils(final String key, final String secret) {
        api = new CodeforcesApi(key, secret);
    }

    private ContestStandings getContestStandings(final int contestId, final Integer from,
                                      final Integer count, final List<String> handles, final Integer room,
                                      final Boolean showUnofficial) throws CodeforcesApiException {
        ContestStandings standings = api.contestStanding(contestId, from ,count, handles, room, showUnofficial);
        return standings;
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

    // A single User only!
    public String getUserRating(String handle) throws CodeforcesApiException {
        User user = api.userInfo(List.of(handle)).get(0);
        return String.valueOf(user.getRating());
    }

    private String parseContestMessage(Contest contest, int index) {
        int relativeTimeSeconds = Math.abs(contest.getRelativeTimeSeconds());
        int seconds = relativeTimeSeconds % 60;
        int minutes =  (relativeTimeSeconds / 60)%60;
        int hour = (relativeTimeSeconds/60/60) % 24;
        int days = (relativeTimeSeconds/60/60)/24;
        return (index + " - **" + contest.getName() + "** starts in **"
                + days + " day" + ( days != 1 ? "s, " : ", ")
                + hour + " hour" + ( hour != 1 ? "s, " : ", ")
                + minutes + " minute" + ( minutes != 1 ? "s and " : " and ")
                + seconds + " second" + ( seconds != 1 ? "s**" : "**"));
    }

    public List<String> getNextContextList(final Boolean gym) throws CodeforcesApiException {
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
