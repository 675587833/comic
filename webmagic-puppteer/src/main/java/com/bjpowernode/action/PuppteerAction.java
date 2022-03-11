package com.bjpowernode.action;

import com.ruiyun.jvppeteer.core.browser.Browser;
import com.ruiyun.jvppeteer.core.page.ElementHandle;
import com.ruiyun.jvppeteer.core.page.Page;
import us.codecraft.webmagic.Request;

import java.util.List;

public class PuppteerAction implements Action {

    public String execute(Browser browser, Request request, int sleepTime) {
        Page page = browser.newPage();
        page.setDefaultNavigationTimeout(1000 * 60 * 5);
        try {
            page.goTo(request.getUrl());
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<ElementHandle> handles = page.$x("//div/img[@referrerpolicy]");
        for (ElementHandle handle : handles) {
            handle.scrollIntoViewIfNeeded();
        }
        page.evaluate("window.scrollBy(0,document.body.scrollHeight)");
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String content = page.content();
        if (page != null) {
            try {
                page.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    @Override
    public String skip(Browser browser, Request request, int sleepTime) {
        Page page = browser.newPage();
        page.setDefaultNavigationTimeout(1000 * 60 * 5);
        try {
            page.goTo(request.getUrl());
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String content = page.content();
        if (page != null) {
            try {
                page.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

}
