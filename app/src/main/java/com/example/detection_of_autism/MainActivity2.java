package com.example.detection_of_autism;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {

    private Button selectBtn;
    private Button predictBtn;
    private TextView resView;
    private ImageView imageView;
    private Bitmap img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        selectBtn = findViewById(R.id.selectBtn);
        predictBtn = (Button) findViewById(R.id.predictBtn);
        resView = (TextView) findViewById(R.id.resView);
        imageView = (ImageView) findViewById(R.id.imageView);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                startActivityForResult(intent, 100);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100){
            imageView.setImageURI(data.getData());
            Uri uri = data.getData();
            try{
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}