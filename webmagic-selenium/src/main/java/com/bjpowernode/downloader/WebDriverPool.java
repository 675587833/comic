package com.bjpowernode.downloader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class WebDriverPool {

    private final int capacity;
    private final static int STAT_RUNNING = 1;
    private final static int STAT_CLODED = 2;
    private AtomicInteger stat = new AtomicInteger(STAT_RUNNING);
    private List<WebDriver> driverList = Collections.synchronizedList(new ArrayList<>());
    private BlockingDeque<WebDriver> innerQueue = new LinkedBlockingDeque<WebDriver>();
    private WebDriver driver = null;

    public WebDriverPool() {
        this(5);
    }

    public WebDriverPool(int capacity) {
        this.capacity = capacity;
    }

    public WebDriver get() throws InterruptedException {

        if (!stat.compareAndSet(STAT_RUNNING, STAT_RUNNING)) {
            throw new IllegalStateException("Already Closed!");
        }
        WebDriver poll = innerQueue.poll();
        if (poll != null) {
            return poll;
        }
        if (driverList.size() < capacity) {
            synchronized (driverList) {
                if (driverList.size() < capacity) {
                    EdgeOptions options = new EdgeOptions();
                    options.addArguments("headless");
                    options.addArguments("disable-gpu");
                    driver = new EdgeDriver(options);
                    innerQueue.add(driver);
                    driverList.add(driver);
                }
            }
        }

        return innerQueue.take();
    }

    public void put(WebDriver driver) {
        if (!stat.compareAndSet(STAT_RUNNING, STAT_RUNNING)) {
            throw new IllegalStateException("Already Closed!");
        }
        innerQueue.add(driver);
    }

    public void close() {
        boolean b = stat.compareAndSet(STAT_RUNNING, STAT_CLODED);
        if (!b) {
            throw new IllegalStateException("Already Closed!");
        }
        for (WebDriver driver : driverList) {
            driver.quit();
            driver = null;
        }
    }
}

