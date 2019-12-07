package hibernateexample.database;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalDateTime;

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

    @NonNull
    @DateTimeFormat(pattern="dd.MM.yyyy, HH:mm")
    private LocalDateTime creationDate;

}
