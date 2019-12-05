package hibernateexample.database;

import lombok.*;
import javax.persistence.*;

@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NonNull
    private Company company;

    @NonNull
    private String name;

    @NonNull
    @Column(length = 1000)
    private String about;

}
