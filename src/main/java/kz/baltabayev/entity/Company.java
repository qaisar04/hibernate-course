package kz.baltabayev.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

//    @OneToMany(mappedBy = "company")
//    @JoinColumn(name = "company_id")
//    private Set<User> users

    public void addUser(User user) {
        users.add(user);
        user.setCompany(this);
    }

}
