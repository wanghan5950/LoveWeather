package com.example.wanghanpc.loveweather.gson.newsGson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class News implements Serializable{

    private static final long serialVersionUID = 6753628523776468033L;

    private String reason;

    private Result result;

    @SerializedName("error_code")
    private String errorCode;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
