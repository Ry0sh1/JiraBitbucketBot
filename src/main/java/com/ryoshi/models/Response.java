package com.ryoshi.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Response {
    private String size;
    private String limit;
    private PullRequest[] values;
}
