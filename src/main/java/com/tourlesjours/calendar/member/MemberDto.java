package com.tourlesjours.calendar.member;


import com.tourlesjours.calendar.member.jpa.AuthorityDto;
import com.tourlesjours.calendar.member.jpa.MemberEntity;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MemberDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int no;
    private String id;
    private String pw;
    private String mail;
    private String phone;
    private AuthorityDto authorityDto;
    private String reg_date;
    private String mod_date;

    public MemberEntity toEntity() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return MemberEntity.builder()
                .memNo(no)
                .memId(id)
                .memPw(pw)
                .memMail(mail)
                .memPhone(phone)
                .authorityEntity(authorityDto != null ? authorityDto.toEntity() : null)
                .memRegDate(reg_date != null ? LocalDateTime.parse(reg_date, formatter) : null)
                .memModDate(mod_date != null ? LocalDateTime.parse(mod_date, formatter) : null)
                .build();
    }

}
