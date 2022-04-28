package com.example.headlesscms.application.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "articles")
public class Article {

    @Id
    private String id = null;

    private String articleTitle;
    private String articleText;
    private String nameOfWebsiteThatArticleBelongsTo;


    public Article(String articleTitle, String articleText, String nameOfWebsiteThatArticleBelongsTo) {
        //several websites can have the same name, because of that a website has a unique id.
        this.id = id;
        this.articleTitle = articleTitle;
        this.articleText = articleText;
        this.nameOfWebsiteThatArticleBelongsTo = nameOfWebsiteThatArticleBelongsTo;
    }

    public String getNameOfWebsiteThatArticleBelongsTo() {
        return nameOfWebsiteThatArticleBelongsTo;
    }

    public void setNameOfWebsiteThatArticleBelongsTo(String nameOfWebsiteThatArticleBelongsTo) {
        this.nameOfWebsiteThatArticleBelongsTo = nameOfWebsiteThatArticleBelongsTo;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }


    public boolean addArticle(Article articleBody) {

        //make newArticleAdded testable
        boolean newArticleAdded = articleBody.addArticle(new Article(articleBody.getArticleTitle(), articleBody.getArticleText(), articleBody.getNameOfWebsiteThatArticleBelongsTo()));
    return newArticleAdded;
    }
}

