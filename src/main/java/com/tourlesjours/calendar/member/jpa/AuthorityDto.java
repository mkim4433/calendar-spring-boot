package com.tourlesjours.calendar.member.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDto {

    private int no;
    private String role_name;

    public AuthorityEntity toEntity() {
        return AuthorityEntity.builder()
                .authNo((byte) no)
                .authRoleName(role_name)
                .build();
    }
}



