package com.example.kig.billiardsguide;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.osgi.OpenCVNativeLoader;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class imageanalysis extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "BilliardsGuide";

    private Button next;

    private ImageView imageView;
    private ImageView iv;
    private ImageView ball_point;
    private ImageView thickness;
    private ImageView power;

    private Mat img_pool;
    private Mat img_rb, img_yb, img_wb;
    private Mat img_input;
    private Mat img_output, img_output1, img_output2, img_output3, img_output4, img_output5, img_output6, img_output7, img_output8, img_output9;
    private int[] ball_addr = new int[31];

    private long pressedTime;

    int hitball;
    int cnt=0;

    public native int[] ImageBinarize(long inputImage, long outputImage, long poolimage, long ybimage, long wbimage, long rbimage, int hitball,
                                      long outputImage1, long outputImage2, long outputImage3, long outputImage4, long outputImage5, long outputImage6,
                                      long outputImage7, long outputImage8, long outputImage9);

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG,"OpenCV is not loaded!");
        }else{
            Log.d(TAG,"OpenCV is loaded successfully!");
        }
    }

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageanalysis);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //당구공, 당구대 이미지 불러오기
        Drawable drawable_pool = getResources().getDrawable(R.drawable.pool1);
        Drawable drawable_rb = getResources().getDrawable(R.drawable.rball);
        Drawable drawable_yb = getResources().getDrawable(R.drawable.yball);
        Drawable drawable_wb = getResources().getDrawable(R.drawable.wball);
        Bitmap pic1 = ((BitmapDrawable)drawable_pool).getBitmap();
        Bitmap pic2 = ((BitmapDrawable)drawable_yb).getBitmap();
        Bitmap pic3 = ((BitmapDrawable)drawable_wb).getBitmap();
        Bitmap pic4 = ((BitmapDrawable)drawable_rb).getBitmap();

        imageView = (ImageView) findViewById(R.id.view);
        Intent intent = getIntent();
        Bitmap temp;
        String hit_ball_color;
        //pic = (Bitmap) intent.getParcelableExtra("image");
        hit_ball_color = intent.getExtras().getString("hitball");

        byte[] arr = getIntent().getByteArrayExtra("image");
        temp = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        imageView.setImageBitmap(temp);

        img_pool = new Mat();
        img_rb = new Mat();
        img_yb = new Mat();
        img_wb = new Mat();
        img_input = new Mat();
        img_output = new Mat();
        img_output1 = new Mat();
        img_output2 = new Mat();
        img_output3 = new Mat();
        img_output4 = new Mat();
        img_output5 = new Mat();
        img_output6 = new Mat();
        img_output7 = new Mat();
        img_output8 = new Mat();
        img_output9 = new Mat();


        Drawable drawable_11 = imageView.getDrawable();
        Bitmap pic = ((BitmapDrawable)drawable_11).getBitmap();

        //test
        Drawable drawable_test = getResources().getDrawable(R.drawable.test333);
        Bitmap pic5 = ((BitmapDrawable)drawable_test).getBitmap();

        //test시 pic->pic5로 변경
        Bitmap bmp32 = pic.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, img_input);

        //당구대,당구공 기본 이미지
        Bitmap bmp_pool = pic1.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp_pool, img_pool);
        Bitmap bmp_yb = pic2.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp_yb, img_yb);
        Bitmap bmp_wb = pic3.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp_wb, img_wb);
        Bitmap bmp_rb = pic4.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp_rb, img_rb);


        //팝업창으로 칠 공이 노란색이면1, 흰색이면2 부여하기
        if(hit_ball_color.equals("1")) {
            hitball = 1;
        }
        else {
            hitball = 2;
        }

        ball_addr = ImageBinarize(img_input.getNativeObjAddr(), img_output.getNativeObjAddr(), img_pool.getNativeObjAddr(), img_yb.getNativeObjAddr(), img_wb.getNativeObjAddr(), img_rb.getNativeObjAddr(), hitball,
                img_output1.getNativeObjAddr(), img_output2.getNativeObjAddr(), img_output3.getNativeObjAddr(), img_output4.getNativeObjAddr(), img_output5.getNativeObjAddr(), img_output6.getNativeObjAddr(),
                img_output7.getNativeObjAddr(), img_output8.getNativeObjAddr(), img_output9.getNativeObjAddr());

        /*
        try {
            ball_addr = ImageBinarize(img_input.getNativeObjAddr(), img_output.getNativeObjAddr(), img_pool.getNativeObjAddr(), img_yb.getNativeObjAddr(), img_wb.getNativeObjAddr(), img_rb.getNativeObjAddr(), hitball,
                    img_output1.getNativeObjAddr(), img_output2.getNativeObjAddr(), img_output3.getNativeObjAddr(), img_output4.getNativeObjAddr(), img_output5.getNativeObjAddr(), img_output6.getNativeObjAddr(),
                    img_output7.getNativeObjAddr(), img_output8.getNativeObjAddr(), img_output9.getNativeObjAddr());
        } catch(Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(imageanalysis.this);
            builder .setTitle("당구대 인식 오류")
                    .setMessage("당구대 모든 부분이 나올 수 있도록 정확하게 촬영해주세요.")
                    //.setPositiveButton("확인", null);
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i1);
                }});
            builder.create();
            builder.show();
        }
        */

        if(ball_addr[30] == -1){
            AlertDialog.Builder builder = new AlertDialog.Builder(imageanalysis.this);
            builder .setTitle("당구대 인식 오류")
                    .setMessage("당구대, 당구공의 모든 부분이 나올 수 있도록 정확하게 촬영해주세요.")
                    //.setPositiveButton("확인", null);
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i1);
                            finish();
                        }});
            builder.create();
            builder.show();
        }

        Bitmap bitmapOutput = Bitmap.createBitmap(img_output.cols(), img_output.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_output, bitmapOutput);
        imageView.setImageBitmap(bitmapOutput);

        ball_point =(ImageView) findViewById(R.id.ball_point);
        if(hitball == 1) {
            if (ball_addr[0] == 1) {
                ball_point.setImageResource(R.drawable.yball_lspin);
            } else if (ball_addr[0] == 2) {
                ball_point.setImageResource(R.drawable.yball_rspin);
            } else {
                ball_point.setImageResource(R.drawable.yball_nospin);
            }
        }
        else {
            if (ball_addr[0] == 1) {
                ball_point.setImageResource(R.drawable.wball_lspin);
            } else if (ball_addr[0] == 2) {
                ball_point.setImageResource(R.drawable.wball_rspin);
            } else {
                ball_point.setImageResource(R.drawable.wball_nospin);
            }
        }

        thickness =(ImageView) findViewById(R.id.thickness);
        if(ball_addr[1] <= 12){
            thickness.setImageResource(R.drawable.thickness1);
        }
        else if(ball_addr[1] <= 20){
            thickness.setImageResource(R.drawable.thickness2);
        }
        else if(ball_addr[1] <= 30){
            thickness.setImageResource(R.drawable.thickness3);
        }
        else if(ball_addr[1] <= 40){
            thickness.setImageResource(R.drawable.thickness4);
        }
        else{
            thickness.setImageResource(R.drawable.thickness5);
        }

        power =(ImageView) findViewById(R.id.power);
        if(ball_addr[2] == 5){
            power.setImageResource(R.drawable.power1);
        }
        else if(ball_addr[2] == 6){
            power.setImageResource(R.drawable.power2);
        }
        else if(ball_addr[2] == 7){
            power.setImageResource(R.drawable.power3);
        }
        else if(ball_addr[2] == 8){
            power.setImageResource(R.drawable.power4);
        }
        else if(ball_addr[2] == 9){
            power.setImageResource(R.drawable.power5);
        }

        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(this);

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //돌아가기 구현
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
        );
    }

    @Override
    public void onClick(View view){
        cnt++;
        if(cnt > ball_addr[30] || cnt >= 10){
            AlertDialog.Builder builder = new AlertDialog.Builder(imageanalysis.this);
            builder .setTitle("알림")
                    .setMessage("더 이상 표시할 경로가 없습니다.")
                    .setPositiveButton("확인", null);
            builder.create();
            builder.show();
        }
        else {
            iv = (ImageView) findViewById(R.id.view);

            if(cnt == 1){
                Bitmap bitmapOutput = Bitmap.createBitmap(img_output1.cols(), img_output1.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(img_output1, bitmapOutput);
                imageView.setImageBitmap(bitmapOutput);

                ball_point =(ImageView) findViewById(R.id.ball_point);
                if(hitball == 1) {
                    if (ball_addr[3] == 1) {
                        ball_point.setImageResource(R.drawable.yball_lspin);
                    } else if (ball_addr[3] == 2) {
                        ball_point.setImageResource(R.drawable.yball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.yball_nospin);
                    }
                }
                else {
                    if (ball_addr[3] == 1) {
                        ball_point.setImageResource(R.drawable.wball_lspin);
                    } else if (ball_addr[3] == 2) {
                        ball_point.setImageResource(R.drawable.wball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.wball_nospin);
                    }
                }

                thickness =(ImageView) findViewById(R.id.thickness);
                if(ball_addr[4] <= 12){
                    thickness.setImageResource(R.drawable.thickness1);
                }
                else if(ball_addr[4] <= 20){
                    thickness.setImageResource(R.drawable.thickness2);
                }
                else if(ball_addr[4] <= 30){
                    thickness.setImageResource(R.drawable.thickness3);
                }
                else if(ball_addr[4] <= 40){
                    thickness.setImageResource(R.drawable.thickness4);
                }
                else{
                    thickness.setImageResource(R.drawable.thickness5);
                }

                power =(ImageView) findViewById(R.id.power);
                if(ball_addr[5] == 5){
                    power.setImageResource(R.drawable.power1);
                }
                else if(ball_addr[5] == 6){
                    power.setImageResource(R.drawable.power2);
                }
                else if(ball_addr[5] == 7){
                    power.setImageResource(R.drawable.power3);
                }
                else if(ball_addr[5] == 8){
                    power.setImageResource(R.drawable.power4);
                }
                else if(ball_addr[5] == 9){
                    power.setImageResource(R.drawable.power5);
                }
            }
            else if(cnt==2){
                Bitmap bitmapOutput = Bitmap.createBitmap(img_output2.cols(), img_output2.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(img_output2, bitmapOutput);
                imageView.setImageBitmap(bitmapOutput);

                ball_point =(ImageView) findViewById(R.id.ball_point);
                if(hitball == 1) {
                    if (ball_addr[6] == 1) {
                        ball_point.setImageResource(R.drawable.yball_lspin);
                    } else if (ball_addr[6] == 2) {
                        ball_point.setImageResource(R.drawable.yball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.yball_nospin);
                    }
                }
                else {
                    if (ball_addr[6] == 1) {
                        ball_point.setImageResource(R.drawable.wball_lspin);
                    } else if (ball_addr[6] == 2) {
                        ball_point.setImageResource(R.drawable.wball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.wball_nospin);
                    }
                }

                thickness =(ImageView) findViewById(R.id.thickness);
                if(ball_addr[7] <= 12){
                    thickness.setImageResource(R.drawable.thickness1);
                }
                else if(ball_addr[7] <= 20){
                    thickness.setImageResource(R.drawable.thickness2);
                }
                else if(ball_addr[7] <= 30){
                    thickness.setImageResource(R.drawable.thickness3);
                }
                else if(ball_addr[7] <= 40){
                    thickness.setImageResource(R.drawable.thickness4);
                }
                else{
                    thickness.setImageResource(R.drawable.thickness5);
                }

                power =(ImageView) findViewById(R.id.power);
                if(ball_addr[8] == 5){
                    power.setImageResource(R.drawable.power1);
                }
                else if(ball_addr[8] == 6){
                    power.setImageResource(R.drawable.power2);
                }
                else if(ball_addr[8] == 7){
                    power.setImageResource(R.drawable.power3);
                }
                else if(ball_addr[8] == 8){
                    power.setImageResource(R.drawable.power4);
                }
                else if(ball_addr[8] == 9){
                    power.setImageResource(R.drawable.power5);
                }
            }
            else if(cnt==3){
                Bitmap bitmapOutput = Bitmap.createBitmap(img_output3.cols(), img_output3.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(img_output3, bitmapOutput);
                imageView.setImageBitmap(bitmapOutput);

                ball_point =(ImageView) findViewById(R.id.ball_point);
                if(hitball == 1) {
                    if (ball_addr[9] == 1) {
                        ball_point.setImageResource(R.drawable.yball_lspin);
                    } else if (ball_addr[9] == 2) {
                        ball_point.setImageResource(R.drawable.yball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.yball_nospin);
                    }
                }
                else {
                    if (ball_addr[9] == 1) {
                        ball_point.setImageResource(R.drawable.wball_lspin);
                    } else if (ball_addr[9] == 2) {
                        ball_point.setImageResource(R.drawable.wball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.wball_nospin);
                    }
                }

                thickness =(ImageView) findViewById(R.id.thickness);
                if(ball_addr[10] <= 12){
                    thickness.setImageResource(R.drawable.thickness1);
                }
                else if(ball_addr[10] <= 20){
                    thickness.setImageResource(R.drawable.thickness2);
                }
                else if(ball_addr[10] <= 30){
                    thickness.setImageResource(R.drawable.thickness3);
                }
                else if(ball_addr[10] <= 40){
                    thickness.setImageResource(R.drawable.thickness4);
                }
                else{
                    thickness.setImageResource(R.drawable.thickness5);
                }

                power =(ImageView) findViewById(R.id.power);
                if(ball_addr[11] == 5){
                    power.setImageResource(R.drawable.power1);
                }
                else if(ball_addr[11] == 6){
                    power.setImageResource(R.drawable.power2);
                }
                else if(ball_addr[11] == 7){
                    power.setImageResource(R.drawable.power3);
                }
                else if(ball_addr[11] == 8){
                    power.setImageResource(R.drawable.power4);
                }
                else if(ball_addr[11] == 9){
                    power.setImageResource(R.drawable.power5);
                }
            }
            else if(cnt==4){
                Bitmap bitmapOutput = Bitmap.createBitmap(img_output4.cols(), img_output4.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(img_output4, bitmapOutput);
                imageView.setImageBitmap(bitmapOutput);

                ball_point =(ImageView) findViewById(R.id.ball_point);
                if(hitball == 1) {
                    if (ball_addr[12] == 1) {
                        ball_point.setImageResource(R.drawable.yball_lspin);
                    } else if (ball_addr[12] == 2) {
                        ball_point.setImageResource(R.drawable.yball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.yball_nospin);
                    }
                }
                else {
                    if (ball_addr[12] == 1) {
                        ball_point.setImageResource(R.drawable.wball_lspin);
                    } else if (ball_addr[12] == 2) {
                        ball_point.setImageResource(R.drawable.wball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.wball_nospin);
                    }
                }

                thickness =(ImageView) findViewById(R.id.thickness);
                if(ball_addr[13] <= 12){
                    thickness.setImageResource(R.drawable.thickness1);
                }
                else if(ball_addr[13] <= 20){
                    thickness.setImageResource(R.drawable.thickness2);
                }
                else if(ball_addr[13] <= 30){
                    thickness.setImageResource(R.drawable.thickness3);
                }
                else if(ball_addr[13] <= 40){
                    thickness.setImageResource(R.drawable.thickness4);
                }
                else{
                    thickness.setImageResource(R.drawable.thickness5);
                }

                power =(ImageView) findViewById(R.id.power);
                if(ball_addr[14] == 5){
                    power.setImageResource(R.drawable.power1);
                }
                else if(ball_addr[14] == 6){
                    power.setImageResource(R.drawable.power2);
                }
                else if(ball_addr[14] == 7){
                    power.setImageResource(R.drawable.power3);
                }
                else if(ball_addr[14] == 8){
                    power.setImageResource(R.drawable.power4);
                }
                else if(ball_addr[14] == 9){
                    power.setImageResource(R.drawable.power5);
                }
            }
            else if(cnt==5){
                Bitmap bitmapOutput = Bitmap.createBitmap(img_output5.cols(), img_output5.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(img_output5, bitmapOutput);
                imageView.setImageBitmap(bitmapOutput);

                ball_point =(ImageView) findViewById(R.id.ball_point);
                if(hitball == 1) {
                    if (ball_addr[15] == 1) {
                        ball_point.setImageResource(R.drawable.yball_lspin);
                    } else if (ball_addr[15] == 2) {
                        ball_point.setImageResource(R.drawable.yball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.yball_nospin);
                    }
                }
                else {
                    if (ball_addr[15] == 1) {
                        ball_point.setImageResource(R.drawable.wball_lspin);
                    } else if (ball_addr[15] == 2) {
                        ball_point.setImageResource(R.drawable.wball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.wball_nospin);
                    }
                }

                thickness =(ImageView) findViewById(R.id.thickness);
                if(ball_addr[16] <= 12){
                    thickness.setImageResource(R.drawable.thickness1);
                }
                else if(ball_addr[16] <= 20){
                    thickness.setImageResource(R.drawable.thickness2);
                }
                else if(ball_addr[16] <= 30){
                    thickness.setImageResource(R.drawable.thickness3);
                }
                else if(ball_addr[16] <= 40){
                    thickness.setImageResource(R.drawable.thickness4);
                }
                else{
                    thickness.setImageResource(R.drawable.thickness5);
                }

                power =(ImageView) findViewById(R.id.power);
                if(ball_addr[17] == 5){
                    power.setImageResource(R.drawable.power1);
                }
                else if(ball_addr[17] == 6){
                    power.setImageResource(R.drawable.power2);
                }
                else if(ball_addr[17] == 7){
                    power.setImageResource(R.drawable.power3);
                }
                else if(ball_addr[17] == 8){
                    power.setImageResource(R.drawable.power4);
                }
                else if(ball_addr[17] == 9){
                    power.setImageResource(R.drawable.power5);
                }
            }
            else if(cnt==6){
                Bitmap bitmapOutput = Bitmap.createBitmap(img_output6.cols(), img_output6.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(img_output6, bitmapOutput);
                imageView.setImageBitmap(bitmapOutput);

                ball_point =(ImageView) findViewById(R.id.ball_point);
                if(hitball == 1) {
                    if (ball_addr[18] == 1) {
                        ball_point.setImageResource(R.drawable.yball_lspin);
                    } else if (ball_addr[18] == 2) {
                        ball_point.setImageResource(R.drawable.yball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.yball_nospin);
                    }
                }
                else {
                    if (ball_addr[18] == 1) {
                        ball_point.setImageResource(R.drawable.wball_lspin);
                    } else if (ball_addr[18] == 2) {
                        ball_point.setImageResource(R.drawable.wball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.wball_nospin);
                    }
                }

                thickness =(ImageView) findViewById(R.id.thickness);
                if(ball_addr[19] <= 12){
                    thickness.setImageResource(R.drawable.thickness1);
                }
                else if(ball_addr[19] <= 20){
                    thickness.setImageResource(R.drawable.thickness2);
                }
                else if(ball_addr[19] <= 30){
                    thickness.setImageResource(R.drawable.thickness3);
                }
                else if(ball_addr[19] <= 40){
                    thickness.setImageResource(R.drawable.thickness4);
                }
                else{
                    thickness.setImageResource(R.drawable.thickness5);
                }

                power =(ImageView) findViewById(R.id.power);
                if(ball_addr[20] == 5){
                    power.setImageResource(R.drawable.power1);
                }
                else if(ball_addr[20] == 6){
                    power.setImageResource(R.drawable.power2);
                }
                else if(ball_addr[20] == 7){
                    power.setImageResource(R.drawable.power3);
                }
                else if(ball_addr[20] == 8){
                    power.setImageResource(R.drawable.power4);
                }
                else if(ball_addr[20] == 9){
                    power.setImageResource(R.drawable.power5);
                }
            }
            else if(cnt==7){
                Bitmap bitmapOutput = Bitmap.createBitmap(img_output7.cols(), img_output7.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(img_output7, bitmapOutput);
                imageView.setImageBitmap(bitmapOutput);

                ball_point =(ImageView) findViewById(R.id.ball_point);
                if(hitball == 1) {
                    if (ball_addr[21] == 1) {
                        ball_point.setImageResource(R.drawable.yball_lspin);
                    } else if (ball_addr[21] == 2) {
                        ball_point.setImageResource(R.drawable.yball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.yball_nospin);
                    }
                }
                else {
                    if (ball_addr[21] == 1) {
                        ball_point.setImageResource(R.drawable.wball_lspin);
                    } else if (ball_addr[21] == 2) {
                        ball_point.setImageResource(R.drawable.wball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.wball_nospin);
                    }
                }

                thickness =(ImageView) findViewById(R.id.thickness);
                if(ball_addr[22] <= 12){
                    thickness.setImageResource(R.drawable.thickness1);
                }
                else if(ball_addr[22] <= 20){
                    thickness.setImageResource(R.drawable.thickness2);
                }
                else if(ball_addr[22] <= 30){
                    thickness.setImageResource(R.drawable.thickness3);
                }
                else if(ball_addr[22] <= 40){
                    thickness.setImageResource(R.drawable.thickness4);
                }
                else{
                    thickness.setImageResource(R.drawable.thickness5);
                }

                power =(ImageView) findViewById(R.id.power);
                if(ball_addr[23] == 5){
                    power.setImageResource(R.drawable.power1);
                }
                else if(ball_addr[23] == 6){
                    power.setImageResource(R.drawable.power2);
                }
                else if(ball_addr[23] == 7){
                    power.setImageResource(R.drawable.power3);
                }
                else if(ball_addr[23] == 8){
                    power.setImageResource(R.drawable.power4);
                }
                else if(ball_addr[23] == 9){
                    power.setImageResource(R.drawable.power5);
                }
            }
            else if(cnt==8){
                Bitmap bitmapOutput = Bitmap.createBitmap(img_output8.cols(), img_output8.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(img_output8, bitmapOutput);
                imageView.setImageBitmap(bitmapOutput);

                ball_point =(ImageView) findViewById(R.id.ball_point);
                if(hitball == 1) {
                    if (ball_addr[24] == 1) {
                        ball_point.setImageResource(R.drawable.yball_lspin);
                    } else if (ball_addr[24] == 2) {
                        ball_point.setImageResource(R.drawable.yball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.yball_nospin);
                    }
                }
                else {
                    if (ball_addr[24] == 1) {
                        ball_point.setImageResource(R.drawable.wball_lspin);
                    } else if (ball_addr[24] == 2) {
                        ball_point.setImageResource(R.drawable.wball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.wball_nospin);
                    }
                }

                thickness =(ImageView) findViewById(R.id.thickness);
                if(ball_addr[25] <= 12){
                    thickness.setImageResource(R.drawable.thickness1);
                }
                else if(ball_addr[25] <= 20){
                    thickness.setImageResource(R.drawable.thickness2);
                }
                else if(ball_addr[25] <= 30){
                    thickness.setImageResource(R.drawable.thickness3);
                }
                else if(ball_addr[25] <= 40){
                    thickness.setImageResource(R.drawable.thickness4);
                }
                else{
                    thickness.setImageResource(R.drawable.thickness5);
                }

                power =(ImageView) findViewById(R.id.power);
                if(ball_addr[26] == 5){
                    power.setImageResource(R.drawable.power1);
                }
                else if(ball_addr[26] == 6){
                    power.setImageResource(R.drawable.power2);
                }
                else if(ball_addr[26] == 7){
                    power.setImageResource(R.drawable.power3);
                }
                else if(ball_addr[26] == 8){
                    power.setImageResource(R.drawable.power4);
                }
                else if(ball_addr[26] == 9){
                    power.setImageResource(R.drawable.power5);
                }
            }
            else if(cnt==9){
                Bitmap bitmapOutput = Bitmap.createBitmap(img_output9.cols(), img_output9.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(img_output9, bitmapOutput);
                imageView.setImageBitmap(bitmapOutput);

                ball_point =(ImageView) findViewById(R.id.ball_point);
                if(hitball == 1) {
                    if (ball_addr[27] == 1) {
                        ball_point.setImageResource(R.drawable.yball_lspin);
                    } else if (ball_addr[27] == 2) {
                        ball_point.setImageResource(R.drawable.yball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.yball_nospin);
                    }
                }
                else {
                    if (ball_addr[27] == 1) {
                        ball_point.setImageResource(R.drawable.wball_lspin);
                    } else if (ball_addr[27] == 2) {
                        ball_point.setImageResource(R.drawable.wball_rspin);
                    } else {
                        ball_point.setImageResource(R.drawable.wball_nospin);
                    }
                }

                thickness =(ImageView) findViewById(R.id.thickness);
                if(ball_addr[28] <= 12){
                    thickness.setImageResource(R.drawable.thickness1);
                }
                else if(ball_addr[28] <= 20){
                    thickness.setImageResource(R.drawable.thickness2);
                }
                else if(ball_addr[28] <= 30){
                    thickness.setImageResource(R.drawable.thickness3);
                }
                else if(ball_addr[28] <= 40){
                    thickness.setImageResource(R.drawable.thickness4);
                }
                else{
                    thickness.setImageResource(R.drawable.thickness5);
                }

                power =(ImageView) findViewById(R.id.power);
                if(ball_addr[29] == 5){
                    power.setImageResource(R.drawable.power1);
                }
                else if(ball_addr[29] == 6){
                    power.setImageResource(R.drawable.power2);
                }
                else if(ball_addr[29] == 7){
                    power.setImageResource(R.drawable.power3);
                }
                else if(ball_addr[29] == 8){
                    power.setImageResource(R.drawable.power4);
                }
                else if(ball_addr[29] == 9){
                    power.setImageResource(R.drawable.power5);
                }
            }
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }

    @Override
    public void onBackPressed(){
        if(pressedTime == 0){
            Toast.makeText(imageanalysis.this,"한번 더 누르면 종료합니다",Toast.LENGTH_LONG).show();
            pressedTime=System.currentTimeMillis();
        }
        else{
            int seconds = (int)(System.currentTimeMillis() - pressedTime);
            if(seconds > 2000){
                pressedTime=0;
            }
            else{
                System.exit(0);
            }
        }
    }
}

