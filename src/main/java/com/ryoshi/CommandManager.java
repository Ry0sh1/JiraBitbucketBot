package com.ryoshi;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    public ConcurrentHashMap<String, ServerCommand> commands;

    public CommandManager(){
        this.commands = new ConcurrentHashMap<>();
    }

    public boolean perform(String command, Member m, TextChannel channel, Message message){
        ServerCommand cmd;
        if((cmd = this.commands.get(command.toLowerCase())) != null){
            cmd.performCommand(m, channel, message, null);
            return true;
        }
        return false;
    }

}
