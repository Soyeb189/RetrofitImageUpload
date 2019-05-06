package com.mobibuzzbd.retrofitimageupload;

import com.google.gson.annotations.SerializedName;

public class ImageClass {

    @SerializedName("phone")
    public String Phone;

    @SerializedName("image")
    private String Image;

    @SerializedName("response")
    public String Response;

    public String getResponse() {
        return Response;
    }
}
