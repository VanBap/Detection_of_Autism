package com.example.detection_of_autism;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.detection_of_autism.ml.LiteModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MainActivity2 extends AppCompatActivity {

    private Button selectBtn;
    private Button predictBtn;
    private Button captureBtn;
    private Button recordBtn;
    private TextView resView;
    private ImageView imageView;
    private VideoView videoView;
    private Bitmap bitmap;
    int REQUEST_CODE_VIDEO_CAPTURE = 2607;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // permission
        getPermission();

        selectBtn = findViewById(R.id.selectBtn);
        predictBtn = (Button) findViewById(R.id.predictBtn);
        captureBtn = (Button) findViewById(R.id.captureBtn);
        recordBtn = (Button) findViewById(R.id.recordBtn);
        resView = (TextView) findViewById(R.id.resView);
        imageView = (ImageView) findViewById(R.id.imageView);
        videoView = (VideoView) findViewById(R.id.videoView);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        //SELECT button
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        //CAPTURE button
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 12);
            }
        });

        //RECORD button
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, 13);
            }
        });

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
                try {
                    LiteModel model = LiteModel.newInstance(MainActivity2.this);

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 150, 150, 3}, DataType.FLOAT32);
                // ==========================================================================================
                    //TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    //tensorImage.load(bitmap);

                    //ByteBuffer byteBuffer = tensorImage.getBuffer();


                    //inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    //LiteModel.Outputs outputs = model.process(inputFeature0);
                    //TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                // ==========================================================================================

                    bitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
                    inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());
                    
                    // Runs model inference and gets result.
                    LiteModel.Outputs outputs = model.process(inputFeature0);
                    // Đang thắc mắc sẽ trả kết quả hiển thị kiểu gì ?
                    resView.setText("Van dep trai");

                    // Releases model resources if no longer used.
                    model.close();


                } catch (IOException e) {
                    // TODO Handle the exception
                }

            }
        });
    }

    // permession
    void getPermission(){
        //if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.CAMERA}, 11);

            }
        //}
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==11){
            if(grantResults.length>0){
                if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void recordVideo(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 10){
            if(data!=null) {
                imageView.setImageURI(data.getData());
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == 12){
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
        else if (requestCode == REQUEST_CODE_VIDEO_CAPTURE && resultCode == RESULT_OK){
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}