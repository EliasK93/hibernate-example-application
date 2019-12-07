package hibernateexample.database;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @Column(length = 1000)
    private String about;

    @NonNull
    @DateTimeFormat(pattern="dd.MM.yyyy, HH:mm")
    private LocalDateTime creationDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "company")
    private List<Product> products;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Industry> industries;

}
