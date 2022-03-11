package com.bjpowernode.action;

import org.openqa.selenium.*;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;

import java.util.List;
import java.util.Map;

public class SeleniumAction implements Action {


    @Override
    public String execute(WebDriver driver, Request request, Task task, int sleepTime) {
        try {
            driver.get(request.getUrl());
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String script = "arguments[0].scrollIntoView();";
        List<WebElement> elements = driver.findElements(By.xpath("//div/img[@referrerpolicy]"));
        for (int i = 0; i < 2; i++) {
            for (WebElement element : elements) {
                String src = element.getAttribute("src");
                if (src.equals(task.getSite().getDomain() + "/loading.jpg")) {
                    ((JavascriptExecutor) driver).executeScript(script, element);
                }
            }
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement element = driver.findElement(By.xpath("/html"));
        String content = element.getAttribute("outerHTML");
        return content;
    }

    @Override
    public String skip(WebDriver driver, Request request, int sleepTime) {
        driver.get(request.getUrl());
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement element = driver.findElement(By.xpath("/html"));
        String content = element.getAttribute("outerHTML");
        return content;
    }
}
