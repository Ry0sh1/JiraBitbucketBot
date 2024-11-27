package com.ryoshi.commands;

import java.net.URL;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import java.util.Base64;

public class PullRequests {

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

           /* while ((inputLine = br.readLine()) != null){
                System.out.println(inputLine);
            }*/

            br.close();

            return inputLine;

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
