package unk.prolib.canesvenatici.tg;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public abstract class CommonLongPollingBot extends TelegramLongPollingBot {
    @NonNull private final BotCredentials credentials;

    @Override
    public String getBotUsername() {
        return credentials.getUsername();
    }

    @Override
    public String getBotToken() {
        return credentials.getToken();
    }

}
