package com.example.android.styletransfer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class StyleActivity extends AppCompatActivity {

    private ImageView temp;
    private SeekBar seekbar;
    public Bitmap bitmap;
    private TextView seekbarnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        temp = (ImageView)findViewById(R.id.imgview);
        LayoutParams lp=(LayoutParams) temp.getLayoutParams();
        lp.width=width;
        lp.height=width;
        temp.setLayoutParams(lp);

        temp=(ImageView)findViewById(R.id.imgview);
        Intent intent = getIntent();
        if(intent != null){
            bitmap = intent.getParcelableExtra("bitmap");
            temp.setImageBitmap(bitmap);
        }else if(intent == null){
            Toast.makeText(StyleActivity.this, "图片传输失败了！", Toast.LENGTH_SHORT).show();
        }

        isBinding();
        onClick();
    }

    private void isBinding(){
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        seekbarnum = (TextView) findViewById(R.id.seekbarnum);
    }

    private void onClick(){
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 数值改变
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekbarnum.setText(""+i);
            }
            // 开始拖动
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            // 停止拖动
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
