package kz.baltabayev.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kz.baltabayev.util.StringUtils.SPACE;

@FetchProfile(name = "withCompanyAndPayment", fetchOverrides = {
        @FetchProfile.FetchOverride(
                entity = User.class,
                association = "company",
                mode = FetchMode.JOIN
        ),
        @FetchProfile.FetchOverride(
                entity = User.class,
                association = "payments",
                mode = FetchMode.JOIN
        )
})
@NamedQuery(name = "findUserByUserame", query = "select u from User u " +
                                                "where u.username = :username " +
                                                "order by u.username asc")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "userChats", "payments"})
@Entity
@Builder
@Table(name = "users", schema = "public")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Comparable<User>, BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Column(unique = true)
    private String username;

    @Convert(attributeName = "info", converter = JsonBinaryType.class)
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id") // названия нашей колонки в таблице 'users'
    private Company company;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserChat> userChats = new ArrayList<>();

    // 1 + N -> 1 + 5 -> 1 + 5/3 -> 3 (BatchSize)
    // 1 + N -> 1 + 1 -> 2 (@Fetch)
    @Builder.Default
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private Set<Payment> payments = new HashSet<>();

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    public String fullName() {
        return getPersonalInfo().getFirstname() + SPACE + getPersonalInfo().getLastname();
    }

}