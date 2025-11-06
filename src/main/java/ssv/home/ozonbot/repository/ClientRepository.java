package ssv.home.ozonbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssv.home.ozonbot.model.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findClientByChatId(String chatId);

    boolean existsByChatId(String chatId);

}
