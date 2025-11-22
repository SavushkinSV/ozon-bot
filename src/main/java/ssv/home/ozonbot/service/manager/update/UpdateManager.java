package ssv.home.ozonbot.service.manager.update;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.service.handler.message.MessageHandler;
import ssv.home.ozonbot.service.manager.Manager;
import ssv.home.ozonbot.service.manager.callback.CallbackQueryManager;
import ssv.home.ozonbot.service.manager.command.CommandManager;

@Service
@AllArgsConstructor
@Slf4j
public class UpdateManager implements Manager {

    private final MessageHandler messageHandler;
    private final CommandManager commandManager;
    private final CallbackQueryManager callbackQueryManager;

    @Override
    public <T extends BotApiObject> BotApiMethod<?> route(T apiObject, TelegramBot bot) {
        Update update = (Update) apiObject;
        BotApiMethod<?> method = null;
        if (update.hasCallbackQuery()) {
            method = callbackQueryManager.route(update.getCallbackQuery(), bot);
        } else if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.isCommand()) { // проверяем, что текст не является командой бота
                method = commandManager.route(message, bot);
            } else {
                method = messageHandler.answerMessage(message, bot);
            }
        }
        return method;
    }
}
