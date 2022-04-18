package com.example.headlesscms.application.controllers;

import com.example.headlesscms.application.entities.Article;
import com.example.headlesscms.application.entities.Website;
import com.example.headlesscms.application.repositories.WebsiteRepository;
import com.example.headlesscms.repositories.UserRepository;
import com.example.headlesscms.security.services.UserDetailsImplementation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/website/articles")
public class ArticleController {

    final WebsiteRepository websiteRepository;
    final UserRepository userRepository;


    public ArticleController(WebsiteRepository websiteRepository, UserRepository userRepository) {
        this.websiteRepository = websiteRepository;
        this.userRepository = userRepository;
    }

    UserDetailsImplementation currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImplementation) authentication.getPrincipal();
    }

    //@GetMapping("/{websiteTitle}/{articleTitle}")

    @PostMapping("/create/{articleTitle}")
    @PreAuthorize("hasRole('admin')")
    public Website createArticle(@PathVariable String websiteTitle, @RequestBody Article articleBody){
        if (!websiteRepository.existsByWebsiteTitle(websiteTitle)){
            System.out.println("website with name "+websiteTitle+ " does not exist.");
            return null;
        }
        if ( websiteRepository.existsByWebsiteTitle(websiteTitle) && ( currentUser().getId().equals(websiteRepository.findByWebsiteTitle(websiteTitle).getCreatorOfWebsite()) || websiteRepository.findByWebsiteTitle(websiteTitle).getModeratorId(currentUser().getId())) ){
        Website website = websiteRepository.findByWebsiteTitle(websiteTitle);
        website.addArticle(articleBody.getArticleTitle(), new Article(articleBody.getArticleTitle(), articleBody.getArticleText()) );

        return websiteRepository.save(website);
        }else {
            return null;
        }

    }



}
