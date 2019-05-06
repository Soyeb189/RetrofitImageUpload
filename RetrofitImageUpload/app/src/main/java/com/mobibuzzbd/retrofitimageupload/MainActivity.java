package com.mobibuzzbd.retrofitimageupload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText phone;
    private Button chooseButton,uploadButton;
    private static ApiInterface apiInterface;
    private ImageView img;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = ApiClint.getApiClint().create(ApiInterface.class);
        phone = findViewById(R.id.phoneNumber);
        chooseButton = findViewById(R.id.chooseBn);
        uploadButton = findViewById(R.id.uploadBn);
        img = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressbar);
        chooseButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.chooseBn:
                selectImage();
                break;

            case R.id.uploadBn:
                uploadImage();
                break;
        }
    }

    private void uploadImage(){
        String Image = imageToString();
        String Phone = phone.getText().toString().trim();
        Call<ImageClass> call = apiInterface.uploadImage(Phone,Image);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {

                if (response.isSuccessful()){
                    if (response.body().getResponse().equals("ok")){

                        Toast.makeText(MainActivity.this, "Server Response", Toast.LENGTH_SHORT).show();
                        img.setVisibility(View.GONE);
                        phone.setVisibility(View.GONE);
                        uploadButton.setEnabled(false);
                        phone.setText("");
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    else if (response.body().getResponse().equals("failed")){

                        Toast.makeText(MainActivity.this, "f", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Failed"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){

            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
                phone.setVisibility(View.VISIBLE);
                uploadButton.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imagebyte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagebyte,Base64.DEFAULT);
    }
}
