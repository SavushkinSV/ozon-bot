package ssv.home.ozonbot.service.manager;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ssv.home.ozonbot.bot.TelegramBot;

public interface Manager {

    <T extends BotApiObject> BotApiMethod<?> route(T apiObject, TelegramBot bot);

}
