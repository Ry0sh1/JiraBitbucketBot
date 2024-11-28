package com.ryoshi.listener;

import com.ryoshi.command.Initialize;
import com.ryoshi.command.PullRequests;
import com.ryoshi.command.ReviewList;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandListener extends ListenerAdapter {



    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        String command = event.getName();

        if (command.equals("initialize")){
            new Initialize().performCommand(event.getMember(), event.getChannel().asTextChannel(), null, null);
            event.reply("** **").complete().deleteOriginal().queue();
        }
        if (command.equals("pull-requests")){
            new PullRequests().performCommand(event.getMember(), event.getChannel().asTextChannel(), null, null);
            event.reply("** **").complete().deleteOriginal().queue();
        }
        if (command.equals("reviews")){
            new ReviewList().performCommand(event.getMember(), event.getChannel().asTextChannel(), null, null);
            event.reply("** **").complete().deleteOriginal().queue();
        }
    }


    public void onGuildReady(GuildReadyEvent event){
        List<CommandData> commandData = new ArrayList<>();

        commandData.add(Commands.slash("initialize", "Initialize the bot"));
        commandData.add(Commands.slash("pull-requests", "Gets all Pull Requests"));
        commandData.add(Commands.slash("reviews", "Count all reviews"));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }


}
