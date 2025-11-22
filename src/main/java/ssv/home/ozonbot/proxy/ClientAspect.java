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
import ssv.home.ozonbot.service.ClientService;

@Aspect
@Order(10)
@Component
@AllArgsConstructor
@Slf4j
public class ClientAspect {

    private final ClientService clientService;

    @Pointcut("execution(* ssv.home.ozonbot.service.handler.command.StartCommandHandler.answerMessage(..))")
    public void answerMessageStartCommandPointcut() {
    }

    @Pointcut("execution(* ssv.home.ozonbot.service.handler.command.ProfileCommandHandler.answerMessage(..))")
    public void answerMessageProfileCommandPointcut() {
    }

    @Pointcut("execution(* ssv.home.ozonbot.service.handler.command.AuthCommandHandler.answerMessage(..))")
    public void answerMessageLoginCommandPointcut() {
    }

    @Pointcut("answerMessageStartCommandPointcut() || answerMessageProfileCommandPointcut() || answerMessageLoginCommandPointcut()")
    public void trackCommandHandlers() {}


    @Around("trackCommandHandlers()")
    public Object addNewClientAdvise(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("ClientAspect addNewClientAdvise");
        Message message = (Message) joinPoint.getArgs()[0];

        if (!clientService.existsByChatId(message.getChatId())) {
            clientService.saveFromUser(message.getFrom());
        }
        return joinPoint.proceed();
    }

}
