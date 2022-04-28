package com.example.headlesscms.application.controllers;

import com.example.headlesscms.application.entities.Article;
import com.example.headlesscms.application.repositories.ArticleRepository;
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

    final WebsiteRepository websiteRepository; //is it needed?
    final ArticleRepository articleRepository;
    final UserRepository userRepository;

    public ArticleController(WebsiteRepository websiteRepository, ArticleRepository articleRepository, UserRepository userRepository) {
        this.websiteRepository = websiteRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    UserDetailsImplementation currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImplementation) authentication.getPrincipal();
    }

    //@GetMapping("/{websiteTitle}/{articleTitle}")
    @PostMapping("/createNewArticle")
    @PreAuthorize("hasRole('admin')")
    public Article createArticle(@RequestBody Article articleBody) {

        //no website-name at all is given
        if (articleBody.getNameOfWebsiteThatArticleBelongsTo().isEmpty()) {
            System.out.println("website with name " + articleBody.getNameOfWebsiteThatArticleBelongsTo() + " is empty.");
            return null;
        }

        String websiteTitel = articleBody.getArticleTitle();
        String nameOfWebsiteThatArticleBelongsTo = articleBody.getNameOfWebsiteThatArticleBelongsTo();

        //website-name not found at listOfAllWebsites
        if (websiteRepository.findByWebsiteTitle(websiteTitel).equals(null)) {
            System.out.println("website with name " + articleBody.getNameOfWebsiteThatArticleBelongsTo() + " does not exist.");
            return null;
        }

        //logged-in user is creatorOfWebsite || logged-in user is moderator on website)
        if (currentUser().getId().equals(websiteRepository.findByWebsiteTitle(nameOfWebsiteThatArticleBelongsTo).getCreatorOfWebsite()) ||
          websiteRepository.findByWebsiteTitle(nameOfWebsiteThatArticleBelongsTo).idCheckedIsModeratorOnWebpage(currentUser().getId())) {

            if (articleBody.addArticle(articleBody)){
                return articleRepository.save(articleBody);

            }

        }
        return null;
    }




}
