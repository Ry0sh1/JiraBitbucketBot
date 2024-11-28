package com.ryoshi.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryoshi.model.PullRequest;
import com.ryoshi.model.Response;
import com.ryoshi.model.Reviews;
import com.ryoshi.model.User;
import com.ryoshi.util.BitBucketApi;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;
import java.util.HashMap;

public class ReviewList implements ServerCommand {

    @Override
    public void performCommand(Member m, TextChannel channel, Message message, String extraString) {
        //Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();
        Reviews pastPullRequests = gson.fromJson(BitBucketApi.getPastPullRequests(), Reviews.class);
        Response currentPullRequests = gson.fromJson(BitBucketApi.getCurrentPullRequests(), Response.class);

        HashMap<String, Integer> reviewCounter = new HashMap<>();
        countReviews(reviewCounter, pastPullRequests.getSize(), pastPullRequests.getValues());
        countReviews(reviewCounter, currentPullRequests.getSize(), currentPullRequests.getValues());

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Review List");
        builder.setColor(Color.RED);
        builder.setDescription("Hier ist die Anzahl von Reviews die jeder bereits gemacht hat:\n");

        reviewCounter.forEach( (name, count) -> {
            builder.addField(name + ": " + count,"",false);
        });

        channel.sendMessageEmbeds(builder.build()).queue();
    }

    private void countReviews(HashMap<String, Integer> reviewCounter, int size, PullRequest[] pullRequests) {
        for (int i = 0; i < size; i++){
            PullRequest currentPullRequest = pullRequests[i];
            for (int j = 0; j < currentPullRequest.getReviewers().length; j++){
                User currentUser = currentPullRequest.getReviewers()[j].getUser();
                if (reviewCounter.containsKey(currentUser.getSlug())){
                    reviewCounter.put(currentUser.getSlug(),reviewCounter.get(currentUser.getSlug()) + 1);
                }else {
                    reviewCounter.put(currentUser.getSlug(), 1);
                }
            }
        }
    }

}
