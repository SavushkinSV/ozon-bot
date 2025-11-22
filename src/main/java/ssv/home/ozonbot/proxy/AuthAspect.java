package ssv.home.ozonbot.proxy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.client.Action;
import ssv.home.ozonbot.entity.client.Client;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.handler.auth.AuthHandler;

@Aspect
@Order(20)
@Component
@AllArgsConstructor
@Slf4j
public class AuthAspect {

    private final ClientService clientService;
    private final AuthHandler authHandler;

    @Pointcut("execution(* ssv.home.ozonbot.service.handler.command.AuthCommandHandler.answerMessage(..))")
    public void answerMethodPointcut() {
    }

    @Around("answerMethodPointcut()")
    public Object authMethodAdvise(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("AuthAspect authMethodAdvise");

        Message message = (Message) joinPoint.getArgs()[0];
        Client client = clientService.findByChatId(message.getChatId());

        if (client.getRole().isAuthenticated()) {
            return joinPoint.proceed();
        }
        if (client.getAction() == Action.AUTH) {
            return joinPoint.proceed();
        }

        return authHandler.answerMessage(message,
                (TelegramBot) joinPoint.getArgs()[1]);
    }
}
