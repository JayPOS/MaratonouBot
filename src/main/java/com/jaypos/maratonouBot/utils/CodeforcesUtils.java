package com.jaypos.maratonouBot.utils;

import ru.covariance.codeforcesapi.CodeforcesApiException;
import ru.covariance.codeforcesapi.entities.ContestStandings;
import ru.covariance.codeforcesapi.CodeforcesApi;
import ru.covariance.codeforcesapi.entities.RanklistRow;

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

}
