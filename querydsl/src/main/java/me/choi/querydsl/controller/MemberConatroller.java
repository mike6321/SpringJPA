package me.choi.querydsl.controller;

import lombok.RequiredArgsConstructor;
import me.choi.querydsl.dto.MemberSearchCondition;
import me.choi.querydsl.dto.MemberTeamDto;
import me.choi.querydsl.repository.MemberJpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberConatroller {

    private final MemberJpaRepository memberJpaRepository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return memberJpaRepository.search(condition);
    }
}
