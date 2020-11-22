package com.example.gift;

public class GiftCon {
    private String image; //문자로 바꿔서 저장!
    private String store;
    private String DDAY;
    private String code;
    private boolean available;

    public GiftCon(String image, String store, String DDAY, String code, boolean available) {
        this.image = image;
        this.store = store;
        this.DDAY = DDAY;
        this.code = code;
        this.available = available;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void encodeImage(){

    }
}
