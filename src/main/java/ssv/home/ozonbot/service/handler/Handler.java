package ssv.home.ozonbot.service.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ssv.home.ozonbot.bot.TelegramBot;

public interface Handler {

    BotApiMethod<?> answer(Update update, TelegramBot bot);

}
