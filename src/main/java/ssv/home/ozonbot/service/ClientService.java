package ssv.home.ozonbot.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ssv.home.ozonbot.entity.client.Action;
import ssv.home.ozonbot.entity.client.Client;
import ssv.home.ozonbot.entity.client.ClientDetails;
import ssv.home.ozonbot.service.data.Role;
import ssv.home.ozonbot.repository.ClientDetailsRepository;
import ssv.home.ozonbot.repository.ClientRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final ClientDetailsRepository clientDetailsRepository;

    public Client save(Client client) {
        return repository.save(client);
    }

    public Client saveFromUser(User user) {
        ClientDetails clientDetails = ClientDetails.builder()
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .registerAt(LocalDateTime.now())
                .build();
        clientDetailsRepository.save(clientDetails);

        Client client = Client.builder()
                .chatId(user.getId())
                .action(Action.FREE)
                .role(Role.EMPTY)
                .clientDetails(clientDetails)
                .build();
        return save(client);
    }

    public Client findByChatId(Long chatId) {
        return repository.findByChatId(chatId).
                orElseThrow(() -> new EntityNotFoundException("Client not found with chatId: " + chatId));
    }

    public Client findByToken(String token) {
        return repository.findByToken(token);
    }

    public boolean existsByChatId(Long chatId) {
        return repository.existsByChatId(chatId);
    }

}
