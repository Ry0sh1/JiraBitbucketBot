package com.ryoshi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class User {
    private String name;
    private String slug;
    private String displayName;
    private String emailAddress;
}
