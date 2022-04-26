package com.example.headlesscms.application.repositories;

import com.example.headlesscms.application.entities.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<Article, String> {

    Article getAllByArticleTitle (String articleTitle);

}
