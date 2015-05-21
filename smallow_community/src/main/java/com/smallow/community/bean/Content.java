package com.smallow.community.bean;

/**
 * Created by smallow on 2015/5/20.
 */
public class Content  {

    private String title;
    private Integer id;
    private String titleImg;
    private String description;
    private String money;


    public Content(Integer id,String title,String description,String money,String titleImg){
        this.id=id;
        this.title=title;
        this.titleImg=titleImg;
        this.description=description;
        this.money=money;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}

