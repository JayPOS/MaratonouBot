package com.jaypos.maratonouBot.controller;

import com.jaypos.maratonouBot.utils.Util;
import ru.covariance.codeforcesapi.CodeforcesApi;
import ru.covariance.codeforcesapi.CodeforcesApiException;
import ru.covariance.codeforcesapi.entities.Contest;
import ru.covariance.codeforcesapi.entities.ContestStandings;
import ru.covariance.codeforcesapi.entities.RanklistRow;
import ru.covariance.codeforcesapi.entities.User;

import java.util.ArrayList;
import java.util.List;

public class CodeforcesControler {

    private CodeforcesApi api = new CodeforcesApi(Util.ENV.getKey(), Util.ENV.getSecret());
    private static final CodeforcesControler cfControler = new CodeforcesControler();
    private static ArrayList<Contest> contestsBuffer = null;

    private CodeforcesControler() {
    }

    public static CodeforcesControler getInstance() {
        return cfControler;
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
}
