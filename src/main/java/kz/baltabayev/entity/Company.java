package kz.baltabayev.entity;

import com.sun.source.tree.Tree;
import lombok.*;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(exclude = "users")
//@ToString(exclude = "users")
@EqualsAndHashCode(of = "name")
@ToString(of = "users")
@Builder
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder.Default
//  @org.hibernate.annotations.OrderBy(clause = "username DESC, lastname ASC") // username и lastname поля наших таблиц
//  @OrderBy("username DESC, personalInfo.lastname ASC") // поля класса User
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "id") // только List и с типом Integer
//    private List<User> users = new ArrayList<>(); // лучше использовать Set, если ordering нужен на уровне прилоежния
    @SortNatural // in-memory Set/Map sorting
//    private Set<User> users = new TreeSet<>();
    private SortedSet<User> users = new TreeSet<>(); // PersistentSortedSet



    @ElementCollection
    @Builder.Default
    @CollectionTable(name = "company_locale", joinColumns = @JoinColumn(name = "company_id"))
//    @AttributeOverride(name = "lang", column = @Column(name = "language"))
    private List<LocaleInfo> locales = new ArrayList<>();


//    @OneToMany(mappedBy = "company")
//    @JoinColumn(name = "company_id")
//    private Set<User> users

    public void addUser(User user) {
        users.add(user);
        user.setCompany(this);
    }

}
