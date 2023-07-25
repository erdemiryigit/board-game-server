package com.example.websocket.games.backgammon.domain;

import lombok.Data;

import java.util.List;

@Data
public class Player {
    private Long id;
    private Checker.Color color;

    private List<Checker> checkers;

}
