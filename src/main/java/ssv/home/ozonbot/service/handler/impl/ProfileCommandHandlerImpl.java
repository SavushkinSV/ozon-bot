package ssv.home.ozonbot.service.handler.impl;

import lombok.AllArgsConstructor;
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
public class ProfileCommandHandlerImpl implements CommandHandler {

    private final MethodFactory methodFactory;
    private final ClientService clientService;

    @Override
    @Transactional
    public BotApiMethod<?> answer(Message message, TelegramBot bot) {
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

    protected SendMessage showProfile(Message message, Client client) {
        Long chatId = message.getChatId();
        StringBuilder text = new StringBuilder();
        ClientDetails clientDetails = client.getClientDetails();

        text.append("\uD83D\uDC64 Имя пользователя: ").append(clientDetails.getFirstName());
        text.append("\n\uD83D\uDCBC Роль: ").append(client.getRole().name());
        text.append("\n\uD83D\uDD11 Токен: ").append(client.getToken());

        return methodFactory.getSendMessageText(chatId,
                text.toString(),
                null);
    }
}
