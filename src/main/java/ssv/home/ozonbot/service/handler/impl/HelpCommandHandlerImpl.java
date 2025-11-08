package ssv.home.ozonbot.service.handler.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.service.data.Command;
import ssv.home.ozonbot.service.factory.AnswerMethodFactory;
import ssv.home.ozonbot.service.handler.CommandHandler;

@Component
@AllArgsConstructor
public class HelpCommandHandlerImpl implements CommandHandler {

    private final AnswerMethodFactory answerMethodFactory;

    @Override
    public BotApiMethod<?> answer(Message message, TelegramBot bot) {
        String text = """
                ℹ️ *Как работает бот?*
                Бот опрашивает на наличие товара, изменение цены и других данных в маркетплейс Ozon и если изменения попадают в диапазон ваших настроек присылает уведомление. Для последующего отслеживания устанавливается новая текущая цена.
                
                ℹ️ *Как часто бот опрашивает маркетплейс Ozon?*
                Минимальный период между опросами 1 час, по __необходимости__ этот период может быть увеличен.
                """;
        return answerMethodFactory.getSendMessageMarkdown(message.getChatId(), text, null);
    }

    @Override
    public String getCommand() {
        return Command.HELP.getCommand();
    }
}
