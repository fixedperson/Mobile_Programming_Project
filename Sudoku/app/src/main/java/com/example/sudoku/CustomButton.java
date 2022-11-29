package com.example.sudoku;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CustomButton extends FrameLayout {
    int row, col, value;
    TextView textView;
    TableLayout memo;
    TextView[] memos;

    public CustomButton(Context context, int row, int col){
        super(context);

        this.row = row;
        this.col = col;

        setClickable(true);

        textView = new TextView(context);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setPadding(10, 30, 10, 30);
        textView.setBackgroundResource(R.drawable.button_selector);
        addView(textView);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        memo = (TableLayout) layoutInflater.inflate(R.layout.layout_memo, null);
        addView(memo);

        int k = 0;
        memos = new TextView[9];
        for(int i = 0; i < 3; i++) {
            TableRow tableRow = (TableRow) memo.getChildAt(i);
            for(int j = 0; j < 3; j++, k++) {
                memos[k] = (TextView) tableRow.getChildAt(j);
            }
        }
    }

    public void set(int a){
        value = a;
        if(a == 0){
            textView.setText(null);
        }

        else {
            textView.setText(Integer.toString(a));
        }
    }
}