package ssv.home.ozonbot.service.handler.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.client.ClientDetails;
import ssv.home.ozonbot.service.data.Command;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.handler.CommandHandler;

@Component
@AllArgsConstructor
public class StartCommandHandlerImpl implements CommandHandler {

    private final ClientService clientService;
    private final MethodFactory methodFactory;

    @Override
    @Transactional
    public BotApiMethod<?> answer(Message message, TelegramBot bot) {
        Long chatId = message.getFrom().getId();
        ClientDetails clientDetails = clientService.getByChatId(chatId).getClientDetails();

        String text = "Здравствуйте <b>" + clientDetails.getFirstName() + " " + clientDetails.getLastName() +
                "</b>!\n\n" + """
                🔅 С помощью этого бота вы сможете отследить изменение цены на понравившиеся товары в маркетплейс Ozon.
                
                🔅 Для начала отслеживания цены на товар отправьте боту артикул товара или ссылку на товар. Можно прислать список артикулов товаров через запятую или пробел.
                """;
        return methodFactory.getSendMessageHtml(chatId, text, null);
    }

    @Override
    public String getCommand() {
        return Command.START.getCommand();
    }
}
