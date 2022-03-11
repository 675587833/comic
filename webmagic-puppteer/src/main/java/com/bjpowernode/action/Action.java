package com.bjpowernode.action;

import com.ruiyun.jvppeteer.core.browser.Browser;
import com.ruiyun.jvppeteer.core.page.Page;
import us.codecraft.webmagic.Request;

public interface Action {

    String execute(Browser browser, Request request,int sleepTime);

    String skip(Browser browser, Request request,int sleepTime);

}
