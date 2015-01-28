package com.smallow.android.news.entity;

/**
 * Created by smallow on 2015/1/27.
 */
public class ContentBean {

    public ContentBean(Integer id,String title,String author){
        this.id=id;
        this.title=title;
        this.author=author;
    }

    private Integer id;
    private String title;


    private String author;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
