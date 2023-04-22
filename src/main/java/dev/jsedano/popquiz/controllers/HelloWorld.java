package dev.jsedano.popquiz.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloWorld {

    @RequestMapping("/index.html")
    public String helloWorld() {
        return "index";
    }
}
