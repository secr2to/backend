package com.emelmujiro.secreto.global.service;

public enum S3DirectoryName {
    ROOMPROFILE("room-profile/"),
    ;

    private final String value;

    S3DirectoryName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
