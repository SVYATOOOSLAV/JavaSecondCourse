package edu.java.bot.model.commands.mainCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.commands.Command;
import edu.java.bot.dao.DataBase;
import edu.java.bot.model.linkvalidators.LinkValidatorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
public class UntrackCommand implements Command {
    private final LinkValidatorManager manager;
    private String botName;

    private final String requestMessage =
        """
        Give me link or links that you want to untracked in massage starting with /untrack
        As an example:
        /untrack
        https://google.com/
        https://chat.openai.com/""";

    private final String successMessageNotSingle =
        "All links were removed from tracklist. If you want to see it send /list";
    private final String successMessageSingle =
        "Link was removed from tracklist. If you want to see it send /list";
    @Autowired
    public UntrackCommand(LinkValidatorManager manager, String botName) {
        this.manager = manager;
        this.botName = botName;
    }

    @Override
    public String commandName() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Отписаться от ресурса / ресурсов ";
    }

    @Override
    public SendMessage handle(long chatID, String text) {
        if (!DataBase.isUserInSystem(chatID)) {
            return new SendMessage(chatID, "Вы не зарегистрированы в системе, введите команду /start");
        }

        String[] commandAndURI = text.split("[\\s\\n]+");
        if (commandAndURI.length == 1) {
            return new SendMessage(chatID, requestMessage);
        }

        List<URI> trackList = getTrackList(commandAndURI, DataBase.getUserTrackList(chatID));
        DataBase.removeLinksFromUser(chatID, trackList);

        if(trackList.isEmpty()){
            return new SendMessage(chatID, "Invalid input, check your links with command /list");
        }
        // if some URI was uncorrected
        if(trackList.size() < commandAndURI.length - 1){
            return new SendMessage(chatID, getCorrectedAndUncorrectedURI(commandAndURI, trackList));
        }

        return new SendMessage(chatID, trackList.size() > 1 ? successMessageNotSingle : successMessageSingle);
    }

    private List<URI> getTrackList(String[] resources, List<URI> previousTrackList) {
        List<URI> newTrackList = new ArrayList<>();
        for (int i = 1; i < resources.length; i++) {
            URI link = URI.create(resources[i]);
            if (previousTrackList.contains(link)) {
                newTrackList.add(link);
            }
        }
        return newTrackList;
    }

    private String getCorrectedAndUncorrectedURI(String[] resources, List<URI> trackList ){
        return trackList.stream().reduce(new StringBuilder(trackList.size() > 1 ?
                    "Links were removed from tracklist: " :
                    "Link was removed from tracklist: "),
                (StringBuilder stringBuilder, URI uri) -> stringBuilder.append(uri).append("\n"),
                StringBuilder::append).append("If want to see all tracklist send /list").append("\n")
            .append(getUncorrectedURI(resources, trackList)).append("\n").toString();
    }

    private String getUncorrectedURI(String[] resources, List<URI> trackList){
        String header = "Invalid links:\n";
        StringBuilder stringBuilder = new StringBuilder(header);

        for (int i = 1; i < resources.length; i++) {
            URI link = URI.create(resources[i]);
            if (!trackList.contains(link)) {
                stringBuilder.append(resources[i]).append("\n");
            }
        }

        return stringBuilder.length() == header.length() ? "" :
            stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }
}
