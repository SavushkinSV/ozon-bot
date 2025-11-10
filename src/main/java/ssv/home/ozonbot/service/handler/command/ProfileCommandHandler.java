package ssv.home.ozonbot.service.handler.command;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.client.Client;
import ssv.home.ozonbot.entity.client.ClientDetails;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.data.Command;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.handler.CommandHandler;

@Component
@AllArgsConstructor
@Slf4j
public class ProfileCommandHandler implements CommandHandler {

    private final MethodFactory methodFactory;
    private final ClientService clientService;

    @Override
    @Transactional
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {
        log.debug("ProfileCommandHandler");
        Client client = clientService.findByChatId(message.getChatId());
        if (client.getRole().isAuthenticated())
            return showProfile(message, client);

        return methodFactory.getSendMessageText(
                message.getChatId(),
                "Вы не авторизованы. Авторизуйтесь с помощью команды /login",
                null);
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
        text.append("\n\uD83D\uDCBC Роль: ").append(client.getRole().name());

        return methodFactory.getSendMessageText(chatId,
                text.toString(),
                null);
    }
}
