package com.DTO;

import lombok.Data;

@Data
public class EvaluationDTO {
    private String submissionId;
    private String judgeId;
    private Integer innovationScore;
    private Integer technicalScore;
    private Integer presentationScore;
    private String feedback;

    // getters & setters
}
