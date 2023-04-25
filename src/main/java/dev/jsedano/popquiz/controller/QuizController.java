package dev.jsedano.popquiz.controller;

import dev.jsedano.popquiz.dto.QuestionDTO;
import dev.jsedano.popquiz.dto.QuizDTO;
import dev.jsedano.popquiz.service.QuizService;
import java.util.ArrayList;
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

  @RequestMapping("/new")
  public String newQuiz(Model model) {
    model.addAttribute("quiz", QuizDTO.builder().build());
    return "newQuiz";
  }

  @RequestMapping("/title")
  public String saveQuizTitle(QuizDTO quizDTO, Model model) {
    model.addAttribute(
        "question", QuestionDTO.builder().parentQuizUuid(quizService.initQuiz(quizDTO)).build());
    return "addQuestion";
  }

  @RequestMapping("/addQuestion")
  public String addQuestion(@RequestParam String action, QuestionDTO questionDTO, Model model) {
    quizService.addQuestion(questionDTO);
    model.addAttribute(
        "question",
        QuestionDTO.builder()
            .answers(new ArrayList<>(4))
            .parentQuizUuid(questionDTO.getParentQuizUuid())
            .build());
    if ("continue".equals(action)) {
      return "addQuestion";
    } else {
      return "redirect:/quiz/new";
    }
  }
}
