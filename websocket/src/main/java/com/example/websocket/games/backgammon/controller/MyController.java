package com.example.websocket.games.backgammon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
@Slf4j
public class MyController {

    private final SimpMessagingTemplate template;

    @RequestMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }


    @GetMapping("hello")
    public String getHello() {
        return "mytemplate";
    }

    @MessageMapping("board/{id}")
    public void greeting(@DestinationVariable("id") String boardId, String message) throws Exception {
        log.info(" board {} /wshello got message {}", boardId, message);
        // check if board exists
        // authenticate user if belongs to this board
        //Greeting greeting = new Greeting("Hello, " + HtmlUtils.htmlEscape(message.name() + "!"));
        this.template.convertAndSend("/topic/board/" + boardId, "ali");
    }

}
