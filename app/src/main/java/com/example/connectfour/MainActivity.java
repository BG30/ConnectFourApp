package com.example.connectfour;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    public static int ROW_SIZE = 6;
    public static int COLUMN_SIZE = 7;

    public static String chip = "EGG";

    //Stores moves in history
    Stack <Integer> rowHistory = new <Integer> Stack();
    Stack <Integer> colHistory = new <Integer> Stack();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //An array containing the ids of the chips
        int[][] idBoard = new int[][]{  {R.id.r11, R.id.r12, R.id.r13, R.id.r14, R.id.r15, R.id.r16, R.id.r17},
                {R.id.r21, R.id.r22, R.id.r23, R.id.r24, R.id.r25, R.id.r26, R.id.r27},
                {R.id.r31, R.id.r32, R.id.r33, R.id.r34, R.id.r35, R.id.r36, R.id.r37},
                {R.id.r41, R.id.r42, R.id.r43, R.id.r44, R.id.r45, R.id.r46, R.id.r47},
                {R.id.r51, R.id.r52, R.id.r53, R.id.r54, R.id.r55, R.id.r56, R.id.r57},
                {R.id.r61, R.id.r62, R.id.r63, R.id.r64, R.id.r65, R.id.r66, R.id.r67}
        };

        //an array that contains all positions on the board
        char[][] positionBoard = new char[ROW_SIZE][COLUMN_SIZE];
        // button logic

        //Undo Button
        Button undo = (Button) findViewById(R.id.undoButton);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rowHistory.isEmpty()) {
                    //get row and col to undo
                    int row = rowHistory.peek();
                    rowHistory.pop();
                    int col = colHistory.peek();
                    colHistory.pop();

                    //clear position on internal board
                    positionBoard[row][col] = '\u0000';

                    //change image in UI
                    ImageView pos = (ImageView) findViewById(idBoard[row][col]);
                    pos.setImageResource(R.drawable.white_background);

                    Toast.makeText(getApplicationContext(), "Undo",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Button 1: row 1 drops
        Button btnCol1 = (Button) findViewById(R.id.Col1Drop);
        btnCol1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClickAction(positionBoard, idBoard, chip, 0);
                chip = switchTurns(chip);
                displayPlayerTurn(chip);
            }
        });

        //Button 2: row 2 drops
        Button btnCol2 = (Button) findViewById(R.id.Col2Drop);
        btnCol2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClickAction(positionBoard, idBoard, chip, 1);
                chip = switchTurns(chip);
                displayPlayerTurn(chip);
            }
        });

        //Button 3: row 3 drops
        Button btnCol3 = (Button) findViewById(R.id.Col3Drop);
        btnCol3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClickAction(positionBoard, idBoard, chip, 2);
                chip = switchTurns(chip);
                displayPlayerTurn(chip);
            }
        });

        //Button 4: row 4 drops
        Button btnCol4 = (Button) findViewById(R.id.Col4Drop);
        btnCol4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClickAction(positionBoard, idBoard, chip, 3);
                chip = switchTurns(chip);
                displayPlayerTurn(chip);
            }
        });

        //Button 5: row 5 drops
        Button btnCol5 = (Button) findViewById(R.id.Col5Drop);
        btnCol5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClickAction(positionBoard, idBoard, chip, 4);
                chip = switchTurns(chip);
                displayPlayerTurn(chip);
            }
        });

        //Button 6: row 6 drops
        Button btnCol6 = (Button) findViewById(R.id.Col6Drop);
        btnCol6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClickAction(positionBoard, idBoard, chip, 5);
                chip = switchTurns(chip);
                displayPlayerTurn(chip);
            }
        });

        //Button 7: row 7 drops
        Button btnCol7 = (Button) findViewById(R.id.Col7Drop);
        btnCol7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClickAction(positionBoard, idBoard, chip, 6);
                chip = switchTurns(chip);
                displayPlayerTurn(chip);
            }
        });


    }

    private void performClickAction(char[][] positionBoard, int[][] idBoard, String chip, int col){
        int rowPlaced = 0;
        //check if move valid: by checking top of column
        if(positionBoard[0][col] == '\u0000'){
            rowPlaced = fillToTop(positionBoard, col, chip);
            ImageView picture = (ImageView) findViewById(idBoard[rowPlaced][col]);
            if(chip == "EGG")
                picture.setImageResource(R.drawable.egg);
            else
                picture.setImageResource(R.drawable.chicken);
        }
        //ask for another move because column full
        else
            Toast.makeText(getApplicationContext(), "Enter a valid move", Toast.LENGTH_SHORT).show();

        //check if in an end state
        //Check for a possible winner
        if(decideWinner(positionBoard,rowPlaced, col,chip)){
            //End of game, return to landing page while clearing boards
            for (int i = 0; i <ROW_SIZE; i++){
                for(int j = 0; j < COLUMN_SIZE; j++){
                    positionBoard[i][j] = '\u0000';

                    ImageView a = (ImageView) findViewById(idBoard[i][j]);
                    a.setImageResource(R.drawable.white_background);
                }
            }
            displayWinnerPopup(chip);
        }

        //all positions full: tie
        if(countXEntries('\u0000', positionBoard) == 0) {
            //End of game, return to landing page while clearing boards
            for (int i = 0; i <ROW_SIZE; i++){
                for(int j = 0; j < COLUMN_SIZE; j++){
                    positionBoard[i][j] = '\u0000';

                    ImageView a = (ImageView) findViewById(idBoard[i][j]);
                    a.setImageResource(R.drawable.white_background);
                }
            }
            displayTiePopup(chip);
        }
        //Not in end state, add move to history
        rowHistory.push(rowPlaced);
        colHistory.push(col);

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

    private void displayPlayerTurn(String turn) {
        Toast.makeText(getApplicationContext(), "It is " + turn + "'s turn", Toast.LENGTH_SHORT).show();
    }

    private int countXEntries(char x, char[][] a){
        int counter = 0;
        for(int i =0; i < ROW_SIZE; i++){
            for (int j =0; j < COLUMN_SIZE; j++){
                if(a[i][j] == x)
                    counter++;
            }
        }
        return counter;
    }

    private String switchTurns(String chip){
        if (chip == "CHICKEN")
            return "EGG";
        else
            return "CHICKEN";
    }

    private void displayWinnerPopup(String chip){
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Congratulations");
        helpBuilder.setMessage("The winner is " + chip);
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Go to main landing page
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(intent);
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    private void displayTiePopup(String chip){
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Tough Game");
        helpBuilder.setMessage("Match ended in draw");
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Go to main landing page
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(intent);
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

}