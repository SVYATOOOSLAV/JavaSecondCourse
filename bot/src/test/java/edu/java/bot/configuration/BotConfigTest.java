package edu.java.bot.configuration;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.mainCommands.HelpCommand;
import edu.java.bot.commands.mainCommands.ListCommand;
import edu.java.bot.commands.mainCommands.StartCommand;
import edu.java.bot.commands.mainCommands.TrackCommand;
import edu.java.bot.commands.mainCommands.UntrackCommand;
import edu.java.bot.linkvalidators.LinkValidator;
import edu.java.bot.linkvalidators.LinkValidatorManager;
import edu.java.bot.linkvalidators.baseValidators.GitHubValidator;
import edu.java.bot.linkvalidators.baseValidators.StackOverflowValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;
import java.util.List;

@Configuration
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotConfigTest {
    @Autowired
    ApplicationConfig applicationConfig;
    @Bean
    String botName(){
        return applicationConfig.botName();
    }
    @Bean
    LinkValidator gitHubValidator (){
        return new GitHubValidator();
    }
    @Bean
    LinkValidator stackOverFlowValidator(){
        return new StackOverflowValidator();
    }
    @Bean
    List<LinkValidator> validators(){
        return List.of(gitHubValidator(), stackOverFlowValidator());
    }
    @Bean
    LinkValidatorManager linkValidatorManager(){
        return new LinkValidatorManager(validators());
    }

    @Bean
    Command startCommand(){
        return new StartCommand(botName());
    }

    @Bean
    Command listCommand(){
        return new ListCommand();
    }

    @Bean
    Command trackCommand(){
        return new TrackCommand(linkValidatorManager(), botName());
    }

    @Bean
    Command untrackCommand(){
        return new UntrackCommand(linkValidatorManager(), botName());
    }

    @Bean
    List<Command> commands(){
        return List.of(startCommand(), listCommand(), trackCommand(), untrackCommand());
    }

    @Bean
    Command helpCommand(){
        return new HelpCommand(commands());
    }
}
