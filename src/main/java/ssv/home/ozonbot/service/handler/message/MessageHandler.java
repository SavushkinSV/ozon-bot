package ssv.home.ozonbot.service.handler.message;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.client.Client;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.handler.Handler;
import ssv.home.ozonbot.util.EncryptionUtil;

@Service
@Slf4j
@AllArgsConstructor
public class MessageHandler implements Handler {

    private final ClientService clientService;
    private final EncryptionUtil encryptionUtil;
    private final MethodFactory methodFactory;

    @Override
    @Transactional
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {
        log.debug("MessageHandler {}", message.getText());
        if (message.hasContact()) {
            return addPhoneNumber(message);
        }
        String text = message.getText();
        if ("Отмена".equals(text)) {
            return methodFactory.sendMessageTextWithDeleteKeyboard(
                    message.getChatId(),
                    "Действие отменено");
        }

        return null;
    }

    @Override
    public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, TelegramBot bot) {
        return null;
    }

    private SendMessage addPhoneNumber(Message message) {
        Client client = clientService.findByChatId(message.getChatId());
        String phoneNumber = encryptionUtil.encrypt(message.getContact().getPhoneNumber(), client);
        client.getClientDetails().setPhoneNumber(phoneNumber);
        clientService.save(client);
        return methodFactory.sendMessageTextWithDeleteKeyboard(message.getChatId(), "Телефон добавлен в профиль");
    }

}
