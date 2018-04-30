package com.example.android.styletransfer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.IDNA;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ChooseActivity extends AppCompatActivity {

    private ImageView temp;
    private Button okbutton;
    private int clicknum = 1;
    public Bitmap bitmap;
    public ImgFile imgfile = new ImgFile();
    private File tmp;// 裁剪后的文件 发送到服务器后可以删除

    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;

    private static final String TAG = "ChooseActivity";
    private static final String REQUEST_URL = "http://10.180.163.147:5000/upload";
    private Handler handler;
    private final Map<String, String> params = new HashMap<String, String>();
    private final Map<String, File> files = new HashMap<String, File>();
    private final Map<String, File> files2 = new HashMap<String, File>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        temp = (ImageView)findViewById(R.id.imgview);
        LayoutParams lp=(LayoutParams) temp.getLayoutParams();
        lp.width=width;
        lp.height=width;
        temp.setLayoutParams(lp);

        isBinding();
        onClick();

    }

    private void isBinding(){
        okbutton=(Button) findViewById(R.id.okbutton);
    }

    private void onClick(){
        okbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(clicknum == 1)
                    Toast.makeText(ChooseActivity.this, "请先选择一张照片！", Toast.LENGTH_SHORT).show();
                else {
                    // 向服务器上传原图和裁剪后的图片
                    // origin img
                    params.clear();
                    files.put("file", imgfile.getFile());

                    handler = new Handler();
                    handler.post(new UploadRunnable());

                    Intent intent = new Intent(ChooseActivity.this, StyleActivity.class);
                    intent.putExtra("bitmap",bitmap);
                    startActivity(intent);
                }
            }
        });
    }

    class UploadRunnable implements Runnable {  // 用于上传文件的多线程
        @Override
        public void run() {
            try {
                final String request = UploadUtils.post(REQUEST_URL, params, files);
                files.clear();
                files.put("file", imgfile.getCroppedFile());
                final String req = UploadUtils.post(REQUEST_URL, params, files);
              //  Toast.makeText(ChooseActivity.this, request, Toast.LENGTH_LONG).show();
            }catch (IOException e){
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void gallery(View view) {
        // 激活系统图库，选择一张图片
        clicknum++;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    public void camera(View view){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if(hasSdcard()){
            tempFile = new File(Environment.getExternalStorageDirectory(),PHOTO_FILE_NAME);
            // 创建imgfile
            imgfile.setFile(tempFile);
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        clicknum++;
        // 开启一个带有返回值的activity
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 350);
        intent.putExtra("outputY", 350);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();

                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                String img_path = actualimagecursor.getString(actual_image_column_index);

                File tmp = new File(img_path);
                imgfile.setFile(tmp);

                crop(uri);
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (hasSdcard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(ChooseActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                bitmap = data.getParcelableExtra("data");
                this.temp.setImageBitmap(bitmap);
            }
            tmp = new File("/storage/emulated/0/DCIM/Camera/cropped.jpg");
            try{
                BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(tmp));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            imgfile.setCroppedFile(tmp);
//            try {
//                // 将临时文件删除
//                //tempFile.delete();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
