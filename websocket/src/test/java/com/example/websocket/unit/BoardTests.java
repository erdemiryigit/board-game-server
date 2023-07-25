package com.example.websocket.unit;

import com.example.websocket.games.backgammon.domain.*;
import com.example.websocket.games.backgammon.service.BoardService;
import com.example.websocket.games.backgammon.service.CloningMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
public class BoardTests {

    @Test
    void testsOnInitialBoard(){

            Board board = new Board();
            board.setInitialPosition();
            Dice dice = new Dice();
            dice.setLeftDice(5);
            dice.setRightDice(3);
            board.setDice(dice);

        BoardService boardService = new BoardService();

            try {
                var moveList = boardService.findLegalMoves(board);
                assert(moveList.size() == 6);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        dice.setLeftDice(5);
        dice.setRightDice(5);
        board.setDice(dice);

        try {
            var moveList = boardService.findLegalMoves(board);
            assert(moveList.size() == 4);
        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }

    @Test
    void noLegalMove() {
        BoardService boardService = new BoardService();
        Board board = new Board();
        BoardState boardState = new BoardState();
        Checker.Color movingColor = Checker.Color.WHITE;

        Checker cw = new Checker(Checker.Color.WHITE);

        boardState.getBoardArray()[movingColor.getArrayIndex(movingColor.getBar())].getCheckers().add(cw);
        Checker cb1 = new Checker(Checker.Color.BLACK);
        Checker cb2 = new Checker(Checker.Color.BLACK);

        boardState.getBoardArray()[23].getCheckers().add(cb1);
        boardState.getBoardArray()[23].getCheckers().add(cb2);

        Dice dice = new Dice();
        dice.setLeftDice(1);
        dice.setRightDice(1);
        board.setDice(dice);

        board.setTurnToMove(Checker.Color.WHITE);


        board.setBoardState(boardState);

        try {
           var moveList = boardService.findLegalMoves(board);
            assert(moveList.size() == 1);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void oneDiePutMove() {
        BoardService boardService = new BoardService();

        Board board = new Board();
        BoardState boardState = new BoardState();
        Checker.Color movingColor = Checker.Color.WHITE;

        Checker cw1 = new Checker(Checker.Color.WHITE);
        Checker cw2 = new Checker(Checker.Color.WHITE);

        boardState.getBoardArray()[movingColor.getArrayIndex(movingColor.getBar())].getCheckers().add(cw1);
        boardState.getBoardArray()[movingColor.getArrayIndex(movingColor.getBar())].getCheckers().add(cw2);

        Checker cb1 = new Checker(Checker.Color.BLACK);
        Checker cb2 = new Checker(Checker.Color.BLACK);

        boardState.getBoardArray()[23].getCheckers().add(cb1);
        boardState.getBoardArray()[23].getCheckers().add(cb2);

        Dice dice = new Dice();
        dice.setLeftDice(1);
        dice.setRightDice(3);
        board.setDice(dice);

        board.setTurnToMove(Checker.Color.WHITE);
        board.setBoardState(boardState);

        try {
            var moveList = boardService.findLegalMoves(board);
            assert(moveList.size() == 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

}
