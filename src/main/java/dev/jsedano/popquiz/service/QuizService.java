package dev.jsedano.popquiz.service;

import dev.jsedano.popquiz.dao.QuizDAO;
import dev.jsedano.popquiz.dto.QuestionDTO;
import dev.jsedano.popquiz.dto.QuizDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

  private final QuizDAO quizDAO;

  public String initQuiz(QuizDTO quizDTO) {
    quizDTO.setUuid(UUID.randomUUID().toString());
    quizDAO.saveQuizTitle(quizDTO);
    quizDAO.setQuestionSize(quizDTO.getUuid(), 0);
    return quizDTO.getUuid();
  }

  public void addQuestion(QuestionDTO questionDTO) {
    int questionSize = quizDAO.getQuestionSize(questionDTO.getParentQuizUuid());
    quizDAO.addQuestion(questionDTO, questionSize);
    quizDAO.setQuestionSize(questionDTO.getParentQuizUuid(), questionSize + 1);
  }

  public void saveQuiz(QuizDTO quizDTO) {
    quizDTO.setUuid(UUID.randomUUID().toString());
    quizDAO.saveQuiz(quizDTO);
  }

  public QuizDTO getQuiz(String uuid) {
    return quizDAO.getQuiz(uuid);
  }
}
