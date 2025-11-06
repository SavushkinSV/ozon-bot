package ssv.home.ozonbot.service.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.data.BotCommandEnum;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.handler.CommandHandler;



@Component
public class StartCommandHandlerImpl implements CommandHandler {

    private final ClientService clientService;

    public StartCommandHandlerImpl(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public BotApiMethod<?> answer(Message message, TelegramBot bot) {
        Long chatId = message.getFrom().getId();

        if(!clientService.existsByChatId(chatId.toString())) {
            clientService.createFromUser(message.getFrom(), chatId.toString());
        }

        return bot.createApiSendMessageCommand("Добро пожаловать");
    }

    @Override
    public String getCommand() {
        return BotCommandEnum.START.getCommand();
    }
}
