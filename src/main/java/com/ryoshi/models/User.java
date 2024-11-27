package com.ryoshi.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class User {
    private String name;
    private String slug;
    private String displayName;
    private String emailAddress;
}
