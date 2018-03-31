package com.example.liuxuanchi.project;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by liuxuanchi on 2017/12/23.
 */

public class TitleLayout extends LinearLayout {
    public TitleLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_layout, this);

//        Button menu = (Button)findViewById(R.id.button_menu);
//        menu.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "You open the menu", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

}
