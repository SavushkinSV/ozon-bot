package ssv.home.ozonbot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.service.handler.CallbackQueryHandler;
import ssv.home.ozonbot.service.router.CommandRouter;
import ssv.home.ozonbot.service.handler.MessageHandler;

@Service
@AllArgsConstructor
public class UpdateDispatcher {

    private final MessageHandler messageHandler;
    private final CommandRouter commandRouter;
    private final CallbackQueryHandler callbackQueryHandler;

    public BotApiMethod<?> distribute(Update update, TelegramBot bot) {
        if (update.hasCallbackQuery()) {
            return callbackQueryHandler.answer(update, bot);
        }
        if (update.hasMessage()) {
            Message message = update.getMessage();
            // проверяем, что текст не является командой бота
            if (message.isCommand()) {
                return commandRouter.route(message, bot);
            }

            return messageHandler.answer(message, bot);
        }

        return null;
    }
}
