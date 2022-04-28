package com.example.headlesscms.application.controllers;

import com.example.headlesscms.application.entities.Article;
import com.example.headlesscms.application.entities.Website;
import com.example.headlesscms.application.repositories.ArticleRepository;
import com.example.headlesscms.application.repositories.WebsiteRepository;
import com.example.headlesscms.models.User;
import com.example.headlesscms.repositories.UserRepository;
import com.example.headlesscms.security.services.UserDetailsImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/websites")
public class WebsiteController {

    final WebsiteRepository websiteRepository;
    final UserRepository userRepository;
    final ArticleRepository articleRepository;

    public WebsiteController(WebsiteRepository websiteRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.websiteRepository = websiteRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    UserDetailsImplementation currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        return userDetails;
    }

    @GetMapping("/{websiteTitle}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getOne(@PathVariable String websiteTitle) {

        if (websiteRepository.existsByWebsiteTitle(websiteTitle) && articleRepository.existsByNameOfWebsiteThatArticleBelongsTo(websiteTitle)) {
            Website website = websiteRepository.findByWebsiteTitle(websiteTitle);
            Article article = articleRepository.findByNameOfWebsiteThatArticleBelongsTo(websiteTitle);

            String message = "Title: " + website.getWebsiteTitle() + ". "
                    + "Description: " + website.getWebsiteDescription()
                    + ". Moderators: " + website.getModerators()
                    + " .Articles: " + articleRepository.findByNameOfWebsiteThatArticleBelongsTo(websiteTitle)
                    + ".  Creator of website: " + website.getCreatorOfWebsite();
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.badRequest().body("ERROR: No site found");
    }

    //to test
    //TODO check static context
    @PostMapping("/createNewWebsite")
    @PreAuthorize("hasRole('USER')")
    public User create(@RequestBody Website websiteBody) {
        User creatorOfWebsite = userRepository.findById(currentUser().getId()).orElse(null);
        if (creatorOfWebsite == null) {
            return null;
        }

        List<User> listOfModeratorsOnWebsite = new ArrayList<>();
        List<Article> listOfArticlesOnWebsite = new ArrayList<>();

        //TODO check static context
        return WebsiteRepository.save(new Website(websiteBody.getId(), websiteBody.getWebsiteTitle(), websiteBody.getWebsiteDescription(), creatorOfWebsite, listOfModeratorsOnWebsite, listOfArticlesOnWebsite));
    }

    //to test
    @PutMapping("/updateWebside")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(@RequestBody Website updatedWebsiteBody) {
        //website exists and logger in user is creator/or moderator
        if (websiteRepository.findByWebsiteTitle(updatedWebsiteBody.getWebsiteTitle()).getCreatorOfWebsite().getId()
                .equals(Objects.requireNonNull(userRepository.findById(currentUser().getId()).orElse(null)).getId())
                || websiteRepository.findByWebsiteTitle(updatedWebsiteBody.getWebsiteTitle()).getListOfModeratorsOnWebsite().contains(currentUser().getId()))
        {
            Website temp = websiteRepository.findByWebsiteTitle(updatedWebsiteBody.getWebsiteTitle());
            temp = updatedWebsiteBody;
            websiteRepository.save(temp);
            return ResponseEntity.ok("Site updated successfully!");
        }

        return ResponseEntity.badRequest().body("ERROR: CREATOR or MODERATOR access is required.");
    }

    //to test
    @PutMapping("/addmoderator")
    @PreAuthorize("hasRole('USER')")
    public Website update(@RequestParam String username) {
        if (websiteRepository.findCreatorOfWebsite(username).getId().equals(currentUser().getId()))
        {
            Website temp = websiteRepository.findCreatorOfWebsite(username);
            temp.addModerator(userRepository.getByUsername(username));
            return websiteRepository.save(temp);
        }
        return null;
    }

    //to test
    @DeleteMapping("/{title}")
    @PreAuthorize("hasRole('USER')")
    public String delete(@RequestParam String username, @RequestBody Website website) {
        String websiteTitleToDelete = website.getWebsiteTitle();
        if (!website.getCreatorOfWebsite().equals(currentUser().getId()))
        {  return "You have no access to this function.";
        }
        if (websiteRepository.findByWebsiteTitle(website.getWebsiteTitle()).equals(website)){
            websiteRepository.delete(website);
            return "Site: " + websiteTitleToDelete + " has been deleted.";
        }
        return "Something went wrong, try again";
    }


    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String update() {
        return "Website updated.";
    }

    @DeleteMapping("/deleteWebsite")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@RequestBody Website website) {
        String websiteTitleToDelete = website.getWebsiteTitle();
        if (!website.getListOfModeratorsOnWebsite().contains(currentUser().getId()))
        {  return "You have no access to this function.";
        }
        if (websiteRepository.findByWebsiteTitle(website.getWebsiteTitle()).equals(website)){
            websiteRepository.delete(website);
            return "Site: " + websiteTitleToDelete + " has been deleted.";
        }
        return "Something went wrong, try again";
    }

}
