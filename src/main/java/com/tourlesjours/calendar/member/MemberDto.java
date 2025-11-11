package com.tourlesjours.calendar.member;

public class MemberDto {

    private int no;
    private String id;
    private String pw;
    private String mail;
    private String phone;
    private int autority_no;
    private String reg_date;
    private String mod_date;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAutority_no() {
        return autority_no;
    }

    public void setAutority_no(int autority_no) {
        this.autority_no = autority_no;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getMod_date() {
        return mod_date;
    }

    public void setMod_date(String mod_date) {
        this.mod_date = mod_date;
    }
}
