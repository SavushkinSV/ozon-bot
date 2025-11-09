package ssv.home.ozonbot.service.handler.message;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.service.handler.Handler;

@Service
public class MessageHandler implements Handler {

    @Override
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {

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
