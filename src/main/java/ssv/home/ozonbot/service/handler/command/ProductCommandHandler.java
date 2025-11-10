package ssv.home.ozonbot.service.handler.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.product.Product;
import ssv.home.ozonbot.service.ProductService;
import ssv.home.ozonbot.service.data.Command;
import ssv.home.ozonbot.service.factory.KeyboardFactory;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.handler.CommandHandler;

import java.util.List;

@Component
@AllArgsConstructor
public class ProductCommandHandler implements CommandHandler {

    private final MethodFactory methodFactory;
    private final KeyboardFactory keyboardFactory;
    private final ProductService productService;

    @Override
    public BotApiMethod<?> answerMessage(Message message, TelegramBot bot) {
        Long chatId = message.getChatId();
        List<Product> productList = productService.findAll();

        if (productList.isEmpty()) {
            return methodFactory.getSendMessageText(
                    chatId,
                    "К сожалению, товаров пока нет в наличии.",
                    null
            );
        }

        return methodFactory.getSendMessageHtml(
                chatId,
                "<b>Каталог наших товаров:</b>\n\n",
                getProductKeyboard(productList)
        );
    }

    @Override
    public String getCommand() {
        return Command.PRODUCT.getCommand();
    }

    private InlineKeyboardMarkup getProductKeyboard(List<Product> productList) {
        List<String> nameList = productList.stream()
                .map(Product::getName)  // извлекаем имя каждого продукта
                .toList();  // собираем в новый список

        List<Integer> configtList = productList.stream()
                .map(product -> 1)
                .toList();

        List<String> dataList = productList.stream()
                .map(product -> "product:" + product.getId())
                .toList();

        return keyboardFactory.getInlineKeyboard(
                nameList,
                configtList,
                dataList
        );
    }

}
