package ssv.home.ozonbot.proxy;

import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.service.ClientService;

/**
 * Возможно удалить и использовать только в классе StartCommandHandlerImpl
 */
@Aspect
@Order(10)
@Component
@AllArgsConstructor
public class ClientCreationAspect {

    private final ClientService clientService;

    @Pointcut("execution(* ssv.home.ozonbot.service.handler.impl.StartCommandHandlerImpl.answer(..))")
    public void answerMethodPointcut() {
    }

    @Around("answerMethodPointcut()")
    public Object answerMethodAdvise(ProceedingJoinPoint joinPoint) throws Throwable {
        Message message = (Message) joinPoint.getArgs()[0];

        if (clientService.existsByChatId(message.getChatId())) {
            return joinPoint.proceed();
        }
        clientService.saveFromUser(message.getFrom());

        return joinPoint.proceed();
    }

}
