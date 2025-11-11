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
public class AboutCommandHandler implements CommandHandler {

    private final MethodFactory methodFactory;

    @Override
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {
        log.debug("AboutCommandHandler");
        String text = """
                *Сыродавленное масло* — это особенный вид масла, который делается исключительно из сырых семян и орехов, по старинной технологии в деревянном бочонке. Такая технология позволяет сохранить 100% полезных свойств, в процессе нет нагрева сырья, нет соприкосновения с металлом, а значит — нет окисления и разрушения важнейших аминокислот.
                
                Преимущества:
                ✅️отсутствие химических веществ в составе;
                ✅️регулярное употребление позволяет снизить уровень «плохого» холестерина и увеличить количество «хорошего»;
                ✅️замена всех других типов масел на сыродавленное поспособствует потере веса;
                ✅️улучшение кровообращения;
                ✅️качества кожи; замедление процессов старения.
                ✅️ Но самое главное - сыродавленные масла богаты питательными веществами
                
                Рекомендации по хранению:
                ☀️ Не оставляйте крышку на бутылочке с маслом открытой на долгое время. Под воздействием кислорода масло быстро окисляется.
                ☀️ Воздействие солнечного света влияет на качество масла, но бутылки темного цвета помогают предотвратить это.
                ☀️ Храните масло в прохладном темном шкафу или в кладовой, а также рекомендуется хранение в холодильнике.
                ☀️ Срок хранения масла 1-2 месяца, поэтому стоит употребить его как можно быстрей.
                """;
        return methodFactory.getSendMessageMarkdown(message.getChatId(), text, null);
    }

    @Override
    public String getCommand() {
        return Command.ABOUT.getCommand();
    }
}
