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


    public static File searchEnv(File file) {
        if(file.isDirectory())
        {
            File[] files = file.listFiles();
            if (files == null)
                return null;
            for (File f: files) {
//                System.out.println(f.toString());
                File found = searchEnv(f);
                if (found != null) {
                    return found;
                }
            }
        }
        else {
            if (file.toString().endsWith("public" + File.separator + "env.json")) {
                return file;
            }
        }
        return null;
    }

    public static File findEnv() {
        Path actualPath = new File(System.getProperty("user.dir")).toPath();
        int count = 0;
        while (null != actualPath && !actualPath.endsWith("MaratonouCodeforcesBot")){
            actualPath = actualPath.getParent();
        }
        if (actualPath == null){
            try {
                actualPath = new File(Util.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath().getParent();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            while (!actualPath.endsWith("MaratonouCodeforcesBot")){
                actualPath = actualPath.getParent();
            }

        }
        File envFile = searchEnv(actualPath.toFile());
        return envFile;
    }

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        File envFile = findEnv();
        if (envFile != null) {
            try {
                ENV = objectMapper.readValue(envFile, Env.class);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("caiu porra");
            }
        }
        else {
            throw new RuntimeException("Env file doesn`t exists");
        }
        cfUtils = new CodeforcesUtils(ENV.getKey(), ENV.getSecret());
    }

    public static String getInviteLink(JDA jda) {
        long clientId = jda.getSelfUser().getIdLong();
        int permission = Permission.MESSAGE_SEND.getOffset();
        return String.format("https://discord.com/api/oauth2/authorize?client_id=%d&permissions=%d", clientId, permission) + "&scope=bot%20applications.commands";
    }
}
