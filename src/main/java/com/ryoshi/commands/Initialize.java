package com.ryoshi.commands;

import com.ryoshi.CommandManager;
import com.ryoshi.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Initialize implements ServerCommand {

    @Override
    public void performCommand(Member m, TextChannel channel, Message message, String extraString) {

        channel.sendMessage("The Bot has been initialized!").queue();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(performPrCommand(m, channel, message), 0, 1,  TimeUnit.DAYS);
    }

    private Runnable performPrCommand(Member m, TextChannel channel, Message message) {
        return () -> {
            CommandManager cmdMgr = new CommandManager();

            cmdMgr.perform("pr", m, channel, message);
        };
    }
}
