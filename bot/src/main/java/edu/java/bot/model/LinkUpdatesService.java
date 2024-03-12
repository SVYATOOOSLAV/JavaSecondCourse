package edu.java.bot.model;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.DTO.exception.InvalidRequestException;
import edu.java.DTO.request.LinkUpdate;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class LinkUpdatesService {
    private final TelegramBot bot;

    public void sendUpdates(LinkUpdate update) {
        idValidator(update.tgChatIds());
        linkValidator(update.url());
        String message = getMessage(update);
        for(long id : update.tgChatIds()){
            bot.execute(new SendMessage(id, message));
        }
    }

    private String getMessage(LinkUpdate update){
        StringBuilder sb = new StringBuilder("Detected changes on");
        sb.append(System.lineSeparator())
            .append(update.url())
            .append(System.lineSeparator())
            .append(update.description());
        return sb.toString();
    }

    private void idValidator(long[] ids){
        if(Arrays.stream(ids).anyMatch(id -> id < 0)){
            throw new InvalidRequestException("invalid id");
        }
    }
    private void linkValidator(String link) {
        try {
            new URI(link);
        } catch (URISyntaxException e) {
            throw new InvalidRequestException("link " + link + " was invalid");
        }
    }
}
