package ssv.home.ozonbot.service.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;

public interface Handler {

    BotApiMethod<?> answerMessage(Message message, TelegramBot bot);

    BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, TelegramBot bot);

}
