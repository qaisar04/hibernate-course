package kz.baltabayev.dto;

import kz.baltabayev.entity.PersonalInfo;
import kz.baltabayev.entity.Role;

public record UserReadDto(Long id,
                          PersonalInfo personalInfo,
                          String username,
                          String info,
                          Role role,
                          CompanyReadDto companyReadDto) {


}
