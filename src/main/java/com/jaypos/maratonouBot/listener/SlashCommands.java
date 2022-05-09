package com.jaypos.maratonouBot.listener;

import com.jaypos.maratonouBot.listener.slash.CodeforcesCommandsListener;
import com.jaypos.maratonouBot.listener.slash.TalkCommandListener;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommands extends ListenerAdapter {
    public SlashCommands() {
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        TalkCommandListener.onTalkCommand(event);
        CodeforcesCommandsListener.onCodeforcesCommand(event);
    }
}
