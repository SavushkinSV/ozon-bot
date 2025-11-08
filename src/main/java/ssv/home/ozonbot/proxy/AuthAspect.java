package ssv.home.ozonbot.proxy;

import lombok.AllArgsConstructor;
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
import ssv.home.ozonbot.entity.client.Role;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.factory.MethodFactory;
import ssv.home.ozonbot.service.manager.auth.AuthManager;

@Aspect
@Order(100)
@Component
@AllArgsConstructor
public class AuthAspect {

    private final ClientService clientService;
    private final AuthManager authManager;
    private final MethodFactory methodFactory;

    @Pointcut("execution(* ssv.home.ozonbot.service.handler.impl.LoginCommandHandlerImpl.answer(..))")
    public void answerMethodPointcut() {
    }

    @Around("answerMethodPointcut()")
    public Object authMethodAdvise(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("authMethodAdvise");
        Message message = (Message) joinPoint.getArgs()[0];
        Client client = clientService.getByChatId(message.getChatId());

        if (client.getRole() != Role.EMPTY) {
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
