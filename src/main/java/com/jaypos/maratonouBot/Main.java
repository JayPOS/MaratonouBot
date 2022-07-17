package com.jaypos.maratonouBot;


import com.jaypos.maratonouBot.entity.MaratonouBot;
import com.jaypos.maratonouBot.utils.Util;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String args[]) throws LoginException, URISyntaxException, InterruptedException {
        MaratonouBot boti;
        boolean is_set = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--env")) {
                if (i + 1 < args.length) {
                    Util.init(args[i+1]);
                    is_set = true;
                }
            }
        }
        if (!is_set) {
            Util.init(Util.findEnv().getPath());
        }
        boti = new MaratonouBot();
    }
}
