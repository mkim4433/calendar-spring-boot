package com.tourlesjours.calendar.member;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
@Data

public class MemberDto {

    private int no;
    private String id;
    private String pw;
    private String mail;
    private String phone;
    private int autority_no;
    private String reg_date;
    private String mod_date;

}
