package ssv.home.ozonbot.proxy;

import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ssv.home.ozonbot.entity.client.Client;
import ssv.home.ozonbot.service.ClientService;
import ssv.home.ozonbot.service.factory.MethodFactory;

@Aspect
@Component
@AllArgsConstructor
public class ProfileCommandAspect {

    private final ClientService clientService;
    private final MethodFactory methodFactory;

    @Pointcut("execution(* ssv.home.ozonbot.service.handler.impl.ProfileCommandHandlerImpl.answer(..))")
    public void answerMethodPointcut() {
    }

    @Around("answerMethodPointcut()")
    public Object answerMethodAdvise(ProceedingJoinPoint joinPoint) throws Throwable {
        Message message = (Message) joinPoint.getArgs()[0];
        Client client = clientService.findByChatId(message.getChatId());


        if (client.getRole().isAuthenticated()) return joinPoint.proceed();

        return methodFactory.getSendMessageText(
                message.getChatId(),
                "Вы не авторизованы. Авторизуйтесь с помощью команды /login",
                null);
    }
}
