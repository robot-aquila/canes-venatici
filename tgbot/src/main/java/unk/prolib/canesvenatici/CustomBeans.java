package unk.prolib.canesvenatici;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import unk.prolib.canesvenatici.tg.BotCredentials;
import unk.prolib.canesvenatici.tg.SampleInformerBot;

@Configuration
public class CustomBeans {

    @Bean
    TelegramLongPollingBot sampleInformerBot(
            @Value("${telegram.bot.sample-informer.username}") String username,
            @Value("${telegram.bot.sample-informer.token}") String token)
    {
        return new SampleInformerBot(new BotCredentials(username, token));
    }
}
