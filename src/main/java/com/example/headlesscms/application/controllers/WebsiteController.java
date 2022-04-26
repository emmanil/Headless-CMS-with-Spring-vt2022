package com.example.headlesscms.application.controllers;

import com.example.headlesscms.application.entities.Website;
import com.example.headlesscms.application.repositories.ArticleRepository;
import com.example.headlesscms.application.repositories.WebsiteRepository;
import com.example.headlesscms.security.services.UserDetailsImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/websites")
public class WebsiteController {

    @Autowired
    WebsiteRepository websiteRepository;

    @Autowired
    ArticleRepository articleRepository;

    UserDetailsImplementation currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        return userDetails;
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public Website get(@PathVariable String id) {
        return websiteRepository.findById(id).get();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Website create(@RequestBody Website website){
        website.setCreatorOfWebsite(currentUser().getId());
        return websiteRepository.save(website);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String update(){
        return "Website updated.";
    }

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String updateProperty(){
        return "Single website property updated.";
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(){
        return "Website is deleted.";
    }

}
