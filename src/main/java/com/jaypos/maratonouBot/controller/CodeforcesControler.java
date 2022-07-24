package com.jaypos.maratonouBot.controller;

import ru.covariance.codeforcesapi.entities.Contest;

import java.util.ArrayList;

public class CodeforcesControler {

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
}
