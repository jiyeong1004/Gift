package com.example.gift;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

public class ManualActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();

    GiftCon giftCon;
    Bitmap image;
    String productName, store, DDAY;
    FirebaseUser owner = auth.getCurrentUser();

    private final int GET_GALLERY_IMAGE = 200;
    Uri selectedImageURI;

    Button uploadImgBtn, registrationBtn;
    EditText productTxt = findViewById(R.id.productText),
            storeTxt = findViewById(R.id.storeText),
            dDayTxt = findViewById(R.id.dDayText);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        //자동등록에서 오씨알 해서 여기로 넘어오기
        uploadImgBtn = findViewById(R.id.uploadImageButton);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        registrationBtn = findViewById(R.id.registrationButton);
        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = productTxt.getText().toString();
                store = storeTxt.getText().toString();
                DDAY = dDayTxt.getText().toString();
                image = BitmapFactory.decodeFile(getRealPathFromURI(selectedImageURI));
                if(productName.equals("") || store.equals("") || DDAY.equals("") || image == null){
                    Toast.makeText(ManualActivity.this, "모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                giftCon = new GiftCon(image, productName, store, DDAY, owner, true);
                giftCon.uploadToStorage();
                finish();
            }
        });
    }
    private String getRealPathFromURI(Uri contentURI){
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentURI, proj, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null & data.getData() != null) {
            selectedImageURI = data.getData();
            //imgView.setImageURI(selectedImageURI);
        }
    }
}