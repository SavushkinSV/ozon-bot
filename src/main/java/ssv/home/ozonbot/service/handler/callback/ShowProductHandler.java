package ssv.home.ozonbot.service.handler.callback;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.product.Product;
import ssv.home.ozonbot.service.ProductService;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.handler.Handler;

@Slf4j
@Service
@AllArgsConstructor
public class ShowProductHandler implements Handler {

    private final MethodFactory methodFactory;
    private final ProductService productService;

    @Override
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, TelegramBot bot) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String[] parts = callbackQuery.getData().split(":", 2);
        Long productId = Long.parseLong(parts[1]);
        Product product = productService.findById(productId);
        bot.executeTelegramApiMethod(methodFactory.getSendPhotoHtml(
                chatId,
                parts[1],
                formatProduct(product)
        ));

        return methodFactory.getDeleteMessage(
                chatId,
                callbackQuery.getMessage().getMessageId());
    }

    private String formatProduct(Product product) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>").append(product.getName()).append("</b>\n");
        sb.append("%,.2f₽\n".formatted(product.getPrice()));
        sb.append("\n<b>Описание:</b>\n");

        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            // Разбиваем описание на абзацы по двойным переносам
            String[] paragraphs = product.getDescription().split(" {2}");
            for (String paragraph : paragraphs) {
                sb.append(paragraph).append("\n\n");
            }
        } else {
            sb.append("Нет описания.");
        }
        return sb.toString();
    }
}
