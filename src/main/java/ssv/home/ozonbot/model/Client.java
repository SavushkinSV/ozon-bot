package ssv.home.ozonbot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "clients")
public class Client extends AbstractEntity {

    private String chatId;

    private String firstName;

    private String lastName;

    private String userName;

    private LocalDateTime registerAt;

}
