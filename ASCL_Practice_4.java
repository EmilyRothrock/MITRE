import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.util.*;

public class ASCL_Practice_4 {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("input4.txt")); // first three values on line are locations of opponent's markers
                                                          // fourth through sixth values are locations of player's markers
                                                          // seventh value is number of dice rolls...opponent always goes first
        for (int fileIndex = 0; fileIndex < 5; fileIndex++) {
            Player opponent = new Player(new Marker(sc.nextInt()), new Marker(sc.nextInt()), new Marker(sc.nextInt())); 
            Player player = new Player(new Marker(sc.nextInt()), new Marker(sc.nextInt()), new Marker(sc.nextInt()));
            PatolliGrid pg = new PatolliGrid(opponent, player);

            int numOfDiceRolls = sc.nextInt();
            for (int i = 0; i < numOfDiceRolls; i++) {
                int roll = sc.nextInt();
                if (i % 2 == 0) { // opponent's turn
                    Marker markerToBeMoved = opponent.findSmallestMarker();
                    pg.diceRoll(markerToBeMoved.getPosition(), roll, markerToBeMoved); // does the moves for a roll
                } else { // player's turn
                    Marker markerToBeMoved = player.findSmallestMarker();
                    pg.diceRoll(markerToBeMoved.getPosition(), roll, markerToBeMoved); // does the moves for a roll
                }
            }

            System.out.println(opponent.sumOfMarker() + " " + player.sumOfMarker());
        }

    }
}

class PatolliGrid {
    private int[][] grid = { { 0, 0, 0, 0, 1, 52, 0, 0, 0, 0 }, { 0, 0, 0, 0, 2, 51, 0, 0, 0, 0 },
            { 7, 6, 5, 4, 3, 50, 49, 48, 47, 46 }, { 8, 9, 10, 11, 12, 41, 42, 43, 44, 45 },
            { 17, 16, 15, 14, 13, 40, 39, 38, 37, 36 }, { 18, 19, 20, 21, 22, 31, 32, 33, 34, 35 },
            { 0, 0, 0, 0, 23, 30, 0, 0, 0, 0 }, { 0, 0, 0, 0, 24, 29, 0, 0, 0, 0 }, { 0, 0, 0, 0, 25, 28, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 26, 27, 0, 0, 0, 0 } };
    private Player opponent;
    private Player player;

    public PatolliGrid(Player opponent, Player player) {
        this.opponent = opponent;
        this.player = player;
    }

    // prints grid as a visual checker
    public void printGrid() {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                System.out.print(grid[r][c] + "\t");
            }
            System.out.println();
            System.out.println();
        }
    }

    public Boolean isPrime(int finalPos) {
        int i = 2;
        boolean flag = false;
        while (i <= finalPos / 2) {
            if (finalPos % i == 0) {
                flag = true;
                break;
            }
            i++;
        }

        if (!flag) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isPerfectSquare(int finalPos) {
        for (int i = 2; i <= 8; i++) {
            if (i * i == finalPos) {
                return true;
            }
        }
        return false;
    }

    // returns whether or not a marker moved horizontally and then vertically on the patolli board during a turn
    public Boolean isHorizontalAndVertical(int startPos, int finalPos) {
        Boolean sixToEight = startPos <= 6 && finalPos >= 8;
        Boolean elevenToThirteen = startPos <= 11 && finalPos >= 13;
        Boolean sixteenToEighteen = startPos <= 16 && finalPos >= 18;
        Boolean twentyOneToTwentyThree = startPos <= 21 && finalPos >= 23;
        Boolean twentySixToTwentyEight = startPos <= 26 && finalPos >= 28;
        Boolean thirtyFourToThirtySix = startPos <= 34 && finalPos >= 36;
        Boolean thirtyNineToFourtyOne = startPos <= 39 && finalPos >= 41;
        Boolean fourtyFourToFourtySix = startPos <= 44 && finalPos >= 46;
        Boolean fourtyNineToFiftyOne = startPos <= 49 && finalPos >= 51;

        if (sixToEight || elevenToThirteen || sixteenToEighteen || twentyOneToTwentyThree || twentySixToTwentyEight
                || thirtyFourToThirtySix || thirtyNineToFourtyOne || fourtyFourToFourtySix || fourtyNineToFiftyOne) {
            return true;
        }
        return false;
    }

    // checks to see if there is already a marker on the location a marker lands on during a turn
    public Boolean isLocationOccupied(int pos) {
        if (opponent.getMarkerOne().getPosition() == pos || opponent.getMarkerTwo().getPosition() == pos
                || opponent.getMarkerThree().getPosition() == pos || player.getMarkerOne().getPosition() == pos
                || player.getMarkerTwo().getPosition() == pos || player.getMarkerThree().getPosition() == pos) {
            return true;
        }
        return false;
    }

    // checks if the spot the marker lands on is a multiple of the die roll (num)
    public static Boolean checkIfMultiple(int num, int multiple) {
        if (multiple % num == 0) {
            return true;
        }
        return false;
    }

    // simulates one dice roll for a player
    public void diceRoll(int startPos, int roll, Marker marker) {
        int endPos = startPos + roll;
        if (this.isLocationOccupied(endPos) || endPos > 52) { // checks if the spot is already occupied or if it's going
                                                              // off the board
            return;
        } else if (endPos == 52) {
            marker = null;
            // System.out.println("\t\tRemove Marker");
        } else if (isPrime(endPos)) { // checks if landing spot is a prime and moves forward six spaces until
                                      // blocked if that is true
            marker.setPosition(endPos);
            for (int i = 1; i <= 6; i++) {
                if (isLocationOccupied(endPos + i)) {
                    break;
                }
                marker.setPosition(endPos + i);
            }
        } else if (isPerfectSquare(endPos)) { // checks if landing spot is perfect square and moves backwards six spaces
                                              // until blocked if that is true
            marker.setPosition(endPos);
            for (int i = 1; i <= 6; i++) {
                if (isLocationOccupied(endPos - i)) {
                    break;
                }
                marker.setPosition(endPos - i);
            }
        } else if (isHorizontalAndVertical(startPos, endPos)) { // checks if the marker moves horizontally and then 
                                                                // vertically, then it checks if the landing spot is 
                                                                // a multiple of the dice roll and appropriately changes 
                                                                // the marker location
            for (int i = roll; i > 0; i--) {
                if (checkIfMultiple(roll, startPos + i) && !isLocationOccupied(startPos + i)) // only moves marker
                                                                                              // if the landing location
                                                                                              // is a multiple of the dice
                                                                                              // roll value
                    marker.setPosition(startPos + i);
            }
        } else { // if none of the above conditions are true, the marker is moved the value of the die roll
            marker.setPosition(endPos);
        }
    }

}

class Player {
    // markers that each player has
    private Marker markerOne;
    private Marker markerTwo;
    private Marker markerThree;

    public Player(Marker markerOne, Marker markerTwo, Marker markerThree) {
        this.markerOne = markerOne;
        this.markerTwo = markerTwo;
        this.markerThree = markerThree;
    }

    // accessor methods
    public Marker getMarkerOne() {
        return markerOne;
    }

    public Marker getMarkerTwo() {
        return markerTwo;
    }

    public Marker getMarkerThree() {
        return markerThree;
    }


    public int sumOfMarker() {
        if (markerOne == null && markerTwo == null && markerThree == null) {
            return 0;
        } else if (markerOne == null && markerTwo == null) {
            return markerThree.getPosition();
        } else if (markerOne == null && markerThree == null) {
            return markerTwo.getPosition();
        } else if (markerTwo == null && markerThree == null) {
            return markerOne.getPosition();
        } else if (markerOne == null) {
            return markerTwo.getPosition() + markerThree.getPosition();
        } else if (markerTwo == null) {
            return markerOne.getPosition() + markerThree.getPosition();
        } else if (markerThree == null) {
            return markerOne.getPosition() + markerTwo.getPosition();
        } else {
            return markerOne.getPosition() + markerTwo.getPosition() + markerThree.getPosition();
        }
    }

    public Marker findSmallestMarker() {
        if (markerOne.getPosition() < markerTwo.getPosition() && markerOne.getPosition() < markerThree.getPosition()) {
            return markerOne;
        } else if (markerTwo.getPosition() < markerOne.getPosition()
                && markerTwo.getPosition() < markerThree.getPosition()) {
            return markerTwo;
        } else {
            return markerThree;
        }
    }
}

class Marker {
    private int position;

    public Marker(int pos) {
        position = pos;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int pos) {
        position = pos;
    }
}