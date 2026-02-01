package com.tourlesjours.calendar.member.security;

import com.tourlesjours.calendar.member.jpa.MemberEntity;
import com.tourlesjours.calendar.member.jpa.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<MemberEntity> optionalMember = memberRepository.findByMemId(username);
        if (optionalMember.isPresent()) {
            MemberEntity foundMember = optionalMember.get();
            return User.builder()
                    .username(foundMember.getMemId())
                    .password(foundMember.getMemPw())
                    .roles(foundMember.getAuthorityEntity().getAuthRoleName())
                    .build();
        }

        return null;
    }
}
