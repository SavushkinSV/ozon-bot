package ssv.home.ozonbot.service.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.service.manager.auth.AuthManager;

import static ssv.home.ozonbot.service.data.Callback.*;

@Service
@AllArgsConstructor
public class CallbackQueryHandler implements Handler {

    private final AuthManager authManager;

    public BotApiMethod<?> answer(Update update, TelegramBot bot) {
        String callbackData = update.getCallbackQuery().getData();
        String keyWord = callbackData.split("_")[0];

        if (AUTH.equals(keyWord)) {
            return authManager.answerCallbackQuery(update.getCallbackQuery(), bot);
        }


        return null;
    }
}
