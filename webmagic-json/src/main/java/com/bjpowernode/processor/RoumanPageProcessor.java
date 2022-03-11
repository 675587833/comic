package com.bjpowernode.processor;

import com.bjpowernode.model.Book;
import com.bjpowernode.model.Chapter;
import com.bjpowernode.model.Image;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

public class RoumanPageProcessor implements PageProcessor {
    private String bookName;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000).setDomain("https://rouman01.xyz")
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36 Edg/99.0.1150.30");

    @Override
    public void process(Page page) {

        if (page.getUrl().regex("/[0-9]+$").match()) {
            String dom = page.getHtml().$("#__NEXT_DATA__").get();
            String bookJson = dom.substring(dom.indexOf(">") + 1, dom.lastIndexOf("<"));
            System.out.println(bookJson);
            ObjectMapper om = new ObjectMapper();
            try {
                Book book = om.readValue(bookJson, Book.class);
                List<Image> images = book.getProps().getPageProps().getImages();
                bookName = book.getProps().getPageProps().getBookName();
                String chapterName = book.getProps().getPageProps().getChapterName();
                page.putField("bookName", bookName);
                page.putField("chapterName", chapterName);
                if (images != null) {
                    System.out.println(images);
                    page.putField("images", images);
                } else {
                    String chapterAPIPath = book.getProps().getPageProps().getChapterAPIPath();
                    if (chapterAPIPath != null) {
                        String request = site.getDomain() + chapterAPIPath;
                        System.out.println(request);
                        page.addTargetRequest(request);
                    }
                }

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } else if (page.getUrl().regex("exp").match()) {

            String chapterJson = page.getJson().jsonPath("$.chapter").get();
            ObjectMapper om = new ObjectMapper();
            try {
                Chapter chapter = om.readValue(chapterJson, Chapter.class);
                String chapterName = chapter.getName();
                List<Image> images = chapter.getImages();
                page.putField("bookName", bookName);
                page.putField("chapterName", chapterName);
                System.out.println(images);
                page.putField("images", images);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } else {
            List<String> urlList = page.getHtml().xpath("//div[@class='bookid_chapter__uNgYr']/a/@href").all();
            System.out.println(urlList);
            for (int i = 0; i < urlList.size(); i++) {
                String request = site.getDomain() + urlList.get(i);
                page.addTargetRequest(request);
            }

        }

    }

    @Override
    public Site getSite() {
        return this.site;
    }


}
