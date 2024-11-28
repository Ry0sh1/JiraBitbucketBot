package com.ryoshi.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class Response {
    private String size;
    private String limit;
    private PullRequest[] values;
}
