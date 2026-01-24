package com.tourlesjours.calendar.member.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_MEMBER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// USER_MEMBER 테이블과 1:1 매칭되는 자바 객체.
public class MemberEntity {

    @Id
    @Column(name = "NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memNo;

    @Column(name = "ID", nullable = false, length = 20)
    private String memId;

    @Column(name = "PW", nullable = false, length = 100)
    private String memPw;

    @Column(name = "MAIL", nullable = false, length = 20)
    private String memMail;

    @Column(name = "PHONE", nullable = false, length = 20)
    private String memPhone;

    @Column(name = "AUTHORITY_NO")
    private int memAuthorityNo;

    @Column(name = "REG_DATE", updatable = false)
    private LocalDateTime memRegDate;

    @Column(name = "MOD_DATE")
    private LocalDateTime memModDate;

    @PrePersist
    protected void onCreate() {
        this.memAuthorityNo = 1;
        this.memRegDate = LocalDateTime.now();
        this.memModDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.memModDate = LocalDateTime.now();
    }

}
