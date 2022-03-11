package com.bjpowernode.pipeline;

import com.bjpowernode.model.Image;
import com.bjpowernode.util.Aria2Util;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.util.List;

public class ImagePipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        String bookName = resultItems.get("bookName");
        String chapterName = resultItems.get("chapterName");
        String dir = "E:\\Download\\" + (bookName + File.separator + chapterName).replaceAll("[\\/:*?\"<>|]+", "");
        List<Image> images = resultItems.get("images");
        if (images != null) {
            try {
                List<String> jsonList = Aria2Util.toJson(images, dir);
                Aria2Util.post(jsonList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}