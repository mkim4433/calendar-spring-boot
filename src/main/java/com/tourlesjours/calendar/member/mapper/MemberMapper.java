package com.tourlesjours.calendar.member.mapper;

import com.tourlesjours.calendar.member.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    public Boolean isMember(String id);

    public int insertMember(MemberDto memberDto);

    public MemberDto selectMemberById(String id);

    public int updateMember(MemberDto memberDto);

    public MemberDto selectMemberByIdAndMail(MemberDto memberDto);

    public int updatePassword(@Param("memId") String id, @Param("memPw") String encodedNewPw);
}
