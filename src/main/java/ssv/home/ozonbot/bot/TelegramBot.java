package ssv.home.ozonbot.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllPrivateChats;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ssv.home.ozonbot.data.BotCommandEnum;
import ssv.home.ozonbot.service.UpdateDispatcher;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
    private final UpdateDispatcher updateDispatcher;

    private ThreadLocal<Update> updateEvent = new ThreadLocal<>();

    @Autowired
    public TelegramBot(BotProperties botProperties, UpdateDispatcher updateDispatcher) throws TelegramApiException {
        super(botProperties.getToken());
        this.botProperties = botProperties;
        this.updateDispatcher = updateDispatcher;
    }

    @PostConstruct
    private void init() throws TelegramApiException {
        telegramBotsApi.registerBot(this); // регистрируем бота
        syncCommandsWithMenu(); // синхронизируем меню
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            this.updateEvent.set(update);
            executeTelegramApiMethod(updateDispatcher.distribute(this.updateEvent.get(), this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает имя Telegram‑бота
     *
     * @return имя Telegram‑бота
     */
    @Override
    public String getBotUsername() {
        return botProperties.getUsername();
    }

    /**
     * Выполняет API‑запросы к Telegram Bot API
     *
     * @param method
     * @param <T>
     * @param <Method>
     * @return
     */
    private <T extends Serializable, Method extends BotApiMethod<T>> T executeTelegramApiMethod(Method method) {
        try {
            if (method != null) {
                return super.sendApiMethod(method);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Синхронизирует командное меню Telegram‑бота с актуальным списком команд
     */
    private void syncCommandsWithMenu() {
        List<BotCommand> commands = BotCommandEnum.getAllCommands();
        SetMyCommands setCommands = new SetMyCommands();
        setCommands.setCommands(commands);
        setCommands.setScope(new BotCommandScopeAllPrivateChats());
        executeTelegramApiMethod(setCommands);
    }

    /**
     * Формирует объект {@code SendMessage} для отправки текстового сообщения через Telegram Bot API
     *
     * @param text текст сообщения
     * @param parseMode тип режима парсинга текста в сообщении
     * @return объект класса {@code SendMessage}
     */
    public SendMessage createApiSendMessageCommand(String text, String parseMode) {
        SendMessage message = new SendMessage();
        message.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        message.setParseMode(parseMode);
        message.setChatId(getCurrentChatId());
        return message;
    }

    /**
     * Метод возвращает ID текущего Telegram-чата
     */
    public Long getCurrentChatId() {
        if (updateEvent.get().hasMessage()) {
            return updateEvent.get().getMessage().getFrom().getId();
        }
        if (updateEvent.get().hasCallbackQuery()) {
            return updateEvent.get().getCallbackQuery().getFrom().getId();
        }

        return null;
    }

}
