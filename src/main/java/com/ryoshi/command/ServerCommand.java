package com.ryoshi.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public interface ServerCommand {
    public void performCommand(Member m, TextChannel channel, Message message, String extraString);

}
