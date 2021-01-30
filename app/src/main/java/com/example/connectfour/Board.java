package com.example.connectfour;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.Stack;

public class Board {
    private static int ROW_SIZE = 6;
    private static int COLUMN_SIZE = 7;

    private static String chip = "EGG";

    //Stores moves in history
    Stack<Integer> rowHistory = new <Integer> Stack();
    Stack <Integer> colHistory = new <Integer> Stack();

    //array containing id of imageViews of chips
    int[][] idBoard = new int[ROW_SIZE][COLUMN_SIZE];

    //array that contains all positions on the board
    char[][] positionBoard = new char[ROW_SIZE][COLUMN_SIZE];

    Activity env;

    //constructor
    public Board(int[][] ids, Activity runner){
        idBoard = ids.clone();
        env = runner;
    }

    //methods
    public void performClickAction(int col){
        int rowPlaced = 0;
        //check if move valid: by checking top of column
        if(positionBoard[0][col] == '\u0000'){
            rowPlaced = fillToTop(positionBoard, col, chip);
            ImageView picture = (ImageView) env.findViewById(idBoard[rowPlaced][col]);
            if(chip == "EGG")
                picture.setImageResource(R.drawable.egg);
            else
                picture.setImageResource(R.drawable.chicken);
        }
        //ask for another move because column full
        else
            Toast.makeText( env.getApplicationContext(), "Enter a valid move", Toast.LENGTH_SHORT).show();

        //check if in an end state
        //Check for a possible winner
        if(decideWinner(positionBoard,rowPlaced, col,chip)){
            //End of game, return to landing page while clearing boards
            for (int i = 0; i <ROW_SIZE; i++){
                for(int j = 0; j < COLUMN_SIZE; j++){
                    positionBoard[i][j] = '\u0000';

                    ImageView a = (ImageView) env.findViewById(idBoard[i][j]);
                    a.setImageResource(R.drawable.white_background);
                }
            }
            displayWinnerPopup(chip);
        }

        //all positions full: tie
        if(!containEmptyEntry(positionBoard)) {
            //End of game, return to landing page while clearing boards
            for (int i = 0; i <ROW_SIZE; i++){
                for(int j = 0; j < COLUMN_SIZE; j++){
                    positionBoard[i][j] = '\u0000';

                    ImageView a = (ImageView) env.findViewById(idBoard[i][j]);
                    a.setImageResource(R.drawable.white_background);
                }
            }
            displayTiePopup(chip);
        }
        //Not in end state, add move to history
        rowHistory.push(rowPlaced);
        colHistory.push(col);

        chip = switchTurns(chip);
        displayPlayerTurn();
    }

    //undo's the last move
    public void undo(){
        if (!rowHistory.isEmpty()) {
            //get row and col to undo
            int row = rowHistory.peek();
            rowHistory.pop();
            int col = colHistory.peek();
            colHistory.pop();

            //clear position on internal board
            positionBoard[row][col] = '\u0000';

            //change image in UI
            ImageView pos = (ImageView) env.findViewById(idBoard[row][col]);
            pos.setImageResource(R.drawable.white_background);
            chip = switchTurns(chip);
            Toast.makeText(env.getApplicationContext(), "Undo",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean containEmptyEntry(char[][] a){
        for(int i =0; i < ROW_SIZE; i++){
            for (int j =0; j < COLUMN_SIZE; j++){
                if(a[i][j] == '\u0000')
                    return true;
            }
        }
        return false;
    }

    private void displayTiePopup(String chip) {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(env);
        helpBuilder.setTitle("Tough Game");
        helpBuilder.setMessage("Match ended in draw");
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Go to main landing page
                        Intent intent = new Intent(env, MainActivity2.class);
                        env.startActivity(intent);
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    private void displayWinnerPopup(String chip) {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(env);
        helpBuilder.setTitle("Congratulations");
        helpBuilder.setMessage("The winner is " + chip);
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Go to main landing page
                        Intent intent = new Intent(env, MainActivity2.class);
                        env.startActivity(intent);
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    private boolean decideWinner(char[][] positionBoard, int row, int col, String chip) {
        if(checkRow(positionBoard, row, chip)) {
            return true;
        }
        else if(checkCol(positionBoard, col, row, chip))
            return true;
        else if(checkDiagonalRight(positionBoard, chip))
            return true;
        else if(checkDiagonalLeft(positionBoard, chip))
            return true;
        else
            return false;
    }

    private boolean checkDiagonalLeft(char[][] positionBoard, String chip) {
        String solution;

        for(int i = 0; i < COLUMN_SIZE ; i++){
            solution = "";
            for(int row = 0, col = i; col < COLUMN_SIZE && row < ROW_SIZE; row++, col++){
                solution += String.valueOf(positionBoard[row][col]);
            }
            if(solution.contains( searchTerm(chip)))
                return true;
        }

        //check col 5 and below
        for(int i = 1; i < ROW_SIZE; i++){
            solution = "";
            for(int row = i, col = 0; row < ROW_SIZE &&  col < COLUMN_SIZE  ; row++, col++){
                solution += String.valueOf(positionBoard[row][col]);
            }
            if(solution.contains( searchTerm(chip) ))
                return true;
        }
        return false;
    }

    private boolean checkDiagonalRight(char[][] positionBoard, String chip) {
        String solution;

        for(int i = 0; i < ROW_SIZE; i++){
            solution = "";

            for(int row = i, col = 0; row >= 0 && col >= 0; row--, col++){
                solution += String.valueOf(positionBoard[row][col]);
            }
            if(solution.contains( searchTerm(chip) ))
                return true;
        }

        for(int cStart = 1; cStart < COLUMN_SIZE; cStart++){
            solution = "";

            for(int row = ROW_SIZE-1, col = cStart; row > 0 && col < COLUMN_SIZE; row--, col++){
                solution += String.valueOf(positionBoard[row][col]);
            }

            if(solution.contains( searchTerm(chip) ))
                return true;
        }

        return false;

    }

    private boolean checkCol(char[][] positionBoard, int col, int row, String chip) {
        String solution = "";

        for(int i = 0; i < ROW_SIZE; i++)
            solution += positionBoard[i][col];

        if(solution.contains( searchTerm(chip) ))
            return true;
        return false;
    }

    private String searchTerm(String chip) {
        if(chip == "EGG")
            return "EEEE";
        return "CCCC";
    }

    private boolean checkRow(char[][] positionBoard, int row, String chip) {
        if( (String.valueOf(positionBoard[row])).contains( searchTerm(chip) ))
            return true;
        return false;
    }

    private int fillToTop(char[][] positionBoard, int col, String chip) {
        int row = ROW_SIZE - 1;
        while(row > 0 && positionBoard[row][col] != 0){
            row--;
        }

        if(chip == "CHICKEN")
            positionBoard[row][col] = 'C';
        else
            positionBoard[row][col] = 'E';

        return row;
    }

    private String switchTurns(String chip){
        if (chip == "CHICKEN")
            return "EGG";
        else
            return "CHICKEN";
    }

    private void displayPlayerTurn() {
        Toast.makeText(env.getApplicationContext(), "It is " + chip + "'s turn", Toast.LENGTH_SHORT).show();
    }

}
