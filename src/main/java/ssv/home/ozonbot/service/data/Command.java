package ssv.home.ozonbot.service.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Описывает команды Telegram‑бота и предоставляет удобный способ работать с ними в коде.
 * Каждая строка задаёт одну команду бота.
 */

@Getter
@AllArgsConstructor
public enum Command {

    START("/start", "Начать работу"),
    HELP("/help", "Показать список команд"),
    PROFILE("/profile", "Ваш профиль"),
    PHOTOS("/photos", "Ваши фото");

    private final String command;
    private final String description;

    /**
     * Возвращает список всех команд Telegram‑бота
     *
     * @return список команд
     */
    public static List<BotCommand> getAllCommands() {
        return Arrays.stream(Command.class.getEnumConstants())
                .map(cmd -> new BotCommand(cmd.command, cmd.description))
                .collect(Collectors.toList());
    }

}
