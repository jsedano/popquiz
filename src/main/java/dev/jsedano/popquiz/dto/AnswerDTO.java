package dev.jsedano.popquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AnswerDTO {
    private String parentQuizUuid;
    private int parentQuestion;
    private String answer;
    private Boolean correct;
}
