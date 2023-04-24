package dev.jsedano.popquiz.dao;

import dev.jsedano.popquiz.dto.QuestionDTO;
import dev.jsedano.popquiz.dto.QuizDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

@RequiredArgsConstructor
@Service
public class QuizDAO {

  private final JedisPooled jedisPooled;
/*
  public void saveQuiz(QuizDTO quiz) {
    jedisPooled.setex(String.format("quiz:%s:title", quiz.getUuid()), 60 * 60, quiz.getTitle());
    for (int i = 0; i < quiz.getQuestions().size(); i++) {
      jedisPooled.setex(
          String.format("quiz:%s:question:%d", quiz.getUuid(), i),
          60 * 60,
          quiz.getQuestions().get(i).getQuestion());
      var answerList = quiz.getQuestions().get(i).getAnswers().entrySet().stream().toList();
      for (int j = 0; j < answerList.size(); j++) {
        jedisPooled.setex(
            String.format("quiz:%s:question:%d:answer:%d", quiz.getUuid(), i, j),
            60 * 60,
            answerList.get(j).getKey());
        jedisPooled.setex(
            String.format("quiz:%s:question:%d:answer:%d:is.correct", quiz.getUuid(), i, j),
            60 * 60,
            answerList.get(j).getValue() ? "true" : "false");
      }
    }
  }
 */

  public void saveQuizTitle(QuizDTO quiz) {
    jedisPooled.setex(String.format("quiz:%s:title", quiz.getUuid()), 60 * 60, quiz.getTitle());
  }

  public void setQuestionSize(String uuid, int size) {
    jedisPooled.setex(
        String.format("quiz:%s:size", uuid), 60 * 60, Integer.toString(size));
  }

  public int getQuestionSize(String uuid) {
    return Integer.parseInt(jedisPooled.get(String.format("quiz:%s:size", uuid)));
  }

  public void addQuestion(QuestionDTO question, int questionIndex) {
    jedisPooled.setex(
        String.format("quiz:%s:question:%d", question.getParentQuizUuid(), questionIndex),
        60 * 60,
        question.getQuestion());
  }
}
