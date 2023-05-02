package dev.jsedano.popquiz.controller;

import dev.jsedano.popquiz.dto.QuestionDTO;
import dev.jsedano.popquiz.dto.QuizDTO;
import dev.jsedano.popquiz.service.QuizService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/quiz")
@RequiredArgsConstructor
@Slf4j
public class QuizController {

  private final QuizService quizService;


  @RequestMapping("/newV2")
  public String newQuizV2(Model model) {
    QuizDTO.QuizDTOBuilder quizDTOBuilder = QuizDTO.builder();
    List<QuestionDTO> questionDTOList = new LinkedList<>();
    for (int i = 0; i < 20; i++) {
      questionDTOList.add(QuestionDTO.builder().answers(new ArrayList<>(4)).build());
    }
    QuizDTO quizDTO = quizDTOBuilder.questions(questionDTOList).build();
    model.addAttribute("quiz", quizDTO);
    return "newQuizV2";
  }

  @RequestMapping("/saveQuiz")
  public String saveQuiz(Model model, QuizDTO quizDTO) {
    quizService.saveQuiz(quizDTO);
    model.addAttribute("quiz", quizService.getQuiz(quizDTO.getUuid()));
    return "quizDashboard";
  }

}
