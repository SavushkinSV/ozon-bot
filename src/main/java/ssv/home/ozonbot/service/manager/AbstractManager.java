package ssv.home.ozonbot.service.manager;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;

public abstract class AbstractManager {

    public abstract BotApiMethod<?> answerCommand(Message message, TelegramBot bot);

    public abstract BotApiMethod<?> answerMessage(Message message, TelegramBot bot);

    public abstract BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, TelegramBot bot);

}
