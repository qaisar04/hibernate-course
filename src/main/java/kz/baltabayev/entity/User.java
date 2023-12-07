package kz.baltabayev.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "profile"})
@Entity
@TypeDef(name = "qaisar", typeClass = JsonBinaryType.class)
@Table(name = "users", schema = "public")
public class User {

//    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_id_generator") // самый оптимальный вариант
////  @GeneratedValue(strategy = GenerationType.SEQUENCE)
////  @SequenceGenerator(name = "user_id_generator", sequenceName = "users_id_seq", allocationSize = 1)
////  hibernate_sequence (default for 'name' @SequenceGenerator)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//  @Id
//  @Embedded (not necessary)
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Column(unique = true)
    private String username;

//    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType") // + регистрируем в Configuration
//    @Type(type = "jsonb")
    @Type(type = "qaisar")
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;

//    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id") // названия нашей колонки в таблице 'users'
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;
}

// POJO - Plain Old Java Object
