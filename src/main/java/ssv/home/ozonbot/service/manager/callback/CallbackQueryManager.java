package ssv.home.ozonbot.service.manager.callback;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.service.handler.auth.AuthHandler;
import ssv.home.ozonbot.service.handler.callback.ShowProductHandler;
import ssv.home.ozonbot.service.manager.Manager;

@Service
@AllArgsConstructor
@Slf4j
public class CallbackQueryManager implements Manager {

    private final AuthHandler authHandler;
    private final ShowProductHandler showProductHandler;

    @Override
    public <T extends BotApiObject> BotApiMethod<?> route(T apiObject, TelegramBot bot) {
        if (apiObject instanceof CallbackQuery callbackQuery) {
            String[] parts = callbackQuery.getData().split(":", 2);
            String keyWord = parts[0];
            log.debug("CallbackQueryHandler callbackData={}", callbackQuery.getData());

            switch (keyWord) {
                case "auth":
                    return authHandler.answerCallbackQuery(callbackQuery, bot);
                case "product":
                    return showProductHandler.answerCallbackQuery(callbackQuery, bot);
                default:
                    log.info("Неизвестный keyWord в callbackData");
            }
        }
        return null;
    }
}
