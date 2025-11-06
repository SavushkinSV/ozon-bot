package ssv.home.ozonbot.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum BotCommandEnum {

    START("/start", "Начать работу"),
    HELP("/help", "Показать список команд"),
    PROFILE("/profile", "Ваш профиль"),
    PHOTOS("/photos", "Ваши фото");

    private final String command;
    private final String description;


    // возвращает список всех команд
    public static List<BotCommand> getAllCommands() {
        return Arrays.stream(BotCommandEnum.class.getEnumConstants())
                .map(cmd -> new BotCommand(cmd.command, cmd.description))
                .collect(Collectors.toList());
    }

}
