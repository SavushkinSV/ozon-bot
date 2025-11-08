package ssv.home.ozonbot.entity.client;

import jakarta.persistence.*;
import lombok.*;
import ssv.home.ozonbot.entity.AbstractEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "client_details")
public class ClientDetails extends AbstractEntity {

    private UUID uuid;

    private String firstName;

    private String lastName;

    private String userName;

    private LocalDateTime registerAt;

}
