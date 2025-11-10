package ssv.home.ozonbot.service.handler.command;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.service.data.Command;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.handler.CommandHandler;

@Component
@AllArgsConstructor
@Slf4j
public class ContactCommandHandler implements CommandHandler {

    private final MethodFactory methodFactory;

    @Override
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {
        log.debug("ContactCommandHandler");
        String caption = """
                <b>Наши контакты</b>
                
                ☎ +7 (920) 646-61-63
                📧 solnce_v_tarelke@mail.ru
                🌐 www.sun.ru
                """;
        bot.executeTelegramApiMethod(methodFactory.getSendPhotoHtml(
                message.getChatId(),
                "about",
                caption
        ));
        return null;
    }

    @Override
    public String getCommand() {
        return Command.CONTACT.getCommand();
    }
}
