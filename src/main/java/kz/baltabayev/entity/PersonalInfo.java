package kz.baltabayev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String firstname;
    private String lastname;

//    @Convert(converter = BirthdayConverter.class) // либо указываем при создании Configuration
//    @Column(name = "birth_date")
    private LocalDate birthDate;

}
