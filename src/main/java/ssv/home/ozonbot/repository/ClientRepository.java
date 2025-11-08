package ssv.home.ozonbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ssv.home.ozonbot.entity.client.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findClientByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

}
