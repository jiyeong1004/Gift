package com.example.gift;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class AutomaticActivity extends AppCompatActivity {
    TessBaseAPI tess;
    String dataPath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic);

        //데이터 경로
        dataPath = getFilesDir() + "/tesseract/";

        //한글 데이터 체크
        checkFile(new File(dataPath + "tessdata/"), "kor");

        //문자 인식을 수행할 tess 객체 생성
        String lang = "kor";
        tess = new TessBaseAPI();
        Log.e("tesseract 초기화", "직전!!!");
        tess.init(dataPath, lang);
        Log.e("tesseract 초기화", "성공~");

        //문자 인식
        processImage(BitmapFactory.decodeResource(getResources(), R.drawable.test));
    }
    public void processImage(Bitmap bitmap){
        Toast.makeText(getApplicationContext(), "잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
        String OCRresult = null;
        tess.setImage(bitmap);
        OCRresult = tess.getUTF8Text();
        TextView OCRTextView = findViewById(R.id.ocr_result);

        OCRTextView.setText(OCRresult);
    }
    private void copyFiles(String lang){
        try{
            //location we want the file to be at
            String filepath = dataPath + "/tessdata/" + lang + ".traineddata";

            //get access to AssetManager
            AssetManager assetManager = getAssets();
            //open byte streams for reading/writing
            InputStream inStream = assetManager.open("tessdata/" + lang + ".traineddata");
            OutputStream outStream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while((read = inStream.read(buffer)) != -1){
                outStream.write(buffer, 0, read);
            }
            outStream.flush();
            outStream.close();
            inStream.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void checkFile(File dir, String lang){
        //directory does not exist, but we can successfully create it
        if(!dir.exists()&&dir.mkdirs()){
            copyFiles(lang);
        }
        //The directory exists, but there is no data file in it
        if(dir.exists()){
            String datafilePath = dataPath + "/tessdata/" + lang + ".traineddata";
            File datafile = new File(datafilePath);
            if(!datafile.exists()){
                copyFiles(lang);
            }
        }
    }
}