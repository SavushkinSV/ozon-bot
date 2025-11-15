package ssv.home.ozonbot.service.handler.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.client.Action;
import ssv.home.ozonbot.entity.client.Client;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.data.Command;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.handler.CommandHandler;

@Component
@AllArgsConstructor
public class AuthCommandHandler implements CommandHandler {

    private final ClientService clientService;
    private final MethodFactory methodFactory;

    @Override
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {

        Long chatId = message.getChatId();
        Client client = clientService.findByChatId(chatId);

        if (client.getAction() == Action.FREE) {
            return methodFactory.getSendMessageText(message.getChatId(), "Вы уже авторизованы.", null);
        }

        return null;
    }

    @Override
    public String getCommand() {
        return Command.AUTH.getCommand();
    }

}
