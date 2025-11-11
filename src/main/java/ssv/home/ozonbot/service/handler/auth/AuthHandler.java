package ssv.home.ozonbot.service.handler.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.client.Action;
import ssv.home.ozonbot.entity.client.Client;
import ssv.home.ozonbot.service.data.Role;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.factory.KeyboardFactory;
import ssv.home.ozonbot.service.handler.Handler;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AuthHandler implements Handler {

    private final MethodFactory methodFactory;
    private final KeyboardFactory keyboardFactory;
    private final ClientService clientService;

    @Override
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {
        log.debug("AuthHandler");
        Long chatId = message.getChatId();
        Client client = clientService.findByChatId(chatId);
        client.setAction(Action.AUTH);
        clientService.save(client);

        return methodFactory.getSendMessageText(
                chatId,
                "Выберите свою роль",
                keyboardFactory.getInlineKeyboard(
                        Role.getDisplayNames(),
                        List.of(2),
                        Role.getAuthCallbackCodes()
                )
        );
    }

    @Override
    public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, TelegramBot bot) {
        String[] parts = callbackQuery.getData().split(":", 2);
        String value = parts[1];
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Client client = clientService.findByChatId(chatId);

        log.debug("CallbackQueryHandler callbackData={}", callbackQuery.getData());
        if (value.equals(Role.EMPLOYEE.getCode())) {
            client.setRole(Role.EMPLOYEE);
        } else {
            client.setRole(Role.CUSTOMER);
        }
        client.setAction(Action.FREE);
        clientService.save(client);
        bot.executeTelegramApiMethod(methodFactory.getAnswerCallbackQuery(
                callbackQuery.getId(),
                "Авторизация прошла успешно. Повторите предыдущий запрос.")
        );

        return methodFactory.getDeleteMessage(
                chatId,
                messageId);
    }
}
