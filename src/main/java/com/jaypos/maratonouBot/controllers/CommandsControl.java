package com.jaypos.maratonouBot.controllers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class CommandsControl {

    private JDA jda;

    public CommandsControl(JDA jda){
        this.jda = jda;
    }

    private void addOiCommand() {
        jda.upsertCommand("oi", "Boti diz oi!")
                .addOptions(new OptionData(USER, "user", "name of the user you want to greet")
                ).queue();
    }

    private void addTalkCommand(){
        jda.upsertCommand("secret", "Boti faz algo secreto!"
        ).addOptions(new OptionData(STRING, "shh", "shh")
                .setRequired(true)
        ).queue();
    }

    private void addCodeforcesRatingCommand() {
        jda.upsertCommand("rating", "Boti resgata rating do handle inserido!")
                .addOptions(new OptionData(STRING, "handle", "handle do usuário a ser pesquisado!")
                        .setRequired(true)
                ).queue();
    }
    private void addCodeforcesNextContestsCommand() {
        jda.upsertCommand("nextcontests", "Boti mostra próximos 5 contests")
                .addOptions(new OptionData(BOOLEAN, "gym",
                        "true para pesquisar gyms e false para não pesquisar, se não preenchido por padrão é false"))
                .queue();
    }

    private void addCodeforcesCommands() {
        addCodeforcesRatingCommand();
        addCodeforcesNextContestsCommand();
    }

    public void addSlashCommands() {
        addOiCommand();
        addCodeforcesCommands();
        addTalkCommand();
    }
}
