package com.petshop.frontend.dto;

public class EndpointDTO {

    private final String method;
    private final String path;
    private final String description;

    public EndpointDTO(String method, String path, String description) {
        this.method = method;
        this.path = path;
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }
}
