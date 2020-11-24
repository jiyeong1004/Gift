package com.example.gift;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class GiftCon implements Serializable {
    private Bitmap image;
    private String encodedImage; //문자로 바꿔서 저장
    private String productName;
    private String store;
    private String DDAY;
    private FirebaseUser owner;
    private boolean available;

    public GiftCon(Bitmap image, String productName, String store, String DDAY, FirebaseUser owner, boolean available) {
        this.image = image;
        this.productName = productName;
        this.store = store;
        this.DDAY = DDAY;
        this.owner = owner;
        this.available = available;
    }

    public String getEncodedImage() {return encodedImage;}

    public void setEncodedImage(String decodedImage) {this.encodedImage = decodedImage;}

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

    public FirebaseUser getOwner() {return owner;}

    public void setOwner(FirebaseUser owner) {this.owner = owner;}

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void encodeImage(Bitmap bitmapImg){
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapImg.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
    }
    public Bitmap decodeImage(){
        Bitmap decodedImg;
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        decodedImg = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedImg;
    }
    public void uploadToStorage(){
        //직렬화한 객체를 db에 저장
        serialize();
    }
    public void downloadFromStorage(){
        //db에서 객체 받아오기!
        deserialize();
    }
    //직렬화
    public void serialize(){

    }
    //역직렬화
    public void deserialize(){

    }
}
