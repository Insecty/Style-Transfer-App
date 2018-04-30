package com.example.android.styletransfer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import android.content.Context;
import java.io.FileOutputStream;
import java.io.IOException;
import android.provider.MediaStore;


public class SaveActivity extends AppCompatActivity {

    private ImageView origin;  // 图片显示view
    private ImageView result;  // 图片显示view
    public Bitmap originBitmap;    // 原图
    private Bitmap resultBitmap; // 结果图
    public int size;
    public int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        result = (ImageView) findViewById(R.id.imgview);
        origin= (ImageView) findViewById(R.id.originview);
        LayoutParams lp = (LayoutParams) result.getLayoutParams();
        lp.width = width;
        result.setLayoutParams(lp);
        LayoutParams lp0 = (LayoutParams) origin.getLayoutParams();
        lp0.width = width;
        origin.setLayoutParams(lp0);

        Intent intent = getIntent();
        if (intent != null) {
            originBitmap=intent.getParcelableExtra("origin");
            resultBitmap = intent.getParcelableExtra("result");
            result.setImageBitmap(resultBitmap);
        } else if (intent == null) {
            Toast.makeText(SaveActivity.this, "图片传输失败了！", Toast.LENGTH_SHORT).show();
        }
        size = resultBitmap.getWidth();
        isBinding();

    }

    private void isBinding() {
    }
    //原图对比
    public void showorigin(View view){
        origin.setImageBitmap(originBitmap);
    }
    //重新调色
    public void again(View view){
        Intent intent = new Intent(SaveActivity.this, StyleActivity.class);
        intent.putExtra("bitmap",originBitmap);
        startActivity(intent);
    }
    //换一张图
    public void newpage(View view){
        Intent intent = new Intent(SaveActivity.this, ChooseActivity.class);
        startActivity(intent);
    }
    //保存到相册
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void save(View view){
        if(saveImageToGallery(SaveActivity.this, resultBitmap)) {
            Toast.makeText(SaveActivity.this, "已保存到相册！", Toast.LENGTH_SHORT).show();
        }
    }



}