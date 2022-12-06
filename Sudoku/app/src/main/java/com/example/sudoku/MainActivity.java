package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    TableLayout table, numberPad;
    CustomButton clicked;
    TableRow[] tableRows;
    CustomButton[][] buttons;

    Dialog dialog;

    Set<CustomButton> conflicts = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        table = (TableLayout)findViewById(R.id.tableLayout);
        numberPad = (TableLayout) findViewById(R.id.numberPad);

        table.setBackgroundColor(Color.GRAY);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp4 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);

        lp.setMargins(0, 0, 0, 0);
        lp2.setMargins(15, 0, 0, 0);
        lp3.setMargins(0, 15, 0, 0);
        lp4.setMargins(15, 15, 0, 0);

        tableRows = new TableRow[9];
        for(int i = 0; i < 9; i++) {
            tableRows[i] = new TableRow(this);
            table.addView(tableRows[i]);
        }
        buttons = new CustomButton[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                buttons[row][col] = new CustomButton(this, row, col);

                buttons[row][col].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        clicked = (CustomButton) view;
                        numberPad.setVisibility(View.VISIBLE);
                    }
                });

                buttons[row][col].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        CustomButton temp = (CustomButton) view;
                        if(temp.value > 0) return false;

                        dialog = new Dialog(MainActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_memo);
                        dialog.show();

                        dialogMemo(view);
                        return true;
                    }
                });

                if(row%3==0 && col%3==0) buttons[row][col].setLayoutParams(lp4);
                else if(row%3==0)buttons[row][col].setLayoutParams(lp3);
                else if(col%3==0)buttons[row][col].setLayoutParams(lp2);
                else buttons[row][col].setLayoutParams(lp);

                tableRows[row].addView(buttons[row][col]);
            }
        }

        Integer[][] numbers = new Integer[9][9];

        Random rd = new Random();

        int cnt = 20;
        while(cnt > 0){
            int r = rd.nextInt(9);
            int c = rd.nextInt(9);

            if(numbers[r][c] == null){
                numbers[r][c] = 0;
                cnt--;
            }
        }

        BoardGenerator board = new BoardGenerator();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(numbers[i][j] == null) {
                    numbers[i][j] = board.get(i,j);
                }
                buttons[i][j].set(numbers[i][j]);
            }
        }

    }

    public void onClickNum1(View v) {
        Button btn = (Button) v;

        String s = btn.getText().toString();
        if(s.matches("^[1-9]$")) {
            clicked.set(Integer.parseInt(s));
            memoDelete(clicked);
            setConflict();
        }
        else if(s.equals("DEL")) {
            clicked.set(0);
            memoDelete(clicked);
            clicked.textView.setBackgroundResource(R.drawable.button_selector);
        }
        unsetConflict();
        numberPad.setVisibility(View.GONE);
    }

    // Conflict
    public void setConflict() {
        boolean isConflict = false;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(buttons[i][j] == clicked) continue;
                if(clicked.value == buttons[i][j].value){
                    if((clicked.row == buttons[i][j].row) || (clicked.col == buttons[i][j].col)){
                        buttons[i][j].textView.setBackgroundColor(Color.RED);
                        conflicts.add(buttons[i][j]);
                        isConflict = true;
                    }
                }
            }
        }

        int row = clicked.row / 3;
        int col = clicked.col / 3;
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(buttons[(row*3)+i][(col*3)+j] == clicked) continue;
                if(buttons[(row*3)+i][(col*3)+j].value == clicked.value){
                    buttons[(row*3)+i][(col*3)+j].textView.setBackgroundColor(Color.RED);
                    conflicts.add(buttons[(row*3)+i][(col*3)+j]);
                    isConflict = true;
                }
            }
        }

        if(isConflict) {
            clicked.textView.setBackgroundColor(Color.RED);
            conflicts.add(clicked);
        }
    }

    // No conflict
    public void unsetConflict() {
        Set<CustomButton> remove = new HashSet<>();
        for(CustomButton btn : conflicts){
            boolean isConflict = false;
            for(int i = 0; i < 9; i++){
                if(btn.col == i) continue;
                if(btn.value == buttons[btn.row][i].value) {
                    isConflict = true;
                    break;
                }
            }

            for(int i = 0; i < 9; i++){
                if(btn.row == i) continue;
                if(btn.value == buttons[i][btn.col].value) {
                    isConflict = true;
                    break;
                }
            }

            int row = btn.row / 3;
            int col = btn.col / 3;
            for(int i=0; i<3; i++){
                for(int j=0; j<3; j++){
                    if(buttons[(row*3)+i][(col*3)+j] == btn) continue;
                    if(buttons[(row*3)+i][(col*3)+j].value == btn.value){
                        isConflict = true;
                        break;
                    }
                }
            }

            if(isConflict) continue;

            remove.add(btn);
        }

        for(CustomButton btn : remove) {
            btn.textView.setBackgroundResource(R.drawable.button_selector);
            conflicts.remove(btn);
        }
    }

    public void dialogMemo(View view){
        int k = 0;
        TableLayout memoLayout = dialog.findViewById(R.id.memobtns);
        ToggleButton[] memos = new ToggleButton[9];
        for(int i = 0; i < 3; i++) {
            TableRow tableRow = (TableRow) memoLayout.getChildAt(i);
            for(int j = 0; j < 3; j++, k++) {
                memos[k] = (ToggleButton) tableRow.getChildAt(j);
            }
        }

        TextView del = dialog.findViewById(R.id.del);
        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView ok = dialog.findViewById(R.id.ok);

        del.setBackgroundResource(R.drawable.dialog_selector);
        cancel.setBackgroundResource(R.drawable.dialog_selector);
        ok.setBackgroundResource(R.drawable.dialog_selector);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memoDelete(view);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomButton temp = (CustomButton) view;
                for(int i = 0; i < 9; i++){
                    if(memos[i].isChecked()) temp.memos[i].setVisibility(View.VISIBLE);
                    else temp.memos[i].setVisibility(View.INVISIBLE);
                }
                dialog.dismiss();
            }
        });
    }

    public void memoDelete(View view){
        CustomButton temp = (CustomButton) view;
        for(int i = 0; i < 9; i++){
            temp.memos[i].setVisibility(View.INVISIBLE);
        }
    }
}