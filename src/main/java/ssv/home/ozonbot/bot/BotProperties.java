package ssv.home.ozonbot.bot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "bot")
@Configuration
public class BotProperties {

    private String token;

    private String username;

}
