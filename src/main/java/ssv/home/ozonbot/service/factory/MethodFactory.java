package ssv.home.ozonbot.service.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.io.InputStream;

@Component
public class MethodFactory {

    /**
     * Формирует объект {@link SendMessage} для отправки текстового сообщения через Telegram Bot API
     *
     * @param chatId    задает {@code ID} чата;
     * @param text      текст сообщения;
     * @param keyboard  клавиатура (inline‑кнопки или reply‑клавиатура);
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

    /**
     * Формирует объект {@code DeleteMessage} для удаления текстового сообщения через Telegram Bot API
     *
     * @param chatId    задает {@code ID} чата;
     * @param messageId задает {@code ID} сообщения.
     * @return объект класса {@link DeleteMessage}
     */
    public DeleteMessage getDeleteMessage(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
    }

    public AnswerCallbackQuery getAnswerCallbackQuery(String callbackQueryId,
                                                      String text) {
        return AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .text(text)
                .build();
    }

    public static InputStream loadImage(String name) {
        try {
            return ClassLoader.getSystemResourceAsStream("images/" + name + ".jpg");
        } catch (Exception e) {
            throw new RuntimeException("Can't load photo!");
        }
    }

    /**
     * Формирует объект {@link SendPhoto} для отправки фото через Telegram Bot API
     *
     * @param chatId    задает {@code ID} чата;
     * @param photoKey  идентификатор изображения
     * @param caption   задает подпись для фото;
     * @param parseMode режим форматирования текста в подписи ("Markdown", "HTML" или null).
     * @return объект класса {@link SendPhoto}
     */
    private SendPhoto getSendPhoto(Long chatId,
                                   String photoKey,
                                   String caption,
                                   String parseMode) {
        try {
            InputFile inputFile = new InputFile();
            var is = loadImage(photoKey);
            inputFile.setMedia(is, photoKey);

            SendPhoto photo = new SendPhoto();
            photo.setPhoto(inputFile);
            photo.setChatId(chatId);

            if (caption != null && !caption.isEmpty()) {
                photo.setCaption(caption);
                photo.setParseMode(parseMode);
            }

            return photo;
        } catch (Exception e) {
            throw new RuntimeException("Can't create photo message!");
        }
    }

    public SendPhoto getSendPhotoHtml(Long chatId,
                                      String photoKey,
                                      String caption) {
        return getSendPhoto(chatId, photoKey, caption, ParseMode.HTML);
    }

    public SendPhoto getSendPhotoText(Long chatId,
                                      String photoKey,
                                      String caption) {
        return getSendPhoto(chatId, photoKey, caption, null);
    }

    public SendPhoto getSendPhotoMarkdown(Long chatId,
                                          String photoKey,
                                          String caption) {
        return getSendPhoto(chatId, photoKey, caption, ParseMode.MARKDOWN);
    }

}
