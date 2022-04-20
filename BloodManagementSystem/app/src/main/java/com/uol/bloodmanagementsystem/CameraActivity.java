package com.uol.bloodmanagementsystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CameraActivity extends AppCompatActivity {

    ImageView picture;
    Button cameraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        picture = (ImageView)findViewById(R.id.picView);
        cameraBtn = (Button)findViewById(R.id.btnCamera);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                startActivity(intent);

//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            picture.setImageBitmap(bitmap);
        }
//        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//        picture.setImageBitmap(bitmap);
    }
}
