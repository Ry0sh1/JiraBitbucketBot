package com.ryoshi.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryoshi.ServerCommand;
import com.ryoshi.models.Response;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.lang.reflect.Member;
import java.net.URL;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import java.util.Base64;

public class PullRequests implements ServerCommand {

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

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


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

        for(int i = 0; i < Integer.parseInt(response.getSize()); i++){
            eb.addField(response.getValues()[i].getTitle(),
                    response.getValues()[i].getAuthor().getUser().getDisplayName() +
                   " / " + response.getValues()[i].getAuthor().getUser().getName() +
                    '\n' + response.getValues()[i].getLinks().getSelf()[0].getHref() + '\n', false);
        }

        channel.sendMessageEmbeds(eb.build()).queue();
    }
}
