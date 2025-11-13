package com.tourlesjours.calendar.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MemberDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    Map<String, MemberDto> db = new HashMap<>();

    public Boolean isMember(String id) {

        String sql = "SELECT COUNT(*) FROM USER_MEMBER WHERE ID = ?";

        int result = jdbcTemplate.queryForObject(sql, Integer.class, id);

        if(result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int insertMember(MemberDto memberDto) {

        String sql = "INSERT INTO USER_MEMBER (ID, PW, MAIL, PHONE) VALUES (?, ?, ?, ?)";

        int result = -1;
        try {
            result = jdbcTemplate.update(sql,
                                        memberDto.getId(),
                                        memberDto.getPw(),
                                        memberDto.getMail(),
                                        memberDto.getPhone());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
