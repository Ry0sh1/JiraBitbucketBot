package com.ryoshi.listener;

import com.ryoshi.JiraBot;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class CommandListener extends ListenerAdapter {

    public void onMessageReceived (MessageReceivedEvent event){
        String message = event.getMessage().getContentDisplay();

        if (event.isFromType(ChannelType.TEXT) && message.startsWith("!")) {
            String[] args = message.substring(1).split(" ");
            if (args.length > 0 && !JiraBot.INSTANCE.getCmdMan().perform(args[0], event.getMember(), event.getChannel().asTextChannel(), event.getMessage())){
                event.getChannel().sendMessage("Unbekannter command").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            }
        }
    }
}