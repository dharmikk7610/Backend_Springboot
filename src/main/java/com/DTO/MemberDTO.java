package com.DTO;

import java.util.UUID;

import lombok.Data;

@Data
public class MemberDTO {
    private UUID userId;
    private String name;
    private String email;
    private String roleInTeam;
}

