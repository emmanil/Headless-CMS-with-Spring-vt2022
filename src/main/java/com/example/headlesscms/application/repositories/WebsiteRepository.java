package com.example.headlesscms.application.repositories;

import com.example.headlesscms.application.entities.Website;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WebsiteRepository extends MongoRepository<Website, String> {

    boolean existsByWebsiteTitle(String websiteTitle);

    Website findByWebsiteTitle(String websiteTitle);

    Website findCreatorOfWebsite(String creatorOfWebsite);

    Website getAllByWebsiteTitle(String websiteTitle);

}
