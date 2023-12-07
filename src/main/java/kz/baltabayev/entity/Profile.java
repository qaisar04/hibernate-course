package kz.baltabayev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
//    @JoinColumn(name = "user_id")
    @PrimaryKeyJoinColumn(name = "user_id")
    private User user;

    private String street;

    private String language;

    public void setUser(User user) {
        user.setProfile(this);
        this.user = user;
        this.id = user.getId();
    }
}