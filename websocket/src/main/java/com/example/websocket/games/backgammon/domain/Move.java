package com.example.websocket.games.backgammon.domain;


public class Move {

    public Move(MoveType moveType, Integer from, Integer to, BoardState preState) {
        this.moveType = moveType;
        this.from = from;
        this.to = to;
        this.preState = preState;
    }

    public enum MoveType {
        CARRY,
        BEAR,
        PUT,
        NILL,
        TERMINATE
    }

    private boolean isHit;
    private final Integer from;
    private final Integer to;

    public BoardState getPreState() {
        return preState;
    }

    private final BoardState preState;

    private BoardState postState;



    private Move nextMove;


    private final MoveType moveType;

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }



    public MoveType getMoveType() {
        return moveType;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public BoardState getPostState() {
        return postState;
    }

    public void setPostState(BoardState postState) {
        this.postState = postState;
    }

    public Move getNextMove() {
        return nextMove;
    }

    public void setNextMove(Move nextMove) {
        this.nextMove = nextMove;
    }



}
