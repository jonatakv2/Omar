package com.example;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class PhotoActivity extends AppCompatActivity {

    private String nome;

    private TextView txt;
    private Button btn;
    private ImageView img;

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        txt = (TextView) findViewById(R.id.txt);
        btn = (Button) findViewById(R.id.btn);
        img = (ImageView) findViewById(R.id.img);

        sharedPreferences = getSharedPreferences("PHOTO", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();

        if(getIntent().getExtras() != null){
            nome = getIntent().getExtras().getString("Nome");
            setTitle("Rua: " + nome);
            txt.setText("Tirou " + sharedPreferences.getInt(nome,0) + " fotos");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(Intent, CAMERA);
                }

            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case CAMERA:
                    Bundle ex = data.getExtras();
                    Bitmap bitmao = (Bitmap) ex.get("data");
                    img.setImageBitmap(bitmao);

                    int cont = sharedPreferences.getInt(nome,0);
                    setTitle(nome);
                    editor.putInt(nome,++cont);
                    editor.apply();

                    txt.setText("Tirou " + sharedPreferences.getInt(nome,0) + " fotos");
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PhotoActivity.this, PlaceOnMapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }
}