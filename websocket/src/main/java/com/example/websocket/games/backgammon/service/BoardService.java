package com.example.websocket.games.backgammon.service;

import com.example.websocket.games.backgammon.domain.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    private  final CloningMapper cloningMapper = CloningMapper.INSTANCE;

    public  List<Move> findLegalMoves(Board board) throws Exception {

        int[] dice = null;
        if (board.getDice().isDouble()) {
            dice = new int[4];
            for (int i = 0; i < 4; i++) {
                dice[i] = board.getDice().getRightDice();
            }
        } else {
            dice = new int[2];
            dice[0] = Math.max(board.getDice().getLeftDice(),board.getDice().getRightDice());
            dice[1] = Math.min(board.getDice().getLeftDice(),board.getDice().getRightDice());;
        }
        List<Move> moveList1 = findLegalMovesForDiceSequence(board,dice);
        if(!board.getDice().isDouble()) {
            int temp = dice[0];
            dice[0] = dice[1];
            dice[1] = temp;
            var  moveList2 = findLegalMovesForDiceSequence(board,dice);
            moveList1.addAll(moveList2);
        }



        moveList1 = removeDuplicates(moveList1);

        if(dice.length == 2) {
            int maxPlay  = 2;
            boolean totallyPlayable = moveList1.stream().anyMatch(m -> {
                m.getMoveType() != Move.MoveType.NILL &&
                        (m.getNextMove() != null && ( m.getNextMove().getMoveType() !=
            })

        }
    }


    private List<Move>  removeDuplicates(List<Move> moveList) {

        // remove duplicates
        Set<Move> moveSet = new TreeSet<Move>(new Comparator<Move>() {

            @Override
            public int compare(Move o1, Move o2) {
                Move leaf1 = o1;
                while(leaf1.getNextMove() != null){
                    leaf1 = leaf1.getNextMove();
                }
                Move leaf2 = o2;
                while(leaf2.getNextMove() != null) {
                    leaf2 = leaf2.getNextMove();
                }

                if(isBoardStateEqual(leaf1.getPostState(),leaf2.getPostState())) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
        });
        moveSet.addAll(moveList);
        return moveSet.stream().toList();
    }

    private boolean isBoardStateEqual(BoardState state1, BoardState state2) {
        boolean boardStateEqual = true;
        if(state1.getBoardArray().length != state2.getBoardArray().length) {
            return false;
        }

        for(int i=0; i < state1.getBoardArray().length; i++) {
            Point p1 = state1.getBoardArray()[i];
            Point p2 = state2.getBoardArray()[i];
            if(p1.getCheckers().size() != p2.getCheckers().size()) {
                return false;
            }
            else if(p1.getCheckers().size() > 0) {
                if(p1.getCheckers().get(0).getColor() != p2.getCheckers().get(0).getColor()) {
                    return false;
                }
            }

        }
        return true;
    }

    private List<Move> findLegalMovesForDiceSequence(Board board, int[] dice) {
        List<Move> prevList = findMovesForDie(board.getBoardState(),dice[0],board.getTurnToMove());
        List<Move> nextList = new ArrayList<>();

        for(int i=1; i < dice.length; i++) {
            for(Move item : prevList) {
                Move move = item;
                while (move.getNextMove() != null) {
                    move = move.getNextMove();
                }

                if(move.getMoveType() == Move.MoveType.NILL || move.getNextMove() != null && move.getNextMove().getMoveType() == Move.MoveType.TERMINATE) {
                    nextList.add(move);
                    continue;
                }
                else {
                    var movesForDie = findMovesForDie(move.getPostState(),dice[i],board.getTurnToMove());
                    for(Move nextMove : movesForDie) {
                        Move parent = new Move(move.getMoveType(),move.getFrom(),move.getTo(),move.getPreState());
                        parent.setHit(move.isHit());
                        parent.setPostState(move.getPostState());
                        parent.setNextMove(nextMove);
                        nextList.add(parent);
                    }
                }
            }

            prevList = nextList;
            nextList = new ArrayList<Move>();
        }

        return prevList;
    }

    private  List<Move> findMovesForDie(BoardState boardState, int die, Checker.Color movingColor) {
        List<Move> legalItems = new ArrayList<Move>();


        var barCheckers = boardState.getBoardArray()[movingColor.getArrayIndex(movingColor.getBar())].getCheckers();

        if (barCheckers.size() > 0) { // kirigi var
            int destination = movingColor.getAbsolutePutDestination(die);
            Point putPoint = boardState.getBoardArray()[destination];

            if (destinationPlayable(boardState.getBoardArray(), destination, movingColor)) {
                Move move = new Move(Move.MoveType.PUT, movingColor.getBar(), movingColor.getRelativePos(destination),boardState);
                if(putPoint.getCheckers().size() == 1 && putPoint.getCheckers().get(0).getColor() != movingColor) {
                    move.setHit(true);
                }
                legalItems.add(move);
            }
        } else if (!allCheckersAtHome(boardState.getBoardArray(), movingColor)) { // herkes evde degil

            for (int relativePos = 24; relativePos > die + 1; relativePos--) {
                Point fromPoint = boardState.getBoardArray()[movingColor.getArrayIndex(relativePos)];
                if (fromPoint.getCheckers().size() > 0 && fromPoint.getCheckers().get(0).getColor() == movingColor) {
                    int destination = movingColor.getArrayIndex(relativePos - die);
                    if (destinationPlayable(boardState.getBoardArray(), destination, movingColor)) {
                        Move move = new Move(Move.MoveType.CARRY, relativePos, movingColor.getRelativePos(destination),boardState);
                        legalItems.add(move);
                    }
                }
            }
        } else { // herkes evde

            Point diePoint = boardState.getBoardArray()[movingColor.getArrayIndex(die)];
            if (diePoint.getCheckers().size() > 0 && diePoint.getCheckers().get(0).getColor() == movingColor) {
                Move moveItem = new Move(Move.MoveType.BEAR, die, movingColor.getBear(),boardState);
                legalItems.add(moveItem);
            } else {
                boolean isEverythingLessThanDice = true;
                for (int i = 6; i > die; i--) {
                    int idx = movingColor.getArrayIndex(i);
                    Point p = boardState.getBoardArray()[idx];
                    if (p.getCheckers().size() > 0 && p.getCheckers().get(0).getColor() == movingColor) {
                        isEverythingLessThanDice = false;
                        break;
                    }
                }
                if (isEverythingLessThanDice) {
                    // find the greatest point bearable
                    for (int i = die - 1; i > 0; i--) {
                        Point p = boardState.getBoardArray()[movingColor.getArrayIndex(i)];
                        if (p.getCheckers().size() > 0 && p.getCheckers().get(0).getColor() == movingColor) {
                            Move moveItem = new Move(Move.MoveType.BEAR, i, movingColor.getBear(),boardState);
                            legalItems.add(moveItem);
                            break;
                        }
                    }

                }
            }
        }
        if(legalItems.size() == 0) {
            // no legal moves
            Move move = new Move(Move.MoveType.NILL,null,null,boardState);
            legalItems.add(move);
        }
        for(Move move : legalItems) {
            move.setPostState(executeOnState(move,movingColor));
            if(isGameEnded(move.getPostState(),movingColor)) {
                move.setNextMove(new Move(Move.MoveType.TERMINATE,null,null,move.getPreState()));
            }
        }
        return legalItems;
    }

    private static BoardState executeOnState( Move move, Checker.Color movingColor)  {
        if(move.getMoveType() == Move.MoveType.NILL || move.getMoveType() == Move.MoveType.TERMINATE) {
            return move.getPreState();
        }
        BoardState cloneState = move.getPreState().copy();
        if(move.getMoveType() == Move.MoveType.NILL || move.getMoveType() == Move.MoveType.TERMINATE) {
            return cloneState;
        }
        Point fromPoint = cloneState.getBoardArray()[movingColor.getArrayIndex(move.getFrom())];
        Point toPoint = cloneState.getBoardArray()[movingColor.getArrayIndex(move.getTo())];

        Checker checker = fromPoint.getCheckers().get(0);

        if (move.getMoveType() == Move.MoveType.PUT || move.getMoveType() == Move.MoveType.CARRY) {
            if (toPoint.getCheckers().size() != 0 && toPoint.getCheckers().get(0).getColor() != movingColor) {
                if (toPoint.getCheckers().size() == 1) {
                    Checker hit = toPoint.getCheckers().get(0);
                    toPoint.getCheckers().remove(hit);
                    cloneState.getBoardArray()[hit.getColor().getArrayIndex(hit.getColor().getBar())].getCheckers().add(hit);
                } else {
                    logger.error("TO_POINT_OCCUPIED");
                }
            }
        }

        fromPoint.getCheckers().remove(checker);
        toPoint.getCheckers().add(checker);
        return cloneState;

    }


    private static boolean allCheckersAtHome(Point[] board, Checker.Color movingColor) {
        boolean allCheckersAtHome = true;
        if (board[movingColor.getArrayIndex(movingColor.getBar())].getCheckers().size() > 0) {
            return false;
        }
        for (int i = 7; i < 25; i++) {
            Point p = board[movingColor.getArrayIndex(i)];
            if (p.getCheckers().size() > 0 && p.getCheckers().get(0).getColor() == movingColor) {
                allCheckersAtHome = false;
                break;
            }
        }
        return allCheckersAtHome;
    }

    private static boolean destinationPlayable(Point[] board, int destination, Checker.Color movingColor) {
        var checkersOnToPosition = board[destination].getCheckers();
        return (checkersOnToPosition == null || checkersOnToPosition.size() < 2 || checkersOnToPosition.stream().anyMatch(c -> c.getColor() == movingColor));
    }

    private static boolean isGameEnded(BoardState boardState, Checker.Color movingColor) {
        Point[] board = boardState.getBoardArray();
        boolean isGameEnded = true;
        if(board[movingColor.getArrayIndex(movingColor.getBar())].getCheckers().size() > 0) {
            return false;
        }
        for (int i = 0; i < 24; i++) {
            Point p = board[i];
            if (p.getCheckers().size() > 0 && p.getCheckers().get(0).getColor() == movingColor) {
                isGameEnded = false;
                break;
            }
        }

        return isGameEnded;
    }
}
