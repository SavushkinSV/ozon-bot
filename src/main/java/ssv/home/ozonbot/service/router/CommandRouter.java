package ssv.home.ozonbot.service.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.service.factory.AnswerMethodFactory;
import ssv.home.ozonbot.service.handler.CommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandRouter {

    private final AnswerMethodFactory answerMethodFactory;
    private final Map<String, CommandHandler> handlers = new HashMap<>();

    @Autowired
    public CommandRouter(List<CommandHandler> allHandlers) {
        allHandlers.forEach(this::registerHandler);
        this.answerMethodFactory = new AnswerMethodFactory();
    }

    public void registerHandler(CommandHandler handler) {
        handlers.put(handler.getCommand(), handler);
    }

    public BotApiMethod<?> route(Message message, TelegramBot bot) {
        CommandHandler handler = handlers.get(message.getText());
        if (handler != null) {
            return handler.answer(message, bot);
        } else {
            // Команда не найдена
            String text = "Неизвестная команда: " + message.getText();
            return answerMethodFactory.getSendMessageText(message.getChatId(), text, null);
        }
    }

}
