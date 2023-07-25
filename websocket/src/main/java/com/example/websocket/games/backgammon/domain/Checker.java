package com.example.websocket.games.backgammon.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Checker {

    public enum Color {
        WHITE(25, 26), BLACK(-3, -4);
        private int bar;
        private int bear;


        private Color(int bar, int bear) {
            this.bar = bar;
            this.bear = bear;
        }

        public int getBar() {
            return this.bar;
        }

        public int getBear() {
            return this.bear;
        }


        public boolean insideHome(int position) {
            if (this == WHITE) {
                return position >= 0 && position <= 5;
            } else if (this == BLACK) {
                return position >= 18 && position <= 23;
            }
            return false;
        }

        public int getArrayIndex(int relativePosition) {
            if (this == WHITE) {
                return relativePosition - 1;
            } else {
                return 24 - relativePosition;
            }
        }

        public int getAbsolutePutDestination(int die) {

            if(this == WHITE) {
                return 24 - die;
            }
            else {
                return die-1;
            }
        }

        public int getRelativePos(int arrIndex) {
            if (this == WHITE) {
                return arrIndex + 1;
            } else {
                return 24 - arrIndex;
            }
        }

    }


    private final Color color;


}
