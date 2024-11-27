package com.ryoshi.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PullRequest {
    private String id;
    private String title;
    private String description;
    private String state;
    private String open;
    private String closed;
    //private String draft; left out for debugging purposes
    private String createdDate;
    private String updatedDate;
    private Author author;
    private Link links;
    private Properties properties;

}
