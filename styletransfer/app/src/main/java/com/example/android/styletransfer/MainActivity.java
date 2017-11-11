package com.example.android.styletransfer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isBinding();
        ButtonListner b= new ButtonListner();
        btn1.setOnClickListener(b);
        btn1.setOnTouchListener(b);
        btn1.setBackgroundResource(R.drawable.beginbutton_up);
    }

    private void isBinding() {
        btn1 = (Button) findViewById(R.id.beginbutton);
    }

    class ButtonListner implements View.OnClickListener, View.OnTouchListener{

        public void onClick(View V) {
            Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
            startActivity(intent);
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                btn1.setBackgroundResource(R.drawable.beginbutton_up);
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btn1.setBackgroundResource(R.drawable.beginbutton_down);
            }
            return false;
        }
    }
}
