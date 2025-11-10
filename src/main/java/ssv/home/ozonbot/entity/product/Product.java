package ssv.home.ozonbot.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import ssv.home.ozonbot.entity.AbstractEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    private Double price;

}
