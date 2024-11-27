package com.ryoshi;


import com.ryoshi.commands.PullRequests;
import com.ryoshi.models.PullRequest;
import com.ryoshi.models.Response;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

        builder.setActivity(Activity.playing("Jona testet gerade"));
        builder.setStatus(OnlineStatus.ONLINE);

        builder.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        this.cmdMan = new CommandManager();

        //Command Manager

        builder.addEventListeners(new CommandListener());

        //Listener

        JDA jiraBot = builder.build();
        PullRequests prGetter = new PullRequests();

        //Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();
        Response response = gson.fromJson(prGetter.getPullRequests(), Response.class);

        System.out.println("Wir haben momentan noch " + response.getSize() + " offene Pull-Requests:");
        System.out.println("==================");
        System.out.println("Title: " + response.getValues()[0].getTitle());
        System.out.println("Status: " + response.getValues()[0].getState());
        System.out.println("Author: " + response.getValues()[0].getAuthor().getUser().getDisplayName() + " / " +
                response.getValues()[0].getAuthor().getUser().getName());
        System.out.println("Link: " + response.getValues()[0].getLinks().getSelf()[0].getHref());
    }

    public CommandManager getCmdMan() {
        return cmdMan;
    }

}