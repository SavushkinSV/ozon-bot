package ssv.home.ozonbot.service.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.data.BotCommandEnum;
import ssv.home.ozonbot.model.Client;
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
        Client client;
        Long chatId = message.getFrom().getId();

        if (!clientService.existsByChatId(chatId.toString())) {
            client = clientService.createFromUser(message.getFrom(), chatId.toString());
        } else {
            client = clientService.getByChatId(chatId.toString());
        }

        String messageText = "Здравствуйте <b>" + client.getFirstName() + " " + client.getLastName() + "</b>!\n\n" + """
                🔅 С помощью этого бота вы сможете отследить изменение цены на понравившиеся товары в маркетплейс Ozon.
                
                🔅 Для начала отслеживания цены на товар отправьте боту артикул товара или ссылку на товар. Можно прислать список артикулов товаров через запятую или пробел.
                """;

        return bot.createApiSendMessageCommand(messageText, ParseMode.HTML);
    }

    @Override
    public String getCommand() {
        return BotCommandEnum.START.getCommand();
    }
}
