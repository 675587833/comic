package com.bjpowernode.util;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class Aria2Socket extends WebSocketClient {

    public Aria2Socket() throws URISyntaxException {
        super(new URI("ws://localhost:6800/jsonrpc"));
    }

    public Aria2Socket(String url) throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake shake) {
        System.out.println("已经连接...");
    }

    @Override
    public void onMessage(String s) {
    }

    @Override
    public void onClose(int i, String s, boolean b) {
    }

    @Override
    public void onError(Exception e) {
    }


}