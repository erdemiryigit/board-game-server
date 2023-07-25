package com.example.websocket.games.backgammon.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


public class Board {
    private static final Logger logger = LoggerFactory.getLogger(Board.class);





    private Checker.Color turnToMove;

    public Checker.Color getTurnToMove() {
        return turnToMove;
    }

    public void setTurnToMove(Checker.Color turnToMove) {
        this.turnToMove = turnToMove;
    }



    private Dice dice;
    public Dice getDice() {
        return dice;
    }
    public void setDice(Dice dice) {
         this.dice = dice;
    }
    public BoardState getBoardState() {
        return boardState;
    }

    public void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }

    private BoardState boardState;

    private static Random RND = new Random();

    private Player player1;
    private Player player2;
    private String id;

    public void setInitialPosition() {
        this.turnToMove = Checker.Color.WHITE;
        BoardState boardState = BoardState.generateInitialState();
        this.boardState = boardState;

    }


    public void  rollDice()  {
        Dice dice = new Dice();
        dice.setLeftDice(RND.nextInt(1, 7));
        dice.setRightDice(RND.nextInt(1, 7));

        this.dice = dice;
    }



}
