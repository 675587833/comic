package com.bjpowernode.downloader;

import com.bjpowernode.action.Action;
import org.openqa.selenium.*;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

import java.io.Closeable;
import java.util.Map;

public class SeleniumDownloader implements Downloader, Closeable {

    private int sleepTime = 0;
    private int poolSize = 1;
    private Action action = null;
    private volatile WebDriverPool pool;

    public SeleniumDownloader() {
        System.getProperties().setProperty("webdriver.edge.driver", "C:/Dev/selenium-driver/msedgedriver.exe");
    }

    public SeleniumDownloader(String path) {
        System.getProperties().setProperty("webdriver.edge.driver", path);
    }

    public SeleniumDownloader setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public SeleniumDownloader setOperator(Action action) {
        this.action = action;
        return this;
    }

    @Override
    public Page download(Request request, Task task) {
        if (pool == null) {
            synchronized (this) {
                pool = new WebDriverPool(poolSize);
            }
        }
        WebDriver driver = null;
        String content = null;
        try {
            driver = pool.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        if (request.getUrl().matches(".*/[0-9]+$")) {
            if (driver != null) {
                if (action != null) {
                    content = action.execute(driver, request, task, sleepTime);
                }
            }
        } else {
            if (driver != null) {
                if (action != null) {
                    content = action.skip(driver, request, sleepTime);
                }
            }
        }
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        pool.put(driver);
        return page;
    }

    @Override
    public void setThread(int thread) {
        this.poolSize = thread;
    }

    @Override
    public void close() {
        pool.close();
    }
}
