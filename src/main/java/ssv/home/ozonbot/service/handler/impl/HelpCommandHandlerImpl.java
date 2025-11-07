package ssv.home.ozonbot.service.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.data.BotCommandEnum;
import ssv.home.ozonbot.service.handler.CommandHandler;

@Component
public class HelpCommandHandlerImpl implements CommandHandler {

    @Override
    public BotApiMethod<?> answer(Message message, TelegramBot bot) {
        String messageText = """
                ℹ️ *Как работает бот?*
                Бот опрашивает на наличие товара, изменение цены и других данных в маркетплейс Ozon и если изменения попадают в диапазон ваших настроек присылает уведомление. Для последующего отслеживания устанавливается новая текущая цена.
                
                ℹ️ *Как часто бот опрашивает маркетплейс Ozon?*
                Минимальный период между опросами 1 час, по __необходимости__ этот период может быть увеличен.
                """;
        return bot.createApiSendMessageCommand(messageText, ParseMode.MARKDOWN);
    }

    @Override
    public String getCommand() {
        return BotCommandEnum.HELP.getCommand();
    }
}
