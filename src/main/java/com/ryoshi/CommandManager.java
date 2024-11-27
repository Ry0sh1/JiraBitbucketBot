package com.ryoshi;

import com.ryoshi.commands.Initialize;
import com.ryoshi.commands.PullRequests;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    public ConcurrentHashMap<String, ServerCommand> commands;

    public CommandManager(){
        this.commands = new ConcurrentHashMap<>();

        this.commands.put("pr", new PullRequests());
        this.commands.put("initialize", new Initialize());
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
