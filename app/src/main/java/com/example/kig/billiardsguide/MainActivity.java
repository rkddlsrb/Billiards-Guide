package com.example.kig.billiardsguide;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener {
    private Button btn1;
    private ImageView imageView;

    private long pressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button)findViewById(R.id.btn1);
        imageView=(ImageView)findViewById(R.id.view);
        btn1.setOnClickListener(this);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Button Button_Camera = (Button) findViewById(R.id.camera);
        Button_Camera.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, 0);
                    }
                }
        );
    }

    @Override
    public void onClick(View view){
        ball_select dialog = new ball_select( this ) ;
        dialog.setOnDismissListener( this ) ;

        Drawable d = imageView.getDrawable();

        if(d==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder .setTitle("알림")
                    .setMessage("당구대 촬영 버튼을 눌러서 당구대를 먼저 촬영 해주세요.")
                    .setPositiveButton("확인", null);
            builder.create();
            builder.show();
        }
        else {
            dialog.show();
        }
    }

    @Override
    public void onDismiss(DialogInterface $dialog) {
        ball_select dialog = (ball_select) $dialog ;
        String hit_ball_color = dialog.getNum();

        Drawable d = imageView.getDrawable();

        Intent intent = new Intent(MainActivity.this, imageanalysis.class);

        Bitmap bmm = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        intent.putExtra("image", byteArray);

        if(hit_ball_color.equals("1")) {
            intent.putExtra("hitball", "1");
        }
        else{
            intent.putExtra("hitball", "2");
        }

        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0) {
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bm);
        }
    }

    @Override
    public void onBackPressed(){
        if(pressedTime == 0){
            Toast.makeText(MainActivity.this,"한번 더 누르면 종료합니다",Toast.LENGTH_LONG).show();
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
