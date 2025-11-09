package ssv.home.ozonbot.entity.client;

import jakarta.persistence.*;
import lombok.*;
import ssv.home.ozonbot.entity.AbstractEntity;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "client")
public class Client extends AbstractEntity {

    private Long chatId;

    @Column(unique = true, nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Action action;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_details_id")
    private ClientDetails clientDetails;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"),
            name = "relationships"
    )
    private List<Client> clients;

    @PrePersist
    private void generateToken() {
        if (token == null) {
            token = UUID.randomUUID().toString();
        }
    }

}
