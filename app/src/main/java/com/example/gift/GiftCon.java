package com.example.gift;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GiftCon implements Serializable {
    private String image;
    private String productName;
    private String store;
    private String DDAY;
    private String owner;
    private boolean available;

    public GiftCon(Bitmap image, String productName, String store, String DDAY, String owner, boolean available) {
        this.encodeImage(image);
        this.productName = productName;
        this.store = store;
        this.DDAY = DDAY;
        this.owner = owner;
        this.available = available;
    }

    public String getProductName() {return productName;}

    public void setProductName(String productName) {this.productName = productName;}

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDDAY() {
        return DDAY;
    }

    public void setDDAY(String DDAY) {
        this.DDAY = DDAY;
    }

    public String getOwner() {return owner;}

    public void setOwner(String owner) {this.owner = owner;}

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {this.available = available;}

    //bitmap을 문자열로
    public void encodeImage(Bitmap bitmapImg){
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapImg.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        image = Base64.encodeToString(b, Base64.DEFAULT);
    }
    //문자열을 bitmap으로
    public Bitmap decodeImage(){
        Bitmap decodedImg;
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        decodedImg = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedImg;
    }
    public void uploadToStorage(String filepath){
        //직렬화한 객체를 db에 저장
        String filename = serialize(filepath);
        Uri fileUri = Uri.parse("file://"+filepath+filename);
        Log.e("파일 uri", fileUri.toString());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(owner+"/"+filename);
        storageRef.putFile(fileUri);

    }
    public void downloadFromStorage(){
        //db에서 객체 받아오기!
        deserialize();
    }
    //직렬화
    private String serialize(String filepath){
        ObjectMapper objectMapper = new ObjectMapper();
        //파일명은 생성한 날짜와 시간
        SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date time = new Date(System.currentTimeMillis());
        String filename = fileNameFormat.format(time)+ ".json";
        GiftCon gc = new GiftCon(this.decodeImage(), this.productName, this.store, this.DDAY, this.owner, true);
        try{
            //json 파일로 저장
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filepath + filename), gc);
        }catch(Exception e){
            e.printStackTrace();
        }
        return filename;
    }
    //역직렬화
    private void deserialize(){

    }
}
