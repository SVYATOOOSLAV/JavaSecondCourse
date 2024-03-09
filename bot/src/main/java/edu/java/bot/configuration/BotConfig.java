package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.model.commands.Command;
import edu.java.bot.model.commands.mainCommands.HelpCommand;
import edu.java.bot.model.commands.mainCommands.ListCommand;
import edu.java.bot.model.commands.mainCommands.StartCommand;
import edu.java.bot.model.commands.mainCommands.TrackCommand;
import edu.java.bot.model.commands.mainCommands.UntrackCommand;
import edu.java.bot.model.linkvalidators.baseValidators.GitHubValidator;
import edu.java.bot.model.linkvalidators.LinkValidator;
import edu.java.bot.model.linkvalidators.LinkValidatorManager;
import edu.java.bot.model.linkvalidators.baseValidators.StackOverflowValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotConfig {
    @Bean
    TelegramBot telegramBot(ApplicationConfig applicationConfig) {
        return new TelegramBot(applicationConfig.telegramToken());
    }

    @Bean
    String botName(ApplicationConfig applicationConfig){
        return applicationConfig.botName();
    }
    @Bean
    LinkValidator gitHubValidator() {
        return new GitHubValidator();
    }

    @Bean
    LinkValidator stackOverflowValidator() {
        return new StackOverflowValidator();
    }

    @Bean
    List<LinkValidator> validators(
        @Qualifier("gitHubValidator") LinkValidator gitHubValidator,
        @Qualifier("stackOverflowValidator") LinkValidator stackOverflowValidator
    ){
        return List.of(gitHubValidator, stackOverflowValidator);
    }
}
