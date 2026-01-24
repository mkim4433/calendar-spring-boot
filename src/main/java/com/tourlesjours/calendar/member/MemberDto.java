package com.tourlesjours.calendar.member;


import lombok.*;

import java.io.Serializable;

//@Getter
//@Setter
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
    private int authority_no;
    private String reg_date;
    private String mod_date;

}
