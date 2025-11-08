package ssv.home.ozonbot.service.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
public class AnswerMethodFactory {

    /**
     * Формирует объект {@code SendMessage} для отправки текстового сообщения через Telegram Bot API
     *
     * @param chatId задает {@code ID} чата;
     * @param text текст сообщения;
     * @param keyboard клавиатура (inline‑кнопки или reply‑клавиатура);
     * @param parseMode режим форматирования текста в сообщении ("Markdown", "HTML" или null).
     * @return объект класса {@code SendMessage}
     */
    private SendMessage getSendMessage(Long chatId,
                                       String text,
                                       ReplyKeyboard keyboard,
                                       String parseMode) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode(parseMode)
                .replyMarkup(keyboard)
                .disableWebPagePreview(true)
                .build();
    }

    public SendMessage getSendMessageText(Long chatId,
                                          String text,
                                          ReplyKeyboard keyboard) {
        return getSendMessage(chatId, text, keyboard, null);
    }

    public SendMessage getSendMessageMarkdown(Long chatId,
                                              String text,
                                              ReplyKeyboard keyboard) {
        return getSendMessage(chatId, text, keyboard, ParseMode.MARKDOWN);
    }

    public SendMessage getSendMessageHtml(Long chatId,
                                          String text,
                                          ReplyKeyboard keyboard) {
        return getSendMessage(chatId, text, keyboard, ParseMode.HTML);
    }

}
