package ssv.home.ozonbot.proxy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.bot.TelegramBot;
import ssv.home.ozonbot.entity.client.Action;
import ssv.home.ozonbot.entity.client.Client;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.manager.auth.AuthManager;

@Aspect
@Component
@AllArgsConstructor
@Slf4j
public class AuthAspect {

    private final ClientService clientService;
    private final AuthManager authManager;
    private final MethodFactory methodFactory;

    @Pointcut("execution(* ssv.home.ozonbot.service.handler.command.LoginCommandHandler.answerMessage(..))")
    public void answerMethodPointcut() {
    }

    @Around("answerMethodPointcut()")
    public Object authMethodAdvise(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("AuthAspect " + "authMethodAdvise");

        Message message = (Message) joinPoint.getArgs()[0];
        Client client = clientService.findByChatId(message.getChatId());

        if (client.getRole().isAuthenticated()) {
            if (client.getAction() == Action.FREE) {
                return methodFactory.getSendMessageText(message.getChatId(), "Вы уже авторизованы.", null);
            }
            return joinPoint.proceed();
        }

        if (client.getAction() == Action.AUTH) {
            return joinPoint.proceed();
        }

        return authManager.answerMessage(message,
                (TelegramBot) joinPoint.getArgs()[1]);
    }
}
