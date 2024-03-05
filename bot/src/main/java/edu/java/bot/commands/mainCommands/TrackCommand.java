package edu.java.bot.commands.mainCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.dao.DataBase;
import edu.java.bot.linkvalidators.LinkValidator;
import edu.java.bot.linkvalidators.LinkValidatorManager;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TrackCommand implements Command {
    private LinkValidatorManager manager;
    private String botName;
    private final String requestMessage =
        """
            Give me link or links that you want to track in massage starting with /track
            As an example:
            /track
            https://google.com/
            https://chat.openai.com/""";

    private final String successMessageNotSingle =
        "All links were added to the the tracklist. If you want to see it send /list";
    private final String successMessageSingle =
        "Link was added to the tracklist. If you want to see it send /list";

    @Autowired
    public TrackCommand(LinkValidatorManager manager, String botName) {
        this.manager = manager;
        this.botName = botName;
    }

    @Override
    public String commandName() {
        return "/track";
    }

    @Override
    public String description() {
        return "Подписаться на ресурс";
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
        DataBase.addLinksToUser(chatID, trackList);

        if(trackList.isEmpty()){
            return new SendMessage(chatID, getSupportedValidators());
        }
        // if some URI was uncorrected
        if(trackList.size() < commandAndURI.length - 1){
            return new SendMessage(chatID, getCorrectedAndUncorrectedURI(commandAndURI, trackList));
        }

        return new SendMessage(chatID, trackList.size() > 1 ? successMessageNotSingle : successMessageSingle);
    }

    private List<URI> getTrackList(String[] resources, List<URI> previousTrackList) {
        Set<URI> newTrackList = new HashSet<>();
        for (int i = 1; i < resources.length; i++) {
            URI link = URI.create(resources[i]);
            if (manager.isValidLink(link) && !previousTrackList.contains(link)) {
                newTrackList.add(link);
            }
        }
        return newTrackList.stream().sorted().toList();
    }

    private String getSupportedValidators(){
        List<LinkValidator> validators = manager.getValidators();
        return validators.stream().reduce(
            new StringBuilder(
                    validators.size() > 1 ? "Bot supports the following resources: " :
                                            "Bot support the following resource: ").append("\n"),
            (StringBuilder stringBuilder, LinkValidator validator) ->
                stringBuilder.append(validator.hostName()).append("\n"), StringBuilder::append).toString();
    }

    private String getCorrectedAndUncorrectedURI(String[] resources, List<URI> trackList ){
        return trackList.stream().reduce(new StringBuilder(trackList.size() > 1 ?
                    "Links were added to the tracklist: " :
                    "Link was added to the tracklist: "),
            (StringBuilder stringBuilder, URI uri) -> stringBuilder.append(uri).append("\n"),
            StringBuilder::append).append("If want to see all tracklist send /list").append("\n")
            .append(getUncorrectedURI(resources)).append("\n").toString();
    }

    private String getUncorrectedURI(String[] resources){
        String header = "Invalid links:\n";
        StringBuilder stringBuilder = new StringBuilder(header);

        for (int i = 1; i < resources.length; i++) {
            URI link = URI.create(resources[i]);
            if (!manager.isValidLink(link)) {
                stringBuilder.append(resources[i]).append("\n");
            }
        }

        return stringBuilder.length() == header.length() ? "" :
            stringBuilder.deleteCharAt(stringBuilder.length()-1).toString();
    }
}
