package com.ryoshi.command;

import com.ryoshi.CommandManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Initialize implements ServerCommand {

    /**
     * Diese Methode wird aufgerufen wenn man den !initialization command ausführt und sie sorgt
     * dafür, dass der loop zum täglichen aufrufen vom PR command gestartet wird.
     * @param m Member der die Nachricht geschrieben hat
     * @param channel Textchannel in dem der command versendet wurde und auch wo dann die PR notification kommt
     * @param message
     * @param extraString
     */

    @Override
    public void performCommand(Member m, TextChannel channel, Message message, String extraString) {

        channel.sendMessage("The Bot has been initialized!").queue();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(performPrCommand(m, channel, message), 0, 1,  TimeUnit.DAYS);
    }


    /**
     * Die Methode wird von der performCommand aufgerufen und übernimmt alle dere Parameter außer den extraString.
     * Diese Methode sorgt dafür, dass der pr command ausgeführt wird und dann ein return value an den scheduler
     * geschickt wird, damit dieser nicht aufhört zu schedulen.
     * @param m Member der den !initialization command ausgeführt hat
     * @param channel Textchannel wo die PR Nachricht letztendlich rein kommt
     * @param message
     * @return
     */

    private Runnable performPrCommand(Member m, TextChannel channel, Message message) {
        return () -> {
            CommandManager cmdMgr = new CommandManager();
            cmdMgr.perform("pr", m, channel, message);
        };
    }
}
