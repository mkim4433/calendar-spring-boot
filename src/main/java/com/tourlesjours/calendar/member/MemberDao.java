package com.tourlesjours.calendar.member;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MemberDao {

    Map<String, MemberDto> db = new HashMap<>();

    public Boolean isMember(String id) {

        return false;
    }

    public int insertMember(MemberDto memberDto) {

        return 1;
    }
}
