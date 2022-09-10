package unk.prolib.canesvenatici.tg;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;

// t.me/UNKSampleInformerBot
@Slf4j
public class SampleInformerBot extends CommonLongPollingBot {

    public SampleInformerBot(BotCredentials credentials) {
        super(credentials);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if ( ! update.hasMessage() || ! update.getMessage().hasText() ) {
            return;
        }
        var response = ResponseBuilder.of(this, update);
        switch ( response.getRequestText() ) {
        case "/start":
            response.appendLine("Welcome aboard!")
                .appendLine("Type /help to display available commands");
            break;
        case "/help":
            response.appendLine("/help - show this help")
                .appendLine("/time - show bot time")
                .appendLine("/status - show the number of events generated")
                .appendLine("/shutdown - shutdown the bot");
            break;
        case "/shutdown":
            response.appendLine("Are you kidding me?");
            break;
        case "/time":
            response.appendLine(LocalDateTime.now().toString());
            break;
        case "/status":
            response.appendLine("count: " + ThreadLocalRandom.current().nextInt());
            break;
        default:
            response.appendLine("Unknown command")
                .appendLine("Type /help to display available commands");
        }
        response.sendResponse();
    }

}
