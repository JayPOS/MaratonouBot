package com.jaypos.maratonouBot.entity;

import com.jaypos.maratonouBot.listener.MessageListener;
import com.jaypos.maratonouBot.utils.Util;
import com.softawii.curupira.core.Curupira;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.net.URISyntaxException;

public class MaratonouBot {
    public static JDA jda;
    private static Curupira curupira;
    public MaratonouBot() throws LoginException, URISyntaxException, InterruptedException {
        jda = JDABuilder.createDefault(Util.ENV.getToken()).build().awaitReady();
        curupira = new Curupira(jda, true, null, "com.jaypos.maratonouBot.listener");
        jda.getPresence().setActivity(Activity.playing("VALORANT"));
        jda.addEventListener(new MessageListener());
        System.out.println(Util.getInviteLink(jda));
    }
}
