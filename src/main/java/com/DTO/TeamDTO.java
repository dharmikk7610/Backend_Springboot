package com.DTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.Entity.Hackathon;

import lombok.Data;

@Data
public class TeamDTO {
    private UUID teamId;
    private String teamName;
    private Hackathon hackathon;
    private List<MemberDTO> members;
    private LocalDateTime createdAt;
    private String inviteToken;
    private UUID leaderId;
}

