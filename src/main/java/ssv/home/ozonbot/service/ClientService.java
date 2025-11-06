package ssv.home.ozonbot.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ssv.home.ozonbot.model.Client;
import ssv.home.ozonbot.repository.ClientRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    public Client create(Client client) {
        return repository.save(client);
    }

    public Client createFromUser(User user, String chatId) {
        Client client = repository.findClientByChatId(chatId)
                .orElse(new Client());
        if (client.getId() == null) {
            client.setChatId(chatId);
            client.setFirstName(user.getFirstName());
            client.setLastName(user.getLastName());
            client.setUserName(user.getUserName());
            client.setRegisterAt(LocalDateTime.now());
        }
        return create(client);
    }

    public Client getByChatId(String chatId) {
        return repository.findClientByChatId(chatId).
                orElseThrow(() -> new EntityNotFoundException("Client not found with chatId: " + chatId));
    }

    public boolean existsByChatId(String chatId) {
        return repository.existsByChatId(chatId);
    }

}
