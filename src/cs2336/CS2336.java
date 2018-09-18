/** Tung Vu - Reversi Game. CS 2336.501
 *  Created 2 classes: 1 is the game itself named cs2336, 1 is the board
 *  Implemented Diagonal moves
 *  Implemented 3 versions of the game
 *  Input for human: row first, column after
 */
package cs2336;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class CS2336 {
    public static void main(String[] args) {
        String turn = "white"; // initiate to white
        int counter = 0;        // death counter
        int move = 0;           // the move each turn
        int N = getN();         // length of the board
        int mode = getMode();   // choose version of the game
        
        Board[] array = new Board[(N+2) * (N+2)];   // Create the board
        
        setUp(array, N);    // set up the starting position
        draw(N, array);     // draw
         //finished set up
         // start the game
        while (counter != 2){
            turn = turnSwitch(turn);                        // switch turn of players
            int[] validMoves = validMoves(N, array, turn);  // array holds the valid moves 
            if (validMoves[0] == 0) counter++;              // check if there are moves available 
            else counter = 0;
            if (counter == 1) continue;                     // count once, check if opponent has available moves
            if (counter == 2) break;                        // count twice, end the game
            switch (mode){                                  // differrent versions of the game
                case 1: if (turn == "black") move = getMove(validMoves, N, turn);   // get move for human
                        if (turn == "white") move = selectMove(validMoves, N, turn);// select move for computer
                        break;
                case 2: if (turn == "black") move = selectMove(validMoves, N, turn);
                        if (turn == "white") move = selectMove(validMoves, N, turn);
                        break;
                case 3: if (turn == "black") move = getMove(validMoves, N, turn);
                        if (turn == "white") move = getMove(validMoves, N, turn);
                        break;
            }
            flip(N, array, move, turn);                     // function for flipping
            draw(N, array);
            score(array, N);                                // print out the score
        }
        System.exit(0);
    }
    
    public static int getMode(){
        Scanner input = new Scanner(System.in);
        int N = 0;
        
        boolean valid = true;
        do {
            valid = true;
            // Print out the menu
            System.out.println("Please choose version of the game: ");
            System.out.println("1. Player vs. Computer");
            System.out.println("2. Computer vs. Computer");
            System.out.println("3. Player vs. Player");
            System.out.println("Enter your choice: ");
            String n = input.nextLine();
            // check the validity of input
            if (n.length() == 0) {
                System.out.println("Invalid input");
                valid = false;
                continue;
            }

            for (char c: n.toCharArray()){
                if (Character.isDigit(c) == false){
                    System.out.println("Invalid input");
                    valid = false;
                    break;
                }
            }
            if (valid == false) continue;
            N = Integer.parseInt(n);
            if (N != 1 && N != 2 && N != 3){
                System.out.println("Invalid input. PLease choose 1, 2 or 3.");
                valid = false;
                continue;
            } else valid = true;

        } while (valid == false);
        return N;
    }
    public static int getN(){
        Scanner input = new Scanner(System.in);
        boolean valid = true;
        int N = 0;
        do {
            valid = true;
            System.out.print("Please enter N: ");
            String n = input.nextLine();
            if (n.length() == 0) {
                System.out.println("Invalid input");
                valid = false;
                continue;
            }

            for (char c: n.toCharArray()){
                if (Character.isDigit(c) == false){
                    System.out.println("Invalid input");
                    valid = false;
                    break;
                }
            }
            if (valid == false) continue;
            N = Integer.parseInt(n);
            if (N % 2 != 0 && N > 0 || N <= 0){
                System.out.println("N must be even. Please enter again.");
                valid = false;
                continue;
            } else valid = true;
            if (N <= 2){
                System.out.println("N too small. Please enter again");
                valid = false;
                continue;
            }
        } while (valid == false);
        return N;
    }
    public static void setUp(Board[] array, int N){
        // set all to empty
        for (int i = 0; i < array.length; i++){
            array[i] = new Board("empty");
        }
        
        // set left border to "border"
        for (int i = 0; i <= (N+2)*(N+2)-(N+2); i = i + N+2){
            array[i].setStatus("border");
        }
        // set right border to "border"
        for (int i = N + 1; i <= (N+2)*(N+2) - 1; i = i + N+2){
            array[i].setStatus("border");
        }
        // set upper border
        
        for (int i = 0; i <= N+1; i++){
            array[i].setStatus("border");
        }
        // set lower border
        for (int i = (N+2)*(N+2) - (N+2); i < array.length; i++){
            array[i].setStatus("border");
        }
        int start = (N/2) * (N+3);
        array[start].setStatus("white");
        array[start + N+3].setStatus("white");
        array[start+1].setStatus("black");
        array[start + N+2].setStatus("black");
    }
    public static void score(Board[] array, int N){
        int black = 0;
        int white = 0;
        for (int i = 0; i < array.length; i++){
            if (array[i].getStatus() == "black") black++;
            if (array[i].getStatus() == "white") white++;
        }
        System.out.println("Score: Black: " + black + "  White: " + white);
        
        if ((black + white) == N*N){ // if there is no moves left
            if (black > white) System.out.println("Black Wins!!!");
            else if (white > black) System.out.println("White Wins!!!");
            else System.out.println("Tie Game!!!");
        }
    }
    public static Board[] flip(int N, Board[] array, int move, String turn){
        int[] direction = {1, -1, -(N+1), N+1, -(N+2), N+2, -(N+3), N+3};
        int j = 0;
        int counter = 0;
        for (int i = 0; i < direction.length; i++){
            counter = 0;
            j = move + direction[i];
            do{
                if (array[j].getStatus() != "empty" && array[j].getStatus() != "border" && array[j].getStatus() != turn ){
                    counter++;
                }
                if (array[j].getStatus() == "empty" || array[j].getStatus() == "border") break;
                if (array[j].getStatus() == turn) break;
                j = j + direction[i];
            } while (array[j].getStatus() != turn);
            if (array[j].getStatus() == "empty" || array[j].getStatus() == "border") continue;
            
            j = move;
            for (int k = 0; k < counter+1; k++){
                array[j].setStatus(turn);
                j = j + direction[i];
            }
        }
        return array;
    }
    public static int getMove(int[] validMoves, int N, String turn){
        int x;
        int y;
        int move = 0;
        boolean valid = true;
        Scanner input = new Scanner(System.in);
        String inputString;
 
        do{
            System.out.print("Please enter your move: ");
            valid = true;
            inputString = input.nextLine();
            for (char c: inputString.toCharArray()){
                if (Character.isDigit(c) == false){
                    System.out.println("Invalid input");
                    valid = false;
                    break;
                }
            }
            if (valid == false) continue;
            char[] c = inputString.toCharArray();
            if (c.length != 2){
                System.out.println("Invalid input");
                valid = false;
                continue;
            }
            x = Character.getNumericValue(c[0]);
            y = Character.getNumericValue(c[1]);
            if (x == 0 || y == 0){
                System.out.print("Invalid move. PLease enter again.\n");
                valid = false;
                continue;
            }
            move = x*(N+2) + y;
            for (int i = 0; i < validMoves.length; i++){
                if (move == validMoves[i]) {
                    System.out.println("Success: " + turn +  " move at (" + x + ", " + y + ")\n");
                    valid = true;
                    break;
                } else valid = false;
            }
            if (valid == true) break;
            System.out.print("Invalid move. PLease enter again.\n");
            valid = false;
        } while (valid == false);
        return move;
    }
    public static int selectMove(int[] validMoves, int N, String turn){
        int c = 0;
        for (int i = 0; i < validMoves.length; i++){
            if (validMoves[i] != 0) c++;
        }
        int random = ThreadLocalRandom.current().nextInt(0, c); // select random from the valid moves
        System.out.print("Success: " + turn +  " move at (" + validMoves[random]/(N+2) + ", " + validMoves[random]%(N+2)  + ")\n");
        return validMoves[random];
    }
    public static int[] validMoves(int N, Board[] array, String turn){
        int[] validMoves = new int[N*N];
        int index = 0;
        int j = 0; // dummy variable to move to the next cell
        for (int i = N+3; i < array. length; i++){
            if (array[i].getStatus() != "empty") continue; // if the cell is not empty then its not valid
            // go east
            j = i+1;
            if (array[j].getStatus() != "empty" && array[j].getStatus() != "border" && array[j].getStatus() != turn){ // if its the opposite color
                do{ // move to the cell after it
                    j = j+1;
                    if (array[j].getStatus() == "empty" || array[j].getStatus() == "border") break;
                    if (array[j].getStatus() == turn) break;
                } while (array[j].getStatus() != turn);
                if (array[j].getStatus() == turn){
                    // now i is the valid index
                    validMoves[index] = i;
                    index++;
                    continue; //////////// its valid, no need to check other direction, move to next cell
                }
            }
            
            // go west
            j = i-1;
            if (array[j].getStatus() != "empty" && array[j].getStatus() != "border" && array[j].getStatus() != turn){ // if its the opposite color
                do{ // move to the cell after it
                    j = j-1;
                    if (array[j].getStatus() == "empty" || array[j].getStatus() == "border") break;
                    if (array[j].getStatus() == turn) break;
                } while (array[j].getStatus() != turn);
                if (array[j].getStatus() == turn){
                    // now i is the valid index
                    validMoves[index] = i;
                    index++;
                    continue; //////////// its valid, no need to check other direction, move to next cell
                }
            }
            
            // go north
            j = i - (N+2);
            if (array[j].getStatus() != "empty" && array[j].getStatus() != "border" && array[j].getStatus() != turn){ // if its the opposite color
                do{ // move to the cell after it
                    j = j - (N+2);
                    if (array[j].getStatus() == "empty" || array[j].getStatus() == "border") break;
                    if (array[j].getStatus() == turn) break;
                } while (array[j].getStatus() != turn);
                if (array[j].getStatus() == turn){
                    // now i is the valid index
                    validMoves[index] = i;
                    index++;
                    continue; //////////// its valid, no need to check other direction, move to next cell
                }
            }
            // go south
            j = i+ (N+2);
            if (array[j].getStatus() != "empty" && array[j].getStatus() != "border" && array[j].getStatus() != turn){ // if its the opposite color
                do{ // move to the cell after it
                    j = j+ (N+2);
                    if (array[j].getStatus() == "empty" || array[j].getStatus() == "border") break;
                    if (array[j].getStatus() == turn) break;
                } while (array[j].getStatus() != turn);
                if (array[j].getStatus() == turn){
                    // now i is the valid index
                    validMoves[index] = i;
                    index++;
                    continue; //////////// its valid, no need to check other direction, move to next cell
                }
            }
            // go north east
            j = i- (N+1);
            if (array[j].getStatus() != "empty" && array[j].getStatus() != "border" && array[j].getStatus() != turn){ // if its the opposite color
                do{ // move to the cell after it
                    j = j - (N+1);
                    if (array[j].getStatus() == "empty" || array[j].getStatus() == "border") break; 
                    if (array[j].getStatus() == turn) break;
                } while (array[j].getStatus() != turn);
                if (array[j].getStatus() == turn){
                    // now i is the valid index
                    validMoves[index] = i;
                    index++;
                    continue; //////////// its valid, no need to check other direction, move to next cell
                }
            }
            // go north west
            j = i - (N+3);
            if (array[j].getStatus() != "empty" && array[j].getStatus() != "border" && array[j].getStatus() != turn){ // if its the opposite color
                do{ // move to the cell after it
                    j = j - (N+3);
                    if (array[j].getStatus() == "empty" || array[j].getStatus() == "border") break; 
                    if (array[j].getStatus() == turn) break;
                } while (array[j].getStatus() != turn);
                if (array[j].getStatus() == turn){
                    // now i is the valid index
                    validMoves[index] = i;
                    index++;
                    continue; //////////// its valid, no need to check other direction, move to next cell
                }
            }
            // go south east
            j = i+(N+3);
            if (array[j].getStatus() != "empty" && array[j].getStatus() != "border" && array[j].getStatus() != turn){ // if its the opposite color
                do{ // move to the cell after it
                    j = j+(N+3);
                    if (array[j].getStatus() == "empty" || array[j].getStatus() == "border") break; 
                    if (array[j].getStatus() == turn) break;
                } while (array[j].getStatus() != turn);
                if (array[j].getStatus() == turn){
                    // now i is the valid index
                    validMoves[index] = i;
                    index++;
                    continue; //////////// its valid, no need to check other direction, move to next cell
                }
            }
            // go south west
            j = i+(N+1);
            if (array[j].getStatus() != "empty" && array[j].getStatus() != "border" && array[j].getStatus() != turn){ // if its the opposite color
                do{ // move to the cell after it
                    j = j+(N+1);
                    if (array[j].getStatus() == "empty" || array[j].getStatus() == "border") break; 
                    if (array[j].getStatus() == turn) break;
                } while (array[j].getStatus() != turn);
                if (array[j].getStatus() == turn){
                    // now i is the valid index
                    validMoves[index] = i;
                    index++;
                    continue; //////////// its valid, no need to check other direction, move to next cell
                }
            }
            
        }
        return validMoves;
                
    }
    public static String turnSwitch(String turn){
        if (turn == "black") return "white";
        else return "black";
    }
    public static void draw(int N, Board[] array){
        
            for(int j = 0; j < array.length; j++){
                if (array[j].getStatus() == "border"){
                    System.out.print("*");
                }
                if (array[j].getStatus() == "empty"){
                    System.out.print("_");
                }
                if (array[j].getStatus() == "black"){
                    System.out.print("B");
                }
                if (array[j].getStatus() == "white"){
                    System.out.print("W");
                }
                if (((j+1) % (N+2)) == 0){
                    System.out.print("\n");
                }
            }
            
    
    }
}
