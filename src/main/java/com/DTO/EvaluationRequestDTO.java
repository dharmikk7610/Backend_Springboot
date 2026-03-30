package com.DTO;

import lombok.Data;

@Data
public class EvaluationRequestDTO {

	private Integer innovationScore;
	private Integer technicalScore;
	private Integer presentationScore;
	private String feedback;

}
