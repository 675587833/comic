package com.bjpowernode;

import com.bjpowernode.pipeline.ImagePipeline;
import com.bjpowernode.processor.RoumanPageProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

@SpringBootApplication
public class JsonApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JsonApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        long begin = System.currentTimeMillis();
        Spider.create(new RoumanPageProcessor())
                .addUrl("https://rouman01.xyz/books/d28ea18d-c00e-4e67-afc2-35467fe1211b")
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10 * 1000)))
                .addPipeline(new ImagePipeline())
                .thread(5)
                .run();
        long end = System.currentTimeMillis();
        System.out.println("执行Application需要:" + (end - begin) / 1000 + "s");


    }
}
