package com.bjpowernode.model;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Aria2 {

    private String id = UUID.randomUUID().toString().replaceAll("-", "");
    private String jsonrpc = "2.0";
    private String method = "aria2.addUri";
    private List<Object> params = new ArrayList<>();

    public Aria2 addParam(Object obj) {
        params.add(obj);
        return this;
    }
}
