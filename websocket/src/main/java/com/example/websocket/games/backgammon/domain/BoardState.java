package com.example.websocket.games.backgammon.domain;

import com.example.websocket.games.backgammon.service.CloningMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardState {

    private Point[] boardArray = new Point[28];

    private static CloningMapper cloningMapper = CloningMapper.INSTANCE;

    public BoardState() {
        for (int i = 0; i < this.boardArray.length; i++) {
            this.boardArray[i] = new Point();
            this.boardArray[i].setCheckers(new ArrayList<Checker>());
        }
    }

    public BoardState copy() {
        return cloningMapper.cloneBoardState(this);
    }


    public static BoardState generateInitialState() {
        BoardState initialState = new BoardState();


        for (int i = 0; i < 15; i++) {
            Checker cw = new Checker(Checker.Color.WHITE);

            Checker cb = new Checker(Checker.Color.BLACK);


            if (List.of(0, 1).contains(i)) {
                initialState.getBoardArray()[23].getCheckers().add(cw);
                initialState.getBoardArray()[0].getCheckers().add(cb);
            }
            if (List.of(2, 3, 4, 5, 6).contains(i)) {
                initialState.getBoardArray()[12].getCheckers().add(cw);
                initialState.getBoardArray()[11].getCheckers().add(cb);
            }
            if (List.of(7, 8, 9).contains(i)) {
                initialState.getBoardArray()[7].getCheckers().add(cw);
                initialState.getBoardArray()[16].getCheckers().add(cb);
            }
            if (List.of(10, 11, 12, 13, 14).contains(i)) {
                initialState.getBoardArray()[5].getCheckers().add(cw);
                initialState.getBoardArray()[18].getCheckers().add(cb);
            }

        }

        return initialState;
    }

}
