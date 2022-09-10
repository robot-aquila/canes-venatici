package unk.prolib.canesvenatici;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class CanesVenaticiApplication implements CommandLineRunner {
    @Autowired private TelegramLongPollingBot sampleInformerBot;

    public static void main(String[] args) {
        SpringApplication.run(CanesVenaticiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(sampleInformerBot); // returned BotSession can be used to shutdown
    }

}
