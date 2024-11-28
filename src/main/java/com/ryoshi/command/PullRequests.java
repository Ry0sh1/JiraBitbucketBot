package com.ryoshi.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryoshi.model.MergeResult;
import com.ryoshi.model.Response;
import com.ryoshi.util.BitBucketApi;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;

public class PullRequests implements ServerCommand {

    private static final String roleId = "1295797738100756520";

    /**
     * Diese Methode ist dafür verantwortlich die eigentliche PR Nachricht zu bauen und zu senden
     * @param m Member von dem der Command kommt
     * @param channel Channel in welchem die Nachricht versendet werden soll
     * @param message
     * @param extraString
     */

    @Override
    public void performCommand(net.dv8tion.jda.api.entities.Member m, TextChannel channel, Message message, String extraString) {
        //Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();
        Response response = gson.fromJson(BitBucketApi.getCurrentPullRequests(), Response.class);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Pull Requests");
        eb.setDescription("Wir haben momentan noch " + response.getSize() + " offene Pull Requests:");
        eb.setColor(Color.RED);

        for (int i = 0; i < response.getSize(); i++) {
            eb.addField(response.getValues()[i].getTitle(),
                    response.getValues()[i].getAuthor().getUser().getDisplayName() +
                    " / " + response.getValues()[i].getAuthor().getUser().getName() +
                    '\n' + response.getValues()[i].getLinks().getSelf()[0].getHref() +
                    '\n' + returnSentenceOnConflicts(response.getValues()[i].getProperties().getMergeResult()) +
                    returnExclamationMarkOnConflicts(response.getValues()[i].getProperties().getMergeResult()) + "\n\n", false);
        }
        //pingt die SWP2024F member da der Ping nicht effektiv ist, wenn man ihn im embed setzt
        channel.sendMessage(channel.getGuild().getRoleById(roleId).getAsMention()).queue();
        channel.sendMessageEmbeds(eb.build()).queue();
    }


    /**
     * Gibt einen String zurück, wenn es bei dem jeweiligen PR einen Merge Konflikt gibt
     * ist wichtig für die Formatierung des embeds
     * @param result MergeResult aus dem PR der gecheckt werden soll
     * @return String, ob ein Merge konfligt vorliegt oder nicht
     */

    private String returnSentenceOnConflicts(MergeResult result){
        if (result.getOutcome().equals("CLEAN")){
            return "Dieser Pull Request hat keine Merge-Konflikte.";
        }
        else return "Dieser Pull Request hat noch offene Merge-Konflikte.";
    }


    /**
     * Gibt einen Discord emoji in form eines ! als String aus, falls Mergekonflikte bestehen
     * @param result MergeResult des zu checkenden PRs
     * @return String eines Discord emojis, falls ein MergeKonflikt bestehen sollte
     */

    private String returnExclamationMarkOnConflicts(MergeResult result){
        if (result.getOutcome().equals("CLEAN")){
            return "";
        }
        else return ":exclamation:";
    }
}
