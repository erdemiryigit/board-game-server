package com.example.websocket.games.backgammon.domain;

import lombok.Data;

import java.util.List;

@Data
public class Point {
    private List<Checker> checkers;
}
