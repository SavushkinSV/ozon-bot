package ssv.home.ozonbot.service.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("admin", "Администратор"),
    CUSTOMER("customer", "Покупатель"),
    EMPLOYEE("employee", "Сотрудник"),
    EMPTY("empty", "Нет роли");

    private final String code;
    private final String displayName;

    public static Role fromCode(String code) {
        return Arrays.stream(values())
                .filter(r -> r.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role code: " + code));
    }

    /**
     * Проверяет, прошёл ли клиент аутентификацию.
     * <p>
     * Метод определяет статус аутентификации клиента на основе его роли. Клиент считается
     * аутентифицированным, если его роль не равна {@link Role#EMPTY} (назначена конкретная роль в системе).
     *
     * @return {@code true}, если клиент аутентифицирован (роль не {@code EMPTY});
     * {@code false}, если клиент не прошёл аутентификацию (роль {@code EMPTY}).
     */
    public boolean isAuthenticated() {
        return this != EMPTY;
    }

    public static List<String> getDisplayNames() {
        return Arrays.stream(values())
                .filter(role -> role != EMPTY && role != ADMIN)
                .map(Role::getDisplayName)
                .toList();
    }

    public static List<String> getCallbackCodes() {
        return Arrays.stream(values())
                .filter(role -> role != EMPTY && role != ADMIN)
                .map(Role::getCode)
                .toList();
    }

    public static List<String> getAuthCallbackCodes() {
        return getCallbackCodes().stream()
                .map(code -> "auth:" + code)  // добавляем префикс к каждому коду
                .toList();
    }
}

