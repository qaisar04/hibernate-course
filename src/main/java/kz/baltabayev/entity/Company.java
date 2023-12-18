package kz.baltabayev.entity;

import lombok.*;
import org.hibernate.annotations.SortNatural;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(of = "name")
@Builder
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "username")
    @SortNatural
    private SortedMap<String, User> users = new TreeMap<>(); // PersistentSortedSet



    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locale", joinColumns = @JoinColumn(name = "company_id"))
    @MapKeyColumn(name = "lang") // для указания ключа Map
    private Map<String, String> locales = new HashMap<>(); // key - lang, val - desc


    public void addUser(User user) {
        users.put(user.getUsername(), user);
        user.setCompany(this);
    }

}
