package com.petshop.frontend.dto;

import java.util.List;

public class ContributorProfileDTO {

    private final Long id;
    private final String name;
    private final String role;
    private final String photoUrl;
    private final String apiArea;
    private final List<EndpointDTO> endpoints;
    private final List<ActivityDTO> recentActivities;

    public ContributorProfileDTO(Long id,
                                 String name,
                                 String role,
                                 String photoUrl,
                                 String apiArea,
                                 List<EndpointDTO> endpoints,
                                 List<ActivityDTO> recentActivities) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.photoUrl = photoUrl;
        this.apiArea = apiArea;
        this.endpoints = List.copyOf(endpoints);
        this.recentActivities = List.copyOf(recentActivities);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getApiArea() {
        return apiArea;
    }

    public List<EndpointDTO> getEndpoints() {
        return endpoints;
    }

    public List<ActivityDTO> getRecentActivities() {
        return recentActivities;
    }
}
