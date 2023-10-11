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

import com.example.detection_of_autism.ml.LiteModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

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

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img = Bitmap.createScaledBitmap(img, 128, 128, true);
                try {
                    LiteModel model = LiteModel.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 150, 150, 3}, DataType.FLOAT32);
                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    tensorImage.load(img);

                    ByteBuffer byteBuffer = tensorImage.getBuffer();


                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    LiteModel.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    // Releases model resources if no longer used.
                    model.close();

                    resView.setText(outputFeature0.getFloatArray()[0] + "\n" + outputFeature0.getFloatArray()[1]);
                } catch (IOException e) {
                    // TODO Handle the exception
                }

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