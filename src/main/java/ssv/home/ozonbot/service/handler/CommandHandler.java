package ssv.home.ozonbot.service.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;

public interface CommandHandler {

    BotApiMethod<?> handle(Message message, TelegramBot bot);
    String getCommand(); // Возвращает команду (например, "/start")
}
