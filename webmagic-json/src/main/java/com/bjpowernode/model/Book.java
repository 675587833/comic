package com.bjpowernode.model;

import lombok.Data;

@Data
public class Book {

    private Props props;
    private String page;
    private Query query;
    private String buildId;
    private Boolean isFallback;
    private Boolean gssp;
    private String[] scriptLoader;

}
