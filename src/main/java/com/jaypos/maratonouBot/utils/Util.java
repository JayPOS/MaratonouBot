package com.jaypos.maratonouBot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaypos.maratonouBot.entity.MaratonouBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

//
public class Util {

    public static Env ENV;
    public static CodeforcesUtils cfUtils;

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        Path currentPath = null;
        try {
            currentPath = new File(MaratonouBot.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath().getParent().getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Path envPath = Paths.get(currentPath.toString(), File.separator, "public", File.separator, "env.json" );
        File envFile = envPath.toFile();
        if (envFile.exists()) {
            try {
                ENV = objectMapper.readValue(envFile, Env.class);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("caiu porra");
            }
        }
        else {
            throw new RuntimeException("There is no path in " + envPath);
        }

        cfUtils = new CodeforcesUtils(ENV.getKey(), ENV.getSecret());
    }

    public static String getInviteLink(JDA jda) {
        long clientId = jda.getSelfUser().getIdLong();
        int permission = Permission.MESSAGE_SEND.getOffset();
        return String.format("https://discord.com/api/oauth2/authorize?client_id=%d&permissions=%d", clientId, permission) + "&scope=bot%20applications.commands";
    }
}
