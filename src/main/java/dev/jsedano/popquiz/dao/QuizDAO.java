package dev.jsedano.popquiz.dao;

import dev.jsedano.popquiz.dto.QuestionDTO;
import dev.jsedano.popquiz.dto.QuizDTO;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

@RequiredArgsConstructor
@Service
public class QuizDAO {

  private final JedisPooled jedisPooled;

  public void saveQuiz(QuizDTO quiz) {
    jedisPooled.setex(String.format("quiz:%s:title", quiz.getUuid()), 60 * 60, quiz.getTitle());
    int i = 0;
    for (; i < quiz.getQuestions().size(); i++) {
      QuestionDTO question = quiz.getQuestions().get(i);
      if (Strings.isBlank(question.getQuestion())) {
        break;
      }
      jedisPooled.setex(
          String.format("quiz:%s:question:%d", quiz.getUuid(), i), 60 * 60, question.getQuestion());
      for (int j = 0; j < question.getAnswers().size(); j++) {
        jedisPooled.setex(
            String.format("quiz:%s:question:%d:answer:%d", quiz.getUuid(), i, j),
            60 * 60,
            question.getAnswers().get(j).getAnswer());
        jedisPooled.setex(
            String.format("quiz:%s:question:%d:answer:%d:is.correct", quiz.getUuid(), i, j),
            60 * 60,
            j == 0 ? "true" : "false");
      }
    }
    jedisPooled.setex(String.format("quiz:%s:size", quiz.getUuid()), 60 * 60, Integer.toString(i));
  }

  public QuizDTO getQuiz(String uuid) {
    QuizDTO.QuizDTOBuilder quizDTOBuilder = QuizDTO.builder().uuid(uuid);
    int size = Integer.parseInt(jedisPooled.get(String.format("quiz:%s:size", uuid)));
    quizDTOBuilder.size(size);
    /*todo get questions and answers */

    return quizDTOBuilder.build();
  }

  public void saveQuizTitle(QuizDTO quiz) {
    jedisPooled.setex(String.format("quiz:%s:title", quiz.getUuid()), 60 * 60, quiz.getTitle());
  }

  public void setQuestionSize(String uuid, int size) {
    jedisPooled.setex(String.format("quiz:%s:size", uuid), 60 * 60, Integer.toString(size));
  }

  public int getQuestionSize(String uuid) {
    return Integer.parseInt(jedisPooled.get(String.format("quiz:%s:size", uuid)));
  }

  public void addQuestion(QuestionDTO question, int questionIndex) {
    jedisPooled.setex(
        String.format("quiz:%s:question:%d", question.getParentQuizUuid(), questionIndex),
        60 * 60,
        question.getQuestion());
    for (int j = 0; j < question.getAnswers().size(); j++) {
      jedisPooled.setex(
          String.format(
              "quiz:%s:question:%d:answer:%d", question.getParentQuizUuid(), questionIndex, j),
          60 * 60,
          question.getAnswers().get(j).getAnswer());
      jedisPooled.setex(
          String.format(
              "quiz:%s:question:%d:answer:%d:is.correct",
              question.getParentQuizUuid(), questionIndex, j),
          60 * 60,
          j == 0 ? "true" : "false");
    }
  }
}
