package com.example.gift;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AutomaticActivity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;

    private ImageView imgView;
    private Button loadImgBtn;
    private Button ocrImgBtn;
    private TextView readTxt;

    TessBaseAPI tess;
    Bitmap selectedImageBitmap;
    Uri selectedImageURI;
    String dataPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic);

        imgView = findViewById(R.id.imageTest);
        loadImgBtn = findViewById(R.id.loadImageButton);
        ocrImgBtn = findViewById(R.id.ocrImageButton);
        readTxt = findViewById(R.id.readText);

        loadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
        ocrImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPath = getFilesDir() + "/tessdata/";
                checkFile(new File(dataPath + "/tessdata/"), "kor");

                String lang = "kor";
                tess  = new TessBaseAPI();
                tess.init(dataPath, lang);
                try{
                    selectedImageBitmap = BitmapFactory.decodeFile(getPathFromURI(selectedImageURI));
                    processImage(selectedImageBitmap);
                }catch(Exception e){
                    Toast.makeText(AutomaticActivity.this, "이미지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    @Override //갤러리에서 이미지 가져와서 변수에 uri저장
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null & data.getData() != null) {
            selectedImageURI = data.getData();
            imgView.setImageURI(selectedImageURI);
            Log.e("AutomaticActivty", getPathFromURI(selectedImageURI));
        }
    }
    //uri에서 이미지 절대경로 가져옴
    private String getPathFromURI(Uri imgURI){
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imgURI, proj, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    //이미지 ocr인식하기
    public void processImage(Bitmap bitmap){
        Toast.makeText(AutomaticActivity.this, "잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
        String OCRresult = null;
        tess.setImage(bitmap);
        OCRresult = tess.getUTF8Text();
        Log.e("AutomaticActivty", String.valueOf(selectedImageURI) + "결과" + OCRresult);
        readTxt.setText(OCRresult);
    }
    //파일이 있는지 확인
    private void checkFile(File dir, String Language) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles(Language);
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if (dir.exists()) {
            String datafilepath = dataPath + "tessdata/" + Language + ".traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles(Language);
            }
        }
    }
    //파일 복사(왜? 일단 있길래 넣어둠)
    private void copyFiles(String Language) {
        try {
            String filepath = dataPath + "/tessdata/" + Language + ".traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/"+Language+".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}