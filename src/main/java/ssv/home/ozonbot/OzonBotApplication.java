package ssv.home.ozonbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ssv.home.ozonbot.bot.BotProperties;

@SpringBootApplication
@EnableConfigurationProperties(BotProperties.class)
@EnableAspectJAutoProxy
public class OzonBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(OzonBotApplication.class, args);
    }

}
