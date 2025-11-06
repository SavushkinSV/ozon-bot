package ssv.home.ozonbot.service.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.data.BotCommandEnum;
import ssv.home.ozonbot.service.handler.CommandHandler;

@Component
public class HelpCommandHandlerImpl implements CommandHandler {

    @Override
    public BotApiMethod<?> answer(Message message, TelegramBot bot) {
        return bot.createApiSendMessageCommand("Показать список команд");
    }

    @Override
    public String getCommand() {
        return BotCommandEnum.HELP.getCommand();
    }
}
