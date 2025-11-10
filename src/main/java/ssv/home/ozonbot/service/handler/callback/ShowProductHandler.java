package ssv.home.ozonbot.service.handler.callback;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.product.Product;
import ssv.home.ozonbot.service.ProductService;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.handler.Handler;

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
        bot.executeTelegramApiMethod(methodFactory.getDeleteMessage(
                chatId,
                callbackQuery.getMessage().getMessageId()));

        bot.executeTelegramApiMethod(methodFactory.getSendPhotoHtml(
                chatId,
                parts[1],
                formatProduct(product)
        ));

        return null;
    }

    private String formatProduct(Product product) {
        return String.format(
                """
                        📦 <b>%s</b>
                        💰 <b>%,.2f₽</b>
                        %s
                        """,
                product.getName(),
                product.getPrice(),
                (product.getDescription() != null ? "📝 " + product.getDescription() : "")
        );
    }
}
