package ssv.home.ozonbot.service.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.service.manager.auth.AuthManager;

import static ssv.home.ozonbot.service.data.Callback.*;

@Service
@AllArgsConstructor
@Slf4j
public class CallbackQueryHandler  {

    private final AuthManager authManager;

    public BotApiMethod<?> answerMessage(Update update, TelegramBot bot) {
        String callbackData = update.getCallbackQuery().getData();
        String keyWord = callbackData.split("_")[0];
        log.info("CallbackQueryHandler answer callbackData={} keyWord={}", callbackData, keyWord);

        if (AUTH.equals(keyWord)) {
            return authManager.answerCallbackQuery(update.getCallbackQuery(), bot);
        }


        return null;
    }
}
