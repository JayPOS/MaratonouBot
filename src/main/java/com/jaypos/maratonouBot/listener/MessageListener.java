package com.jaypos.maratonouBot.listener;

import com.jaypos.maratonouBot.entity.MaratonouBot;
import com.jaypos.maratonouBot.utils.Util;
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
        if (event.getAuthor().isBot()) {
            System.out.println("Maldito BOT!");
            return;
        }

        final Message message = event.getMessage();
        final ChannelType channelType = event.getChannelType();
        final MessageChannel messageChannel = event.getChannel();
        final String content = message.getContentRaw();

        if (channelType == ChannelType.TEXT) {
            if (content.equals(COMMAND_PREFIX + "ping")) {
//                long time = System.currentTimeMillis();
                messageChannel.sendMessage("pong!").queue();
            } else if (content.equals(COMMAND_PREFIX + "oi")) {
                messageChannel.sendMessage("Oi " + event.getAuthor().getAsMention() + "!").queue();
            } else if (content.equals(COMMAND_PREFIX + "invitelink")) {
                messageChannel.sendMessage("**Me chama pro seu discord!**\nToma meu link: " + Util.getInviteLink(MaratonouBot.jda)).queue();
            } else if (messageChannel.getName().equals("test")) {
                // tests here when finish testing, move to if above!
                if (content.equals(COMMAND_PREFIX + "channel")) {
                    messageChannel.sendMessage("**Sim! Vc tah no canal *test*"+  "**").queue();
                }
                else if (content.equals(COMMAND_PREFIX + "contest")) {
                    messageChannel.sendMessage("").queue();
                }
            }
        }
    }
}
