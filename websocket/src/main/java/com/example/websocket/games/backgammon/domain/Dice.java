package com.example.websocket.games.backgammon.domain;

import lombok.Data;

@Data
public class Dice {
    private int leftDice;
    private int rightDice;


    public boolean isDouble() {
        return this.leftDice == this.rightDice;
    }
}
