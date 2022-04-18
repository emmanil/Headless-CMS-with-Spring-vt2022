package com.example.headlesscms.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class Role {
    @Id
    private String id;

    private EnumRole name;

    public Role() {

    }

    public void EnumRole(EnumRole name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EnumRole getNameEnumRole() {
        return name;
    }

    public void setName(EnumRole name) {
        this.name = name;
    }
}
