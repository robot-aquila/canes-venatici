package unk.prolib.canesvenatici.tg;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public class ResponseBuilder {
    @NonNull private final TelegramLongPollingBot owner;
    @NonNull private final Update update;
    @NonNull private StringBuilder messageBuilder = new StringBuilder();

    public static ResponseBuilder of(TelegramLongPollingBot owner, Update update) {
        return new ResponseBuilder(owner, update);
    }

    public String getRequestText() {
        return update.getMessage().getText();
    }

    public void sendResponse() {
        var message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(messageBuilder.toString());
        try {
            owner.execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send message: ", e);
        }
    }

    public ResponseBuilder appendLine(String text) {
        messageBuilder.append(text).append('\n');
        return this;
    }
}
