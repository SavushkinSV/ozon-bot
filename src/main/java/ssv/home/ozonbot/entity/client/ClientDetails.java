package ssv.home.ozonbot.entity.client;

import jakarta.persistence.*;
import lombok.*;
import ssv.home.ozonbot.entity.AbstractEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "client_details")
public class ClientDetails extends AbstractEntity {

    private String firstName;

    private String lastName;

    private String userName;

    private LocalDateTime registerAt;

}
