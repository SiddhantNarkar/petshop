package com.petshop.frontend.dto;

public class ActivityDTO {

    private final String iconType;
    private final String action;
    private final String timeAgo;
    private final String endpoint;

    public ActivityDTO(String iconType, String action, String timeAgo, String endpoint) {
        this.iconType = iconType;
        this.action = action;
        this.timeAgo = timeAgo;
        this.endpoint = endpoint;
    }

    public String getIconType() {
        return iconType;
    }

    public String getAction() {
        return action;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
