package com.emelmujiro.secreto.global.service;

public enum S3DirectoryName {
    ROOM_USER_PROFILE("room-user-profile/"),
    ROOM_IMAGE("room-image/"),
    FEED_IMAGE("feed-image/"),
    ;

    private final String value;

    S3DirectoryName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
