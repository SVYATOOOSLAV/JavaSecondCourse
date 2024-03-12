package edu.java.bot.model.objects.clients;

import edu.java.DTO.response.LinkResponse;
import edu.java.DTO.response.ListLinksResponse;
import java.net.URI;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        ScrapperClient scrapperClient = new ScrapperClient();
        scrapperClient.deleteUser(1).get();
//        LinkResponse response = scrapperClient.addLink(1, "https://github.com/SVYATOOOSLAV/my-projec").get();
//        ListLinksResponse resp = scrapperClient.getTrackList(1).get();
//        System.out.println(response.link());
//        String[] uris = Arrays.stream(resp.links()).map(linkResponse -> linkResponse.link().toString()).toArray(String[]::new);
//        System.out.println(String.join("\n",uris) );
    }
}
