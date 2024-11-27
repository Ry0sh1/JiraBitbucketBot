package com.ryoshi;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class JiraBot {

    public static JiraBot INSTANCE;
    private CommandManager cmdMan;

    public static void main (String[] args){
        try {
            new JiraBot();
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
    }

    public JiraBot() throws LoginException{
        INSTANCE = this;

        JDABuilder builder = JDABuilder.createDefault(System.getenv("API_KEY"));

        builder.setActivity(Activity.playing("On Work"));
        builder.setStatus(OnlineStatus.IDLE);

        builder.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        this.cmdMan = new CommandManager();

        //Command Manager

        builder.addEventListeners(new CommandListener());

        //Listener

        JDA jiraBot = builder.build();
    }

    public CommandManager getCmdMan() {
        return cmdMan;
    }

}