package ssv.home.ozonbot.service.manager.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.handler.CommandHandler;
import ssv.home.ozonbot.service.manager.Manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandManager implements Manager {

    private final MethodFactory methodFactory;
    private final Map<String, CommandHandler> handlers = new HashMap<>();

    @Autowired
    public CommandManager(List<CommandHandler> allHandlers) {
        allHandlers.forEach(this::registerHandler);
        this.methodFactory = new MethodFactory();
    }

    public void registerHandler(CommandHandler handler) {
        handlers.put(handler.getCommand(), handler);
    }

    @Override
    public <T extends BotApiObject> BotApiMethod<?> route(T apiObject, TelegramBot bot) {
        if (apiObject instanceof Message message) {
            CommandHandler handler = handlers.get(message.getText());
            if (handler != null) {
                return handler.answerMessage(message, bot);
            } else {
                String text = "Неизвестная команда: " + message.getText();
                return methodFactory.getSendMessageText(message.getChatId(), text, null);
            }
        }
        return null;
    }
}
