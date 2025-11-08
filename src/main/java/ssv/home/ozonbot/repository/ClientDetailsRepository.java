package ssv.home.ozonbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssv.home.ozonbot.entity.client.ClientDetails;

@Repository
public interface ClientDetailsRepository extends JpaRepository<ClientDetails, Long> {

}
