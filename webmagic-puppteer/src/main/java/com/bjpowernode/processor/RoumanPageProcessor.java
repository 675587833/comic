package com.bjpowernode.processor;

import com.bjpowernode.model.Image;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

public class RoumanPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000).setDomain("https://rouman01.xyz")
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36 Edg/99.0.1150.30");

    @Override
    public void process(Page page) {

        if (page.getUrl().regex("/[0-9]+$").match()) {
            String title = page.getHtml().xpath("//title/text()").get();
            String bookName = title.substring(title.indexOf("《") + 1, title.indexOf("》"));
            String chapterName = page.getHtml().xpath("//h3/text()").get();
            System.out.println(bookName);
            System.out.println(chapterName);
            page.putField("bookName", bookName);
            page.putField("chapterName", chapterName);
            List<String> srcList = page.getHtml().xpath("//div/img[@referrerpolicy]/@src").all();
            List<Image> images = new ArrayList<>();
            for (String src : srcList) {
                Image image = new Image();
                image.setSrc(src);
                images.add(image);
            }
            System.out.println(images);
            page.putField("images", images);

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
