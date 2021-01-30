package com.example.connectfour;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
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

        //Creating instance of the Connect 4 Board
        Board board = new Board(idBoard, this);

        //Undo Button
        Button undo = (Button) findViewById(R.id.undoButton);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.undo();
            }
        });

        //Button 1: row 1 drops
        Button btnCol1 = (Button) findViewById(R.id.Col1Drop);
        btnCol1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.performClickAction(0);
            }
        });

        //Button 2: row 2 drops
        Button btnCol2 = (Button) findViewById(R.id.Col2Drop);
        btnCol2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.performClickAction(1);
            }
        });

        //Button 3: row 3 drops
        Button btnCol3 = (Button) findViewById(R.id.Col3Drop);
        btnCol3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.performClickAction(2);
            }
        });

        //Button 4: row 4 drops
        Button btnCol4 = (Button) findViewById(R.id.Col4Drop);
        btnCol4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.performClickAction(3);
            }
        });

        //Button 5: row 5 drops
        Button btnCol5 = (Button) findViewById(R.id.Col5Drop);
        btnCol5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.performClickAction(4);
            }
        });

        //Button 6: row 6 drops
        Button btnCol6 = (Button) findViewById(R.id.Col6Drop);
        btnCol6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.performClickAction(5);
            }
        });

        //Button 7: row 7 drops
        Button btnCol7 = (Button) findViewById(R.id.Col7Drop);
        btnCol7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.performClickAction(6);
            }
        });
    }
}