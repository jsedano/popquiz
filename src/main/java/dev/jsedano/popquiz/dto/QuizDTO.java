package dev.jsedano.popquiz.dto;

import java.util.List;
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
public class QuizDTO {

  private String uuid;
  private String title;
  private Integer size;
  private List<QuestionDTO> questions;
}
