package com.example.headlesscms.application.repositories;

import com.example.headlesscms.application.entities.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<Article, String> {

    boolean existsByArticleTitle(String ArticleTitle);

    boolean existsByNameOfWebsiteThatArticleBelongsTo(String NameOfWebsiteThatArticleBelongsTo);

    Article findByNameOfWebsiteThatArticleBelongsTo(String NameOfWebsiteThatArticleBelongsTo);

    Article getByArticleTitleExists(String articleTitle);

    Article findByArticleTitle(String ArticleTitle);


}
