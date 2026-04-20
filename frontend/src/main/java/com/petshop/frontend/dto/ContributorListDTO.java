package com.petshop.frontend.dto;

public class ContributorListDTO {

    private Long id;
    private String name;
    private String role;
    private String photoUrl;
    private String apiArea;

    public ContributorListDTO() {
    }

    public ContributorListDTO(Long id,
                              String name,
                              String role,
                              String photoUrl,
                              String apiArea) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.photoUrl = photoUrl;
        this.apiArea = apiArea;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getApiArea() {
        return apiArea;
    }

    public void setApiArea(String apiArea) {
        this.apiArea = apiArea;
    }
}
