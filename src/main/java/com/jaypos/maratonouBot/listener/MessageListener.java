package com.jaypos.maratonouBot.listener;

import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    private static final String COMMAND_PREFIX = "!";
    public MessageListener() {

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) {
            System.out.println("Maldito BOT!");
            return;
        }

        final Message message = event.getMessage();
        final ChannelType channelType = event.getChannelType();
        final MessageChannel messageChannel = event.getChannel();
        final String content = message.getContentRaw();

        if (channelType == ChannelType.TEXT) {
            if(content.equals(COMMAND_PREFIX + "ping")) {
//                long time = System.currentTimeMillis();
                messageChannel.sendMessage("pong!").queue();
            }
        }
    }
}
