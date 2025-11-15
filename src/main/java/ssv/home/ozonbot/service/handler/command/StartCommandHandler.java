package ssv.home.ozonbot.service.handler.command;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.client.ClientDetails;
import ssv.home.ozonbot.service.data.Command;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.handler.CommandHandler;

@Component
@AllArgsConstructor
@Slf4j
public class StartCommandHandler implements CommandHandler {

    private final ClientService clientService;
    private final MethodFactory methodFactory;

    @Override
    @Transactional
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {
        log.debug("StartCommandHandler");
        Long chatId = message.getFrom().getId();

        ClientDetails clientDetails = clientService.findByChatId(chatId).getClientDetails();
        StringBuilder sb = new StringBuilder();
        sb.append("Здравствуйте <b>").append(clientDetails.getFirstName());
        if (clientDetails.getLastName() != null) {
            sb.append(" ").append(clientDetails.getLastName());
        }
        sb.append("</b>!\n\n").append("""
                🔅 С помощью этого бота вы можете ознакомиться и заказать настоящий, полезный продукт, который принесет пользу для всей вашей семьи!.
                
                🔅 <b>Сыродавленное масло</b> — это особенный вид масла, который делается исключительно из сырых семян и орехов, по старинной технологии в деревянном бочонке. Такая технология позволяет сохранить 100% полезных свойств, в процессе нет нагрева сырья, нет соприкосновения с металлом, а значит — нет окисления и разрушения важнейших аминокислот.
                """);

        return methodFactory.getSendMessageHtml(chatId, sb.toString(), null);
    }

    @Override
    public String getCommand() {
        return Command.START.getCommand();
    }
}
