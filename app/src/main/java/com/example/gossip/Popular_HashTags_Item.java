package com.example.gossip;

public class Popular_HashTags_Item {
    int image;
    String image_info;
    public Popular_HashTags_Item(int i,String s){
        this.image=i;
        this.image_info=s;
    }

//    public Popular_HashTags_Item(int black_panther) {
//    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getImage_info() {
        return image_info;
    }

    public void setImage_info(String image_info) {
        this.image_info = image_info;
    }
}
