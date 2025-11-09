package ssv.home.ozonbot.entity.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("admin", "Администратор"),
    STUDENT("student", "Ученик"),
    TEACHER("teacher", "Учитель"),
    EMPTY("empty", "Нет роли");

    private final String code;
    private final String displayName;

    public static Role fromCode(String code) {
        return Arrays.stream(values())
                .filter(r -> r.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role code: " + code));
    }

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
                .map(code -> "auth_" + code)  // добавляем префикс к каждому коду
                .toList();
    }
}

