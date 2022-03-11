package com.bjpowernode.downloader;

import com.bjpowernode.action.Action;
import com.ruiyun.jvppeteer.core.Puppeteer;
import com.ruiyun.jvppeteer.core.browser.Browser;
import com.ruiyun.jvppeteer.options.LaunchOptions;
import com.ruiyun.jvppeteer.options.LaunchOptionsBuilder;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PuppteerDownloader implements Downloader, Closeable {

    private String path;
    private int sleepTime = 0;
    private Action action = null;
    private Browser browser = null;

    public PuppteerDownloader() {
        this.path = "C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe";
        this.setBrowser();
    }

    public PuppteerDownloader(String path) {
        this.path = path;
        this.setBrowser();
    }

    public PuppteerDownloader setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public PuppteerDownloader setOperator(Action action) {
        this.action = action;
        return this;
    }

    public PuppteerDownloader setBrowser() {
        List<String> args = new ArrayList<>();
        args.add("--no-sandbox");
        args.add("--disable-setuid-sandbox");
        LaunchOptions options = new LaunchOptionsBuilder().withArgs(args).withExecutablePath(path).build();
        try {
            browser = Puppeteer.launch(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public Page download(Request request, Task task) {
        String content = null;
        if (request.getUrl().matches(".*/[0-9]+$")) {
            if (browser != null) {
                if (action != null) {
                    content = action.execute(browser, request, sleepTime);
                }
            }
        } else {
            if (browser != null) {
                if (action != null) {
                    content = action.skip(browser, request, sleepTime);
                }
            }
        }
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        return page;
    }

    @Override
    public void setThread(int i) {

    }

    @Override
    public void close() throws IOException {
        if (browser.pages().size() == 1) {
            browser.close();
        }
    }
}
