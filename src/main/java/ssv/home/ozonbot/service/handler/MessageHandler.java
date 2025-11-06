package ssv.home.ozonbot.service.handler;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MessageHandler {

    public BotApiMethod<?> answer(Message message) {

//            String messageText = message.getText();
//            Long chatId = message.getChatId();
//
//            String responseText = "Вы написали: " + messageText;
//
//            SendMessage message = new SendMessage();
//            message.setChatId(String.valueOf(chatId));
//            message.setText(responseText);
//
//            try {
//                bot.execute(message);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }

            return null;


    }
}
