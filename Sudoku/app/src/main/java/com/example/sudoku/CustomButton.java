package com.example.sudoku;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CustomButton extends FrameLayout {
    int row, col, value;
    TextView textView;

    public CustomButton(Context context, int row, int col){
        super(context);

        this.row = row;
        this.col = col;

        textView = new TextView(context);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setPadding(10, 30, 10, 30);
        textView.setBackgroundResource(R.drawable.button_selector);

        setClickable(true);

        addView(textView);
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