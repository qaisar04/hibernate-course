package kz.baltabayev.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kz.baltabayev.entity.PersonalInfo;
import kz.baltabayev.entity.Role;
import kz.baltabayev.validation.UpdateCheck;

public record UserCreateDto(@Valid PersonalInfo personalInfo,
                            @NotNull String username,
                            String info,
                            @NotNull(groups = UpdateCheck.class) Role role,
                            Integer companyId) {
}
