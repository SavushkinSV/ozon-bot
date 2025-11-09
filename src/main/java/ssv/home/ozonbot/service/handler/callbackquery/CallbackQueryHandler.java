package ssv.home.ozonbot.service.handler.callbackquery;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.service.handler.Handler;
import ssv.home.ozonbot.service.handler.auth.AuthHandler;

import static ssv.home.ozonbot.service.data.Callback.*;

@Service
@AllArgsConstructor
@Slf4j
public class CallbackQueryHandler implements Handler {

    private final AuthHandler authHandler;

    @Override
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, TelegramBot bot) {
        String callbackData = callbackQuery.getData();
        String keyWord = callbackData.split("_")[0];
        log.info("CallbackQueryHandler answerCallbackQuery callbackData={} keyWord={}", callbackData, keyWord);

        if (AUTH.equals(keyWord)) {
            return authHandler.answerCallbackQuery(
                    callbackQuery,
                    bot);
        }
        return null;
    }
}
