package com.jaypos.maratonouBot.entity;

import com.jaypos.maratonouBot.controllers.GuildControl;
import com.jaypos.maratonouBot.listener.MessageListener;
import com.jaypos.maratonouBot.listener.SlashCommands;
import com.jaypos.maratonouBot.utils.Env;
import com.jaypos.maratonouBot.utils.Util;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MaratonouBot {
    public static JDA jda;
    public static GuildControl guildControl;
    public MaratonouBot() throws LoginException, URISyntaxException, InterruptedException {
        jda = JDABuilder.createDefault(Util.ENV.getToken()).build().awaitReady();
        jda.getPresence().setActivity(Activity.playing("VALORANT"));
        jda.addEventListener(new MessageListener());
        jda.addEventListener(new SlashCommands());
        guildControl = new GuildControl();
        guildControl.addGuildById(Util.ENV.getTestingGuildName(), Util.ENV.getTestingGuildId());
        guildControl.addSlashCommandToGuild("maratonou!", "oi", "Bot diz oi!");
        System.out.println(Util.getInviteLink(jda));
    }


}
