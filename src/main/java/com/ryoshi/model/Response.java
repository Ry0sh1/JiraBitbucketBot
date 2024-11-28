package com.ryoshi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class Response {
    private int size;
    private int limit;
    private PullRequest[] values;
}
