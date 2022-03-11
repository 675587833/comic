package com.bjpowernode.action;

import org.openqa.selenium.WebDriver;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;

public interface Action {

    String execute(WebDriver driver, Request request, Task task, int sleepTime);

    String skip(WebDriver driver, Request request, int sleepTime);
}
