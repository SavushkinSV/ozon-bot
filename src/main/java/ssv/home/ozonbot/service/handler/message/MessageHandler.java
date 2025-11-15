package ssv.home.ozonbot.service.handler.message;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.client.Client;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.handler.Handler;

@Service
@Slf4j
@AllArgsConstructor
public class MessageHandler implements Handler {

    private final ClientService clientService;

    @Override
    @Transactional
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {
        if (message.hasContact()) {
            String phoneNumber = message.getContact().getPhoneNumber();
            log.debug("MessageHandler " + phoneNumber);
            Client client = clientService.findByChatId(message.getChatId());
            client.getClientDetails().setPhoneNumber(phoneNumber);
            clientService.save(client);
            return null;
        }
        log.debug("MessageHandler " + message.getText());
        SendMessage responseMessage = new SendMessage();
        responseMessage.setChatId(message.getChatId());
        String text = message.getText();
        if ("Отмена".equals(text)) {
            responseMessage.setText("Действие отменено");
            ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
            keyboardRemove.setRemoveKeyboard(true);
            responseMessage.setReplyMarkup(keyboardRemove);
        }

        return responseMessage;

    }

    @Override
    public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, TelegramBot bot) {
        return null;
    }


}
