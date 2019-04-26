package com.example.udacity_6_news_app;

public class News {
    // Declaring variables
    private String article_title, article_section, article_date, article_url, article_thumbnail, article_authors;

    // Initializing
    public News(String title, String section, String webPublicationDateAndTime, String webUrl, String thumbnail, String authors) {
        this.article_title = title;
        this.article_section = section;
        this.article_date = webPublicationDateAndTime;
        this.article_url = webUrl;
        this.article_thumbnail = thumbnail;
        this.article_authors = authors;
    }

    String getArticle_title() {
        return article_title;
    }

    String getArticle_section() {
        return article_section;
    }

    String getArticle_date() {
        return article_date;
    }

    String getArticle_url() {
        return article_url;
    }

    String getArticle_thumbnail(){
        return article_thumbnail;
    }

    String getArticle_authors() {
        return article_authors;
    }
}
