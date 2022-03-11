package com.bjpowernode.model;

import lombok.Data;

import java.util.List;

@Data
public class PageProps {
    private String bookName;
    private String[] alias;
    private String chapterName;
    private String description;
    private List<Image> images;
    private Integer totalChapter;
    private String[] tags;
    private String session;
    private String chapterAPIPath;
    private String adChapterBottom;

}
