package ssv.home.ozonbot.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllPrivateChats;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ssv.home.ozonbot.service.data.Command;
import ssv.home.ozonbot.service.manager.update.UpdateManager;

import java.io.Serializable;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final UpdateManager updateManager;

    @Autowired
    public TelegramBot(BotProperties botProperties,
                       UpdateManager updateManager) {
        super(botProperties.getToken());
        this.botProperties = botProperties;
        this.updateManager = updateManager;
    }

    @PostConstruct
    private void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this); // регистрируем бота
            syncCommandsWithMenu(new BotCommandScopeAllPrivateChats()); // синхронизируем меню
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        executeTelegramApiMethod(updateManager.route(update, this));
    }

    /**
     * Возвращает имя Telegram‑бота
     *
     * @return имя Telegram‑бота.
     */
    @Override
    public String getBotUsername() {
        return botProperties.getUsername();
    }

    /**
     * Выполняет API‑запросы к Telegram Bot API
     *
     * @param method API‑запрос к Telegram Bot API ({@code SendMessage}, {@code CallbackQuery}).
     * @return результат выполнения Telegram Bot API.
     */
    public <T extends Serializable> T executeTelegramApiMethod(BotApiMethod<T> method) {
        if (method == null) return null;
        try {
            return super.sendApiMethod(method);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Message executeTelegramApiMethod(SendPhoto message) {
        try {
            return super.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Синхронизирует командное меню Telegram‑бота с актуальным списком команд
     */
    private void syncCommandsWithMenu(BotCommandScope scope) {
        List<BotCommand> commandList = Command.getAllCommands();
        SetMyCommands setCommands = SetMyCommands.builder()
                .commands(commandList)
                .scope(scope)
                .build();
        executeTelegramApiMethod(setCommands);
    }

}
