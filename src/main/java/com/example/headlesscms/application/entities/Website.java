package com.example.headlesscms.application.entities;

import com.example.headlesscms.models.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "websites")
public class Website {

    @Id
    private final String id;

    @NotBlank
    @Size(max = 50)
    @TextIndexed
    private String websiteTitle;

    @Size(max = 200)
    private String websiteDescription;

    @DocumentReference
    private User creatorOfWebsite;

    @DocumentReference
    private List<User> listOfModeratorsOnWebsite;

    //TODO check mongoDB
    @DocumentReference
    private List<Article> listOfArticlesOnWebsite;

    //TODO check mongoDB
    @DocumentReference
    private List<Website> listOfAllWebsites;


    public Website(String id, String websiteTitle, String websiteDescription, User creatorOfWebsite, List<User> listOfModeratorsOnWebsite, List<Article> listOfArticlesOnWebsite) {
        //several websites can have the same name, because of that a website has a unique id.
        this.id = id;
        this.websiteTitle = websiteTitle;
        this.websiteDescription = websiteDescription;
        this.creatorOfWebsite = creatorOfWebsite;
        this.listOfModeratorsOnWebsite = listOfModeratorsOnWebsite;
        this.listOfArticlesOnWebsite = listOfArticlesOnWebsite;
    }

    public String getId() {
        return id;
    }

    public boolean idCheckedIsModeratorOnWebpage(String id) {
        for (User mod : listOfModeratorsOnWebsite) {
            if (id.equals(mod.getId())) {
                return true;
            }
        }
        return false;
    }

    public String getWebsiteTitle() {
        return websiteTitle;
    }

    public void setWebsiteTitle(String websiteTitle) {
        this.websiteTitle = websiteTitle;
    }

    public String getWebsiteDescription() {
        return websiteDescription;
    }

    public void setWebsiteDescription(String websiteDescription) {
        this.websiteDescription = websiteDescription;
    }

    public List<User> getListOfModeratorsOnWebsite() {
        return listOfModeratorsOnWebsite;
    }

    //add moderator (duplicate method)
    public List<String> getModerators() {
        List<String> temp = new ArrayList<>();
        for (User mod : listOfModeratorsOnWebsite) {
            temp.add("Username: " + mod.getUsername() +
                    ". Email: " + mod.getEmail() + " ");
        }
        return temp;
    }

    public void setListOfModeratorsOnWebsite(List<User> listOfModeratorsOnWebsite) {
        this.listOfModeratorsOnWebsite = listOfModeratorsOnWebsite;
    }

    public List<Article> getListOfArticlesOnWebsite() {
        return listOfArticlesOnWebsite;
    }

    //TODO to test
    public List<Website> getListOfAllWebsites() {
        //create new object to return, for more safety
        List<Website> listOfAllWebsitesTemp = new ArrayList<>();
        listOfAllWebsitesTemp.addAll(getListOfAllWebsites());
        return listOfAllWebsites;
    }

    public boolean addModerator(User user) {
        if (listOfModeratorsOnWebsite.contains(user)) {
            return true;
        }
        return listOfModeratorsOnWebsite.add(user);
    }


    public User getCreatorOfWebsite() {
        return creatorOfWebsite;
    }

    public void setCreatorOfWebsite(User creatorOfWebsite) {
        this.creatorOfWebsite = creatorOfWebsite;
    }
}
