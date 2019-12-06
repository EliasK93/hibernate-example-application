package hibernateexample.database;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Data
public class Industry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @Column(length = 1000)
    private String about;

    @NonNull
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "industries")
    private List<Company> companies;

}
