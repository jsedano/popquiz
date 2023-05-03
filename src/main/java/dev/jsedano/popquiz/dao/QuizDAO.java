package dev.jsedano.popquiz.dao;

import dev.jsedano.popquiz.dto.AnswerDTO;
import dev.jsedano.popquiz.dto.QuestionDTO;
import dev.jsedano.popquiz.dto.QuizDTO;
import java.util.LinkedList;
import java.util.List;
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
    quizDTOBuilder.title(jedisPooled.get(String.format("quiz:%s:title", uuid)));
    int size = Integer.parseInt(jedisPooled.get(String.format("quiz:%s:size", uuid)));
    quizDTOBuilder.size(size);

    List<QuestionDTO> questionDTOList = new LinkedList<>();
    for (int i = 0; i < size; i++) {
      QuestionDTO.QuestionDTOBuilder questionDTOBuilder = QuestionDTO.builder();
      questionDTOBuilder.question(jedisPooled.get(String.format("quiz:%s:question:%d", uuid, i)));
      List<AnswerDTO> answerDTOList = new LinkedList<>();
      for (int j = 0; j < 4; j++) {
        AnswerDTO.AnswerDTOBuilder answerDTOBuilder = AnswerDTO.builder();
        answerDTOBuilder.answer(
            jedisPooled.get(String.format("quiz:%s:question:%d:answer:%d", uuid, i, j)));
        answerDTOBuilder.correct(
            Boolean.parseBoolean(
                jedisPooled.get(
                    String.format("quiz:%s:question:%d:answer:%d:is.correct", uuid, i, j))));
        answerDTOList.add(answerDTOBuilder.build());
      }
      questionDTOBuilder.answers(answerDTOList);
      questionDTOList.add(questionDTOBuilder.build());
    }
    quizDTOBuilder.questions(questionDTOList);
    return quizDTOBuilder.build();
  }
}
