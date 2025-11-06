package ssv.home.ozonbot.service.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.service.handler.CommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandRouter {

    private final Map<String, CommandHandler> handlers = new HashMap<>();

    @Autowired
    public CommandRouter(List<CommandHandler> allHandlers) {
        allHandlers.forEach(this::registerHandler);
    }

    public void registerHandler(CommandHandler handler) {
        handlers.put(handler.getCommand(), handler);
    }

    public BotApiMethod<?> route(Message message, TelegramBot bot) {
        String text = message.getText();

        CommandHandler handler = handlers.get(text);
        if (handler != null) {
            return handler.answer(message, bot);
        } else {
            // Команда не найдена
            SendMessage errorMsg = new SendMessage();
            errorMsg.setChatId(message.getChatId());
            errorMsg.setText("Неизвестная команда: " + text);
            return errorMsg;
        }
    }

}
