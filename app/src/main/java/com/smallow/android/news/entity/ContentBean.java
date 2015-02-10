package com.smallow.android.news.entity;

/**
 * Created by smallow on 2015/1/27.
 */
public class ContentBean {

    public ContentBean(Integer id, String title, String author, String publishDate,String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
        this.content=content;
    }

    private Integer id;
    private String title;
    private String author;
    private String publishDate;
    private String content;

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

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
