package me.choi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {

    private final Long id;
    private final String username;
    private final String teamName;
}
