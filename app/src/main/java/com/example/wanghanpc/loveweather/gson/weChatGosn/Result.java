package com.example.wanghanpc.loveweather.gson.weChatGosn;

import java.util.List;

public class Result {

    private List<listItem> list;

    private String totalPage;

    private String ps;

    private String pno;

    public List<listItem> getList() {
        return list;
    }

    public void setList(List<listItem> list) {
        this.list = list;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }
}
