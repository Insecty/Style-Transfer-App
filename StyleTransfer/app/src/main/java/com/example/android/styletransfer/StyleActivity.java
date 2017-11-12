package com.example.android.styletransfer;

import android.content.Intent;
import android.content.res.Resources;
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
 import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;


public class StyleActivity extends AppCompatActivity {

    private ImageView temp;  // 图片显示view
    private SeekBar seekbar; // 进度条
    public Bitmap bitmap;    // 原图
    public Bitmap interbitmap; // 中间调整的每次数据
    private Bitmap srcBitmap; // 每次修改后的原图;
    private TextView seekbarnum; // 进度条数值
    public int size;

    private Resources resources;
    int selectcolor;
    int unselectcolor;
    //private int flag=-1;//1代表亮度，2代表对比度，3代表饱和度，4代表黑白处理
    private Button button0,button1,button2,button3,button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        temp = (ImageView)findViewById(R.id.imgview);
        LayoutParams lp=(LayoutParams) temp.getLayoutParams();
        lp.width=width;
        temp.setLayoutParams(lp);

        temp=(ImageView)findViewById(R.id.imgview);
        Intent intent = getIntent();
        if(intent != null){
            bitmap = intent.getParcelableExtra("bitmap");
            temp.setImageBitmap(bitmap);
        }else if(intent == null){
            Toast.makeText(StyleActivity.this, "图片传输失败了！", Toast.LENGTH_SHORT).show();
        }
        srcBitmap=bitmap;
        interbitmap = srcBitmap;
        size=srcBitmap.getWidth();
        isBinding();

        resources = getBaseContext().getResources();
        selectcolor = resources.getColor(R.color.colorYellow);
        unselectcolor = resources.getColor(R.color.colorAccent);

    }

    private void isBinding(){
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        seekbarnum = (TextView) findViewById(R.id.seekbarnum);
        button0 = (Button) findViewById(R.id.origin);
        button1 = (Button) findViewById(R.id.light);
        button2= (Button) findViewById(R.id.contrast);
        button3= (Button) findViewById(R.id.saturation);
        button4 = (Button) findViewById(R.id.baw);
    }

    public void origin(View view){
        // 按钮显示
        button0.setTextColor(selectcolor);
        button1.setTextColor(unselectcolor);
        button2.setTextColor(unselectcolor);
        button3.setTextColor(unselectcolor);
        button4.setTextColor(unselectcolor);

        // 切换回原图
        temp.setImageBitmap(bitmap);
        srcBitmap = bitmap;
        interbitmap = bitmap;
        seekbar.setProgress(0);
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

    public void light(View view){
        //按钮显示设置
        srcBitmap = interbitmap;
        button1.setTextColor(selectcolor);
        button0.setTextColor(unselectcolor);
        button2.setTextColor(unselectcolor);
        button3.setTextColor(unselectcolor);
        button4.setTextColor(unselectcolor);

        //进度条调节
        seekbar.setProgress(0);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 数值改变
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekbarnum.setText(""+i);
                Bitmap bmp = Bitmap.createBitmap(size, size, Config.ARGB_8888);
                //获取实际用来应用的数值
                int brightness = i;//亮度数值
                ColorMatrix cMatrix = new ColorMatrix();
                //利用数值创建颜色矩阵
                cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness,0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });//亮度矩阵
                //创建画笔
                Paint paint = new Paint();
                //设定矩阵
                paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
                //创建画布
                Canvas canvas = new Canvas(bmp);
                //绘制
                canvas.drawBitmap(srcBitmap, 0, 0, paint);
                temp.setImageBitmap(bmp);
                interbitmap = bmp;
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

    public void contrast(View view){
        //按钮显示设置
        srcBitmap = interbitmap;
        button2.setTextColor(selectcolor);
        button1.setTextColor(unselectcolor);
        button0.setTextColor(unselectcolor);
        button3.setTextColor(unselectcolor);
        button4.setTextColor(unselectcolor);

        //进度条调节
        seekbar.setProgress(0);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 数值改变
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekbarnum.setText(""+i);
                Bitmap bmp = Bitmap.createBitmap(size, size, Config.ARGB_8888);
                //获取实际用来应用的数值
                float contrast = (float) ((i + 100) /100.0);//对比度数值
                ColorMatrix cMatrix = new ColorMatrix();
                //利用数值创建颜色矩阵
                cMatrix.set(new float[] { contrast, 0, 0, 0, 0, 0, contrast, 0, 0, 0, 0, 0, contrast, 0, 0, 0, 0, 0, 1, 0 });//对比度矩阵
                //创建画笔
                Paint paint = new Paint();
                //设定矩阵
                paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
                //创建画布
                Canvas canvas = new Canvas(bmp);
                //绘制
                canvas.drawBitmap(srcBitmap, 0, 0, paint);
                temp.setImageBitmap(bmp);
                interbitmap = bmp;
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

    public void saturation(View view){
        //按钮显示设置
        srcBitmap = interbitmap;
        button3.setTextColor(selectcolor);
        button1.setTextColor(unselectcolor);
        button2.setTextColor(unselectcolor);
        button0.setTextColor(unselectcolor);
        button4.setTextColor(unselectcolor);
        //进度条调节
        seekbar.setProgress(0);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 数值改变
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekbarnum.setText(""+i);
                Bitmap bmp = Bitmap.createBitmap(size, size, Config.ARGB_8888);
                ColorMatrix cMatrix = new ColorMatrix();
                //利用数值创建颜色矩阵
                cMatrix.setSaturation((float) ((i+44.44) *2.25 / 100.0));//饱和度矩阵
                //创建画笔
                Paint paint = new Paint();
                //设定矩阵
                paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
                //创建画布
                Canvas canvas = new Canvas(bmp);
                //绘制
                canvas.drawBitmap(srcBitmap, 0, 0, paint);
                temp.setImageBitmap(bmp);
                interbitmap = bmp;
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

    public void baw(View view){
        //按钮显示设置
        srcBitmap = interbitmap;
        button4.setTextColor(selectcolor);
        button1.setTextColor(unselectcolor);
        button2.setTextColor(unselectcolor);
        button3.setTextColor(unselectcolor);
        button0.setTextColor(unselectcolor);
        //改变图片
        Bitmap bmp = Bitmap.createBitmap(size, size, Config.ARGB_8888);
        ColorMatrix cMatrix = new ColorMatrix();
        //利用数值创建颜色矩阵
        cMatrix.setSaturation(0);//黑白（即饱和度为0）
        //创建画笔
        Paint paint = new Paint();
        //设定矩阵
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        //创建画布
        Canvas canvas = new Canvas(bmp);
        //绘制
        canvas.drawBitmap(srcBitmap, 0, 0, paint);
        temp.setImageBitmap(bmp);
        interbitmap = bmp;
        seekbar.setProgress(0);
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
