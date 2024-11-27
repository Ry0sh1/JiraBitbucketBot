package com.ryoshi.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryoshi.ServerCommand;
import com.ryoshi.models.MergeResult;
import com.ryoshi.models.Response;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.net.URL;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PullRequests implements ServerCommand {

    /**
     * Diese Methode zieht sich die JSON datei mit den ganzen daten von der BitBucket API
     * die env USERNAME und PASSWORD sind hierbei für den Jira login
     * @return
     */

    public String getPullRequests() {
        String httpsURL = "https://git.swl.informatik.uni-oldenburg.de//rest/api/latest/projects/SWP2024/repos/SWP2024f/pull-requests";
        final String auth = System.getenv("USERNAME") + ":" + System.getenv("PASSWORD");

        try {
            URL url = new URL(httpsURL);
            byte[] encodedBytes = Base64.getEncoder().encode(auth.getBytes());
            String encoded = new String(encodedBytes);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + encoded);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            inputLine = br.readLine();

            br.close();

            return inputLine;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Diese Methode ist dafür verantwortlich die eigentliche PR Nachricht zu bauen und zu senden
     * @param m Member von dem der Command kommt
     * @param channel Channel in welchem die Nachricht versendet werden soll
     * @param message
     * @param extraString
     */

    @Override
    public void performCommand(net.dv8tion.jda.api.entities.Member m, TextChannel channel, Message message, String extraString) {
        PullRequests prGetter = new PullRequests();

        //Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();
        Response response = gson.fromJson(prGetter.getPullRequests(), Response.class);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Pull Requests");
        eb.setDescription("Wir haben momentan noch " + response.getSize() + " offene Pull Requests:");

        for (int i = 0; i < Integer.parseInt(response.getSize()); i++) {
            eb.addField(response.getValues()[i].getTitle(),
                    response.getValues()[i].getAuthor().getUser().getDisplayName() +
                    " / " + response.getValues()[i].getAuthor().getUser().getName() +
                    '\n' + response.getValues()[i].getLinks().getSelf()[0].getHref() +
                    '\n' + returnSentenceOnConflicts(response.getValues()[i].getProperties().getMergeResult()) +
                    returnExclamationMarkOnConflicts(response.getValues()[i].getProperties().getMergeResult()) + "\n\n", false);
        }
        //pingt die SWP2024F member da der Ping nicht effektiv ist, wenn man ihn im embed setzt
        channel.sendMessage("@SWP2024F").queue();
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
