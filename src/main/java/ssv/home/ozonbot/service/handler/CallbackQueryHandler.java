package ssv.home.ozonbot.service.handler;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ssv.home.ozonbot.bot.TelegramBot;

@Service
public class CallbackQueryHandler {
    public BotApiMethod<?> answer(Update update, TelegramBot bot) {

        return null;
    }
}
