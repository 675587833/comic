package com.bjpowernode.model;

import lombok.Data;

import java.util.List;

@Data
public class Chapter {
    private String name;
    private List<Image> images;
    private String description;

}
