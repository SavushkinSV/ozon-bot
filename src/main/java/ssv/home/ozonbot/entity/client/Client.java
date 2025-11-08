package ssv.home.ozonbot.entity.client;

import jakarta.persistence.*;
import lombok.*;
import ssv.home.ozonbot.entity.AbstractEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "client")
public class Client extends AbstractEntity {

    private Long chatId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Action action;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_details_id")
    private ClientDetails clientDetails;

}
