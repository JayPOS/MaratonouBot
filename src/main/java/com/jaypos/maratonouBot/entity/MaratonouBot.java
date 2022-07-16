package com.jaypos.maratonouBot.entity;

import com.jaypos.maratonouBot.controllers.CommandsControl;
import com.jaypos.maratonouBot.listener.MessageListener;
import com.jaypos.maratonouBot.listener.SlashCommands;
import com.jaypos.maratonouBot.utils.Util;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.net.URISyntaxException;

public class MaratonouBot {
    public static JDA jda;
    public static CommandsControl commandsControl;
    public MaratonouBot() throws LoginException, URISyntaxException, InterruptedException {
        jda = JDABuilder.createDefault(Util.ENV.getToken()).build().awaitReady();
        jda.getPresence().setActivity(Activity.playing("VALORANT"));
        jda.addEventListener(new MessageListener());
        jda.addEventListener(new SlashCommands());
        commandsControl = new CommandsControl(jda);
        commandsControl.addSlashCommands();
        System.out.println(Util.getInviteLink(jda));
    }
}
