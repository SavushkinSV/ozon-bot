package ssv.home.ozonbot.service.handler.command;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.client.Client;
import ssv.home.ozonbot.entity.client.ClientDetails;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.data.Command;
import ssv.home.ozonbot.service.factory.KeyboardFactory;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.handler.CommandHandler;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class ProfileCommandHandler implements CommandHandler {

    private final MethodFactory methodFactory;
    private final KeyboardFactory keyboardFactory;
    private final ClientService clientService;

    @Override
    @Transactional
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {
        log.debug("ProfileCommandHandler");
        Client client = clientService.findByChatId(message.getChatId());
        if (!client.getRole().isAuthenticated()) {
            return methodFactory.getSendMessageText(
                    message.getChatId(),
                    "Вы не авторизованы. Авторизуйтесь с помощью команды " + Command.AUTH.getCommand(),
                    null);
        }
        bot.executeTelegramApiMethod(showProfile(message, client));

        if (client.getClientDetails().getPhoneNumber() == null) {
            bot.executeTelegramApiMethod(sendContactRequest(message));
        }

        return null;
    }

    @Override
    public String getCommand() {
        return Command.PROFILE.getCommand();
    }

    /**
     * Формирует объект {@link SendMessage} для отображения профиля пользователя в чате.
     * <p>
     * Метод собирает основные данные о клиенте (имя, роль, токен) и формирует текстовое сообщение
     * с использованием эмодзи‑иконок для визуального выделения полей.
     *
     * @param message объект {@link Message}, полученный от Telegram.
     * @param client  объект {@link Client}, содержащий данные пользователя.
     * @return объект {@link SendMessage}, готовый к отправке через Telegram Bot API.
     */
    protected SendMessage showProfile(Message message, Client client) {
        Long chatId = message.getChatId();
        StringBuilder text = new StringBuilder();
        ClientDetails clientDetails = client.getClientDetails();

        text.append("\uD83D\uDC64 Имя пользователя: ").append(clientDetails.getFirstName());
        text.append("\n\uD83D\uDCBC Роль: ").append(client.getRole().getDisplayName());

        String phoneNumber = (client.getClientDetails().getPhoneNumber() == null) ? "Нет" : "Есть";
        text.append("\n☎ Телефон: ").append(phoneNumber);

        return methodFactory.getSendMessageText(chatId,
                text.toString(),
                null);
    }

    private SendMessage sendContactRequest(Message message) {
        ReplyKeyboardMarkup keyboard = keyboardFactory.getReplyKeyboard(
                List.of("Поделиться", "Отмена"),
                List.of(2)
        );
        keyboard.getKeyboard().get(0).get(0).setRequestContact(true);
        keyboard.setResizeKeyboard(true); // клавиатура подстраивается под экран
        keyboard.setOneTimeKeyboard(true);  // скрывать после нажатия
        return methodFactory.getSendMessageText(message.getChatId(),
                "В Вашем профиле не указан номер телефона. Пожалуйста, поделитесь номером телефона.",
                keyboard);
    }

}
