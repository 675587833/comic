package com.bjpowernode.util;

import com.bjpowernode.model.Aria2;
import com.bjpowernode.model.Image;
import com.bjpowernode.model.Param;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;

public class Aria2Util {

    public static void post(List<String> jsonList) throws IOException, InterruptedException {
        HttpClient builder = HttpClient.newHttpClient();
        for (String aria2Json : jsonList) {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:6800/jsonrpc"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(aria2Json))
                    .build();
            HttpResponse<String> response = builder.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == SC_OK) {

            } else {
                System.out.println(response.body());
            }
        }
    }

    public static void send(List<String> jsonList) throws URISyntaxException {
        WebSocketClient client = new Aria2Socket();
        client.connect();
        while (!client.getReadyState().equals(ReadyState.OPEN)) {
            System.out.println("等待连接...");
        }
        for (String aria2Json : jsonList) {
            client.send(aria2Json);
        }

        client.close();
    }

    public static List<String> toJson(List<Image> images, String dir) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<String> jsonList = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            String url = image.getSrc();
            String extName = url.substring(url.lastIndexOf("."));
            String fileName = i + extName;
            Param param = new Param();
            param.setDir(dir).setOut(fileName).setReferer("*");
            Aria2 aria2 = new Aria2();
            aria2.addParam(List.of(url)).addParam(param);
            String aria2Json = om.writeValueAsString(aria2);
            jsonList.add(aria2Json);
        }

        return jsonList;
    }
}
